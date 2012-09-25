package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.more.ScreenYourGroup;
import com.linkedin.android.tests.data.DataProvider;
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
        HardwareActions.takeCurrentActivityScreenshot("Groups screen");
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

}
