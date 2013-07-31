package net.stuxcrystal.configuration;

import net.stuxcrystal.configuration.generators.xml.XmlGenerator;
import net.stuxcrystal.configuration.generators.yaml.YamlGenerator;
import net.stuxcrystal.configuration.types.*;

/**
 * Basic constructor.
 */
class BaseConstructor implements Constructor {

    /**
     * Loads all existing generators.
     *
     * @param loader The loader where to add the generators.
     */
    @Override
    public void loadGenerators(ConfigurationLoader loader) {
        // Adds predefined generators.
        loader.addTreeGenerator(new YamlGenerator());                 // ".yml" and ".yaml"
        loader.addTreeGenerator(new XmlGenerator());                  // ".xml"
    }

    /**
     * Load all available logging types.
     *
     * @param loader The loader where to add the types.
     */
    @Override
    public void loadValueTypes(ConfigurationLoader loader) {
        // Adds predefined types.
        loader.addType(new ByteArrayType());                          // byte[]   to String using Base64
        loader.addType(new ArrayType());                              // All other arrays.
        loader.addType(new ListType());                               // Lists.
        loader.addType(new MapType());                                // Maps
        loader.addType(new EnumType());                               // Enums
        loader.addType(new ConfigurationType());                      // Other configurations.
        loader.addType(new NumberType());                             // Plain scalars.
        loader.addType(new StringType());                             // Simple strings.
        loader.addType(new FileType());                               // Paths.
        loader.addType(new SerializableType());                       // All other serializable types.
    }
}
