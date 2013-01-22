package com.linkedin.android.utils.viewUtils;

import java.util.List;

import android.widget.ImageButton;

import com.jayway.android.robotium.solo.Solo;
import com.linkedin.android.tests.data.DataProvider;

/**
 * The class contains util methods for ImageButton.
 * 
 * @author alexander.makarov
 * @created Jan 16, 2013 1:26:02 PM
 */
public class ImageButtonUtils {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Returns {@code ImageButton} by specified index. Not throw exception like solo.getImageButton.
     * 
     * @param imageButtonIndex index of ImageButton
     * @return {@code ImageButton} by <b>imageButtonIndex</b> index
     */
    public static ImageButton getImageButtonByIndex(int imageButtonIndex) {
        Solo solo = DataProvider.getInstance().getSolo();
        List<ImageButton> currrentImageButtons = solo.getCurrentImageButtons();
        if (null == currrentImageButtons || currrentImageButtons.size() <= imageButtonIndex) {
            return null;
        }
        return currrentImageButtons.get(imageButtonIndex);
    }
}
