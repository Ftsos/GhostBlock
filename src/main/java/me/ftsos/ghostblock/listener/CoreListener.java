package me.ftsos.ghostblock.listener;

import me.ftsos.ghostblock.GhostBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class CoreListener implements Listener {
    private GhostBlock plugin;

    public CoreListener(GhostBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        plugin.getGhostBlockManager().clearBlocks(event.getPlayer().getUniqueId());
    }
}
