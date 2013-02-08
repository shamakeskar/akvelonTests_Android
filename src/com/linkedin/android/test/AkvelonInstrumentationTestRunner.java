package com.linkedin.android.test;

import java.lang.reflect.Method;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

import com.linkedin.android.popups.PopupShareUpdateVisibility;
import com.linkedin.android.popups.PopupSyncContacts;
import com.linkedin.android.screens.common.ScreenAddConnections;
import com.linkedin.android.screens.common.ScreenBrowser;
import com.linkedin.android.screens.common.ScreenCalSplash;
import com.linkedin.android.screens.common.ScreenExpose;
import com.linkedin.android.screens.common.ScreenGroups;
import com.linkedin.android.screens.common.ScreenGroupsYouMightLike;
import com.linkedin.android.screens.common.ScreenLogin;
import com.linkedin.android.screens.common.ScreenNewMessage;
import com.linkedin.android.screens.common.ScreenPYMK;
import com.linkedin.android.screens.common.ScreenSearch;
import com.linkedin.android.screens.common.ScreenUpdatedProfileRollup;
import com.linkedin.android.screens.inbox.ScreenAllMessages;
import com.linkedin.android.screens.inbox.ScreenInbox;
import com.linkedin.android.screens.inbox.ScreenInvitationDetails;
import com.linkedin.android.screens.inbox.ScreenInvitationsAll;
import com.linkedin.android.screens.inbox.ScreenMessageDetail;
import com.linkedin.android.screens.inbox.ScreenNewInvitation;
import com.linkedin.android.screens.more.ScreenAllPopularDiscussion;
import com.linkedin.android.screens.more.ScreenCompanies;
import com.linkedin.android.screens.more.ScreenDiscussionDetails;
import com.linkedin.android.screens.more.ScreenGroupsAndMore;
import com.linkedin.android.screens.more.ScreenGroupsDiscussionList;
import com.linkedin.android.screens.more.ScreenJobs;
import com.linkedin.android.screens.more.ScreenNewDiscussion;
import com.linkedin.android.screens.settings.ScreenAbiLearnMore;
import com.linkedin.android.screens.settings.ScreenReportProblem;
import com.linkedin.android.screens.settings.ScreenSettings;
import com.linkedin.android.screens.settings.ScreenSettingsCalendar;
import com.linkedin.android.screens.updates.ScreenAddComment;
import com.linkedin.android.screens.updates.ScreenAddressBookImport;
import com.linkedin.android.screens.updates.ScreenCalendar;
import com.linkedin.android.screens.updates.ScreenCategories;
import com.linkedin.android.screens.updates.ScreenCommentsList;
import com.linkedin.android.screens.updates.ScreenFeedDetail;
import com.linkedin.android.screens.updates.ScreenLikesList;
import com.linkedin.android.screens.updates.ScreenLinkedInToday;
import com.linkedin.android.screens.updates.ScreenNewConnectionsRollUp;
import com.linkedin.android.screens.updates.ScreenNewJobsRollUp;
import com.linkedin.android.screens.updates.ScreenNewsArticleDetailsFromLinkedInToday;
import com.linkedin.android.screens.updates.ScreenShareUpdate;
import com.linkedin.android.screens.updates.ScreenUpdates;
import com.linkedin.android.screens.you.ScreenEditProfile;
import com.linkedin.android.screens.you.ScreenInCommon;
import com.linkedin.android.screens.you.ScreenProfile;
import com.linkedin.android.screens.you.ScreenProfilePhoto;
import com.linkedin.android.screens.you.ScreenRecentActivity;
import com.linkedin.android.screens.you.ScreenWhosViewedYou;
import com.linkedin.android.screens.you.ScreenYou;
import com.linkedin.android.screens.you.ScreenYouConnections;
import com.linkedin.android.screens.you.ScreenYourProfilePhoto;
import com.linkedin.android.tests.ActionSuiteTest;
import com.linkedin.android.tests.BaseTestCase;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.tests.utils.LoginActions;
import com.linkedin.android.tests.utils.Registry;
import com.linkedin.android.tests.utils.Tag;
import com.linkedin.android.tests.utils.TestDiscover;

/**
 * Custom InstrumentationTestRunner for create list of tags to run.
 * 
 * @author alexander.makarov
 * @created Jan 8, 2013 2:51:59 PM
 */
