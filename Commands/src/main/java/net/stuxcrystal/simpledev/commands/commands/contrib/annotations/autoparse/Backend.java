package net.stuxcrystal.simpledev.commands.commands.contrib.annotations.autoparse;

import java.lang.annotation.*;

/**
 * Will return the server backend.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Backend {
}
