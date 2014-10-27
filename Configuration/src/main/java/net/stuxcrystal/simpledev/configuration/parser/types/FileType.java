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

package net.stuxcrystal.simpledev.configuration.parser.types;

import net.stuxcrystal.simpledev.configuration.parser.ConfigurationParser;
import net.stuxcrystal.simpledev.configuration.parser.ValueType;
import net.stuxcrystal.simpledev.configuration.parser.exceptions.ValueException;
import net.stuxcrystal.simpledev.configuration.parser.node.DataNode;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import net.stuxcrystal.simpledev.configuration.parser.utils.ReflectionUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class FileType implements ValueType<File> {

    @Override
    public boolean isValidType(Object object, Type cls) throws ReflectiveOperationException {
        return File.class.isAssignableFrom(ReflectionUtil.toClass(cls));
    }

    @Override
    @SuppressWarnings("unchecked")
    public File parse(Object object, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        if (!value.hasChildren())
            throw new ValueException("The node has no values.");
        return new File(((Node<String>) value).getData());
    }

    @Override
    public Node<?> dump(Object object, ConfigurationParser parser, Type type, File data) throws ReflectiveOperationException, ValueException {
        return new DataNode(data.getPath());
    }


}
