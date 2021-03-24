package io.gitlab.kyleafmine.publicevents.menus;

import io.gitlab.kyleafmine.publicevents.PublicEvents;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class TextEntryManager implements Listener {
    public static TextEntryManager instance;
    public static TextEntryManager init() {
        instance = new TextEntryManager();
        return instance;
    }
    HashMap<Player,SettingsMenu> players = new HashMap<>();

    public void requestText(Player p, SettingsMenu s) {
        players.put(p, s);
        p.closeInventory();
        p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE,  1, 1);
        p.sendTitle("Type a command in chat", "Without / in front");
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("All PlaceholderAPI placeholders are supported"));

    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void chat(AsyncPlayerChatEvent e) {
        if (players.containsKey( e.getPlayer())) {
            e.setCancelled( false);
            Bukkit.getScheduler().scheduleSyncDelayedTask(PublicEvents.instance, new Runnable() {
                @Override
                public void run() {
                    if (e.getMessage().equalsIgnoreCase("cancel")) {
                        players.get(e.getPlayer()).cancelCallback();
                        players.remove(e.getPlayer());
                    } else {
                        players.get(e.getPlayer()).callback(e.getMessage());
                        players.remove(e.getPlayer());
                    }
                }
            });
        }
    }
}
