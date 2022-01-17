package com.timewars.parkourplugin.commands;

import com.timewars.parkourplugin.ParkourPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetType implements TabExecutor {

    ParkourPlugin parkourPlugin;
    public SetType(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }
    public ArrayList<String> types = new ArrayList<>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        types.add("deathrun");
        types.add("regular");

        if ( args.length == 1 )
        {
            for ( int i = 0 ; i < types.size() ; i++)
            {
                if (types.get(i).contains(args[0]))
                {
                    list.add(types.get(i));
                }
            }
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            if (!parkourPlugin.getParkoursInEditing().containsKey(p))
            {
                p.sendMessage("Select a parkour to edit first! Use " + ChatColor.AQUA + "startedit" + "!");
                return true;
            }

            String parkourName = parkourPlugin.getParkoursInEditing().get(p);
            String type = args[0];

            if ( types.contains(type) )
            {
                parkourPlugin.getParkours().get(parkourName).setType(type);
                p.sendMessage("Parkour type was set to " + ChatColor.AQUA + type);
            }
            else p.sendMessage(ChatColor.AQUA + type + " is not a valid type!");


            return true;
        }
        return false;
    }
}
