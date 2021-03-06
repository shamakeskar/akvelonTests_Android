package com.linkedin.android.screens.more;

import junit.framework.Assert;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenGroups;
import com.linkedin.android.screens.common.ScreenPYMK;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.updates.ScreenShareUpdate;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Groups&More screen.
 * 
 * @author Irina Gracheva
 * @created Aug 15, 2012
 */
public class ScreenGroupsAndMore extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.networkpage.NetworkPageActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NetworkPageActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenGroupsAndMore() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        Assert.assertTrue("'PEOPLE YOU MAY KNOW' label is not present on Groups&More screen",
                getSolo().searchText("PEOPLE YOU MAY KNOW", 1, false));

        Assert.assertTrue("'Groups' label is not present on Groups&More screen", getSolo()
                .searchText("Groups", 1, false));

        HardwareActions.takeCurrentActivityScreenshot("Groups&More screen");
        // TODO complete verification.
    }

    @Override
    public void waitForMe() {
        Assert.assertTrue("Cannot wait to launch activity '" + ACTIVITY_SHORT_CLASSNAME + "'",
                getSolo()
                        .waitForActivity(ACTIVITY_SHORT_CLASSNAME, DataProvider.WAIT_DELAY_DEFAULT));
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Opens 'People You May Know' screen.
     * 
     * @return {@code ScreenPYMK}.
     */
    public ScreenPYMK openPYMKScreen() {
        TextView text = getSolo().getText("PEOPLE YOU MAY KNOW");
        Assert.assertNotNull("'PEOPLE YOU MAY KNOW' label not present", text);
        Assert.assertTrue("'PEOPLE YOU MAY KNOW' label not shown", text.isShown());

        Logger.i("Tapping on 'PEOPLE YOU MAY KNOW' label");
        getSolo().clickOnText("PEOPLE YOU MAY KNOW");
        return new ScreenPYMK();
    }

    /**
     * Opens 'Groups' screen.
     * 
     * @return ScreenGroups
     */
    public ScreenGroups openGroupsScreen() {
        tapOnGroups();
        return new ScreenGroups();
    }

    /**
     * Taps on 'Groups' label.
     */
    public void tapOnGroups() {
        TextView groupsText = getSolo().getText("GROUPS");
        ViewUtils.tapOnView(groupsText, "'GROUPS' label");
    }

    /**
     * Taps on 'Jobs' label.
     */
    public void tapOnJobs() {
        TextView jobsText = getSolo().getText("Jobs");
        ViewUtils.tapOnView(jobsText, "'Jobs' label");

    }

    /**
     * Taps on 'Share' button.
     */
    public void tapOnShareButton() {
        View button = getRightButtonInNavigationBar();
        ViewUtils.tapOnView(button, "'Share' button");
    }

    /**
     * Opens 'Share Update' screen.
     * 
     * @return ScreenShareUpdate.
     */
    public ScreenShareUpdate openShareUpdateScreen() {
        tapOnShareButton();
        return new ScreenShareUpdate();
    }
    
    /**
     * Opens SEARCH screen.
     * 
     * @return {@code SearchScreen} with just opened 'SEARCH' screen.
     */
    public ScreenSearch openSearchScreen() {
        EditText searchBar = getSearchBar();
        getSolo().clickOnView(searchBar);
        Logger.i("Tapping on 'Search bar'");
        ScreenSearch searchScreen = new ScreenSearch();
        return searchScreen;
    }

}
