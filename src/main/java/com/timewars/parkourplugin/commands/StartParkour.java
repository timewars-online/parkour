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

public class StartParkour implements TabExecutor {

    ParkourPlugin parkourPlugin;
    public StartParkour(ParkourPlugin parkourPlugin) {
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
        if(sender instanceof Player)
        {
            Player p = (Player) sender;

            String parkourName = args[0];
            if ( !parkourPlugin.getParkours().containsKey(parkourName) )
            {
                p.sendMessage("There is no parkour called " + ChatColor.DARK_RED + parkourName + ". Select existing parkour!");
                return true;
            }
            Parkour parkour = parkourPlugin.getParkours().get(parkourName);
            if(parkour.isEnabled()) parkourPlugin.startNewSession(p, parkour);

            p.sendMessage(ChatColor.DARK_RED + "Parkour started!");
            return true;
        }
        return false;
    }

}
