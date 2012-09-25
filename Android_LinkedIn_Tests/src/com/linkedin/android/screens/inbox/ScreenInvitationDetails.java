package com.linkedin.android.screens.inbox;

import junit.framework.Assert;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for Invitation Detail screen.
 * 
 * @author Aleksey.Chichagov
 * @created Sep 11, 2012 3:27:37 PM
 */
public class ScreenInvitationDetails extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.invitations.ViewInvitationActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ViewInvitationActivity";

    static final Rect2DP UP_ARROW_BUTTON = new Rect2DP(225, 25, 43, 55);
    static final Rect2DP DOWN_ARROW_BUTTON = new Rect2DP(267, 25, 43, 55);

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenInvitationDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("Title '.. of ..' is not present", getSolo().getText(0).getText()
                .toString().indexOf("of") > -1);

        ImageButton okButton = getSolo().getImageButton(0);
        ImageButton cancelButton = getSolo().getImageButton(1);
        ImageButton replayButton = getSolo().getImageButton(2);

        Assert.assertTrue("'Ok' button is not present (or its coordinates are wrong)", LayoutUtils
                .isViewPlacedInLayout(okButton, LayoutUtils.LOWER_LEFT_OF_3_BUTTONS_LAYOUT));
        Assert.assertTrue("'Cancel' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(cancelButton,
                        LayoutUtils.LOWER_CENTER_OF_3_BUTTONS_LAYOUT));
        Assert.assertTrue("'Replay' button is not present (or its coordinates are wrong)",
                LayoutUtils.isViewPlacedInLayout(replayButton,
                        LayoutUtils.LOWER_RIGHT_OF_3_BUTTONS_LAYOUT));

        Assert.assertNotNull("'Up arrow' button is not presented", getUpArrowButton());
        Assert.assertNotNull("'Down arrow' button is not presented", getDownArrowButton());

        verifyINButton();

        HardwareActions.takeCurrentActivityScreenshot("Invitation screen");
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
     * Gets 'Up Arrow' button {@code ImageView}
     * 
     * @return 'Up Arrow' button {@code ImageView}
     */
    public ImageView getUpArrowButton() {
        ImageView upArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.UP_ARROW_BUTTON_LAYOUT, UP_ARROW_BUTTON.width, UP_ARROW_BUTTON.height,
                Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
        return upArrowButton;
    }

    /**
     * Gets 'Down Arrow' button {@code ImageView}.
     * 
     * @return 'Down Arrow' button {@code ImageView}.
     */
    public ImageView getDownArrowButton() {
        ImageView downArrowButton = LayoutUtils.getImageViewByItsLayoutAndSize(
                LayoutUtils.DOWN_ARROW_BUTTON_LAYOUT, DOWN_ARROW_BUTTON.width,
                DOWN_ARROW_BUTTON.height, Rect2DP.DEFAULT_ACCURACY_OF_COMPARING);
        return downArrowButton;
    }

    /**
     * Taps on 'Ok' button.
     */
    public void tapOnOkButton() {
        ImageButton okButton = getSolo().getImageButton(0);
        ViewUtils.tapOnView(okButton, "Ok");
    }

    /**
     * Taps on 'Cancel' button.
     */
    public void tapOnCancelButton() {
        ImageButton cancelButton = getSolo().getImageButton(1);
        ViewUtils.tapOnView(cancelButton, "Cancel");
    }

    /**
     * Taps on 'Replay' button.
     */
    public void tapOnReplayButton() {
        ImageButton replayButton = getSolo().getImageButton(2);
        ViewUtils.tapOnView(replayButton, "Replay");
    }

    /**
     * Taps on 'Up Arrow' button.
     */
    public void tapOnUpArrowButton() {
        ImageView upArrowButton = getUpArrowButton();
        ViewUtils.tapOnView(upArrowButton, "Up arrow button");
    }

    /**
     * Taps on 'Down Arrow' button.
     */
    public void tapOnDownArrowButton() {
        ImageView downArrowButton = getDownArrowButton();
        ViewUtils.tapOnView(downArrowButton, "Down arrow button");
    }

    /**
     * Taps on Connection, who send invite.
     */
    public void tapOnConnectionProfile() {
        TextView connection = getSolo().getText(1);
        ViewUtils.tapOnView(connection, "Connection profile");
    }

    /**
     * Opens {@code ScreenProfileOfNotConnectedUser} of first user in invitation
     * list.
     * 
     * @return {@code ScreenProfileOfNotConnectedUser}.
     */
    public ScreenProfileOfNotConnectedUser openConnectionProfile() {
        tapOnConnectionProfile();
        return new ScreenProfileOfNotConnectedUser();
    }
}