package net.zoizoi.plugin.drownair.logic;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PluginLogic implements Listener {
    private final JavaPlugin plugin;
    private final HashMap<UUID, AirLevelDirector> airDitectors = new HashMap<>();
    private final HashMap<UUID, Integer> remainingAir = new HashMap<>();

    private BukkitRunnable logic = new BukkitRunnable() {
        private final HashMap<UUID, Integer> inAirTick = new HashMap<>();

        @Override
        public void run() {
            for (AirLevelDirector ditector : airDitectors.values()) {
                Player player = ditector.getTarget();
                player.setRemainingAir(player.getMaximumAir());
                if (player.getEyeLocation().getBlock().getType() == Material.WATER) {
                    inAirTick.put(player.getUniqueId(), 0);

                    airDitectors.get(player.getUniqueId()).setAirticks(0);
                } else {
                    if (inAirTick.getOrDefault(player.getUniqueId(), 0) < player.getMaximumAir()) {
                        inAirTick.put(player.getUniqueId(), inAirTick.getOrDefault(player.getUniqueId(), 0) + 1);
                    } else {
                        if(inAirTick.get(player.getUniqueId()) % 20 == 0){
                            player.damage(1);
                        }
                    }
                    airDitectors.get(player.getUniqueId()).setAirticks(30 * (10 - inAirTick.getOrDefault(player.getUniqueId(), 0) / 30 - 1) + 3);
                }

                // if(airDitectors.get(player.getUniqueId()).getAirticks() == 273)airDitectors.get(player.getUniqueId()).setAirticks(0);

                plugin.getLogger().info("inAirTick : " + inAirTick.getOrDefault(player.getUniqueId(), 0));
                plugin.getLogger().info("Airticks : " + airDitectors.get(player.getUniqueId()).getAirticks());
                plugin.getLogger().info("RemainingAir : " + player.getRemainingAir());
            }
        }
    };
    private final BukkitTask logicTask;

    public PluginLogic(JavaPlugin Plugin) {
        plugin = Plugin;
        logicTask = logic.runTaskTimer(plugin, 0, 1);
    }

    public void putDrownPlayer(Player player) {
        if (airDitectors.containsKey(player.getUniqueId())) return;
        airDitectors.put(player.getUniqueId(), new AirLevelDirector(player));
        remainingAir.putIfAbsent(player.getUniqueId(), 10);
        airDitectors.get(player.getUniqueId()).setAirticks(30 * (remainingAir.get(player.getUniqueId()) - 1) + 3);
        airDitectors.get(player.getUniqueId()).run();
    }

    public void removeDrownPlayer(Player player) {
        if (!airDitectors.containsKey(player.getUniqueId())) return;
        airDitectors.get(player.getUniqueId()).finalize();
        airDitectors.get(player.getUniqueId()).cancel();
        airDitectors.remove(player.getUniqueId());
    }

    public void End() {
        logicTask.cancel();
        logic.cancel();
    }

    @EventHandler
    public void OnPlayerLogin(PlayerJoinEvent e) {
        putDrownPlayer(e.getPlayer());
        plugin.getLogger().info("OnPlayerLogin");
    }

    @EventHandler
    public void OnPlayerLogout(PlayerQuitEvent e) {
        removeDrownPlayer(e.getPlayer());
        plugin.getLogger().info("OnPlayerLogout");
    }

}
