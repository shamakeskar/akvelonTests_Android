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
    EDIT_TEXT("EditText");
    private String typeValue;
    
    private ANDROID_VIEW_NAMES(String type) {
        typeValue = type;
    }
    
    static public ANDROID_VIEW_NAMES getType(String pType) {
        for (ANDROID_VIEW_NAMES type: ANDROID_VIEW_NAMES.values()) {
            if (type.getTypeValue().equals(pType)) {
                return type;
            }
        }
        return UNKNOWN;
    }
    
    public String getTypeValue() {
        return typeValue;
    }
}
