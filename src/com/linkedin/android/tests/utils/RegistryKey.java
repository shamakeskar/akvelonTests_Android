package com.linkedin.android.tests.utils;

import java.util.Arrays;

import com.linkedin.android.utils.Logger;

/**
 * Object with data of action for registry this action in {@code Registry}.
 * 
 * @author alexander.makarov
 * @created Jan 17, 2013 9:04:28 PM
 */
public class RegistryKey {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    // Metric (name, string id) for action.
    private final String key;
    // Parameters to action method (strings via comma).
    private final Object[] payload;
    // Representation of action for toString method.
    private final String representation;

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Creates RegistryKey object. 
     * 
     * @param key name of action
     * @param payload payload for action
     */
    public RegistryKey(String key, Object... payload) {
        if (key == null || key.length() == 0) {
            String errorMessage = "Cannot create RegistryKey with empty 'key' value.";
            IllegalStateException exc = new IllegalStateException(errorMessage);
            Logger.e("", exc);
            throw exc;
        }
        this.key = key;
        this.payload = payload;
        representation = key + ": " + Arrays.toString(payload);
    }

    // METHODS --------------------------------------------------------------
    /**
     * Returns key.
     * 
     * @return String value.
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns payload.
     * 
     * @return String[] value.
     */
    public Object[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return representation;
    }
}
