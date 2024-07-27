package ru.spigotmc.destroy.newbiechat.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.spigotmc.destroy.newbiechat.NewbieChat;
import ru.spigotmc.destroy.newbiechat.database.Database;
import ru.spigotmc.destroy.newbiechat.util.Util;

public class AsyncChatListener implements Listener {

    private final NewbieChat main;
    public AsyncChatListener(NewbieChat main) {
        Bukkit.getPluginManager().registerEvents(this, main);
        this.main = main;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("newbiechat.bypass")) {
            return;
        }
        Database sql = new Database();
        if(!sql.isBlocked(player.getName())) {
            return;
        }
        int time = sql.getTime(player.getName());
        if(time > 0) {
            e.setCancelled(true);
            String cd = Util.formattedTime(time);
            for(String s : main.getConfig().getStringList("chat-lock-actions")) {
                Util.startWithCheck(player,s,cd);
            }
        }
    }

}
