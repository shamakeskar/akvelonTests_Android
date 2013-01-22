package com.linkedin.android.tests;

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
import com.linkedin.android.tests.data.StringData;
import com.linkedin.android.tests.utils.LoginActions;

/**
 * Temporary class for regressions.
 * 
 * @author alexander.makarov
 * @created Dec 25, 2012 7:18:33 PM
 */
public class TmpRegressions extends BaseTestCase {
    // CONSTANTS ------------------------------------------------------------

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------
    public TmpRegressions() {
        super(TmpRegressions.class.getSimpleName());
    }

    // METHODS --------------------------------------------------------------
    public void sanity() {
        startTest("12345678", "sanity");

        ScreenLogin.login();
        ScreenLogin.login_tap_signin_precondition(StringData.test_email_large_connections,
                StringData.test_password_large_connections);
        ScreenLogin.login_tap_signin();
        PopupSyncContacts.abi_tap_not_sync();
        ScreenCalSplash.cal_splash_tap_later();
        ScreenUpdates.updates_tap_expose();
        ScreenExpose.expose_tap_updates();
        ScreenUpdates.updates_tap_expose();
        ScreenExpose.expose_tap_you();
        ScreenYou.you_tap_expose();
        ScreenExpose.expose_tap_inbox();
        ScreenInbox.inbox_tap_expose();
        ScreenExpose.expose_tap_groups_and_more();
        ScreenGroupsAndMore.groups_and_more_tap_pymk();
        ScreenPYMK.pymk_back();
        ScreenGroupsAndMore.groups_and_more_tap_groups();
        ScreenGroups.groups_tap_back();
        ScreenGroupsAndMore.groups_and_more_tap_jobs();
        ScreenJobs.jobs_home_tap_back();
        ScreenGroupsAndMore.groups_and_more_tap_companies();
        LoginActions.logout();// logout
        ScreenLogin.login();
        ScreenLogin.login_tap_signin_precondition(StringData.test_email_large_connections,
                StringData.test_password_large_connections);
        ScreenLogin.login_tap_signin();

        passTest();
    }

    public void test_regression_login() {
        startTest("12345678", "dsfasdfsg sdffs dfs");

        ScreenLogin.go_to_login();
        ScreenLogin.login();
        ScreenLogin.login_error_dialog_tap_ok();
        ScreenLogin.login_tap_signin_precondition(StringData.test_email, StringData.test_password);
        ScreenLogin.login_tap_signin();
        ScreenLogin.login_tap_signin_reset();

        passTest();
    }

    public void test_regression_cal_splash() {
        startTest("42117303", "regression_cal_splash");

        ScreenCalSplash.cal_splash();
        ScreenCalSplash.cal_splash_tap_learn_more();
        ScreenCalSplash.cal_splash_tap_learn_more_reset();
        ScreenCalSplash.cal_splash_tap_later();
        ScreenCalSplash.cal_splash_tap_later_reset();
        ScreenCalSplash.cal_splash_tap_sync();
        ScreenCalSplash.cal_splash_tap_sync_reset();

        passTest();
    }

    public void test_regression_expose() {
        startTest("42117105", "regression_expose");

        ScreenExpose.go_to_expose();
        ScreenExpose.expose_tap_expose();
        ScreenExpose.expose_tap_expose_reset();
        ScreenExpose.expose_tap_settings();
        ScreenExpose.expose_tap_settings_reset();
        ScreenExpose.expose_tap_you();
        ScreenExpose.expose_tap_you_reset();
        ScreenExpose.expose_tap_updates();
        ScreenExpose.expose_tap_updates_reset();
        ScreenExpose.expose_tap_inbox();
        ScreenExpose.expose_tap_inbox_reset();
        ScreenExpose.expose_tap_groups_and_more();
        ScreenExpose.expose_tap_groups_and_more_reset();
        ScreenExpose.expose_tap_search();
        ScreenExpose.expose_tap_search_reset();

        passTest();
    }

    public void test_regression_groups_and_more() {
        startTest("42118115", "regression_groups_and_more");

        ScreenGroupsAndMore.go_to_groups_and_more();
        ScreenGroupsAndMore.groups_and_more_tap_pymk();
        ScreenGroupsAndMore.groups_and_more_tap_pymk_reset();
        ScreenGroupsAndMore.groups_and_more_tap_groups();
        ScreenGroupsAndMore.groups_and_more_tap_groups_reset();
        ScreenGroupsAndMore.groups_and_more_tap_jobs();
        ScreenGroupsAndMore.groups_and_more_tap_jobs_reset();
        ScreenGroupsAndMore.groups_and_more_tap_companies();
        ScreenGroupsAndMore.groups_and_more_tap_companies_reset();
        ScreenGroupsAndMore.groups_and_more_tap_expose();
        ScreenGroupsAndMore.groups_and_more_tap_expose_reset();
        ScreenGroupsAndMore.groups_and_more_tap_search();
        ScreenGroupsAndMore.groups_and_more_tap_search_reset();
        ScreenGroupsAndMore.groups_and_more_tap_share();
        ScreenGroupsAndMore.groups_and_more_tap_share_reset();

        passTest();
    }

