package com.tsunyoku.osuplugin;

import com.tsunyoku.osuplugin.commands.BeatmapCommand;
import com.tsunyoku.osuplugin.commands.BeatmapsetCommand;
import com.tsunyoku.osuplugin.commands.UserCommand;
import org.bukkit.Bukkit;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;

public final class OsuPlugin extends JavaPlugin {
    public static String apiKey;
    public static boolean permissionsRequired;
    public static String permissionString;

    @Override
    public void onEnable() {
        getCommand("beatmapset").setExecutor(new BeatmapsetCommand(this));
        getCommand("beatmap").setExecutor(new BeatmapCommand(this));
        getCommand("user").setExecutor(new UserCommand(this));

        // check & store osu! api key
        this.saveDefaultConfig();
        this.apiKey = this.getConfig().getString("api_key");
        this.permissionsRequired = this.getConfig().getBoolean("permissions.enabled");
        this.permissionString = this.getConfig().getString("permissions.permission");

        // warn in console & to any online ops that there is no api key
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            System.out.println("WARNING: No osu! api key has been set! You will not be able to use any commands which require the use of an API key.");

            Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(user -> user.sendMessage(
                    "No osu! api key has been set! You will not be able to use any commands which require the use of an API key."
            ));
        }
    }
}