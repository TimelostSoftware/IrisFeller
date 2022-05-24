package com.timelost.irisfeller.util;

import com.timelost.irisfeller.IrisFeller;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class Jobs {

    public static int async(Runnable r, int delay){
        return Bukkit.getScheduler().scheduleAsyncDelayedTask(IrisFeller.getPlugin(), r,delay);
    }

    public static int sync(Runnable r, int delay){
        return Bukkit.getScheduler().scheduleSyncDelayedTask(IrisFeller.getPlugin(), r,delay);
    }

    public static BukkitTask scheduleSyncTask(long period, Runnable onIteration, boolean delayStart) {
        return Bukkit.getScheduler().runTaskTimer(IrisFeller.getPlugin(), onIteration, period, delayStart ? period : 0);
    }
    public static void async(Runnable r){
        IrisFeller.async.queue(r);
    }

    public static void sync(Runnable r){
        IrisFeller.sync.queue(r);
    }
}
