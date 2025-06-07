package by.kotor.playerStats.manager;

import by.kotor.playerStats.PlayerStats;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getServer;

public class DatabaseManager {
    private final Logger log = Bukkit.getLogger();
    private Connection connection;
    private static DatabaseManager instance;


    private DatabaseManager() {
        try {
            File dbFile = new File(PlayerStats.getInstance().getDataFolder(), "player-stats.db");
            PlayerStats.getInstance().getDataFolder().mkdirs();
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                create table if not exists player_kills (
                    uuid text primary key,
                    player_name text not null,
                    kills integer
                )""");

        } catch (SQLException e) {
            log.severe("Failed to connect to the database:" + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(PlayerStats.getInstance());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.severe("Failed to close the database:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addKillAsync(UUID uuid, String name) {
        Bukkit.getScheduler().runTaskAsynchronously(PlayerStats.getInstance(), () -> {
            String SQL_ADD_KILL = """
            insert into player_kills (uuid, player_name, kills)
            values (?,?,1)
            on conflict(uuid) do update set kills = kills + 1, player_name = ?""";

            try (PreparedStatement stmt = connection.prepareStatement(SQL_ADD_KILL)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, name);
                stmt.setString(3, name);
                stmt.executeUpdate();
            } catch (SQLException e) {
                log.severe("Failed to add kill to the database:" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void getKillsAsync(UUID uuid, Consumer<Integer> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(PlayerStats.getInstance(), () -> {
            String SQL_GET_KILLS = "select kills from player_kills where uuid = ?";
            int kills = 0;

            try (PreparedStatement stmt = connection.prepareStatement(SQL_GET_KILLS)) {
                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    kills = rs.getInt("kills");
                }
            } catch (SQLException e) {
                log.severe("Failed to get kills from the database:" + e.getMessage());
                e.printStackTrace();
            }

            int finalKills = kills;

            Bukkit.getScheduler().runTask(PlayerStats.getInstance(), () -> {
                callback.accept(finalKills);
            });
        });
    }

    public void getTopFiveKillsAsync(Consumer<Map<String, Integer>> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(PlayerStats.getInstance(), () -> {
            String SQL_GET_TOP_FIVE_KILLS = "select player_name, kills from player_kills order by kills desc limit 5";

            Map<String, Integer> topFiveKills = new LinkedHashMap<>();
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(SQL_GET_TOP_FIVE_KILLS);
                while (rs.next()) {
                    topFiveKills.put(rs.getString("player_name"), rs.getInt("kills"));
                }
            } catch (SQLException e) {
                log.severe("Failed to get top five player_kills from the database:" + e.getMessage());
                e.printStackTrace();
            }

            Bukkit.getScheduler().runTask(PlayerStats.getInstance(), () -> {
                callback.accept(topFiveKills);
            });
        });
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        } return instance;
    }
}
