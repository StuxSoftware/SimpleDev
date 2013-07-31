package net.stuxcrystal.configuration.annotations;

import java.lang.annotation.*;

/**
 * Marks a ConfigurationSection.
 *
 * @author StuxCrystal
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Configuration {

}
