/**
 * 
 */
package com.linkedin.android.screens.more;

import java.util.concurrent.Callable;

import android.widget.TextView;

import com.linkedin.android.screens.base.BaseINScreen;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;

/**
 * @author kate.dzhgundzhgiya
 * 
 */
public class ScreenDiscussionCommentList extends BaseINScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.groupsandnews.groups.GroupPostCommentsListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "GroupPostCommentsListActivity";

    private static final ViewIdName TITLE_VIEW = new ViewIdName("navigation_bar_title");
    private static final String TITLE = "Comments";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenDiscussionCommentList() {
        super(ACTIVITY_CLASSNAME);
    }

    // METHODS --------------------------------------------------------------
    @Override
    public void verify() {
        ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);
        WaitActions.waitForTrueInFunction("'Comments' title is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        TextView titleView = (TextView) Id.getViewByViewIdName(TITLE_VIEW);
                        return ((titleView != null) && (titleView.getText().equals(TITLE)));
                    }
                });
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Likes");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }
}
