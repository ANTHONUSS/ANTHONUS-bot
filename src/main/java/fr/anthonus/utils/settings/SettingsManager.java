package fr.anthonus.utils.settings;

import java.util.HashMap;
import java.util.Map;

public class SettingsManager {
    private static Map<Long, Setting> settingsMap = new HashMap<>();

    public static Map<Long, Setting> getSettingsMap() {
        return settingsMap;
    }
}