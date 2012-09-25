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
 * Class for Jobs screen (this screen is not working on emulator).
 * 
 * @author Aleksey.Chichagov
 * @created Aug 30, 2012 12:20:37 PM
 */
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
        Assert.assertTrue("'New Discussion' label is not present",
                getSolo().waitForText("New Discussion", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        Button sendButton = getSolo().getButton("Send");
        Assert.assertNotNull("'Send' button is not presented.", sendButton);
        
        Assert.assertNotNull("'Edit discussion text' view is not presented.", getSolo().getEditText(0));
        Assert.assertNotNull("'Edit detail text' view is not presented.", getSolo().getEditText(1));
        
        HardwareActions.takeCurrentActivityScreenshot("New Discussion screen");
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
        ViewUtils.tapOnView(sendButton, "'Send' button");
    }
    
    /**
     * Types random text of your discussion.
     * 
     * @return text
     */
    public String typeRandomTextOfDiscussion() {
        String text = "Text " + Math.random();
        Assert.assertNotNull("Text field is not present.", getSolo().getEditText(0));

        Logger.i("Typing random text: '" + text + "'");
        getSolo().enterText(0, text);

        return text;
    }
    
    /**
     * Types random detail text of your discussion.
     * 
     * @return text
     */
    public String typeRandomDetailTextOfDiscussion() {
        String text = "Detail " + Math.random();
        Assert.assertNotNull("Detail field is not present.", getSolo().getEditText(1));

        Logger.i("Typing random detail text: '" + text + "'");
        getSolo().enterText(1, text);

        return text;
    }
    
}