package me.mocha.trustedchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TrustedChat extends JavaPlugin {
    private static final String TRUSTED_CHAT_TOGGLE_KEY = "trusted_chat_toggle";
    private Set<String> trustedPlayers = new HashSet<>();
    private FileConfiguration config;
    private String prefix;


    @Override
    public void onEnable() {
        getLogger().info("TrustedChat by Mocha (mochamilkrl)");
        getLogger().info("TrustedChat is starting...");
        getLogger().info("Loading config.");

        // Load configuration
        config = getConfig();

        // Set defaults (after loading the configuration) **DUH**
        config.addDefault("prefix", "[CHANGE ME IN CONFIG]");
        config.addDefault("trusted_players", new ArrayList<>());
        config.options().copyDefaults(true);
        saveConfig();

        // Load trusted players from configuration
        if (config.contains("trusted_players")) {
            trustedPlayers.addAll(config.getStringList("trusted_players"));
        }

        // Register the commands
        getCommand("t").setExecutor(new TCommand(this));
        getCommand("tc").setExecutor(new ToggleTrustedChatCommand(this));
        getCommand("reloadtrustedchat").setExecutor(new ReloadConfig(this));
        getCommand("listtrustedplayers").setExecutor(new ListTrustedPlayersCommand(this));
        // Register events
        getServer().getPluginManager().registerEvents(new TrustedChatListener(this), this);
        //Listener trustedChatJoinListener = (new ListTrustedPlayersCommand(this);
        //getServer().getPluginManager().registerEvents(trustedChatJoinListener, this);
        //getServer().getPluginManager().registerEvents((Listener) new ListTrustedPlayersCommand(this), this);
        getLogger().info("TrustedChat has loaded successfully.");
    }

    public void reloadConfiguration() {
        reloadConfig();
        config = getConfig();

        // Reload chat prefix
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"));

        // Re-populate trustedPlayers set after reloading configuration
        trustedPlayers.clear();
        if (config.contains("trusted_players")) {
            trustedPlayers.addAll(config.getStringList("trusted_players"));
        }
    }

    // Method to add a player to the trusted list
    public void addTrustedPlayer(String playerName) {
        trustedPlayers.add(playerName);
        saveConfig();
    }

    // Method to remove a player from the trusted list
    public void removeTrustedPlayer(String playerName) {
        trustedPlayers.remove(playerName);
        saveConfig();
    }

    // Method to get the list of trusted players
    public Set<String> getTrustedPlayers() {
        return trustedPlayers;
    }

    public void sendToTrustedChat(Player player, String message) {
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"));
        // Iterate through all online players to check for trusted chat permission
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("trustedchat.trusted")) {
                onlinePlayer.sendMessage(prefix + " " + player.getName() + ": " + message);
                getLogger().info( player.getName() + ": " + message + "[Trusted Chat]"); // Send message to console
            }
        }
    }

    public void toggleTrustedChat(Player player) {
        if (player.hasPermission("trustedchat.trusted")) {
            boolean currentState = isToggleEnabled(player);
            player.setMetadata(TRUSTED_CHAT_TOGGLE_KEY, new FixedMetadataValue(this, !currentState));

            // Provide feedback to the player
            if (!currentState) {
                player.sendMessage("Trusted chat message sending is now enabled.");
            } else {
                player.sendMessage("Trusted chat message sending is now disabled.");
            }
        } else {
            player.sendMessage("You don't have permission to toggle trusted chat.");
        }
    }

    public boolean isToggleEnabled(Player player) {
        if (player.hasMetadata(TRUSTED_CHAT_TOGGLE_KEY)) {
            for (MetadataValue value : player.getMetadata(TRUSTED_CHAT_TOGGLE_KEY)) {
                if (value.getOwningPlugin().equals(this)) {
                    return value.asBoolean();
                }
            }
        }
        return false; // Default value if metadata not found
    }

    @Override
    public void onDisable() {
        // Save trusted players to configuration
        config.set("trusted_players", new ArrayList<>(trustedPlayers));
        saveConfig();
    }
}
