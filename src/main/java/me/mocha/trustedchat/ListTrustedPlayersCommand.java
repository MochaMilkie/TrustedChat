package me.mocha.trustedchat;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ListTrustedPlayersCommand implements CommandExecutor {

    private final TrustedChat plugin;

    public ListTrustedPlayersCommand(TrustedChat plugin) {
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
        if (!player.hasPermission("trustedchat.listall")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        // Execute command logic
        List<String> trustedPlayers = new ArrayList<>();

        // Check online players for trusted permissions
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("trustedchat.trusted")) {
                trustedPlayers.add(onlinePlayer.getName());
            }
        }

        // Check offline players for trusted permissions
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.isOnline() && offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer().hasPermission("trustedchat.trusted")) {
                trustedPlayers.add(offlinePlayer.getName());
            }
        }

        // Check newly joined players for trusted permissions
        for (String joinedPlayer : plugin.getTrustedPlayers()) {
            Player onlinePlayer = Bukkit.getPlayerExact(joinedPlayer);
            if (onlinePlayer != null && onlinePlayer.isOnline() && onlinePlayer.hasPermission("trustedchat.trusted")) {
                trustedPlayers.add(onlinePlayer.getName());
            }
        }

        // Send list of trusted players to the player
        if (trustedPlayers.isEmpty()) {
            player.sendMessage("No players have the trustedchat.trusted permission.");
        } else {
            player.sendMessage("Trusted players:");
            for (String trustedPlayer : trustedPlayers) {
                player.sendMessage("- " + trustedPlayer);
            }
        }

        return true;
    }
}
