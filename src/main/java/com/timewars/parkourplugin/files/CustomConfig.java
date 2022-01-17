package com.timewars.parkourplugin.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

public class CustomConfig {

    private static File file; //real file which we convert to customFile
    private static FileConfiguration customFile;

    public static void setup()
    {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("ParkourPlugin").getDataFolder(), "customconfig.yml");

        if(!file.exists())
        {
            System.out.println("Creating New File!");
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                System.out.println("Couldn't setup a file");
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void save()
    {
        try
        {
            customFile.save(file);
        }
        catch (IOException e)
        {
            System.out.println("Couldn't save CustomFile!");
        }
    }

    public static void reload()
    {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getCustomFile() {
        return customFile;
    }

    public static void setCustomFile(FileConfiguration customFile) {
        CustomConfig.customFile = customFile;
    }
}
