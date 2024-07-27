package ru.spigotmc.destroy.newbiechat.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ru.spigotmc.destroy.newbiechat.NewbieChat;
import ru.spigotmc.destroy.newbiechat.database.Database;
import ru.spigotmc.destroy.newbiechat.util.Util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CommandListener implements org.bukkit.event.Listener {

    private final NewbieChat main;

    public CommandListener(NewbieChat main) {
        Bukkit.getPluginManager().registerEvents(this, main);
        this.main = main;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if(main.getConfig().getBoolean("block-commands.enable") && e.getMessage().contains(":")) {
            if (player.hasPermission("newbiechat.doubled")) {
                return;
            }
            e.setCancelled(true);
            for(String sz : main.getConfig().getStringList("block-commands.actions")) {
                Util.startWithCheck(player, sz);
            }
        }
        if (player.hasPermission("newbiechat.bypass")) {
            return;
        }
        Database sql = new Database();
        if(!sql.isBlocked(player.getName())) {
            return;
        }
        for(String s : main.getConfig().getStringList("lock-commands")) {
            if (e.getMessage().toLowerCase().startsWith("/"+s+" ") || e.getMessage().equalsIgnoreCase("/"+s)) {
                e.setCancelled(true);
                int time = sql.getTime(player.getName());
                if(time > 0) {
                    String cd = Util.formattedTime(time);
                    for(String sz : main.getConfig().getStringList("command-lock-actions")) {
                        Util.startWithCheck(player,sz,cd);
                    }
                    e.setCancelled(true);
                }
            }
        }
    }
}