    public void test_regression_groups() {
        startTest("42118159", "regression_groups");

        ScreenGroups.go_to_groups();
        ScreenGroups.groups_tap_back();
        ScreenGroups.groups();
        ScreenGroups.groups_tap_expose();
        ScreenGroups.groups_tap_expose_reset();
        ScreenGroups.groups_tap_gyml();
        ScreenGroups.groups_tap_gyml_reset();
        ScreenGroups.groups_tap_group();
        ScreenGroups.groups_tap_group_reset();

        passTest();
    }

    public void test_regression_pymk() {
        startTest("42118141", "regression_groups");

        ScreenPYMK.go_to_pymk();
        ScreenPYMK.pymk_back();
        ScreenPYMK.pymk();
        ScreenPYMK.pymk_tap_expose();
        ScreenPYMK.pymk_tap_expose_reset();
        ScreenPYMK.pymk_tap_profile();
        ScreenPYMK.pymk_tap_profile_reset();
        ScreenPYMK.pymk_pull_refresh();
        ScreenPYMK.pymk_scroll_load_more();

        passTest();
    }

    public void test_regression_jobs_home() {
        startTest("42119273", "regression_jobs_home");

        ScreenJobs.go_to_jobs_home();
        ScreenJobs.jobs_home_tap_expose();
        ScreenJobs.jobs_home_tap_expose_reset();
        ScreenJobs.jobs_home_tap_back();
        ScreenJobs.jobs_home();

        // TODO: implement other actions (webview actions).

        passTest();
    }

    public void test_regression_settings() {
        startTest("42121041", "regression_setting");

        ScreenSettings.go_to_settings();
        ScreenSettings.settings();
        ScreenSettings.settings_tap_sync_calendar();
        ScreenSettings.settings_tap_sync_calendar_reset();
        ScreenSettings.settings_tap_add_con();
        ScreenSettings.settings_tap_add_con_reset();
        ScreenSettings.settings_toggle_push_notifications();
        ScreenSettings.settings_toggle_push_notifications_reset();
        ScreenSettings.settings_dialog_tap_sync_all_contacts();
        ScreenSettings.settings_dialog_tap_sync_existing_contacts();
        ScreenSettings.settings_dialog_tap_sync_cancel();
        ScreenSettings.settings_tap_signout();

        passTest();
    }

    public void test_regression_search() {
        startTest("42121305", "regression_search");

        ScreenSearch.go_to_search();
        ScreenSearch.search_tap_profile();
        ScreenSearch.search_tap_profile_reset();
        ScreenSearch.search_tap_cancel();

        passTest();
    }

    public void test_regression_updates() {
        startTest("42117131", "test_regression_updates");

        ScreenUpdates.go_to_updates();
        ScreenUpdates.updates_tap_news();
        ScreenUpdates.updates_tap_news_reset();
        ScreenUpdates.updates_tap_update();
        ScreenUpdates.updates_tap_update_reset();
        ScreenUpdates.updates_tap_search();
        ScreenUpdates.updates_tap_search_reset();
        ScreenUpdates.updates_tap_share();
        ScreenUpdates.updates_tap_share_reset();
        ScreenUpdates.updates_tap_cal();
        ScreenUpdates.updates_tap_cal_reset();
        ScreenUpdates.updates_pull_refresh();
        ScreenUpdates.updates_scroll_load_more();

        passTest();
    }

    public void test_regression_groups_gyml() {
        startTest("42118173", "regression_groups_gyml");

        ScreenGroupsYouMightLike.go_to_groups_gyml();
        ScreenGroupsYouMightLike.groups_gyml_tap_back();
        ScreenGroupsYouMightLike.groups_gyml();
        ScreenGroupsYouMightLike.groups_gyml_tap_expose();
        ScreenGroupsYouMightLike.groups_gyml_tap_expose_reset();
        ScreenGroupsYouMightLike.groups_gyml_tap_join();
        ScreenGroupsYouMightLike.groups_gyml_tap_group();
        ScreenGroupsYouMightLike.groups_gyml_tap_group_reset();
        ScreenGroupsYouMightLike.groups_gyml_pull_refresh();
        ScreenGroupsYouMightLike.groups_gyml_scroll_load_more();

        passTest();
    }

    public void test_regression_groups_discussion_list_view_popular() {
        startTest("42118697", "regression_groups_discussion_list_view_popular");

        ScreenAllPopularDiscussion.go_to_groups_discussion_list_view_popular();
        ScreenAllPopularDiscussion.groups_discussion_list_view_popular_tap_back();
        ScreenAllPopularDiscussion.groups_discussion_list_view_popular();
        ScreenAllPopularDiscussion.groups_discussion_list_view_popular_tap_expose();
        ScreenAllPopularDiscussion.groups_discussion_list_view_popular_tap_expose_reset();
        ScreenAllPopularDiscussion.groups_discussion_list_view_popular_tap_post_new_discussion();
        ScreenAllPopularDiscussion
                .groups_discussion_list_view_popular_tap_post_new_discussion_reset();
        ScreenAllPopularDiscussion.groups_discussion_list_view_popular_tap_discussion();
        ScreenAllPopularDiscussion.groups_discussion_list_view_popular_tap_discussion_reset();
        ScreenAllPopularDiscussion.groups_discussion_list_view_popular_scroll_load_more();

        passTest();
    }

