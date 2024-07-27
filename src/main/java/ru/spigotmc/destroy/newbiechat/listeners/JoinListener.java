package ru.spigotmc.destroy.newbiechat.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.spigotmc.destroy.newbiechat.NewbieChat;
import ru.spigotmc.destroy.newbiechat.database.Database;
import ru.spigotmc.destroy.newbiechat.util.Util;

public class JoinListener implements Listener {
    private final NewbieChat main;
    public JoinListener(NewbieChat main) {
        Bukkit.getPluginManager().registerEvents(this, main);
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Database sql = new Database();
        if(sql.firstJoin(player.getName())) {
            sql.setBlock(player.getName(), main.getConfig().getInt("cooldown"));
            for(String s : main.getConfig().getStringList("first-join-actions")) {
                Util.startWithCheck(player, s);
            }
        }
    }

}
