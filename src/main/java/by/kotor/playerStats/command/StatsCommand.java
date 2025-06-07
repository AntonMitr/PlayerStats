package by.kotor.playerStats.command;

import by.kotor.playerStats.manager.DatabaseManager;
import by.kotor.playerStats.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            databaseManager.getKillsAsync(player.getUniqueId(), kills -> {
                        ChatUtil.sendMessage(player,
                                "&aMobs killed: " + kills);
                    });
            return true;
        }
        ChatUtil.sendMessage(sender,
                "&cYou must be a player to use this command!");
        return true;
    }

}
