package by.kotor.playerStats.event;

import by.kotor.playerStats.manager.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillEvent implements Listener {
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null ) {
            Player killer = event.getEntity().getKiller();
            databaseManager.addKillAsync(killer.getUniqueId(), killer.getName());
        }
    }

}
