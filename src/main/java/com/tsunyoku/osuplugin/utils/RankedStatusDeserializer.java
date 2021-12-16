package com.tsunyoku.osuplugin.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.tsunyoku.osuplugin.enums.RankedStatus;

import java.lang.reflect.Type;

public class RankedStatusDeserializer implements JsonDeserializer<RankedStatus> {
    @Override
    public RankedStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
        return RankedStatus.findByAbbr(json.getAsInt());
    }
}
