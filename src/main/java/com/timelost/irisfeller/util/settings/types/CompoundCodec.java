package com.timelost.irisfeller.util.settings.types;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.timelost.irisfeller.util.settings.ParseResult;

import java.util.Map;

public class CompoundCodec<A> implements EntryType<A> {

    private final Map<String, EntryType<?>> fields = Maps.newHashMap();

    @Override
    public ParseResult<A> parse(JsonElement element) {
        return null;
    }

    @Override
    public JsonElement serialize(A object) {
        return null;
    }
}