    public void test_regression_news() {
        startTest("42118021", "regression_news");

        ScreenLinkedInToday.go_to_news();
        ScreenLinkedInToday.news_tap_back();
        ScreenLinkedInToday.news();
        ScreenLinkedInToday.news_tap_article();
        ScreenLinkedInToday.news_tap_article_reset();
        ScreenLinkedInToday.news_tap_expose();
        ScreenLinkedInToday.news_tap_expose_reset();
        ScreenLinkedInToday.news_manage();
        ScreenLinkedInToday.news_manage_reset();
        ScreenLinkedInToday.news_pull_refresh();

        passTest();
    }

    public void test_regression_connections() {
        startTest("42120183", "regression_connections");

        ScreenYouConnections.go_to_connections();
        ScreenYouConnections.connections_tap_expose();
        ScreenYouConnections.connections_tap_expose_reset();
        ScreenYouConnections.connections_tap_back();
        ScreenYouConnections.connections_tap_back_reset();
        ScreenYouConnections.connections_tap_add_con();
        ScreenYouConnections.connections_tap_add_con_reset();
        ScreenYouConnections.connections_tap_search();
        ScreenYouConnections.connections_tap_search_reset();
        ScreenYouConnections.connections_tap_pymk();
        ScreenYouConnections.connections_tap_pymk_reset();
        ScreenYouConnections.connections_tap_profile();
        ScreenYouConnections.connections_tap_profile_reset();
        ScreenYouConnections.connections_pull_refresh();

        // TODO add connections_tap_scrollbar and other

        passTest();
    }

    public void test_regression_groups_discussion_list() {
        startTest("44900922", "regression_groups_discussion_list");

        ScreenGroupsDiscussionList.go_to_groups_discussion_list();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_back();
        ScreenGroupsDiscussionList.groups_discussion_list();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_expose();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_expose_reset();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_post_new_discussion();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_post_new_discussion_reset();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_discussion();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_discussion_reset();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_view_all_popular();
        ScreenGroupsDiscussionList.groups_discussion_list_tap_view_all_popular_reset();
        ScreenGroupsDiscussionList.groups_discussion_list_scroll_load_more();

        passTest();
    }

    public void test_regression_share() {
        startTest("42116659", "regression_share");

        ScreenShareUpdate.go_to_share();
        ScreenShareUpdate.share();
        ScreenShareUpdate.share_tap_visibility();
        ScreenShareUpdate.share_tap_visibility_reset();
        ScreenShareUpdate.share_tap_share_precondition();
        ScreenShareUpdate.share_tap_share();
        ScreenShareUpdate.share_tap_share_reset();
        ScreenShareUpdate.share_tap_cancel();

        passTest();
    }

    public void test_regression_groups_discussion_list_compose() {
        startTest("42118843", "regression_groups_discussion_list_compose");

        ScreenNewDiscussion.go_to_groups_discussion_list_compose();
        ScreenNewDiscussion.groups_discussion_list_compose_tap_cancel();
        ScreenNewDiscussion.groups_discussion_list_compose();
        ScreenNewDiscussion.groups_discussion_list_compose_tap_post();

        passTest();
    }

    public void test_regression_share_visibility() {
        startTest("54237894", "regression_groups_discussion_list_view_popular");

        PopupShareUpdateVisibility.go_to_share_visibility();
        PopupShareUpdateVisibility.share_visibility_tap_back();
        PopupShareUpdateVisibility.share_visibility_tap_back_reset();
        PopupShareUpdateVisibility.share_visibility_tap_connections_only();
        PopupShareUpdateVisibility.share_visibility_tap_connections_only_reset();
        PopupShareUpdateVisibility.share_visibility_tap_anyone();

        passTest();
    }

    public void test_regression_report_problem() {
        startTest("42121071", "regression_report_problem");

        ScreenReportProblem.go_to_report_problem();
        ScreenReportProblem.report_problem_tap_back();
        ScreenReportProblem.report_problem();
        ScreenReportProblem.report_problem_tap_send();

        passTest();
    }

    public void test_regression_news_manage() {
        startTest("42118071", "regression_news_manage");

        ScreenCategories.go_to_news_manage();
        ScreenCategories.news_manage_tap_back();
        ScreenCategories.news_manage();
        ScreenCategories.news_manage_tap_expose();
        ScreenCategories.news_manage_tap_expose_reset();

        passTest();
    }

    public void test_regression_updates_profile_rollup_list() {
        startTest("42117865", "regression_updates_profile_rollup_list");

        ScreenUpdatedProfileRollup.go_to_updates_profile_rollup_list();
        ScreenUpdatedProfileRollup.updates_profile_rollup_list_tap_back();
        ScreenUpdatedProfileRollup.updates_profile_rollup_list();
        ScreenUpdatedProfileRollup.updates_profile_rollup_list_tap_expose();
        ScreenUpdatedProfileRollup.updates_profile_rollup_list_tap_expose_reset();
        ScreenUpdatedProfileRollup.updates_profile_rollup_list_tap_update();
        ScreenUpdatedProfileRollup.updates_profile_rollup_list_tap_update_reset();

        passTest();
    }

    public void test_regression_wvmp() {
        startTest("42120209", "regression_wvmp");

        ScreenWhosViewedYou.go_to_wvmp();
        ScreenWhosViewedYou.wvmp_expose();
        ScreenWhosViewedYou.wvmp_expose_reset();
        ScreenWhosViewedYou.wvmp_back();
        ScreenWhosViewedYou.wvmp();
        ScreenWhosViewedYou.wvmp_tap_profile();
        ScreenWhosViewedYou.wvmp_tap_profile_reset();

        passTest();
    }

