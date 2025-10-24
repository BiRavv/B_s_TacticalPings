package biraw.online.b_s_TactikalPings;

import biraw.online.b_s_TactikalPings.Managers.ConfigManager;
import biraw.online.b_s_TactikalPings.Managers.TeamCommandManager;
import biraw.online.b_s_TactikalPings.Listeners.PlayerLeaveL;
import biraw.online.b_s_TactikalPings.Listeners.PlayerPingL;
import biraw.online.b_s_TactikalPings.Managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class B_s_TacticalPings extends JavaPlugin {

    private static B_s_TacticalPings instance;
    public static B_s_TacticalPings getInstance(){return instance;}

    public FileConfiguration config(){
        return getConfig();
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new PlayerPingL(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveL(),this);
        TeamCommandManager tcm = new TeamCommandManager();
        this.getCommand("pt").setExecutor(tcm);
        this.getCommand("pt").setTabCompleter(tcm);
        ConfigManager.loadAndSaveConfig();

        getLogger().info(" ");
        getLogger().info("O=========================================================O");
        getLogger().info("    The B's TacticalPing plugin has loaded successfully!");
        getLogger().info("       This is B's TacticalPing for Minecraft 1.21+"    );
        getLogger().info("                       Author: BiRaw");
        getLogger().info("         Discord: https://discord.gg/XwFqu7uahX ^.^");
        getLogger().info("O=========================================================O");
        getLogger().info(" ");
    }

    @Override
    public void onDisable() {
        TeamManager.closeAllTeams();
        Bukkit.getScheduler().cancelTasks(this);
    }
}
