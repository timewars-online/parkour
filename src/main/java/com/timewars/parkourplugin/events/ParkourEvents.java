package com.timewars.parkourplugin.events;

import com.timewars.parkourplugin.ParkourPlugin;
import com.timewars.parkourplugin.classes.Parkour;
import com.timewars.parkourplugin.classes.Session;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ParkourEvents implements Listener {
    ParkourPlugin parkourPlugin;


    public ParkourEvents(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("It works!");
        }
    }

    @EventHandler
    public void onPlayerLeave (PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        if ( parkourPlugin.getSessions().containsKey(p) )
        {
            Session session = parkourPlugin.getSessions().get(p);
            session.quitParkour();
        }
        if ( parkourPlugin.getParkoursInEditing().containsKey(p))
        {
            Parkour parkour = parkourPlugin.getParkours().get(parkourPlugin.getParkoursInEditing().get(p));

            parkourPlugin.getParkoursInEditing().remove(p);
            parkour.setEnabled(true);
            parkour.setStatusEnabled();
        }
    }

    @EventHandler
    public void onPlayerRightClickItem(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if ( parkourPlugin.getSessions().containsKey(p) )
        {
            Material currentItem = p.getInventory().getItemInMainHand().getType();
            Session session = parkourPlugin.getSessions().get(p);

            if (e.getAction().equals(Action.PHYSICAL))
            {
                Block b = e.getClickedBlock();
                if ( parkourPlugin.getCheckPointMaterials().contains(b.getType()) &&
                        session.getParkour().getCheckpoints().contains(b.getLocation()) )
                {
                    session.setLastPosition(p.getLocation());
                }
            }
            else if (currentItem == parkourPlugin.getQuitParkourItem())
            {
                if ( parkourPlugin.getSessions().containsKey(p))
                {
                    p.sendMessage("Ending parkour (item) ...");
                    session.quitParkour();
                }
            }
            else if (currentItem == parkourPlugin.getRestartParkourItem())
            {
                p.sendMessage("restart parkour (item) ...");
                Parkour parkour = session.getParkour();

                p.teleport(parkour.getStart());
                session.setTimeInParkour(0);
                session.setLastPosition(parkour.getStart());
                session.resetSchedulerParkourTime();
                session.resetSchedulerBlockStandTime();
            }
            else if (currentItem == parkourPlugin.getTeleportToLastCheckpoint())
            {
                p.sendMessage("last checkpoint parkour (item) ...");
                p.teleport(session.getLastPosition());
            }
        }

    }

}
