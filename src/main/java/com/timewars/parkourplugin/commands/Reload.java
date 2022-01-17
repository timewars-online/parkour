package com.timewars.parkourplugin.commands;

import com.timewars.parkourplugin.ParkourPlugin;
import com.timewars.parkourplugin.classes.Parkour;
import com.timewars.parkourplugin.files.CustomConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Reload implements TabExecutor {

    ParkourPlugin parkourPlugin;

    public Reload(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( sender instanceof Player)
        {
            Player p = (Player) sender;

            CustomConfig.reload();
            p.sendMessage("reloaded");
            return true;
        }
        return false;
    }

}
