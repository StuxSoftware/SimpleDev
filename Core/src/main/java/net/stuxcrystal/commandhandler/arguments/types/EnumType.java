package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;

/**
 * Implementation for enumerations.
 */
public class EnumType extends net.stuxcrystal.configuration.types.EnumType implements ArgumentType {


    @Override
    public boolean isTypeSupported(Class<?> cls) {
        try {
            return isValidType(null, null, cls);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    @Override
    public Object convert(String value, Class<?> toClass, CommandExecutor executor, CommandBackend backend) {
        return convert(value, toClass);
    }
}
