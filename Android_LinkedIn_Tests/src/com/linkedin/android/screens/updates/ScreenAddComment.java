package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.EditText;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;

/**
 * Screen to add comment.
 * 
 * @author alexey.makhalov
 * @created Sep 17, 2012 6:18:41 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenAddComment extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.AddCommentActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "AddCommentActivity";

    public static final int SEND_BUTTON_INDEX = 0;
    public static final int COMMENT_FIELD_INDEX = 0;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenAddComment() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        // Verify current activity.
        Assert.assertTrue(
                "Wrong activity " + ACTIVITY_SHORT_CLASSNAME, 
                getSolo().getCurrentActivity().getClass().getSimpleName()
                .equals(ACTIVITY_SHORT_CLASSNAME));
        
        Assert.assertTrue("'LinkedIn' label is not present",
                getSolo().waitForText("LinkedIn"));
        
        Button sendButton = getSolo().getButton("Send");
        EditText commentTextEditor = getSolo().getEditText(COMMENT_FIELD_INDEX);
        
        Assert.assertNotNull("'Send' button is not presented", sendButton);
        Assert.assertNotNull("'Comment Field' text editor is not presented", commentTextEditor);
        
        Assert.assertTrue("'Send' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(sendButton,
                LayoutUtils.UPPER_RIGHT_BUTTON_LAYOUT)); 
        
        HardwareActions.takeCurrentActivityScreenshot("Add Comment screen");
    }

    @Override
    public void waitForMe() {
        getSolo().waitForActivity(ACTIVITY_SHORT_CLASSNAME);
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Send' button.
     */
    public void tapOnSendButton() {
        Button shareButton = getSendButton();

        Logger.i("Tapping on 'Send' button");
        getSolo().clickOnView(shareButton);
    }

    /**
     * Gets 'Send' button.
     * 
     * @return Send button
     */
    private static Button getSendButton() {
        Button shareButton = getSolo().getButton(SEND_BUTTON_INDEX);
        Assert.assertNotNull("'Send' button is  not present.", shareButton);
        return shareButton;
    }

    /**
     * Types update text to 'Share an update' field.
     * 
     * @param updateText
     *              text to share. If null - uses random generated text
     * @return update text that was entered.
     */
    public String typeUpdateText(String updateText) {
        if (updateText == null) 
            updateText = "Test update " + Math.random();
        Assert.assertNotNull("'Share an update' field is not present.", getSolo().getEditText(0));

        Logger.i("Typing random update text: '" + updateText + "'");
        getSolo().enterText(0, updateText);

        return updateText;
    }
    
    /**
     * Taps on send button and verify toasts "Posting comment" and "Comment sent".
     */
    public void postComment()
    {
        verifyTwoToastsStart("Posting comment", "Comment sent");
        tapOnSendButton();
        verifyTwoToastsEnd();
        
    }
}
