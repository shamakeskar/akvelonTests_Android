package com.linkedin.android.tests.data;

import junit.framework.Assert;
import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;

/**
 * Class for work with string ID's of UI elements in LinkedIn.apk.
 * 
 * @author alexey.makhalov
 * @created Sep 4, 2012 3:21:16 PM
 */
public class Id {
    // CONSTANTS ------------------------------------------------------------
    // Target package.
    public static final String ANDROID_PACKAGE_NAME = "android";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Gets {@code View} by it's {@code idName}.
     * 
     * @param name
     *            view id name
     * @return {@code View} with specified {@code idName}
     */
    public static View getViewByName(ViewIdName idName) {
        Solo solo = DataProvider.getInstance().getSolo();
        int viewId = getIdByName(idName);
        return solo.getView(viewId);
    }
    
    /**
     * Gets view id by it's {@code idName}.
     * 
     * @param name
     *            view id name
     * @return id of view with specified {@code idName}
     */
    private static int getIdByName(ViewIdName idName) {
        Solo solo = DataProvider.getInstance().getSolo();
        Activity currentActivity = solo.getCurrentActivity();
        Assert.assertNotNull("There is no current activity", currentActivity);

        Resources currentResources = currentActivity.getResources();
        Assert.assertNotNull("There is no resources on current activity", currentResources);

        int id = currentResources.getIdentifier(idName.toString(), null, null);

        Assert.assertTrue("Can't get ID by name: " + idName, id > 0);
        return id;
    }
}