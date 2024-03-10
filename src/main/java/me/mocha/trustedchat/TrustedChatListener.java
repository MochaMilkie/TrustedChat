package me.mocha.trustedchat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TrustedChatListener implements Listener {

    private final TrustedChat plugin;

    public TrustedChatListener(TrustedChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (plugin.isToggleEnabled(event.getPlayer())) {
            plugin.sendToTrustedChat(event.getPlayer(), event.getMessage());
            event.setCancelled(true); // Cancel the original chat message
        }
    }
}