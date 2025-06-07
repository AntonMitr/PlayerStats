package by.kotor.playerStats;

import by.kotor.playerStats.command.HologramCommand;
import by.kotor.playerStats.command.StatsCommand;
import by.kotor.playerStats.command.TopKillsCommand;
import by.kotor.playerStats.event.KillEvent;
import by.kotor.playerStats.manager.DatabaseManager;
import by.kotor.playerStats.manager.HologramManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerStats extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new KillEvent(),this);

        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("topkills").setExecutor(new TopKillsCommand());
        getCommand("hologram-top-five-kill-mobs").setExecutor(new HologramCommand());
        getLogger().info("PlayerStats enabled");

    }

    @Override
    public void onDisable() {
        DatabaseManager.getInstance().close();
        getLogger().info("PlayerStats disabled");
    }

    public static PlayerStats getInstance() {
        return JavaPlugin.getPlugin(PlayerStats.class);
    }
}
