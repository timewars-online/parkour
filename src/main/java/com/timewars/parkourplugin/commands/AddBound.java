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

public class AddBound implements TabExecutor {

    ParkourPlugin parkourPlugin;

    public AddBound(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if ( args.length == 1 ) list.add("<region-name>");

        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( sender instanceof Player)
        {
            Player p = (Player) sender;
            if (!parkourPlugin.getParkoursInEditing().containsKey(p))
            {
                p.sendMessage("Select a parkour to edit first! Use " + ChatColor.AQUA + "starteditind" + "!");
                return true;
            }

            Parkour parkour = parkourPlugin.getParkours().get(parkourPlugin.getParkoursInEditing().get(p));
            String regionName = args[0];

            for (Bound bound: parkour.getBounds())
            {
                if ( bound.getBoundName().equals(regionName) )
                {
                    p.sendMessage("There is already a parkour called " + ChatColor.AQUA + regionName + ChatColor.WHITE + "! Select another name!" );
                    return true;
                }
            }

            String parkourName = parkourPlugin.getParkoursInEditing().get(p);

            World world = new BukkitWorld(p.getWorld());
            Region region = null;
            try {
                region = parkourPlugin.getWorldEditPlugin().getSession(p).getSelection(parkourPlugin.getWorldEditPlugin().getSession(p).getSelectionWorld());
                Bound bound = createBound(region, p.getWorld().getName());
                bound.setBoundName(regionName);
                parkour.getBounds().add(bound);
            } catch (Exception e) {
                p.sendMessage("You have to select a region to add it!");
            }
            p.sendMessage("Successfuly added a region called" + ChatColor.AQUA + regionName + "!");
            return true;
        }
        return false;
    }

    public Bound createBound(Region region, String world) {
        int x = region.getMinimumPoint().getX();
        int y = region.getMinimumPoint().getY();
        int z = region.getMinimumPoint().getZ();
        int x1 = region.getMaximumPoint().getX();
        int y1 = region.getMaximumPoint().getY();
        int z1 = region.getMaximumPoint().getZ();
        return new Bound(world, x, y, z, x1, y1, z1);
    }

    public Bound createBound(int x, int y, int z, int x1, int y1, int z1, String world) {
        return new Bound(world, x, y, z, x1, y1, z1);
    }

}
