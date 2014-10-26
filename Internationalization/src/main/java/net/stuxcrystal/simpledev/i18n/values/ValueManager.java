package net.stuxcrystal.simpledev.i18n.values;

import net.stuxcrystal.simpledev.i18n.contrib.resolvers.BaseTypeResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manager for values.
 */
public class ValueManager {

    /**
     * Contains all values.
     */
    private List<ValueResolver> values = new ArrayList<>(Arrays.asList((ValueResolver)new BaseTypeResolver()));

    /**
     * Contains a list of filters.
     */
    private List<Filter> filters = new ArrayList<>();

    /**
     * Converts an object to a string.
     * @param value The value to convert.
     * @return The object to convert.
     */
    public String toString(Object value) {
        return null;
    }

}