    public void test_regression_profile_photo() {
        startTest("42120147", "regression_profile_photo");

        ScreenYourProfilePhoto.go_to_profile_photo();
        ScreenYourProfilePhoto.profile_photo_tap_back();
        ScreenYourProfilePhoto.profile_photo();
        ScreenYourProfilePhoto.profile_photo_tap_edit();
        ScreenYourProfilePhoto.profile_photo_actionsheet_tap_cancel();

        passTest();
    }

    public void test_regression_profile_edit() {
        startTest("42120331", "regression_profile_edit");

        ScreenEditProfile.go_to_profile_edit();
        ScreenEditProfile.profile_edit_tap_done();

        /*
         * disableLogoutAtEndForCurrentTest(); AndroidWebDriver driver =
         * WebViewUtils.getAndroidWebDriver();
         * WebViewUtils.logPageHtmlCode(driver); Logger.d("start find");
         * List<WebElement> elem = null; try { elem =
         * driver.findElements(By.tagName("div")); } catch (Exception e){
         * Logger.e("By.tagName('div')", e); } int i = 0; while (i <
         * elem.size()) { Logger.d(elem.get(i).getText()); i++; }
         * 
         * try { elem = driver.findElements(By.tagName("span")); } catch
         * (Exception e){ Logger.e("By.tagName('span')", e); } i = 0; while (i <
         * elem.size()) { Logger.d(elem.get(i).getText()); i++; }
         */

        // TODO complete all webview actions.

        passTest();
    }

    public void test_regression_cal() {
        startTest("42117519", "regression_cal");

        ScreenCalendar.go_to_cal();
        ScreenCalendar.cal_tap_back();
        ScreenCalendar.cal();
        ScreenCalendar.cal_tap_expose();
        ScreenCalendar.cal_tap_expose_reset();
        ScreenCalendar.cal_scroll_load_next();
        ScreenCalendar.cal_tap_today();
        ScreenCalendar.cal_scroll_load_previous();

        passTest();
    }

    public void test_regression_companies() {
        startTest("42119135", "regression_companies");

        ScreenCompanies.go_to_companies();
        ScreenCompanies.companies_tap_back();
        ScreenCompanies.companies_tap_back_reset();
        ScreenCompanies.companies_tap_expose();
        ScreenCompanies.companies_tap_expose_reset();
        // TODO implement companies_tap_company and companies_tap_recommended
        // ScreenCompanies.companies_tap_company();
        // ScreenCompanies.companies_tap_recommended();
        passTest();
    }

    public void test_regression_updates_connection_rollup_list() {
        startTest("42117879", "regression_updates_connection_rollup_list");

        ScreenNewConnectionsRollUp.go_to_updates_connection_rollup_list();
        ScreenNewConnectionsRollUp.updates_connection_rollup_list_tap_back();
        ScreenNewConnectionsRollUp.updates_connection_rollup_list();
        ScreenNewConnectionsRollUp.updates_connection_rollup_list_tap_update();
        ScreenNewConnectionsRollUp.updates_connection_rollup_list_tap_update_reset();
        ScreenNewConnectionsRollUp.updates_connection_rollup_list_tap_expose();
        ScreenNewConnectionsRollUp.updates_connection_rollup_list_tap_expose_reset();

        passTest();
    }

    public void test_regression_recent_activity() {
        startTest("42121009", "regression_recent_activity");

        ScreenRecentActivity.go_to_recent_activity();
        ScreenRecentActivity.recent_activity_back();
        ScreenRecentActivity.recent_activity();
        ScreenRecentActivity.recent_activity_expose();
        ScreenRecentActivity.recent_activity_expose_reset();
        ScreenRecentActivity.recent_activity_tap_detail();
        ScreenRecentActivity.recent_activity_tap_detail_reset();

        passTest();
        disableFinishActivitiesAtEndForCurrentTest(); // App crash (db already
                                                      // closed).
    }

    public void test_regression_inbox_mail_all() {
        startTest("42119453", "regression_inbox_mail_all");

        ScreenAllMessages.go_to_inbox_mail_all();
        ScreenAllMessages.inbox_mail_all_tap_back();
        ScreenAllMessages.inbox_mail_all();
        ScreenAllMessages.inbox_mail_all_pull_refresh();
        ScreenAllMessages.inbox_mail_all_scroll_load_more();
        ScreenAllMessages.inbox_mail_all_tap_compose_new_message();
        ScreenAllMessages.inbox_mail_all_tap_compose_new_message_reset();
        ScreenAllMessages.inbox_mail_all_tap_message();

        passTest();
    }

    public void test_regression_comment_compose() {
        startTest("42121305", "regression_search");

        ScreenAddComment.go_to_comment_compose();
        ScreenAddComment.comment_compose_tap_cancel();
        ScreenAddComment.comment_compose_tap_cancel_reset();
        ScreenAddComment.comment_compose_tap_send_precondition(null);
        ScreenAddComment.comment_compose_tap_send();

        passTest();
    }

