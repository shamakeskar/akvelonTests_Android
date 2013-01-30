package com.linkedin.android.screens.updates;

import java.util.Calendar;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.linkedin.android.popups.PopupSyncContacts;
import com.linkedin.android.screens.base.BaseListScreen;
import com.linkedin.android.screens.common.ScreenCalSplash;
import com.linkedin.android.screens.common.ScreenLogin;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for CALENDAR screen.
 * 
 * @author alexey.makhalov
 * @created Aug 30, 2012 11:55 AM
 */
public class ScreenCalendar extends BaseListScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.spotlight.CalendarListActivity";

    public static final String ACTIVITY_SHORT_CLASSNAME = "CalendarListActivity";

    // ImageView: calendar image at top right corner
    private static final ViewIdName NUS_SPOTLIGHT_CALENDAR_IMAGE = new ViewIdName(
            "nus_spotlight_calendar_image");
    private static final ViewIdName EVENT_TIME = new ViewIdName("event_row_time");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenCalendar() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------

    public void verify() {
        Assert.assertTrue("'CALENDAR' screen is not present",
                getSolo().waitForText("Calendar", 1, DataProvider.WAIT_DELAY_SHORT, false, false));

        HardwareActions.takeCurrentActivityScreenshot("CALENDAR screen");
    };

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Calendar");
    }

    /**
     * Taps on Calendar.
     */
    static public ScreenCalendar tapOnCalendarBigImage() {
        View calendarBigImage = Id.getViewByViewIdName(NUS_SPOTLIGHT_CALENDAR_IMAGE);
        ViewUtils.tapOnView(calendarBigImage, "calendar big image");
        return new ScreenCalendar();
    }

    /**
     * Opens any Meeting.
     */
    public ScreenCalendarEventDetail openAnyMeeting() {
        TextView event = Id.getViewByName(EVENT_TIME, TextView.class);
        Assert.assertNotNull("No meeting is present on CALENDAR screen", event);
        Logger.i("Tapping on first Meeting");
        getSolo().clickOnView(event);
        return new ScreenCalendarEventDetail();
    }

    public void tapOnTodayButton() {
        Button todayButton = getSolo().getButton("Today");

        Assert.assertNotNull("No 'Today' button on CALENDAR screen", todayButton);

        ViewUtils.tapOnView(todayButton, "'Today' button");
    }

    public boolean isOnToday() {
        Calendar c = Calendar.getInstance();
        String dayOfMonth = DateFormat.format("dd", c).toString();
        String dayOfWeek = DateFormat.format("EEEE", c).toString();
        String monthYear = DateFormat.format("MMMM yyyy", c).toString();

        return getSolo().getText(dayOfMonth) != null && getSolo().getText(dayOfWeek) != null
                && getSolo().getText(monthYear) != null;
    }

    @TestAction(value = "go_to_cal")
    public static void go_to_cal(String email, String password) {
        LoginActions.logout();
        ScreenLogin loginScreen = new ScreenLogin();

        // Type credentials and tap 'Sign In'.
        loginScreen.typeEmail(email);
        loginScreen.typePassword(password);
        loginScreen.tapOnSignInButton();

        // Tap 'Do not sync'.
        loginScreen.handleSyncContactsDialog(PopupSyncContacts.DO_NOT_SYNC_TEXT);

        ScreenCalSplash screenCalSplash = new ScreenCalSplash();
        screenCalSplash.tapAddCalendarButton();
        screenCalSplash.handleInButtonHint();
        new ScreenUpdates();

        cal("go_to_cal");
    }

    @TestAction(value = "cal")
    public static void cal() {
        cal("cal");
    }

    public static void cal(String screenshotName) {
        new ScreenUpdates().openCalendarScreen();

        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "cal_tap_expose")
    public static void cal_tap_expose() {
        new ScreenCalendar().openExposeScreen();

        TestUtils.delayAndCaptureScreenshot("cal_tap_expose");
    }

    @TestAction(value = "cal_tap_expose_reset")
    public static void cal_tap_expose_reset() {
        HardwareActions.pressBack();
        new ScreenCalendar();

        TestUtils.delayAndCaptureScreenshot("cal_tap_expose_reset");
    }

    @TestAction(value = "cal_tap_back")
    public static void cal_tap_back() {
        HardwareActions.pressBack();
        new ScreenUpdates();

        TestUtils.delayAndCaptureScreenshot("cal_tap_back");
    }

    @TestAction(value = "cal_scroll_load_next")
    public static void cal_scroll_load_next() {
        Assert.assertNotNull("Where is no ListView on screen or it's null",
                ListViewUtils.getFirstListView());
        final int countOfEvents = ListViewUtils.getFirstListView().getCount();
        Logger.i("Scroll to bottom of screen");
        HardwareActions.scrollToBottomNTimes(1, 0);
        WaitActions.waitForProgressBarDisappear(1, DataProvider.WAIT_DELAY_DEFAULT);
        WaitActions.waitForTrueInFunction("New events are not loaded", new Callable<Boolean>() {
            public Boolean call() {
                ListView firstListView = ListViewUtils.getFirstListView();
                if (firstListView != null)
                    return (firstListView.getCount() > countOfEvents);
                else
                    return false;
            }
        });

        TestUtils.delayAndCaptureScreenshot("cal_scroll_load_next");
    }

    @TestAction(value = "cal_scroll_load_previous")
    public static void cal_scroll_load_previous() {
        Assert.assertNotNull("Where is no ListView on screen or it's null",
                ListViewUtils.getFirstListView());
        final int countOfEvents = ListViewUtils.getFirstListView().getCount();
        Logger.i("Scroll to top of screen");
        getSolo().scrollToTop();
        WaitActions.waitForProgressBarDisappear(0, DataProvider.WAIT_DELAY_DEFAULT);
        WaitActions.waitForTrueInFunction("New events are not loaded", new Callable<Boolean>() {
            public Boolean call() {
                ListView firstListView = ListViewUtils.getFirstListView();
                if (firstListView != null)
                    return (firstListView.getCount() > countOfEvents);
                else
                    return false;
            }
        });

        TestUtils.delayAndCaptureScreenshot("cal_scroll_load_previous");
    }

    @TestAction(value = "cal_tap_today")
    public static void cal_tap_today() {
        ScreenCalendar screenCalendar = new ScreenCalendar();
        screenCalendar.tapOnTodayButton();
        // Wait for animation finished.
        WaitActions.waitForScreenUpdate();

        Assert.assertTrue("Calendar displayes not today date", screenCalendar.isOnToday());

        TestUtils.delayAndCaptureScreenshot("cal_tap_today");
    }
}
