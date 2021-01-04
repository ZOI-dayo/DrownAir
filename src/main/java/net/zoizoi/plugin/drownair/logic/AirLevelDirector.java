package net.zoizoi.plugin.drownair.logic;

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AirLevelDirector extends BukkitRunnable implements PacketListener {

    // our variables
    private final WrappedDataWatcher.WrappedDataWatcherObject ADDRESS;
    private final Player target;
    private int airticks; // 30 * (x - 1) + 3 [x : the number of bubbles]

    public AirLevelDirector(Player target) {
        // set the target
        this.target = target;

        // this is our "address" where we will be writing
        ADDRESS = new WrappedDataWatcher.WrappedDataWatcherObject(1, WrappedDataWatcher.Registry.get(Integer.class));

        // adds the listener to ProtocolLib
        ProtocolLibrary.getProtocolManager().addPacketListener(this);

        // schedules the task, async, each tick
        runTaskTimerAsynchronously(getPlugin(), 0, 1);
    }

    @Override
    protected void finalize() {
        destroy();
    }

    public void destroy() {
        // remove listener
        ProtocolLibrary.getProtocolManager().removePacketListener(this);

        // cancel event, if not cancelled already
        if (!isCancelled())
            cancel();
    }

    public int getAirticks() {
        return airticks;
    }

    public void setAirticks(int airticks) {
        this.airticks = airticks;
    }

    public Player getTarget() {
        return target;
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        // check player
        if (e.getPlayer() != target)
            return;

        // get wrapper that helps us get the data
        WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata(e.getPacket());

        // create the watcher that safely overrides the data
        WrappedDataWatcher watcher = new WrappedDataWatcher(wrapper.getMetadata());

        // override the desired address
        watcher.setObject(ADDRESS, airticks);

        // apply changes pack to the wrapper
        wrapper.setMetadata(watcher.getWatchableObjects());
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        // unused
    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        // listens to entity metadata
        return ListeningWhitelist.newBuilder().types(PacketType.Play.Server.ENTITY_METADATA).build();
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        // we won't be listening to packets from a client
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    @Override
    public Plugin getPlugin() {
        // this is my plugin. for testing purposes I will get it like this
        // you should probably have access to your plugin's instance
        // because it will be faster
        return Bukkit.getPluginManager().getPlugin("DrownAir");
    }

    @Override
    public void run() {
        // send a dummy packed
        // we will listen to this packet
        // and we will override it at "onPacketSending(PacketEvent)"
        WrapperPlayServerEntityMetadata w = new WrapperPlayServerEntityMetadata();
        w.setEntityID(target.getEntityId());
        w.sendPacket(target);

        // check the player's status
        if (target.isOnline()) return;

        // player is offline
        cancel();
        destroy();
    }

}
