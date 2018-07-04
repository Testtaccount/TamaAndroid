package com.tama.q_municate_core.qb.helpers;


import android.content.Context;
import android.text.TextUtils;
import com.facebook.login.LoginManager;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.auth.session.QBSession;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.LoginType;
import com.tama.q_municate_core.models.UserCustomData;
import com.tama.q_municate_core.utils.UserFriendUtils;
import com.tama.q_municate_core.utils.Utils;
import com.tama.q_municate_core.utils.helpers.CoreSharedHelper;
import com.tama.q_municate_db.managers.DataManager;
import java.io.File;

public class QBAuthHelper extends BaseHelper {

    private static final String TAG_ANDROID = "android";

    public QBAuthHelper(Context context) {
        super(context);
    }

    public QBUser login(QBUser inputUser) throws QBResponseException, BaseServiceException {
        QBUser qbUser;
        QBAuth.createSession();
        String password = inputUser.getPassword();
        qbUser = (QBUser) QBUsers.signIn(inputUser);

        if (!hasUserCustomData(qbUser)) {
            qbUser.setOldPassword(password);
            updateUser(qbUser);
        }

        String token = QBAuth.getBaseService().getToken();
        qbUser.setPassword(password);

        saveOwnerUser(qbUser);

        AppSession.startSession(qbUser);

        return qbUser;
    }

    private void saveOwnerUser(QBUser qbUser) {
        QMUser user = UserFriendUtils.createLocalUser(qbUser);
        DataManager.getInstance().getUserRequestDataManager().createOrUpdate(user);
    }

    public QBUser login(String socialProvider, String accessToken,
        String accessTokenSecret) throws QBResponseException, BaseServiceException {
        QBUser qbUser;
        QBSession session = (QBSession) QBAuth.createSession();

        if (socialProvider.equals(QBProvider.TWITTER_DIGITS)){
            qbUser = (QBUser) QBUsers.signInUsingTwitterDigits(accessToken, accessTokenSecret);
            CoreSharedHelper.getInstance().saveTDServiceProvider(accessToken);
            CoreSharedHelper.getInstance().saveTDCredentials(accessTokenSecret);
        } else {
            qbUser = (QBUser) QBUsers.signInUsingSocialProvider(socialProvider, accessToken, accessTokenSecret);
            CoreSharedHelper.getInstance().saveFBToken(accessToken);
        }

        qbUser.setPassword(session.getToken());

        if (!hasUserCustomData(qbUser)) {
            qbUser.setOldPassword(session.getToken());
            qbUser = updateUser(qbUser);
        }

        qbUser.setPassword(session.getToken());
        String qbToken = QBAuth.getBaseService().getToken();

        saveOwnerUser(qbUser);

        AppSession.startSession(qbUser);

        return qbUser;
    }

    public QBUser signup(QBUser inputUser, File file) throws QBResponseException, BaseServiceException {
        QBUser qbUser;
        UserCustomData userCustomData = new UserCustomData();

        QBAuth.createSession();
        String password = inputUser.getPassword();
        inputUser.setOldPassword(password);
        inputUser.setCustomData(Utils.customDataToString(userCustomData));

        StringifyArrayList<String> stringifyArrayList = new StringifyArrayList<String>();
        stringifyArrayList.add(TAG_ANDROID);
        inputUser.setTags(stringifyArrayList);

        qbUser = (QBUser) QBUsers.signUpSignInTask(inputUser);

        if (file != null) {
            QBFile qbFile = (QBFile) QBContent.uploadFileTask(file, true, (String) null);
            userCustomData.setAvatarUrl(qbFile.getPublicUrl());
            inputUser.setCustomData(Utils.customDataToString(userCustomData));
            qbUser = (QBUser) QBUsers.updateUser(inputUser);
        }

        qbUser.setCustomDataClass(UserCustomData.class);
        qbUser.setPassword(password);
        String token = QBAuth.getBaseService().getToken();

        saveOwnerUser(qbUser);

        AppSession.startSession(qbUser);

        return qbUser;
    }

    public void logout() throws QBResponseException {
        AppSession activeSession = AppSession.getSession();
        if (activeSession != null) {
            activeSession.closeAndClear();
        }

        LoginManager.getInstance().logOut();
        QBAuth.deleteSession();
    }

    public QBUser updateUser(QBUser inputUser) throws QBResponseException {
        QBUser user;

        String password = inputUser.getPassword();

        UserCustomData userCustomDataNew = getUserCustomData(inputUser);
        inputUser.setCustomData(Utils.customDataToString(userCustomDataNew));

        inputUser.setPassword(null);
        inputUser.setOldPassword(null);

        user = (QBUser) QBUsers.updateUser(inputUser);

        if (LoginType.EMAIL.equals(AppSession.getSession().getLoginType())) {
            user.setPassword(password);
        } else {
            try {
                user.setPassword(QBAuth.getBaseService().getToken());
            } catch (BaseServiceException e) {


            }
        }

        return user;
    }

