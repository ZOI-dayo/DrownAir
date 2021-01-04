package net.zoizoi.plugin.drownair;

import net.zoizoi.plugin.drownair.core.command.CommandManager;
import net.zoizoi.plugin.drownair.core.command.DrownAirCommand;
import net.zoizoi.plugin.drownair.logic.AirLevelDirector;
import net.zoizoi.plugin.drownair.logic.PluginLogic;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private CommandManager commandManager;
    public PluginLogic logic;

    @Override
    public void onEnable() {
        // Plugin startup logic
        commandManager = new CommandManager(this);
        commandManager.addRootCommand(new DrownAirCommand(commandManager));
        logic = new PluginLogic();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
