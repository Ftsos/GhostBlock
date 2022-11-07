package me.ftsos.ghostblock.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Colorizer {
    public static String colorizeText(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void colorize(String text, Player player) {
        player.sendMessage(Colorizer.colorizeText(text));
    }

    public static void colorize(String text, CommandSender player) {
        player.sendMessage(Colorizer.colorizeText(text));
    }
}
