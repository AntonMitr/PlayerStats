package by.kotor.playerStats.command;

import by.kotor.playerStats.manager.DatabaseManager;
import by.kotor.playerStats.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class TopKillsCommand implements CommandExecutor {
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendMessage(sender, "&cYou must be a player to use this command!");
            return true;
        }

        databaseManager.getTopFiveKillsAsync(top -> {
            ChatUtil.sendMessage(player, "&6Top 5 kills:");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : top.entrySet()) {
                ChatUtil.sendMessage(player,
                        "&e" + rank++ + ". " + entry.getKey() + " - " + entry.getValue());
            }
        });

        return true;
    }
}
