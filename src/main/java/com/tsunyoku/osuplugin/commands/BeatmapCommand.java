package com.tsunyoku.osuplugin.commands;

import com.tsunyoku.osuplugin.*;
import com.tsunyoku.osuplugin.models.BeatmapModel;
import com.tsunyoku.osuplugin.models.BeatmapsetModel;
import com.tsunyoku.osuplugin.utils.OsuUtils;
import com.tsunyoku.osuplugin.utils.GeneralUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;

public class BeatmapCommand implements CommandExecutor {
    public static OsuPlugin plugin;
    public BeatmapCommand(OsuPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return false; }
        Player player = (Player)sender;

        if (this.plugin.permission_required && !player.hasPermission(this.plugin.permission)) {
            player.sendMessage("You don't have permission to use this command!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Invalid beatmap ID, please make sure you have provided one!");
            return true;
        }

        String beatmap_id = args[0];

        BeatmapModel beatmap;
        try {
            beatmap = OsuUtils.getBeatmap(beatmap_id);
            if (beatmap == null) {
                player.sendMessage("Failed to get the map from the API, please check you have provided a valid one!");
                return true;
            }
        } catch (IOException e) {
            player.sendMessage("Failed to get the map from the API, please check you have provided a valid one!");
            return true;
        }

        BeatmapsetModel map_info;
        try {
            map_info = OsuUtils.getBeatmapset(String.valueOf(beatmap.ParentSetID));
            if (map_info == null) {
                player.sendMessage("Failed to get the map from the API, please check you have provided a valid one!");
                return true;
            }
        } catch (IOException e) {
            player.sendMessage("Failed to get the map from the API, please check you have provided a valid one!");
            return true;
        }

        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta)writtenBook.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setTitle(
                GeneralUtils.formatString("Beatmap Info for {0}", beatmap_id)
        );

        bookMeta.addPage(
                String.join("\n",
                        GeneralUtils.formatString("§lBeatmap Info for {0}§r\n", beatmap.BeatmapID),
                        GeneralUtils.formatString("Full Title: {0} - {1}", map_info.Artist, map_info.Title),
                        GeneralUtils.formatString("Mapper: {0}", map_info.Creator),
                        GeneralUtils.formatString("Status: {0}", map_info.RankedStatus.name()),
                        GeneralUtils.formatString("beatmap: {0}", beatmap.DiffName),
                        GeneralUtils.formatString("BPM: {0}", beatmap.BPM),
                        GeneralUtils.formatString("AR: {0}", beatmap.AR),
                        GeneralUtils.formatString("OD: {0}", beatmap.OD),
                        GeneralUtils.formatString("CS: {0}", beatmap.CS),
                        GeneralUtils.formatString("HP: {0}", beatmap.HP),
                        GeneralUtils.formatString("Star Rating: {0}", beatmap.DifficultyRating),
                        GeneralUtils.formatString("Max Combo: {0}", beatmap.MaxCombo)
                )
        );

        writtenBook.setItemMeta(bookMeta);

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("Your inventory is full! Please clear a slot for the book.");
            return true;
        }

        player.getInventory().addItem(writtenBook);

        return true;
    }
}
