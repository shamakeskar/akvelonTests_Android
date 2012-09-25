package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Groups You Might Like screen.
 * 
 * @author nikita.chehomov
 * @created Aug 31, 2012 2:05:62 PM
 */
// Group join request sent.
public class ScreenGroupsYouMightLike extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupsYouMightLikeListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupsYouMightLikeListActivity";

    static int INDEX_OF_LAYOUT_CHILD = 3;
    static int INDEX_OF_LOCK = 1;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenGroupsYouMightLike() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue(
                "'You Might Like' label is not present",
                getSolo().waitForText("You Might Like", 1, DataProvider.WAIT_DELAY_LONG, false,
                        false));

        verifyINButton();
        HardwareActions.takeCurrentActivityScreenshot("Groups You Might Like screen");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'OK' button on popup.
     */
    public void tapOnOkOnPopup() {
        Button okButton = getSolo().getButton("Ok");
        ViewUtils.tapOnView(okButton, "'Ok' button");
    }

    /**
     * Taps on 'Cancel' button on popup.
     */
    public void tapOnCancelOnPopup() {
        Button cancelButton = getSolo().getButton("Cancel");
        ViewUtils.tapOnView(cancelButton, "'Cancel' button");
    }

    /**
     * Taps on first visible 'Join' button.
     */
    public void tapOnFirstVisibleJoinButton() {
        Button joinButton = getSolo().getButton("Join");
        ViewUtils.tapOnView(joinButton, "'Join' button");
    }

    /**
     * Taps on first visible closed group.
     */
    public void tapOnVisibleClosedGroup() {

        RelativeLayout layout = null, layoutChild = null;
        ImageView lockImage = null;
        for (Button button : getSolo().getCurrentButtons()) {
            if (button.getParent() instanceof RelativeLayout) {
                layout = (RelativeLayout) button.getParent();
                if (layout.getChildAt(INDEX_OF_LAYOUT_CHILD) instanceof RelativeLayout) {
                    layoutChild = (RelativeLayout) layout.getChildAt(INDEX_OF_LAYOUT_CHILD);
                    if (layoutChild.getChildAt(INDEX_OF_LOCK).isShown())
                        lockImage = (ImageView) layoutChild.getChildAt(INDEX_OF_LOCK);
                }
            }
        }
        ViewUtils.tapOnView(lockImage, "'Lock' image (show)");
    }

    /**
     * Taps on first visible not closed group.
     */
    public void tapOnVisibleNotClosedGroup() {

        RelativeLayout layout = null, layoutChild = null;
        ImageView lockImage = null;
        for (Button button : getSolo().getCurrentButtons()) {
            if (button.getParent() instanceof RelativeLayout) {
                layout = (RelativeLayout) button.getParent();
                if (layout.getChildAt(INDEX_OF_LAYOUT_CHILD) instanceof RelativeLayout) {
                    layoutChild = (RelativeLayout) layout.getChildAt(INDEX_OF_LAYOUT_CHILD);
                    if (!layoutChild.getChildAt(INDEX_OF_LOCK).isShown())
                        lockImage = (ImageView) layoutChild.getChildAt(INDEX_OF_LOCK);
                }
            }
        }
        ViewUtils.tapOnView(lockImage, "'Lock' image (not show)");
    }
}
