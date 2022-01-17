package com.timewars.parkourplugin.commands;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.timewars.parkourplugin.ParkourPlugin;
import com.timewars.parkourplugin.classes.Bound;
import com.timewars.parkourplugin.classes.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ShowBound implements TabExecutor {

    ParkourPlugin parkourPlugin;

    public ShowBound(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }
    public ArrayList<String> boundNames = new ArrayList<>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if( sender instanceof Player)
        {
            Player p = (Player) sender;
            Parkour parkour = parkourPlugin.getParkours().get(parkourPlugin.getParkoursInEditing().get(p));

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
                p.sendMessage("Select a parkour to edit first! Use " + ChatColor.AQUA + "starteditind" + "!");
                return true;
            }
            String boundName = args[0];
            String parkourName = parkourPlugin.getParkoursInEditing().get(p);
            Parkour parkour = parkourPlugin.getParkours().get(parkourName);

            Bound bound = parkour.getBound(boundName);

            Location block1 = new Location( bound.getWorld(), bound.getX(), bound.getY(), bound.getZ() );
            Location block2 = new Location( bound.getWorld(), bound.getX2(), bound.getY2(), bound.getZ2() );
            BlockData block = Bukkit.createBlockData(Material.SLIME_BLOCK);

            for ( int i = block1.getBlockX(); i <= block2.getBlockX(); i++)
            {
                p.sendBlockChange(new Location(bound.getWorld(), i, bound.getY(), bound.getZ()),block);
                p.sendBlockChange(new Location(bound.getWorld(), i, bound.getY2(), bound.getZ()),block);
                p.sendBlockChange(new Location(bound.getWorld(), i, bound.getY(), bound.getZ2()),block);
                p.sendBlockChange(new Location(bound.getWorld(), i, bound.getY2(), bound.getZ2()),block);
            }

            for ( int i = block1.getBlockY(); i <= block2.getBlockY(); i++)
            {
                p.sendBlockChange(new Location(bound.getWorld(), bound.getX(), i, bound.getZ()),block);
                p.sendBlockChange(new Location(bound.getWorld(), bound.getX2(), i, bound.getZ()),block);
                p.sendBlockChange(new Location(bound.getWorld(), bound.getX(), i, bound.getZ2()),block);
                p.sendBlockChange(new Location(bound.getWorld(), bound.getX2(), i, bound.getZ2()),block);
            }

            for ( int i = block1.getBlockZ(); i <= block2.getBlockZ(); i++)
            {
                p.sendBlockChange(new Location(bound.getWorld(), bound.getX(), bound.getY(), i),block);
                p.sendBlockChange(new Location(bound.getWorld(), bound.getX2(), bound.getY(), i),block);
                p.sendBlockChange(new Location(bound.getWorld(), bound.getX(), bound.getY2(), i),block);
                p.sendBlockChange(new Location(bound.getWorld(), bound.getX2(), bound.getY2(), i),block);
            }

            BukkitScheduler scheduler;
            scheduler = parkourPlugin.getServer().getScheduler();

            scheduler.scheduleSyncDelayedTask(parkourPlugin, new Runnable()
            {
                @Override
                public void run()
                {
                    for ( int i = block1.getBlockX(); i <= block2.getBlockX(); i++)
                    {
                        Location location = new Location(bound.getWorld(), i, bound.getY(), bound.getZ());
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), i, bound.getY2(), bound.getZ());
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), i, bound.getY(), bound.getZ2());
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), i, bound.getY2(), bound.getZ2());
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());
                    }

                    for ( int i = block1.getBlockY(); i <= block2.getBlockY(); i++)
                    {
                        Location location = new Location(bound.getWorld(), bound.getX(), i, bound.getZ());
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), bound.getX2(), i, bound.getZ());
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), bound.getX(), i, bound.getZ2());
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), bound.getX2(), i, bound.getZ2());
                        p.sendBlockChange(location, p.getWorld().getBlockAt(location).getBlockData());


                    }

                    for ( int i = block1.getBlockZ(); i <= block2.getBlockZ(); i++)
                    {
                        Location location = new Location(bound.getWorld(), bound.getX(), bound.getY(), i);
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), bound.getX2(), bound.getY(), i);
                        p.sendBlockChange(location,p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), bound.getX(), bound.getY2(), i);
                        p.sendBlockChange(location, p.getWorld().getBlockAt(location).getBlockData());

                        location = new Location(bound.getWorld(), bound.getX2(), bound.getY2(), i);
                        p.sendBlockChange(location, p.getWorld().getBlockAt(location).getBlockData());
                    }
                }
            },100);

            p.sendMessage("Successfuly showed you " + ChatColor.AQUA + boundName + "!");
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
