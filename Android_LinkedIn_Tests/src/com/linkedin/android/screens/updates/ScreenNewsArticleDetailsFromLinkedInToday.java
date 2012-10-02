package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ViewAssertUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for News article Details screen (opened from LINKEDIN TODAY screen).
 * 
 * @author dmitry.somov
 * @created Aug 14, 2012 2:54:37 PM
 */
public class ScreenNewsArticleDetailsFromLinkedInToday extends ScreenNewsArticleDetails {
    // CONSTANTS ------------------------------------------------------------
    static final Rect2DP UP_ARROW_BUTTON = new Rect2DP(224.7f, 25.4f, 42.7f, 55.4f);
    static final Rect2DP DOWN_ARROW_BUTTON = new Rect2DP(267, 25, 45, 57);

    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.news.ViewArticleActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewArticleActivity";

    private static final int MESSAGE_BUTTON_INDEX = 0;
    private static final int FORWARD_BUTTON_INDEX = 1;

    private static final int ARTICLE_NUMBER_TITLE_INDEX = 0;

    private static final ViewIdName UP_ARROW_BUTTON_ID_NAME = new ViewIdName("nav_inbox_previous");
    private static final ViewIdName DOWN_ARROW_BUTTON_ID_NAME = new ViewIdName("nav_inbox_next");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenNewsArticleDetailsFromLinkedInToday() {
        super();
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @Override
    public void verify() {
        getSolo().assertCurrentActivity(
                "Wrong activity (expected " + ACTIVITY_CLASSNAME + ", get "
                        + getSolo().getCurrentActivity().getClass().getName() + ")",
                ACTIVITY_SHORT_CLASSNAME);
        Assert.assertTrue("Title '.. of ..' is not present", getSolo().getText(0).getText()
                .toString().indexOf("of") > -1);

        verifyUpArrowButton();

        verifyDownArrowButton();

        verifyINButton();

        ImageButton forwardButton = getSolo().getImageButton(FORWARD_BUTTON_INDEX);
        ImageButton messageButton = getSolo().getImageButton(MESSAGE_BUTTON_INDEX);

        Assert.assertNotNull("'Forward' button is not present", forwardButton);
        Assert.assertNotNull("'Message' button is not present", messageButton);

        Assert.assertTrue("'Send' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(messageButton,
                        LayoutUtils.LOWER_LEFT_OF_2_BUTTONS_LAYOUT));
        Assert.assertTrue("'Forward' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(forwardButton,
                        LayoutUtils.LOWER_RIGHT_OF_2_BUTTONS_LAYOUT));

        verifyArticleNumberTitle();

        verifyImage();

        HardwareActions.takeCurrentActivityScreenshot("News Article Details screen");
    }

    /**
     * Gets Down Arrow Button.
     * 
     * @return Down Arrow Button {@code ImageView}.
     */
    public ImageView getDownArrowButton() {
        ImageView downArrowButton = Id.getViewByName(DOWN_ARROW_BUTTON_ID_NAME, ImageView.class);
        return downArrowButton;
    }

    /**
     * Gets Up Arrow Button.
     * 
     * @return Up Arrow Button {@code ImageView}.
     */
    public ImageView getUpArrowButton() {
        ImageView downArrowButton = Id.getViewByName(UP_ARROW_BUTTON_ID_NAME, ImageView.class);
        return downArrowButton;
    }

    /**
     * Taps on up arrow button. (Change current article).
     */
    public void tapOnUpArrowButton() {
        ImageView upArrowButton = getUpArrowButton();

        ViewUtils.tapOnView(upArrowButton, "up arrow button");
    }

    /**
     * Taps on down arrow button. (Change current article).
     */
    public void tapOnDownArrowButton() {
        ImageView downArrowButton = getDownArrowButton();

        ViewUtils.tapOnView(downArrowButton, "down arrow button");
    }

    /**
     * Verify Down Arrow Button.
     */
    private void verifyDownArrowButton() {
        ImageView downArrowButton = getDownArrowButton();
        ViewAssertUtils.assertViewIsPlacedInLayout(
                "'Down Arrow' button is not present (or its coordinates are wrong)",
                downArrowButton, LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT);
    }

    /**
     * Verify Up Arrow Button.
     */
    private void verifyUpArrowButton() {
        ImageView upArrowButton = getUpArrowButton();
        Assert.assertNotNull("'Up arrow' button is not presented", upArrowButton);
        WaitActions.waitForViewRect(upArrowButton, UP_ARROW_BUTTON,
                DataProvider.WAIT_DELAY_DEFAULT, "'Up Arrow' button coordinates are wrong");
    }

    /**
     * Verifies that article number title {@code TextView} loaded properly.
     */
    private void verifyArticleNumberTitle() {
        final int titleLoadWaitTimeSec = 3;
        final String articleNumberTitleSeparator = "of";

        if (!getArticleNumberTitleText().contains(articleNumberTitleSeparator)) {
            WaitActions.delay(titleLoadWaitTimeSec);
        }
        Assert.assertTrue("Title '.. of ..' is not present",
                getArticleNumberTitleText().contains(articleNumberTitleSeparator));
    }

    /**
     * Returns current article number title text.
     * 
     * @return current article number title text.
     */
    private String getArticleNumberTitleText() {
        TextView articleNumberTitle = getArticleNumberTitle();
        Assert.assertNotNull("There is no article number (...of...) title", articleNumberTitle);
        return articleNumberTitle.getText().toString();
    }

    /**
     * Returns current article number title {@code TextView}.
     * 
     * @return current article number title {@code TextView}.
     */
    private TextView getArticleNumberTitle() {
        return getSolo().getText(ARTICLE_NUMBER_TITLE_INDEX);
    }
}
