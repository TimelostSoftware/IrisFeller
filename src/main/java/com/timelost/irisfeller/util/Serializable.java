package com.timelost.irisfeller.util;

@FunctionalInterface
public interface Serializable<K> {
    K serialize();
}
