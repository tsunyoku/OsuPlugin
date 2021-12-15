package com.tsunyoku.osuplugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;

import static com.tsunyoku.osuplugin.Utils.format_string;
import static com.tsunyoku.osuplugin.Utils.status_from_int;

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
            beatmap = Utils.GetBeatmap(beatmap_id);
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
            map_info = Utils.GetBeatmapset(beatmap.ParentSetID);
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
                format_string("Beatmap Info for {0}", beatmap_id)
        );

        bookMeta.addPage(
                String.join("\n",
                        format_string("§lBeatmap Info for {0}§r\n", beatmap.BeatmapID),
                        format_string("Full Title: {0} - {1}", map_info.Artist, map_info.Title),
                        format_string("Mapper: {0}", map_info.Creator),
                        format_string("Status: {0}", status_from_int(map_info.RankedStatus)),
                        format_string("beatmap: {0}", beatmap.DiffName),
                        format_string("BPM: {0}", beatmap.BPM),
                        format_string("AR: {0}", beatmap.AR),
                        format_string("OD: {0}", beatmap.OD),
                        format_string("CS: {0}", beatmap.CS),
                        format_string("HP: {0}", beatmap.HP),
                        format_string("Star Rating: {0}", beatmap.DifficultyRating),
                        format_string("Max Combo: {0}", beatmap.MaxCombo)
                )
        );

        writtenBook.setItemMeta(bookMeta);

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("Your inventory is not empty! Please clear a slot for the book.");
            return true;
        }

        player.getInventory().addItem(writtenBook);

        return true;
    }
}