    public QBUser updateUser(QBUser user, File file) throws QBResponseException {
        QBUser newUser = new QBUser();

        QBFile qbFile = (QBFile) QBContent.uploadFileTask(file, true, (String) null);
        newUser.setId(user.getId());
        newUser.setPassword(user.getPassword());
        newUser.setFileId(qbFile.getId());
        newUser.setFullName(user.getFullName());

        UserCustomData userCustomData = getUserCustomData(user);
        userCustomData.setAvatarUrl(qbFile.getPublicUrl());
        newUser.setCustomData(Utils.customDataToString(userCustomData));

        return updateUser(newUser);
    }

    private UserCustomData getUserCustomData(QBUser user) {
        if (TextUtils.isEmpty(user.getCustomData())) {
            return new UserCustomData();
        }

        UserCustomData userCustomData = Utils.customDataToObject(user.getCustomData());

        if (userCustomData != null) {
            return userCustomData;
        } else {
            return new UserCustomData();
        }
    }

    private boolean hasUserCustomData(QBUser user) {
        if (TextUtils.isEmpty(user.getCustomData())) {
            return false;
        }
        UserCustomData userCustomData = Utils.customDataToObject(user.getCustomData());
        return userCustomData != null;
    }

    public void resetPassword(String email) throws QBResponseException {
        QBAuth.createSession();
        QBUsers.resetPassword(email);
    }

    public QBUser changePasswordUser(QBUser inputUser) throws QBResponseException {
        QBUser user;
        String password = inputUser.getPassword();
        user = (QBUser) QBUsers.updateUser(inputUser);
        user.setPassword(password);

        return user;
    }
}








