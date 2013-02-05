package com.linkedin.android.screens.more;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenGroups;
import com.linkedin.android.screens.common.ScreenPYMK;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.updates.ScreenShareUpdate;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Groups&More screen.
 * 
 * @author Irina Gracheva
 * @created Aug 15, 2012
 */
public class ScreenGroupsAndMore extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.networkpage.NetworkPageActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "NetworkPageActivity";
    private static final String LABEL_COMPANIES = "COMPANIES";
    private static final ViewIdName ITEM_LAYOUT_ID = new ViewIdName("title");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenGroupsAndMore() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_LONG,
                "'Groups and More' screen is not loaded", new Callable<Boolean>() {
                    public Boolean call() {
                        ArrayList<View> views = Id.getListOfViewByViewIdName(ITEM_LAYOUT_ID);
                        if (views.size() >= 3) {
                            for (int i = 0; i < views.size(); i++) {
                                if (!views.get(i).isShown())
                                    return false;
                            }
                            return true;
                        }
                        return false;
                    }
                });
        String[] viewTextArray = { "PEOPLE YOU_MAY_KNOW", "GROUPS", "JOBS", LABEL_COMPANIES };
        for (final String text : viewTextArray) {
            WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_SHORT, "Label '" + text
                    + "' is not present on the Groups & More screen", new Callable<Boolean>() {
                public Boolean call() {
                    TextView currentTextView = TextViewUtils.getTextViewByText(text);
                    return (currentTextView != null);
                }
            });
        }
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Groups and More");
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
        TextView text = TextViewUtils.getTextViewByText("PEOPLE YOU MAY KNOW");
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
     * Opens 'Jobs' screen.
     * 
     * @return ScreenJobs
     */
    public ScreenJobs openJobsScreen() {
        tapOnJobs();
        return new ScreenJobs();
    }

    /**
     * Opens 'Companies' screen.
     * 
     * @return ScreenCompanies
     */
    public ScreenCompanies openCompaniesScreen() {
        tapOnCompanies();
        return new ScreenCompanies();
    }

    /**
     * Taps on 'Groups' label.
     */
    public void tapOnGroups() {
        TextView groupsText = TextViewUtils.getTextViewByText("GROUPS");
        ViewUtils.tapOnView(groupsText, "'GROUPS' label");
    }

    /**
     * Taps on 'Jobs' label.
     */
    public void tapOnJobs() {
        TextView jobsText = TextViewUtils.getTextViewByText("Jobs");
        if (jobsText == null)
            jobsText = TextViewUtils.getTextViewByText("JOBS");
        ViewUtils.tapOnView(jobsText, "'Jobs' label");

    }

    /**
     * Taps on 'Companies' label.
     */
    public void tapOnCompanies() {
        getSolo().scrollToBottom();
        // Scroll and wait for loading label 'Companies'.
        WaitActions.waitForTrueInFunction("'Companies' label is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return getSolo().searchText(LABEL_COMPANIES);
                    }
                });

        ViewUtils.tapOnView(TextViewUtils.getTextViewByText(LABEL_COMPANIES), "'Companies' label");
    }

    /**
     * Taps on 'Share' button.
     */
    public void tapOnShareButton() {
        View button = getRightButtonInNavigationBar();
        Assert.assertNotNull("'Share button' is not present.", button);

        Logger.i("Tapping on 'Share' button");
        getSolo().clickOnView(button);
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
        View searchBar = getSearchBar();
        ViewUtils.tapOnView(searchBar, "Search bar");
        ScreenSearch searchScreen = new ScreenSearch();
        return searchScreen;
    }

    // ACTIONS --------------------------------------------------------------
    public static void groups_and_more(String screenshotName) {
        new ScreenExpose(null).openGroupsAndMoreScreen();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "groups_and_more")
    public static void groups_and_more() {
        groups_and_more("groups_and_more");
    }

    @TestAction(value = "go_to_groups_and_more")
    public static void go_to_groups_and_more(String email, String password) {
        ScreenExpose.go_to_expose(email, password);
        groups_and_more("go_to_groups_and_more");
    }

    @TestAction(value = "groups_and_more_tap_pymk")
    public static void groups_and_more_tap_pymk() {
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openPYMKScreen();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_pymk");
    }

    @TestAction(value = "groups_and_more_tap_pymk_reset")
    public static void groups_and_more_tap_pymk_reset() {
        HardwareActions.pressBack();
        new ScreenGroupsAndMore();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_pymk_reset");
    }

    @TestAction(value = "groups_and_more_tap_groups")
    public static void groups_and_more_tap_groups() {
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openGroupsScreen();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_groups");
    }

    @TestAction(value = "groups_and_more_tap_groups_reset")
    public static void groups_and_more_tap_groups_reset() {
        HardwareActions.pressBack();
        new ScreenGroupsAndMore();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_groups_reset");
    }

    public static void groups_and_more_tap_jobs(String screenshotName) {
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        getSolo().scrollDown();
        GroupsAndMore.openJobsScreen();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "groups_and_more_tap_jobs")
    public static void groups_and_more_tap_jobs() {
        groups_and_more_tap_jobs("groups_and_more_tap_jobs");
    }

    @TestAction(value = "groups_and_more_tap_jobs_reset")
    public static void groups_and_more_tap_jobs_reset() {
        HardwareActions.pressBack();
        new ScreenGroupsAndMore();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_jobs_reset");
    }

    @TestAction(value = "groups_and_more_tap_companies")
    public static void groups_and_more_tap_companies() {
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openCompaniesScreen();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_companies");
    }

    @TestAction(value = "groups_and_more_tap_companies_reset")
    public static void groups_and_more_tap_companies_reset() {
        HardwareActions.pressBack();
        getSolo().scrollToTop();
        new ScreenGroupsAndMore();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_companies_reset");
    }

    @TestAction(value = "groups_and_more_tap_expose")
    public static void groups_and_more_tap_expose() {
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openExposeScreen();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_expose");
    }

    @TestAction(value = "groups_and_more_tap_expose_reset")
    public static void groups_and_more_tap_expose_reset() {
        groups_and_more();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_expose_reset");
    }

    @TestAction(value = "groups_and_more_tap_search")
    public static void groups_and_more_tap_search() {
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openSearchScreen();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_search");
    }

    @TestAction(value = "groups_and_more_tap_search_reset")
    public static void groups_and_more_tap_search_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenGroupsAndMore();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_search_reset");
    }

    @TestAction(value = "groups_and_more_tap_share")
    public static void groups_and_more_tap_share() {
        ScreenGroupsAndMore GroupsAndMore = new ScreenGroupsAndMore();
        GroupsAndMore.openShareUpdateScreen();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_share");
    }

    @TestAction(value = "groups_and_more_tap_share_reset")
    public static void groups_and_more_tap_share_reset() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_and_more_tap_share_reset");
    }
}
