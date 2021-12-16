package com.tsunyoku.osuplugin.commands;

import com.tsunyoku.osuplugin.OsuPlugin;
import com.tsunyoku.osuplugin.models.UserModel;
import com.tsunyoku.osuplugin.utils.GeneralUtils;
import com.tsunyoku.osuplugin.utils.OsuUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

public class UserCommand implements CommandExecutor {
    public static OsuPlugin plugin;
    public UserCommand(OsuPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { return false; }
        Player player = (Player)sender;

        if (args.length != 1) {
            player.sendMessage("Invalid user ID, please make sure you have provided one!");
            return true;
        }

        String userID = args[0];

        UserModel user;
        try {
            user = OsuUtils.getUser(userID, this.plugin.api_key);
            if (user == null) {
                player.sendMessage("Failed to get the user from the API, please check you have provided a valid one!");
                return true;
            }
        } catch (IOException e) {
            player.sendMessage("Failed to get the user from the API, please check you have provided a valid one!");
            return true;
        }

        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta)writtenBook.getItemMeta();
        bookMeta.setAuthor(player.getName());
        bookMeta.setTitle(
                GeneralUtils.formatString("User Info for {0}", userID)
        );

        bookMeta.addPage(
                String.join("\n",
                        GeneralUtils.formatString("§lBasic Info for {0}§r\n", userID),
                        GeneralUtils.formatString("User ID: {0}", user.user_id),
                        GeneralUtils.formatString("Username: {0}", user.username),
                        GeneralUtils.formatString("Country: {0}", new Locale("", user.country).getDisplayCountry()),
                        GeneralUtils.formatString("Performance Points: {0}", Math.round(user.pp_raw)),
                        GeneralUtils.formatString("Rank: #{0} ({1}#{2})", user.pp_rank, user.country, user.pp_country_rank),
                        GeneralUtils.formatString("Accuracy: {0}%", new DecimalFormat("0.##").format(user.accuracy))
                )
        );

        // add more advanced info here

        writtenBook.setItemMeta(bookMeta);

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("Your inventory is full! Please clear a slot for the book.");
            return true;
        }

        player.getInventory().addItem(writtenBook);

        return true;
    }
}
