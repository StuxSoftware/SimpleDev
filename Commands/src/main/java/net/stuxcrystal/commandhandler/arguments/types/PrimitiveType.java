package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;
import net.stuxcrystal.commandhandler.utils.ReflectionUtils;

import java.util.logging.Level;

/**
 * Primiive Argument type.
 */
public class PrimitiveType implements ArgumentType {


    /**
     * List of all class types.
     */
    public static final Class<?>[] WRAPPER_TYPES = new Class[]{
            Boolean.class, Character.class, Integer.class, Long.class,
            Short.class, Byte.class, Double.class, Float.class,
    };

    /**
     * Checks if the type is a wrapper type.
     *
     * @param cls
     * @return
     */
    private static boolean isWrapper(Class<?> cls) {

        for (Class<?> clz : WRAPPER_TYPES) {
            if (clz.equals(cls)) return true;
        }

        return false;

    }

    /**
     * Wraps a primitive class.
     *
     * @param before The primitive type.
     * @return The wrapper type.
     */
    public static Class<?> wrap(Class<?> before) {
        if (!before.isPrimitive()) return before;

        for (Class<?> clz : WRAPPER_TYPES) {
            if (ReflectionUtils.getFieldQuiet(clz, null, "TYPE").equals(before))
                return clz;
        }

        return before;
    }


    @Override
    public boolean isTypeSupported(Class<?> cls) {
        return ReflectionUtils.toClass(cls).isPrimitive() || isWrapper(ReflectionUtils.toClass(cls));
    }

    @Override
    public Object convert(String value, Class<?> toClass, CommandExecutor executor, CommandBackend backend) {
        try {
            if (!(toClass.isPrimitive() || isWrapper(toClass)))
                throw new InternalError("Invalid type.");

            Class<?> wrapper = wrap(toClass);
            try {
                if (wrapper.equals(Boolean.class))
                    return Boolean.valueOf(value);
                else if (wrapper.equals(Character.class))
                    return value.charAt(0);
                else if (wrapper.equals(Integer.class))
                    return Integer.valueOf(value);
                else if (wrapper.equals(Long.class))
                    return Long.valueOf(value);
                else if (wrapper.equals(Short.class))
                    return Short.valueOf(value);
                else if (wrapper.equals(Byte.class))
                    return Byte.valueOf(value);
                else if (wrapper.equals(Double.class))
                    return Double.valueOf(value);
                else if (wrapper.equals(Float.class))
                    return Float.valueOf(value);
                else
                    throw new InternalError("Failed to parse number-value.");
            } catch (NumberFormatException e) {
                throw new InternalError("Failed to parse string...", e);
            }
        } catch (InternalError e) {
            backend.getLogger().log(Level.WARNING, "Failed to convert value: " + value, e);
            return null;
        }
    }
}