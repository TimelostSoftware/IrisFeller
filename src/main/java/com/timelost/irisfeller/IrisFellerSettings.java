package com.timelost.irisfeller;

import com.timelost.irisfeller.util.settings.Entry;
import com.timelost.irisfeller.util.settings.types.EntryType;
import com.timelost.irisfeller.util.settings.Settings;


import java.io.File;

public class IrisFellerSettings extends Settings {

    public static final Entry<Double> MAX_SERVER_TICK_PERCENT = new Entry<>("maxServerTickPercent", EntryType.DOUBLE, 1.5, p -> { });
    public static final Entry<String> LOGGER_TOOL = new Entry<>("loggerTool", EntryType.STRING, "axe", b -> { });
    public static final Entry<Boolean> DEBUG = new Entry<>("debug", EntryType.BOOLEAN, false, b -> { });
    public static final Entry<Boolean> USE_DURABILITY = new Entry<>("useDurability", EntryType.BOOLEAN, true, b -> { });
    public static final Entry<Boolean> USE_SOUNDS = new Entry<>("useSounds", EntryType.BOOLEAN, true, b -> { });
    public static final Entry<Boolean> USE_EASTEREGG_SOUNDS = new Entry<>("useEasterEggSounds", EntryType.BOOLEAN, true, b -> { });
    public static final Entry<Boolean> DROP_NATURALLY = new Entry<>("breakNaturally", EntryType.BOOLEAN, true, b -> { });

    public IrisFellerSettings(File parentDir) {
        super(IrisFeller.getPlugin().getLogger(), new File(parentDir, "settings.json"));
    }

    @Override
    protected void registerEntries() {
        registerField(USE_DURABILITY, MAX_SERVER_TICK_PERCENT, USE_SOUNDS, USE_EASTEREGG_SOUNDS, LOGGER_TOOL, DROP_NATURALLY, DEBUG);
    }
}
