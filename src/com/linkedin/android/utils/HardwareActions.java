package com.linkedin.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import junit.framework.Assert;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.data.Id;
import com.linkedin.android.tests.data.ViewIdName;

/**
 * Class for hardware actions. Singleton.
 * 
 * @author alexander.makarov
 * @created Aug 2, 2012 7:21:16 PM
 */
public class HardwareActions {
    // CONSTANTS ------------------------------------------------------------
    // Default directory for save screenshots.
    private static final String SCREENSHOT_DEFAULT_DIR = "0_tmp";

    /** Default screenshot name */
    public static final String NO_NAME = "NoName";
    private static final String TAG = /* Constants.TAG + */":AutomationUtils";
    @SuppressLint("SdCardPath")
    private static final String SCREEN_SHOT_DIR = "/sdcard/layouttest";
    private static final String EXTEN = ".png";
    private static final int QUALITY = 100;

    private static final ViewIdName TOOLBAR_LAYOUT = new ViewIdName("bottom_bar");

    // PROPERTIES -----------------------------------------------------------
    // Instance of HardwareActions.
    private static volatile HardwareActions instance;

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Return instance of singleton.
     * 
     * @return HardwareActions singleton.
     */
    public static HardwareActions getInstance() {
        if (instance == null)
            synchronized (DataProvider.class) {
                if (instance == null)
                    instance = new HardwareActions();
            }
        return instance;
    }

    /**
     * Pressing Back button
     */
    public static void pressBack() {
        Logger.i("Tapping on hardware Back button");
        DataProvider.getInstance().getSolo().goBack();
    }

    /**
     * Pressing Back button until activity doesn't change.
     */
    public static void goBackOnPreviousActivity() {
        String originalActivity = DataProvider.getInstance().getSolo().getCurrentActivity()
                .getClass().getSimpleName();
        String currentActivity = originalActivity;
        while (originalActivity.equals(currentActivity)) {
            Logger.i("Tapping on hardware Back button");
            DataProvider.getInstance().getSolo().goBack();
            WaitActions.waitForScreenUpdate();// To load next activity.
            currentActivity = DataProvider.getInstance().getSolo().getCurrentActivity().getClass()
                    .getSimpleName();
        }
    }

    /**
     * Pressing Menu button
     */
    public static void pressMenu() {
        Logger.i("Tapping on hardware Menu button");
        DataProvider.getInstance().getSolo().sendKey(Solo.MENU);
    }

    /**
     * Pressing on menu option with name buttonName.
     * 
     * @param buttonName
     *            - name option for tap.
     */
    public static void tapOnMenuOption(String optionName) {
        Logger.i("Tapping on menu item with name '" + optionName + "'");
        DataProvider.getInstance().getSolo().clickOnMenuItem(optionName);
    }

    /**
     * Pressing on first menu option.
     * 
     * @param optionName
     *            is name of option (for logging).
     */
    public static void pressFirstMenuOption(String optionName) {
        final Solo solo = DataProvider.getInstance().getSolo();
        WaitActions.waitForTrueInFunction("'" + optionName + "' option in menu is not present",
                new Callable<Boolean>() {
                    public Boolean call() {
                        if (solo.getCurrentViews().size() > 1) {
                            return solo.getCurrentViews().get(1).getClass().getSimpleName()
                                    .toString().equals("IconMenuItemView");
                        }
                        return false;
                    }
                });

        Logger.i("Tapping on '" + optionName + "' item in menu");
        solo.clickOnView(solo.getCurrentViews().get(1));
    }

    /**
     * Pressing Home button
     */
    public static void pressHome() {
        Logger.i("Tapping on hardware Home button");
        DataProvider.getInstance().getSolo().sendKey(KeyEvent.KEYCODE_HOME);
    }

    /**
     * Pressing Search button
     */
    public static void pressSearch() {
        Logger.i("Tapping on hardware Searsh button");
        DataProvider.getInstance().getSolo().sendKey(KeyEvent.KEYCODE_SEARCH);
    }

