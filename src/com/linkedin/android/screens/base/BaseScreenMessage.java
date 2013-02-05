package com.linkedin.android.screens.base;

import junit.framework.Assert;
import android.view.View;
import android.widget.Button;

import com.linkedin.android.screens.common.ScreenAddConnections;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
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
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.messages.MessageComposeActivity";
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
        verifyCurrentActivity();

        Button sendButton = getSolo().getButton(0);
        Assert.assertNotNull("'Send' button is not presented", sendButton);
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

}
