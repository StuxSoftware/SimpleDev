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

package net.stuxcrystal.configuration.test;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: stuxcrystal
 * Date: 25.07.13
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class SerializableObject implements Serializable {

    String testString = "abcdef";
    int testInt = 789985;
    byte[] byteArray = new byte[]{(byte) 0xf0, (byte) 0x70, (byte) 0x98, (byte) 0xff};


}
