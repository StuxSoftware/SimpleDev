package net.stuxcrystal.simpledev.i18n.contrib.resolvers;

import net.stuxcrystal.simpledev.i18n.values.NotFoundException;
import net.stuxcrystal.simpledev.i18n.values.ValueManager;
import net.stuxcrystal.simpledev.i18n.values.ValueResolver;

/**
 * Resolver that supports basic types.
 */
public class BaseTypeResolver implements ValueResolver {

    @Override
    public Object resolve(ValueManager manager, String name) throws NotFoundException {

        switch (name) {
            // Null as a value.
            case "null":
                return null;

            // Boolean objects.
            case "true":
                return true;

            case "false":
                return false;

            default:
                throw new NotFoundException();
        }
    }
}
