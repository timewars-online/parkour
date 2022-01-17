package com.timewars.parkourplugin.commands;

import com.timewars.parkourplugin.ParkourPlugin;
import com.timewars.parkourplugin.classes.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CreateParkour implements TabExecutor {

    ParkourPlugin parkourPlugin;
    public CreateParkour(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if ( args.length == 1 ) list.add("<parkour-name>");
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if ( sender instanceof Player)
        {
            Player p = (Player) sender;

            String parkourName = args[0];
            if (parkourPlugin.getParkoursInEditing().containsKey(p))
            {
                p.sendMessage("Finish editing " + ChatColor.DARK_RED + parkourPlugin.getParkoursInEditing().get(p) +
                        " first! Use " + ChatColor.AQUA + "endedit" + "!");
                return true;
            }

            if ( parkourPlugin.getParkours().containsKey(parkourName) )
            {
                p.sendMessage(ChatColor.DARK_RED + parkourName + " is already in USE! Please " + ChatColor.AQUA + "change" + " the parkour name!");
                return true;
            }
            parkourPlugin.getParkours().put(parkourName, new Parkour(parkourName));
            Parkour parkour = parkourPlugin.getParkours().get(parkourName);
            parkour.setEnabled(false);
            parkour.setStatusEditing();
            parkourPlugin.getParkoursInEditing().put(p, parkourName);
            p.sendMessage(ChatColor.DARK_RED + parkourName + " was just created and now in edit mode!");
            return true;
        }
        return false;
    }

}
