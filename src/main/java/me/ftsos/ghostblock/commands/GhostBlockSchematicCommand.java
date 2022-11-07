package me.ftsos.ghostblock.commands;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import me.ftsos.ghostblock.GhostBlock;
import me.ftsos.ghostblock.utils.Colorizer;
import me.ftsos.ghostblock.utils.Schematic;
import me.ftsos.ghostblock.utils.materials.BlockDataMaterial;
import me.ftsos.ghostblock.utils.materials.DirectionalBlockDataMaterial;
import me.ftsos.ghostblock.utils.materials.MultipleFacingBlockDataMaterial;
import me.ftsos.ghostblock.visualizer.VisualBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class GhostBlockSchematicCommand implements CommandExecutor {
    private GhostBlock plugin;
    private WorldEdit worldEdit;

    public GhostBlockSchematicCommand(GhostBlock plugin) {
        this.plugin = plugin;
        this.worldEdit = plugin.getProvider().getWorldEdit();
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {

        if(!sender.hasPermission(plugin.getProvider().getLocale().getSchematicCommandPermission())) {
            Colorizer.colorize(plugin.getProvider().getLocale().getNoPermission(), sender);
            return false;
        }

        if(args.length != 3) {
            Colorizer.colorize(plugin.getProvider().getLocale().getSchematicCommandUsage(), sender);
            return false;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            Colorizer.colorize("&cThe player'" + args[0] + "' it's not online", sender);
            return false;
        }

        File file = new File(plugin.getDataFolder(), "schematics/" + args[2]);
        Schematic schematic = new Schematic(plugin, file);
        String locationString = args[1];
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
            pasteSchematic(location, player, schematic);
            Colorizer.colorize(plugin.getProvider().getLocale().getSuccessfulSchematicSent(), sender);
        } catch (Exception exception) {
            Colorizer.colorize(plugin.getProvider().getLocale().getSchematicCommandError(), sender);
            exception.printStackTrace();
        }

        return true;
    }
    /**
     * Pastes a schematic, with the time defaulting to 1 block per second
     * @param location location to paste from
     * @param player player pasting
     * @return list of locations where schematic blocks will be pasted, null if schematic locations will replace blocks
     */
    public void pasteSchematic(final Location location, Player player, Schematic schematic) {

        final Map<Location, BaseBlock> pasteBlocks = schematic.getBlocks(location, player);
        List<VisualMultipleFacingBlockData> multipleFacingBlocks = new ArrayList<>();

        for (Map.Entry<Location, BaseBlock> entry : pasteBlocks.entrySet()) {
            if(entry == null) continue;
            if(entry.getKey() == null) continue;
            if(entry.getValue() == null) continue;
            Location blockLocation = entry.getKey();
            BlockData blockData = BukkitAdapter.adapt(entry.getValue());
            //BlockDataMaterial blockDataMaterial = null;
            //BlockData newBlockData
            //BlockState blockState = BukkitAdapter.adapt(blockData);
           // BlockData newBlockData = blockData.getMaterial().createBlockData((bd) -> ((Directional)bd).setFacing(Block));
          //  if (blockData instanceof Directional) {
                //blockDataMaterial = new DirectionalBlockDataMaterial(block.getState());
          //  } else if (blockData instanceof MultipleFacing) {
         //       blockDataMaterial = new MultipleFacingBlockDataMaterial(block.getState());
         //   }

            if(blockData.getMaterial().isLegacy()) {
                plugin.getGhostBlockManager().sendBlock(player, blockData.getMaterial(), blockData.getMaterial().getId(), blockLocation);
            } else {
                //Bukkit.broadcastMessage("not legacy");
                //if(blockData instanceof Directional) Bukkit.broadcastMessage("Directional " + ((Directional) blockData).getFacing());
                if(blockData instanceof MultipleFacing) {
                    //Bukkit.broadcastMessage("MultipleFacing " + ((MultipleFacing) blockData).getAllowedFaces());
                    MultipleFacing multipleFacing = (MultipleFacing) blockData;

                    multipleFacingBlocks.add(new VisualMultipleFacingBlockData(multipleFacing, blockLocation));
                    //plugin.getGhostBlockManager().sendBlock(paster, blockData, blockLocation);

                } else {
                    plugin.getGhostBlockManager().sendBlock(player, blockData, blockLocation);
                }
            }

        }

        for(VisualMultipleFacingBlockData visualMultipleFacingBlockData : multipleFacingBlocks) {
            MultipleFacing multipleFacing = visualMultipleFacingBlockData.getMultipleFacing();
            Location blockLocation = visualMultipleFacingBlockData.getBlockLocation();

            multipleFacing.getAllowedFaces().forEach(bf -> {
                Location locationBlockRelative = blockLocation.getBlock().getRelative(bf).getLocation();
                //if(plugin.getGhostBlockManager().getVisualBlock(player, locationBlockRelative) != null)
                VisualBlock fakeBlock = plugin.getGhostBlockManager().getVisualBlock(player, locationBlockRelative);

                Material rel = fakeBlock == null ? blockLocation.getBlock().getRelative(bf).getType() : fakeBlock.getType();
                if (rel == Material.AIR
                        || rel.toString().contains("SLAB")) {
                    if (multipleFacing.hasFace(bf)) multipleFacing.setFace(bf, false);
                } else {
                    if (!rel.toString().contains("SLAB")
                            && !Tag.ANVIL.getValues().contains(rel)
                            && rel.isSolid()
                            && rel.isBlock()) {
                        if (!multipleFacing.hasFace(bf)) multipleFacing.setFace(bf, true);
                    }
                }
            });

            //Bukkit.broadcastMessage("Multiple Facing " + blockLocation.getBlockX() + " " + blockLocation.getBlockY() + " " + blockLocation.getBlockZ() + " " + multipleFacing.getFaces());
           //player.sendBlockChange(blockLocation, multipleFacing);
            plugin.getGhostBlockManager().sendBlock(player, multipleFacing, blockLocation);
        }

    }
}

class VisualMultipleFacingBlockData {
    private MultipleFacing multipleFacing;
    private Location blockLocation;

    public VisualMultipleFacingBlockData(MultipleFacing multipleFacing, Location blockLocation) {
        this.multipleFacing = multipleFacing;
        this.blockLocation = blockLocation;
    }

    public MultipleFacing getMultipleFacing() {
        return multipleFacing;
    }

    public Location getBlockLocation() {
        return blockLocation;
    }

}