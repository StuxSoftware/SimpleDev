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

package net.stuxcrystal.simpledev.commandhandler.utils;


/**
 * Implements all colors of minecraft.
 */
public enum MessageColor {

    BLACK('0'),
    DARK_BLUE('1'),
    DARK_GREEN('2'),
    DARK_AQUA('3'),
    DARK_RED('4'),
    DARK_PURPLE('5'),
    GOLD('6'),
    LIGHT_GRAY('7'),
    DARK_GRAY('8'),
    LIGHT_BLUE('9'),
    LIGHT_GREEN('a'),
    LIGHT_AQUA('b'),
    LIGHT_RED('c'),
    LIGHT_PURPLE('d'),
    YELLOW('e'),
    WHITE('f'),

    MAGIC('k', true),
    BOLD('l', true),
    STRIKETHROUGH('m', true),
    UNDERLINE('n', true),
    ITALIC('o', true),

    RESET('r');

    public static final MessageColor RED = LIGHT_RED;
    public static final MessageColor BLUE = LIGHT_BLUE;
    public static final MessageColor GREEN = LIGHT_GREEN;
    public static final MessageColor AQUA = LIGHT_AQUA;
    public static final MessageColor GRAY = LIGHT_GRAY;

    /**
     * The internal code of the color.
     */
    private final char code;

    /**
     * Is this color tag formatting.
     */
    private final boolean format;

    /**
     * The color char.
     */
    public static final char COLOR_CHAR = '\u00A7';

    private MessageColor(char code) {
        this(code, false);
    }

    private MessageColor(char code, boolean format) {
        this.code = code;
        this.format = format;
    }

    /**
     * Converts this color to a string.
     *
     * @return The string representing the color.
     */
    public String toString() {
        return new String(new char[]{COLOR_CHAR, this.code});
    }

    /**
     * Checks if this is a format character.
     *
     * @return true if this color is a format tag.
     */
    public boolean isFormat() {
        return format;
    }
}
