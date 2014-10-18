package net.stuxcrystal.commandhandler.component;

import java.lang.annotation.*;

/**
 * Defines a component.<p />
 *
 * Any method that are annotated by this annotation must have the following format:<br />
 * {@code ReturnType ComponentName(CommandExecutor ce[, ...]);}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Component {

}
