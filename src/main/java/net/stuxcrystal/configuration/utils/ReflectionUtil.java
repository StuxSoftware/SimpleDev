package net.stuxcrystal.configuration.utils;

import java.lang.reflect.*;

/**
 * This class stores all utilities used to perform reflection operations.
 *
 * @author StuxCrystal
 */
public final class ReflectionUtil {

    /**
     * Returns the component type of the array by the class passed to the function. If the passed class is not an array
     * the class will be returned.
     *
     * @param cls The class to retrieve the component type.
     * @return the component type.
     */
    public static Class<?> getComponentType(Class<?> cls) {
        Class<?> current = cls;
        while (current.isArray())
            current = cls.getComponentType();
        return current;
    }

    /**
     * Instantiates the given class without throwing any exception.
     *
     * @param cls The class to be instantiated.
     * @return null if the instantiation failes.
     */
    public static <T> T newInstance(Class<? extends T> cls) {

        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the generic type argument.
     *
     * @param type
     * @return
     */
    public static Type[] getGenericArguments(Type type) {
        if (type instanceof GenericArrayType) {
            return getGenericArguments(((GenericArrayType) type).getGenericComponentType());
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameters = (ParameterizedType) type;
            return parameters.getActualTypeArguments();
        } else {
            return new Class<?>[0];
        }
    }

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
