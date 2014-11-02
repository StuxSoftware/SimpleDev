package net.stuxcrystal.simpledev.commands.commands.contrib.annotations.autoparse;

import java.lang.annotation.*;

/**
 * Tells the command how to parse the argument.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Argument {

    /**
     * The value index.
     * @return The value index.
     */
    int value();

    /**
     * The stop index. (List only)
     * @return The maximal index of the argument.
     */
    int stop() default Integer.MAX_VALUE;

    /**
     * The step for the argument slice. (List only)
     * @return The argument slice.
     */
    int step() default 1;

    /**
     * Contains the default value. (Non-Array only)
     * @return The default value.
     */
    String defaultValue() default "§";

    /**
     * The type of the annotation (used when the type of the argument cannot be determined from the parameter type)
     * @return The type of the annotation.
     */
    Class<?> type() default void.class;
}
