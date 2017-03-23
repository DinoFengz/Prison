/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Provides utilities for manipulating text.
 *
 * @author Faizaan A. Datoo
 * @since API 0.1
 */
public class Text {

    private static final long millisPerSecond = 1000;
    private static final long millisPerMinute = 60 * millisPerSecond;
    private static final long millisPerHour = 60 * millisPerMinute;
    private static final long millisPerDay = 24 * millisPerHour;
    private static final long millisPerWeek = 7 * millisPerDay;
    private static final long millisPerMonth = 31 * millisPerDay;
    private static final long millisPerYear = 365 * millisPerDay;
    private static final Map<String, Long> unitMillis = CollectionUtil
        .map("years", millisPerYear, "months", millisPerMonth, "weeks", millisPerWeek, "days",
            millisPerDay, "hours", millisPerHour, "minutes", millisPerMinute, "seconds",
            millisPerSecond);
    private static String headingLine = repeat("-", 52);

    private Text() {
    }

    /**
     * Splits a string at a delimiter. The delimiter may include regular expressions to assist in
     * splitting.
     *
     * @param text      The text to split.
     * @param delimiter The delimiter to split at, which may contain regular expressions.
     * @return The array of strings, split at the delimiter. The delimiter is not included in any of
     * the entries.
     */
    public static String[] explodeRegex(String text, String delimiter) {
        return text.split(delimiter);
    }

    /**
     * Splits a string at a delimiter. Any RegEx characters found in the delimiter string will be
     * ignored, which is useful in many cases.
     *
     * @param text      The text to split.
     * @param delimiter The delimiter to split at, with any the regular expression characters
     *                  ignored.
     * @return The array of strings, split at the delimiter. The delimiter is not included in any of
     * the entries.
     */
    public static String[] explode(String text, String delimiter) {
        return explodeRegex(text, Pattern.quote(delimiter));
    }

    /**
     * Replaces the last occurrence of a String delimiter, found by a regular expression, with a
     * replacement string.
     *
     * @param text        The text.
     * @param regex       The regular expression, which is used to find the last occurrence.
     * @param replacement The replacement string.
     * @return The string, with the last occurrence replaced.
     */
    public static String replaceLastRegex(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    /**
     * Replaces the last occurrence of a String delimiter. Regular expressions will be ignored.
     *
     * @param text        The text.
     * @param toReplace   The text to find the last occurrence of, and replace.
     * @param replacement The replacement string.
     * @return The string, with the last occurrence replaced.
     */
    public static String replaceLast(String text, String toReplace, String replacement) {
        int pos = text.lastIndexOf(toReplace);
        if (pos > -1) {
            return text.substring(0, pos) + replacement + text
                .substring(pos + toReplace.length(), text.length());
        } else {
            return text;
        }
    }

    /**
     * Combine an array of strings, inserting a string to separate them (the glue).
     *
     * @param text The array of strings to combine.
     * @param glue The glue to put between the combined strings.
     * @return The combined string.
     */
    public static String implode(String[] text, String glue) {
        StringBuilder builder = new StringBuilder();
        for (String t : text) {
            builder.append(t).append(glue);
        }
        return replaceLast(builder.toString(), glue, "");
    }

    /*
     * Time-related utilities
     */

    /**
     * Combines an array of strings, inserting a comma between each entry.
     *
     * @param text The array of strings to combine.
     * @return The combined string.
     */
    public static String implodeWithComma(String[] text) {
        return implode(text, ",");
    }

    /**
     * Combines an array of strings, inserting a comma between each entry and a dot at the end.
     *
     * @param text The array of strings to combine.
     * @return The combined string.
     */
    public static String implodeCommaAndDot(String[] text) {
        return implodeWithComma(text) + ".";
    }

    /**
     * Translates the color codes (a-f) (A-F) (0-9), prefixed by a certain character, into
     * Minecraft-readable color codes. <p> <p>Use of this method is discouraged. Implementations are
     * recommended to translate color codes using their native internal's APIs. This assumes that the
     * server mod will accept vanilla Minecraft color codes, although implementations such as Sponge
     * do not do this. However, because there are some practical uses for a method like this, it
     * exists in a non-deprecated but discouraged state.
     *
     * @param text   The text to translate color codes in.
     * @param prefix The color code prefix, which comes before the color codes.
     * @return The translated string.
     */
    public static String translateColorCodes(String text, char prefix) {
        if (prefix == 167) {
            return text; // No need to translate, it's already been translated
        }
        char[] b = text.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == prefix && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167; // Section symbol
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    /**
     * Translates color codes (a-f) (A-F) (0-9), prefixed by an ampersand, into Minecraft-readable
     * color codes. <p> <p>Use of this method is discouraged. Implementations are recommended to
     * translate color codes using their native internal's APIs. This assumes that the server mod will
     * accept vanilla Minecraft color codes, although implementations such as Sponge do not do this.
     * However, because there are some practical uses for a method like this, it exists in a
     * non-deprecated but discouraged state.
     *
     * @param text The text to translate.
     * @return The translated string.
     * @see #translateColorCodes(String, char)
     */
    public static String translateAmpColorCodes(String text) {
        return translateColorCodes(text, '&');
    }

    /**
     * Converts a double (3.45) into a US-localized currency string ($3.45).
     *
     * @param number The number to format.
     * @return The formatted string.
     * @see NumberFormat#getCurrencyInstance(Locale)
     */
    public static String numberToDollars(double number) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(number);
    }

    /**
     * Repeats some text n amount of times.
     *
     * @param txt The text to repeat.
     * @param n   The amount of times to repeat it.
     * @return The repeated string.
     */
    public static String repeat(String txt, int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(txt);
        }
        return builder.toString();
    }

