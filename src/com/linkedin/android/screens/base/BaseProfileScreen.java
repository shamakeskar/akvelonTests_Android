package com.linkedin.android.screens.base;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.ImageView;

import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ViewAssertUtils;

/**
 * Base class for profile screens.
 * 
 * @author vasily.gancharov
 * @created Aug 21, 2012 16:52:37 PM
 */
public abstract class BaseProfileScreen extends BaseListScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.profile.v2.ViewProfileActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewProfileActivity";

    protected static final String CONNECTIONS_LABEL = "Connections";
    protected static final String RECENT_ACTIVITY_LABEL = "Recent Activity";
    protected static final String EXPERIENCE_LABEL = "Experience";
    protected static final String IN_COMMON_LABEL = "In Common";
    
    // ImageView: profile photo
    private static final ViewIdName PHOTO_ID = new ViewIdName("picture");

    // CONSTRUCTORS ---------------------------------------------------------
    public BaseProfileScreen(String activityName) {
        super(activityName);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();
        WaitActions.waitForTrueInFunction("Photo is not present", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ImageView photoView = (ImageView) Id.getViewByViewIdName(PHOTO_ID);
                return ((photoView != null) && (photoView.isShown()));
            }
        });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Profile");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
    
    /**
     * Gets profile photo frame
     * 
     * @return {@code ImageView} profile photo frame
     */
    protected ImageView getProfilePhotoFrame() {
        // TODO: Implement me!
        return getProfilePhoto();
        /*
    	ImageView photo = getProfilePhoto();
    	RelativeLayout parent = (RelativeLayout)photo.getParent();
    	for (int i = 0; i < parent.getChildCount(); i++) {
    		View child = parent.getChildAt(i);
    		if (child instanceof ImageView && child.getId() == -1
    				&& child.getWidth() > photo.getWidth() 
    				&& child.getHeight() > photo.getHeight()
    				&& child.getTop() < photo.getTop()
    				&& child.getLeft() < photo.getLeft()) {
        		return (ImageView)child;
    		}
    	}
        return null;
        */
    }

    /**
     * Gets profile photo
     * 
     * @return {@code ImageView} profile photo
     */
    protected ImageView getProfilePhoto() {
        return (ImageView)Id.getViewByViewIdName(PHOTO_ID);
    }

    // VERIFICATION METHODS -------------------------------------------------
    /**
     * Verify profile photo frame is present
     * 
     * @param photoFrameRectangle
     *            expected photo frame boundaries
     */
    protected void assertPhotoFramePresent(Rect2DP photoFrameRectangle) {
        ImageView profilePhotoFrame = getProfilePhotoFrame();

        Assert.assertNotNull("Photo's frame is not present", profilePhotoFrame);
    }

    /**
     * Verify profile photo is present
     * 
     * @param photoRectangle
     *            expected photo boundaries
     */
    protected void assertPhotoPresent(Rect2DP photoRectangle) {
        ImageView profilePhoto = getProfilePhoto();
        WaitActions.waitForViewRect(profilePhoto, photoRectangle, DataProvider.WAIT_DELAY_DEFAULT,
                "Photo is not present");
    }

    /**
     * Verify 'Connections' label is present
     */
    protected void assertConnectionsLabelPresent() {
        final int minimumNumberOfMatches = 1;

        boolean isConnectionsLabelPresent = getSolo().waitForText(CONNECTIONS_LABEL,
                minimumNumberOfMatches, DataProvider.WAIT_DELAY_LONG, false, false);
        Assert.assertTrue("'" + CONNECTIONS_LABEL + "' label is not present",
                isConnectionsLabelPresent);
    }

    /**
     * Verify 'Recent Activity' label is present
     */
    protected void assertRecentActivityLabelPresent() {
        ViewAssertUtils.assertLabel(RECENT_ACTIVITY_LABEL);
    }

    /**
     * Verify 'Experience' label is present
     */
    protected void assertExperienceLabelPresent() {
        ViewAssertUtils.assertLabel(EXPERIENCE_LABEL);
    }

    /**
     * Verify 'In Common' label is present
     */
    protected void assertInCommonLabelPresent() {
        ViewAssertUtils.assertLabel(IN_COMMON_LABEL);
    }

}