public class AkvelonInstrumentationTestRunner extends InstrumentationTestRunner {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------
    private final static Registry registry = new Registry();
    static {
        registry.register(LoginActions.class);
        registry.register(ScreenLogin.class);
        registry.register(PopupSyncContacts.class);
        registry.register(ScreenCalSplash.class);
        registry.register(ScreenExpose.class);
        registry.register(ScreenGroupsAndMore.class);
        registry.register(ScreenGroups.class);
        registry.register(ScreenPYMK.class);
        registry.register(ScreenJobs.class);
        registry.register(ScreenSettings.class);
        registry.register(ScreenSearch.class);
        registry.register(ScreenUpdates.class);
        registry.register(ScreenGroupsYouMightLike.class);
        registry.register(ScreenAllPopularDiscussion.class);
        registry.register(ScreenLinkedInToday.class);
        registry.register(ScreenYouConnections.class);
        registry.register(ScreenGroupsDiscussionList.class);
        registry.register(ScreenShareUpdate.class);
        registry.register(ScreenNewDiscussion.class);
        registry.register(PopupShareUpdateVisibility.class);
        registry.register(ScreenReportProblem.class);
        registry.register(ScreenCategories.class);
        registry.register(ScreenUpdatedProfileRollup.class);
        registry.register(ScreenWhosViewedYou.class);
        registry.register(ScreenYourProfilePhoto.class);
        registry.register(ScreenEditProfile.class);
        registry.register(ScreenCalendar.class);
        registry.register(ScreenCompanies.class);
        registry.register(ScreenNewConnectionsRollUp.class);
        registry.register(ScreenRecentActivity.class);
        registry.register(ScreenAllMessages.class);
        registry.register(ScreenAddComment.class);
        registry.register(ScreenProfilePhoto.class);
        registry.register(ScreenNewMessage.class);
        registry.register(ScreenNewJobsRollUp.class);
        registry.register(ScreenSettingsCalendar.class);
        registry.register(ScreenInbox.class);
        registry.register(ScreenInCommon.class);
        registry.register(ScreenInvitationsAll.class);
        registry.register(ScreenFeedDetail.class);
        registry.register(ScreenYou.class);
        registry.register(ScreenNewsArticleDetailsFromLinkedInToday.class);
        registry.register(ScreenAbiLearnMore.class);
        registry.register(ScreenAddressBookImport.class);
        registry.register(ScreenAddConnections.class);
        registry.register(ScreenCommentsList.class);
        registry.register(ScreenInvitationDetails.class);
        registry.register(ScreenLikesList.class);
        registry.register(ScreenBrowser.class);
        registry.register(ScreenMessageDetail.class);
        registry.register(ScreenNewInvitation.class);
        registry.register(ScreenProfile.class);
        registry.register(ScreenDiscussionDetails.class);
    }

    // Variables for run custom classes or methods.
    private Class<? extends BaseTestCase> customClass = null;
    // in sample = TmpRegressions.class;
    private String customMethodName = "sanity_part2";
    // in sample = "test_regression_news";

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Get tags from file and returns TestSuite with it for run in JUnit
     * (Robotium).
     */
    @Override
    public TestSuite getAllTests() {
        DataProvider.getInstance().setContext(getContext());
        DataProvider.getInstance().setRegistry(registry);

        // Run custom class or method if specified.
        if (customMethodName != null && customMethodName.length() > 0 && customClass != null) {
            InstrumentationTestSuite suiteTmp = new InstrumentationTestSuite(this);
            Method method = null;
            try {
                method = customClass.getMethod(customMethodName);
            } catch (Exception e) {
                Assert.fail("Cannot find method '" + customMethodName + "' in class '"
                        + customClass.getSimpleName() + "'");
            }
            suiteTmp.addTest(new ActionSuiteTest(customClass, method));
            return suiteTmp;
        } else if (customClass != null) {
            InstrumentationTestSuite suiteTmp = new InstrumentationTestSuite(this);
            suiteTmp.addTestSuite(customClass);
            return suiteTmp;
        }

        // Create and save list of tags to run.
        String pathToXml = null;
        try {
            ArrayList<Tag> tags = TestDiscover.getTagsFromXml(pathToXml);
            DataProvider.getInstance().setTags(tags);
        } catch (Throwable e) {
            Assert.fail("Cannot get tags from XML: " + e.getMessage());
        }

        // Create new test suite.
        // TODO implement logic for create few test suites.
        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
        for (Tag tag : DataProvider.getInstance().getTags()) {
            suite.addTest(new ActionSuiteTest(tag));
        }

        // Set start tag index.
        DataProvider.getInstance().setTagsCounter(0);

        return suite;
    }
}