    /**
     * Take screenshot from {@code View} in default directory.
     * 
     * @param view
     *            is view for take screenshot.
     * @param name
     *            is name for this screenshot.
     */
    public static void takeViewScreenshot(final View view, final String name) {
        final String folder = Environment.getExternalStorageDirectory().getPath() + "/"
                + SCREENSHOT_DEFAULT_DIR;
        Logger.i("Saving screenshot '" + name + "' in '" + SCREENSHOT_DEFAULT_DIR + "'");

        DataProvider.getInstance().getSolo().getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setDrawingCacheEnabled(true);
                try {
                    view.buildDrawingCache();

                    File screenShotDir = new File(folder);
                    if (!screenShotDir.exists()) {
                        Assert.assertTrue("Cannot create folder '" + folder + "'", screenShotDir.mkdirs());
                    }
                    FileOutputStream fos = new FileOutputStream(new File(screenShotDir, name + EXTEN));
                    try {
                        view.buildDrawingCache(true);
                        view.getDrawingCache().compress(CompressFormat.PNG, QUALITY, fos);
                        view.destroyDrawingCache();
                    } finally {
                        fos.close();
                    }
                } catch (Exception e) {
                    Logger.e("Cannot save screenshot.", e);
                }
            }
        });
    }

    /**
     * Take screenshot from current {@code Activity}.
     * 
     * @param name
     *            is name for this screenshot.
     */
    public static void takeCurrentActivityScreenshot(String name) {
        takeViewScreenshot(DataProvider.getInstance().getSolo().getCurrentActivity().getWindow()
                .getDecorView(), (name == null ? DataProvider.getInstance().getSolo()
                .getCurrentActivity().getClass().getSimpleName() : name));
    }

    /**
     * Take screenshot of a {@code View} and store it under a given name.
     * 
     * @param view
     *            View of which to take screenshot.
     * @param name
     *            String name under which to save screenshot.
     */
    public static void takeScreenShot(View view, String name) {
        takeScreenShot(view, name, SCREEN_SHOT_DIR);
    }

    /**
     * Take screenshot of a {@code View} and store it under a given name.
     * 
     * @param view
     *            View of which to take screenshot.
     * @param name
     *            String name under which to save screenshot.
     * @param shotDir
     *            String name of directory in which to save screenshot.
     */
    public static void takeScreenShot(final View view, final String name, final String shotDir) {
        Logger.i("Screenshot: " + shotDir + "/" + name + ".png for View " + view);

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        try {
            File screenShotDir = new File(shotDir);
            if (!screenShotDir.exists()) {
                screenShotDir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new File(screenShotDir, name + EXTEN));
            try {
                view.getDrawingCache().compress(CompressFormat.PNG, QUALITY, fos);
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Screenshot failure: " + e.getLocalizedMessage(), e);
        }
    }

    /**
     * Scrolls up the screen.
     */
    public static void scrollUp() {
        while (DataProvider.getInstance().getSolo().scrollUp()) {
            DataProvider.getInstance().getSolo().drag(0, 0, 60, 5, 1);
        }
    }

    /**
     * Scrolls down the screen.
     */
    public static void scrollDown() {
        while (DataProvider.getInstance().getSolo().scrollDown()) {
            DataProvider.getInstance().getSolo().drag(0, 0, 5, 60, 1);
        }
    }

    /*
     * Scrolls down the screen once on the default pixels count once (not to the
     * bottom of the page).
     */
    public static void scrollDownOnce() {
        if (DataProvider.getInstance().getSolo().scrollDown()) {
            DataProvider
                    .getInstance()
                    .getSolo()
                    .drag(0, 0, 5 / ScreenResolution.getScreenDensity(),
                            60 / ScreenResolution.getScreenDensity(), 1);
        }
    }

    /**
     * Scrolls screen down specified number of times. Stops scrolling if it is
     * not possible.
     * 
     * @param numberOfScrolls
     *            number of scrolls
     */
    public static void scrollDownNTimes(int numberOfScrolls) {
        Solo solo = DataProvider.getInstance().getSolo();
        for (int i = 0; i < numberOfScrolls; i++) {
            if (!solo.scrollDown()) {
                return;
            }
        }
    }

    /**
     * Scrolls screen down specified number of times. Waits {@code delayMs}
     * (milliseconds) after each scroll.
     * 
     * @param numberOfScrolls
     *            number of scrolls
     * @param delayMs
     *            wait time after each scroll (in milliseconds)
     */
    public static void scrollDownNTimes(int numberOfScrolls, int delayMs) {
        Solo solo = DataProvider.getInstance().getSolo();
        for (int i = 0; i < numberOfScrolls; i++) {
            solo.scrollDown();
            solo.sleep(delayMs);
        }
    }

    /**
     * Scrolls screen down until {@code element} became visible.
     * 
     * @param element
     *            - element for scroll to.
     * @param checkShown
     *            - <b>true</b> for end scroll, when property isShown() of
     *            {@code element} became true.
     * @param maxCountOfScrolls
     *            - max count of scrolls.
     */
    public static void scrollDownUntilElementVisible(View element, boolean checkShown,
            int maxCountOfScrolls) {
        View toolbar = Id.getViewByViewIdName(TOOLBAR_LAYOUT);
        int toolbarHeight = ((toolbar != null) && (toolbar.isShown())) ? toolbar.getHeight() : 0;
        for (int i = 0; i < maxCountOfScrolls; i++) {
            int xy[] = new int[2];
            element.getLocationOnScreen(xy);
            if (xy[1] + element.getHeight() < ScreenResolution.getScreenHeight() - toolbarHeight) {
                break;
            }
            if ((checkShown) && (element.isShown())) {
                break;
            }
            DataProvider.getInstance().getSolo().drag(0, 0, 200, 100, 10);
            // Wait for animation finished.
            WaitActions.waitForScreenUpdate();
        }
    }

    /**
     * Scrolls to the bottom of screen specified number of times. Waits
     * {@code delayMs} (milliseconds) each time after achieving of the bottom.
     * 
     * @param numberOfScrolls
     *            number of scrolls to the bottom
     * @param delayMs
     *            wait time after each achieving of the bottom (in milliseconds)
     */
    public static void scrollToBottomNTimes(int numberOfScrolls, int delayMs) {
        Solo solo = DataProvider.getInstance().getSolo();
        for (int i = 0; i < numberOfScrolls; i++) {
            solo.scrollToBottom();
            solo.sleep(delayMs);
        }
    }

    // TODO implement this code from LinkedIn
    // /**
    // * Take screenshot of a {@code View} for a layout test, and store it under
    // a
    // * given name.
    // * example of how to pull out screenshot using adb pull:
    // * <br>
    // * <code>
    // * String[] command = new String[]{"adb", "pull", "/sdcard/",
    // "/foo/bar/"};
    // * Process child = Runtime.getRuntime().exec(command);
    // * </code>
    // *
    // * @param activity Activity under test - {@code final} so UI thread sees
    // it.
    // * @param name String screenshot name - {@code final} so UI thread sees
    // it.
    // * @throws Exception if there is a problem.
    // */
    // public static void takeScreenShotForLayoutTest(final Activity activity,
    // final String name) throws Exception {
    // final String tag = TAG + "_takeScreenShotForLayoutTest()";
    //
    // boolean isTest = LiConfigParser.getInstance(activity).isLayoutTestMode();
    // Log.d(tag, "Layout test? "+ isTest);
    // if (!isTest) { // Do nothing if not layout test
    // return;
    // }
    //
    // final String locale = activity
    // .getSharedPreferences(LINKEDIN_PREFS_NAME, 0)
    // .getString(LOCALE_OVERRIDE, EMPTY_STRING);
    //
    // final String screenShotDir = TextUtils.isEmpty(locale)? SCREEN_SHOT_DIR
    // : SCREEN_SHOT_DIR + separatorChar + locale;
    //
    // final Object sync = new Object();
    // synchronized (sync) {
    // activity.runOnUiThread(new Runnable() {
    // @Override
    // public void run() {
    // synchronized (sync) {
    // try {
    // View rootView = activity.getWindow().getDecorView();
    // takeScreenShot(rootView, name, screenShotDir);
    // } catch (Exception e) {
    // Log.e(tag, e.getLocalizedMessage(), e);
    // } finally {
    // sync.notifyAll();
    // }
    // }
    // }
    // });
    // sync.wait();
    // }
    // }
    /**
     * Returns {@code Bitmap} for current screen.
     * 
     * @return Bitmap image of current screen.
     */
    public static Bitmap saveCurrentScreenScreenshotInBitmap() {
        View view = DataProvider.getInstance().getSolo().getCurrentActivity().getWindow()
                .getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap screenshot = view.getDrawingCache();
        return screenshot;
    }

    /**
     * Waits for a 20 seconds and searches for progress bar on the current page
     * *
     * 
     * @return true if progress bar was appeared
     * */
    public static boolean waitProgressBar() {
        int waitStepsCount = DataProvider.WAIT_DELAY_DEFAULT
                / DataProvider.WAIT_DELAY_FOR_PROGRESS_BAR_STEP;
        for (int i = 0; i < waitStepsCount; i++) {
            if (DataProvider.getInstance().getSolo().getCurrentProgressBars().size() > 0) {
                return true;
            }
            DataProvider.getInstance().getSolo()
                    .sleep(DataProvider.WAIT_DELAY_FOR_PROGRESS_BAR_STEP);
        }
        return false;
    }
    
    /**
     * Scrolls up to top of screen.
     * 
     * @param numberOfScrolls
     *            number of scrolls to the top
     */
    public static void scrollToTop(int numberOfScrolls){
        for(int i = 0; i < numberOfScrolls; i++){
            if(DataProvider.getInstance().getSolo().scrollUp()){
                DataProvider.getInstance().getSolo().scrollToTop();
            } else {
                return;
            }
        }
    }
    
    /**
     * Scrolls up to top of screen.
     */
    public static void scrollToTop(){
        scrollToTop(DataProvider.DEFAULT_SCROLLS_COUNT);
    }
}
