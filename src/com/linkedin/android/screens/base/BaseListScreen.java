package com.linkedin.android.screens.base;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.database.DataSetObserver;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
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
     * Class for check list content changed state.
     */
    class listContentChangedObserver extends DataSetObserver {
        private boolean isContentChanged = false;

        /**
         * Sets {@code isContentChanged} to <b>false</b>.
         */
        public void resetState() {
            isContentChanged = false;
        }

        /**
         * Returns {@code isContentChanged} value.
         */
        public boolean getState() {
            return isContentChanged;
        }

        @Override
        public void onChanged() {
            isContentChanged = true;
        }
    }

    /**
     * Waits for any {@code ListView} appears.
     * 
     * @return first {@code ListView}.
     */
    protected ListView waitForListView() {
        WaitActions.waitForTrueInFunction("Current screen does not contain ListView",
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (ListViewUtils.getFirstListView() != null);
                    }
                });

        return ListViewUtils.getFirstListView();
    }

    /**
     * Refreshes screen.
     */
    public void refreshScreen() {
        HardwareActions.tapOnMenuOption(MENU_ITEM_REFRESH);

        // Get adapter for listView and bind observer to it.
        ListAdapter adapter = waitForListView().getAdapter();
        final listContentChangedObserver observer = new listContentChangedObserver();
        adapter.registerDataSetObserver(observer);

        try {
            // Wait for first event from observer to determine start of
            // refreshing.
            WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_LONG,
                    "Refreshing not started", new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return observer.getState();
                        }
                    });

            // Wait with some delay for observer stopped raise events.
            boolean isScreenRefreshed = false;
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < DataProvider.WAIT_DELAY_LONG) {
                observer.resetState();

                // Wait with TIME_BETWEEN_REFRESH_LOADS delay for raising event.
                int waitStepsCount = DataProvider.TIME_BETWEEN_REFRESH_LOADS
                        / DataProvider.WAIT_DELAY_STEP;
                for (int i = 0; i < waitStepsCount; i++) {
                    DataProvider.getInstance().getSolo().sleep(DataProvider.WAIT_DELAY_STEP);
                    if (observer.getState()) {
                        break;
                    }
                }

                // If event not raised during TIME_BETWEEN_REFRESH_LOADS delay,
                // then refreshing finished.
                if (!observer.getState()) {
                    isScreenRefreshed = true;
                    break;
                }
            }

            Assert.assertTrue("Refreshing not finished", isScreenRefreshed);
        } catch (Exception e) {
            adapter.unregisterDataSetObserver(observer);

            Assert.fail(e.getMessage());
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
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_LONG, "Spinner didn't disapear.",
                new Callable<Boolean>() {
                    public Boolean call() {
                        return !ViewUtils.isListContainVisibleViews(getSolo()
                                .getCurrentProgressBars().toArray());
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
