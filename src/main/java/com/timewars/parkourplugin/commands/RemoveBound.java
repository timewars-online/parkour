package com.timewars.parkourplugin.commands;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.timewars.parkourplugin.ParkourPlugin;
import com.timewars.parkourplugin.classes.Bound;
import com.timewars.parkourplugin.classes.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveBound implements TabExecutor {

    ParkourPlugin parkourPlugin;

    public RemoveBound(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if( sender instanceof Player)
        {
            Player p = (Player) sender;
            Parkour parkour = parkourPlugin.getParkours().get(parkourPlugin.getParkoursInEditing().get(p));
            ArrayList<String> boundNames = new ArrayList<>();
            for (Bound bound: parkour.getBounds())
            {
                boundNames.add(bound.getBoundName());
            }

            if ( args.length == 1 )
            {
                for ( int i = 0 ; i < boundNames.size() ; i++)
                {
                    if (boundNames.get(i).contains(args[0]))
                    {
                        list.add(boundNames.get(i));
                    }
                }
            }
        }

        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( sender instanceof Player)
        {
            Player p = (Player) sender;


            if (!parkourPlugin.getParkoursInEditing().containsKey(p))
            {
                p.sendMessage("Select a parkour to edit first! Use " + ChatColor.AQUA + "startedit" + "!");
                return true;
            }

            String regionName = args[0];


            boolean removed = parkourPlugin.getParkours().get(parkourPlugin.getParkoursInEditing().get(p)).removeBound(regionName);

            if (removed)
            {
                p.sendMessage("Successfully removed a region called" + ChatColor.AQUA + regionName + "!");
            }
            else p.sendMessage("There is no region called " + ChatColor.AQUA + regionName + "!");

            return true;
        }
        return false;
    }

}
