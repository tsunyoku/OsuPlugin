package com.tsunyoku.osuplugin;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;

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
            map_info = OsuUtils.getBeatmapset(beatmapset);
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
                Utils.formatString("Beatmapset Info for {0}", beatmapset)
        );

        bookMeta.addPage(
                String.join("\n",
                        "§lBeatmapset Info§r\n",
                        Utils.formatString("Full Title: {0} - {1}", map_info.Artist, map_info.Title),
                        Utils.formatString("Mapper: {0}", map_info.Creator),
                        Utils.formatString("Status: {0}", OsuUtils.statusFromInt(map_info.RankedStatus))
                )
        );

        for (BeatmapModel difficulty: map_info.ChildrenBeatmaps) {
            bookMeta.addPage(
                    String.join("\n",
                            Utils.formatString("§lBeatmap Info for {0}§r\n", difficulty.BeatmapID),
                            Utils.formatString("Difficulty: {0}", difficulty.DiffName),
                            Utils.formatString("BPM: {0}", difficulty.BPM),
                            Utils.formatString("AR: {0}", difficulty.AR),
                            Utils.formatString("OD: {0}", difficulty.OD),
                            Utils.formatString("CS: {0}", difficulty.CS),
                            Utils.formatString("HP: {0}", difficulty.HP),
                            Utils.formatString("Star Rating: {0}", difficulty.DifficultyRating),
                            Utils.formatString("Max Combo: {0}", difficulty.MaxCombo)
                    )
            );
        }

        writtenBook.setItemMeta(bookMeta);

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("Your inventory is full! Please clear a slot for the book.");
            return true;
        }

        player.getInventory().addItem(writtenBook);

        return true;
    }
}
