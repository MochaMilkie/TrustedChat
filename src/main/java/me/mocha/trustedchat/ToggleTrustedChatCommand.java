package me.mocha.trustedchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleTrustedChatCommand implements CommandExecutor {

    private TrustedChat plugin;

    public ToggleTrustedChatCommand(TrustedChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has permission to execute the command
        if (!player.hasPermission("trustedchat.trusted")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        // Execute command logic
        plugin.toggleTrustedChat(player);
        return true;
    }
}