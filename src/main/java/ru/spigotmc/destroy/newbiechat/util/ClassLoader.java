package ru.spigotmc.destroy.newbiechat.util;

import org.bukkit.Bukkit;
import ru.spigotmc.destroy.newbiechat.database.DataType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader {

    public void loadDriver(DataType type) {
        try {
        switch (type) {
            case H2:
                Class.forName("org.h2.Driver");
                break;
            case MYSQL:
                Class.forName("com.mysql.jdbc.Driver");
        }
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().severe(type.name()+" driver not loaded!");
        }

    }

}
