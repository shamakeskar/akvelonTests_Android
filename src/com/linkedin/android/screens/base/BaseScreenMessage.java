package com.linkedin.android.screens.base;

import junit.framework.Assert;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.linkedin.android.screens.common.ScreenAddConnections;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * This is common messaging screen class.
 * Child should implement getHeader() method as minimum.
 * 
 * @author alexey.makhalov
 */
@SuppressWarnings("rawtypes")
public abstract class BaseScreenMessage extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.messages.MessageComposeActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "MessageComposeActivity";

    // "Add users" button (below "Share").
    public final static ViewIdName ADD_USERS_BUTTON = new ViewIdName("add_users_button");

    // CONSTRUCTORS ---------------------------------------------------------
    public BaseScreenMessage() {
        super(ACTIVITY_CLASSNAME);
    }

    // ABSTRACT METHODS -----------------------------------------------------
    /**
     * Returns screen header to compare with.
     */
    abstract protected String getHeader();

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        Assert.assertTrue("'" + getHeader() + "' label is not presented",
                getSolo().waitForText(getHeader()));

        Button sendButton = getSolo().getButton(0);
        ImageView addConnectionButton = (ImageView) Id.getViewByViewIdName(ADD_USERS_BUTTON);
        //getSolo().getImage(0);

        Assert.assertNotNull("'Send' button is not presented", sendButton);
        Assert.assertNotNull("'Add' button is not presented", addConnectionButton);

        HardwareActions.takeCurrentActivityScreenshot("Message screen (" + getHeader() + ")");
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
        Button sendButton = getSolo().getButton(0);
        ViewUtils.tapOnView(sendButton, "'Send' button");
    }

    /**
     * Taps on 'Send' button and verify toasts.
     */
    public void sendMessage() {
        verifyTwoToastsStart("Sending message", "Message sent");
        tapOnSendButton();
        verifyTwoToastsEnd();
    }

    /**
     * Taps on 'Add Connections' button.
     */
    public void tapOnAddConnectionsButton() {
        View view = Id.getViewByViewIdName(ADD_USERS_BUTTON);
        ViewUtils.tapOnView(view, "Add Recipients button");
    }

    /**
     * Opens 'Add Connections' screen.
     * 
     * @return {@code AddConnectionsScreen} with just opened 'Add Connections'
     *         screen.
     */
    public ScreenAddConnections openAddConnectionsScreen() {
        tapOnAddConnectionsButton();
        return new ScreenAddConnections();
    }

    /**
     * Types subject of your message.
     * 
     * @param subject
     *          String contains subject, 
     *          If null or EMPTY_LINE, random text will be use
     * @return subject which was sent
     */
    public String typeSubject(String subject) {
        if (subject == null || subject == "") {
            subject = "Subject " + Math.random();
        }
        Assert.assertNotNull("Subject field is not present.", getSolo().getEditText(1));

        Logger.i("Typing random subject: '" + subject + "'");
        getSolo().enterText(1, subject);

        return subject;
    }

    /**
     * Types message.
     * 
       * @param message
     *          String contains message you want to sent.
     *          If null or EMPTY_LINE, random text will be use
   * @return message which was sent
     */
    public String typeMessage(String message) {
        if (message == null || message == "") {
            message = "Test message " + Math.random();
        }
        Assert.assertNotNull("Message field is not present.", getSolo().getEditText(2));

        Logger.i("Typing random message: '" + message + "'");
        getSolo().enterText(2, message);

        return message;
    }

}
