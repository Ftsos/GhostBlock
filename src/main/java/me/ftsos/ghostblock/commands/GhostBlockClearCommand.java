package me.ftsos.ghostblock.commands;

import me.ftsos.ghostblock.GhostBlock;
import me.ftsos.ghostblock.utils.Colorizer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class GhostBlockClearCommand implements CommandExecutor {
    private GhostBlock plugin;

    public GhostBlockClearCommand(GhostBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!sender.hasPermission(plugin.getProvider().getLocale().getClearCommandPermission())) {
            sender.sendMessage(plugin.getProvider().getLocale().getNoPermission());
            return false;
        }

        /*if(args.length == 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) {
                Colorizer.colorize("&cThe player '" + args[0] + "' isn't online", sender);
                return false;
            }

            plugin.getGhostBlockManager().clearVisualBlocks(player.getUniqueId());
            Colorizer.colorize(plugin.getProvider().getLocale().getBlockRemovedMessage(), sender);
            return true;
        }*/

        if(args.length == 2) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) {
                Colorizer.colorize("&cThe player '" + args[0] + "' isn't online", sender);
                return false;
            }

            String locationString = args[1];
            String[] locationStringArray = locationString.split(":");
            int blockX = player.getLocation().getBlockX();
            int blockY = player.getLocation().getBlockY() - 1;
            int blockZ = player.getLocation().getBlockZ();

            if (locationStringArray.length == 3) {
                blockX = Integer.parseInt(locationStringArray[0]);
                blockY = Integer.parseInt(locationStringArray[1]);
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

            plugin.getGhostBlockManager().clearVisualBlock(player.getUniqueId(), location);
            Colorizer.colorize(plugin.getProvider().getLocale().getBlockRemovedMessage(), sender);
            return true;

        }

        if(args.length == 3) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) {
                Colorizer.colorize("&cThe player '" + args[0] + "' isn't online", sender);
                return false;
            }

            String firstLocationString = args[1];
            String[] firstLocationStringArray = firstLocationString.split(":");
            int firstBlockX = player.getLocation().getBlockX();
            int firstBlockY = player.getLocation().getBlockY() - 1;
            int firstBlockZ = player.getLocation().getBlockZ();

            if (firstLocationStringArray.length == 3) {
                firstBlockX = Integer.parseInt(firstLocationStringArray[0]);
                firstBlockY = Integer.parseInt(firstLocationStringArray[1]);
                firstBlockZ = Integer.parseInt(firstLocationStringArray[2]);
            } else {
                Colorizer.colorize("&cBad location args, the args should look like this 'blockX:blockY:blockZ'", sender);
                return false;
            }

            String secondLocationString = args[1];
            String[] secondLocationStringArray = secondLocationString.split(":");
            int secondBlockX = player.getLocation().getBlockX();
            int secondBlockY = player.getLocation().getBlockY() - 1;
            int secondBlockZ = player.getLocation().getBlockZ();

            if (secondLocationStringArray.length == 3) {
                secondBlockX = Integer.parseInt(secondLocationStringArray[0]);
                secondBlockY = Integer.parseInt(secondLocationStringArray[1]);
                String blockZCommentString = secondLocationStringArray[2];
                String[] blockZCommentArray = blockZCommentString.split("#");
                if(blockZCommentArray.length == 1) {
                    secondBlockZ = Integer.parseInt(blockZCommentArray[0]);
                } else if(blockZCommentArray.length == 0){
                    Colorizer.colorize("&cZ coord argument not included", sender);
                } else if(blockZCommentArray.length == 2) {
                    secondBlockZ = Integer.parseInt(blockZCommentArray[0]);
                }
            } else {
                Colorizer.colorize("&cBad location args, the args should look like this 'blockX:blockY:blockZ'", sender);
                return false;
            }

            Location firstLocation = new Location(player.getWorld(), firstBlockX, firstBlockY, firstBlockZ);
            Location secondLocation = new Location(player.getWorld(), secondBlockX, secondBlockY, secondBlockZ);
            try {
                plugin.getGhostBlockManager().clearVisualBlockFill(player.getUniqueId(), firstLocation, secondLocation);
            } catch (Exception e) {
                Colorizer.colorize(plugin.getProvider().getLocale().getClearCommandErrorMessage(), sender);
                e.printStackTrace();
            }
            Colorizer.colorize(plugin.getProvider().getLocale().getBlockRemovedMessage(), sender);
            return true;
        }

        sender.sendMessage(plugin.getProvider().getLocale().getClearCommandUsage());
        return true;
    }
}
