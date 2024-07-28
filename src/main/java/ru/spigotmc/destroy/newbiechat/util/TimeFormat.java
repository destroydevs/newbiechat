package ru.spigotmc.destroy.newbiechat.util;

import ru.spigotmc.destroy.newbiechat.NewbieChat;

import java.text.SimpleDateFormat;

public class TimeFormat {

    public static String format(int seconds) {
        int hours = seconds / 3600; // Количество часов
        int minutes = (seconds % 3600) / 60; // Количество минут
        int remainingSeconds = seconds % 60; // Оставшиеся секунды

        return NewbieChat.config().getString("time-format", "%h%ч. %m%м. %s%с.")
                .replace("%h%", String.valueOf(hours))
                .replace("%m%", String.valueOf(minutes))
                .replace("%s%", String.valueOf(remainingSeconds));
    }

}
