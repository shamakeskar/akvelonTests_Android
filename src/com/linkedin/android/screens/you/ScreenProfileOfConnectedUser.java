package com.linkedin.android.screens.you;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.ContactInfoUtils;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for test Profile from my contacts screen.
 * 
 * @author vasily.gancharov
 * @created Aug 23, 2012 18:45:37 PM
 */
public class ScreenProfileOfConnectedUser extends ScreenProfile {

    // CONSTANTS ------------------------------------------------------------
    static final Rect2DP CALL_BUTTON_RECT = new Rect2DP(8.0f, 212.0f, 96.0f, 40.0f);
    static final Rect2DP SEND_MESSAGE_BUTTON_RECT = new Rect2DP(112.0f, 212.0f, 96.0f, 40.0f);
    static final Rect2DP FORWARD_BUTTON_RECT = new Rect2DP(216.0f, 212.0f, 96.0f, 40.0f);

    public static final Rect2DP CALL_BUTTON_LAYOUT = new Rect2DP(0, 208,
            LayoutUtils.screenWidth / 3, 151.0f);
    public static final Rect2DP SEND_MESSAGE_BUTTON_LAYOUT = new Rect2DP(
            LayoutUtils.screenWidth / 3, 208, LayoutUtils.screenWidth / 3, 151.0f);
    public static final Rect2DP FORWARD_BUTTON_LAYOUT = new Rect2DP(
            LayoutUtils.screenWidth * 2 / 3, 208, LayoutUtils.screenWidth / 3, 151.0f);

    static final int CALL_BUTTON_INDEX = 0;
    static final int SEND_MESSAGE_BUTTON_INDEX = 1;
    static final int FORWARD_BUTTON_INDEX = 2;

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
     * Gets 'Call' image button
     * 
     * @return Call image button
     */
    private ImageButton getCallButton() {
        return getSolo().getImageButton(CALL_BUTTON_INDEX);
    }

    /**
     * Gets 'Send message' image button
     * 
     * @return Send message image button
     */
    private ImageButton getSendMessageButton() {
        return getSolo().getImageButton(SEND_MESSAGE_BUTTON_INDEX);
    }

    /**
     * Gets 'Forward' image button
     * 
     * @return Forward image button
     */
    private ImageButton getForwardButton() {
        return getSolo().getImageButton(FORWARD_BUTTON_INDEX);
    }

    /**
     * Taps on 'Call' button.
     */
    public void tapOnCallButton() {
        ImageButton callButton = getCallButton();
        ViewUtils.tapOnView(callButton, "'Call' button");
    }

    /**
     * Taps on 'Send Message' button.
     */
    public void tapOnSendMessageButton() {
        ImageButton sendMessageButton = getSendMessageButton();
        ViewUtils.tapOnView(sendMessageButton, "'Send Message' button.");
    }

    /**
     * Taps on 'Forward' button.
     */
    public void tapOnForwardButton() {
        ImageButton forwardButton = getForwardButton();
        ViewUtils.tapOnView(forwardButton, "'Forward' button.");
    }

    /**
     * Opens 'New Message' screen.
     * 
     * @return NewMessageScreen
     */
    public ScreenNewMessage openNewMessageScreen() {
        tapOnSendMessageButton();
        return new ScreenNewMessage();
    }

}
