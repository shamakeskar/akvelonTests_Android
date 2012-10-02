package com.linkedin.android.tests;

import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.http.message.BasicNameValuePair;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.fixtures.ServerRequestResponseUtils;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.utils.HardwareActions;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.StringUtils;

/**
 * Base test case. Abstract.
 * 
 * @author alexander.makarov
 * @created Aug 2, 2012 1:16:59 PM
 */
public abstract class BaseTestCase extends ActivityInstrumentationTestCase2 {
    // CONSTANTS ------------------------------------------------------------
    public static final String START_TEST = "Start test ";
    public static final String DONE = "Done: ";
    public static final String PASS = "Pass: ";
    public static final String END_TEST = "Test over.-------------------------------------------------------------------";
    private static final String START_FIXTURES_FOR_TEST_REMOTE_METHOD_NAME = "startFixturesForTest";
    private static final String STOP_FIXTURES_FOR_CURRENT_TEST_REMOTE_METHOD_NAME = "stopFixturesForCurrentTest";

    // PROPERTIES -----------------------------------------------------------
    // Flag with fixtures test server state.
    private static boolean isFixturesStartedForCurrentTest = false;
    // Flag that current test is pass.
    private static boolean isCurrentTestPass;

    // Launcher Activity class.
    private static Class<?> launcherActivityClass;
    static {
        try {
            launcherActivityClass = Class.forName(DataProvider.LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    // TODO variable for disable logout in end of each test.
    protected boolean DISABLE_LOGOUT = false;

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * Constructor for test case class. Need get tag of current test case.
     * 
     * @param currentTestTag
     *            is tag of current test case
     */
    @SuppressWarnings("unchecked")
    public BaseTestCase(String currentTestTag) {
        super(DataProvider.TARGET_PACKAGE_ID, launcherActivityClass);
        DataProvider.getInstance().setCurrentTestTag(currentTestTag);

        // Please make sure that the names of tests begin with the word "test"

    }

    // METHODS --------------------------------------------------------------
    public void setUp() throws Exception {
        // Update Solo object.
        DataProvider.getInstance().setSolo(new Solo(getInstrumentation()));
        getActivity();

        // TODO remove line on release
        Logger.d("setUp");
    }

    @Override
    public void tearDown() throws Exception {
        Logger.i(END_TEST);
        
        if (!isCurrentTestPass) {
            Logger.i("Current activity: " + getSolo().getCurrentActivity().getClass().getCanonicalName());
            Logger.logElements();
            HardwareActions.takeCurrentActivityScreenshot("Screenshot for fail in " + DataProvider.getInstance().getCurrentTestId());
        }

        // Logout in end of test.
        if (!DISABLE_LOGOUT) {
            LoginActions.logout();
            DISABLE_LOGOUT = false;// It will work only for 1 test.
        }

        // Stop fixtures for current test
        // Important! Make it after logout to be in loggedOut state.
        if (isFixturesStartedForCurrentTest) {
            stopFixturesForCurrentTest();
            isFixturesStartedForCurrentTest = false;
        }

        // Robotium will finish all the activities that have been opened.
        DataProvider.getInstance().getSolo().finishOpenedActivities();

        // TODO remove on release
        Logger.d("tearDown");
    }

    /**
     * Returns {@code Solo} object
     * 
     * @return {@code Solo} object
     */
    protected Solo getSolo() {
        return DataProvider.getInstance().getSolo();
    }

    /**
     * Starts fixtures for test with specified {@code testNumber} and verifies
     * are they started correctly
     * 
     * @param fixtureName
     *            name fixture to run
     */
    protected void startFixture(String fixtureName) {
        final String testNumberParamName = "testNumber";

        ArrayList<BasicNameValuePair> paramsToPassToServer = new ArrayList<BasicNameValuePair>();
        paramsToPassToServer.add(new BasicNameValuePair(testNumberParamName, fixtureName));
        String responseFromServer = ServerRequestResponseUtils.sendRequestToDefaultServer(
                START_FIXTURES_FOR_TEST_REMOTE_METHOD_NAME, paramsToPassToServer);

        // Verify VCR is currently running
        boolean isNoResponseFromServer = StringUtils.isNullOrEmpty(responseFromServer);
        Assert.assertFalse("VCR does not started. Please run 'run_fixtures.bat' to start it.",
                isNoResponseFromServer);

        // Verify Fixtures started correctly
        boolean isFixturesStarted = ServerRequestResponseUtils.OPERATION_COMPLETE_SUCCESSFULLY_MESSAGE
                .equals(responseFromServer);
        Assert.assertTrue("During start of fixtures for test: " + fixtureName
                + " following exception occured: " + responseFromServer, isFixturesStarted);
        isFixturesStartedForCurrentTest = true;
    }

    /**
     * Stops fixtures for current test and verifies are they stopped correctly
     */
    protected void stopFixturesForCurrentTest() {
        String responseFromServer = ServerRequestResponseUtils.sendRequestToDefaultServer(
                STOP_FIXTURES_FOR_CURRENT_TEST_REMOTE_METHOD_NAME, null);
        boolean isFixturesStopped = ServerRequestResponseUtils.OPERATION_COMPLETE_SUCCESSFULLY_MESSAGE
                .equals(responseFromServer);
        Assert.assertTrue(
                "Following exception occured during stop of fixtures for current test (may be empty string): "
                        + responseFromServer, isFixturesStopped);
    }

    /**
     * Must be called at start of each test.
     * Saves <b>testId</b> and <b>testName</b> for caller.
     * Logs string like "Start test <i>testName</i>: '<i>testName</i>'"
     * 
     * @param testId - id of test like "XXXXXXXX" (from PT)
     * @param testName - name of test
     */
    protected void startTest(String testId, String testName){
        Assert.assertTrue("Wrong test ID", testId.length() == 8);
        Logger.i(START_TEST + testId + ": '" + testName + "'");
        DataProvider.getInstance().setCurrentTestIdAndName(testId, testName);
        isCurrentTestPass = false;
    }

    /**
     * Must be called if test is pass.
     * Logs string like "Pass: '<i>testId</i>'"
     */
    protected void passTest(){
        Logger.i(PASS + DataProvider.getInstance().getCurrentTestId());
        isCurrentTestPass = true;
    }
}
