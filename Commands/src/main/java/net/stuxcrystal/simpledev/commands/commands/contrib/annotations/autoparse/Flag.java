package net.stuxcrystal.simpledev.commands.commands.contrib.annotations.autoparse;

import java.lang.annotation.*;

/**
 * The flag.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Flag {

    /**
     * Support for flags.
     * @return The flag to check.
     */
    char value();
}
