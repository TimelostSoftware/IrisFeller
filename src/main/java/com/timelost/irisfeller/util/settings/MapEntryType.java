package com.timelost.irisfeller.util.settings;

import com.google.gson.JsonElement;
import com.timelost.irisfeller.util.settings.types.EntryType;

import java.util.Map;

public class MapEntryType<K, V> implements EntryType<Map<K, V>> {



    @Override
    public ParseResult<Map<K, V>> parse(JsonElement element) {
        return null;
    }

    @Override
    public JsonElement serialize(Map<K, V> object) {
        return null;
    }
}
