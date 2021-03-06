package com.linkedin.android.screens.more;

import junit.framework.Assert;
import android.widget.Button;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Comment Discussion screen.
 * 
 * @author Aleksey.Chichagov
 * @created Aug 30, 2012 3:48:37 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenCommentDiscussion extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupPostCommentAddActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupPostCommentAddActivity";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenCommentDiscussion() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Comment' label is not present",
                getSolo().waitForText("Comment", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        Button sendButton = getSolo().getButton("Send");
        Assert.assertNotNull("'Send' button is not presented.", sendButton);

        Assert.assertNotNull("'Edit text' view is not presented.", getSolo().getEditText(0));

        HardwareActions.takeCurrentActivityScreenshot("Comment discussion screen");
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
     * Taps on 'Send' button.
     */
    public void tapOnSendButton() {
        Button sendButton = getSolo().getButton("Send");
        Assert.assertNotNull("There is no 'Send' button on current screen", sendButton);
        verifyTwoToastsStart("Posting comment", "Comment sent");
        ViewUtils.tapOnView(sendButton, "'Send' button");
        verifyTwoToastsEnd();
    }

    /**
     * Types random comment.
     * 
     * @return comment
     */
    public String typeComment(String comment) {
        if (comment == null)
            comment = "Comment " + Math.random();
        Assert.assertNotNull("Comment field is not present.", getSolo().getEditText(0));

        Logger.i("Typing comment: '" + comment + "'");
        getSolo().enterText(0, comment);

        return comment;
    }
}