    public void test_regression_profile_photo_from_profile() {
        startTest("42120963", "regression_profile_photo_from_profile");

        ScreenProfilePhoto.go_to_profile_photo_from_profile();
        ScreenProfilePhoto.profile_photo_from_profile_tap_back();

        passTest();
    }

    public void test_regression_message_compose() {
        startTest("42119497", "regression_message_compose");

        ScreenNewMessage.go_to_message_compose();
        ScreenNewMessage.message_compose_tap_cancel();
        ScreenNewMessage.message_compose();
        ScreenNewMessage.message_compose_tap_add_recipients();
        ScreenNewMessage.message_compose_tap_add_recipients_reset();
        ScreenNewMessage.message_compose_tap_send_precondition();
        ScreenNewMessage.message_compose_tap_send();

        passTest();
    }

    public void test_regression_updates_new_jobs_rollup_list() {
        startTest("42117899", "regression_updates_new_jobs_rollup_list");

        ScreenNewJobsRollUp.go_to_updates_new_jobs_rollup_list();
        ScreenNewJobsRollUp.updates_new_jobs_rollup_list_tap_back();
        ScreenNewJobsRollUp.updates_new_jobs_rollup_list();
        ScreenNewJobsRollUp.updates_new_jobs_rollup_list_tap_expose();
        ScreenNewJobsRollUp.updates_new_jobs_rollup_list_tap_expose_reset();
        ScreenNewJobsRollUp.updates_new_jobs_rollup_list_tap_profile();
        ScreenNewJobsRollUp.updates_new_jobs_rollup_list_tap_profile_reset();
        ScreenNewJobsRollUp.updates_new_jobs_rollup_list_tap_message();
        ScreenNewJobsRollUp.updates_new_jobs_rollup_list_tap_message_reset();

        passTest();
    }

    public void test_regression_cal_sync() {
        startTest("42121087", "regression_cal_sync");

        ScreenSettingsCalendar.go_to_cal_sync();
        ScreenSettingsCalendar.cal_sync_tap_back();
        ScreenSettingsCalendar.cal_sync_tap_back_reset();
        ScreenSettingsCalendar.cal_sync_toggle_add_cal();

        passTest();
    }

    public void test_regression_inbox() {
        startTest("42119331", "regression_inbox");

        ScreenInbox.go_to_inbox();
        ScreenInbox.inbox_tap_expose();
        ScreenInbox.inbox_tap_expose_reset();
        ScreenInbox.inbox_tap_search();
        ScreenInbox.inbox_tap_search_reset();
        ScreenInbox.inbox_tap_compose_actionsheet();
        ScreenInbox.inbox_tap_compose_actionsheet_reset();
        ScreenInbox.inbox_tap_compose_actionsheet();
        ScreenInbox.inbox_compose_actionsheet_tap_new_message();
        ScreenInbox.inbox_compose_actionsheet_tap_new_message_reset();
        ScreenInbox.inbox_tap_compose_actionsheet();
        ScreenInbox.inbox_compose_actionsheet_tap_new_invitation();
        ScreenInbox.inbox_compose_actionsheet_tap_new_invitation_reset();
        ScreenInbox.inbox_tap_message();
        ScreenInbox.inbox_tap_message_reset();
        ScreenInbox.inbox_tap_all_mail();
        ScreenInbox.inbox_tap_all_mail_reset();
        ScreenInbox.inbox_tap_all_invitations();
        ScreenInbox.inbox_tap_all_invitations_reset();
        ScreenInbox.inbox_pull_refresh();
        ScreenInbox.inbox_tap_notification();
        ScreenInbox.inbox_tap_notification_reset();
        ScreenInbox.inbox_tap_invitation();
        ScreenInbox.inbox_tap_invitation_reset();
        ScreenInbox.inbox_tap_invitation_accept();
        ScreenInbox.inbox_tap_invitation_decline();

        passTest();
    }

    public void test_regression_incommon() {
        startTest("42120987", "regression_incommon");

        ScreenInCommon.go_to_incommon();
        ScreenInCommon.incommon_tap_back();
        ScreenInCommon.incommon();
        ScreenInCommon.incommon_tap_expose();
        ScreenInCommon.incommon_tap_expose_reset();
        ScreenInCommon.incommon_tap_profile();
        ScreenInCommon.incommon_tap_profile_reset();

        passTest();
    }

    public void test_regression_inbox_invitations_all() {
        startTest("42119381", "regression_inbox_invitations_all");

        ScreenInvitationsAll.go_to_inbox_invitations_all();
        ScreenInvitationsAll.inbox_invitations_all_tap_back();
        ScreenInvitationsAll.inbox_invitations_all();
        ScreenInvitationsAll.inbox_invitations_all_tap_expose();
        ScreenInvitationsAll.inbox_invitations_all_tap_expose_reset();
        ScreenInvitationsAll.inbox_invitations_all_tap_compose_new_invitation();
        ScreenInvitationsAll.inbox_invitations_all_tap_compose_new_invitation_reset();
        ScreenInvitationsAll.inbox_invitations_all_tap_invitation();
        ScreenInvitationsAll.inbox_invitations_all_tap_invitation_reset();
        ScreenInvitationsAll.inbox_invitations_all_pull_refresh();
        ScreenInvitationsAll.inbox_invitations_all_tap_invitation_accept();
        ScreenInvitationsAll.inbox_invitations_all_tap_invitation_decline();
        // TODO: inbox_invitations_all_scroll_load_more need precondition.
        // ScreenInvitationsAll.inbox_invitations_all_scroll_load_more();

        passTest();
    }

