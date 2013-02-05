package com.linkedin.android.screens.base;

import java.util.ArrayList;

import junit.framework.Assert;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linkedin.android.popups.PopupForward;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenReplyMessage;
import com.linkedin.android.screens.updates.ScreenAddComment;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ImageButtonUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

public abstract class BaseScreenSharedNewsDetails extends BaseINScreen {

    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.home.DetailsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "DetailsListActivity";

    static final Rect2DP IMAGE_RECT = new Rect2DP(15, 95, 50, 50);
    static final Rect2DP IMAGE_NEWS_RECT = new Rect2DP(11, 222, 53, 53);

    public static final ViewIdName CONNECTION_PROFILE_SECTION_CHEVRON = new ViewIdName(
            "sht2_header_title");
    // ImageButton: like it button
    private static final ViewIdName LIKE_BUTTON = new ViewIdName("like_button");
    // TextView: like text '1 person likes this' for example
    public static final ViewIdName LIKE_TEXT = new ViewIdName("likes_text");
    // ImageButton: comment it button
    private static final ViewIdName COMMENT_BUTTON = new ViewIdName("comment_button");
    // TextView's for options in Forward popup.
    public static final ViewIdName OPTION_IN_FORWARD_POPUP = new ViewIdName("text1");
    // Static ID of options on Forward popup.
    public static final int OPTION_ID_FORWARD_POPUP = 16908308;

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public BaseScreenSharedNewsDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    public BaseScreenSharedNewsDetails(String activityClassname) {
        super(activityClassname);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'Details' label is not present",
                getSolo().waitForText("Details", 1, DataProvider.WAIT_DELAY_LONG, false, false));

        ImageButton likeButton = ImageButtonUtils.getImageButtonByIndex(0);
        ImageButton commentButton = ImageButtonUtils.getImageButtonByIndex(1);
        ImageButton forwardButton = ImageButtonUtils.getImageButtonByIndex(2);

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
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Shared");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on Forward button.
     */
    public void tapOnForwardButton() {
        ImageButton forwardButton = getSolo().getImageButton(2);
        Assert.assertNotNull("'Forward' button is not present.", forwardButton);
        ViewUtils.tapOnView(forwardButton, "'Forward' button");
    }

    /**
     * Returns {@code ArrayList} of options (as {@code String}) in Forward popup
     * (including "Cancel").
     * 
     * @return {@code ArrayList} of options (as {@code String}) in Forward popup
     */
    public ArrayList<String> getCountOptionsForForwardPopup() {
        ArrayList<String> options = new ArrayList<String>();
        ArrayList<View> views = Id.getListOfViewById(OPTION_ID_FORWARD_POPUP);
        for (View view : views) {
            if (!(view instanceof TextView))
                Assert.fail("Wrong ID in Forward popup for options");
            options.add(((TextView) view).getText().toString());
        }
        if (getSolo().searchText("Cancel"))
            options.add("Cancel");

        return options;
    }

    /**
     * Taps on Like button.
     */
    public void tapOnLikeButton() {
        ImageButton likeButton = (ImageButton) Id.getViewByViewIdName(LIKE_BUTTON);
        Assert.assertTrue("'Like' button is not present.", likeButton.isShown());
        ViewUtils.tapOnView(likeButton, "'Like' button");
    }

    /**
     * Taps on Comment button.
     */
    public void tapOnCommentButton() {
        ImageButton comment = (ImageButton) Id.getViewByViewIdName(COMMENT_BUTTON);
        ViewUtils.tapOnView(comment, "'Comment' button");
    }

    /**
     * Opens AddComment screen.
     */
    public ScreenAddComment openAddCommentScreen() {
        tapOnCommentButton();
        return new ScreenAddComment();
    }

    /**
     * Taps on Cancel button on popup.
     */
    public void tapOnCancelButtonOnPopup() {
        PopupForward popup = new PopupForward(PopupForward.CANCEL_TEXT);
        popup.tapOnCancelButtonOnPopup();
    }

    /**
     * Tap on 'Share' -> 'Send to Connection' Opens 'New Message' screen.
     * 
     * @return {@code ScreenNewMessage} with just opened 'New Message' screen.
     */
    public ScreenNewMessage openSendToConnectionScreen() {
        tapOnForwardButton();
        PopupForward popup = new PopupForward(PopupForward.SEND_TO_CONNECTION_TEXT);
        popup.tapOnOption(PopupForward.SEND_TO_CONNECTION_TEXT);

        return new ScreenNewMessage();
    }

    /**
     * Tap on 'Share' -> 'Reply Privately' Opens 'New Message' screen.
     * 
     * @return {@code ScreenNewMessage} with just opened 'New Message' screen.
     */
    public ScreenReplyMessage openReplyPrivatelyScreen() {
        tapOnForwardButton();
        PopupForward popup = new PopupForward(PopupForward.REPLY_PRIVATELY_TEXT);
        popup.tapOnOption(PopupForward.REPLY_PRIVATELY_TEXT);
        return new ScreenReplyMessage();
    }

    /**
     * Taps on image of connection, who create discussion.
     */
    public static void tapOnConnectionProfile() {
        View connectionProfileSectionChevron = Id
                .getViewByViewIdName(CONNECTION_PROFILE_SECTION_CHEVRON);
        Assert.assertNotNull("There is no chevron in 'Discussion author' section",
                connectionProfileSectionChevron);
        ViewUtils.tapOnView(connectionProfileSectionChevron, "'Author' row");
    }

    /**
     * Opens Connection Profile whos create dicussion.
     */
    public ScreenProfile openConnectionProfile() {
        tapOnConnectionProfile();
        return new ScreenProfile();
    }
}
