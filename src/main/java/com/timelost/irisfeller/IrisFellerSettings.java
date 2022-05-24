package com.timelost.irisfeller;

import com.timelost.irisfeller.util.settings.Entry;
import com.timelost.irisfeller.util.settings.types.EntryType;
import com.timelost.irisfeller.util.settings.Settings;

import java.io.File;

public class IrisFellerSettings extends Settings {

    public static final Entry<Boolean> USE_DURABILITY = new Entry<>("useDurability", EntryType.BOOLEAN, true, b -> { });
    public static final Entry<Double> MAX_SERVER_TICK_PERCENT = new Entry<>("useDurability", EntryType.DOUBLE, 1.5, b -> { });

    public IrisFellerSettings(File parentDir) {
        super(IrisFeller.getPlugin().getLogger(), new File(parentDir, "settings.json"));
    }

    @Override
    protected void registerEntries() {
        registerField(USE_DURABILITY);
        registerField(MAX_SERVER_TICK_PERCENT);
    }
}
