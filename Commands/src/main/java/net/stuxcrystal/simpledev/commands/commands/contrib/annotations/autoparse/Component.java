package net.stuxcrystal.simpledev.commands.commands.contrib.annotations.autoparse;

import java.lang.annotation.*;

/**
 * <p>Any parameter with this annotation will cause the parameter to be converted to its component.</p>
 * <p>
 *     If the resolved object is not derived from the type {@link net.stuxcrystal.simpledev.commands.utils.HandleWrapper}
 *     this annotation will be ignored.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Component {}
