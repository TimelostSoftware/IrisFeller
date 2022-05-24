package com.timelost.irisfeller.util.settings;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.timelost.irisfeller.util.LoggerUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Settings {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final List<Entry<?>> fields = Lists.newArrayList();

    private final File file;
    private final FileWatcher fileWatcher;
    private final Logger logger;

    public Settings(Logger logger, File file) {
        this.file = file;
        this.logger = logger;

        registerEntries();

        if(!file.exists() || file.isDirectory()) {
            try {
                LoggerUtils.log(logger, Level.INFO, "Settings file missing, generating new default file.");
                if(file.isDirectory())
                    FileUtils.deleteQuietly(file);
                else
                    file.getParentFile().mkdirs();
                file.createNewFile();
                writeJson();
            } catch(IOException e) {
                LoggerUtils.logExceptionStack(logger, false, e, "An error occurred while writing the default settings file:");
            }
        } else
            doReload(false);

        this.fileWatcher = new FileWatcher(file);
    }

    public void update() {
        if(fileWatcher.checkModified())
            doReload(true);
    }

    public void write() {
        writeJson();
    }

    protected abstract void registerEntries();

    protected void registerField(Entry<?>... entries) {
        fields.addAll(Arrays.stream(entries).filter(e -> fields.stream().noneMatch(e::equals)).collect(Collectors.toSet()));
    }

    private void doReload(boolean triggerListeners) {
        try(FileReader reader = new FileReader(file)) {
            JsonElement element = JsonParser.parseReader(reader);
            JsonObject obj = element.getAsJsonObject();
            fields.forEach(entry -> {
                try {
                    entry.update(obj, triggerListeners);
                } catch(SettingsParseException e) {
                    LoggerUtils.logStringStack(logger, false, e.getStringStack(), "Failed to parse settings entry \"%s\", skipping!", entry.getKey());
                }
            });
        } catch(IOException e) {
            LoggerUtils.logExceptionStack(logger, false, e, "An error occurred while reloading the settings file:");
        }
    }

    private void writeJson() {
        try(FileWriter writer = new FileWriter(file)) {
            JsonObject obj = new JsonObject();
            fields.forEach(entry -> entry.serialize(obj));
            GSON.toJson(obj, writer);
        } catch(IOException e) {
            LoggerUtils.logExceptionStack(logger, false, e, "An error occurred while writing the settings file:");
        }
    }
}
