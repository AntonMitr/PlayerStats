package by.kotor.playerStats.hologram;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private Location location;
    private List<String> text = new ArrayList<>();
    private List<ArmorStand> lines = new ArrayList<>();

    public Hologram(Location location) {
        this.location = location;

        location.setY(location.getY() - 2);
        spawnAll();
    }

    public void spawnAll() {
        Location  currentLocation = location.clone();

        for (String line : text) {
            addArmorStandToList(currentLocation, line);

            currentLocation.setY(currentLocation.getY() - 0.3);
        }
    }

    public void addArmorStandToList(Location location, String text) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
        armorStand.setCustomNameVisible(true);

        lines.add(armorStand);
    }

    public void setText(List<String> newText) {
        Location  currentLocation = location.clone();

        for (int i = 0; i < newText.size(); i++) {
            if (i < lines.size()) {
                lines.get(i).setCustomName(ChatColor.translateAlternateColorCodes('&', newText.get(i)));
            } else {
                addArmorStandToList(currentLocation, newText.get(i));
            }

            currentLocation.setY(currentLocation.getY() - 0.3);
        }

        if (newText.size() < text.size()) {
            for (int i = newText.size(); i < text.size(); i++) {
                lines.get(i).remove();
            }
            lines = lines.subList(0, newText.size());
        }
        text = newText;
    }
}
