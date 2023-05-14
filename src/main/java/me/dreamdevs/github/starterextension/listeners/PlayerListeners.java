package me.dreamdevs.github.starterextension.listeners;

import me.dreamdevs.github.starterextension.StarterExtensionMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void joinEvent(final PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()) {
            StarterExtensionMain.getInstance().getStarterManager().loadItems(event.getPlayer());
        }
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        event.getDrops().removeIf(itemStack -> StarterExtensionMain.getInstance().getStarterManager().isStarterItem(itemStack));
    }

    @EventHandler
    public void respawnEvent(PlayerRespawnEvent event) {
        StarterExtensionMain.getInstance().getStarterManager().loadItems(event.getPlayer());
    }

}