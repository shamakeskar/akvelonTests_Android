package com.linkedin.android.screens.base;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.widget.ProgressBar;

import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Base class for screens with list content.
 * 
 * @author evgeny.agapov
 * @created Jan 10, 2013 6:05:35 PM
 */
public abstract class BaseListScreen extends BaseINScreen {
    public static final String MENU_ITEM_REFRESH = "Refresh";

    public BaseListScreen(String activityClassname) {
        super(activityClassname);
    }

    /**
     * Refreshes screen.
     */
    public void refreshScreen() {
        Logger.i("Refresh screen");
        
        HardwareActions.pressMenu();

        HardwareActions.tapOnMenuOption(MENU_ITEM_REFRESH);
        // Verify screen.
        try {
            this.getClass().newInstance();
        } catch (IllegalAccessException e) {
            Assert.fail("Couldn't create new instance of class " + this.getClass());
        } catch (InstantiationException e) {
            Assert.fail("Couldn't create new instance of class " + this.getClass());
        }
    }

    /**
     * Verifies spinner appeared and then disappeared.
     */
    public void verifyLoadMoreSpinner() {
        // APP BUG: spinner doesn't present sometimes.
        // Check that you see spinner.
        Assert.assertTrue("Spinner is not present.",
                getSolo().waitForView(ProgressBar.class, 1, DataProvider.WAIT_DELAY_SHORT, false));
        // Wait when the spinner will disappear.
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_LONG,
                "Spinner didn't disapear.", new Callable<Boolean>() {
                    public Boolean call() {
                        return !ViewUtils.isListContainVisibleViews(getSolo().getCurrentProgressBars().toArray());
                    }
                });
    }

    /**
     * Scrolls down for load more.
     */
    public void scrollDownForLoadMore() {
        Logger.i("Scroll down for load more data");
        getSolo().scrollToBottom();

        verifyLoadMoreSpinner();
    }
}
