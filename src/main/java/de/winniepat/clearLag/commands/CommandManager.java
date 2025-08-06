package de.winniepat.clearLag.commands;

import de.winniepat.clearLag.ClearLag;
import org.bukkit.command.PluginCommand;

public class CommandManager {
    private final ClearLag plugin;

    public CommandManager(ClearLag plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        PluginCommand clearLagCommand = plugin.getCommand("clearlag");
        if (clearLagCommand != null) {
            clearLagCommand.setExecutor(new ClearLagCommand(plugin));
            clearLagCommand.setTabCompleter(new ClearLagCommand(plugin));
        }
    }
}