package net.stuxcrystal.simpledev.commands.commands.contrib.annotations.autoparse;

import java.lang.annotation.*;

/**
 * <p>All parameters with this annotation will contain the executor.</p>
 * <p>
 *     If the parameter is not a CommandExecutor, we will pass the handle.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Executor {}
