package de.winniepat.clearLag;

import de.winniepat.clearLag.commands.CommandManager;
import de.winniepat.clearLag.listeners.EntityListener;
import de.winniepat.clearLag.tasks.ClearLagTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class ClearLag extends JavaPlugin {
    private ConfigManager configManager;
    private ClearLagTask clearLagTask;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        configManager.setupConfig();

        this.clearLagTask = new ClearLagTask(this);
        clearLagTask.startScheduledTasks();

        new CommandManager(this).registerCommands();

        getServer().getPluginManager().registerEvents(new EntityListener(this), this);

        getLogger().info("ClearLagPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        if (clearLagTask != null) {
            clearLagTask.cancelTasks();
        }
        getLogger().info("ClearLagPlugin has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ClearLagTask getClearLagTask() {
        return clearLagTask;
    }
}