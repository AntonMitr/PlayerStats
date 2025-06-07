package by.kotor.playerStats.manager;

import by.kotor.playerStats.PlayerStats;
import by.kotor.playerStats.hologram.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HologramManager {

    public static void startStatUpdate(Hologram hologram) {
        new BukkitRunnable() {
            public void run() {
                DatabaseManager.getInstance().getTopFiveKillsAsync(top -> {
                    List<String> resultLines = new ArrayList<>();
                    resultLines.add("&eTop 5 players in killing mobs");
                    int rank = 1;
                    for (Map.Entry<String, Integer> entry : top.entrySet()) {
                        resultLines.add(ChatColor.translateAlternateColorCodes('&', "&e" + rank++ + ". " +  entry.getKey() + " - " + entry.getValue()));
                    }
                    hologram.setText(resultLines);
                });
            }
        }.runTaskTimer(PlayerStats.getInstance(), 0L, 1200L);
    }
}
