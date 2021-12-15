package com.tsunyoku.osuplugin;

import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;

public class Utils {
    public static String format_string(String string, Object ... params) {
        return new MessageFormat(string).format(params);
    }

    private static String readRequest(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();

        int cp;
        while ((cp = reader.read()) != -1) {
            builder.append((char)cp);
        }

        return builder.toString();
    }

    public static BeatmapsetModel GetBeatmapset(int beatmapset_id) throws IOException {
        InputStream stream = new URL(format_string("https://api.chimu.moe/cheesegull/s/{0}", beatmapset_id)).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String jsonText = readRequest(reader);
            Gson gson = new Gson();
            return gson.fromJson(jsonText, BeatmapsetModel.class);
        } finally {
            stream.close();
        }
    }

    public static BeatmapModel GetBeatmap(int beatmap_id) throws IOException {
        InputStream stream = new URL(format_string("https://api.chimu.moe/cheesegull/b/{0}", beatmap_id)).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String jsonText = readRequest(reader);
            Gson gson = new Gson();
            return gson.fromJson(jsonText, BeatmapModel.class);
        } finally {
            stream.close();
        }
    }

    public static String status_from_int(int status) {
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
