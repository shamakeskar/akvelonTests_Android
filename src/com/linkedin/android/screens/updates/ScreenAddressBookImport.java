package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

@SuppressWarnings("rawtypes")
public class ScreenAddressBookImport extends BaseScreen {
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.common.MoreInfoActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "MoreInfoActivity";

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenAddressBookImport() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        String curActivityName = getSolo().getCurrentActivity().getClass().getSimpleName();
        Assert.assertTrue("Wrong activity (expected " + ACTIVITY_SHORT_CLASSNAME + ")",
                curActivityName.equals(ACTIVITY_SHORT_CLASSNAME));
    }

    @Override
    public void waitForMe() {
        getSolo().waitForActivity(ACTIVITY_SHORT_CLASSNAME);
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    public static boolean isOnLearnMoreScreen() {
        return getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ACTIVITY_SHORT_CLASSNAME);
    }

    /**
     * Taps on hardware Back button and call delayAndCaptureScreenshot with
     * specified screenshot name.
     * 
     * @param screenShotName
     *            screenshot name
     */
    private static void pressBack(String screenShotName) {
        HardwareActions.pressBack();
        TestUtils.delayAndCaptureScreenshot(screenShotName);
    }

    // ACTIONS --------------------------------------------------------------
    public static void go_to_address_book_import(String email, String password) {
        ScreenExpose.go_to_expose(email, password);
        TestUtils.delayAndCaptureScreenshot("go_to_expose");

        new ScreenExpose(null).openSettingsScreen();
        TestUtils.delayAndCaptureScreenshot("go_to_address_book_import");
    }

    public static void address_book_import_tap_add_connections() {
        Logger.i("Scrooling down");
        getSolo().scrollDown();        
        TextView addConnection = TextViewUtils.getTextViewByText("Add Connections");
        ViewUtils.tapOnView(addConnection, "Add Connections" + " button");
        TestUtils.delayAndCaptureScreenshot("address_book_import_tap_add_connections");
    }

    public static void address_book_import_tap_learn_more() {
        ViewUtils.waitForToastDisappear();
        Logger.i("Scrooling to the bottom");
        getSolo().scrollToBottom();        
        ViewUtils.tapOnLink("Learn More");
        TestUtils.delayAndCaptureScreenshot("tap_on_learn_more");

        new ScreenAddressBookImport();
        Assert.assertTrue("Learn More is present in the screen", isOnLearnMoreScreen());
        TestUtils.delayAndCaptureScreenshot("address_book_import_tap_learn_more");
    }

    public static void address_book_import_learn_more_reset() {
        pressBack("address_book_import_learn_more_reset");
    }

    public static void address_book_import_tap_continue() {
        Button continueBtn = getSolo().getButton("Continue");
        ViewUtils.tapOnView(continueBtn, "Continue button");
    }

    public static void address_book_import_wait_progress_bar() {
        Assert.assertTrue("Progress bar is not on the page", HardwareActions.waitProgressBar());
        TestUtils.delayAndCaptureScreenshot("address_book_import_wait_progress_bar");
    }

    public static void address_book_import_add_connections_reset() {
        TextView ok = TextViewUtils.getTextViewByText("OK");
        if (ok != null) {
            ViewUtils.tapOnView(ok, "OK button");
        }
        TestUtils.delayAndCaptureScreenshot("address_book_import_add_connections_reset");
    }
}
