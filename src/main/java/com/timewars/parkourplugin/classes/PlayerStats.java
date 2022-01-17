package com.timewars.parkourplugin.classes;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats implements ConfigurationSerializable {

    private String playerName;

    private HashMap<String, Double> bestTimes = new HashMap<>();

    private HashMap<String, Integer> attempts = new HashMap<>();

    public PlayerStats(String playerName, HashMap<String, Double> bestTimes, HashMap<String, Integer> attempts) {
        this.playerName = playerName;
        this.bestTimes = bestTimes;
        this.attempts = attempts;
    }

    public PlayerStats(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("player", playerName);
        serialized.put("bestTimes", bestTimes );
        serialized.put("attempts", attempts );
        return serialized;
    }

    public static PlayerStats deserialize(Map<String, Object> deserialize) {
        return new PlayerStats(
                (String) deserialize.get("player"),
                (HashMap<String, Double>) deserialize.get("bestTimes"),
                (HashMap<String, Integer>) deserialize.get("attempts")
        );
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public HashMap<String, Double> getBestTimes() {
        return bestTimes;
    }

    public void setBestTimes(HashMap<String, Double> bestTimes) {
        this.bestTimes = bestTimes;
    }

    public HashMap<String, Integer> getAttempts() {
        return attempts;
    }

    public void setAttempts(HashMap<String, Integer> attempts) {
        this.attempts = attempts;
    }
}
