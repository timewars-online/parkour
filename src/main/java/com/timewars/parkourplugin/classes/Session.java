package com.timewars.parkourplugin.classes;

import com.timewars.parkourplugin.ParkourPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Session  {

    ParkourPlugin parkourPlugin;
    Location lastStandingBlock;
    int timeStand = 1;

    public Session(ParkourPlugin parkourPlugin, Parkour parkour, Player p, ItemStack[] inventory) {
        this.parkourPlugin = parkourPlugin;

        this.parkour = parkour;
        this.lastPosition = parkour.getStart();
        this.p = p;

        schedulerBoundsAndDeathBlocks = getServer().getScheduler();
        schedulerParkourTime = getServer().getScheduler();
        schedulerStandBlock = getServer().getScheduler();
        this.inventoryBeforeStart = inventory;

        p.sendMessage(ChatColor.AQUA + "Session started!");

        checkPlayerInBoundsAndDeathBlocks(p);

        if (parkour.getType().equals("deathrun"))
        {
            p.sendMessage("start death");
            //CheckDeathRunTimer(p);
            lastStandingBlock = new Location(parkour.getStart().getWorld(),0,0,0);
            timeStand = Math.round(parkour.getTimeStandBlock() * 20);
            checkStandTime(p);
        }
        p.setAllowFlight(false);
    }

    private ItemStack[] inventoryBeforeStart;
    private Player p;
    private Parkour parkour;

    private Location lastPosition;

    double timeInParkour = 0;

    BukkitScheduler schedulerParkourTime;

    BukkitScheduler schedulerBoundsAndDeathBlocks;

    BukkitScheduler schedulerStandBlock;

    private boolean timerWork = true;


    public void checkPlayerInBoundsAndDeathBlocks(Player p)
    {
        List<Bound> bounds = parkour.getBounds();
        schedulerBoundsAndDeathBlocks.scheduleSyncDelayedTask(parkourPlugin, new Runnable()
        {
            @Override
            public void run()
            {
                boolean isInside = false;
                for (Bound bound:
                        bounds) {
                    if(bound.isInRegion(p.getLocation()))
                    {
                        isInside = true;
                        break;
                    }
                }
                timeInParkour +=0.1;
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED.toString() + ChatColor.BOLD +
                        "<<" + String.valueOf(timeInParkour) + ">>"));

                if ( parkour.getFinish().isInRegion(p.getLocation()))
                {
                    finishParkour();
                    return;
                }
                if ( !isInside )
                {
                    p.sendMessage("You left this parkour asshole");
                    quitParkour();
                    return;
                }
                if ( parkour.getDeathBlocks().contains(checkBlockUnder(p)) )
                {
                    teleportToCheckpoint();
                }
                if (!parkour.isEnabled())
                {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED.toString() + ChatColor.BOLD +
                            "Admins are editing parkour! Sorry!"));
                    p.teleport(parkour.getStart());
                    quitParkour();
                }
                if ( timerWork ) checkPlayerInBoundsAndDeathBlocks(p);
            }
        }, 2); //20 == 1sec

    }


    public void CheckDeathRunTimer(Player p)
    {

        schedulerParkourTime.scheduleSyncDelayedTask(parkourPlugin, new Runnable()
        {
            @Override
            public void run()
            {
                if ( timeInParkour < parkour.getTimeDeathRun() )
                {
                    p.sendMessage(String.valueOf(timeInParkour));
                    timeInParkour+= 1;
                }
                else
                {
                    timeInParkour = 0;
                    p.teleport(parkour.getStart());
                    setLastPosition(parkour.getStart());
                }
                if (timerWork) CheckDeathRunTimer(p);
            }
        }, 20);

    }


    public void checkStandTime(Player p)
    {
        schedulerStandBlock.scheduleSyncDelayedTask(parkourPlugin, new Runnable()
        {
            @Override
            public void run()
            {
                Location standingLocation = p.getLocation().add(0,-.1,0).getBlock().getLocation();

                if ( lastStandingBlock.equals(standingLocation) )
                {
                    teleportToCheckpoint();
                }
                else
                    lastStandingBlock = standingLocation;

                if (timerWork) checkStandTime(p);
            }
        }, timeStand );
    }

    private void teleportToCheckpoint()
    {
        p.teleport(getLastPosition());
        if ( getLastPosition().equals(parkour.getStart()))
        {
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED.toString() + ChatColor.BOLD +
                    "Try again"));
            quitParkour();
        }

    }

    public Material checkBlockUnder(Player player)
    {
        Location standingLocation = player.getLocation();
        Material standingBlock = standingLocation.add(0,-.1,0).getBlock().getType();
        if (standingBlock.equals(Material.LAVA) || standingBlock.equals(Material.WATER)){
            standingBlock = standingLocation.add(0, .3, 0).getBlock().getType();
        }
        return standingBlock;
    }


    public void quitParkour()
    {
        timerWork = false;
        p.getInventory().setContents(inventoryBeforeStart);

        p.setAllowFlight(true);
        parkourPlugin.getSessions().remove(p);
        parkourPlugin.removeSession(this);
    }


    public void finishParkour()
    {
        HashMap<String, PlayerStats> playerStats = parkourPlugin.getPlayerStats();

        if ( !playerStats.containsKey(p.getName()) )
        {
            playerStats.put(p.getName(), new PlayerStats(p.getName()));
        }

        HashMap<String, Integer> attempts = playerStats.get(p.getName()).getAttempts();
        HashMap<String, Double> bestTimes = playerStats.get(p.getName()).getBestTimes();

        if (attempts.containsKey(parkour.getName()))
            attempts.put(parkour.getName(), attempts.get(parkour.getName()) + 1);
        else
            attempts.put(parkour.getName(), 1);

        checkSetTime(bestTimes, timeInParkour);

        p.sendMessage("Parkour " + parkour.getName() + " finished! " + ChatColor.AQUA  + "Congrats!");
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.AQUA.toString() + ChatColor.BOLD +
                "<<" + "Finished in " + timeInParkour + ">>"));
        parkourPlugin.savePlayerStats(); // move to OnDisable
        quitParkour();
    }

    private void checkSetTime(HashMap<String, Double> bestTimes, double time)
    {
        if (!bestTimes.containsKey(parkour.getName()) )
        {
            bestTimes.put(parkour.getName(), time);
            return;
        }

        if (time < bestTimes.get(parkour.getName())) bestTimes.put(parkour.getName(), time);
    }

    public double getTimeInParkour() {
        return timeInParkour;
    }

    public void setTimeInParkour(float timeInParkour) {
        this.timeInParkour = timeInParkour;
    }

    public void resetSchedulerParkourTime()
    {
        schedulerParkourTime = getServer().getScheduler();
    }
    public void resetSchedulerBlockStandTime()
    {
        schedulerParkourTime = getServer().getScheduler();
    }

    public BukkitScheduler getSchedulerParkourTime() {
        return schedulerParkourTime;
    }

    public BukkitScheduler getSchedulerBoundsAndDeathBlocks() {
        return schedulerBoundsAndDeathBlocks;
    }

    public Location getLastPosition() {
        return lastPosition;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    public Parkour getParkour() {
        return parkour;
    }

    public void setParkour(Parkour parkour) {
        this.parkour = parkour;
    }

    public void setLastPosition(Location lastPosition) {
        this.lastPosition = lastPosition;
    }

    public ItemStack[] getInventoryBeforeStart() {
        return inventoryBeforeStart;
    }
}
