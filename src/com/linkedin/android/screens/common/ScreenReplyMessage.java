package com.linkedin.android.screens.common;

import junit.framework.Assert;

import com.linkedin.android.screens.base.BaseScreenMessage;
import com.linkedin.android.tests.data.DataProvider;

/**
 * Class for 'Reply' screen.
 * 
 * @author alexey.makhalov
 * @created Sep 19, 2012 3:28:23 PM
 */
public class ScreenReplyMessage extends BaseScreenMessage {
    // CONSTANTS ------------------------------------------------------------
    private static final String HEADER = "Reply";

    // METHODS -----------------------------------------------------------
    protected String getHeader() {
        return HEADER;
    }
    
    public void verify() {
        Assert.assertTrue("'Jobs' label is not present",
                getSolo().waitForText(HEADER, 1, DataProvider.WAIT_DELAY_LONG, false, false));
    }
}

