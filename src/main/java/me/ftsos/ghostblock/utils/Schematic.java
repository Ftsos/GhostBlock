package me.ftsos.ghostblock.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BaseBlock;
import me.ftsos.ghostblock.GhostBlock;
import me.ftsos.ghostblock.utils.materials.BlockDataMaterial;
import me.ftsos.ghostblock.utils.materials.DirectionalBlockDataMaterial;
import me.ftsos.ghostblock.utils.materials.MultipleFacingBlockDataMaterial;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Schematic {

    private final GhostBlock plugin;

    private final File schematic;
    private final List<BaseBlock> blocks;
    private Clipboard clipboard;

    /**
     * @param plugin your plugin instance
     * @param schematic file to the schematic
     */
    public Schematic(final GhostBlock plugin, final File schematic) {
        this.plugin = plugin;
        this.schematic = schematic;
        this.blocks = new ArrayList<>();

        // Read and cache
        try (FileInputStream inputStream = new FileInputStream(schematic)) {
            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
            ClipboardReader reader = format.getReader(inputStream);
            this.clipboard = reader.read();

            // Get all blocks in the schematic
            final BlockVector3 minimumPoint = clipboard.getMinimumPoint();
            final BlockVector3 maximumPoint = clipboard.getMaximumPoint();
            final int minX = minimumPoint.getX();
            final int maxX = maximumPoint.getX();
            final int minY = minimumPoint.getY();
            final int maxY = maximumPoint.getY();
            final int minZ = minimumPoint.getZ();
            final int maxZ = maximumPoint.getZ();

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        final BlockVector3 at = BlockVector3.at(x, y, z);
                        BaseBlock block = clipboard.getFullBlock(at);
                        blocks.add(block);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pastes a schematic, with a specified time
     * @param paster player pasting
     * @return collection of locations where schematic blocks will be pasted, null if schematic locations will replace blocks
     */
    @Nullable
    public Map<Location, BaseBlock> getBlocks(final Location loc, final Player paster) {
        final Map<Location, BaseBlock> pasteBlocks = new LinkedHashMap<>();
        final BlockFace face = paster.getFacing();
        try {
            //final Data tracker = new Data();

            // Rotate based off the player's facing direction
            double yaw = paster.getEyeLocation().getYaw();
            // So unfortunately, WorldEdit doesn't support anything other than multiples of 90.
            // Here we round it to the nearest multiple of 90.
            yaw = MathsUtil.roundHalfUp((int) yaw, 90);
            // Apply the rotation to the clipboard
            final ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
            //clipboardHolder.setTransform(new AffineTransform().rotateY(yaw));
            final Clipboard transformedClipboard = clipboardHolder.getClipboard();

            // Get all blocks in the schematic
            final BlockVector3 minimumPoint = transformedClipboard.getMinimumPoint();
            final BlockVector3 maximumPoint = transformedClipboard.getMaximumPoint();
            final int minX = minimumPoint.getX();
            final int maxX = maximumPoint.getX();
            final int minY = minimumPoint.getY();
            final int maxY = maximumPoint.getY();
            final int minZ = minimumPoint.getZ();
            final int maxZ = maximumPoint.getZ();

            final int width = transformedClipboard.getRegion().getWidth();
            final int height = transformedClipboard.getRegion().getHeight();
            final int length = transformedClipboard.getRegion().getLength();
            final int widthCentre = width / 2;
            final int heightCentre = height / 2;
            final int lengthCentre = length / 2;


            int minBlockY = loc.getWorld().getMaxHeight();
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        final BlockVector3 at = BlockVector3.at(x, y, z);
                        BaseBlock block = transformedClipboard.getFullBlock(at);
                        // Ignore air blocks, change if you want
                        if (block.getBlockType().getMaterial().isAir()) continue;

                        // Here we find the relative offset based off the current location.
                        final double offsetX = Math.abs(maxX - x);
                        final double offsetY = Math.abs(maxY - y);
                        final double offsetZ = Math.abs(maxZ - z);

                        final Location offsetLoc = loc.clone().subtract(offsetX - widthCentre, offsetY - heightCentre, offsetZ - lengthCentre);
                        if (offsetLoc.getBlockY() < minBlockY) minBlockY = offsetLoc.getBlockY();


                        pasteBlocks.put(offsetLoc, block);
                    }
                }
            }

            /*
             * Verify location of pasting
             */
            /*boolean validated = true;
            for (Location validate : pasteBlocks.keySet()) {
                final BaseBlock baseBlock = pasteBlocks.get(validate);
                final boolean isWater = validate.clone().subtract(0, 1, 0).getBlock().getType() == Material.WATER;
                final boolean isAir = minBlockY == validate.getBlockY() && validate.clone().subtract(0, 1, 0).getBlock().getType().isAir();
                final boolean isSolid = validate.getBlock().getType().isSolid();
                final boolean isTransparent = options.contains(Options.IGNORE_TRANSPARENT) && validate.getBlock().isPassable() && !validate.getBlock().getType().isAir();*/

                //if (!options.contains(Options.PLACE_ANYWHERE) && (isWater || isAir || isSolid) && !isTransparent) {
                    // Show fake block where block is interfering with schematic
                 //   if (options.contains(Options.USE_GAME_MARKER)) {
                //        PacketSender.sendBlockHighlight(paster, validate, Color.RED, 51);
               //     } else paster.sendBlockChange(validate, Material.RED_STAINED_GLASS.createBlockData());
                //    validated = false;
             //   } else {
                    // Show fake block for air
             //       if (options.contains(Options.USE_FAKE_BLOCKS)) {
             //           paster.sendBlockChange(validate, BukkitAdapter.adapt(baseBlock));
             //       } else if (options.contains(Options.USE_GAME_MARKER)) {
            //            PacketSender.sendBlockHighlight(paster, validate, Color.GREEN, 51);
             //       } else paster.sendBlockChange(validate, Material.GREEN_STAINED_GLASS.createBlockData());
           //     }

              //  if (!options.contains(Options.PREVIEW) && !options.contains(Options.USE_GAME_MARKER)) {
              //      Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            //            if (validate.getBlock().getType() == Material.AIR) paster.sendBlockChange(validate.getBlock().getLocation(), Material.AIR.createBlockData());
              //      }, 60);
             //   }
          //  }

          //  if (options.contains(Options.PREVIEW)) return new ArrayList<>();
          //  if (!validated) return null;

         //   if (options.contains(Options.REALISTIC)) {
          //      Map<Location, BaseBlock> sorted
         //               = pasteBlocks.entrySet()
         //               .stream()
          //              .sorted(Comparator.comparingInt(i -> i.getKey().getBlockY()))
         //               .collect(Collectors.toMap(
          //                      Map.Entry::getKey,
          //                      Map.Entry::getValue,
         //                       (e1, e2) -> e1, LinkedHashMap::new));
        //        pasteBlocks.clear();
         //       pasteBlocks.putAll(sorted);
           // }
            // Start pasting each block every tick
            //final AtomicReference<Task> task = new AtomicReference<>();

           // tracker.trackCurrentBlock = 0;

            /*unnable pasteTask = () -> {
                // Get the block, set the type, data, and then update the state.
                Location key = (Location) pasteBlocks.keySet().toArray()[tracker.trackCurrentBlock];
                final BlockData data = BukkitAdapter.adapt(pasteBlocks.get(key));
                final Block block = key.getBlock();
                block.setType(data.getMaterial(), false);
                block.setBlockData(data);

                block.getState().update(true, false);*/

                // Play block effects. Change to what you want.
                /*block.getLocation().getWorld().spawnParticle(Particle.CLOUD, block.getLocation(), 6);
                block.getLocation().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());

                tracker.trackCurrentBlock++;

                if (tracker.trackCurrentBlock >= pasteBlocks.size()) {
                    task.get().stop();
                    tracker.trackCurrentBlock = 0;
                }*/
           // };

            //ask.set(TaskBuilder.newBuilder().sync().every(time).run(pasteTask));
            return pasteBlocks;
        } catch (final Exception e) {
            e.printStackTrace();
        } return null;
    }




}