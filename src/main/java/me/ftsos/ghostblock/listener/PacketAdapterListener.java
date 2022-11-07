package me.ftsos.ghostblock.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.ftsos.ghostblock.GhostBlock;
import me.ftsos.ghostblock.visualizer.VisualBlock;
import me.ftsos.ghostblock.packetwrappers.WrapperPlayServerBlockChange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;


public class PacketAdapterListener extends PacketAdapter {
    private GhostBlock plugin;
    public PacketAdapterListener(GhostBlock plugin) {
        super(plugin, ListenerPriority.HIGHEST,
                PacketType.Play.Server.BLOCK_CHANGE);
        this.plugin = plugin;
    }

    public void onPacketSending(PacketEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {

            WrapperPlayServerBlockChange wrapper = new WrapperPlayServerBlockChange(event.getPacket());
            BlockPosition blockLocation = wrapper.getLocation();
            //Bukkit.broadcastMessage(event.getPacket().toString());
            if (hasBeenFaked(event.getPlayer(), blockLocation)) {
                VisualBlock block = getVisualBlock(event.getPlayer(), blockLocation);
                WrappedBlockData newBlockData;
                if(block.hasDataId()) {
                    //Bukkit.broadcastMessage("hasID");
                    newBlockData = WrappedBlockData.createData(block.getType(), block.getData());
                    //event.getPlayer().sendBlockChange(toLocation(event.getPlayer().getWorld(), blockLocation), block.getType(), ((byte)((int)block.getData())));
                } else {
                    newBlockData = WrappedBlockData.createData(block.getBlockData());
                    //if(block.getBlockData() instanceof MultipleFacing) Bukkit.broadcastMessage("multiple facing");
                    //event.getPlayer().sendBlockChange(toLocation(event.getPlayer().getWorld(), blockLocation), block.getBlockData());
                }

                //newBlockData.setTypeAndData(block.getType(), block.getData());
                /*Bukkit.broadcastMessage("Before Before Data (BlockData) " + block.getBlockData().toString());
                Bukkit.broadcastMessage("Before thing " + newBlockData.toString());
                Bukkit.broadcastMessage("First thing " + wrapper.getBlockData().toString());*/
                wrapper.setBlockData(newBlockData);
                //Bukkit.broadcastMessage("2 " + wrapper.getBlockData().toString());
                /*PacketContainer blockChangePacket = plugin.getProvider().getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_CHANGE);
                blockChangePacket.getBlockPositionModifier().write(0, blockLocation);
                blockChangePacket.getBlockData().write(0, newBlockData);
                Bukkit.broadcastMessage("3 " + blockChangePacket.getBlockData().read(0).toString());
                event.setPacket(blockChangePacket);
                Bukkit.broadcastMessage( "4 "+ event.getPacket().getBlockData().read(0).toString());*/
                //event.s
            }
        }
    }

    /*
     * A helper method to determine if a block at a location has been faked
     */
    private boolean hasBeenFaked(Player player, BlockPosition location) {
        if(getVisualBlock(player, location) == null) return false;
        return true;
    }

    private VisualBlock getVisualBlock(Player player, BlockPosition blockPosition) {
        Location location = new Location(player.getWorld(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        return plugin.getGhostBlockManager().getVisualBlock(player, location);
    }

    private Location toLocation(World world, BlockPosition blockPosition) {
        return new Location(world, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }
}
