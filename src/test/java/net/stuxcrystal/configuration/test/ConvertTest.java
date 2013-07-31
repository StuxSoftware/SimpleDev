package net.stuxcrystal.configuration.test;

import junit.framework.TestCase;
import net.stuxcrystal.commandhandler.TranslationManager;

import java.util.Map;

/**
 * Test conversions done in the library.
 */
public class ConvertTest extends TestCase {

    /**
     * Converts a string/string map to a string/object map.
     */
    public void testMapConvert() {
        Map<String, Object> map = (Map<String, Object>) (Map) TranslationManager.getDefaults();
    }
}
