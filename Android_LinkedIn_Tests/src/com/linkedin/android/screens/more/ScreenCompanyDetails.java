package com.linkedin.android.screens.more;

import junit.framework.Assert;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;

/**
 * Class for CompanyDetails screen.
 * 
 * @author alexander.makarov
 * @created Sep 27, 2012 2:46:14 PM
 */
public class ScreenCompanyDetails extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.jsbridge.CompaniesActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "CompaniesActivity";

    public static final String PAGE_LABEL = "Company";

    private static final ViewIdName WEBVIEW_CONTAINER = new ViewIdName("webview_container");
    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenCompanyDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        verifyCurrentActivity();

        while(getSolo().waitForText("Loading...")); // wait utill content is loaded
        getSolo().waitForText(PAGE_LABEL, 1, DataProvider.WAIT_DELAY_DEFAULT);
        verifyINButton();
        FrameLayout webview = Id.getViewByName(WEBVIEW_CONTAINER, FrameLayout.class);
        Assert.assertNotNull("Web view container is not present", webview);
        
        HardwareActions.takeCurrentActivityScreenshot("Company Details screen");
    }
    
    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Company Details");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
}
