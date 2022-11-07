package me.ftsos.ghostblock.utils;

import me.ftsos.ghostblock.GhostBlock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Locale {
    private GhostBlock plugin;
    public Locale(GhostBlock plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        FileConfiguration defaultConfig = getDefaultConfig();
        for (String key : defaultConfig.getKeys(true)) {
            if(!defaultConfig.contains(key)) {
                plugin.getProvider().getLogger().log("The default config key" + key + ", could not be setup because it was not found as contained", LogLevel.ERROR);
                continue;
            }
            Object defaultValue = defaultConfig.get(key);

            setupDefaultValue(key, defaultValue);
        }
    }

    public FileConfiguration getDefaultConfig() {
        InputStream customClassStream = plugin.getClass().getResourceAsStream("/config.yml");
        InputStreamReader streamReader = new InputStreamReader(customClassStream);
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(streamReader);
        return defaultConfig;
    }

    /*
    * This function will set the default values of a path if the path doesn't exist.
    * The function will also return false if the path alredy exist, and true, if it was able to set it up.
    * */
    private boolean setupDefaultValue(String path, Object toSet) {
        if (!plugin.getConfig().contains(path)) return false;
        plugin.getConfig().set(path, toSet);
        return true;
    }

    public String getCommandUsage() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.command.command-usage"));
    }
    //clear-command
    public String getClearCommandUsage() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.clear-command.command-usage"));
    }

    public String getBlockRemovedMessage() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.clear-command.successful-block-removed"));
    }

    public String getClearCommandPermission() {
        return plugin.getConfig().getString("permissions.clear-command.permission");
    }

    public String getClearCommandErrorMessage() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.clear-command.error-on-console"));
    }

    public String getNoPermission() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.no-permission"));
    }

    public String getSchematicCommandPermission() {
        return plugin.getConfig().getString("permissions.schematic-command.permission");
    }

    public String getSchematicCommandUsage() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.schematic-command.command-usage"));
    }

    public String getSuccessfulSchematicSent() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.schematic-command.successful-schematic-sent"));
    }

    public String getSchematicCommandError() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.schematic-command.error-on-console"));
    }

    public String getCommandPermission() {
        return plugin.getConfig().getString("permissions.command.permission");
    }

    public String getPlacedBlockMessage() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.command.successful-block-placed"));
    }

    public String getErrorMessage() {
        return Colorizer.colorizeText(plugin.getConfig().getString("messages.command.error-on-console"));
    }
}
