package net.stuxcrystal.commandhandler.utils;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;

/**
 * Attributes for a message.
 */
public class MessageAttributes extends AttributedCharacterIterator.Attribute {

    public static final MessageAttributes FORMAT = new MessageAttributes("format");

    /**
     * Constructs an {@code Attribute} with the given name.
     */
    protected MessageAttributes(String name) {
        super(name);
    }
}
