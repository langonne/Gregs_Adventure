package org.gregsquad.gregsadventure.card;
import java.util.*;

/**
 * Utility class for loading configurations from the cards.properties file.
 */
public class ConfigLoader {

    public static final ResourceBundle cardsList = ResourceBundle.getBundle("../resources/config"); // Load cards.properties

    /**
     * Retrieves an integer value from the cards.properties file.
     * If the key is not found or the value cannot be parsed as an integer, returns -1.
     *
     * @param key the key to retrieve the value
     * @return the integer value associated with the key, or -1 if not found or unable to parse
     */
    public static int getInt(String key) {
        try {
            return Integer.parseInt(cardsList.getString(key));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retrieves a string value from the cards.properties file.
     * If the key is not found, returns null.
     *
     * @param key the key to retrieve the value
     * @return the string value associated with the key, or null if not found
     */
    public static String getString(String key) {
        try {
            return cardsList.getString(key);
        } catch (Exception e) {
            return null;
        }
    }
}