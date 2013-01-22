package com.linkedin.android.screens.you;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.ContactInfoUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for test Profile to invite screen.
 * 
 * @author vasily.gancharov
 * @created Aug 23, 2012 18:40:37 PM
 */
public class ScreenProfileOfNotConnectedUser extends ScreenProfile {

    // CONSTANTS ------------------------------------------------------------
    static final Rect2DP INVITE_TO_CONNECT_BUTTON_RECT = new Rect2DP(112.0f, 212.0f, 96.0f, 40.0f);
    static final Rect2DP FORWARD_IMAGE_BUTTON_RECT = new Rect2DP(182.0f, 212.0f, 130.0f, 40.0f);

    public static final Rect2DP INVITE_TO_CONNECT_BUTTON_LAYOUT = new Rect2DP(0, 208, 180.0f,
            151.0f);
    public static final Rect2DP FORWARD_IMAGE_BUTTON_LAYOUT = new Rect2DP(170.0f, 210.0f, 150.0f,
            60.0f);

    static final int INVITE_TO_CONNECT_BUTTON_INDEX = 0;
    static final int FORWARD_IMAGE_BUTTON_INDEX = 0;

    private static ViewIdName DEGREE = new ViewIdName("degree");
    
    // METHODS --------------------------------------------------------------
    /**
     * Gets image of profile degree.
     * 
     * @return {@code ImageView} degree of profile.
     */
    private ImageView getProfileDegree() {
        View degree = Id.getViewByViewIdName(DEGREE);
        if (degree instanceof ImageView) {
            return (ImageView) degree;
        }
        return null;
    }

    /**
     * Gets number of degree profile.
     * 
     * @return Int number of degree.
     */
    public int getNumberOfDegree() {
        return ContactInfoUtils.getContactDegree(getProfileDegree());
    }

    /**
     * Gets 'Invite to connect' image button
     * 
     * @return Call image button
     */
    private Button getInviteToConnectButton() {
        return getSolo().getButton(INVITE_TO_CONNECT_BUTTON_INDEX);
    }

    /**
     * Gets 'Forward' image button
     * 
     * @return Forward image button
     */
    private ImageButton getForwardButton() {
        return getSolo().getImageButton(FORWARD_IMAGE_BUTTON_INDEX);
    }

    /**
     * Taps on 'Invite to connect' button and checks toasts.
     */
    public void inviteByTappingOnInviteToConnectButton() {
        Button inviteToConnectButton = getInviteToConnectButton();

        verifyTwoToastsStart("Sending invitation", "Invitation sent");
        ViewUtils.tapOnView(inviteToConnectButton, "'Invite to connect' button");
        verifyTwoToastsEnd();
    }

    /**
     * Taps on 'Forward' button.
     */
    public void tapOnForwardButton() {
        ImageButton forwardButton = getForwardButton();
        ViewUtils.tapOnView(forwardButton, "'Forward' button.");
    }
}
