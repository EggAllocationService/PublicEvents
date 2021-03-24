package io.gitlab.kyleafmine.publicevents.menus;

import io.gitlab.kyleafmine.publicevents.PublicEvents;
import io.gitlab.kyleafmine.publicevents.config.GlobalConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsMenu implements Listener {
    Player p;
    Inventory i;

    int[] selected = new int[]{0, 0, 0};

    int selectedTextx = -1;
    int selectedTexty = -1;

    public SettingsMenu(Player bb) {
        p = bb;
        i = Bukkit.createInventory(null, 27, "Settings - PublicEvents");
        Bukkit.getPluginManager().registerEvents(this, PublicEvents.instance);
        p.openInventory(i);
        render();
    }
    public void render() {
        i.clear();
        i.setItem(11, iStackHelper(Material.DIAMOND_BLOCK, "&aFirst Place Prize", GlobalConfig.getConfig().firstPlaceCommands, selected[0])); // first place
        i.setItem(13, iStackHelper(Material.GOLD_BLOCK, "&aSecond Place Prize", GlobalConfig.getConfig().secondPlaceCommands, selected[1])); // second place
        i.setItem(15, iStackHelper(Material.IRON_BLOCK, "&aThird Place Prize", GlobalConfig.getConfig().thirdPlaceCommands, selected[2])); // third place
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getInventory() == this.i) {
            if (this.selectedTextx == -1) {
                // there is nothing selected, person pressed escape
                HandlerList.unregisterAll(this);
                try {
                    GlobalConfig.getConfig().save();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSaved config!"));
                } catch (IOException fe) {
                    fe.printStackTrace();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere was an error saving the config (check console)"));
                }
            }
        }
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if (e.getClickedInventory() != this.i) return;
        Bukkit.getLogger().info("click");
        Bukkit.getLogger().info(e.getClick().name() + " " + e.getSlot());
        e.setCancelled(true);
        /*if (e.getSlot() != 11 && e.getSlot() != 13 && e.getSlot() != 15) {
            return;
        }*/

        if (e.getSlot() == 11) {
            // first place editing
            if (e.getClick() == ClickType.LEFT) {
                // left click: add new to the end
                selectedTextx = 0;
                selectedTexty = -1;
                TextEntryManager.instance.requestText(p, this);
            } else if (e.getClick() == ClickType.RIGHT) {
                //  right click: replace selected
                selectedTextx = 0;
                selectedTexty = selected[0];
                TextEntryManager.instance.requestText(p, this);
            } else if (e.getClick() == ClickType.DROP) {
                // drop: delete selected
                GlobalConfig.getConfig().firstPlaceCommands.remove(selected[0]);

            } else if (e.getClick() == ClickType.NUMBER_KEY) {
                // big hours boys
                selected[0] = e.getHotbarButton();
            }
        } else if (e.getSlot() == 13) {
            // first place editing
            if (e.getClick() == ClickType.LEFT) {
                // left click: add new to the end
                selectedTextx = 1;
                selectedTexty = -1;
                TextEntryManager.instance.requestText(p, this);
            } else if (e.getClick() == ClickType.RIGHT) {
                //  right click: replace selected
                selectedTextx = 1;
                selectedTexty = selected[1];
                TextEntryManager.instance.requestText(p, this);
            } else if (e.getClick() == ClickType.DROP) {
                // drop: delete selected
                GlobalConfig.getConfig().secondPlaceCommands.remove(selected[1]);

            } else if (e.getClick() == ClickType.NUMBER_KEY) {
                // big hours boys
                selected[1] = e.getHotbarButton();
            }
        }else if (e.getSlot() == 15) {
            // first place editing
            if (e.getClick() == ClickType.LEFT) {
                // left click: add new to the end
                selectedTextx = 2;
                selectedTexty = -1;
                TextEntryManager.instance.requestText(p, this);
            } else if (e.getClick() == ClickType.RIGHT) {
                //  right click: replace selected
                selectedTextx = 2;
                selectedTexty = selected[2];
                TextEntryManager.instance.requestText(p, this);
            } else if (e.getClick() == ClickType.DROP) {
                // drop: delete selected
                GlobalConfig.getConfig().thirdPlaceCommands.remove(selected[2]);


            } else if (e.getClick() == ClickType.NUMBER_KEY) {
                // big hours boys
                selected[2] = e.getHotbarButton();
            }
        }
        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        render();
    }

    public void callback(String s) {
        // restore previous state
        if (selectedTextx == 0) {
            if (selectedTexty == -1) {
                // add to end
                GlobalConfig.getConfig().firstPlaceCommands.add(s);
            }  else {
                GlobalConfig.getConfig().firstPlaceCommands.set(selectedTexty, s);
            }
        }
        if (selectedTextx == 1) {
            if (selectedTexty == -1) {
                // add to end
                GlobalConfig.getConfig().secondPlaceCommands.add(s);
            }  else {
                GlobalConfig.getConfig().secondPlaceCommands.set(selectedTexty, s);
            }
        }
        if (selectedTextx == 2) {
            if (selectedTexty == -1) {
                // add to end
                GlobalConfig.getConfig().thirdPlaceCommands.add(s);
            }  else {
                GlobalConfig.getConfig().thirdPlaceCommands.set(selectedTexty, s);
            }
        }
        try {
            GlobalConfig.getConfig().save();
        } catch (IOException e) {
            e.printStackTrace();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere was an error saving the config (check console)"));
        }
        selectedTextx = -1;
        selectedTexty = -1;
        p.openInventory(i);
        render();
    }

    public void cancelCallback() {
        selectedTextx = -1;
        selectedTexty = -1;
        p.openInventory(i);
        render();
    }

    public ItemStack iStackHelper(Material m, String s, ArrayList<String> options, int selected) {
        ItemStack b = new ItemStack(m, 1);
        ItemMeta bm = b.getItemMeta();
        bm.setDisplayName(ChatColor.translateAlternateColorCodes('&', s));
        Bukkit.getLogger().info("istackhelper with options of " + options.size());

            ArrayList<String> lore = new ArrayList<>();
            int index = 0;
            for (String c : options) {
                if (index == selected) {
                    // selected one
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&a&l[&b>&a&l] &r" + c));
                } else {
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&8[>] " + c));
                }
                index++;
            }
            lore.add(ChatColor.translateAlternateColorCodes('&', "&aLeft-Click to add command"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&dRight-Click to overwrite selected command"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&4Drop to delete"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b[0-9] on your keyboard to select command"));
            bm.setLore(lore);
        b.setItemMeta(bm);
        return b;
    }

}
