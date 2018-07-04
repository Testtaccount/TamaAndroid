package com.tama.chat.utils.helpers;

import android.content.Context;

import com.tama.q_municate_core.utils.helpers.CoreSharedHelper;

public class SharedHelper extends CoreSharedHelper {

    public class Constants {

//        public static final String NAME = "Tama";
//
//        public static final String IMPORT_INITIALIZED = "import_initialized";
//        public static final String FIRST_AUTH = "first_auth";
//        public static final String FB_TOKEN = "fb_token";
//        public static final String FIREBASE_TOKEN = "firebase_token";
//
//        public static final String USER_ID = "user_id";
//        public static final String USER_EMAIL = "user_email";
//        public static final String USER_PASSWORD = "user_password";
//        public static final String USER_FULL_NAME = "full_name";
//        public static final String USER_FB_ID = "facebook_id";
//        public static final String USER_TWITTER_ID = "twitter_id";
//        public static final String USER_TD_ID = "twitter_digits_id";
//        public static final String USER_CUSTOM_DATA = "user_custom_data";
//
//        public static final String PUSH_NEED_TO_OPEN_DIALOG = "push_need_to_open_dialog";
//        public static final String PUSH_DIALOG_ID = "push_dialog_id";
//        public static final String PUSH_USER_ID = "push_user_id";
//        public static final String PUSH_REGISTRATION_ID = "push_registration_id";
//        public static final String PUSH_APP_VERSION = "push_app_version";
//
//        public static final String CALL_HW_CODEC = "call_hw_codec";
//        public static final String CALL_RESOLUTION = "call_resolution";
//        public static final String CALL_STARTBITRATE = "call_startbitrate";
//        public static final String CALL_STARTBITRATE_VALUE = "call_startbitrate_value";
//        public static final String CALL_VIDEO_CODEC = "call_video_codec";
//        public static final String CALL_AUDIO_CODEC = "call_audio_codec";
//
//        public static final String LAST_OPEN_ACTIVITY = "last_open_activity";
//
//        public static final String PERMISSIONS_SAVE_FILE_WAS_REQUESTED = "permission_save_file_was_requested";


        public static final String USER_IS_EUROPEAN = "user_is_european";
        public static final String USER_AGREEMENT = "user_agreement";
        public static final String REMEMBER_ME = "remember_me";
        public static final String TAMA_ACCOUNT_ID = "tama_account_id";
        public static final String TAMA_ACCOUNT_PHONE_NUMBER = "tama_account_phone_number";
        public static final String PRODUCTS_LIST = "products_list";
        public static final String ENABLING_PUSH_NOTIFICATIONS = "enabling_push_notifications";
        public static final String ENABLING_SAVE_PHOTO_VIDEO = "enabling_save_photo_video";
        public static final String TAMA_ACCOUNT_TOKEN_TYPE = "TAMA_ACCOUNT_TOKEN_TYPE";
        public static final String TAMA_ACCOUNT_EXPIRES_IN = "TAMA_ACCOUNT_EXPIRES_IN";
        public static final String TAMA_ACCOUNT_ACCESS_TOKEN = "TAMA_ACCOUNT_ACCESS_TOKEN";
        public static final String TAMA_ACCOUNT_REFRESH_TOKEN = "TAMA_ACCOUNT_REFRESH_TOKEN";
    }

    public SharedHelper(Context context) {
        super(context);
    }

    public boolean isShownUserAgreement() {
        return getPref(Constants.USER_AGREEMENT, false);
    }

    public void saveShownUserAgreement(boolean save) {
        savePref(Constants.USER_AGREEMENT, save);
    }

    public boolean isSavedRememberMe() {
        return getPref(Constants.REMEMBER_ME, false);
    }

    public void saveSavedRememberMe(boolean save) {
        savePref(Constants.REMEMBER_ME, save);
    }

    public String getTamaAccountId() {
        return getPref(Constants.TAMA_ACCOUNT_ID, null);
    }

    public void saveTamaAccountId(String id) {
        savePref(Constants.TAMA_ACCOUNT_ID, id);
    }


    public String getTamaAccountTokenType() {
        return getPref(Constants.TAMA_ACCOUNT_TOKEN_TYPE, null);
    }

    public void saveTamaAccountTokenType(String tokenType) {
        savePref(Constants.TAMA_ACCOUNT_TOKEN_TYPE, tokenType);
    }


    public String getTamaAccountExpiresIn() {
        return getPref(Constants.TAMA_ACCOUNT_EXPIRES_IN, null);
    }

    public void saveTamaAccountExpiresIn(String expiresIn) {
        savePref(Constants.TAMA_ACCOUNT_EXPIRES_IN, expiresIn);
    }

    public String getTamaAccountAccessToken() {
        return getPref(Constants.TAMA_ACCOUNT_ACCESS_TOKEN, "DEFAULT");
    }

    public void saveTamaAccountAccessToken(String accessToken) {
        savePref(Constants.TAMA_ACCOUNT_ACCESS_TOKEN, accessToken);
    }


    public String getTamaAccountRefreshToken() {
        return getPref(Constants.TAMA_ACCOUNT_REFRESH_TOKEN, null);
    }

    public void saveTamaAccountRefreshToken(String refreshToken) {
        savePref(Constants.TAMA_ACCOUNT_REFRESH_TOKEN, refreshToken);
    }


    public Boolean getTamaIsEurope() {
        return getPref(Constants.USER_IS_EUROPEAN, false);
    }

    public void saveTamaIsEurope(boolean bool) {
        savePref(Constants.USER_IS_EUROPEAN, bool);
    }

    public String getTamaAccountPhoneNumber() {
        return getPref(Constants.TAMA_ACCOUNT_PHONE_NUMBER, null);
    }

    public void saveTamaAccountPhoneNumber(String number) {
        savePref(Constants.TAMA_ACCOUNT_PHONE_NUMBER, number);
    }

    public String getProductsList() {
        return getPref(Constants.PRODUCTS_LIST, null);
    }

    public void saveProductsList(String id) {
        savePref(Constants.PRODUCTS_LIST, id);
    }

    public boolean isEnablePushNotifications() {
        return getPref(Constants.ENABLING_PUSH_NOTIFICATIONS, true);
    }

    public void saveEnablePushNotifications(boolean enable) {
        savePref(Constants.ENABLING_PUSH_NOTIFICATIONS, enable);
    }

    public boolean isEnableSavePhotoVideo() {
        return getPref(Constants.ENABLING_SAVE_PHOTO_VIDEO, true);
    }

    public void saveEnableSavePhotoVideo(boolean enable) {
        savePref(Constants.ENABLING_SAVE_PHOTO_VIDEO, enable);
    }
}