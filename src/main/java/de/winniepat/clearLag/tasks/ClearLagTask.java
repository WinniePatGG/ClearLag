package de.winniepat.clearLag.tasks;

import de.winniepat.clearLag.ClearLag;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;
import java.util.List;
import java.util.stream.Collectors;

public class ClearLagTask {
    private final ClearLag plugin;
    private BukkitTask clearTask;
    private BukkitTask warningTask;

    public ClearLagTask(ClearLag plugin) {
        this.plugin = plugin;
    }

    public void startScheduledTasks() {
        cancelTasks();

        if (!plugin.getConfigManager().getConfig().getBoolean("auto-clear-enabled", true)) {
            if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info("Auto-clear is disabled in config");
            }
            return;
        }

        int interval = plugin.getConfigManager().getConfig().getInt("clear-interval", 300) * 20;
        clearTask = Bukkit.getScheduler().runTaskTimer(plugin, this::startClearWarning, interval, interval);

        if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("[DEBUG] Scheduled clear task started with interval: " + interval + " ticks");
        }
    }

    private void startClearWarning() {
        String warningMessage = plugin.getConfigManager().getConfig().getString("warning-message");
        if (warningMessage != null && !warningMessage.isEmpty()) {
            Bukkit.broadcastMessage(warningMessage.replace('&', '§'));
        }

        warningTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            int cleared = clearEntities();
            sendClearedMessage(cleared);
        }, 200);
    }

    public int clearEntities() {
        if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Starting entity clearance...");
        }

        List<String> whitelist = plugin.getConfigManager().getConfig().getStringList("item-whitelist");
        List<String> blacklist = plugin.getConfigManager().getConfig().getStringList("item-blacklist");
        int minAge = plugin.getConfigManager().getConfig().getInt("item-min-age", 0) * 20;
        long currentTick = Bukkit.getCurrentTick();

        List<Entity> entitiesToClear = Bukkit.getWorlds().stream()
                .flatMap(world -> world.getEntities().stream())
                .filter(entity -> shouldClear(entity, whitelist, blacklist, minAge, currentTick))
                .collect(Collectors.toList());

        entitiesToClear.forEach(Entity::remove);

        if (plugin.getConfigManager().getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info("Cleared " + entitiesToClear.size() + " entities");
        }

        return entitiesToClear.size();
    }

    private boolean shouldClear(Entity entity, List<String> whitelist, List<String> blacklist, int minAge, long currentTick) {
        if (entity == null) return false;

        if (entity instanceof Player player && player.hasPermission("clearlag.bypass")) {
            return false;
        }

        if (minAge > 0 && entity instanceof Item item) {
            if ((currentTick - item.getTicksLived()) < minAge) {
                return false;
            }
        }

        if (entity instanceof Item item) {
            String material = item.getItemStack().getType().toString();
            if (whitelist.contains(material)) {
                return false;
            }
            if (blacklist.contains(material)) {
                return true;
            }
        }

        return switch (entity.getType()) {
            case ITEM -> plugin.getConfigManager().getConfig().getBoolean("clear-items", true);
            case EXPERIENCE_ORB -> plugin.getConfigManager().getConfig().getBoolean("clear-xp", true);
            case ARROW, SPECTRAL_ARROW, SNOWBALL, EGG, ENDER_PEARL, WITHER_SKULL, FIREBALL, DRAGON_FIREBALL, SHULKER_BULLET, LLAMA_SPIT ->
                    plugin.getConfigManager().getConfig().getBoolean("clear-projectiles", true);
            case MINECART, CHEST_MINECART, FURNACE_MINECART, TNT_MINECART, HOPPER_MINECART, COMMAND_BLOCK_MINECART ->
                    plugin.getConfigManager().getConfig().getBoolean("clear-vehicles", false);
            default -> plugin.getConfigManager().getConfig().getBoolean("clear-mobs", false) && isMob(entity.getType());
        };
    }

    private void sendClearedMessage(int count) {
        String message = plugin.getConfigManager().getConfig().getString("cleared-message");
        if (message != null && !message.isEmpty()) {
            Bukkit.broadcastMessage(message
                    .replace("%count%", String.valueOf(count))
                    .replace('&', '§'));
        }
    }

    private boolean isMob(EntityType type) {
        return switch (type) {
            case ITEM, EXPERIENCE_ORB, AREA_EFFECT_CLOUD, PAINTING, ITEM_FRAME,
                 GLOW_ITEM_FRAME, END_CRYSTAL, EVOKER_FANGS, LEASH_KNOT, LIGHTNING_BOLT,
                 PLAYER, ARMOR_STAND -> false;
            default -> type.isAlive();
        };
    }

    public void cancelTasks() {
        if (clearTask != null) {
            clearTask.cancel();
            clearTask = null;
        }
        if (warningTask != null) {
            warningTask.cancel();
            warningTask = null;
        }
    }

    public void restartTasks() {
        cancelTasks();
        startScheduledTasks();
    }
}