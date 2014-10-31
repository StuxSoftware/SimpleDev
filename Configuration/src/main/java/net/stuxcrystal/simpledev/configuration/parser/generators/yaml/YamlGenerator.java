/*
 * Copyright 2013 StuxCrystal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package net.stuxcrystal.simpledev.configuration.parser.generators.yaml;

import net.stuxcrystal.simpledev.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.simpledev.configuration.parser.NodeTreeGenerator;
import net.stuxcrystal.simpledev.configuration.parser.node.ArrayNode;
import net.stuxcrystal.simpledev.configuration.parser.node.DataNode;
import net.stuxcrystal.simpledev.configuration.parser.node.MapNode;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class YamlGenerator implements NodeTreeGenerator {

    /**
     * Make sure we get to initiate the yaml parser.
     */
    private static final YamlParser parser = new YamlParser();

    public YamlGenerator() {}

    public String getName() {
        return "yaml";
    }

    @Override
    public boolean isValidFileName(String name) throws IOException {
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }

    @Override
    public Node<?> parse(InputStream stream, ConfigurationHandler cParser) throws IOException {
        return YamlParser.parse(stream);
    }

    @Override
    public void dump(OutputStream stream, Node<?> node, ConfigurationHandler parser) throws IOException {
        dump(new OutputStreamWriter(stream), node, parser);

    }

    private void dump(Writer writer, Node<?> node, ConfigurationHandler parser) throws IOException {
        CommentAwareDumper dumper = new CommentAwareDumper(parser);
        dumper.dump(writer, node);
        writer.close();
    }
}
