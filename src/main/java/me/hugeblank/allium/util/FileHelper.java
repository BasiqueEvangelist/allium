package me.hugeblank.allium.util;

import me.hugeblank.allium.Allium;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.FileSystemException;
import java.nio.file.Path;

public class FileHelper {
    /* Allium Plugin directory spec
        /scripts
            /<unique dir name> | unique file name, bonus point if using the namespace ID
                /<libs and stuff>
                main.lua | file loaded by Allium
     */

    public static final File PLUGIN_DIR = FabricLoader.getInstance().getGameDir().resolve("scripts").toFile();

    public static File getPluginsDirectory() {
        if (!PLUGIN_DIR.exists()) {
            Allium.LOGGER.warn("Missing plugin directory, creating one for you");
            if (!PLUGIN_DIR.mkdir()) {
                Allium.LOGGER.error("Could not create plugin directory, something is seriously wrong!");
                throw new RuntimeException("Failed to create plugin directory", new FileSystemException(PLUGIN_DIR.toPath().toAbsolutePath().toString()));
            }
        }
        return PLUGIN_DIR;
    }

    public static boolean isDirectoryPlugin(File dir) {
        return dir.isDirectory() && dir.toPath().resolve("main.lua").toFile().exists();
    }

    public static Path getMainPath(Path pluginPath) {
        return pluginPath.resolve("main.lua");
    }
}
