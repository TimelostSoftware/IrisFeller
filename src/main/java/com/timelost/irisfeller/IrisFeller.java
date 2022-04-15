package com.timelost.irisfeller;

import com.timelost.irisfeller.util.IrisToolbeltManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class IrisFeller extends JavaPlugin {
    private static IrisFeller plugin;

    @Override
    public void onEnable() {
        plugin = this;
        try {
            IrisToolbeltManager.setup();
            startup("Hello Iris! i got your objects!");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.getServer().getPluginManager().registerEvents(new BlockBreakLogger(), IrisFeller.this);

    }

    @Override
    public void onDisable() {
        IrisFeller.info("Goodbye Iris... Make sure you save the mantle!");
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
