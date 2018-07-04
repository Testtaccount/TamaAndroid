package com.tama.chat.utils.helpers.notification;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.utils.SystemUtils;
import com.tama.chat.utils.helpers.SharedHelper;
import com.tama.q_municate_core.models.NotificationEvent;
//popoxac nael notification
public class ChatNotificationHelper {

    public static final String MESSAGE = "message";
    public static final String DIALOG_ID = "dialog_id";
    public static final String USER_ID = "user_id";

    private Context context;
    private SharedHelper appSharedHelper;
    private String dialogId;
    private int userId;

    private static String message;
    private static boolean isLoginNow;

    public ChatNotificationHelper(Context context) {
        this.context = context;
        appSharedHelper = App.getInstance().getAppSharedHelper();
    }

    public void parseChatMessage(Bundle extras) {
        if (extras.getString(ChatNotificationHelper.MESSAGE) != null) {
            message = extras.getString(ChatNotificationHelper.MESSAGE);
        }

        if (extras.getString(ChatNotificationHelper.USER_ID) != null) {
            userId = Integer.parseInt(extras.getString(ChatNotificationHelper.USER_ID));
        }

        if (extras.getString(ChatNotificationHelper.DIALOG_ID) != null) {
            dialogId = extras.getString(ChatNotificationHelper.DIALOG_ID);
        }

        if (SystemUtils.isAppRunningNow()) {
            return;
        }

        if (isOwnMessage(userId)){
            return;
        }

        boolean chatPush = userId != 0 && !TextUtils.isEmpty(dialogId);

        if (chatPush) {
            saveOpeningDialogData(userId, dialogId);
            saveOpeningDialog(true);
            sendChatNotification(message, userId, dialogId);
//            if (AppSession.getSession().getUser() != null && !isLoginNow) { Hayk
//                isLoginNow = true;
//                LoginHelper loginHelper = new LoginHelper(context);
//                loginHelper.makeGeneralLogin(new GlobalLoginListener());
//                return;
//            }
        } else {
            // push about call
//            sendNotification(message);
            sendCommonNotification(message);
        }

        saveOpeningDialog(false);
    }

    public void sendChatNotification(String message, int userId, String dialogId) {
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setTitle(context.getString(R.string.app_name));
        notificationEvent.setSubject(message);
        notificationEvent.setBody(message);

        NotificationManagerHelper.sendChatNotificationEvent(context, userId, dialogId, notificationEvent);
    }

    private void sendCommonNotification(String message) {
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setTitle(context.getString(R.string.app_name));
        notificationEvent.setSubject(message);
        notificationEvent.setBody(message);

        NotificationManagerHelper.sendCommonNotificationEvent(context, notificationEvent);
    }

//    //chka
//    public void sendNotification(String message) {
//        NotificationEvent notificationEvent = new NotificationEvent();
//        notificationEvent.setTitle(context.getString(R.string.app_name));
//        notificationEvent.setSubject(message);
//        notificationEvent.setBody(message);
//        NotificationManagerHelper.sendNotificationEvent(context, notificationEvent);
//    }

    public void saveOpeningDialogData(int userId, String dialogId) {
        appSharedHelper.savePushUserId(userId);
        appSharedHelper.savePushDialogId(dialogId);
    }

    public void saveOpeningDialog(boolean open) {
        appSharedHelper.saveNeedToOpenDialog(open);
    }

//    //chka
//    private boolean isPushForPrivateChat() {
//        Dialog dialog = DataManager.getInstance().getDialogDataManager().getByDialogId(dialogId);
//        return dialog != null && dialog.getType().equals(Dialog.Type.PRIVATE);
//    }
//
//    //chka
//    private class GlobalLoginListener extends SimpleGlobalLoginListener {
//
//        @Override
//        public void onCompleteQbChatLogin() {
//            isLoginNow = false;
//
//            saveOpeningDialog(true);
//
//            Intent intent = SystemUtils.getPreviousIntent(context);
//            if (!isPushForPrivateChat() || intent == null) {
//                sendNotification(message);
//            }
//        }
//
//        @Override
//        public void onCompleteWithError(String error) {
//            isLoginNow = false;
//
//            saveOpeningDialog(false);
//        }
//    }

    private boolean isOwnMessage(int senderUserId) {
        return appSharedHelper.getUserId() == senderUserId;
    }
}