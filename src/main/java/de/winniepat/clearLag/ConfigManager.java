package de.winniepat.clearLag;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setupConfig() {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();

        config.addDefault("clear-interval", 300);
        config.addDefault("auto-clear-enabled", true);
        config.addDefault("max-entities-per-chunk", 25);
        config.addDefault("prevent-spawns-when-full", true);
        config.addDefault("clear-items", true);
        config.addDefault("clear-mobs", true);
        config.addDefault("clear-xp", true);
        config.addDefault("clear-projectiles", true);
        config.addDefault("clear-vehicles", true);
        config.addDefault("item-whitelist", List.of("DIAMOND", "NETHERITE_INGOT"));
        config.addDefault("item-blacklist", List.of("ROTTEN_FLESH", "COBBLESTONE"));
        config.addDefault("item-min-age", 120);
        config.addDefault("warning-message", "&cWarning: Clearing laggy entities in 10 seconds!");
        config.addDefault("cleared-message", "&aCleared &e%count% &aentities to reduce lag!");
        config.addDefault("spawn-prevent-message", "&cToo many entities in this chunk! Cannot spawn more.");
        config.addDefault("debug", false);
        config.addDefault("log-cleared-entities", false);

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public int getClearInterval() {
        return config.getInt("clear-interval");
    }

    public boolean isAutoClearEnabled() {
        return config.getBoolean("auto-clear-enabled");
    }

    public boolean shouldPreventSpawnsWhenFull() {
        return config.getBoolean("prevent-spawns-when-full");
    }

    public boolean shouldLogClearedEntities() {
        return config.getBoolean("log-cleared-entities");
    }
}