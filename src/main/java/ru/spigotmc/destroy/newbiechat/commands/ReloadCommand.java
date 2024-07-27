package ru.spigotmc.destroy.newbiechat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.spigotmc.destroy.newbiechat.NewbieChat;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {

    NewbieChat main;
    public ReloadCommand(NewbieChat main) {
        main.getCommand("newbiechat").setExecutor(this);
        main.getCommand("newbiechat").setTabCompleter(this);
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("newbiechat.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("commands.no-perm")));
            return true;
        }
        if(args.length < 1) {
            sender.sendMessage(ChatColor.RED+"Недостаточно аргументов.");
            return true;
        }
        if(args[0].equalsIgnoreCase("reload")) {
            main.reloadConfig();
            sender.sendMessage(ChatColor.GREEN+"Конфигурация перезагружена.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1) {
            list.add("reload");
            return list;
        }
        return null;
    }
}
