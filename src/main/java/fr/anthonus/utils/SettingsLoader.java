package fr.anthonus.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SettingsLoader {
    public static String getVersion() {
        try (InputStream in = SettingsLoader.class.getClassLoader().getResourceAsStream("version.txt")) {
            if (in != null) {
                return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
            }
        } catch (IOException ignored) {}
        return "unknown";
    }
}
