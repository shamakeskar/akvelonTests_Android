package com.linkedin.android.screens.common;

import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;
import com.linkedin.android.tests.utils.TestAction;
import com.linkedin.android.tests.utils.TestUtils;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.ScreenResolution;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ViewAssertUtils;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
import com.linkedin.android.utils.viewUtils.TextViewUtils;
import com.linkedin.android.utils.viewUtils.ViewUtils;

/**
 * Class for 'Add Connections' screen.
 * 
 * @author dmitry.somov
 * @created Aug 22, 2012 6:22:40 PM
 */
@SuppressWarnings("rawtypes")
public class ScreenAddConnections extends BaseScreen {
    // CONSTANTS ------------------------------------------------------------
    public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.redesign.connections.ConnectionListActivity";
    public static final String ACTIVITY_SHORT_CLASSNAME = "ConnectionListActivity";

    public static final String CONNECTIONS_LABEL = "Connections";
    public static final String DONE_BUTTON_LABEL = "Done";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public static final Rect2DP DONE_BUTTON_LAYOUT = new Rect2DP(5,
            LayoutUtils.screenHeight - 150.0f, 153.0f, 151.0f);
    public static final Rect2DP CANCEL_BUTTON_LAYOUT = new Rect2DP(153.0f,
            LayoutUtils.screenHeight - 150.0f, 161.7f, 151.0f);

    public static final float X_COORDINATE_SCROLL = ScreenResolution.getScreenWidth() - 5;
    public static final float Y_COORDIANTE_SCROLL_START = 84 * ScreenResolution.getScreenDensity();
    public static final float Y_COORDINATE_SCROLL_END = ScreenResolution.getScreenHeight();

    public static ViewIdName ID_NAME_OF_CHECKBOX = new ViewIdName("check_box");
    public static ViewIdName ID_NAME_OF_CANCEL_BUTTON = new ViewIdName("cancel_multi_select_button");
    public static ViewIdName ID_NAME_OF_DONE_BUTTON = new ViewIdName("done_multi_select_button");
    public static ViewIdName ID_NAME_OF_FIRST_SHOW_USER = new ViewIdName("display_name");
    public static ViewIdName ID_NAME_ADD_USERS = new ViewIdName("add_users_button");

    // CONSTRUCTORS ---------------------------------------------------------
    public ScreenAddConnections() {
        super(ACTIVITY_CLASSNAME);
    }

    // PROPERTIES -----------------------------------------------------------

    // METHODS --------------------------------------------------------------{
    @Override
    public void verify() {
        verifyCurrentActivity();
    }

    @Override
    public void waitForMe() {
        WaitActions.waitSingleActivity(ACTIVITY_SHORT_CLASSNAME, "Add Connections");
    }

    @Override
    public String getActivityShortClassName() {
        return ACTIVITY_SHORT_CLASSNAME;
    }

    /**
     * Chooses connection in connections list. If <b>connectionsName</b> is null
     * chooses first connection in connections list.
     * 
     * @param connectionName
     *            is connection name which need to choose
     */
    public void chooseConnection(String connectionName) {
        if (connectionName == null) {
            connectionName = getSolo().getText(1).getText().toString();
        }

        Assert.assertTrue("Connection is not present: " + connectionName,
                getSolo().searchText(connectionName));

        // Get checkbox layout.
        View checkbox = null;
        View connectionLayout = (RelativeLayout) TextViewUtils.getTextViewByPartialText(
                connectionName).getParent();
        for (View view : getSolo().getViews(connectionLayout))
            if (view instanceof CheckedTextView) {
                checkbox = view;
                break;
            }

        Assert.assertNotNull("Checkbox for '" + connectionName + "' is not present", checkbox);

        Logger.i("Choosing '" + connectionName + "' in connections list");
        getSolo().clickOnView(checkbox);
    }

    /**
     * Taps on 'Done' button.
     */
    public void tapOnDoneButton() {
        Button done = (Button) Id.getViewByViewIdName(ID_NAME_OF_DONE_BUTTON);
        ViewUtils.tapOnView(done, "Done");
    }

    /**
     * Taps on 'Cancel' button.
     */
    public void tapOnCancelButton() {
        Button button = getCancelButton();
        ViewUtils.tapOnView(button, "'Cancel' button");
    }

    /**
     * Gets 'Done' button
     * 
     * @return 'Done' {@code Button}
     */
    public Button getDoneButton() {
        return getSolo().getButton(DONE_BUTTON_LABEL);
    }

