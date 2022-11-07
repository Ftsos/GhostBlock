package me.ftsos.ghostblock.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.hakan.core.HCore;
import com.sk89q.worldedit.WorldEdit;
import me.ftsos.ghostblock.GhostBlock;

public class Provider {
    private GhostBlock plugin;
    private ProtocolManager protocolManager;
    private WorldEdit worldEdit;
    private Logger logger;
    private Locale locale;
    public Provider(GhostBlock plugin) {
        this.plugin = plugin;
        registerClasses();
    }

    public void registerClasses() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.worldEdit = WorldEdit.getInstance();
        this.logger = new Logger(plugin);
        this.locale = new Locale(plugin);
        HCore.initialize(plugin);
    }

    public WorldEdit getWorldEdit() {
        return worldEdit;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public Logger getLogger() {
        return logger;
    }

    public Locale getLocale() {
        return locale;
    }
}
