package net.zoizoi.plugin.drownair.logic;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PluginLogic/* implements Listener*/ {
    private final HashMap<UUID, AirLevelDirector> airDitectors = new HashMap<>();

    private BukkitRunnable logic = new BukkitRunnable() {
        @Override
        public void run() {
            for (AirLevelDirector ditector : airDitectors.values()) {
                Player player = ditector.getTarget();
            }
        }
    };

    public PluginLogic() {
    }

    public void putDrownPlayer(Player player) {
        if (airDitectors.containsKey(player.getUniqueId())) return;
        airDitectors.put(player.getUniqueId(), new AirLevelDirector(player));
        airDitectors.get(player.getUniqueId()).setAirticks(30 * (5 - 1) + 3);
        airDitectors.get(player.getUniqueId()).run();
    }

    public void removeDrownPlayer(Player player) {
        if (!airDitectors.containsKey(player.getUniqueId())) return;
        airDitectors.get(player.getUniqueId()).finalize();
        airDitectors.get(player.getUniqueId()).cancel();
        airDitectors.remove(player.getUniqueId());
    }
}
