package ru.spigotmc.destroy.newbiechat.util;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Util {

    public static String formattedTime(long time) {
        return TimeFormat.format((int) time);
    }

    public static void startWithCheck(Player player, String sz) {
        if(sz.startsWith("[MESSAGE]")) {
            String s2 = sz.replace("[MESSAGE] ", "").replace("[MESSAGE]", "");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s2));
        }
        if(sz.startsWith("[TITLE]")) {
            String s2 = sz.replace("[TITLE] ", "").replace("[TITLE]", "");
            String[] split = s2.split(";");
            player.sendTitle(split[0].replace("&", "§"), split[1].replace("&", "§"), 20, 15, 20);
        }
        if(sz.startsWith("[SOUND]")) {
            String s2 = sz.replace("[SOUND] ", "").replace("[SOUND]", "");
            String[] split = s2.split(";");
            Sound sound = Sound.valueOf(split[0]);
            int volume = Integer.parseInt(split[1]);
            int pitch = Integer.parseInt(split[2]);
            player.playSound(player.getLocation(), sound, (float)volume, (float)pitch);
        }
    }

    public static void startWithCheck(Player player, String sz, String cd) {
        if(sz.startsWith("[MESSAGE]")) {
            String s2 = sz.replace("[MESSAGE] ", "").replace("[MESSAGE]", "")
                    .replace("%time%", cd);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s2));
        }
        if(sz.startsWith("[TITLE]")) {
            String s2 = sz.replace("[TITLE] ", "").replace("[TITLE]", "")
                    .replace("%time%", cd);
            String[] split = s2.split(";");
            player.sendTitle(split[0].replace("&", "§"), split[1].replace("&", "§"), 20, 15, 20);
        }
        if(sz.startsWith("[SOUND]")) {
            String s2 = sz.replace("[SOUND] ", "").replace("[SOUND]", "");
            String[] split = s2.split(";");
            Sound sound = Sound.valueOf(split[0]);
            int volume = Integer.parseInt(split[1]);
            int pitch = Integer.parseInt(split[2]);
            player.playSound(player.getLocation(), sound, (float)volume, (float)pitch);
        }
    }

}
