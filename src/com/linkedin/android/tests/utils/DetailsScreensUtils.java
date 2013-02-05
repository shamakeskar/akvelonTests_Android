package com.linkedin.android.tests.utils;

import java.util.concurrent.Callable;

import junit.framework.Assert;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.base.BaseScreenSharedNewsDetails;
import com.linkedin.android.screens.more.ScreenCompanyDetails;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.StringUtils;
import com.linkedin.android.utils.WaitActions;

public final class DetailsScreensUtils {
    // CONSTANTS ------------------------------------------------------------
    private static final ViewIdName COMMENTS_LAYOUT = new ViewIdName("comments_layout");
    private static final ViewIdName LIKES_LAYOUT = new ViewIdName("likes_layout");
    private static final ViewIdName UP_LAYOUT = new ViewIdName("nav_inbox_previous");
    private static final ViewIdName DOWN_LAYOUT = new ViewIdName("nav_inbox_next");

    private static final String VIEW_ALL_TEXT = "View all";
    private static final String LIKE_THIS_TEXT = " like ";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Returns {@code TextView} object with text 'n people liked this'.
     * 
     * @return {@code TextView} object with text 'n people liked this'.
     */
    public static TextView getPeopleLikedLabel() {
        ViewGroup likesCommentsLayouts = (ViewGroup) Id.getViewByViewIdName(LIKES_LAYOUT);
        for (int i = 0; i < likesCommentsLayouts.getChildCount(); i++) {
            try {
                TextView commentField = (TextView) likesCommentsLayouts.getChildAt(i);
                if (commentField.getText().toString().indexOf(LIKE_THIS_TEXT) != -1) {
                    return commentField;
                }
            } catch (ClassCastException e) {
            }
        }

        return null;
    }

    /**
     * Returns count of likes.
     * 
     * @return count of likes.
     */
    public static int getLikesCount() {
        TextView likesView = getPeopleLikedLabel();
        if (likesView == null)
            return 0;
        else {
            Integer number = StringUtils.getNumberFromString(likesView.getText().toString());
            if (number == null)
                return 0;
            else
                return number.intValue();
        }
    }

    /**
     * Check that count of likes changed.
     * 
     * @param countOfLikes
     *            count of likes that must be changed.
     */
    public static void verifyCountOfLikesChanged(final int countOfLikes) {
        WaitActions.waitForTrueInFunction("Count of likes (" + countOfLikes + ") not changed",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return countOfLikes != getLikesCount();
                    }
                });
    }

    /**
     * Gets {@code View} with "View all .. comments" text.
     * 
     * @return {@code View} with "View all .. comments" text, or <b>null</b> if
     *         it absent.
     */
    public static View getViewAllCommentsButton() {
        ViewGroup likesCommentsLayouts = (ViewGroup) Id.getViewByViewIdName(COMMENTS_LAYOUT);
        for (int i = 0; i < likesCommentsLayouts.getChildCount(); i++) {
            try {
                TextView commentField = (TextView) likesCommentsLayouts.getChildAt(i);
                if (commentField.getText().toString().indexOf(VIEW_ALL_TEXT) != -1) {
                    return commentField;
                }
            } catch (ClassCastException e) {
            }
        }

        return null;
    }

    /**
     * Opens Profile whos create dicussion. It might be User Profile or Company
     */
    public static BaseINScreen openProfileAuthor() {
        BaseScreenSharedNewsDetails.tapOnConnectionProfile();

        WaitActions.waitMultiplyActivities(new String[] { ScreenProfile.ACTIVITY_SHORT_CLASSNAME,
                ScreenCompanyDetails.ACTIVITY_SHORT_CLASSNAME });

        if (DataProvider.getInstance().getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ScreenProfile.ACTIVITY_SHORT_CLASSNAME))
            return new ScreenProfile();
        else
            return new ScreenCompanyDetails();
    }
    
    /**
     * Tapping on Up button to load previous screen
     */
    public static void tapUpButton() {
        ImageView upButton = (ImageView) Id.getViewByViewIdName(UP_LAYOUT);
        Assert.assertTrue("'Up' button is not enabled", upButton.isEnabled());
        Logger.i("Tapping on 'Up' button");
        DataProvider.getInstance().getSolo().clickOnView(upButton);
    }
    
    /**
     * Tapping on Down button to load next screen
     */
    public static void tapDownButton() {
        ImageView downButton = (ImageView) Id.getViewByViewIdName(DOWN_LAYOUT);
        Assert.assertTrue("'Down' button is not enabled", downButton.isEnabled());
        Logger.i("Tapping on 'Down' button");
        DataProvider.getInstance().getSolo().clickOnView(downButton);
    }
}
