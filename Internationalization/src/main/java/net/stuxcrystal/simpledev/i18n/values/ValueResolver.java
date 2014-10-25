package net.stuxcrystal.simpledev.i18n.values;

/**
 * Represents the value
 */
public interface ValueResolver {

    /**
     * Resolves a variable.
     * @param manager  The manager that searches for the variable.
     * @param name     The name of the variable.
     * @return The variable.
     * @throws net.stuxcrystal.simpledev.i18n.values.NotFoundException If the value couldn't be found.
     */
    public Object resolve(ValueManager manager, String name) throws NotFoundException;

}
