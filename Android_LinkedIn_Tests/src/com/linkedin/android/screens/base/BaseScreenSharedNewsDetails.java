package com.linkedin.android.screens.base;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageButton;

import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenReplyMessage;
import com.linkedin.android.screens.updates.ScreenAddComment;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;

public abstract class BaseScreenSharedNewsDetails extends BaseINScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.DetailsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "DetailsListActivity";

    static final Rect2DP IMAGE_RECT = new Rect2DP(15, 95, 50, 50);
    static final Rect2DP IMAGE_NEWS_RECT = new Rect2DP(11, 222, 53, 53);

    protected static final ViewIdName CONNECTION_PROFILE_SECTION_CHEVRON = new ViewIdName(
            "connections_chevron");

    // ImageButton: like it button
    private static final ViewIdName LIKE_BUTTON = new ViewIdName("like_button");
    // TextView: like text '1 person likes this' for example
    public static final ViewIdName LIKE_TEXT = new ViewIdName("likes_text");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public BaseScreenSharedNewsDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Details' label is not present",
                getSolo().waitForText("Details", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        ImageButton likeButton = getSolo().getImageButton(0);
        ImageButton commentButton = getSolo().getImageButton(1);
        ImageButton forwardButton = getSolo().getImageButton(2);

        Assert.assertNotNull("'Like' button is not present", likeButton);
        Assert.assertNotNull("'Comment' button is not present", commentButton);
        Assert.assertNotNull("'Forward' button is not present", forwardButton);

        Assert.assertTrue("'Like' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(likeButton,
                        LayoutUtils.LOWER_LEFT_OF_3_BUTTONS_LAYOUT));
        Assert.assertTrue("'Comment' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(commentButton,
                        LayoutUtils.LOWER_CENTER_OF_3_BUTTONS_LAYOUT));
        Assert.assertTrue("'Forward' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(forwardButton,
                        LayoutUtils.LOWER_RIGHT_OF_3_BUTTONS_LAYOUT));

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
     * Taps on Forward button and wait for popup appears
     * 
     * @return number of popup menu items except cancel button
     */
    public int tapOnForwardButton() {
        int nr = 0;
        Assert.assertNotNull("'Forward' button is not present.", getSolo().getImageButton(2));

        Logger.i("Tapping on 'Forward' button");
        getSolo().clickOnImageButton(2);

        if (getSolo().searchText("Share"))
            nr++;
        if (getSolo().searchText("Send to Connection"))
            nr++;
        if (getSolo().searchText("Reply Privately"))
            nr++;

        getSolo().searchText("Cancel");
        // TODO: May be better to return ArrayList of String
        // But it is not necessary now.
        return nr;
    }

    /**
     * Taps on Like button.
     */
    public void tapOnLikeButton() {
        ImageButton likeButton = (ImageButton) Id.getViewByName(LIKE_BUTTON);
        Assert.assertTrue("'Like' button is not present.", likeButton.isShown());

        Logger.i("Tapping on 'Like' button");
        getSolo().clickOnView(likeButton);
    }

    /**
     * Taps on Comment button.
     */
    public void tapOnCommentButton() {
        Assert.assertNotNull("'Comment' button is not present.", getSolo().getImageButton(1));

        Logger.i("Tapping on 'Comment' button");
        getSolo().clickOnImageButton(1);
    }

    /**
     * Opens AddComment screen.
     */
    public ScreenAddComment openAddCommentScreen() {
        tapOnCommentButton();
        return new ScreenAddComment();
    }

    /**
     * Taps on Send to Connection on popup.
     */
    public void tapOnSendToConnectionOnPopup() {
        Assert.assertNotNull("'SendToConnection' button is not present.",
                getSolo().searchText("Send to Connection"));

        Logger.i("Tapping on 'SendToConnection' button");
        getSolo().clickOnText("Send to Connection");
    }

    /**
     * Taps on Reply Privately on popup.
     */
    public void tapOnReplyPrivatelyOnPopup() {
        Assert.assertTrue("'Reply Privately' button is not present.",
                getSolo().searchText("Reply Privately"));

        Logger.i("Tapping on 'Reply Privately' button");
        getSolo().clickOnText("Reply Privately");
    }

    /**
     * Taps on Cancel button on popup.
     */
    public void tapOnCancelButtonOnPopup() {
        Assert.assertTrue("'Cancel' button is not present.", getSolo().searchText("Cancel"));

        Logger.i("Tapping on 'Cancel' button");
        getSolo().clickOnText("Cancel");
    }

    /**
     * Tap on 'Share' -> 'Send to Connection' Opens 'New Message' screen.
     * 
     * @return {@code ScreenNewMessage} with just opened 'New Message' screen.
     */
    public ScreenNewMessage openSendToConnectionScreen() {
        tapOnForwardButton();
        getSolo().waitForText("Send to Connection", 1, DataProvider.WAIT_DELAY_DEFAULT);
        tapOnSendToConnectionOnPopup();

        return new ScreenNewMessage();
    }

    /**
     * Tap on 'Share' -> 'Reply Privately' Opens 'New Message' screen.
     * 
     * @return {@code ScreenNewMessage} with just opened 'New Message' screen.
     */
    public ScreenReplyMessage openReplyPrivatelyScreen() {
        tapOnForwardButton();
        getSolo().waitForText("Reply Privately", 1, DataProvider.WAIT_DELAY_DEFAULT);
        tapOnReplyPrivatelyOnPopup();

        return new ScreenReplyMessage();
    }

    /**
     * Taps on image of connection, who create discussion.
     */
    public void tapOnConnectionProfile() {
        View connectionProfileSectionChevron = Id.getViewByName(CONNECTION_PROFILE_SECTION_CHEVRON);
        Assert.assertNotNull("There is no chevron in 'Discussion author' section",
                connectionProfileSectionChevron);

        getSolo().clickOnView(connectionProfileSectionChevron);
    }

}
