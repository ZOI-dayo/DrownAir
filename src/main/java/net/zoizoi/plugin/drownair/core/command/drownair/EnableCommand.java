package net.zoizoi.plugin.drownair.core.command.drownair;

import net.zoizoi.plugin.drownair.Main;
import net.zoizoi.plugin.drownair.core.command.CommandManager;
import net.zoizoi.plugin.drownair.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EnableCommand extends CommandMaster {
    public EnableCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "enable";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ((Main) manager.plugin).logic.putDrownPlayer((Player) sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // return subCommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
