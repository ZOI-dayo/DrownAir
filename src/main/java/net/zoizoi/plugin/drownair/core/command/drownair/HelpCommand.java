package net.zoizoi.plugin.drownair.core.command.drownair;

import net.zoizoi.plugin.drownair.core.command.CommandManager;
import net.zoizoi.plugin.drownair.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends CommandMaster {

    public HelpCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        sender.sendMessage("---DrownAir---");
        sender.sendMessage("/drownair help : このメニューを開く");
        sender.sendMessage("/drownair enable : 有効化");
        sender.sendMessage("/drownair disable : 無効化");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // return subCommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
