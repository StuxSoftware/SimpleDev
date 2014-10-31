package net.stuxcrystal.simpledev.commands.arguments.types;

import net.stuxcrystal.simpledev.commands.CommandBackend;
import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentType;
import net.stuxcrystal.simpledev.commands.utils.ReflectionUtils;

/**
 * Implementation for enumerations.
 */
public class EnumType implements ArgumentType {

    @Override
    public boolean isTypeSupported(Class<?> cls) {
        return ReflectionUtils.toClass(cls).isEnum();
    }

    @Override
    public Object convert(String value, Class<?> toClass, CommandExecutor executor, CommandBackend backend) {
        Enum[] enumValues = (Enum[])toClass.getEnumConstants();

        // Try exact name matching.
        for (Enum eval : enumValues) {
            if (eval.name().equals(value))
                return eval;
        }

        // Match without any regards to case.
        for (Enum eval : enumValues) {
            if (eval.name().equalsIgnoreCase(value))
                return eval;
        }

        throw new NumberFormatException("Unknown enum value");
    }
}
