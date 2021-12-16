package com.tsunyoku.osuplugin.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsunyoku.osuplugin.enums.RankedStatus;
import com.tsunyoku.osuplugin.models.BeatmapModel;
import com.tsunyoku.osuplugin.models.BeatmapsetModel;
import com.tsunyoku.osuplugin.models.UserModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class OsuUtils {

    public static BeatmapsetModel getBeatmapset(String beatmapset_id) throws IOException {
        InputStream stream = new URL(GeneralUtils.formatString("https://api.chimu.moe/cheesegull/s/{0}", beatmapset_id)).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String jsonText = GeneralUtils.readRequest(reader);

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(RankedStatus.class, new RankedStatusDeserializer());
            Gson gson = builder.create();

            return gson.fromJson(jsonText, BeatmapsetModel.class);
        } finally {
            stream.close();
        }
    }

    public static BeatmapModel getBeatmap(String beatmap_id) throws IOException {
        InputStream stream = new URL(GeneralUtils.formatString("https://api.chimu.moe/cheesegull/b/{0}", beatmap_id)).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String jsonText = GeneralUtils.readRequest(reader);
            Gson gson = new Gson();
            return gson.fromJson(jsonText, BeatmapModel.class);
        } finally {
            stream.close();
        }
    }

    public static UserModel getUser(String user_id, String api_key) throws IOException {
        InputStream stream = new URL(GeneralUtils.formatString("https://old.ppy.sh/api/get_user?k={0}&u={1}", api_key, user_id)).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String jsonText = GeneralUtils.readRequest(reader);
            Gson gson = new Gson();
            return gson.fromJson(jsonText, UserModel[].class)[0];
        } finally {
            stream.close();
        }
    }
}
