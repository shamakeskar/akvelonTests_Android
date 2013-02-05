/**
 * 
 */
package com.linkedin.android.screens.common;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.Button;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.WaitActions;

/**
 * @author kate.dzhgundzhgiya
 * 
 */
@SuppressWarnings("rawtypes")
public class ScreenIntermediateLogin extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.authenticator.v2.AuthenticatorActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "AuthenticatorActivity";

    public static final ViewIdName SIGN_IN_BUTTON = new ViewIdName("login_button");

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenIntermediateLogin() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    public void verify() {
        verifyCurrentActivity();
        // Wait until getSolo().getViews() starts return views, not exception.
        WaitActions.waitForTrueInFunction(
                "Seems like application closed and Robotium cannot show views.",
                new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        ArrayList<View> views;
                        try {
                            views = getSolo().getViews();
                        } catch (Exception ignored) {
                            return false;
                        }
                        return views != null;
                    }
                });
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "'Login' screen is not present", new Callable<Boolean>() {
                    public Boolean call() {
                        Button signInButton = (Button) Id.getViewByViewIdName(SIGN_IN_BUTTON);
                        Assert.assertNotNull("'Sign In' button is not present", signInButton);
                        Rect2DP viewRect = new Rect2DP(signInButton);
                        return signInButton.getVisibility() == View.VISIBLE && 
                                viewRect.x > 0 && viewRect.x < ScreenResolution.getScreenWidthDP();
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "AuthenticatorActivity");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Taps on 'Sign In' button.
     */
    public void tapOnSignInButton() {
    	View view = null;
        try {
            view = getSolo().getText("Sign In");
        } catch (Throwable e) {
            Assert.fail("Cannot find link 'Sign In' to tap.");
        }
        Assert.assertNotNull("Cannot find link 'Sign In' to tap.");
        getSolo().clickOnView(view);
    }
}
