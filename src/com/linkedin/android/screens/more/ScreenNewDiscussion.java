package com.linkedin.android.screens.more;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.Button;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.screens.common.ScreenGroups;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Jobs screen (this screen is not working on emulator).
 * 
 * @author Aleksey.Chichagov
 * @created Aug 30, 2012 12:20:37 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenNewDiscussion extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupDiscussionAddActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupDiscussionAddActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewDiscussion() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue(
                "'New Discussion' label is not present",
                getSolo().waitForText("New Discussion", 1, DataProvider.WAIT_DELAY_LONG, false,
                        false));

        Button sendButton = getSolo().getButton("Send");
        Assert.assertNotNull("'Send' button is not presented.", sendButton);

        Assert.assertNotNull("'Edit discussion text' view is not presented.", getSolo()
                .getEditText(0));
        Assert.assertNotNull("'Edit detail text' view is not presented.", getSolo().getEditText(1));
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "New Discussion");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Send' button.
     */
    public void tapOnSendButton() {
        Button sendButton = getSolo().getButton("Send");
        ViewUtils.tapOnView(sendButton, "'Send' button");
    }

    /**
     * Verify that 'New Discussion' screen dismissed.
     */
    public void verifyThatNewDiscussionScreenDismissed() {
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'New Discussion' screen is not dissapear", new Callable<Boolean>() {
                    public Boolean call() {
                        return !(isCurrentActivityOpened());
                    }
                });
    }

    // ACTIONS --------------------------------------------------------------
    public static void groups_discussion_list_compose(String screenshotName) {
        new ScreenYourGroup().openNewDiscussionScreen();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "groups_discussion_list_compose")
    public static void groups_discussion_list_compose() {
        groups_discussion_list_compose("groups_discussion_list_compose");
    }

    @TestAction(value = "go_to_groups_discussion_list_compose")
    public static void go_to_groups_discussion_list_compose(String email, String password) {
        ScreenGroups.go_to_groups(email, password);
        ScreenGroups.groups_tap_group();
        groups_discussion_list_compose("go_to_groups_discussion_list_compose");
    }

    @TestAction(value = "groups_discussion_list_compose_tap_cancel")
    public static void groups_discussion_list_compose_tap_cancel() {
        HardwareActions.goBackOnPreviousActivity();
        TestUtils.delayAndCaptureScreenshot("groups_discussion_list_compose_tap_cancel");
    }

    /*
     * @TestAction(value = "groups_discussion_list_compose_tap_post") public
     * static void groups_discussion_list_compose_tap_post() {
     * Assert.assertNotNull("Text field is not present.",
     * getSolo().getEditText(0));
     * 
     * Logger.i("Typing text in discussion"); getSolo().enterText(0,
     * TestUtils.verifyText("Cool Group"));
     * 
     * ScreenNewDiscussion screenNewDiscussion = new ScreenNewDiscussion();
     * screenNewDiscussion.tapOnSendButton();
     * 
     * screenNewDiscussion.verifyThatNewDiscussionScreenDismissed();
     * TestUtils.delayAndCaptureScreenshot
     * ("groups_discussion_list_compose_tap_post"); }
     */
}