package net.stuxcrystal.configuration.generators.yaml;

import org.yaml.snakeyaml.resolver.Resolver;

class StringOnlyResolver extends Resolver {

    /**
     * Don't implement any resolvers.
     */
    protected void addImplicitResolvers() {

    }

}
