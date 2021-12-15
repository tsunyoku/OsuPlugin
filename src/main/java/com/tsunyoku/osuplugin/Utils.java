package com.tsunyoku.osuplugin;

import java.io.*;
import java.text.MessageFormat;

public class Utils {
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
