package com.linkedin.android.utils.asserts;

import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.viewUtils.ViewUtils;

import junit.framework.Assert;
import android.view.View;
import android.widget.TextView;

/**
 * The class contains 'assert' methods for {@code View}s verification.
 * 
 * @author vasily.gancharov
 * @created Aug 21, 2012 15:40:31 PM
 */
public class ViewAssertUtils {

    /**
     * Verifies that specified {@code view} not null and placed in
     * {@code layout}
     * 
     * @param view
     *            view for verification
     * @param viewName
     *            name of the {@code view}
     * @param layout
     *            layout on which {@code view} must be placed
     * 
     */
    public static void assertViewIsPlacedInLayout(String errorMessage, View view, Rect2DP layout) {
        boolean isViewPlacedOnSpecifiedLayout = LayoutUtils.isViewPlacedInLayout(view, layout);
        Assert.assertTrue(errorMessage, isViewPlacedOnSpecifiedLayout);
    }
    /**
     * Verify that label specified is present on current screen.
     * 
     * @param label
     *            label that must be presented on screen
     */
    public static void assertLabel(String label) {
        if (null == label) {
            return;
        }
        TextView recentActivityLabel = ViewUtils.searchTextViewInActivity(label, true);
        Assert.assertNotNull("'" + label + "' label is not present", recentActivityLabel);
    }
}
