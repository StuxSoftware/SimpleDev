package net.stuxcrystal.commandhandler;

import java.lang.annotation.*;

/**
 * Defines a subcommand.
 *
 * @author StuxCrystal
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubCommand {

    /**
     * The class used for the subcommand.
     *
     * @return A list of classobject that can be instantiated without an argument.
     */
    public Class<?>[] value();

    /**
     * When should the subcommand be called. Defaults to never.
     *
     * @return The time when the function is to be called.
     */
    public CallTime time() default CallTime.NEVER;
}
