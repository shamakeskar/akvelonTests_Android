package com.linkedin.android.screens.settings;

import junit.framework.Assert;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.popups.Popup;
import com.linkedin.android.popups.PopupSyncContacts;
import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.common.ScreenBrowser;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenLogin;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Settings screen.
 * 
 * @author alexander.makarov
 * @created Aug 10, 2012 12:05:37 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenSettings extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.settings.SettingsActivity2";
    public static final String ACTIVITY_SHORT_CLASSNAME = "SettingsActivity2";
    public static final ViewIdName ID_NAME_OF_ADD_CONNECTIONS = new ViewIdName("add_connections_text");

    static final String PRIVATE_POLICY_TEXT = "Privacy Policy";
    static final String GEGERALl_SETTINGS_TEXT = "General Settings";
    static final String DEVICE_SPECIFIC_EULA_TEXT = "Device-specific EULA";
    static final String USER_AGREEMENT_TEXT = "User Agreement";
    static final String LANGUAGE_TEXT = "Language";
    static final String ADD_YOUR_CALENDAR_TEXT = "Add Your Calendar";
    static final String SYNC_All_TEXT = "Sync all";
    static final String SYNC_LINKEDIN_CONTACTS_TEXT = "Sync with existing contacts";
    static final String DO_NOT_SYNC_LINKEDIN_CONTACTS_TEXT = "Do not sync LinkedIn contacts";
    static final String REPORT_PROBLEM_TEXT = "Report problem";
    static final String CONFIGURE_SERVER_URL_TEXT = "Configure server URL";
    static final String CONFIGURE_RICH_STREAM_TEXT = "Configure Rich Stream";
    static final String TURN_ON_OFF_FEATURES_TEXT = "Turn on/off features";
    static final String SET_SERVER_ADDRESS_TEXT = "Set Server Address";
    static final String ADD_YOUR_CONNECTIONS = "Add Connections";
    static final String TURN_OF_NOTIFICATION = "Turn on notifications";
    static final String CONTACTS_SYNC = "Contacts Sync";

    @TestAction(value = "go_to_settings")
    public static void go_to_settings(String email, String password) {
        ScreenUpdates screenUpdates = LoginActions.openUpdatesScreenOnStart(email, password);
        ScreenExpose screenExpose = screenUpdates.openExposeScreen();
        screenExpose.openSettingsScreen();
        TestUtils.delayAndCaptureScreenshot("go_to_settings");
    }

    // EditText: new server URL text input
    private static final ViewIdName NEW_SERVER = new ViewIdName("debug_new_server_url");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenSettings() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);
    }

    public void verifyScreenContent() {
        Logger.i("Start verify.");

        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);

        // Check that 'General Settings' section is shown.
        AssertItem(GEGERALl_SETTINGS_TEXT);
        AssertItem("Version");
        AssertItem(PRIVATE_POLICY_TEXT);
        AssertItem(DEVICE_SPECIFIC_EULA_TEXT);
        AssertItem(USER_AGREEMENT_TEXT);
        AssertItem(LANGUAGE_TEXT);

        // Check that 'Notifications' section is shown.
        AssertItem("Notifications");
        AssertItem("Enable system notifications");

        // Check that 'Add Calendar' section is shown.
        AssertItem("Add Calendar");
        AssertItem(ADD_YOUR_CALENDAR_TEXT);

        // Scroll down.
        Logger.i("Scroll screen down");
        Assert.assertTrue("Scroll down not performed", getSolo().scrollDown());

        // Check that 'Contacts Sync' section is shown.
        AssertItem("Contacts Sync");

        // Check that 'Debug Options' section is shown.
        AssertItem("Debug Options");
        AssertItem(REPORT_PROBLEM_TEXT);
        AssertItem("Enable debug logs");
        AssertItem(TURN_ON_OFF_FEATURES_TEXT);
        AssertItem(CONFIGURE_SERVER_URL_TEXT);
        AssertItem(CONFIGURE_RICH_STREAM_TEXT);

        AssertButton("Sign Out");

        HardwareActions.takeCurrentActivityScreenshot("Settings screen");
    }

    /**
     * Taps on 'Turn on/off features'
     * 
     */
    public ScreenSettingsOnOfFeatures tapOnScreenSettingsOnOfFeatures() {
        // Get TextView by text
        TextView item = getSolo().getText(TURN_ON_OFF_FEATURES_TEXT);

        // Tap on TextVview
        ViewUtils.tapOnView(item, TURN_ON_OFF_FEATURES_TEXT);

        ScreenSettingsOnOfFeatures screenSettingsOnOfFeatures = new ScreenSettingsOnOfFeatures();

        // Assert ScreenSettingsSendReport
        Assert.assertNotNull("'" + TURN_ON_OFF_FEATURES_TEXT
                + "' not are ScreenSettingsRichStreamSettings screen.", screenSettingsOnOfFeatures);
        return screenSettingsOnOfFeatures;
    }

    /**
     * Taps on 'Configure Rich Stream'
     * 
     */
    public ScreenSettingsRichStreamSettings tapOnConfigureRichStream() {
        // Get TextView by text
        TextView item = getSolo().getText(CONFIGURE_RICH_STREAM_TEXT);

        // Tap on TextVview
        ViewUtils.tapOnView(item, CONFIGURE_RICH_STREAM_TEXT);

        ScreenSettingsRichStreamSettings screenSettingsRichStreamSettings = new ScreenSettingsRichStreamSettings();

        // Assert ScreenSettingsSendReport
        Assert.assertNotNull("'" + CONFIGURE_RICH_STREAM_TEXT
                + "' not are ScreenSettingsRichStreamSettings screen.",
                screenSettingsRichStreamSettings);
        return screenSettingsRichStreamSettings;
    }

    /**
     * Open new Popup Configure Server URL settings.s
     */
    public void tapOnConfigureServerURLPopup() {
        HardwareActions.scrollDown();
        // Get TextView by text
        TextView item = getSolo().getText(CONFIGURE_SERVER_URL_TEXT);

        // Tap on TextVview
        ViewUtils.tapOnView(item, CONFIGURE_SERVER_URL_TEXT);

        Popup popup = new Popup("LinkedIn", "PRODUCTION");
        Assert.assertNotNull("'" + CONFIGURE_SERVER_URL_TEXT + "' is not are Popup.", popup);
    }

    /**
     * Set new server URL.
     * 
     * @param serverURL
     *            is String like "10.0.2.2/data"
     */
    public void setServerURL(String serverURL) {
        tapOnConfigureServerURLPopup();
        EditText urlText = (EditText) Id.getViewByViewIdName(NEW_SERVER);
        Logger.i("Entering server URL: " + serverURL);
        getSolo().enterText(urlText, serverURL);
        Logger.i("Clicking on '" + SET_SERVER_ADDRESS_TEXT + "' button");
        getSolo().clickOnButton(SET_SERVER_ADDRESS_TEXT);
    }

    /**
     * Taps on 'Report problem'
     * 
     */
    public ScreenSettingsSendReport tapOnSendReport() {
        // Get TextView by text
        TextView item = getSolo().getText(REPORT_PROBLEM_TEXT);

        // Tap on TextVview
        ViewUtils.tapOnView(item, REPORT_PROBLEM_TEXT);

        ScreenSettingsSendReport screenSettingsSendReport = new ScreenSettingsSendReport();

        // Assert ScreenSettingsSendReport
        Assert.assertNotNull("'" + REPORT_PROBLEM_TEXT
                + "' not are ScreenSettingsSendReport screen.", screenSettingsSendReport);
        return screenSettingsSendReport;
    }

    /**
     * Taps on 'Do not sync LinkedIn contacts'
     * 
     */
    public void tapOnDoNotSync() {
        // Get TextView by text
        TextView item = getSolo().getText(DO_NOT_SYNC_LINKEDIN_CONTACTS_TEXT);

        // Tap on TextVview
        ViewUtils.tapOnView(item, DO_NOT_SYNC_LINKEDIN_CONTACTS_TEXT);

        Popup popup = new Popup("Sync LinkedIn Contacts", "Sync all");
        Assert.assertNotNull("'" + DO_NOT_SYNC_LINKEDIN_CONTACTS_TEXT + "' is not are Popup.",
                popup);
        AssertItem(SYNC_All_TEXT, SYNC_LINKEDIN_CONTACTS_TEXT, DO_NOT_SYNC_LINKEDIN_CONTACTS_TEXT);
    }

    /**
     * Taps on 'Add your Calendar'
     * 
     */
    public ScreenSettingsCalendar tapOnAddYourCalendar() {
        // Get TextView by text
        TextView item;
        int maxCountScroll = 5;
        do {
            item = TextViewUtils.getTextViewByText(ADD_YOUR_CALENDAR_TEXT);
            if (item != null)
                break;
            getSolo().scrollDown();
            maxCountScroll--;
        } while (maxCountScroll > 0);
        Assert.assertNotNull("'Add Your Calendar' is not present", item);

        // Tap on TextVview
        ViewUtils.tapOnView(item, ADD_YOUR_CALENDAR_TEXT);

        // Extract new ScreenCalendar
        ScreenSettingsCalendar screenCalendar = new ScreenSettingsCalendar();

        // Assert ScreenCalendar
        Assert.assertNotNull("'" + ADD_YOUR_CALENDAR_TEXT
                + "' not are ScreenSettingsCalendar screen.", screenCalendar);
        return screenCalendar;
    }

    /**
     * Open new Popup Language settings.
     * 
     */
    public void tapOnLanguagePopup() {
        // Get TextView by text
        TextView item = getSolo().getText(LANGUAGE_TEXT);

        // Tap on TextVview
        ViewUtils.tapOnView(item, LANGUAGE_TEXT);

        Popup popup = new Popup(LANGUAGE_TEXT, "English");
        Assert.assertNotNull("'" + LANGUAGE_TEXT + "' is not are Popup.", popup);
        popup.tapOnButton("Cancel");
    }

    /**
     * Open new ScreenBrowser by TextView text
     * 
     */
    public ScreenBrowser openBrowserByText(String lableToTapName) {
        // Get TextView by text
        TextView item = getSolo().getText(lableToTapName);

        // Tap on TextVview
        ViewUtils.tapOnView(item, lableToTapName);

        // Extract new ScreenBrowser
        ScreenBrowser screenBrowser = new ScreenBrowser();

        // Assert screenBrowser
        Assert.assertNotNull("'" + lableToTapName + "' not are ScreenBrowser screen.",
                screenBrowser);

        return screenBrowser;
    }

    /**
     * Opens 'You' screen ({@code ScreenYou})
     * 
     * @return 'You' screen ({@code ScreenYou})
     */
    public ScreenYou openYouScreen() {
        HardwareActions.pressBack();
        ScreenYou screenYou = new ScreenYou();
        return screenYou;
    }

    /**
     * Assert Item on the Settings item.
     * 
     * @param name
     *            is name of item
     */
    public void AssertItem(String... names) {
        for (String name : names) {
            Assert.assertNotNull("'" + name + "' item is not present on Settings screen.",
                    getSolo().getText(name));
        }
    }

    /**
     * Assert button on the Settings.
     * 
     * @param name
     *            is name of button
     */
    public void AssertButton(String name) {
        Assert.assertNotNull("'" + name + "' button is not present. on Settings screen.", getSolo()
                .getButton(name));
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Settings");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Sign Out' button.
     */
    public void tapOnSignOutButton() {
        while (getSolo().scrollDown())
            ;
        Assert.assertNotNull("'Sign Out' button is not present.", getSolo().getButton("Sign Out"));

        Logger.i("Tapping on 'Sign Out' button");
        getSolo().clickOnButton("Sign Out");
    }

    /**
     * Taps on the Settings item.
     * 
     * @param name
     *            is name of item on which should tap
     */
    public void tapOnItem(String name) {
        View item = getSolo().getText(name);
        Assert.assertNotNull("'" + name + "' item is not present on Settings screen.", item);

        Logger.i("Tapping on '" + name + "'" + " item");
        getSolo().clickOnView(item);
    }

    /**
     * Enables calendar feature.
     */
    public void enableCalendar() {
        tapOnItem("Add Your Calendar");

        if (!getSolo().isCheckBoxChecked(0)) {
            getSolo().clickOnCheckBox(0);
        }
    }

    /**
     * Disables calendar feature.
     */
    public void disableCalendar() {
        tapOnItem("Add Your Calendar");

        if (getSolo().isCheckBoxChecked(0)) {
            getSolo().clickOnCheckBox(0);
        }
    }
    
    @TestAction(value = "settings")
    public static void settings() {
        new ScreenExpose(null).openSettingsScreen();
        TestUtils.delayAndCaptureScreenshot("settings");
    }
    
    @TestAction(value = "settings_tap_sync_calendar")
    public static void settings_tap_sync_calendar() {
        new ScreenSettings().tapOnAddYourCalendar();
        TestUtils.delayAndCaptureScreenshot("settings_tap_sync_calendar");
    }
    
    @TestAction(value = "settings_tap_add_con")
    public static void settings_tap_add_con() {
        TextView addCon = (TextView) Id.getViewByViewIdName(ID_NAME_OF_ADD_CONNECTIONS);
        ViewUtils.tapOnView(addCon, "Add Connections", true);
        new ScreenSettingsAddConnections();
        TestUtils.delayAndCaptureScreenshot("settings_tap_add_con");
    }

    @TestAction(value = "settings_toggle_push_notifications")
    public static void settings_toggle_push_notifications() {
        new ScreenSettings().tickInPushNotifications();
        TestUtils.delayAndCaptureScreenshot("settings_toggle_push_notifications");
    }
    
    @TestAction(value = "settings_toggle_push_notifications_reset")
    public static void settings_toggle_push_notifications_reset() {
        new ScreenSettings().tickInPushNotifications();
        TestUtils.delayAndCaptureScreenshot("settings_toggle_push_notifications_reset");
    }

    /**
     * Put a tick in paragraph notification.
     */
    public void tickInPushNotifications() {
        TextView notifications;
        int maxCountScroll = 5;
        do {
            notifications = TextViewUtils.getTextViewByText(TURN_OF_NOTIFICATION);
            if (notifications != null)
                break;
            getSolo().scrollUp();
            maxCountScroll--;
        } while (maxCountScroll > 0);
        Assert.assertNotNull("'" + TURN_OF_NOTIFICATION
                + "' item is not present on Settings screen.", notifications);
        RelativeLayout parent = (RelativeLayout) notifications.getParent();
        CheckBox checkBox = (CheckBox) parent.getChildAt(1);
        Assert.assertNotNull("' Check box " + TURN_OF_NOTIFICATION
                + "' is not present on Settings screen.", checkBox);
        boolean isChecked = checkBox.isChecked();
        Logger.i("Tapping in Check box '" + TURN_OF_NOTIFICATION + "'");
        getSolo().clickOnView(checkBox);
        // Wait for check mark;
        WaitActions.waitForScreenUpdate();
        if (checkBox.isChecked() != isChecked)
            return;
        Assert.fail("Checkmark is not present");
    }
    
    @TestAction(value = "settings_tap_signout")
    public static void settings_tap_signout() {
        new ScreenSettings().tapOnSignOutButton();
        new ScreenLogin();
    }
    
    @TestAction(value = "settings_tap_sync_calendar_reset")
    public static void settings_tap_sync_calendar_reset() {
        ScreenSettings.backInScreenSetting("settings_tap_sync_calendar_reset");
    }

    @TestAction(value = "settings_tap_add_con_reset")
    public static void settings_tap_add_con_reset() {
        ScreenSettings.backInScreenSetting("settings_tap_add_con_reset");
    }

    /**
     * Tap back button and verify 'ScreenSetting'.
     * 
     * @param screenName
     *            - name for screen.
     */
    public static void backInScreenSetting(String screenName) {
        HardwareActions.pressBack();
        new ScreenSettings();
        TestUtils.delayAndCaptureScreenshot(screenName);
    }
    
    @TestAction(value = "settings_dialog_tap_sync_all_contacts")
    public static void settings_dialog_tap_sync_all_contacts() {
        PopupSyncContacts popup = new ScreenSettings().popupSyncContacts();
        popup.tapOnSyncAll();
        TestUtils.delayAndCaptureScreenshot("settings_dialog_tap_sync_all_contacts");
    }

    /**
     * Tap in 'Sync LinkedIn Contacts'.
     * 
     * @return popup 'Sync LinkedIn Contacts'.
     */
    public PopupSyncContacts popupSyncContacts() {
        // Get TextView by text
        int maxCountScroll = 5;
        TextView sync;
        do {
            sync = TextViewUtils.getTextViewByText("Debug Options");
            if (sync != null)
                break;
            getSolo().scrollDown();
            maxCountScroll--;
        } while (maxCountScroll > 0);
        Assert.assertNotNull(CONTACTS_SYNC + " not present", getSolo().getText(CONTACTS_SYNC));
        Logger.i("Tapping on Sync LinkedIn Contacts");
        getSolo().clickOnText(SYNC_All_TEXT.substring(1, 4), 2);
        return new PopupSyncContacts();
    }
    
    @TestAction(value = "settings_dialog_tap_sync_existing_contacts")
    public static void settings_dialog_tap_sync_existing_contacts() {
        PopupSyncContacts popup = new ScreenSettings().popupSyncContacts();
        popup.tapOnDoNotSync();
        TestUtils.delayAndCaptureScreenshot("settings_dialog_tap_sync_existing_contacts");
    }
    
    @TestAction(value = "settings_dialog_tap_sync_cancel")
    public static void settings_dialog_tap_sync_cancel() {
        new ScreenSettings().popupSyncContacts();
        backInScreenSetting("settings_dialog_tap_sync_cancel");
    }

    @TestAction(value = "settings_precondition")
    public static void settings_precondition() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("settings_precondition");
    }
}
