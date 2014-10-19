package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.configuration.parser.annotations.Configuration;
import net.stuxcrystal.configuration.parser.annotations.Value;

/**
 * The configuration.
 */
@Configuration
public class InterBanConfig {

    @Value(name="enable-history", comment={
            "Example configuration value. True/False are allowed."
    })
    public boolean enableHistory = false;
}
