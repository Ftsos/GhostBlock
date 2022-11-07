package me.ftsos.ghostblock.utils;

import me.ftsos.ghostblock.GhostBlock;
import org.bukkit.ChatColor;

public class Logger {
    private GhostBlock plugin;

    public Logger(GhostBlock plugin) {
        this.plugin = plugin;
    }
    /**
     *
     * This function will receive a log string, log level, and log it to the default logger with a default color.
     * Also, if the log has color codes like &c, that code will be translated, and will overwrite the default color
     *
     * */
    public void log(String toLog, LogLevel level) {
        switch (level) {
            case ERROR:
            case SEVERE:
                plugin.getLogger().severe(level.getColor() + Colorizer.colorizeText(toLog));
                break;
            case FINE:
                plugin.getLogger().fine(level.getColor() + Colorizer.colorizeText(toLog));
                break;
            case INFO:
                plugin.getLogger().info(level.getColor() + Colorizer.colorizeText(toLog));
                break;
        }
    }
    /**
     *
     * This function will recive a log string, log level, and a color, and log it to the default logger with the specified color.
     * Also, if the log has color codes like &c, that code will be translated, and will overwrite the specified color
     *
     * */
    public void log(String toLog, LogLevel level, ChatColor color) {
        switch (level) {
            case ERROR:
            case SEVERE:
                plugin.getLogger().severe(color + Colorizer.colorizeText(toLog));
                break;
            case FINE:
                plugin.getLogger().fine(color + Colorizer.colorizeText(toLog));
                break;
            case INFO:
                plugin.getLogger().info(color + Colorizer.colorizeText(toLog));
                break;
        }
    }
}

enum LogLevel {
    INFO(ChatColor.AQUA),
    FINE(ChatColor.GREEN),
    ERROR(ChatColor.RED),
    SEVERE(ChatColor.DARK_RED);

    private ChatColor color;

    LogLevel(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }
}
