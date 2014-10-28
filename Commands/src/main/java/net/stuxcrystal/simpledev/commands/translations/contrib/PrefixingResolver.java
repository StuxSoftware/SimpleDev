package net.stuxcrystal.simpledev.commands.translations.contrib;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.translations.TranslationManager;
import net.stuxcrystal.simpledev.commands.translations.ValueResolver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A "proxy" resolver that uses prefixes.
 * <p />
 * Please note that the resolver will try to preserve the
 * the order the values are returned.
 */
public class PrefixingResolver implements ValueResolver {

    /**
     * The actual prefix.
     */
    private final String prefix;

    /**
     * The resolver.
     */
    private final ValueResolver resolver;

    /**
     * Creates a new prefixing resolver with the given prefix.
     * @param prefix    The prefix of the resolver.
     * @param resolver  The resolver to prefix.
     */
    public PrefixingResolver(String prefix, ValueResolver resolver) {
        this.prefix = prefix;
        this.resolver = resolver;
    }

    @Override
    public Object get(TranslationManager manager, CommandExecutor<?> executor, String name) {
        if (name.startsWith(prefix))
            return this.resolver.get(manager, executor, name.substring(prefix.length()));
        return null;
    }
}
