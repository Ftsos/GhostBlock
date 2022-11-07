package me.ftsos.ghostblock;

import me.ftsos.ghostblock.commands.GhostBlockClearCommand;
import me.ftsos.ghostblock.commands.GhostBlockCommand;
import me.ftsos.ghostblock.commands.GhostBlockSchematicCommand;
import me.ftsos.ghostblock.listener.CoreListener;
import me.ftsos.ghostblock.listener.PacketAdapterListener;
import me.ftsos.ghostblock.utils.Provider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GhostBlock extends JavaPlugin {
    private Provider provider;
    private GhostBlockCommand ghostBlockManager;
    @Override
    public void onEnable() {
        this.provider = new Provider(this);
        this.saveDefaultConfig();
        registerCommands();
        registerListeners();
    }

    public void registerCommands() {
        this.ghostBlockManager = new GhostBlockCommand(this);
        this.getCommand("ghostblock").setExecutor(ghostBlockManager);
        this.getCommand("ghostblockclear").setExecutor(new GhostBlockClearCommand(this));
        this.getCommand("ghostblockschematic").setExecutor(new GhostBlockSchematicCommand(this));
    }

    public void registerListeners() {
        PacketAdapterListener packetAdapterListener = new PacketAdapterListener(this);
       this.provider.getProtocolManager().addPacketListener(packetAdapterListener);
        this.getServer().getPluginManager().registerEvents(new CoreListener(this), this);
    }

    public GhostBlockCommand getGhostBlockManager() {
        return ghostBlockManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Provider getProvider() {
        return provider;
    }
}
