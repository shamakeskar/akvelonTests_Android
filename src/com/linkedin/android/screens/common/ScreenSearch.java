package com.linkedin.android.screens.common;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.StringUtils;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.viewUtils.ImageViewUtils;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for test Search screen.
 * 
 * @author vasily.gancharov
 * @created Aug 21, 2012 12:17:32 PM
 */
// NOTE: this screen is not inherited from BaseINScreen
// because there is no IN button here.
// So here are methods to work with search bar.
@SuppressWarnings("rawtypes")
public class ScreenSearch extends BaseScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.search.SearchActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "SearchActivity";

    static final String SEARCH_BAR_FIELD_LABEL = "Search bar";
    static final String SEARCH_ICON_LABEL = "Search button";
    static final String CONNECTIONS_LABEL = "Connections";

    static final float SCREEN_WIDTH = ScreenResolution.getScreenWidth()
            / ScreenResolution.getScreenDensity();
    static final Rect2DP SEARCH_BAR_LAYOUT = new Rect2DP(0, 0, SCREEN_WIDTH - 54.0f, 82.0f);
    static final Rect2DP SEARCH_ICON_RECT = new Rect2DP(262f, 29f, 55f, 49f);
    static final Rect2DP SEARCH_BAR_RECT = new Rect2DP(10f, 33f, 252f, 48f);
    public static final ViewIdName SEARCH_BUTTON = new ViewIdName("searchButton");
    public static final ViewIdName SEARCH_EDIT_TEXT = new ViewIdName("searchEditText");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenSearch() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

        WaitActions.waitForTrueInFunction(
                "Search screen is not present (list with search results is not present)",
                new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return ListViewUtils.isCurrentListViewsNotEmpty();
                    }
                });
        WaitActions.waitForTrueInFunction(
                "Search screen is not present (search icon in right corneris not present)",
                new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return getSearchIcon() != null;
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Search");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Returns search bar.
     * 
     * @return if exist search bar {@code EditText}, else <b>null</b>
     */
    public EditText getSearchBar() {
        EditText searchBar = (EditText) Id.getViewByViewIdName(SEARCH_EDIT_TEXT);
        if (searchBar != null && searchBar.isShown()) {
            return searchBar;
        } else {
            return null;
        }
    }

    /**
     * Gets connections list
     * 
     * @return {@code ListView} connections list
     */
    public ListView getConnectionsList() {
        return ListViewUtils.getFirstListView();
    }

    /**
     * Gets first item from Connections list
     * 
     * @return {@code View} first item from Connections list
     */
    public View getFirstVisibleConnectionFromList() {
        final int firstVisibleConnectionFromListIndex = 1;

        ListView connectionsList = getConnectionsList();
        if (null == connectionsList
                || connectionsList.getCount() <= firstVisibleConnectionFromListIndex) {
            return null;
        }
        return connectionsList.getChildAt(firstVisibleConnectionFromListIndex);
    }

    /**
     * Taps on first item from Connection list
     */
    public void tapOnFirstVisibleConnectionProfileScreen() {
        View firstVisibleConnectionFromList = getFirstVisibleConnectionFromList();
        Assert.assertNotNull("Connection list is empty", firstVisibleConnectionFromList);
        ViewUtils.tapOnView(firstVisibleConnectionFromList, "first connection in list");
    }

    /**
     * Opens profile of connected user with name <b>connectionName</b>.
     * 
     * @param connectionName
     *            is name of user to open profile screen
     * @return
     */
    public ScreenProfileOfConnectedUser open1dConnectionProfileScreen(String connectionName) {
        TextView name = getSolo().getText(connectionName, false);
        Assert.assertNotNull("Cannot find connection with name '" + connectionName + "' in list",
                name);

        Logger.i("Tapping on connection '" + name.getText() + "' in list");
        getSolo().clickOnView(name);

        return new ScreenProfileOfConnectedUser();
    }

    /**
     * Search for contact with specified {@code contactName}.
     * 
     * @param contactName
     *            name of searched contact
     */
    public void searchForContact(String contactName) {
        if (StringUtils.isNullOrEmpty(contactName)) {
            Assert.fail("Contact name is empty");
        }
        Logger.i("Typing '" + contactName + "' in searh field");
        typeTextIntoSearchBar(contactName);
        WaitActions.waitForTrueInFunction("Search progress bar is not appear.", new Callable<Boolean>() {
            public Boolean call() throws Exception {
                ArrayList<ProgressBar> progressBars = getSolo().getCurrentProgressBars();
                return progressBars.size() > 0;
            }
        });
        WaitActions.waitForProgressBarDisappear(0, DataProvider.WAIT_DELAY_DEFAULT);
    }

    /**
     * Gets degree {@code ImageView} of contact with specified
     * {@code contactName}
     * 
     * @param contactName
     *            contact name to get degree {@code ImageView}
     * @return contact degree {@code ImageView} or <b>null</b> if contact
     *         null/has no degree
     */
    public ImageView getContactDegree(TextView contactName) {
        int indexOfContactDegreeImageView = 2;

        RelativeLayout contactNameLayout = getContactItemLayout(contactName);
        if (null == contactNameLayout) {
            return null;
        }
        ImageView contactDegreeImageView = ImageViewUtils.getImageViewFromLayoutByIndex(
                contactNameLayout, indexOfContactDegreeImageView);
        return contactDegreeImageView;
    }

    /**
     * Gets parent layout of {@code contactNameLayout}
     * 
     * @param contactName
     *            contact name
     * @return parent {@code RelativeLayout} of {@code contactNameLayout} or
     *         <b>null</b> if {@code contactNameLayout} has no parent/it's
     *         parent not {@code RelativeLayout}
     */
    private RelativeLayout getContactItemLayout(TextView contactName) {
        if (null == contactName) {
            return null;
        }

        ViewParent contactNameParentView = contactName.getParent();
        if (!(contactNameParentView instanceof RelativeLayout)) {
            return null;
        }
        RelativeLayout contactNameLayout = (RelativeLayout) contactNameParentView;
        return contactNameLayout;
    }

    /**
     * Types specified {@code text} into 'Search bar'
     * 
     * @param text
     *            text to type into 'Search bar'
     */
    private void typeTextIntoSearchBar(String text) {
        EditText searchBar = getSearchBar();
        if (null == searchBar) {
            return;
        }
        getSolo().enterText(searchBar, text);
        Logger.i("Enter '" + text + "' in '" + SEARCH_BAR_FIELD_LABEL + "' field");
    }

    /**
     * Returns icon witch placed in right from search bar in navigation bar.
     * 
     * @return if exist right button {@code ImageView}, else <b>null</b>
     */
    private ImageView getSearchIcon() {
        ImageView searchButton = (ImageView) Id.getViewByViewIdName(SEARCH_BUTTON);
        if (searchButton != null && searchButton.isShown()) {
            return searchButton;
        } else {
            return null;
        }
    }

    // ACTIONS --------------------------------------------------------------
    @TestAction(value = "go_to_search")
    public static void go_to_search(String email, String password) {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart(email, password);
        screenUpdates.openSearchScreen();
        TestUtils.delayAndCaptureScreenshot("go_to_search");
    }

    @TestAction(value = "search_search")
    public static void search_search(String query) {
        new ScreenSearch().searchForContact(query);
        TestUtils.delayAndCaptureScreenshot("search_search");
    }

    @TestAction(value = "search_tap_cancel")
    public static void search_tap_cancel() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdates();
        TestUtils.delayAndCaptureScreenshot("search_tap_cancel");
    }

    @TestAction(value = "search_tap_profile")
    public static void search_tap_profile(String profileName) {
        ListView connectionsList = ListViewUtils.getFirstListView();
        if (null == connectionsList || connectionsList.getCount() < 1) {
            Assert.fail("There is not search results.");
        }
        ArrayList<TextView> views = TextViewUtils.getTextViewsInLayout(profileName,
                connectionsList, false);
        Assert.assertTrue("Cannot find '" + profileName + "' in search results.", views.size() > 0);
        ViewUtils.tapOnView(views.get(0), profileName + " profile");
        new ScreenProfile();
        TestUtils.delayAndCaptureScreenshot("search_tap_profile");
    }

    @TestAction(value = "search_tap_profile_reset")
    public static void search_tap_profile_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenSearch();
        TestUtils.delayAndCaptureScreenshot("search_tap_profile_reset");
    }

}
