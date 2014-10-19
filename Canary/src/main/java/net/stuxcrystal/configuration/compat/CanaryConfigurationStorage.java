package net.stuxcrystal.configuration.compat;

import net.canarymod.Canary;
import net.canarymod.plugin.Plugin;
import net.stuxcrystal.configuration.storage.ConfigurationLocation;
import net.stuxcrystal.configuration.storage.StorageBackend;
import net.stuxcrystal.configuration.storage.contrib.filebased.FileConfigurationLocation;
import net.visualillusionsent.utils.PropertiesFile;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;




/**
 * The configuration storage.
 */
public class CanaryConfigurationStorage implements StorageBackend {

    private final Plugin plugin;

    public CanaryConfigurationStorage(Plugin plugin) {
        this.plugin = plugin;
    }

    public static String getModuleName(String... module) {
        if (module.length == 0)
            return null;

        if (module[0] == null) {
            module = (String[]) ArrayUtils.remove(module, 0);
        }

        if (module.length == 0)
            return null;

        return StringUtils.join(module, '.');
    }

    // SOURCE: http://www.journaldev.com/842/how-to-get-file-extension-in-java
    private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return null;
    }

    // SOURCE: http://stackoverflow.com/questions/924394/how-to-get-file-name-without-the-extension
    private static String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }

    @Override
    public ConfigurationLocation getConfiguration(String[] module, String world, String name) {
        if ("config".equalsIgnoreCase(name))
            throw new IllegalArgumentException("'config' is a reserved name for configurations.");

        String moduleName = CanaryConfigurationStorage.getModuleName(module);

        // Add config extension to module main configurations.
        if (name == null && moduleName != null)
            name = "config";

        // We will use xml as a default.
        String ext = "xml";
        if (name != null) {
            moduleName = CanaryConfigurationStorage.getModuleName(moduleName, name);
            ext = CanaryConfigurationStorage.getFileExtension(name);
        }

        // Get the actual directory.
        PropertiesFile file;

        // Use the corrent configuration.
        if (moduleName == null) {
            if (world == null) {
                file = this.plugin.getConfig();
            } else {
                file = this.plugin.getWorldConfig(Canary.getServer().getWorld(world));
            }
        } else {
            if (world == null) {
                file = this.plugin.getModuleConfig(moduleName);
            } else {
                file = this.plugin.getWorldModuleConfig(moduleName, Canary.getServer().getWorld(world));
            }
        }

        File result;
        if (ext == null)
            // Just use the file if we don't care about the configuration type.
            result = new File(file.getFilePath());
        else {
            // Strip the extension if we care about the file extension.
            File pre = new File(file.getFilePath());
            result = new File(pre.getParentFile(), CanaryConfigurationStorage.stripExtension(pre.getName())+"." + ext);
        }

        return new FileConfigurationLocation(result);
    }
}