    public void test_regression_feed_detail() {
        startTest("42117719", "regression_feed_detail");
        disableLogoutAtEndForCurrentTest();

        ScreenFeedDetail.go_to_feed_detail();
        ScreenFeedDetail.feed_detail_tap_back();
        ScreenFeedDetail.feed_detail_tap_back_reset();
        ScreenFeedDetail.feed_detail_tap_expose();
        ScreenFeedDetail.feed_detail_tap_expose_reset();
        ScreenFeedDetail.feed_detail_tap_profile_author();
        ScreenFeedDetail.feed_detail_tap_profile_author_reset();
        ScreenFeedDetail.feed_detail_tap_like_toggle();
        ScreenFeedDetail.feed_detail_tap_url();
        ScreenFeedDetail.feed_detail_tap_url_reset();
        // ScreenFeedDetail.feed_detail_tap_actionsheet();

        // TODO continue to implement regression.

        passTest();
    }

    public void test_regression_you() {
        startTest("42120083", "regression_you");

        ScreenYou.go_to_you();
        ScreenYou.you_tap_expose();
        ScreenYou.you();
        ScreenYou.you_tap_search();
        ScreenYou.you_tap_search_reset();
        ScreenYou.you_tap_share();
        ScreenYou.you_tap_share_reset();
        ScreenYou.you_tap_photo();
        ScreenYou.you_tap_photo_reset();
        ScreenYou.you_tap_change_photo();
        ScreenYou.you_photo_actonsheet_tap_cancel();
        ScreenYou.you_tap_profile_edit();
        ScreenYou.you_tap_profile_edit_reset();
        ScreenYou.you_tap_fwd();
        ScreenYou.you_fwd_actionsheet_tap_cancel();
        ScreenYou.you_tap_fwd();
        ScreenYou.you_fwd_actionsheet_tap_send();
        ScreenYou.you_fwd_actionsheet_tap_send_reset();
        ScreenYou.you_tap_wvmp();
        ScreenYou.you_tap_wvmp_reset();
        ScreenYou.you_tap_activity();
        ScreenYou.you_tap_activity_reset();
        ScreenYou.you_tap_connections();
        ScreenYou.you_tap_connections_reset();
        ScreenYou.you_tap_groups();
        ScreenYou.you_tap_groups_reset();
        ScreenYou.you_tap_website();
        ScreenYou.you_tap_website_reset();
        ScreenYou.you_tap_twitter();
        ScreenYou.you_tap_twitter_reset();

        passTest();
    }

    public void test_regression_news_detail() {
        startTest("42120963", "regression_news_detail");

        ScreenNewsArticleDetailsFromLinkedInToday.go_to_news_detail();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_back();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_article_up();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_article_down();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_view_article_header();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_view_article_header_reset();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_view_article_image();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_view_article_image_reset();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_message();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_message_reset();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_share();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_share_reset();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_expose();
        ScreenNewsArticleDetailsFromLinkedInToday.news_detail_tap_expose_reset();

        passTest();
    }

    public void test_regression_abi_learn_more() {
        startTest("42120887", "regression_abi_learn_more");

        ScreenAbiLearnMore.go_to_abi_learn_more();

        passTest();
    }

    public void test_regression_address_book_import() {
        startTest("01234567", "regression_address_book_import");

        ScreenAddressBookImport.go_to_address_book_import();
        ScreenAddressBookImport.address_book_import_tap_add_connections();
        ScreenAddressBookImport.address_book_import_tap_learn_more();
        ScreenAddressBookImport.address_book_import_learn_more_reset();
        ScreenAddressBookImport.address_book_import_tap_continue();
        ScreenAddressBookImport.address_book_import_wait_progress_bar();
        ScreenAddressBookImport.address_book_import_add_connections_reset();

        passTest();
    }

    public void test_regression_feed_detail_comments_list() {
        startTest("42117767", "regression_feed_detail_comments_list");

        ScreenCommentsList.go_to_feed_detail_comments_list();
        ScreenCommentsList.feed_detail_comments_list_tap_back();
        ScreenCommentsList.feed_detail_comments_list();
        ScreenCommentsList.feed_detail_comments_list_tap_expose();
        ScreenCommentsList.feed_detail_comments_list_tap_expose_reset();
        ScreenCommentsList.feed_detail_comments_list_tap_profile_author();
        ScreenCommentsList.feed_detail_comments_list_tap_profile_author_reset();
        ScreenCommentsList.feed_detail_comments_list_scroll_load_more();

        passTest();
    }

    public void test_regression_inbox_invitation_detail() {
        startTest("42119429", "regression_inbox_invitation_detail");

        ScreenInvitationDetails.go_to_inbox_invitation_detail();
        ScreenInvitationDetails.inbox_invitation_detail_tap_back();
        ScreenInvitationDetails.inbox_invitation_detail();
        ScreenInvitationDetails.inbox_invitation_detail_tap_invitation_down();
        ScreenInvitationDetails.inbox_invitation_detail_tap_invitation_up();
        ScreenInvitationDetails.inbox_invitation_detail_tap_profile();
        ScreenInvitationDetails.inbox_invitation_detail_tap_profile_reset();
        ScreenInvitationDetails.inbox_invitation_detail_tap_reply();
        ScreenInvitationDetails.inbox_invitation_detail_tap_reply_reset();
        ScreenInvitationDetails.inbox_invitation_detail_tap_expose();
        ScreenInvitationDetails.inbox_invitation_detail_tap_expose_reset();
        ScreenInvitationDetails.inbox_invitation_detail_tap_accept();
        ScreenInvitationDetails.inbox_invitation_detail();
        ScreenInvitationDetails.inbox_invitation_detail_tap_decline();

        passTest();
    }

