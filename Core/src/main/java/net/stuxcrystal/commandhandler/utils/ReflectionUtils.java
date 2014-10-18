package net.stuxcrystal.commandhandler.utils;

import java.lang.reflect.*;

/**
 * Reflection utils.
 */
public class ReflectionUtils {


    /**
     * Extractes the {@link Class} out of the type.
     *
     * @param type The type where the class is to extract.
     * @return a Class object.
     */
    public static Class<?> toClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof GenericArrayType) {
            return Array.newInstance(toClass(((GenericArrayType) type).getGenericComponentType()), 0).getClass();
        } else {
            throw new IllegalArgumentException("This method does not support the type");
        }
    }


    /**
     * Returns the value of a field.
     *
     * @param cls    The class of the field.
     * @param object The instance of the class. Null to access static fields.
     * @param fields The field names.
     * @param <T>    The result type.
     * @return The value of the field.
     * @throws ReflectiveOperationException The field name.
     */
    public static <T> T getField(Class<?> cls, Object object, String... fields) throws ReflectiveOperationException {

        if (object == null) {
            Field field = cls.getDeclaredField(fields[0]);
            field.setAccessible(true);
            return (T) field.get(null);
        }

        Object current = object;
        Class<?> currentClass = cls;
        for (String fieldName : fields) {
            Field field;
            field = currentClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            object = field.get(object);
            if (object == null)
                throw new NullPointerException("ReflectionUtility: " + fieldName);

            currentClass = current.getClass();
        }

        return (T) current;
    }

    /**
     * Returns a field without throwing an ReflectiveOperationException.
     *
     * @param cls    The class that contains the field.
     * @param object The instance of the class.
     * @param fields The path of fields.
     * @param <T>    Type of the field.
     * @return The value of the field, or null if the an ReflectiveOperationException was thrown.
     */
    public static <T> T getFieldQuiet(Class<?> cls, Object object, String... fields) {
        try {
            return getField(cls, object, fields);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }



}
