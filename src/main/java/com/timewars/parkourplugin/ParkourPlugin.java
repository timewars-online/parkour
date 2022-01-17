package com.timewars.parkourplugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import com.timewars.parkourplugin.classes.Bound;
import com.timewars.parkourplugin.classes.Parkour;
import com.timewars.parkourplugin.classes.PlayerStats;
import com.timewars.parkourplugin.classes.Session;
import com.timewars.parkourplugin.commands.*;
import com.timewars.parkourplugin.events.ParkourEvents;
import com.timewars.parkourplugin.files.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;


import java.util.*;

public final class ParkourPlugin extends JavaPlugin implements Listener {

    private WorldEditPlugin worldEditPlugin;
    private HashMap<String, Parkour> parkours = new HashMap<>();
    private HashMap <Player, String> parkoursInEditing = new HashMap<>();
    private HashMap<Player, Session> sessions = new HashMap<>();
    private HashMap<String, PlayerStats> playerStats = new HashMap<>();

    private Material restartParkourItem = Material.GOLDEN_AXE;
    private Material teleportToLastCheckpoint = Material.GOLD_INGOT;
    private Material quitParkourItem = Material.IRON_INGOT;

    private ItemStack[] parkourInventory;

    private List<Material> checkPointMaterials = new ArrayList<>();

    BukkitScheduler schedulerStartParkour;

    static {
        ConfigurationSerialization.registerClass(Parkour.class);
        ConfigurationSerialization.registerClass(Bound.class);
        ConfigurationSerialization.registerClass(PlayerStats.class);
    }

    public void saveParkours()
    {
        for ( Map.Entry<String, Parkour> entry : parkours.entrySet())
        {
            System.out.println();
            System.out.println(entry.getKey() + " " + entry.getValue());
            System.out.println();
            if (entry.getKey() != "deathBlocks" )
                CustomConfig.getCustomFile().set("parkours." + entry.getKey() ,entry.getValue()) ;
            CustomConfig.save();
        }
    }

    public void loadParkours()
    {
        CustomConfig.getCustomFile().getConfigurationSection("parkours").getKeys(false).forEach(key ->{
            Parkour parkour = (Parkour) CustomConfig.getCustomFile().get("parkours." + key);
            System.out.println();
            System.out.println(key + " " + parkour);
            System.out.println();
            parkours.put(key, parkour);
        });
    }

    public void savePlayerStats()
    {
        for ( Map.Entry<String, PlayerStats> entry : playerStats.entrySet())
        {
            System.out.println();
            System.out.println(entry.getKey() + " " + entry.getValue());
            System.out.println();
            CustomConfig.getCustomFile().set("playerStats." + entry.getKey() ,entry.getValue()) ;
            CustomConfig.save();
        }
    }

    public void loadPlayerStats()
    {
        CustomConfig.getCustomFile().getConfigurationSection("playerStats").getKeys(false).forEach(key ->{
            PlayerStats playerStat = (PlayerStats) CustomConfig.getCustomFile().get("playerStats." + key);
            System.out.println();
            System.out.println(key + " " + playerStat);
            System.out.println();
            playerStats.put(key, playerStat);
        });
    }

