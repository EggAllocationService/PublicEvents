package io.gitlab.kyleafmine.publicevents;

import io.gitlab.kyleafmine.publicevents.config.GlobalConfig;
import io.gitlab.kyleafmine.publicevents.menus.TextEntryManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class PublicEvents extends JavaPlugin {
    public static File configFile;
    public static PublicEvents instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Loading config file");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        configFile = Paths.get(getDataFolder().getAbsolutePath(), "config.bin").toFile();
        if (!configFile.exists()) {
            // create new basic config, probably first launch
            getLogger().info("Creating default config");
            try {
                configFile.createNewFile();
                GlobalConfig.defaults();
            } catch (IOException e) {
                e.printStackTrace();
                getServer().getPluginManager().disablePlugin(this);
            }

        } else {
            try {
                GlobalConfig.load();
            } catch (Exception e) {
                e.printStackTrace();
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        getLogger().info("registering event listeners");
        getServer().getPluginManager().registerEvents(TextEntryManager.init(), this);

        getLogger().info("regiestering commands");
        getCommand("publicevents").setExecutor(new MainCommand());
        // stuffs
    }
}
