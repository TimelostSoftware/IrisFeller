package com.timelost.irisfeller.util.settings;

import com.google.gson.JsonObject;
import com.timelost.irisfeller.util.settings.types.EntryType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class Entry<V> {

    @Getter
    private final String key;

    private final EntryType<V> type;
    private final V defaultValue;
    private final Consumer<V> onChange;

    private V value;

    public V get() {
        return value == null ? (value = defaultValue) : value;
    }

    public void set(V value, boolean triggerListeners) {
        if(value.equals(this.value))
            return;
        this.value = value;
        if(triggerListeners)
            onChange.accept(this.value);
    }

    public void reset(boolean triggerListeners) {
        set(this.defaultValue, triggerListeners);
    }

    void serialize(JsonObject json) {
        json.add(key, type.serialize(this.value == null ? defaultValue : value));
    }

    void update(JsonObject obj, boolean triggerListener) throws SettingsParseException {
        if(!obj.has(key))
            reset(triggerListener);
        else {
            ParseResult<V> result = type.parse(obj.get(key));
            if(result.hasError())
                throw new SettingsParseException(result.getError(), key);
            set(result.getResult(), triggerListener);
        }
    }
}