    @Override
    public void onEnable() {
        System.out.println("________________________________________________________________");
        System.out.println("ParkourPlugin Starts...");
        System.out.println("________________________________________________________________");

        CustomConfig.setup();

        if(CustomConfig.getCustomFile().contains("parkours"))
            loadParkours();

        if(CustomConfig.getCustomFile().contains("playerStats"))
            loadPlayerStats();

        worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");

        MainCommand parkourCommands = new MainCommand();
        parkourCommands.registerCommand("addcheckpoint",new AddCheckPoint(this));
        parkourCommands.registerCommand("adddeathblock",new AddDeathBlock(this));
        parkourCommands.registerCommand("addbound",new AddBound(this));
        parkourCommands.registerCommand("createparkour",new CreateParkour(this));
        parkourCommands.registerCommand("editend",new EditEnd(this));
        parkourCommands.registerCommand("removecheckpoint",new RemoveCheckpoint(this));
        parkourCommands.registerCommand("removedeathblock",new RemoveDeathBlock(this));
        parkourCommands.registerCommand("settimedeathrun",new SetTimeDeathRun(this));
        parkourCommands.registerCommand("setspawnpoint",new SetSpawnpoint(this));
        parkourCommands.registerCommand("settype",new SetType(this));
        parkourCommands.registerCommand("edit",new Edit(this));
        parkourCommands.registerCommand("removebound",new RemoveBound(this));
        parkourCommands.registerCommand("startparkour",new StartParkour(this));
        parkourCommands.registerCommand("stats",new Stats(this));
        parkourCommands.registerCommand("settimestand",new SetTimeStandBlock(this));
        parkourCommands.registerCommand("reload",new Reload(this));
        parkourCommands.registerCommand("setfinish",new SetFinish(this));
        parkourCommands.registerCommand("setstartbound",new SetStartBound(this));
        parkourCommands.registerCommand("removeparkour",new RemoveParkour(this));
        parkourCommands.registerCommand("showbound",new ShowBound(this));

        getCommand("parkour").setExecutor(parkourCommands);
        getServer().getPluginManager().registerEvents(new ParkourEvents(this),this);


        parkourInventory = new ItemStack[36];
        parkourInventory[0] = new ItemStack(teleportToLastCheckpoint);
        parkourInventory[1] = new ItemStack(restartParkourItem);
        parkourInventory[2] = new ItemStack(quitParkourItem);

        setItemStackName(parkourInventory[0], "Back to checkpoint");
        setItemStackName(parkourInventory[1], "Restart");
        setItemStackName(parkourInventory[2], "Quit");

        checkPointMaterials.add(Material.STONE_PRESSURE_PLATE);
        checkPointMaterials.add(Material.OAK_PRESSURE_PLATE);

        schedulerStartParkour = getServer().getScheduler();
        checkPlayerStartParkour();
    }

    @Override
    public void onDisable() {
        System.out.println("ParkourPlugin ShutsDown...");
    }

    private void checkPlayerStartParkour()
    {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        schedulerStartParkour.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run()
            {
                for (Player player: players)
                {
                    for (String parkourName: parkours.keySet())
                    {
                        Parkour parkour = parkours.get(parkourName);
                        if ( parkour.getStartBound() != null )
                        {
                            boolean entered = parkour.getStartBound().isInRegion(player.getLocation());
                            if (entered && !sessions.containsKey(player))
                            {
                                if (parkour.isEnabled())
                                    startNewSession(player, parkours.get(parkourName));
                            }
                        }
                    }
                }
                checkPlayerStartParkour();
            }
        }, 2);
    }

    public static void setItemStackName(ItemStack renamed, String customName) {
        ItemMeta renamedMeta = renamed.getItemMeta();
        renamedMeta.setDisplayName(customName);
        renamed.setItemMeta(renamedMeta);
    }

    public void startNewSession(Player p, Parkour parkour)
    {
        ItemStack[] inventory = p.getInventory().getContents();
        Session session = new Session(this, parkour, p, inventory);
        p.getInventory().setContents(parkourInventory); // check whether this works TODO

        sessions.put(p, session);
    }

    public void removeSession(Session session)
    {
        sessions.remove(session);
    }

    public Material getRestartParkourItem() {
        return restartParkourItem;
    }

    public Material getTeleportToLastCheckpoint() {
        return teleportToLastCheckpoint;
    }

    public Material getQuitParkourItem() {
        return quitParkourItem;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public HashMap<String, Parkour> getParkours() {
        return parkours;
    }
    public void setParkours(HashMap<String, Parkour> parkours) {
        this.parkours = parkours;
    }

    public HashMap<Player, String> getParkoursInEditing() {
        return parkoursInEditing;
    }
    public void setParkoursInEditing(HashMap<Player, String> parkoursInEditing) {
        this.parkoursInEditing = parkoursInEditing;
    }

    public HashMap<Player, Session> getSessions() {
        return sessions;
    }
    public void setSessions(HashMap<Player, Session> sessions) {
        this.sessions = sessions;
    }

    public List<Material> getCheckPointMaterials() {
        return checkPointMaterials;
    }

    public HashMap<String, PlayerStats> getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(HashMap<String, PlayerStats> playerStats) {
        this.playerStats = playerStats;
    }
}
