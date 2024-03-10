package me.mocha.trustedchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfig implements CommandExecutor {

    private final TrustedChat plugin;

    public ReloadConfig(TrustedChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("reloadtrustedchat")) {
            if (!sender.hasPermission("trustedchat.reload")) {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }

            plugin.reloadConfiguration();
            sender.sendMessage("TrustedChat configuration reloaded successfully.");
            return true;
        }
        return false;
    }
}