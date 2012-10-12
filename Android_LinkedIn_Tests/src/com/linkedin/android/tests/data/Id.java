package com.linkedin.android.tests.data;

import java.util.ArrayList;

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
        View view = getViewByViewIdName(idName);
        if (!viewType.isInstance(view)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T viewOfSpecifiedType = (T) view;
        return viewOfSpecifiedType;
    }

    /**
     * Returns {@code View} by it's {@code idName}.
     * 
     * @param idName
     *            view id name
     * @return {@code View} with specified {@code idName} or <b>null</b> if not
     *         found.
     */
    public static View getViewByViewIdName(ViewIdName idName) {
        Solo solo = DataProvider.getInstance().getSolo();
        int viewId = getIdByViewIdName(idName);
        return solo.getView(viewId);
    }

    /**
     * Returns view id by it's {@code idName}.
     * 
     * @param idName
     *            view id name.
     * @return id of view with specified {@code idName}.
     */
    private static int getIdByViewIdName(ViewIdName idName) {
        Activity currentActivity = DataProvider.getInstance().getSolo().getCurrentActivity();
        Assert.assertNotNull("Error getting current Activity", currentActivity);
        Resources resources = currentActivity.getResources();
        Assert.assertNotNull("Error getting Resources form current Activity", resources);

        int id = resources.getIdentifier(idName.toString(), null, null);
        return id;
    }

    /**
     * Returns id for <b>idName</b>.
     * 
     * @param idName
     *            is ID name for resource.
     * @return integer value of resource ID or <b>0</b> if not found.
     */
    public static int getIdByIdName(String idName) {
        Solo solo = DataProvider.getInstance().getSolo();
        Activity currentActivity = solo.getCurrentActivity();
        return currentActivity.getResources().getIdentifier(idName, null, null);
    }

    /**
     * Returns {@code ArrayList} of {@code View} with specified id.
     * 
     * @param viewsId
     *            ID of views
     * @return {@code ArrayList} of {@code View} with specified ID
     */
    public static ArrayList<View> getListOfViewById(int viewsId) {
        ArrayList<View> list = new ArrayList<View>();
        for (View view : DataProvider.getInstance().getSolo().getViews()) {
            if (view.getId() == viewsId) {
                list.add(view);
            }
        }
        return list;
    }

    /**
     * Returns {@code ArrayList} of {@code View} by {@code ViewIdName}
     * 
     * @param idName
     *            {@code ViewIdName} of views
     * @return {@code ArrayList} of {@code View} with specified
     *         {@code ViewIdName}
     */
    public static ArrayList<View> getListOfViewByViewIdName(ViewIdName idName) {
        return getListOfViewById(getIdByViewIdName(idName));
    }
}
