package com.linkedin.android.screens.more;

import com.linkedin.android.screens.base.BaseDiscussionDetailsScreen;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;

/**
 * Class for "Discussion details from Recent updates" screen.
 * 
 * @author vasily.gancharov
 * @created Sep 19, 2012 14:05:31 PM
 */
public class ScreenDiscussionDetailsFromRecentUpdates extends BaseDiscussionDetailsScreen {
    
    // CONSTANTS ------------------------------------------------------------

    private static final String DETAILS_LABEL = "Details";
    
    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        
        WaitActions.waitForText(DETAILS_LABEL, "'" + DETAILS_LABEL + "' label is not present");
        
        assertLikeButton();
        assertCommentButton();
        
        verifyINButton();
        
        HardwareActions.takeCurrentActivityScreenshot("Discussion detail from Recent updates screen");
    }

}
