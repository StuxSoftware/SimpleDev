package net.stuxcrystal.simpledev.commands.translations.contrib;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.translations.TranslationManager;
import net.stuxcrystal.simpledev.commands.translations.ValueResolver;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolver for enums so we can easily add enumerations
 * as values for translations.
 */
public class EnumResolver implements ValueResolver {

    /**
     * Contains all objects.
     */
    private final Map<String, Enum> constantMap;

    /**
     * The enumeration for the values.
     * @param enumeration The enumeration to add.
     */
    public EnumResolver(Class<?> enumeration) {
        this.constantMap = new HashMap<>(enumeration.getEnumConstants().length);
        for (Enum constant : (Enum[])enumeration.getEnumConstants()) {
            this.constantMap.put(constant.name(), constant);
        }
    }

    @Override
    public Enum get(TranslationManager manager, CommandExecutor<?> executor, String name) {
        return this.constantMap.get(name);
    }
}
