package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;
import net.stuxcrystal.configuration.exceptions.ValueException;
import net.stuxcrystal.configuration.types.NumberType;

import java.util.logging.Level;

/**
 * Primiive Argument type.
 */
public class PrimitiveType extends NumberType implements ArgumentType {

    @Override
    public boolean isTypeSupported(Class<?> cls) {
        try {
            return this.isValidType(null, null, cls);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    @Override
    public Object convert(String value, Class<?> toClass, CommandExecutor executor, CommandBackend backend) {
        try {
            return parseValue(toClass, value);
        } catch (ValueException e) {
            backend.getLogger().log(Level.WARNING, "Failed to convert value: " + value, e);
            return null;
        }
    }
}
