package com.timelost.irisfeller;

import com.google.common.collect.Lists;
import com.timelost.irisfeller.util.settings.Entry;
import com.timelost.irisfeller.util.settings.types.EntryType;
import com.timelost.irisfeller.util.settings.Settings;
import com.timelost.irisfeller.util.settings.types.EnumEntryType;
import org.bukkit.Material;


import java.io.File;
import java.util.List;

public class IrisFellerSettings extends Settings {

    public static final Entry<Double> MAX_SERVER_TICK_PERCENT = new Entry<>("maxServerTickPercent", EntryType.DOUBLE, 1.5, b -> IrisFeller.info("HOTLOADED MAX_SERVER_TICK_PERCENT." + b));
    public static final Entry<String> LOGGER_TOOL = new Entry<>("loggerTool", EntryType.STRING, "axe", b -> IrisFeller.info("HOTLOADED LOGGER_TOOL." + b));
    public static final Entry<Boolean> DEBUG = new Entry<>("debug", EntryType.BOOLEAN, false, b -> IrisFeller.info("HOTLOADED DEBUG." + b));
    public static final Entry<Boolean> USE_DURABILITY = new Entry<>("useDurability", EntryType.BOOLEAN, true, b -> IrisFeller.info("HOTLOADED USE_DURABILITY." + b));

    public static final Entry<Boolean> USE_HUNGER = new Entry<>("useHunger", EntryType.BOOLEAN, true, b -> IrisFeller.info("HOTLOADED USE_HUNGER." + b));
    public static final Entry<Boolean> LEAF_DAMAGE = new Entry<>("leavesCauseDamage", EntryType.BOOLEAN, false, b -> IrisFeller.info("HOTLOADED LEAF_DAMAGE." + b));

    public static final Entry<Boolean> USE_SOUNDS = new Entry<>("useSounds", EntryType.BOOLEAN, true, b -> IrisFeller.info("HOTLOADED USE_SOUNDS." + b));
    public static final Entry<Boolean> USE_EASTEREGG_SOUNDS = new Entry<>("useEasterEggSounds", EntryType.BOOLEAN, true, b -> IrisFeller.info("HOTLOADED USE_EASTEREGG_SOUNDS." + b));
    public static final Entry<Boolean> DROP_NATURALLY = new Entry<>("breakNaturally", EntryType.BOOLEAN, false, b -> IrisFeller.info("HOTLOADED DROP_NATURALLY." + b));
    public static final Entry<List<Material>> BLACKLIST = new Entry<>(
            "blacklist",
            EntryType.STRING.mapTo(
                    s -> EnumEntryType.getEnumOrDefault(Material.class, Material.AIR, e -> s.equals(e.getKey().toString())),
                    m -> m.getKey().toString()).listOf(),
            Lists.newArrayList(Material.AIR, Material.STONE, Material.GRASS, Material.GRASS_BLOCK, Material.COBBLESTONE, Material.DIRT, Material.COARSE_DIRT, Material.ANDESITE, Material.GRAVEL),
            b -> IrisFeller.info("HOTLOADED BLACKLIST." + b));

    public IrisFellerSettings(File parentDir) {
        super(IrisFeller.getPlugin().getLogger(), new File(parentDir, "settings.json"));
    }

    @Override
    protected void registerEntries() {
        registerField(USE_DURABILITY, MAX_SERVER_TICK_PERCENT, USE_SOUNDS, USE_EASTEREGG_SOUNDS, LOGGER_TOOL, DROP_NATURALLY, DEBUG, BLACKLIST);
    }
}
