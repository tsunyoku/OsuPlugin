package com.tsunyoku.osuplugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class OsuPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getCommand("beatmapset").setExecutor(new BeatmapsetCommand(this));
        getCommand("beatmap").setExecutor(new BeatmapCommand(this));
    }
}

