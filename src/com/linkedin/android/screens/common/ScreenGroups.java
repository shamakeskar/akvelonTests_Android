package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.more.ScreenGroupsAndMore;
import com.linkedin.android.screens.more.ScreenYourGroup;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Groups screen.
 * 
 * @author nikita.chehomov
 * @created Aug 27, 2012 4:28:54 PM
 */
public class ScreenGroups extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupsListActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenGroups() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Groups' label is not present",
                getSolo().waitForText("Groups", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        verifyINButton();
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Groups");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Groups You Might Like' label.
     */
    public void tapOnGroupsYouMightLike() {
        TextView GYMLView = getSolo().getText("Groups You Might Like");
        ViewUtils.tapOnView(GYMLView, "'Groups You Might Like' label");
    }

    /**
     * Opens 'Groups You Might Like' screen.
     * 
     * @return ScreenGroupsYouMightLike.
     */
    public ScreenGroupsYouMightLike openGroupsYouMightLikeScreen() {
        tapOnGroupsYouMightLike();
        return new ScreenGroupsYouMightLike();
    }

    /**
     * Opens 'First Visible Group You Consist' screen.
     * 
     * @return ScreenYourGroup.
     */
    public ScreenYourGroup openFirstVisibleGroupYouConsist() {
        tapOnFirstVisibleGroupYouConsist();
        return new ScreenYourGroup();
    }

    /**
     * Taps on first visible group you consist.
     */
    public void tapOnFirstVisibleGroupYouConsist() {
        int firstGroupIndex = 0;
        Assert.assertTrue("'Your Groups' view not presented", getSolo().searchText("Your Groups"));
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            if (view.isShown()) {
                firstGroupIndex++;
                String textOfView = view.getText().toString();
                if (textOfView.equalsIgnoreCase("Your Groups"))
                    break;
            }
        }
        TextView groupView = getSolo().getText(firstGroupIndex);
        ViewUtils.tapOnView(groupView, "'Group' view");
    }

    /**
     * Taps on first visible group you pending approval.
     */
    public void tapOnFirstVisibleGroupYouPendingApproval() {
        int firstGroupYouPendingApproval = 0;
        Assert.assertTrue("'Pending Membership' view not presented",
                getSolo().searchText("Pending Membership"));
        for (TextView view : getSolo().getCurrentTextViews(null)) {
            if (view.isShown()) {
                firstGroupYouPendingApproval++;
                String textOfView = view.getText().toString();
                if (textOfView.equalsIgnoreCase("Pending Membership"))
                    break;
            }
        }
        TextView groupPendingApprovalView = getSolo().getText(firstGroupYouPendingApproval);
        ViewUtils.tapOnView(groupPendingApprovalView, "'Group Pending Approval' view");
    }

    // ACTIONS --------------------------------------------------------------
    public static void groups(String screenshotName) {
        ScreenGroupsAndMore.groups_and_more_tap_groups();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    public static void groups() {
        groups("groups");
    }

    public static void go_to_groups() {
        ScreenGroupsAndMore.go_to_groups_and_more();
        groups("go_to_groups");
    }

    @TestAction(value = "groups_tap_back")
    public static void groups_tap_back() {
        HardwareActions.pressBack();
        new ScreenGroupsAndMore();
        TestUtils.delayAndCaptureScreenshot("groups_tap_back");
    }

    public static void groups_tap_expose() {
        new ScreenGroups().openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("groups_tap_expose");
    }

    public static void groups_tap_expose_reset() {
        tapOnINButton();
        new ScreenGroups();
        TestUtils.delayAndCaptureScreenshot("groups_tap_expose_reset");
    }

    public static void groups_tap_gyml() {
        new ScreenGroups().openGroupsYouMightLikeScreen();
        TestUtils.delayAndCaptureScreenshot("groups_tap_gyml");
    }

    public static void groups_tap_gyml_reset() {
        HardwareActions.pressBack();
        new ScreenGroups();
        TestUtils.delayAndCaptureScreenshot("groups_tap_gyml_reset");
    }

    public static void groups_tap_group() {
        new ScreenGroups().openFirstVisibleGroupYouConsist();
        TestUtils.delayAndCaptureScreenshot("groups_tap_group");
    }

    public static void groups_tap_group_reset() {
        HardwareActions.pressBack();
        new ScreenGroups();
        TestUtils.delayAndCaptureScreenshot("groups_tap_group_reset");
    }

}
