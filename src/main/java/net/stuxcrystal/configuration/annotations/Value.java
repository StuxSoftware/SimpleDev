package net.stuxcrystal.configuration.annotations;

import java.lang.annotation.*;

/**
 * Marks a value.<p />
 * If used on a class this will be the header of the entire file.
 *
 * @author StuxCrystal
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Value {
    /**
     * The name of the value.<p />
     * If not given, it uses the name of the field.
     */
    public String name() default "";

    /**
     * The comment that is written above the value.
     */
    public String[] comment() default {};
}
