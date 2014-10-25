package net.stuxcrystal.simpledev.commandhandler.translations.contrib;

import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;
import net.stuxcrystal.simpledev.commandhandler.translations.ValueResolver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolver for enums so we can easily add enumerations
 * as values for translations.
 */
public class EnumResolver implements ValueResolver {

    /**
     * The reference to an enumeration.
     */
    private final Class<?> enumeration;

    /**
     * The enumeration for the values.
     * @param enumeration The enumeration to add.
     */
    public EnumResolver(Class<?> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public Map<String, String> getFormatMap(CommandExecutor executor) {
        Enum[] constants = (Enum[])enumeration.getEnumConstants();
        Map<String, String> result = new LinkedHashMap<>(constants.length);
        for (Enum constant : constants) {
            result.put(constant.name(), constant.toString());
        }
        return result;
    }
}
