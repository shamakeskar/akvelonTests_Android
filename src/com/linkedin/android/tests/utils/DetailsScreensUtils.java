package com.linkedin.android.tests.utils;

import java.util.concurrent.Callable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.StringUtils;
import com.linkedin.android.utils.WaitActions;

public final class DetailsScreensUtils {
    // CONSTANTS ------------------------------------------------------------
    private static final ViewIdName COMMENTS_LAYOUT = new ViewIdName("comments_layout");
    private static final ViewIdName LIKES_LAYOUT = new ViewIdName("likes_layout");

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
}
