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

package net.stuxcrystal.configuration.exceptions;

/**
 * Thrown when an invalid value is given.
 *
 * @author StuxCrystal
 */
public class ValueException extends ConfigurationException {

    private static final long serialVersionUID = 1L;

    public ValueException() {
    }

    public ValueException(String arg0) {
        super(arg0);
    }

    public ValueException(Throwable arg0) {
        super(arg0);
    }

    public ValueException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ValueException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}