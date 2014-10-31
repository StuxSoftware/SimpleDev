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
package net.stuxcrystal.simpledev.commands.commands.contrib.annotations;

import java.lang.annotation.*;

/**
 * Defines a command.<p />
 *
 * The command always looks like this:<br />
 * {@code void &lt;CommandName&gt;(CommandExecutor sender, ArgumentParser args);}
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
     * @return The name of the command.
     */
    public String value() default "";

    /**
     * The aliases of the command.
     *
     * @return The aliases of the command.
     */
    public String[] aliases() default {};

    /**
     * The permission needed to execute.<br>
     * Default: None
     *
     * @return The permission for the command.
     */
    public String permission() default "";

    /**
     * The description of the command.
     *
     * @return The description
     */
    public String description() default "";

    /**
     * Can the command be executed as a player.<br>
     * Default: true
     *
     * @return Can the console execute the command.
     */
    public boolean asConsole() default true;

    /**
     * Can the command be executed as a player.<br>
     * Default: true
     *
     * @return Can a player execute the command.
     */
    public boolean asPlayer() default true;

    /**
     * Should the command be execute asynchronous.
     *
     * @return Should the command be executed asynchronously?
     */
    public boolean async() default false;

    /**
     * All supported flags.
     *
     * @return All supported flags.
     */
    public String flags() default "";

    /**
     * The minimal size of arguments needed.
     *
     * @return The minimal amount of arguments.
     */
    public int minSize() default -1;

    /**
     * The maximal size of arguments needed.
     *
     * @return the maximal amount of arguments.
     */
    public int maxSize() default -1;

    /**
     * Should the command only executed as an operator if no permissions-system is supported.
     *
     * @return If we fall back to operator permissions, are the operators the only one who are allowed to use the command?
     */
    public boolean opOnly() default false;

}
