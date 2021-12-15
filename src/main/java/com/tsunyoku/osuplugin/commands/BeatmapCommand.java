package com.tsunyoku.osuplugin.commands;

import com.tsunyoku.osuplugin.*;
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

        if (args.length != 1) {
            player.sendMessage("Invalid beatmap ID, please make sure you have provided one!");
            return true;
        }

        int beatmap_id = Integer.parseInt(args[0]);

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
            map_info = OsuUtils.getBeatmapset(beatmap.ParentSetID);
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
                Utils.formatString("Beatmap Info for {0}", beatmap_id)
        );

        bookMeta.addPage(
                String.join("\n",
                        Utils.formatString("§lBeatmap Info for {0}§r\n", beatmap.BeatmapID),
                        Utils.formatString("Full Title: {0} - {1}", map_info.Artist, map_info.Title),
                        Utils.formatString("Mapper: {0}", map_info.Creator),
                        Utils.formatString("Status: {0}", OsuUtils.statusFromInt(map_info.RankedStatus)),
                        Utils.formatString("beatmap: {0}", beatmap.DiffName),
                        Utils.formatString("BPM: {0}", beatmap.BPM),
                        Utils.formatString("AR: {0}", beatmap.AR),
                        Utils.formatString("OD: {0}", beatmap.OD),
                        Utils.formatString("CS: {0}", beatmap.CS),
                        Utils.formatString("HP: {0}", beatmap.HP),
                        Utils.formatString("Star Rating: {0}", beatmap.DifficultyRating),
                        Utils.formatString("Max Combo: {0}", beatmap.MaxCombo)
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
