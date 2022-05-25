package com.timelost.irisfeller.util.settings.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.timelost.irisfeller.util.Serializable;
import com.timelost.irisfeller.util.settings.ParseResult;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface EntryType<E> {

    ParseResult<E> parse(JsonElement element);
    JsonElement serialize(E object);

    default EntryType<List<E>> listOf() { return new ListEntryType<>(this); }

    default <V> EntryType<V> alias(Function<E, V> to, Function<V, E> from) {
        EntryType<E> et = this;
        return new EntryType<>() {
            public ParseResult<V> parse(JsonElement element) {
                ParseResult<E> value = et.parse(element);
                if(value.hasError())
                    return ParseResult.error("Failed to parse mapped EntryType: " + element, value.getError());
                return ParseResult.of(to.apply(value.getResult()));
            }
            public JsonElement serialize(V object) { return et.serialize(from.apply(object)); }
        };
    }

    EntryType<Boolean> BOOLEAN = new EntryType<>() {
        public ParseResult<Boolean> parse(JsonElement e) {
            if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isBoolean())
                return ParseResult.of(e.getAsBoolean());
            return ParseResult.error("Element is not a Boolean: " + e);
        }
        public JsonElement serialize(Boolean object) { return new JsonPrimitive(object); }
    };

    EntryType<String> STRING = new EntryType<>() {
        public ParseResult<String> parse(JsonElement e) {
            if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isString())
                return ParseResult.of(e.getAsString());
            return ParseResult.error("Element is not a String: " + e);
        }
        public JsonElement serialize(String object) { return new JsonPrimitive(object); }
    };

    EntryType<Byte> BYTE = new EntryType<>() {
        public ParseResult<Byte> parse(JsonElement e) {
            if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber())
                return ParseResult.of(e.getAsByte());
            return ParseResult.error("Element is not a Byte: " + e);
        }
        public JsonElement serialize(Byte object) { return new JsonPrimitive(object); }
    };

    EntryType<Short> SHORT = new EntryType<>() {
        public ParseResult<Short> parse(JsonElement e) {
            if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber())
                return ParseResult.of(e.getAsShort());
            return ParseResult.error("Element is not a Short: " + e);
        }
        public JsonElement serialize(Short object) { return new JsonPrimitive(object); }
    };

    EntryType<Integer> INTEGER = new EntryType<>() {
        public ParseResult<Integer> parse(JsonElement e) {
            if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber())
                return ParseResult.of(e.getAsInt());
            return ParseResult.error("Element is not a Integer: " + e);
        }
        public JsonElement serialize(Integer object) { return new JsonPrimitive(object); }
    };

    EntryType<Long> LONG = new EntryType<>() {
        public ParseResult<Long> parse(JsonElement e) {
            if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber())
                return ParseResult.of(e.getAsLong());
            return ParseResult.error("Element is not a Long: " + e);
        }
        public JsonElement serialize(Long object) { return new JsonPrimitive(object); }
    };

    EntryType<Float> FLOAT = new EntryType<>() {
        public ParseResult<Float> parse(JsonElement e) {
            if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber())
                return ParseResult.of(e.getAsFloat());
            return ParseResult.error("Element is not a Float: " + e);
        }
        public JsonElement serialize(Float object) { return new JsonPrimitive(object); }
    };

    EntryType<Double> DOUBLE = new EntryType<>() {
        public ParseResult<Double> parse(JsonElement e) {
            if(e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber())
                return ParseResult.of(e.getAsDouble());
            return ParseResult.error("Element is not a Double: " + e);
        }
        public JsonElement serialize(Double object) { return new JsonPrimitive(object); }
    };

    static <E extends Enum<E> & Serializable<String>> EntryType<E> enumType(Class<E> enumClass) { return new EnumEntryType<>(enumClass); }

    static <K, V> EntryType<Map<K, V>> mapType(EntryType<K> key, EntryType<V> value) { return new MapEntryType<>(key, value); }
}
