package com.timewars.parkourplugin.commands;

import com.timewars.parkourplugin.ParkourPlugin;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveCheckpoint implements TabExecutor {
    ParkourPlugin parkourPlugin;
    public RemoveCheckpoint(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ( sender instanceof Player)
        {
            Player p = (Player) sender;
            if (!parkourPlugin.getParkoursInEditing().containsKey(p))
            {
                p.sendMessage("Select a parkour to edit first! Use " + ChatColor.AQUA + "startedit" + "!");
                return true;
            }

            String parkourName = parkourPlugin.getParkoursInEditing().get(p);
            Block targetBlock = p.getTargetBlock(null,100);

            // which blocks are checkpoints TODO
            //if (targetBlock.getType() != Material.Plate)

            if (!parkourPlugin.getParkours().get(parkourName).getCheckpoints().contains(targetBlock.getLocation()) )
            {
                p.sendMessage("There is no checkpoint like that in " + ChatColor.DARK_RED + parkourName);
                return true;
            }
            parkourPlugin.getParkours().get(parkourName).getCheckpoints().remove(targetBlock.getLocation());
            p.sendMessage("Successfully removed checkpoint from " + ChatColor.DARK_RED + parkourName +
                    ". Position is " + targetBlock.getLocation().getX() + " " + targetBlock.getLocation().getY() + " " +
                    targetBlock.getLocation().getZ());
            return true;
        }
        return false;
    }


}
