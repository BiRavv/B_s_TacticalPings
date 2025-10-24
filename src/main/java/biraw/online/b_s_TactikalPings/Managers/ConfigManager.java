package biraw.online.b_s_TactikalPings.Managers;

import biraw.online.b_s_TactikalPings.B_s_TacticalPings;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static final B_s_TacticalPings plugin = B_s_TacticalPings.getInstance();

    private static int ping_reach;
    public static int getPingReach(){return ping_reach;}
    private static int ping_limit;
    public static int getPingLimit(){return ping_limit;}
    private static int ping_duration;
    public static int getPingDuration(){return ping_duration;}


    public static void loadAndSaveConfig(){
        ping_reach = loadAndSaveConfigValue("ping_reach",200);
        ping_limit = loadAndSaveConfigValue("ping_limit",10);
        ping_duration = loadAndSaveConfigValue("ping_duration",8);
    }


    private static  <T> T loadAndSaveConfigValue(String key, T defaultValue) {
        // Ensure config file exists
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        if (!config.isSet(key)) {
            config.set(key, defaultValue);
            plugin.saveConfig();
            plugin.getLogger().info("Added missing config value: " + key + " = " + defaultValue);
            return defaultValue;
        } else {
            T value = (T) config.get(key); // Chill its gut
            plugin.getLogger().info("Loaded config value: " + key + " = " + value);
            return value;
        }
    }
}
