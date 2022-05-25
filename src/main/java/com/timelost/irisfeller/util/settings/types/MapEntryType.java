package com.timelost.irisfeller.util.settings.types;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.timelost.irisfeller.util.settings.ParseResult;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class MapEntryType<K, V> implements EntryType<Map<K, V>> {

    private final EntryType<K> keyEntryType;
    private final EntryType<V> valueEntryType;

    @Override
    public ParseResult<Map<K, V>> parse(JsonElement element) {
        if(!element.isJsonObject())
            return ParseResult.error("Object is not a JSON Map: " + element);
        JsonObject obj = element.getAsJsonObject();
        Map<K ,V> map = Maps.newHashMap();
        for(Map.Entry<String, JsonElement> e : obj.entrySet()) {
            ParseResult<K> k = keyEntryType.parse(new JsonPrimitive(e.getKey()));
            if(k.hasError())
                return ParseResult.error("Failed to parse key \"" + e.getKey() + "\"!", k.getError());
            ParseResult<V> v = valueEntryType.parse(e.getValue());
            if(v.hasError())
                return ParseResult.error("Failed to parse value for key \"" + e.getKey() + "\": " + e.getValue(), k.getError());
            map.put(k.getResult(), v.getResult());
        }
        return ParseResult.of(map);
    }

    @Override
    public JsonElement serialize(Map<K, V> object) {
        JsonObject obj = new JsonObject();
        object.forEach((k, v) -> obj.add(keyEntryType.serialize(k).getAsString(), valueEntryType.serialize(v)));
        return obj;
    }
}
