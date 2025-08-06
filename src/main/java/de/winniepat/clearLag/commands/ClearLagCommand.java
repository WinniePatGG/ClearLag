package de.winniepat.clearLag.commands;

import de.winniepat.clearLag.ClearLag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ClearLagCommand implements CommandExecutor, TabCompleter {
    private final ClearLag plugin;

    public ClearLagCommand(ClearLag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§aClearLag Plugin Commands:");
            sender.sendMessage("§b/clearlag now §f- Clear entities now");
            sender.sendMessage("§b/clearlag reload §f- Reload the config");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "now":
                int cleared = plugin.getClearLagTask().clearEntities();
                return true;
            case "reload":
                plugin.getConfigManager().reloadConfig();
                plugin.getClearLagTask().restartTasks();
                sender.sendMessage("§aConfig reloaded!");
                return true;
            default:
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("now");
            completions.add("reload");
        }
        return completions;
    }
}