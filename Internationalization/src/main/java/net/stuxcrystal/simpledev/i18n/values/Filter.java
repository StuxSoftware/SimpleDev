package net.stuxcrystal.simpledev.i18n.values;

/**
 * Implementation of a translation filter.
 */
public interface Filter {

    /**
     * Applies the filter to the given object.
     *
     * @param manager    The value manager for the values.
     * @param arguments  All arguments that are supported.
     * @return The result object.
     */
    public Object apply(ValueManager manager, Object arguments);

}
