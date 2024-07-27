package ru.spigotmc.destroy.newbiechat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.spigotmc.destroy.newbiechat.commands.ReloadCommand;
import ru.spigotmc.destroy.newbiechat.database.Database;
import ru.spigotmc.destroy.newbiechat.listeners.AsyncChatListener;
import ru.spigotmc.destroy.newbiechat.listeners.CommandListener;
import ru.spigotmc.destroy.newbiechat.listeners.JoinListener;
import ru.spigotmc.destroy.newbiechat.util.ClassLoader;

public final class NewbieChat extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Database sql = new Database();
        sql.connect();
        new AsyncChatListener(this);
        new ReloadCommand(this);
        new CommandListener(this);
        new JoinListener(this);
        msg("&e============================");
        msg("&aВерсия &f" + getDescription().getVersion());
        msg("&aРазработчик:&b https://t.me/byteswing");
        msg("&e============================");
        sql.startChecks();
    }

    public static FileConfiguration config() {
        return NewbieChat.getPlugin(NewbieChat.class).getConfig();
    }

    @Override
    public void onDisable() {
        Database sql = new Database();
        Database.saveCooldowns();
        sql.close();
    }

    private void msg(String msg) {
        String p = ChatColor.translateAlternateColorCodes('&', "&e"+getName()+" | ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', p+msg));
    }}
