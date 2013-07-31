package net.stuxcrystal.configuration.test;

import junit.framework.TestCase;
import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.exceptions.ConfigurationException;
import net.stuxcrystal.configuration.generators.xml.XmlGenerator;
import net.stuxcrystal.configuration.generators.yaml.YamlGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Tests XML.
 */
public class XMLDumpTest extends TestCase {

    public void testDump() throws ConfigurationException, ReflectiveOperationException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ConfigurationLoader cl = new ConfigurationLoader();
        cl.dumpStream(baos, new XmlGenerator(), new TestConfiguration());

        // System.out.println(baos.toString("UTF-8"));

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        TestConfiguration tc = cl.parseStream(bais, new XmlGenerator(), TestConfiguration.class);

        assertEquals(tc.test, "ABC\n D");
        assertTrue(Arrays.equals(tc.values, new String[]{"abc", "def", "ghi>"}));
        assertTrue(Arrays.equals(tc.byteArrayTest, new byte[]{(byte) 0xf0, (byte) 0x00, (byte) 0xac}));

        cl.convert(new ByteArrayInputStream(baos.toByteArray()), (baos = new ByteArrayOutputStream()),
                new XmlGenerator(), new YamlGenerator(),
                TestConfiguration.class);

        System.out.println(baos.toString());
    }

}
