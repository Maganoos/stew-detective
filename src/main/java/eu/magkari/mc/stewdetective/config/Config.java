package eu.magkari.mc.stewdetective.config;

import eu.magkari.mc.stewdetective.StewDetective;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;
    private ConfigData data;

    public Config(Path path) {
        this.path = path;
    }

    public void load() {
        if (!Files.exists(path)) {
            saveConfig(true);
            return;
        }

        try (var reader = Files.newBufferedReader(path)) {
            this.data = GSON.fromJson(reader, ConfigData.class);
            if (this.data == null) {
                StewDetective.LOGGER.warn("Config file was empty or malformed. Using default settings.");
                this.data = new ConfigData(true);
                save();
            }
        } catch (JsonIOException | JsonSyntaxException e) {
            StewDetective.LOGGER.warn("Invalid config JSON, using default settings.", e);
            this.data = new ConfigData(true);
            save();
        } catch (IOException e) {
            StewDetective.LOGGER.error("Failed to read or create config file", e);
            this.data = new ConfigData(true);
        }
    }

    public void saveConfig(boolean heldItemInfoIntegration) {
        this.data = new ConfigData(heldItemInfoIntegration);
        save();
    }

    private void save() {
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(this.data, writer);
            StewDetective.LOGGER.info("Saved config at {}", path);
        } catch (IOException e) {
            StewDetective.LOGGER.error("Failed to write config file", e);
        }
    }

    public boolean isEnabled() {
        return data != null && data.heldItemInfoIntegration;
    }

    public void setEnabled(boolean heldItemInfoIntegration) {
        this.data.heldItemInfoIntegration = heldItemInfoIntegration;
        save();
    }
}
class ConfigData {
    boolean heldItemInfoIntegration;

    ConfigData(boolean heldItemInfoIntegration) {
        this.heldItemInfoIntegration = heldItemInfoIntegration;
    }
}