package org.gregsquad.gregsadventure.card;
import java.util.*;

/**
 * Utility class for loading configurations from the cards.properties file.
 */
public class ConfigLoader {


    private static final int MAX_ID = 600;
    public static final ResourceBundle cardsList = ResourceBundle.getBundle("config");
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

    
    public static int[] getIdTable() {
        int size = getInt("numberOfCards");
        int[] idTable = new int[size];
        int j = 0;
        for(int i = 0; i < MAX_ID; i++){
            if(ConfigLoader.isValid("" + i)){
                idTable[j] = i;
                j++;
            }
        }
        return idTable;
    }

    public static boolean isValid(String key) {
        try {
            return cardsList.containsKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves a boolean value from the cards.properties file.
     * If the key is not found or the value cannot be parsed as a boolean, returns false.
     *
     * @param key the key to retrieve the value
     * @return the boolean value associated with the key, or false if not found or unable to parse
     */
    public static boolean getBoolean(String key) {
        try {
            return Boolean.parseBoolean(cardsList.getString(key));
        } catch (Exception e) {
            return false;
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