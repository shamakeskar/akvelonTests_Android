package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.Button;
import android.widget.EditText;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Screen to share news article with comment
 * 
 * @author vasily.gancharov
 * @created Aug 13, 2012 1:35:35 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenShareNewsArticle extends BaseScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.UpdateStatusActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "UpdateStatusActivity";

    public static final int ADD_COMMENT_TEXT_FIELD_MAX_VALUE_LENGTH = 666;
    public static final int ADD_COMMENT_TEXT_FIELD_DEFAULT_VALUE_LENGTH = 15;

    public static final String SHARE_BUTTON_LABEL = "Share";
    public static final String COMMENT_FIELD_NAME = "";

    public static final int SHARE_BUTTON_INDEX = 0;
    public static final int COMMENT_FIELD_INDEX = 0;

    // TODO change to valid coordinates and size
    static final Rect2DP SHARE_BUTTON_FRAME_RECT = new Rect2DP(0.0f, 28.0f, 54.6f, 49.3f);

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenShareNewsArticle() {
        super(ACTIVITY_CLASSNAME);
    }

    @Override
    public void verify() {
        // Verify current activity.
        Assert.assertTrue("Wrong activity " + ACTIVITY_SHORT_CLASSNAME, getSolo()
                .getCurrentActivity().getClass().getSimpleName().equals(ACTIVITY_SHORT_CLASSNAME));

        Assert.assertTrue("'LinkedIn' label is not present", getSolo().waitForText("LinkedIn"));

        Button shareButton = getSolo().getButton(SHARE_BUTTON_LABEL);
        EditText commentTextEditor = getSolo().getEditText(COMMENT_FIELD_INDEX);

        Assert.assertNotNull("'Share' button is not presented", shareButton);
        Assert.assertNotNull("'Comment Field' text editor is not presented", commentTextEditor);

        HardwareActions.takeCurrentActivityScreenshot("Share News Article screen.");
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Share News Article");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Types comment to Comment field.
     * 
     * @return comment that was entered.
     */
    public String typeRandomComment() {
        String comment = "Test comment " + Math.random();
        Assert.assertNotNull("Comment field is not present.", getSolo().getEditText(0));

        Logger.i("Typing random comment: '" + comment + "'");
        getSolo().enterText(0, comment);

        return comment;
    }

    /**
     * Types comment to Comment field.
     * 
     * @return comment that was entered.
     */
    public void typeInComment(String comment) {
        typeTextToField(COMMENT_FIELD_INDEX, COMMENT_FIELD_NAME, comment);
    }

    /**
     * Shares news article with comment
     * 
     */
    public ScreenNewsArticleDetails shareComment() {

        verifyTwoToastsStart("Sending update", "Update sent");
        tapOnShareButton();
        verifyTwoToastsEnd();

        ScreenNewsArticleDetails newsArticleDetailsScreen = new ScreenNewsArticleDetails();
        return newsArticleDetailsScreen;
    }

    /**
     * Taps on 'Share' button.
     */
    public void tapOnShareButton() {
        Button shareButton = getShareButton();
        ViewUtils.tapOnView(shareButton, "'Share' button");
    }

    /**
     * Gets Share button
     * 
     * @return Share button
     */
    private static Button getShareButton() {
        Button shareButton = getSolo().getButton(SHARE_BUTTON_INDEX);
        Assert.assertNotNull("'Share' button not present", shareButton);
        return shareButton;
    }

    /**
     * Shares the news by tapping on Share button and verify toasts.
     */
    public void shareNews() {
        verifyTwoToastsStart("Sending update", "Update sent");
        tapOnShareButton();
        verifyTwoToastsEnd();
    }
}
