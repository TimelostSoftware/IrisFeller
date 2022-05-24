package com.timelost.irisfeller.util.settings;

import lombok.Getter;

import java.util.List;

public class SettingsParseException extends Exception {

    @Getter
    private final List<String> stringStack;

    public SettingsParseException(List<String> stack, String key) {
        super("Failed to read value for entry \"" + key+ "\"!");
        this.stringStack = stack;
    }
}
