package com.timewars.parkourplugin.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parkour implements ConfigurationSerializable {

    private String name;
    private String type = "regular";
    private String status = "editing";

    private List<Material> deathBlocks = new ArrayList<>();
    private List<String> deathBlocksStrings = new ArrayList<>();

    private List<Location> checkpoints = new ArrayList<>();

    private List<Bound> bounds = new ArrayList<>();

    private Location start;
    private Bound finish;
    private Bound startBound;

    private float timeDeathRun;
    private float timeStandBlock;

    private boolean enabled;

    public Parkour(String name) {
        this.name = name;
    }

    public Parkour(String name, String type, List<String> deathBlocks, List<Location> checkPoints,
                   List<Bound> bounds, Location start, Bound finish, Bound startBound, float timeDeathRun,
                   float timeStandBlock, boolean enabled) {
        this.name = name;
        this.type = type;
        for ( String deathBlock: deathBlocks )
        {
            this.deathBlocks.add(Material.getMaterial(deathBlock));
        }
        this.deathBlocksStrings = deathBlocks;
        this.checkpoints = checkPoints;
        this.bounds = bounds;
        this.start = start;
        this.finish = finish;
        this.startBound = startBound;
        this.timeDeathRun = timeDeathRun;
        this.timeStandBlock = timeStandBlock;
        this.enabled = enabled;
    }

    public void showStats(Player p)
    {
        p.sendMessage(name + "\n" + type + "\n");

        p.sendMessage(deathBlocks.toString());
        p.sendMessage(checkpoints.toString());
        p.sendMessage(bounds.toString());

        p.sendMessage("start at " + start.toString());
        p.sendMessage("end at " + finish.toString());

        p.sendMessage("deathrun time " + timeDeathRun);

    }

    public boolean removeBound (String name)
    {
        for (Bound bound: bounds)
        {
            if ( bound.getBoundName().equals(name) )
            {
                bounds.remove(bound);
                return true;
            }
        }
        return false;
    }

    public Bound getBound (String name)
    {
        for (Bound bound: bounds)
        {
            if ( bound.getBoundName().equals(name) )
            {
                return bound;
            }
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatusEditing() {
        status = "editing";
    }

    public void setStatusSaving() {
        status = "saving";
    }

    public void setStatusEnabled() {
        status = "enabled";
    }

    public List<Bound> getBounds() {
        return bounds;
    }

    public void setBounds(List<Bound> bounds) {
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<Material> getDeathBlocks() {
        return deathBlocks;
    }

    public Location getStart() {
        return start;
    }

    public Bound getFinish() {
        return finish;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDeathBlocks(List<Material> deathBlocks) {
        this.deathBlocks = deathBlocks;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setFinish(Bound finish) {
        this.finish = finish;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getTimeDeathRun() {
        return timeDeathRun;
    }

    public void setTimeDeathRun(float timeDeathRun) {
        this.timeDeathRun = timeDeathRun;
    }

    public float getTimeStandBlock() {
        return timeStandBlock;
    }

    public void setTimeStandBlock(float timeStandBlock) {
        this.timeStandBlock = timeStandBlock;
    }

    public List<String> getDeathBlocksStrings() {
        return deathBlocksStrings;
    }

    public void setDeathBlocksStrings(List<String> deathBlocksStrings) {
        this.deathBlocksStrings = deathBlocksStrings;
    }

    public List<Location> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Location> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Bound getStartBound() {
        return startBound;
    }

    public void setStartBound(Bound startBound) {
        this.startBound = startBound;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("name", name );
        serialized.put("finish", finish );
        serialized.put("startBound", startBound );
        serialized.put("start", start );
        serialized.put("timeDeathRun", timeDeathRun);
        serialized.put("timeStandBlock", timeStandBlock);
        serialized.put("type", type);
        serialized.put("enabled", enabled);
        serialized.put("deathBlocksStrings", deathBlocksStrings);
        serialized.put("bounds", bounds);
        serialized.put("checkpoints", checkpoints);
        return serialized;
    }

    public static Parkour deserialize(Map<String, Object> deserialize) {
        return new Parkour(
                (String) deserialize.get("name"),
                (String) deserialize.get("type"),
                (List<String>) deserialize.get("deathBlocksStrings"),
                (List<Location>) deserialize.get("checkpoints"),
                (List<Bound>) deserialize.get("bounds"),
                (Location) deserialize.get("start"),
                (Bound) deserialize.get("finish"),
                (Bound) deserialize.get("startBound"),
                (float) Float.parseFloat(deserialize.get("timeDeathRun").toString()),
                (float) Float.parseFloat(deserialize.get("timeStandBlock").toString()),
                (boolean) deserialize.get("enabled")
                );
    }
}
