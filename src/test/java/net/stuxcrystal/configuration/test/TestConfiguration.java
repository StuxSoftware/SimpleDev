package net.stuxcrystal.configuration.test;

import net.stuxcrystal.configuration.annotations.Configuration;
import net.stuxcrystal.configuration.annotations.Value;

@Configuration
public class TestConfiguration {

    @Value(comment = {"", "Dies ist ein Testkommentar."})
    public String test = "ABC\n D\n";

    public String[] values = new String[]{"abc", "def", "ghi>"};

    public byte[] byteArrayTest = new byte[]{(byte) 0xf0, (byte) 0x00, (byte) 0xac};

    public SerializableObject so = new SerializableObject();
}