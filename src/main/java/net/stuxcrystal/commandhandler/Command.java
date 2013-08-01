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
package net.stuxcrystal.commandhandler;

import java.lang.annotation.*;

/**
 * Defines a command.
 *
 * @author StuxCrystal
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Command {

    /**
     * The name of the command.<p />
     *
     * If the value doesn't have a value, the name of the method will be used.
     *
     * @return
     */
    public String value() default "";

    /**
     * The aliases of the command.
     *
     * @return
     */
    public String[] aliases() default {};

    /**
     * The permission needed to execute.<br>
     * Default: None
     *
     * @return
     */
    public String permission() default "";

    /**
     * The description of the command.
     *
     * @return
     */
    public String description() default "";

    /**
     * Can the command be executed as a player.<br>
     * Default: true
     *
     * @return
     */
    public boolean asConsole() default true;

    /**
     * Can the command be executed as a player.<br>
     * Default: true
     *
     * @return
     */
    public boolean asPlayer() default true;

    /**
     * Should the command be execute asynchronous.
     *
     * @return
     */
    public boolean async() default false;

    /**
     * All supported flags.
     *
     * @return
     */
    public String flags() default "";

    /**
     * The minimal count of arguments needed.
     *
     * @return
     */
    public int minSize() default -1;

    /**
     * The maximal count of arguments needed.
     *
     * @return
     */
    public int maxSize() default -1;

    /**
     * Should the command only executed as an operator.
     *
     * @return
     */
    public boolean opOnly() default false;

}
