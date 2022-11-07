package me.ftsos.ghostblock.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.ftsos.ghostblock.GhostBlock;
import me.ftsos.ghostblock.utils.Colorizer;
import me.ftsos.ghostblock.visualizer.VisualBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GhostBlockCommand implements CommandExecutor {

    private GhostBlock plugin;
    private HashMap<UUID, HashMap<Location, VisualBlock>> blocks;

    public GhostBlockCommand(GhostBlock plugin) {
        this.plugin = plugin;
        this.blocks = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {



        CommandSender sender = commandSender;

        if (!sender.hasPermission(plugin.getProvider().getLocale().getCommandPermission())) {
            sender.sendMessage(plugin.getProvider().getLocale().getNoPermission());
            return false;
        }

        if (args.length == 2) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                Colorizer.colorize("&cThe player'" + args[0] + "' it's not online", sender);
                return false;
            }

            String blockTypeString = args[1];
            String[] blockTypeStringArray = blockTypeString.split(":");
            int dataId = 0;
            String materialString = "";
            if (blockTypeStringArray.length == 1) {
                materialString = blockTypeStringArray[0];
            } else if (blockTypeStringArray.length == 2) {
                materialString = blockTypeStringArray[0];
                dataId = Integer.parseInt(blockTypeStringArray[1]);
            } else {
                Colorizer.colorize("&cBad material args, the args should look like this 'material_name:data_id', or 'material_name'", sender);
                return false;
            }

            //Optional<XMaterial> materialOptional = XMaterial.matchXMaterial(materialString.toUpperCase());
            //Material material = materialOptional.isPresent() ? materialOptional.get().parseMaterial() : null;
            Material material = Material.matchMaterial(materialString);
            if(material == null) {
                Colorizer.colorize("&cBad material args, the material '" + materialString.toUpperCase() + "' doesn't exist", sender);
                return false;
            }

            Location location = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());
            try {
                sendBlock(player, material, dataId, location);
                Colorizer.colorize(plugin.getProvider().getLocale().getPlacedBlockMessage(), player);
            } catch (Exception ex) {
                Colorizer.colorize(plugin.getProvider().getLocale().getErrorMessage(), sender);
            }

            Colorizer.colorize("&aBlock spawned correctly", sender);
            return true;
        } else if (args.length == 3) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                Colorizer.colorize("&cThe player'" + args[0] + "' it's not online", sender);
                return false;
            }

            String blockTypeString = args[1];
            String[] blockTypeStringArray = blockTypeString.split(":");
            int dataId = 0;
            String materialString = "";
            if (blockTypeStringArray.length == 1) {
                materialString = blockTypeStringArray[0];
            } else if (blockTypeStringArray.length == 2) {
                materialString = blockTypeStringArray[0];
                dataId = Integer.parseInt(blockTypeStringArray[1]);
            } else {
                Colorizer.colorize("&cBad material args, the args should look like this 'material_name:data_id', or 'material_name'", sender);
                return false;
            }

            //Optional<XMaterial> materialOptional = XMaterial.matchXMaterial(materialString.toUpperCase());
            //Material material = materialOptional.isPresent() ? materialOptional.get().parseMaterial() : null;
            Material material = Material.matchMaterial(materialString);

            if(material == null) {
                Colorizer.colorize("&cBad material args, the material '" + materialString.toUpperCase() + "' doesn't exist", sender);
                return false;
            }

            String locationString = args[2];
            String[] locationStringArray = locationString.split(":");
            int blockX = player.getLocation().getBlockX();
            int blockY = player.getLocation().getBlockY() - 1;
            int blockZ = player.getLocation().getBlockZ();

            if (locationStringArray.length == 3) {
                blockX = Integer.parseInt(locationStringArray[0]);
                blockY = Integer.parseInt(locationStringArray[1]);
                // this is for allowing comentary, should be done with #comment
                String blockZCommentString = locationStringArray[2];
                String[] blockZCommentArray = blockZCommentString.split("#");
                if(blockZCommentArray.length == 1) {
                    blockZ = Integer.parseInt(blockZCommentArray[0]);
                } else if(blockZCommentArray.length == 0){
                    Colorizer.colorize("&cZ coord argument not included", sender);
                } else if(blockZCommentArray.length == 2) {
                    blockZ = Integer.parseInt(blockZCommentArray[0]);
                }
            } else {
                Colorizer.colorize("&cBad location args, the args should look like this 'blockX:blockY:blockZ'", sender);
                return false;
            }

            Location location = new Location(player.getWorld(), blockX, blockY, blockZ);
            try {
                sendBlock(player, material, dataId, location);
                Colorizer.colorize(plugin.getProvider().getLocale().getPlacedBlockMessage(), player);
            } catch (Exception ex) {
                Colorizer.colorize(plugin.getProvider().getLocale().getErrorMessage(), sender);
            }

            Colorizer.colorize("&aBlock spawned correctly", sender);
            return true;
        } else if (args.length == 4) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                Colorizer.colorize("&cThe player'" + args[0] + "' it's not online", sender);
                return false;
            }

            String blockTypeString = args[1];
            String[] blockTypeStringArray = blockTypeString.split(":");
            int dataId = 0;
            String materialString = "";
            if (blockTypeStringArray.length == 1) {
                materialString = blockTypeStringArray[0];
            } else if (blockTypeStringArray.length == 2) {
                materialString = blockTypeStringArray[0];
                dataId = Integer.parseInt(blockTypeStringArray[1]);
            } else {
                Colorizer.colorize("&cBad material args, the args should look like this 'material_name:data_id', or 'material_name'", sender);
                return false;
            }

            //Optional<XMaterial> materialOptional = XMaterial.matchXMaterial(materialString.toUpperCase());
            //Material material = materialOptional.isPresent() ? materialOptional.get().parseMaterial() : null;

            Material material = Material.matchMaterial(materialString);


            if(material == null) {
                Colorizer.colorize("&cBad material args, the material '" + materialString.toUpperCase() + "' doesn't exist", sender);
                return false;
            }

            String locationString = args[2];
            String[] locationStringArray = locationString.split(":");
            int blockX = player.getLocation().getBlockX();
            int blockY = player.getLocation().getBlockY() - 1;
            int blockZ = player.getLocation().getBlockZ();

            if (locationStringArray.length == 3) {
                blockX = Integer.parseInt(locationStringArray[0]);
                blockY = Integer.parseInt(locationStringArray[1]);
                blockZ = Integer.parseInt(locationStringArray[2]);
            } else {
                Colorizer.colorize("&cBad location args, the args should look like this 'blockX:blockY:blockZ' (First Coords)", sender);
                return false;
            }

            Location min = new Location(player.getWorld(), blockX, blockY, blockZ);

            String locationStringSecond = args[3];
            String[] locationStringArraySecond = locationStringSecond.split(":");
            int blockX2 = player.getLocation().getBlockX();
            int blockY2 = player.getLocation().getBlockY() - 1;
            int blockZ2 = player.getLocation().getBlockZ();

            if (locationStringArraySecond.length == 3) {
                blockX2 = Integer.parseInt(locationStringArraySecond[0]);
                blockY2 = Integer.parseInt(locationStringArraySecond[1]);
                String blockZCommentString = locationStringArraySecond[2];
                String[] blockZCommentArray = blockZCommentString.split("#");
                if(blockZCommentArray.length == 1) {
                    blockZ2 = Integer.parseInt(blockZCommentArray[0]);
                } else if(blockZCommentArray.length == 0){
                    Colorizer.colorize("&cZ coord argument not included", sender);
                } else if(blockZCommentArray.length == 2) {
                    blockZ2 = Integer.parseInt(blockZCommentArray[0]);
                }
            } else {
                Colorizer.colorize("&cBad location args, the args should look like this 'blockX:blockY:blockZ' (Second Coords)", sender);
                return false;
            }

            Location max = new Location(player.getWorld(), blockX2, blockY2, blockZ2);
            for (int x = (int) Math.ceil(Math.max(max.getBlockX(), min.getBlockX())); x >= (int) Math.floor(Math.min(max.getBlockX(), min.getBlockX())); x--) {
                for (int z = (int) Math.ceil(Math.max(max.getBlockZ(), min.getBlockZ())); z >= (int) Math.floor(Math.min(max.getBlockZ(), min.getBlockZ())); z--) {
                    for (int y = (int) Math.ceil(Math.max(max.getBlockY(), min.getBlockY())); y >= (int) Math.floor(Math.min(max.getBlockY(), min.getBlockY())); y--) {
                        try {
                            sendBlock(player, material, dataId, new Location(player.getWorld(), x, y, z));
                        } catch (Exception ex) {
                            Colorizer.colorize(plugin.getProvider().getLocale().getErrorMessage(), sender);
                        }
                    }
                }
            }


            Colorizer.colorize(plugin.getProvider().getLocale().getPlacedBlockMessage(), player);
            Colorizer.colorize("&aBlock spawned correctly", sender);
            return true;
        } else {
            Colorizer.colorize(plugin.getProvider().getLocale().getCommandUsage(), sender);
            return true;
        }
    }


    public void sendBlock(Player player, Material blockType, int dataId, Location location) {
        ProtocolManager protocolManager = plugin.getProvider().getProtocolManager();

        //Magia
        PacketContainer blockChangePacket = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        WrappedBlockData typeBlock = WrappedBlockData.createData(blockType, dataId);

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        blockChangePacket.getBlockPositionModifier().write(0, blockPosition);
        blockChangePacket.getBlockData().write(0, typeBlock);



                try {
                    addVisualBlock(player.getUniqueId(), new VisualBlock(player.getUniqueId(), blockType, dataId), location);
                    protocolManager.sendServerPacket(player, blockChangePacket);
                    //Colorizer.colorize("&aOops, now your the only one that sees one glass block under your feets", player);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    //Colorizer.colorize("&cThere's a problem sending you the packet, please review the console", player);
                }



    }

    public void sendBlock(Player player, BlockData blockData, Location location) {

        ProtocolManager protocolManager = plugin.getProvider().getProtocolManager();

        //Magia
        PacketContainer blockChangePacket = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        WrappedBlockData typeBlock = WrappedBlockData.createData(blockData);
        //WrappedBlockData typeBlock = WrappedBlockData.createData(blockData);
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        blockChangePacket.getBlockPositionModifier().write(0, blockPosition);
        blockChangePacket.getBlockData().write(0, typeBlock);



        try {
            addVisualBlock(player.getUniqueId(), new VisualBlock(player.getUniqueId(), blockData), location);
            protocolManager.sendServerPacket(player, blockChangePacket);
            //Colorizer.colorize("&aOops, now your the only one that sees one glass block under your feets", player);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            //Colorizer.colorize("&cThere's a problem sending you the packet, please review the console", player);
        }



    }

    @Deprecated
    public void sendBlockSpigot(Player player, BlockData blockData, Location location) {




        try {
            addVisualBlock(player.getUniqueId(), new VisualBlock(player.getUniqueId(), blockData), location);
            player.sendBlockChange(location, blockData);
            //Colorizer.colorize("&aOops, now your the only one that sees one glass block under your feets", player);
        } catch (Exception e) {
            e.printStackTrace();
            //Colorizer.colorize("&cThere's a problem sending you the packet, please review the console", player);
        }



    }

    public void sendBlock(Player player, Material blockType, Location location) {
        ProtocolManager protocolManager = plugin.getProvider().getProtocolManager();

        //Magia
        PacketContainer blockChangePacket = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        WrappedBlockData typeBlock = WrappedBlockData.createData(blockType);

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        blockChangePacket.getBlockPositionModifier().write(0, blockPosition);
        blockChangePacket.getBlockData().write(0, typeBlock);



        try {
            addVisualBlock(player.getUniqueId(), new VisualBlock(player.getUniqueId(), blockType), location);
            protocolManager.sendServerPacket(player, blockChangePacket);
            //Colorizer.colorize("&aOops, now your the only one that sees one glass block under your feets", player);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            //Colorizer.colorize("&cThere's a problem sending you the packet, please review the console", player);
        }



    }

    private void addVisualBlock(UUID player, VisualBlock block, Location location) {
        HashMap<Location, VisualBlock> blocksArray;
        if (!blocks.containsKey(player)) {
            blocksArray = new HashMap<>();
        } else {
            blocksArray = blocks.get(player);
        }
        blocksArray.put(location, block);
        blocks.put(player, blocksArray);
    }

    public VisualBlock getVisualBlock(Player player, Location blockPosition) {
        if (!blocks.containsKey(player.getUniqueId())) {
            return null;
        }

        HashMap<Location, VisualBlock> visualBlocks = blocks.get(player.getUniqueId());
        for (Map.Entry<Location, VisualBlock> set : visualBlocks.entrySet()) {
            VisualBlock block = set.getValue();
            Location location = set.getKey();
            if (location.getBlockX() == blockPosition.getBlockX() &&
                    location.getBlockY() == blockPosition.getBlockY() &&
                    location.getBlockZ() == blockPosition.getBlockZ() &&
                    location.getWorld() == blockPosition.getWorld()
            ) return block;
        }
        return null;
    }

    public boolean hasVisualBlocks(Player player) {
        if (blocks.containsKey(player.getUniqueId())) return true;
        return false;
    }


    public void clearAllVisualBlocks(UUID player) {
        if(!blocks.containsKey(player)) return;
        HashMap<Location, VisualBlock> visualBlocks = blocks.get(player);
        Player playerObj = Bukkit.getPlayer(player);
        for (Map.Entry<Location, VisualBlock> set : visualBlocks.entrySet()) {
            if (playerObj != null){
                Location location = set.getKey();
                Block originalBlock = location.getWorld().getBlockAt(location);
                sendBlock(playerObj, originalBlock.getType(), originalBlock.getData(), location);
        }
    }

        blocks.remove(player);
    }

    public void clearVisualBlockFill(UUID player, Location min, Location max) {
        if(!blocks.containsKey(player)) return;
        HashMap<Location, VisualBlock> visualBlocks = blocks.get(player);
        List<Location> toDeleteLocations = new ArrayList<>();
        Player playerObj = Bukkit.getPlayer(player);
        if(playerObj == null) return;
        for (Map.Entry<Location, VisualBlock> set : visualBlocks.entrySet()) {
            Location blockPosition = set.getKey();
            int firstX = min.getBlockX();
            int firstY = min.getBlockY();
            int firstZ = min.getBlockZ();

            int secondX = max.getBlockX();
            int secondY = max.getBlockY();
            int secondZ = max.getBlockZ();

            int x = blockPosition.getBlockX();
            int y = blockPosition.getBlockY();
            int z = blockPosition.getBlockZ();

            if (x >= (int) Math.floor(Math.min(max.getBlockX(), min.getBlockX())) &&
                    z >= (int) Math.floor(Math.min(max.getBlockZ(), min.getBlockZ())) &&
                    y >= (int) Math.floor(Math.min(max.getBlockY(), min.getBlockY()))
            ){
                Block originalBlock = blockPosition.getWorld().getBlockAt(blockPosition);
                toDeleteLocations.add(blockPosition);
                sendBlock(playerObj, originalBlock.getType(), originalBlock.getData(), blockPosition);
            }
        }

        for(Location location : toDeleteLocations) {
            if(!visualBlocks.containsKey(location)) continue;
            visualBlocks.remove(location);
        }

    }

    public void clearVisualBlock(UUID player, Location location) {
        if(!blocks.containsKey(player)) return;
        HashMap<Location, VisualBlock> visualBlocks = blocks.get(player);
        List<Location> toDeleteLocations = new ArrayList<>();
        Player playerObj = Bukkit.getPlayer(player);
        if(playerObj == null) return;
        for (Map.Entry<Location, VisualBlock> set : visualBlocks.entrySet()) {
            Location blockPosition = set.getKey();
            if (location.getBlockX() == blockPosition.getBlockX() &&
                    location.getBlockY() == blockPosition.getBlockY() &&
                    location.getBlockZ() == blockPosition.getBlockZ() &&
                    location.getWorld() == blockPosition.getWorld()){
                Block originalBlock = location.getWorld().getBlockAt(location);
                toDeleteLocations.add(blockPosition);
                sendBlock(playerObj, originalBlock.getType(), originalBlock.getData(), location);

            }
        }

        for(Location locationToDelete : toDeleteLocations) {
            if(!visualBlocks.containsKey(locationToDelete)) continue;
            visualBlocks.remove(locationToDelete);
        }

    }

    public void clearBlocks(UUID player) {
        if(!blocks.containsKey(player)) return;
        blocks.remove(player);
    }

    public HashMap<UUID, HashMap<Location, VisualBlock>> getBlocks() {
        return blocks;
    }
}

