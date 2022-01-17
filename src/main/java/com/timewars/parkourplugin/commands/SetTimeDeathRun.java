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

public class SetTimeDeathRun implements TabExecutor {

    ParkourPlugin parkourPlugin;

    public SetTimeDeathRun(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if ( args.length == 1 ) list.add("<time>");
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



            Parkour parkour = parkourPlugin.getParkours().get(parkourPlugin.getParkoursInEditing().get(p));
            parkour.setTimeDeathRun(Float.valueOf(args[0]));
            p.sendMessage("The DeathRun time of " +  ChatColor.AQUA + parkour.getName() + " now is " + ChatColor.BOLD + parkour.getTimeDeathRun() );

            return true;
        }
        return false;
    }
}
