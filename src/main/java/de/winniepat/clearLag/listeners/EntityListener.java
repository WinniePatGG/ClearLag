package de.winniepat.clearLag.listeners;

import de.winniepat.clearLag.ClearLag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityListener implements Listener {
    private final ClearLag plugin;

    public EntityListener(ClearLag plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!plugin.getConfigManager().shouldPreventSpawnsWhenFull()) {
            return;
        }

        if (shouldPreventSpawn(event.getEntity())) {
            event.setCancelled(true);
            String message = plugin.getConfigManager().getConfig().getString("spawn-prevent-message");
            if (message != null && event.getEntity() instanceof Player player) {
                player.sendMessage(message.replace('&', '§'));
            }
        }
    }

    private boolean shouldPreventSpawn(Entity entity) {
        int maxEntities = plugin.getConfigManager().getConfig().getInt("max-entities-per-chunk", 25);
        return entity.getLocation().getChunk().getEntities().length >= maxEntities;
    }
}