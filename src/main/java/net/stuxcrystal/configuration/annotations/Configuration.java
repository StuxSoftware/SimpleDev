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

    /**
     * If the node representing the configuration has no comment, use
     * this to define a default header.
     */
    public String[] header() default {};

}
