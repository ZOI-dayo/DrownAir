package net.zoizoi.plugin.drownair;

import net.zoizoi.plugin.drownair.core.command.CommandManager;
import net.zoizoi.plugin.drownair.core.command.DrownAirCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        commandManager = new CommandManager(this);
        commandManager.addRootCommand(new DrownAirCommand(commandManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
