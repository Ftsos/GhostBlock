package me.ftsos.ghostblock.commands;

import com.hakan.core.HCore;
import com.hakan.core.npc.HNPC;
import me.ftsos.ghostblock.GhostBlock;
import me.ftsos.ghostblock.utils.Colorizer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GhostNPCCommand implements CommandExecutor {
    private GhostBlock plugin;

    public GhostNPCCommand(GhostBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(!sender.hasPermission(plugin.getProvider().getLocale().getSchematicCommandPermission())) {
            Colorizer.colorize(plugin.getProvider().getLocale().getNoPermission(), sender);
            return false;
        }

        //command location skin title players

        if(args.length < 4) {
            Colorizer.colorize(plugin.getProvider().getLocale().getSchematicCommandUsage(), sender);
            return false;
        }

        /*Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            Colorizer.colorize("&cThe player'" + args[0] + "' it's not online", sender);
            return false;
        }*/

        String locationString = args[0];
        String[] locationStringArray = locationString.split(":");
        int blockX = 0;
        int blockY = 0;
        int blockZ = 0;

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

        String skin = args[1];

        String title = args[2];

        List<Player> players = new ArrayList<>();

        for (int i = 3; i < args.length; i++) {
            Player player = Bukkit.getPlayer(args[i]);
            if(player != null) {
                players.add(player);
            }
        }

        //There's gotta be at least one player

        Location location = new Location(players.get(0).getWorld(), blockX, blockY, blockZ);

        createAndSaveNpc(players, location, Arrays.asList(title), skin);

        return false;
    }

    public UUID createNpc(List<Player> playersToShow, Location location, List<String> hologramTitles, String skin) {
        UUID id = UUID.randomUUID();
        createNpc(playersToShow, location, hologramTitles, skin, id);
        return id;
    }

    public HNPC createAndGetNpc(List<Player> playersToShow, Location location, List<String> hologramTitles, String skin) {
        UUID id = UUID.randomUUID();
        HNPC npc = HCore.npcBuilder(id.toString())
                .showEveryone(false)
                .location(location)
                .lines(hologramTitles)
                .skin(skin)
                .build();

        npc.addViewer(playersToShow);
        return npc;
    }

    public HNPC createAndSaveNpc(List<Player> playersToShow, Location location, List<String> hologramTitles, String skin) {
        UUID id = UUID.randomUUID();
        HNPC npc = HCore.npcBuilder(id.toString())
                .showEveryone(false)
                .location(location)
                .lines(hologramTitles)
                .skin(skin)
                .build();

        npc.addViewer(playersToShow);

        return npc;
    }

    public void createNpc(List<Player> playersToShow, Location location, List<String> hologramTitles, String skin, UUID id) {

        HNPC npc = HCore.npcBuilder(id.toString())
                .showEveryone(false)
                .location(location)
                .lines(hologramTitles)
                .skin(skin)
                .build();

        npc.addViewer(playersToShow);

    }
}
