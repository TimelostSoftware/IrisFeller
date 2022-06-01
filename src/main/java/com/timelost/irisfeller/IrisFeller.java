package com.timelost.irisfeller;

import com.timelost.irisfeller.util.IrisToolbeltManager;
import com.timelost.irisfeller.util.Jobs;
import com.timelost.irisfeller.util.TheChonker;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class IrisFeller extends JavaPlugin {
    private static IrisFeller plugin;
    public static TheChonker async;
    public static TheChonker sync;

    public static IrisFellerSettings settings;

    @Override
    public void onEnable() {
        plugin = this;

        settings = new IrisFellerSettings(getDataFolder());
        Jobs.scheduleSyncTask(100L, () -> settings.update(), true);

        async = new TheChonker(true);
        sync = new TheChonker(false);

        try {
            startup("Hello Iris! i got your objects!");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(new BlockBreakLogger(), IrisFeller.this);

    }

    @Override
    public void onDisable() {
        IrisFeller.info("Goodbye Iris... Make sure you save the mantle!");
        getServer().getScheduler().cancelTasks(this);
    }

    public static IrisFeller getPlugin() {
        return plugin;
    }


    // Print Shit
    private static String tl() {
        return ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "IrisFeller" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    }

    public static void msg(String string) {
        try {
            System.out.println(string.replaceAll("(<([^>]+)>)", ""));
        } catch (Throwable ignored1) {

        }
    }

    public static void info(String string) {
        msg(tl() + "Info: " + string);
    }
    public static void startup(String string) {
        msg(tl()  + string);
    }

    public static void error(String string) {
        msg(tl() + ChatColor.DARK_RED + "  ERROR:" + string);
    }

}
