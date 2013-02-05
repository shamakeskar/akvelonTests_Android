package com.linkedin.android.screens.base;

import com.linkedin.android.utils.WaitActions;

/**
 * 
 * @author alexander.makarov
 * @created Jan 11, 2013 3:55:34 PM
 */
@SuppressWarnings("rawtypes")
public abstract class BaseEditProfileScreen extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.jsbridge.ProfileEditActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ProfileEditActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public BaseEditProfileScreen() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Edit Profile (or subscreen)");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
}
