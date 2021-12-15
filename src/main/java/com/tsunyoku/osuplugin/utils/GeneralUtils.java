package com.tsunyoku.osuplugin.utils;

import java.io.*;
import java.text.MessageFormat;

public class GeneralUtils {
    public static String formatString(String string, Object ... params) {
        return new MessageFormat(string).format(params);
    }

    public static String readRequest(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();

        int cp;
        while ((cp = reader.read()) != -1) {
            builder.append((char)cp);
        }

        return builder.toString();
    }
}
