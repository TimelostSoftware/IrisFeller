package com.timelost.irisfeller.util.settings.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.timelost.irisfeller.util.Serializable;
import com.timelost.irisfeller.util.settings.ParseResult;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class EnumEntryType<E extends Enum<E> & Serializable<String>> implements EntryType<E> {

    private final Class<E> enumClass;

    @Override
    public ParseResult<E> parse(JsonElement element) {
        if(!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString())
            return ParseResult.error("Element is not a serialized enum: " + element);
        String key = element.getAsString();
        Optional<E> res = getEnum(enumClass, e -> e.serialize().equalsIgnoreCase(key));
        if(res.isEmpty())
            return ParseResult.error("\"" + key + "\" is not part of the " + enumClass.getSimpleName() + " enum.");
        return ParseResult.of(res.get());
    }

    @Override
    public JsonElement serialize(E object) {
        return new JsonPrimitive(object.serialize());
    }

    public static <E extends Enum<E>> Optional<E> getEnum(Class<E> clazz, Predicate<E> predicate) {
        for(E e : clazz.getEnumConstants())
            if(predicate.test(e))
                return Optional.of(e);
        return Optional.empty();
    }

    public static <E extends Enum<E>> E getEnumOrDefault(Class<E> clazz, E defaultValue, Predicate<E> test) {
        return getEnum(clazz, test).orElse(defaultValue);
    }
}
