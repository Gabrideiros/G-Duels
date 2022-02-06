package com.github.gabrideiros.duels.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CustomConfig {
    private final JavaPlugin plugin;
    private final String fileName;
    private File file;
    private FileConfiguration configuration;

    public CustomConfig(JavaPlugin plugin, String dir, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(dir, fileName);
    }

    public void saveDefaultConfig() {
        try {
            this.file = new File(plugin.getDataFolder(), fileName);

            if (!this.file.exists()) {
                this.plugin.saveResource(this.fileName, false);
            }

            this.configuration = YamlConfiguration.loadConfiguration(this.file);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public FileConfiguration getConfig() {
        return this.configuration;
    }

    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            this.getConfig().save(this.file);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }
}
