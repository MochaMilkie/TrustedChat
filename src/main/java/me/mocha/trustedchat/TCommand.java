package me.mocha.trustedchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TCommand implements CommandExecutor {

    private final TrustedChat plugin;

    public TCommand(TrustedChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player has permission to use the trusted chat command
        if (!player.hasPermission("trustedchat.trusted")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        // Check if there is a message provided with the command
        if (args.length == 0) {
            player.sendMessage("Usage: /t <message>");
            return true;
        }

        // Concatenate the message
        StringBuilder messageBuilder = new StringBuilder();
        for (String arg : args) {
            messageBuilder.append(arg).append(" ");
        }
        String message = messageBuilder.toString().trim();

        // Send the message to trusted chat
        plugin.sendToTrustedChat(player, message);

        return true;
    }
}