    /**
     * Gets 'Cancel' button
     * 
     * @return 'Cancel' {@code Button}
     */
    public Button getCancelButton() {
        return getSolo().getButton(CANCEL_BUTTON_LABEL);
    }

    /**
     * Gets 'Connections' {@code ListView}
     * 
     * @return 'Connections' {@code ListView}
     */
    public ListView getConnectionsList() {
        return ListViewUtils.getFirstListView();
    }

    /**
     * Verifies 'Connections' list
     */
    public void assertConnectionsList() {
        ListView connectionsList = getConnectionsList();
        Assert.assertNotNull("'Connections' list is not present", connectionsList);
        Assert.assertTrue("'Connections' list width does not equal to screen width",
                ListViewUtils.isListViewWidthEqualToScreenWidth(connectionsList));
        Assert.assertTrue("'Connections' list is empty",
                ListViewUtils.isListViewNotEmpty(connectionsList));
    }

    /**
     * Verify 'Connections' label is present
     */
    @SuppressWarnings("unused")
    private void assertConnectionsLabelPresent() {
        ViewAssertUtils.assertLabel(CONNECTIONS_LABEL);
    }

    @TestAction(value = "go_to_select_recipients")
    public static void go_to_select_recipients(String email, String password) {
        ScreenNewMessage.go_to_message_compose(email, password);
        select_recipients("go_to_select_recipients");
    }

    public static void select_recipients(String screenshotName) {
        ImageView recipients = (ImageView) Id.getViewByViewIdName(ID_NAME_ADD_USERS);
        ViewUtils.tapOnView(recipients, "Add recipients");
        new ScreenAddConnections();
        TestUtils.delayAndCaptureScreenshot(screenshotName);
    }

    @TestAction(value = "select_recipients")
    public static void select_recipients() {
        select_recipients("select_recipients");
    }

    @TestAction(value = "select_recipients_tap_scrollbar")
    public static void select_recipients_tap_scrollbar() {
        TextView firstName = (TextView) Id.getViewByViewIdName(ID_NAME_OF_FIRST_SHOW_USER);

        TestUtils.tapScrollBar();

        TextView secondName = (TextView) Id.getViewByViewIdName(ID_NAME_OF_FIRST_SHOW_USER);

        // Compare that first show user is changed (screen is scrolled).
        Assert.assertTrue("Scroll is not completed", !secondName.getText().toString()
                .equalsIgnoreCase(firstName.getText().toString()));
        TestUtils.delayAndCaptureScreenshot("select_recipients_tap_scrollbar");
    }

    @TestAction(value = "select_recipients_tap_select")
    public static void select_recipients_tap_select(final String recipient) {
        TextView connection = TextViewUtils.getTextViewByText(recipient);
        RelativeLayout layout = (RelativeLayout) connection.getParent();
        CheckedTextView check = (CheckedTextView) layout.getChildAt(3);
        Assert.assertNotNull("Checkbox is not present", check);
        final boolean isChecked = check.isChecked();
        ViewUtils.tapOnView(check, "check box");
        WaitActions.waitForTrueInFunction(DataProvider.WAIT_DELAY_DEFAULT,
                "CheckBox is not checked", new Callable<Boolean>() {
                    public Boolean call() {
                        TextView connection = TextViewUtils.getTextViewByText(recipient);
                        RelativeLayout layout = (RelativeLayout) connection.getParent();
                        CheckedTextView check = (CheckedTextView) layout.getChildAt(3);
                        if (check == null) {
                            return false;
                        } else {
                            boolean checked = check.isChecked();
                            return (checked != isChecked);
                        }
                    }
                });

        TestUtils.delayAndCaptureScreenshot("select_recipients_tap_select");
    }

    @TestAction(value = "select_recipients_tap_cancel")
    public static void select_recipients_tap_cancel() {
        Button cancel = (Button) Id.getViewByViewIdName(ID_NAME_OF_CANCEL_BUTTON);
        ViewUtils.tapOnView(cancel, "Cancel");
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("select_recipients_tap_cancel");
    }

    @TestAction(value = "select_recipients_tap_done")
    public static void select_recipients_tap_done() {
        Button done = (Button) Id.getViewByViewIdName(ID_NAME_OF_DONE_BUTTON);
        ViewUtils.tapOnView(done, "Done");
        new ScreenNewMessage();
        TestUtils.delayAndCaptureScreenshot("select_recipients_tap_done");
    }
}
