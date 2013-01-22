package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.ScreenResolution;
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
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.search.SearchActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "SearchActivity";

    static final String SEARCH_BAR_FIELD_LABEL = "Search bar";
    static final String SEARCH_ICON_LABEL = "Search button";
    static final String CONNECTIONS_LABEL = "Connections";

    static final float SCREEN_WIDTH = ScreenResolution.getScreenWidth()
            / ScreenResolution.getScreenDensity();
    static final Rect2DP SEARCH_BAR_LAYOUT = new Rect2DP(0, 0, SCREEN_WIDTH - 54.0f, 82.0f);
    static final Rect2DP SEARCH_ICON_RECT = new Rect2DP(262f, 29f, 55f, 49f);
    static final Rect2DP SEARCH_BAR_RECT = new Rect2DP(10f, 33f, 252f, 48f);
    static final int WAIT_FOR_SCREEN_LOAD_COMPLETELY = 10;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenSearch() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        // Delay for wait then 'Search bar' and 'Search button' loaded.
        WaitActions.delay(WAIT_FOR_SCREEN_LOAD_COMPLETELY);

        // Verify presence of search button.
        Assert.assertNotNull("Search button is not present", getSearchIcon());

        // Check that list views is not empty.
        Assert.assertTrue("List views is not present", ListViewUtils.isCurrentListViewsNotEmpty());

        // Check that list views has same width as screen
        ListView firstListView = ListViewUtils.getFirstListView();
        Assert.assertTrue("Width of list views is not equal width of device screen.",
                ListViewUtils.isListViewWidthEqualToScreenWidth(firstListView));
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

        return LayoutUtils.getEditTextByItsLayoutAndSize(SEARCH_BAR_LAYOUT, SEARCH_BAR_RECT.width,
                SEARCH_BAR_RECT.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
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
     * Search for contact with specified {@code contactName}
     * 
     * @param contactName
     *            name of searched contact
     * @return {@code TextView} first found contact name or <b>null</b> if there
     *         is no such contact
     */
    public TextView searchForContact(String contactName) {
        final int progressBarAppearanceWaitTimeSec = 3;

        typeTextIntoSearchBar(contactName);

        // wait while progress bar appears
        WaitActions.delay(progressBarAppearanceWaitTimeSec);
        // wait while search results appear
        WaitActions.waitForProgressBarDisappear(0, DataProvider.WAIT_DELAY_DEFAULT);
        HardwareActions.takeCurrentActivityScreenshot("Search contact '" + contactName + "'");

        View firstVisibleConnectionFromList = getFirstVisibleConnectionFromList();
        if (!(firstVisibleConnectionFromList instanceof LinearLayout)) {
            return null;
        }
        LinearLayout firstVisibleConnectionLayout = (LinearLayout) firstVisibleConnectionFromList;
        TextView foundContactNameTextView = TextViewUtils.searchTextViewInLayout(contactName,
                firstVisibleConnectionLayout, true);
        return foundContactNameTextView;
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
        return LayoutUtils.getImageViewByItsLayoutAndSize(LayoutUtils.UPPER_RIGHT_BUTTON_LAYOUT,
                SEARCH_ICON_RECT.width, SEARCH_ICON_RECT.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
    }

    public static void go_to_search() {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart();
        screenUpdates.openSearchScreen();
        TestUtils.delayAndCaptureScreenshot("go_to_search");
    }

    public static void search_tap_cancel() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenUpdates();
        TestUtils.delayAndCaptureScreenshot("search_tap_cancel");
    }

    public static void search_tap_profile() {
        new ScreenSearch().tapOnFirstVisibleConnectionProfileScreen();
        new ScreenProfile();
        TestUtils.delayAndCaptureScreenshot("search_tap_profile");        
    }

    public static void search_tap_profile_reset() {
        HardwareActions.goBackOnPreviousActivity();
        new ScreenSearch();
        TestUtils.delayAndCaptureScreenshot("search_tap_profile_reset");
    }

}
