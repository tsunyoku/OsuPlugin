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

        String beatmapset = args[0];

        BeatmapsetModel map_info;
        try {
            map_info = OsuUtils.getBeatmapset(beatmapset);
            if (map_info == null) {
                player.sendMessage("Failed to get the set from the API, please check you have provided a valid one!");
                return true;
            }
        } catch (IOException e) {
            player.sendMessage("Failed to get the set from the API, please check you have provided a valid one!");
            return true;
        }

        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta)writtenBook.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setTitle(
                GeneralUtils.formatString("Beatmapset Info for {0}", beatmapset)
        );

        bookMeta.addPage(
                String.join("\n",
                        "§lBeatmapset Info§r\n",
                        GeneralUtils.formatString("Full Title: {0} - {1}", map_info.Artist, map_info.Title),
                        GeneralUtils.formatString("Mapper: {0}", map_info.Creator),
                        GeneralUtils.formatString("Status: {0}", map_info.RankedStatus.name())
                )
        );

        for (BeatmapModel difficulty: map_info.ChildrenBeatmaps) {
            bookMeta.addPage(
                    String.join("\n",
                            GeneralUtils.formatString("§lBeatmap Info for {0}§r\n", difficulty.BeatmapID),
                            GeneralUtils.formatString("Difficulty: {0}", difficulty.DiffName),
                            GeneralUtils.formatString("BPM: {0}", difficulty.BPM),
                            GeneralUtils.formatString("AR: {0}", difficulty.AR),
                            GeneralUtils.formatString("OD: {0}", difficulty.OD),
                            GeneralUtils.formatString("CS: {0}", difficulty.CS),
                            GeneralUtils.formatString("HP: {0}", difficulty.HP),
                            GeneralUtils.formatString("Star Rating: {0}", difficulty.DifficultyRating),
                            GeneralUtils.formatString("Max Combo: {0}", difficulty.MaxCombo)
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
