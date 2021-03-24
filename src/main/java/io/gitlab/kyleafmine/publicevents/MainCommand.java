package io.gitlab.kyleafmine.publicevents;

import io.gitlab.kyleafmine.publicevents.menus.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command");
            return true;
        }
        if (!sender.hasPermission("publicevents.admin")) {
            sender.sendMessage("You do not have permission to run that command!");
            return false;
        }
        Player p = (Player) sender;
        new MainMenu(p);
        return true;
    }
}
