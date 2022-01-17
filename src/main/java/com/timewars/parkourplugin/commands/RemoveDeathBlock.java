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

public class RemoveDeathBlock implements TabExecutor {

    ParkourPlugin parkourPlugin;
    public RemoveDeathBlock(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player)
        {
            Player p = (Player) sender;
            if (!parkourPlugin.getParkoursInEditing().containsKey(p))
            {
                p.sendMessage("Select a parkour to edit first! Use " + ChatColor.AQUA + "startedit" + "!");
                return true;
            }

            String parkourName = parkourPlugin.getParkoursInEditing().get(p);
            Block targetBlock = p.getTargetBlock(null,100);

            if ( !parkourPlugin.getParkours().get(parkourName).getDeathBlocks().contains(targetBlock.getType()) )
            {
                p.sendMessage("Block " + ChatColor.AQUA + targetBlock.getType().toString() + " is not in " + ChatColor.DARK_RED
                + parkourName);
                return true;
            }
            parkourPlugin.getParkours().get(parkourName).getDeathBlocks().remove(targetBlock.getType());
            p.sendMessage("Block " + ChatColor.AQUA + targetBlock.getType().toString() + " was removed from " + ChatColor.DARK_RED
                    + parkourName);
            return true;
        }
        return false;
    }

}