    public void test_regression_feed_detail_likes_list() {
        startTest("42117757", "regression_feed_detail_likes_list");

        ScreenLikesList.go_to_feed_detail_likes_list();
        ScreenLikesList.feed_detail_likes_list_tap_back();
        ScreenLikesList.feed_detail_likes_list();
        ScreenLikesList.feed_detail_likes_list_tap_expose();
        ScreenLikesList.feed_detail_likes_list_tap_expose_reset();
        ScreenLikesList.feed_detail_likes_list_tap_profile();
        ScreenLikesList.feed_detail_likes_list_tap_profile_reset();
        ScreenLikesList.feed_detail_likes_list_scroll_load_more();

        passTest();
    }

    public void test_regression_browser() {
        startTest("42117081", "regression_browser");

        // TODO: add browser_tap_browser_back/forward than we will work with
        // webView.
        ScreenBrowser.go_to_browser_precondition();
        ScreenBrowser.go_to_browser();
        ScreenBrowser.browser_tap_back();
        ScreenBrowser.browser();
        ScreenBrowser.browser_tap_expose();
        ScreenBrowser.browser_tap_expose_reset();
        ScreenBrowser.browser_tap_refresh();
        ScreenBrowser.browser_tap_actionsheet();
        ScreenBrowser.browser_actionsheet_tap_share();
        ScreenBrowser.browser_actionsheet_tap_share_reset();
        ScreenBrowser.browser_tap_actionsheet();
        ScreenBrowser.browser_actionsheet_tap_send_to_connection();

        passTest();
    }

    public void test_regression_abi_toast() {
        startTest("42120880", "test_regression_ABI_Toats ");

        PopupSyncContacts.go_to_popup_abi_toast();

        passTest();
    }

    public void test_regression_inbox_message_detail() {
        startTest("42119477", "regression_inbox_message_detail");

        ScreenMessageDetail.go_to_inbox_message_detail();
        ScreenMessageDetail.inbox_message_detail_tap_back();
        ScreenMessageDetail.inbox_message_detail_tap_back_reset();
        ScreenMessageDetail.inbox_message_detail_tap_message_up();
        ScreenMessageDetail.inbox_message_detail_tap_message_down();
        ScreenMessageDetail.inbox_message_detail_tap_profile();
        ScreenMessageDetail.inbox_message_detail_tap_profile_reset();
        ScreenMessageDetail.inbox_message_detail_toggle_read_state();
        ScreenMessageDetail.inbox_message_detail_tap_archive();
        ScreenMessageDetail.inbox_message_detail_tap_archive_reset();
        ScreenMessageDetail.inbox_message_detail_tap_delete();
        ScreenMessageDetail.inbox_message_detail_tap_delete_reset();
        ScreenMessageDetail.inbox_message_detail_tap_respond_actionsheet();
        ScreenMessageDetail.inbox_message_detail_respond_actionsheet_tap_reply();
        ScreenMessageDetail.inbox_message_detail_respond_actionsheet_tap_reply_reset();
        ScreenMessageDetail.inbox_message_detail_tap_respond_actionsheet();
        ScreenMessageDetail.inbox_message_detail_respond_actionsheet_tap_reply_all();
        ScreenMessageDetail.inbox_message_detail_respond_actionsheet_tap_reply_all_reset();
        ScreenMessageDetail.inbox_message_detail_tap_respond_actionsheet();
        ScreenMessageDetail.inbox_message_detail_respond_actionsheet_tap_forward();
        ScreenMessageDetail.inbox_message_detail_respond_actionsheet_tap_forward_reset();
        ScreenMessageDetail.inbox_message_detail_tap_respond_actionsheet();
        ScreenMessageDetail.inbox_message_detail_respond_actionsheet_tap_cancel();
        ScreenMessageDetail.inbox_message_detail_tap_compose_actionsheet();
        ScreenMessageDetail.inbox_message_detail_compose_actionsheet_tap_new_message();
        ScreenMessageDetail.inbox_message_detail_compose_actionsheet_tap_new_message_reset();
        ScreenMessageDetail.inbox_message_detail_tap_compose_actionsheet();
        ScreenMessageDetail.inbox_message_detail_compose_actionsheet_tap_new_invitation();
        ScreenMessageDetail.inbox_message_detail_compose_actionsheet_tap_new_invitation_reset();
        ScreenMessageDetail.inbox_message_detail_tap_compose_actionsheet();
        ScreenMessageDetail.inbox_message_detail_compose_actionsheet_tap_cancel();
        ScreenMessageDetail.inbox_message_detail_tap_expose();
        ScreenMessageDetail.inbox_message_detail_tap_expose_reset();
        ScreenMessageDetail.inbox_message_detail_tap_url();
        passTest();
    }