//chka
//import android.content.Context;
//import android.text.TextUtils;
//import com.facebook.login.LoginManager;
//import com.quickblox.auth.QBAuth;
//import com.quickblox.auth.model.QBProvider;
//import com.quickblox.auth.session.QBSession;
//import com.quickblox.content.QBContent;
//import com.quickblox.content.model.QBFile;
//import com.quickblox.core.exception.BaseServiceException;
//import com.quickblox.core.exception.QBResponseException;
//import com.quickblox.core.helper.StringifyArrayList;
//import com.quickblox.q_municate_user_service.model.QMUser;
//import com.quickblox.users.QBUsers;
//import com.quickblox.users.model.QBUser;
//import com.tama.q_municate_core.models.AppSession;
//import com.tama.q_municate_core.models.LoginType;
//import com.tama.q_municate_core.models.UserCustomData;
//import com.tama.q_municate_core.utils.UserFriendUtils;
//import com.tama.q_municate_core.utils.Utils;
//import com.tama.q_municate_core.utils.helpers.CoreSharedHelper;
//import com.tama.q_municate_db.managers.DataManager;
//import com.tama.q_municate_db.models.User;
//import java.io.File;
//
////chka pp
//public class QBAuthHelper extends BaseHelper {
//
//    private static final String TAG_ANDROID = "android";
//
//    public QBAuthHelper(Context context) {
//        super(context);
//    }
//
//    public QBUser login(QBUser inputUser) throws QBResponseException, BaseServiceException {
//        QBUser qbUser;
//        QBAuth.createSession();
//        String password = inputUser.getPassword();
//        qbUser = (QBUser) QBUsers.signIn(inputUser);
//
//        if (!hasUserCustomData(qbUser)) {
//            qbUser.setOldPassword(password);
//            updateUser(qbUser);
//        }
//
//        String token = QBAuth.getBaseService().getToken();
//        qbUser.setPassword(password);
//
//        saveOwnerUser(qbUser);
//
//        AppSession.startSession(qbUser);
//
//        return qbUser;
//    }
//
//    private void saveOwnerUser(QBUser qbUser) {
//        QMUser user = UserFriendUtils.createLocalUser(qbUser, User.Role.OWNER);
//        DataManager.getInstance().getUserDataManager().createOrUpdate(user);
//    }
//
//    public QBUser login(String socialProvider, String accessToken,
//            String accessTokenSecret) throws QBResponseException, BaseServiceException {
//        QBUser qbUser;
//        QBSession session = (QBSession) QBAuth.createSession();
//
//        if (socialProvider.equals(QBProvider.TWITTER_DIGITS)){
//            qbUser = (QBUser) QBUsers.signInUsingTwitterDigits(accessToken, accessTokenSecret);
//            CoreSharedHelper.getInstance().saveTDServiceProvider(accessToken);
//            CoreSharedHelper.getInstance().saveTDCredentials(accessTokenSecret);
//        } else {
//            qbUser = (QBUser) QBUsers.signInUsingSocialProvider(socialProvider, accessToken, accessTokenSecret);
//            CoreSharedHelper.getInstance().saveFBToken(accessToken);
//        }
//
//        qbUser.setPassword(session.getToken());
//
//        if (!hasUserCustomData(qbUser)) {
//            qbUser.setOldPassword(session.getToken());
//            qbUser = updateUser(qbUser);
//        }
//
//        qbUser.setPassword(session.getToken());
//        String qbToken = QBAuth.getBaseService().getToken();
//
//        saveOwnerUser(qbUser);
//
//        AppSession.startSession(qbUser);
//
//        return qbUser;
//    }
//
//    public QBUser signup(QBUser inputUser, File file) throws QBResponseException, BaseServiceException {
//        QBUser qbUser;
//        UserCustomData userCustomData = new UserCustomData();
//
//        QBAuth.createSession();
//        String password = inputUser.getPassword();
//        inputUser.setOldPassword(password);
//        inputUser.setCustomData(Utils.customDataToString(userCustomData));
//
//        StringifyArrayList<String> stringifyArrayList = new StringifyArrayList<String>();
//        stringifyArrayList.add(TAG_ANDROID);
//        inputUser.setTags(stringifyArrayList);
//
//        qbUser = (QBUser) QBUsers.signUpSignInTask(inputUser);
//
//        if (file != null) {
//            QBFile qbFile = (QBFile) QBContent.uploadFileTask(file, true, (String) null);
//            userCustomData.setAvatarUrl(qbFile.getPublicUrl());
//            inputUser.setCustomData(Utils.customDataToString(userCustomData));
//            qbUser = (QBUser) QBUsers.updateUser(inputUser);
//        }
//
//        qbUser.setCustomDataClass(UserCustomData.class);
//        qbUser.setPassword(password);
//        String token = QBAuth.getBaseService().getToken();
//
//        saveOwnerUser(qbUser);
//
//        AppSession.startSession(qbUser);
//
//        return qbUser;
//    }
//
//    public void logout() throws QBResponseException {
//        AppSession activeSession = AppSession.getSession();
//        if (activeSession != null) {
//            activeSession.closeAndClear();
//        }
//
//        LoginManager.getInstance().logOut();
//        QBAuth.deleteSession();
//    }
//
//    public QBUser updateUser(QBUser inputUser) throws QBResponseException {
//        QBUser user;
//
//        String password = inputUser.getPassword();
//
//        UserCustomData userCustomDataNew = getUserCustomData(inputUser);
//        inputUser.setCustomData(Utils.customDataToString(userCustomDataNew));
//
//        inputUser.setPassword(null);
//        inputUser.setOldPassword(null);
//
//        user = (QBUser) QBUsers.updateUser(inputUser);
//
//        if (LoginType.EMAIL.equals(AppSession.getSession().getLoginType())) {
//            user.setPassword(password);
//        } else {
//            user.setPassword(QBAuth.getSession().toString());
//        }
//
//        return user;
//    }
//
//    public QBUser updateUser(QBUser user, File file) throws QBResponseException {
//        QBUser newUser = new QBUser();
//
//        QBFile qbFile = (QBFile) QBContent.uploadFileTask(file, true, (String) null);
//        newUser.setId(user.getId());
//        newUser.setPassword(user.getPassword());
//        newUser.setFileId(qbFile.getId());
//        newUser.setFullName(user.getFullName());
//
//        UserCustomData userCustomData = getUserCustomData(user);
//        userCustomData.setAvatarUrl(qbFile.getPublicUrl());
//        newUser.setCustomData(Utils.customDataToString(userCustomData));
//
//        return updateUser(newUser);
//    }
//
//    private UserCustomData getUserCustomData(QBUser user) {
//        if (TextUtils.isEmpty(user.getCustomData())) {
//            return new UserCustomData();
//        }
//
//        UserCustomData userCustomData = Utils.customDataToObject(user.getCustomData());
//
//        if (userCustomData != null) {
//            return userCustomData;
//        } else {
//            return new UserCustomData();
//        }
//    }
//
//    private boolean hasUserCustomData(QBUser user) {
//        if (TextUtils.isEmpty(user.getCustomData())) {
//            return false;
//        }
//        UserCustomData userCustomData = Utils.customDataToObject(user.getCustomData());
//        return userCustomData != null;
//    }
//
//    public void resetPassword(String email) throws QBResponseException {
//        QBAuth.createSession();
//        QBUsers.resetPassword(email);
//    }
//
//    public QBUser changePasswordUser(QBUser inputUser) throws QBResponseException {
//        QBUser user;
//        String password = inputUser.getPassword();
//        user = (QBUser) QBUsers.updateUser(inputUser);
//        user.setPassword(password);
//
//        return user;
//    }
//}