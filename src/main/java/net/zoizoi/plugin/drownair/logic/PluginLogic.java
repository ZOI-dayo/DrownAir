package net.zoizoi.plugin.drownair.logic;

import org.bukkit.Material;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PluginLogic implements Listener {
    private final JavaPlugin plugin;
    private final HashMap<UUID, AirLevelDirector> airDirectors = new HashMap<>();
    private final HashMap<UUID, Integer> inAirTick = new HashMap<>();

    private final BukkitRunnable logic = new BukkitRunnable() {
        // private final HashMap<UUID, Integer> inAirTick = new HashMap<>();

        @Override
        public void run() {
            for (AirLevelDirector director : airDirectors.values()) {
                Player player = director.getTarget();
                player.setRemainingAir(player.getMaximumAir());
                boolean isWaterlogged = false;
                if(player.getEyeLocation().getBlock().getBlockData() instanceof Waterlogged) isWaterlogged = ((Waterlogged)player.getEyeLocation().getBlock().getBlockData()).isWaterlogged();
                Material block = player.getEyeLocation().getBlock().getType();
                // plugin.getLogger().info(block.name());

                if (isWaterlogged || block == Material.WATER || block == Material.KELP || block == Material.KELP_PLANT || block == Material.SEAGRASS || block == Material.TALL_SEAGRASS || block == Material.BUBBLE_COLUMN) {
                    inAirTick.put(player.getUniqueId(), 0);

                    airDirectors.get(player.getUniqueId()).setAirticks(0);
                } else {
                    inAirTick.put(player.getUniqueId(), inAirTick.getOrDefault(player.getUniqueId(), 0) + 1);
                    if (inAirTick.getOrDefault(player.getUniqueId(), 0) > player.getMaximumAir()) {
                        if(inAirTick.get(player.getUniqueId()) % 20 == 0){
                            player.damage(2);
                            // plugin.getLogger().info("inAirTick.get(player.getUniqueId()) : " + inAirTick.get(player.getUniqueId()));
                        }
                    }
                    airDirectors.get(player.getUniqueId()).setAirticks(30 * (10 - inAirTick.getOrDefault(player.getUniqueId(), 0) / 30 - 1) + 3);
                }

                // if(airDirectors.get(player.getUniqueId()).getAirticks() == 273)airDirectors.get(player.getUniqueId()).setAirticks(0);
/*
                plugin.getLogger().info("inAirTick : " + inAirTick.getOrDefault(player.getUniqueId(), 0));
                plugin.getLogger().info("Airticks : " + airDirectors.get(player.getUniqueId()).getAirticks());
                plugin.getLogger().info("RemainingAir : " + player.getRemainingAir());

 */
            }
        }
    };
    private final BukkitTask logicTask;

    public PluginLogic(JavaPlugin Plugin) {
        plugin = Plugin;
        logicTask = logic.runTaskTimer(plugin, 0, 1);
    }

    public void putDrownPlayer(Player player) {
        if (airDirectors.containsKey(player.getUniqueId())) return;
        airDirectors.put(player.getUniqueId(), new AirLevelDirector(player));
        inAirTick.putIfAbsent(player.getUniqueId(), 0);
        airDirectors.get(player.getUniqueId()).setAirticks(30 * (inAirTick.get(player.getUniqueId()) / 30 - 1) + 3);
        airDirectors.get(player.getUniqueId()).run();
    }

    public void removeDrownPlayer(Player player) {
        if (!airDirectors.containsKey(player.getUniqueId())) return;
        airDirectors.get(player.getUniqueId()).finalize();
        airDirectors.get(player.getUniqueId()).cancel();
        airDirectors.remove(player.getUniqueId());
    }

    public void End() {
        logicTask.cancel();
        logic.cancel();
    }

    @EventHandler
    public void OnPlayerLogin(PlayerJoinEvent e) {
        putDrownPlayer(e.getPlayer());
        // plugin.getLogger().info("OnPlayerLogin");
    }

    @EventHandler
    public void OnPlayerLogout(PlayerQuitEvent e) {
        removeDrownPlayer(e.getPlayer());
        // plugin.getLogger().info("OnPlayerLogout");
    }

    @EventHandler
    public void OnPlayerRespawn(PlayerRespawnEvent e){
        if(airDirectors.containsKey(e.getPlayer().getUniqueId())) inAirTick.put(e.getPlayer().getUniqueId(), 0);
    }

}