    public void test_regression_wvmp_profiles_rollup() {
        startTest("42120313", "regression_wvmp_profiles_rollup");

        ScreenWhosViewedYou.go_to_wvmp_profiles_rollup();
        ScreenWhosViewedYou.wvmp_profiles_rollup_tap_back();
        ScreenWhosViewedYou.wvmp_profiles_rollup();
        ScreenWhosViewedYou.wvmp_profiles_rollup_tap_expose();
        ScreenWhosViewedYou.wvmp_profiles_rollup_tap_expose_reset();
        ScreenWhosViewedYou.wvmp_profiles_rollup_tap_profile();

        passTest();
    }

    public void test_regression_invitation_compose() {
        startTest("42120887", "regression_invitation_compose");

        ScreenNewInvitation.go_to_invitation_compose();
        ScreenNewInvitation.invitation_compose_tap_cancel();
        ScreenNewInvitation.invitation_compose_tap_cancel_reset();
        ScreenNewInvitation.invitation_compose_tap_send_precondition();
        ScreenNewInvitation.invitation_compose_tap_send();

        passTest();
    }

    public void test_regression_profile() {
        startTest("42120927", "regression_profile");

        ScreenProfile.go_to_profile();
        ScreenProfile.profile_tap_back();
        ScreenProfile.profile();
        ScreenProfile.profile_tap_expose();
        ScreenProfile.profile_tap_expose_reset();
        ScreenProfile.profile_tap_fwd();
        ScreenProfile.profile_fwd_actionsheet_tap_cancel();
        ScreenProfile.profile_tap_fwd();
        ScreenProfile.profile_fwd_actionsheet_tap_send();
        ScreenProfile.profile_fwd_actionsheet_tap_send_reset();
        ScreenProfile.profile_tap_search();
        ScreenProfile.profile_tap_search_reset();
        ScreenProfile.profile_tap_message();
        ScreenProfile.profile_tap_message_reset();
        ScreenProfile.profile_tap_photo();
        ScreenProfile.profile_tap_photo_reset();
        ScreenProfile.profile_tap_activity();
        ScreenProfile.profile_tap_activity_reset();
        ScreenProfile.profile_tap_common();
        ScreenProfile.profile_tap_common_reset();
        ScreenProfile.profile_tap_connections();
        ScreenProfile.profile_tap_connections_reset();
        ScreenProfile.profile_tap_groups();
        ScreenProfile.profile_tap_groups_reset();
        ScreenProfile.profile_tap_website();
        ScreenProfile.profile_tap_website_reset();
        ScreenProfile.profile_tap_profile();
        ScreenProfile.profile_tap_profile_reset();
        ScreenProfile.profile_tap_twitter();
        ScreenProfile.profile_tap_twitter_reset();
        ScreenProfile.profile_tap_exp_company();
        ScreenProfile.profile_tap_exp_company_reset();
        ScreenProfile.profile_tap_address();
        ScreenProfile.profile_tap_address_reset();
        ScreenProfile.profile_tap_accept_invite_precondition();
        ScreenProfile.profile_tap_accept_invite();
        // TODO: this action send invite to real user.
        // ScreenProfile.profile_tap_invite_precondition();
        // ScreenProfile.profile_tap_invite();

        passTest();
    }

    public void test_regression_groups_discussion_detail() {
        startTest("42118807", "regression_groups_discussion_detail");

        ScreenDiscussionDetails.go_to_groups_discussion_detail();
        ScreenDiscussionDetails.groups_discussion_detail_tap_back();
        ScreenDiscussionDetails.groups_discussion_detail();
        ScreenDiscussionDetails.groups_discussion_detail_tap_expose();
        ScreenDiscussionDetails.groups_discussion_detail_tap_expose_reset();
        ScreenDiscussionDetails.groups_discussion_detail_tap_discussion_down();
        ScreenDiscussionDetails.groups_discussion_detail_tap_discussion_up();
        ScreenDiscussionDetails.groups_discussion_detail_tap_profile_author();
        ScreenDiscussionDetails.groups_discussion_detail_tap_profile_author_reset();
        ScreenDiscussionDetails.groups_discussion_detail_tap_comment();
        ScreenDiscussionDetails.groups_discussion_detail_tap_comment_reset();
        ScreenDiscussionDetails.groups_discussion_detail_tap_profile_commenter();
        ScreenDiscussionDetails.groups_discussion_detail_tap_profile_commenter_reset();
        ScreenDiscussionDetails.groups_discussion_detail_tap_likes_list();
        ScreenDiscussionDetails.groups_discussion_detail_tap_likes_list_reset();
        ScreenDiscussionDetails.groups_discussion_detail_tap_like_toggle();
        ScreenDiscussionDetails.groups_discussion_detail_tap_comments_list();
        ScreenDiscussionDetails.groups_discussion_detail_tap_comments_list_reset();

        passTest();
    }

    public void test_regression_select_recipients() {
        startTest("42119617", "regression_select_recipients");

        ScreenAddConnections.go_to_select_recipients();
        ScreenAddConnections.select_recipients_tap_scrollbar();
        ScreenAddConnections.select_recipients_tap_cancel();
        ScreenAddConnections.select_recipients();
        ScreenAddConnections.select_recipients_tap_select();
        ScreenAddConnections.select_recipients_tap_done();

        passTest();
    }
}