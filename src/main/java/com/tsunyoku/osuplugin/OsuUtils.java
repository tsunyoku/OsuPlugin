package com.tsunyoku.osuplugin;

import com.google.gson.Gson;
import com.tsunyoku.osuplugin.models.BeatmapModel;
import com.tsunyoku.osuplugin.models.BeatmapsetModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class OsuUtils {

    public static BeatmapsetModel getBeatmapset(int beatmapset_id) throws IOException {
        InputStream stream = new URL(Utils.formatString("https://api.chimu.moe/cheesegull/s/{0}", beatmapset_id)).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String jsonText = Utils.readRequest(reader);
            Gson gson = new Gson();
            return gson.fromJson(jsonText, BeatmapsetModel.class);
        } finally {
            stream.close();
        }
    }

    public static BeatmapModel getBeatmap(int beatmap_id) throws IOException {
        InputStream stream = new URL(Utils.formatString("https://api.chimu.moe/cheesegull/b/{0}", beatmap_id)).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String jsonText = Utils.readRequest(reader);
            Gson gson = new Gson();
            return gson.fromJson(jsonText, BeatmapModel.class);
        } finally {
            stream.close();
        }
    }

    public static String statusFromInt(int status) {
        switch (status) {
            case -2:
                return "Graveyard";
            case -1:
                return "WIP";
            case 0:
                return "Pending";
            case 1:
                return "Ranked";
            case 2:
                return "Approved";
            case 3:
                return "Qualified";
            case 4:
                return "Loved";
            default:
                return "Unknown";
        }
    }
}
