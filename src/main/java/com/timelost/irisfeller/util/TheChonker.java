package com.timelost.irisfeller.util;

import com.timelost.irisfeller.IrisFeller;
import com.timelost.irisfeller.IrisFellerSettings;
import org.bukkit.Bukkit;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TheChonker {
    private final Queue<Runnable> queue = new ConcurrentLinkedDeque<>();
    private int id;
    private double limmit = IrisFellerSettings.MAX_SERVER_TICK_PERCENT.get();
    private final boolean async;

    public TheChonker(boolean async) {
        this.async = async;
        id = async ? Bukkit.getScheduler().scheduleAsyncRepeatingTask(IrisFeller.getPlugin(), this::tick, 0, 0) : Bukkit.getScheduler().scheduleSyncRepeatingTask(IrisFeller.getPlugin(), this::tick, 0, 0);
    }

    private void tick() {
        if(queue.peek() != null){
            IrisFeller.info("Current Active Queue: "+ queue.size() );

        }
        PrecisionStopwatch p = PrecisionStopwatch.start();
        while(p.getMilliseconds() < limmit){
            Runnable r = queue.poll();
            if (r != null){
                r.run();
            }else {
                break;
            }
        }
    }

    public void queue(Runnable r) {
        queue.add(r);
    }



}
