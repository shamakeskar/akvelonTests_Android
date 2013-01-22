package com.linkedin.android.screens.more;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
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

    private static final ViewIdName HEADER_LAYOUT = new ViewIdName("navigation_bar_title");
    //private static final ViewIdName WEBVIEW_CONTAINER = new ViewIdName("webview_container");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenCompanyDetails() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

        WaitActions.waitForTrueInFunction("'Company' screen is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView header = (TextView) Id.getViewByViewIdName(HEADER_LAYOUT);
                        Assert.assertNotNull("Header of Company screen is not present", header);
                        return header.getText().equals("Company");
                    }
                });
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
