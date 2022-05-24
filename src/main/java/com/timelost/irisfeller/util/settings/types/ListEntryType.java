package com.timelost.irisfeller.util.settings.types;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.timelost.irisfeller.util.settings.ParseResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListEntryType<E> implements EntryType<List<E>>{

    private final EntryType<E> type;

    @Override
    public ParseResult<List<E>> parse(JsonElement element) {
        if(!element.isJsonArray())
            return ParseResult.error("Element is not an Array: " + element);
        JsonArray array = element.getAsJsonArray();
        List<E> result = Lists.newArrayList();
        for(JsonElement e : array) {
            ParseResult<E> parse = type.parse(e);
            if(parse.hasError())
                return ParseResult.error("Malformed element in array:", "\t" + parse.getError());
            result.add(parse.getResult());
        }
        return ParseResult.of(result);
    }

    @Override
    public JsonElement serialize(List<E> object) {
        JsonArray array = new JsonArray();
        object.forEach(type::serialize);
        return array;
    }
}
