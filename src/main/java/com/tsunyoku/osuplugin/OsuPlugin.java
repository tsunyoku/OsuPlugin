package com.tsunyoku.osuplugin;

import org.bukkit.Bukkit;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;

public final class OsuPlugin extends JavaPlugin {
    public static String api_key;

    @Override
    public void onEnable() {
        getCommand("beatmapset").setExecutor(new BeatmapsetCommand(this));
        getCommand("beatmap").setExecutor(new BeatmapCommand(this));

        // check & store osu! api key
        this.saveDefaultConfig();
        this.api_key = this.getConfig().getString("api_key");

        // warn in console & to any online ops that there is no api key
        if (this.api_key == null || this.api_key.isEmpty()) {
            System.out.println("⚠️No osu! api key has been set! You will not be able to use any commands which require the use of an API key.");

            Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(user -> user.sendMessage(
                    "No osu! api key has been set! You will not be able to use any commands which require the use of an API key."
            ));
        }
    }
}