    /**
     * Generates a title heading for a chat display.
     *
     * @param txt The {@link String} to show as the title.
     * @return The title heading string.
     */
    public static String titleize(String txt) {
        txt = translateAmpColorCodes("&3" + txt);

        String center = translateAmpColorCodes("&7< " + txt + " &7>");

        int centerlen = center.length();
        int pivot = headingLine.length() / 2;

        int balance = -1;
        int eatLeft = (centerlen / 2) - balance;
        int eatRight = (centerlen - eatLeft) + balance;

        if (eatLeft < pivot) {
            return translateAmpColorCodes(
                "&8" + (headingLine.substring(0, pivot - eatLeft)) + " " + center + " &8"
                    + (headingLine.substring(pivot + eatRight)));
        } else {
            return center;
        }
    }

    /**
     * Prepends a 4-space tab in front of some text.
     *
     * @param text The text to tab in front of.
     * @return The tabbed string.
     */
    public static String tab(String text) {
        return "&f    " + text;
    }

    /**
     * Takes a millisecond amount and converts it into a pretty, human-readable string.
     *
     * @param millis The time until (or since if this is negative) something occurs.
     * @return The human-readable string.
     */
    public static String getTimeUntilString(long millis) {
        String ret = "";

        double millisLeft = (double) Math.abs(millis);

        List<String> unitCountParts = new ArrayList<>();
        for (Map.Entry<String, Long> entry : unitMillis.entrySet()) {
            if (unitCountParts.size() == 3) {
                break;
            }
            String unitName = entry.getKey();
            long unitSize = entry.getValue();
            long unitCount = (long) Math.floor(millisLeft / unitSize);
            if (unitCount < 1) {
                continue;
            }
            millisLeft -= unitSize * unitCount;
            unitCountParts.add(unitCount + " " + unitName);
        }

        if (unitCountParts.size() == 0) {
            return "just now";
        }

        ret += implodeWithComma(unitCountParts.toArray(new String[unitCountParts.size()]));
        ret += " ";
        if (millis <= 0) {
            ret += "ago";
        } else {
            ret += "from now";
        }

        return ret;
    }

    /**
     * Makes a base noun plural, depending on the quantity. For example,
     * entering "thing" as the base noun would make it "things", if the quantity
     * exceeds one.
     *
     * @param baseNoun The non-plural base noun. Example: "thing"
     * @param quantity The amount.
     * @return The pluralized string.
     */
    public static String pluralize(String baseNoun, int quantity) {
        if (quantity == 1) {
            return baseNoun;
        } else {
            return baseNoun + "s";
        }
    }


}
