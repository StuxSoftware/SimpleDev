package net.stuxcrystal.simpledev.i18n.contrib.filters;

import net.stuxcrystal.simpledev.i18n.values.Filter;
import net.stuxcrystal.simpledev.i18n.values.ValueManager;

/**
 * Filter that escapes the string.
 */
public class EscapeFilter implements Filter {

    @Override
    public Object apply(ValueManager manager, Object value, String arguments) {
        if (!(value instanceof CharSequence))
            value = manager.toString(value);

        String val = value.toString();
        return val;
    }
}
