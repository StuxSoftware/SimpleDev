package net.stuxcrystal.simpledev.commands.translations.contrib;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
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
    public Map<String, String> getFormatMap(CommandExecutor executor) {
        Map<String, String> proxied = this.resolver.getFormatMap(executor);
        Map<String, String> newMap = new LinkedHashMap<>(proxied.size());

        for (Map.Entry<String, String> entry : proxied.entrySet()) {
            newMap.put(this.prefix + entry.getKey(), entry.getValue());
        }

        return newMap;
    }
}
