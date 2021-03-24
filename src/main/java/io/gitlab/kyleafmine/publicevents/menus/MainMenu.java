package io.gitlab.kyleafmine.publicevents.menus;

import io.gitlab.kyleafmine.publicevents.PublicEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu implements Listener {
    Player p;
    Inventory i;

    public MainMenu(Player bp ) {
        p = bp;
        i = Bukkit.createInventory(null, 27, "Main Menu - PublicEvents");
        Bukkit.getPluginManager().registerEvents(this, PublicEvents.instance);
        p.openInventory(i);
        render();
    }

    public void render() {
        i.clear();
        ItemStack settings = new ItemStack(Material.COMPARATOR);
        ItemMeta h = settings.getItemMeta();
        h.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r&aSettings"));
        settings.setItemMeta(h);
        i.setItem(11, settings);
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() != this.i) {
            return;
        }
        e.setCancelled(true);
        if (e.getSlot() == 11) {
            // clicked settings
            p.closeInventory();
            new SettingsMenu(p);
        }
    }
    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getInventory() == this.i) {
            HandlerList.unregisterAll(this);
        }

    }
}
