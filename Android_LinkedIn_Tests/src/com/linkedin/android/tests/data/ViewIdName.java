package com.linkedin.android.tests.data;

/**
 * Class for represent resource names for LinkedIn app.
 * 
 * @author alexey.makhalov
 * @created Sep 7, 2012 3:21:16 PM
 */
public class ViewIdName {
    
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    // Resource name (format: Package:Type/Entry, Type equals "id" constantly)
    private String fullName;

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Creates {@code ViewIdName} object by entry name.
     * 
     * @param entry is name of Entry (see resource name format)
     */
    public ViewIdName(String entry) {
        fullName = DataProvider.TARGET_PACKAGE_ID + ":id/" + entry;
    }

    /**
     * Creates {@code ViewIdName} object by package name and {@code View} entry name.
     * 
     * @param packageName is name of Package (see resource name format)
     * @param entry is name of Entry (see resource name format)
     */
    public ViewIdName(String packageName, String entry) {
        fullName = packageName + ":id/" + entry;
    }
    
    // METHODS --------------------------------------------------------------
    /**
     * Returns fullName.
     */
    public String toString() {
        return fullName;
    }

}
