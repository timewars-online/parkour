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

public class SetSpawnpoint implements TabExecutor {

    ParkourPlugin parkourPlugin;
    public SetSpawnpoint(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
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
            Location spawnPoint = p.getLocation();
            parkourPlugin.getParkours().get(parkourName).setStart(spawnPoint);
            p.sendMessage("Spawnpoint was successfully added at " + ChatColor.AQUA + spawnPoint.getX() + " " + spawnPoint.getY() +
                    " " + spawnPoint.getZ());
            return true;
        }
        return false;
    }
}
