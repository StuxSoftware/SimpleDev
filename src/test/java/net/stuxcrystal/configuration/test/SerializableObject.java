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
