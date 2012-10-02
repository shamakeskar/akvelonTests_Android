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
     * Gets {@code View} of specified {@code viewType} by its {@code idName}.
     * 
     * @param idName
     *            view id name.
     * @param viewType
     *            type of required view.
     * @return {@code View} of specified {@code viewType} with {@code idName} or
     *         <b>null</b> if there is no such view.
     */
    public static <T extends View> T getViewByName(ViewIdName idName, Class<T> viewType) {
        View view = getViewByName(idName);
        if (!viewType.isInstance(view)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T viewOfSpecifiedType = (T) view;
        return viewOfSpecifiedType;
    }

    /**
     * Gets {@code View} by it's {@code idName}.
     * 
     * @param idName
     *            view id name
     * @return {@code View} with specified {@code idName} or <b>null</b> if not
     *         found.
     */
    public static View getViewByName(ViewIdName idName) {
        Solo solo = DataProvider.getInstance().getSolo();
        int viewId = getIdByName(idName);
        return solo.getView(viewId);
    }

    /**
     * Gets view id by it's {@code idName}.
     * 
     * @param idName
     *            view id name.
     * @return id of view with specified {@code idName}.
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
    
    /**
     * Returns id for <b>idName</b>.
     * 
     * @param idName is ID name for resource.
     * @return integer value of resource ID or <b>0</b> if not found.
     */
    public static int getIdByIdName(String idName){
        Solo solo = DataProvider.getInstance().getSolo();
        Activity currentActivity = solo.getCurrentActivity();
        return currentActivity.getResources().getIdentifier(idName, null, null);
    }
}
