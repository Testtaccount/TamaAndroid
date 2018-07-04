package com.tama.q_municate_core.models;

import android.text.TextUtils;
import android.util.Log;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.users.model.QBUser;
import com.tama.q_municate_core.utils.helpers.CoreSharedHelper;
import java.io.Serializable;
import java.util.Date;

//import com.quickblox.core.server.BaseService;

public class AppSession implements Serializable {

    private static final Object lock = new Object();
    private static AppSession activeSession;

    private CoreSharedHelper coreSharedHelper;
    private LoginType loginType;
    private QBUser qbUser;

    private String qbToken;
    private String tamaAccountId;

    private ChatState chatState = ChatState.FOREGROUND;


    //chka hanel
//    private AppSession(LoginType loginType, QBUser qbUser, String qbToken) {
//        coreSharedHelper = CoreSharedHelper.getInstance();
//        this.loginType = loginType;
//        this.qbUser = qbUser;
//        this.qbToken = qbToken;
//        save();
//    }

    private AppSession(QBUser qbUser) {
        this.loginType = getLoginTypeBySessionParameters(QBSessionManager.getInstance().getSessionParameters());
        this.qbUser = qbUser;
        coreSharedHelper = CoreSharedHelper.getInstance();
        save();
    }

    public void updateState(ChatState state){
        chatState = state;
    }

    public ChatState getChatState() {
        return chatState;
    }


    //chka hanel
//    public static void startSession(LoginType loginType, QBUser user, String qbToken) {
//        activeSession = new AppSession(loginType, user, qbToken);
//    }

    public static void startSession(QBUser user) {
        activeSession = new AppSession(user);
    }

    private static AppSession getActiveSession() {
        synchronized (lock) {
            return activeSession;
        }
    }

    public static AppSession load() {

        //hanel
        String loginTypeRaw = CoreSharedHelper.getInstance().getLoginType();
        String qbToken = CoreSharedHelper.getInstance().getQBToken();

        int userId = CoreSharedHelper.getInstance().getUserId();
        String userFullName =CoreSharedHelper.getInstance().getUserFullName();

        QBUser qbUser = new QBUser();
        qbUser.setId(userId);
        qbUser.setEmail(CoreSharedHelper.getInstance().getUserEmail());
        qbUser.setPassword(CoreSharedHelper.getInstance().getUserPassword());
        qbUser.setFullName(userFullName);
        qbUser.setPhone(CoreSharedHelper.getInstance().getUserFullPhoneNumber());
        qbUser.setFacebookId(CoreSharedHelper.getInstance().getFBId());
        qbUser.setTwitterId(CoreSharedHelper.getInstance().getTwitterId());
        qbUser.setTwitterDigitsId(CoreSharedHelper.getInstance().getTwitterDigitsId());
        qbUser.setCustomData(CoreSharedHelper.getInstance().getUserCustomData());

        //hanel
//        LoginType loginType = LoginType.valueOf(loginTypeRaw);

        activeSession = new AppSession(qbUser);

        //hanel
//        return new AppSession(loginType, qbUser, qbToken);
        return activeSession;
    }


    public static boolean isSessionExistOrNotExpired(long expirationTime) {
        //hanel
//        try {
//            BaseService baseService = QBAuth.getBaseService();
//            String token = baseService.getToken();
            QBSessionManager qbSessionManager = QBSessionManager.getInstance();
            String token = qbSessionManager.getToken();
            if (token == null) {
                Log.d("AppSession", "token == null");
                return false;
            }
//            Date tokenExpirationDate = baseService.getTokenExpirationDate();
            Date tokenExpirationDate = qbSessionManager.getTokenExpirationDate();
            long tokenLiveOffset = tokenExpirationDate.getTime() - System.currentTimeMillis();
            return tokenLiveOffset > expirationTime;
//        } catch (BaseServiceException e) {
//            Log.d("AppSession", "BaseServiceException: " + e.getMessage());
//            // nothing by default
//        }
//        return false;
    }

    public static AppSession getSession() {
        AppSession activeSession = AppSession.getActiveSession();
        if (activeSession == null) {
            activeSession = AppSession.load();
        }
        return activeSession;
    }

    public void closeAndClear() {
        //hanel
//        coreSharedHelper.saveQBToken(null);
//        coreSharedHelper.saveLoginType(null);

        coreSharedHelper.clearUserData();

        activeSession = null;
    }

    public QBUser getUser() {
        return qbUser;
    }

    public void save() {
//        coreSharedHelper.saveQBToken(qbToken);
//        coreSharedHelper.saveLoginType(loginType.toString());
        saveUser(qbUser);
    }

    public void updateUser(QBUser qbUser) {
        this.qbUser = qbUser;
        saveUser(this.qbUser);
    }

    private void saveUser(QBUser user) {
        coreSharedHelper.saveUserId(user.getId());
        coreSharedHelper.saveUserEmail(user.getEmail());
        coreSharedHelper.saveUserPassword(user.getPassword());
        coreSharedHelper.saveUserFullName(user.getFullName());
        coreSharedHelper.saveFBId(user.getFacebookId());
        coreSharedHelper.saveTwitterId(user.getTwitterId());
        coreSharedHelper.saveTwitterDigitsId(user.getTwitterDigitsId());
        coreSharedHelper.saveUserFullPhoneNumber(user.getPhone());
        coreSharedHelper.saveUserCustomData(user.getCustomData());
    }

    public boolean isLoggedIn() {
        //hanel & chka
//        return qbUser != null && !TextUtils.isEmpty(qbToken) && tamaAccountId!=null;
        return QBSessionManager.getInstance().getSessionParameters() != null && tamaAccountId!=null;
    }

    public boolean isSessionExist() {
        //hanel
//        return loginType != null && !TextUtils.isEmpty(qbToken);
        return !TextUtils.isEmpty(QBSessionManager.getInstance().getToken());
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setTamaAccountId(String tamaAccountId) {
        this.tamaAccountId = tamaAccountId;
    }

    public String getTamaAccountId(){
        return tamaAccountId;
    }

    private LoginType getLoginTypeBySessionParameters(QBSessionParameters sessionParameters){
        LoginType result = null;
        if(sessionParameters == null){
            return null;
        }
        String socialProvider = sessionParameters.getSocialProvider();
        if(socialProvider == null){
            result = LoginType.EMAIL;
        } else if (socialProvider.equals(QBProvider.FACEBOOK)){
            result = LoginType.FACEBOOK;
        } else if (socialProvider.equals(QBProvider.FIREBASE_PHONE)){
            result = LoginType.FIREBASE_PHONE;
        } else if (socialProvider.equals(QBProvider.TWITTER_DIGITS)){ //for correct migration from TWITTER_DIGITS to FIREBASE_PHONE
            result = LoginType.FIREBASE_PHONE;
        }

        if (result != null) {
            loginType = result;
        }

        return result;
    }

    public enum ChatState {
        BACKGROUND, FOREGROUND
    }

}