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

public class BeatmapsetCommand implements CommandExecutor {
    public static OsuPlugin plugin;
    public BeatmapsetCommand(OsuPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { return false; }

        Player player = (Player)sender;

        if (args.length != 1) {
            player.sendMessage("Invalid beatmapset ID, please make sure you have provided one!");
            return true;
        }

        int beatmapset = Integer.parseInt(args[0]);

        BeatmapsetModel map_info;
        try {
            map_info = Utils.GetBeatmapset(beatmapset);
            if (map_info == null) {
                player.sendMessage("Failed to get the set from the API, please check you have provided a valid one!");
            }
        } catch (IOException e) {
            player.sendMessage("Failed to get the set from the API, please check you have provided a valid one!");
            return true;
        }

        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta)writtenBook.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setTitle(
                format_string("Beatmapset Info for {0}", beatmapset)
        );

        bookMeta.addPage(
                String.join("\n",
                        "§lBeatmapset Info§r\n",
                        format_string("Full Title: {0} - {1}", map_info.Artist, map_info.Title),
                        format_string("Mapper: {0}", map_info.Creator),
                        format_string("Status: {0}", status_from_int(map_info.RankedStatus))
                )
        );

        BeatmapModel[] difficulties = map_info.ChildrenBeatmaps;
        for (int i = 0; i < difficulties.length; i++) {
            BeatmapModel difficulty = difficulties[i];

            bookMeta.addPage(
                    String.join("\n",
                            format_string("§lBeatmap Info for {0}§r\n", difficulty.BeatmapID),
                            format_string("Difficulty: {0}", difficulty.DiffName),
                            format_string("BPM: {0}", difficulty.BPM),
                            format_string("AR: {0}", difficulty.AR),
                            format_string("OD: {0}", difficulty.OD),
                            format_string("CS: {0}", difficulty.CS),
                            format_string("HP: {0}", difficulty.HP),
                            format_string("Star Rating: {0}", difficulty.DifficultyRating),
                            format_string("Max Combo: {0}", difficulty.MaxCombo)
                    )
            );
        }

        writtenBook.setItemMeta(bookMeta);

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("Your inventory is not empty! Please clear a slot for the book.");
            return true;
        }

        player.getInventory().addItem(writtenBook);

        return true;
    }
}
