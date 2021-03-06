package com.linkedin.android.screens.common;

import junit.framework.Assert;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkedin.android.screens.base.BaseScreen;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;
import com.linkedin.android.utils.WaitActions;
import com.linkedin.android.utils.asserts.ScreenAssertUtils;
import com.linkedin.android.utils.asserts.ViewAssertUtils;
import com.linkedin.android.utils.viewUtils.ListViewUtils;
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
	public static final String ACTIVITY_CLASSNAME = "com.linkedin.android.connections.ConnectionListActivity";
	public static final String ACTIVITY_SHORT_CLASSNAME = "ConnectionListActivity";

	public static final String CONNECTIONS_LABEL = "Connections";
	public static final String DONE_BUTTON_LABEL = "Done";
	public static final String CANCEL_BUTTON_LABEL = "Cancel";

	public static final Rect2DP DONE_BUTTON_LAYOUT = new Rect2DP(5,
			LayoutUtils.screenHeight - 150.0f, 153.0f, 151.0f);
	public static final Rect2DP CANCEL_BUTTON_LAYOUT = new Rect2DP(153.0f,
			LayoutUtils.screenHeight - 150.0f, 161.7f, 151.0f);

	// CONSTRUCTORS ---------------------------------------------------------
	public ScreenAddConnections() {
		super(ACTIVITY_CLASSNAME);
	}

	// PROPERTIES -----------------------------------------------------------

	// METHODS --------------------------------------------------------------{
	@Override
	public void verify() {
		ScreenAssertUtils.assertValidActivity(ACTIVITY_SHORT_CLASSNAME);

		while (getSolo().waitForText("Loading..."))
			; // wait utill content is loaded
		while (getSolo().waitForText("Loading..."))
			; // we should wait twice

		getSolo().waitForText(CONNECTIONS_LABEL, 1, DataProvider.WAIT_DELAY_DEFAULT);
		assertConnectionsLabelPresent();

		Button doneButton = getDoneButton();
		ViewAssertUtils.assertViewIsPlacedInLayout("'Done' button is not present", doneButton,
				DONE_BUTTON_LAYOUT);

		Button cancelButton = getCancelButton();
		ViewAssertUtils.assertViewIsPlacedInLayout("'Cancel' button is not present", cancelButton,
				CANCEL_BUTTON_LAYOUT);

		assertConnectionsList();

		HardwareActions.takeCurrentActivityScreenshot("'Add Connections' screen");
	}

	@Override
	public void waitForMe() {
		WaitActions.waitMultiplyActivities(new String[] { ACTIVITY_SHORT_CLASSNAME });
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

		TextView connection = getSolo().getText(connectionName, true);

		Assert.assertTrue("Connection is not present: " + connectionName,
				(connection.getParent() instanceof RelativeLayout));

		// Get checkbox layout.
		View checkbox = null;
		View connectionLayout = (RelativeLayout) connection.getParent();
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
		Button button = getDoneButton();
		ViewUtils.tapOnView(button, "'Done' button");
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
	private void assertConnectionsLabelPresent() {
		ViewAssertUtils.assertLabel(CONNECTIONS_LABEL);
	}
}
