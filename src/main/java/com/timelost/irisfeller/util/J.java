package com.timelost.irisfeller.util;

import com.timelost.irisfeller.IrisFeller;
import org.bukkit.Bukkit;

public class J {

    public static int a(Runnable r, int delay){
        return Bukkit.getScheduler().scheduleAsyncDelayedTask(IrisFeller.getPlugin(), r,delay);
    }

    public static int s(Runnable r, int delay){
        return Bukkit.getScheduler().scheduleSyncDelayedTask(IrisFeller.getPlugin(), r,delay);
    }

    public static void a(Runnable r){
        IrisFeller.async.queue(r);
    }

    public static void s(Runnable r){
        IrisFeller.sync.queue(r);
    }


}
