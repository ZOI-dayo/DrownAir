package net.zoizoi.plugin.drownair.core.command;

import net.zoizoi.plugin.drownair.core.command.drownair.EnableCommand;
import net.zoizoi.plugin.drownair.core.command.drownair.HelpCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DrownAirCommand extends CommandMaster {
    public DrownAirCommand(CommandManager manager) {
        super(manager);
        addSubCommand(new HelpCommand(this.manager));
        addSubCommand(new EnableCommand(this.manager));
    }

    @Override
    public String getName() {
        return "drownair";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (String s : args) System.out.println(s);
        if (args.length < 2) return false;
        return subCommands.get(args[1]).onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length - 1));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length <= 1)
            return Stream.of(getName()).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        if (args.length <= 2)
            return subCommands.keySet().stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
        return subCommands.get(args[1]).onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length - 1));
    }
}
