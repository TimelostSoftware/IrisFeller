package com.timelost.irisfeller.util;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.lang.reflect.Method;

public class IrisToolbeltManager {

    public static boolean hasMantleObject(World world, int x, int y, int z) {
        return getMantleIdentity(world, x, y, z) != null;
    }

    public static void deleteMantleBlock(World world, int x, int y, int z) {
        try {
            Method m = Class.forName("com.volmit.iris.core.tools.IrisToolbelt").getDeclaredMethod("deleteMantleData", World.class, int.class, int.class, int.class, Class.class);
            m.invoke(null, world, x, y, z, BlockData.class);
            m.invoke(null, world, x, y, z, String.class);
        } catch(Throwable ignored) {}
    }

    public static BlockData getMantleBlock(World world, int x, int y, int z) {
        try {
            Method m = Class.forName("com.volmit.iris.core.tools.IrisToolbelt").getDeclaredMethod("getMantleData", World.class, int.class, int.class, int.class, Class.class);
            BlockData s = (BlockData) m.invoke(null, world, x, y, z, BlockData.class);
            if(s != null) {return s;}
        } catch(Throwable ignored) {}
        return null;
    }

    public static String getMantleIdentity(World world, int x, int y, int z) {
        try {
            Method m = Class.forName("com.volmit.iris.core.tools.IrisToolbelt").getDeclaredMethod("getMantleData", World.class, int.class, int.class, int.class, Class.class);
            String s = (String) m.invoke(null, world, x, y, z, String.class);
            if(s != null) {return s;}
        } catch(Throwable ignored) {}
        return null;
    }
}
