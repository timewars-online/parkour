package com.timewars.parkourplugin.commands;

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

public class Edit implements TabExecutor {

    ParkourPlugin parkourPlugin;
    public Edit(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if( sender instanceof Player)
        {
            Player p = (Player) sender;

            ArrayList<String> parkourNames = new ArrayList<>();
            for (String parkourName: parkourPlugin.getParkours().keySet())
            {
                parkourNames.add(parkourName);
            }

            if ( args.length == 1 )
            {
                for ( int i = 0 ; i < parkourNames.size() ; i++)
                {
                    if (parkourNames.get(i).contains(args[0]))
                    {
                        list.add(parkourNames.get(i));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player)
        {
            Player p = (Player) sender;

            String parkourName = args[0];
            Parkour parkour = parkourPlugin.getParkours().get(parkourName);

            if ( !parkourPlugin.getParkours().containsKey(parkourName) )
            {
                p.sendMessage("There is no parkour called " + ChatColor.DARK_RED + parkourName + ". Select existing parkour!");
                return true;
            }

            if ( parkourPlugin.getParkoursInEditing().containsKey(p) && parkourPlugin.getParkoursInEditing().get(p).equals(parkourName) )
            {
                parkour.setEnabled(true);
                parkourPlugin.getParkoursInEditing().remove(p);
                p.sendMessage(ChatColor.DARK_RED + parkourName + " is now enabled and playable!" );

                parkour.setStatusSaving();
                parkourPlugin.saveParkours();
                parkour.setStatusEnabled();
                p.sendMessage("Saved?");
                return true;
            }

            if ( parkourPlugin.getParkoursInEditing().containsKey(p)  )
            {
                p.sendMessage("You are already editing " + ChatColor.DARK_RED + parkourPlugin.getParkoursInEditing().get(p) +
                        ChatColor.AQUA + ". Stop editing your current parkour first!");
                return true;
            }

            if ( !parkour.isEnabled() )
            {
                p.sendMessage("Someone else is already editing this parkour");
                return true;
            }

            parkour.setStatusEditing();
            parkour.setEnabled(false);

            parkourPlugin.getParkoursInEditing().put(p,parkourName);
            p.sendMessage(ChatColor.DARK_RED + parkourName + " is now set to disabled and can be edited!");
            return true;
        }
        return false;
    }

}
