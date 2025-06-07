package by.kotor.playerStats.command;

import by.kotor.playerStats.hologram.Hologram;
import by.kotor.playerStats.manager.HologramManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HologramCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        Hologram hologram = new Hologram(player.getLocation());

        HologramManager.startStatUpdate(hologram);
        return true;
    }

}
