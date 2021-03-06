/*
 * Copyright 2013 StuxCrystal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package net.stuxcrystal.simpledev.configuration.parser.annotations;

import java.lang.annotation.*;

/**
 * Marks a value.<p />
 * Please note that this annotation can also be used on methods of Interfaces.
 *
 * @author StuxCrystal
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
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

    /**
     * Is the value transient.
     */
    public boolean transientValue() default false;
}
