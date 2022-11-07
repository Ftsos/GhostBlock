package me.ftsos.ghostblock.visualizer;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.UUID;

public class VisualBlock {
    private UUID player;
    //private Material blockType;
    private Integer dataId;
    private BlockData blockData;

    public VisualBlock(UUID player, Material blockType, int dataId) {
        this.player = player;
        this.blockData = blockType.createBlockData();
        this.dataId = dataId;
        /*this.blockType = blockType;
        this.dataId = dataId;*/
    }

    public VisualBlock(UUID player, BlockData blockData) {
        this.player = player;
        this.blockData = blockData;
        this.dataId = null;
    }

    public VisualBlock(UUID player, Material blockType) {
        this.player = player;
        this.blockData = blockType.createBlockData();
        this.dataId = null;
    }

    public UUID getPlayer() {
        return player;
    }

    public Material getType() {
        return blockData.getMaterial();
    }

    public boolean hasDataId() {
        if(dataId == null) return false;
        return true;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public Integer getData() {
        if(dataId == null) return null;
        return dataId;
    }

}
