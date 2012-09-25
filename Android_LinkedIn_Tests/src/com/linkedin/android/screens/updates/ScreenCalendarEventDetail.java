package com.linkedin.android.screens.updates;

import junit.framework.Assert;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.screens.you.ScreenProfileOfConnectedUser;
import com.linkedin.android.screens.you.ScreenProfileOfNotConnectedUser;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;

/**
 * Class for EVENT DETAIL screen.
 * 
 * @author alexey.makhalov
 * @created Aug 30, 2012 1:40 PM
 */
public class ScreenCalendarEventDetail extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.spotlight.EventDetailListActivity";

    public static final String ACTIVITY_SHORT_CLASSNAME = "EventDetailListActivity";
        
    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenCalendarEventDetail() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    
    public void verify() {
        // TODO add more conditions?
        Assert.assertTrue("'EVENT DETAIL' screen is not present",
                getSolo().waitForText("Event Detail", 1, DataProvider.WAIT_DELAY_SHORT, false, false));

        HardwareActions.takeCurrentActivityScreenshot("EVENT DETAIL screen");
    };

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    @Override
    public void waitForMe() {
        WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
    }    
    
    /**
     * tap on "View All Attendees" button.
     */
    public void tapOnViewAllAttendees() {
        HardwareActions.scrollDown();
        TextView article = getSolo().getText("View All Attendees");
        
        Assert.assertNotNull("'View All Attendees' button is not present on 'EVENT DETAIL' screen", article);

        Logger.i("Tapping on 'View All Attendees' button");
        getSolo().clickOnView(article);
        
        Assert.assertTrue("'All Attendees' screen is not present",
                getSolo().waitForText("All Attendees", 1, DataProvider.WAIT_DELAY_SHORT, false, false));
    }
    
    public ScreenProfileOfNotConnectedUser findAnyNotConnectedUserAndOpenScreen() {
        HardwareActions.scrollDown();
    	ListView list = (ListView)getSolo().getView(ListView.class, 0);
        Assert.assertNotNull("List of All Attendees is not present on 'EVENT DETAIL' screen", list);
        int i = list.getChildCount();
        while (--i != 0) {
            View child = list.getChildAt(i);
            if (child == null) continue;
            getSolo().clickOnView(child);
            
            // Skip invite examples
            if (getSolo().waitForText("I'd like to add you to my", 1, DataProvider.WAIT_DELAY_SHORT, false, false)) {
            	do {
            		HardwareActions.pressBack();
            	} while (!getSolo().waitForText("All Attendees", 1, DataProvider.WAIT_DELAY_STEP, false, false));
            	continue;
            }
            // TODO: Add some checking here if there is no 2nd degree or further profiles
            return new ScreenProfileOfNotConnectedUser();
        }
		return null;
	}


    public ScreenProfileOfConnectedUser findFirstConnectedUserAndOpenScreen() {
        HardwareActions.scrollUp();
    	ListView list = (ListView)getSolo().getView(ListView.class, 0);
        Assert.assertNotNull("List of All Attendees is not present on 'EVENT DETAIL' screen", list);
        int n = list.getChildCount();
        for (int i = 1; i < n; i++) {
            View child = list.getChildAt(i);
            if (child == null) continue;
            getSolo().clickOnView(child);
            
            // If we meet invite examples, there is no first degree connection
            if (getSolo().waitForText("I'd like to add you to my", 1, DataProvider.WAIT_DELAY_SHORT, false, false)) {
            	do {
            		HardwareActions.pressBack();
            	} while (!getSolo().waitForText("All Attendees", 1, DataProvider.WAIT_DELAY_STEP, false, false));
            	return null;
            }

            // If we meet 2nd degree or further profiles, there is no first degree connection
            if (getSolo().waitForText("Invite to Connect", 1, DataProvider.WAIT_DELAY_SHORT, false, false)) {
            	do {
            		HardwareActions.pressBack();
            	} while (!getSolo().waitForText("All Attendees", 1, DataProvider.WAIT_DELAY_STEP, false, false));
            	return null;
            }
            
            // TODO: Add some checking here
            return new ScreenProfileOfConnectedUser();
        }
		return null;
	}

}
