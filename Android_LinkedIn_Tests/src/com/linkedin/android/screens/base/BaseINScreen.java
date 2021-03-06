package com.linkedin.android.screens.base;

import junit.framework.Assert;
import android.widget.EditText;
import android.widget.ImageView;

import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.utils.LayoutUtils;
import com.linkedin.android.utils.Logger;
import com.linkedin.android.utils.Rect2DP;

/**
 * Base class for screens with 'in' button in left top corner.
 * 
 * @author alexander.makarov
 * @created Aug 6, 2012 4:35:35 PM
 */
@SuppressWarnings("rawtypes")
public abstract class BaseINScreen extends BaseScreen {

    // CONSTANTS ------------------------------------------------------------
    static final Rect2DP IN_BUTTON_RECT = new Rect2DP(0.0f, 28.0f, 54.6f, 49.3f);
    static final Rect2DP RIGHT_NAV_BUTTON_RECT = new Rect2DP(265.4f, 28.0f, 54.6f, 49.3f);
    static final float SEARCHBAR_HEIGHT_DP = 48.0f;
    
    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    /**
     * 
     * @param activityClassname
     */
    public BaseINScreen(String activityClassname) {
        super(activityClassname);
    }

    // METHODS --------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.linkedin.android.screens.BaseScreen#verify()
     */
    @Override
    abstract public void verify();

    /**
     * Returns 'IN' button.
     * 
     * @return if exist 'IN' button {@code ImageView}, else <b>null</b>
     */
    public static ImageView getINButton() {
        for (ImageView view : getSolo().getCurrentImageViews()) {
            if (view == null)
                continue;        	
            if (LayoutUtils.isViewPlacedInLayout(view, LayoutUtils.UPPER_LEFT_BUTTON_LAYOUT)) {
                Rect2DP viewRect = new Rect2DP(view);
                if (viewRect.isSizeEqual(IN_BUTTON_RECT.width, IN_BUTTON_RECT.height, 1.0f)) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * Returns button witch placed in right from search bar in navigation bar.
     * 
     * @return if exist right button {@code ImageView}, else <b>null</b>
     */
    public ImageView getRightButtonInNavigationBar() {
        for (ImageView view : getSolo().getCurrentImageViews()) {
            if (LayoutUtils.isViewPlacedInLayout(view, LayoutUtils.UPPER_RIGHT_BUTTON_LAYOUT)) {
                Rect2DP viewRect = new Rect2DP(view);
                if (viewRect.isSizeEqual(RIGHT_NAV_BUTTON_RECT.width, RIGHT_NAV_BUTTON_RECT.height,
                        1.0f)) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * Returns search bar.
     * 
     * @return if exist search bar {@code EditText}, else <b>null</b>
     */
    public EditText getSearchBar() {
        for (EditText view : getSolo().getCurrentEditTexts()) {
            if (LayoutUtils.isViewPlacedInLayout(view, LayoutUtils.SEARCH_BAR_LAYOUT)) {
                Rect2DP viewRect = new Rect2DP(view);
                if (Math.abs(viewRect.height - SEARCHBAR_HEIGHT_DP) < 1.0f) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * Verifies 'IN' button.
     */
    public void verifyINButton() {
        ImageView in = getINButton();
        Assert.assertNotNull("'IN' button is not presented", in);
    }

    /**
     * Verifies button witch placed in right from search bar in navigation bar.
     * 
     * @param buttonName
     *            is name of button for logging
     */
    public void verifyRightButtonInNavigationBar(String buttonName) {
        Assert.assertNotNull("'" + buttonName + "' button is not presented",
                getRightButtonInNavigationBar());
    }
    
    /**
     * Opens 'Expose' screen by tapping on 'IN' button.
     * 
     * @return {@code ScreenExpose} object.
     */
    public ScreenExpose openExposeScreen()
    {
        tapOnINButton();
        return new ScreenExpose(this);
    }

    /**
     * Verifies search bar in head of screen.
     */
    public void verifySearchBar() {
        Assert.assertNotNull("Searchbar is not present", getSearchBar());
    }

    /**
     * Taps on 'IN' button.
     */
    public static void tapOnINButton() {
        ImageView inButton = getINButton();
        Assert.assertNotNull("'IN' button is not presented", inButton);

        Logger.i("Tapping on 'IN' button");
        getSolo().clickOnView(inButton);
    }

    /**
     * Taps on button witch placed in right from search bar in navigation bar.
     * 
     * @param buttonName
     *            is name of button for logging
     */
    public void tapOnRightButtonInNavigationBar(String buttonName) {
        ImageView rightButton = getRightButtonInNavigationBar();
        Assert.assertNotNull("'" + buttonName + "' button is not presented", rightButton);

        Logger.i("Tapping on '" + buttonName + "' button");
        getSolo().clickOnView(rightButton);
    }
    
}
