package net.stuxcrystal.configuration;

/**
 * Constructs an ConfigurationLoader.
 */
public interface Constructor {

    /**
     * Loads generators.
     *
     * @param loader The loader where to add the generators.
     */
    public void loadGenerators(ConfigurationLoader loader);

    /**
     * Loads value types.
     *
     * @param loader The loader where to add the types.
     */
    public void loadValueTypes(ConfigurationLoader loader);

}
