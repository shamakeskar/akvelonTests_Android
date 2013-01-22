package com.linkedin.android.utils.viewUtils;

/**
 * Enum for strings with android widgets names. For use its in 'switch'.
 * 
 * @author alexander.makarov
 * @created Aug 6, 2012 7:34:04 PM
 */
public enum ANDROID_VIEW_NAMES {
    UNKNOWN("unknown"),
    BUTTON("Button"),
    TEXT_VIEW("TextView"),
    LIST_VIEW("ListView"),
    PROGRESS_BAR("ProgressBar"),
    IMAGE_VIEW("ImageView"),
    IMAGE_BUTTON("ImageButton"),
    EDIT_TEXT("EditText"),
    LINK_TEXT_VIEW("LinkTextView");
    private String typeValue;
    
    /**
     * Constructor for ANDROID_VIEW_NAMES.
     * 
     * @param type associated string (for compare in switch-case).
     */
    private ANDROID_VIEW_NAMES(String type) {
        typeValue = type;
    }
    
    /**
     * Returns {@code ANDROID_VIEW_NAMES} object associated with this string.
     * 
     * @param pType associated string.
     * @return {@code ANDROID_VIEW_NAMES} object associated with this string.
     */
    static public ANDROID_VIEW_NAMES getType(String pType) {
        for (ANDROID_VIEW_NAMES type: ANDROID_VIEW_NAMES.values()) {
            if (type.getTypeValue().equals(pType)) {
                return type;
            }
        }
        return UNKNOWN;
    }
    
    /**
     * Returns associated string.
     * 
     * @return associated string.
     */
    public String getTypeValue() {
        return typeValue;
    }
}
