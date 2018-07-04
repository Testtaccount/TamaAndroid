package com.tama.chat.utils.helpers;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.q_municate_user_service.QMUserService;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.activities.chats.BaseDialogActivity;
import com.tama.q_municate_core.service.QBServiceConsts;
import com.tama.q_municate_db.managers.DataManager;
import com.tama.q_municate_db.models.Dialog;
import com.tama.q_municate_db.models.DialogOccupant;

public class ActivityUIHelper {

    private BaseActivity baseActivity;
    private QMUser senderUser;
    private Dialog messagesDialog;
    private String message;
    private boolean isPrivateMessage;

    public ActivityUIHelper(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public void showChatMessageNotification(Bundle extras) {
        QMUser senderUser = (QMUser) extras.getSerializable(QBServiceConsts.EXTRA_USER);
        String message = extras.getString(QBServiceConsts.EXTRA_CHAT_MESSAGE);
        String dialogId = extras.getString(QBServiceConsts.EXTRA_DIALOG_ID);
        if (isChatDialogExist(dialogId) && senderUser != null) {
            message = baseActivity.getString(R.string.snackbar_new_message_title, senderUser.getFullName(), message);
            if (!TextUtils.isEmpty(message)) {
                showNewNotification(getChatDialogForNotification(dialogId), senderUser, message);
            }
        }
    }

    private boolean isChatDialogExist(String dialogId) {
        return DataManager.getInstance().getQBChatDialogDataManager().exists(dialogId);
    }

    private QBChatDialog getChatDialogForNotification(String dialogId) {
        return DataManager.getInstance().getQBChatDialogDataManager().getByDialogId(dialogId);
    }

    //chka
//    private boolean isMessagesDialogCorrect(String dialogId) {
//        messagesDialog = DataManager.getInstance().getDialogDataManager().getByDialogId(dialogId);
//        return messagesDialog != null;
//    }

    public void showContactRequestNotification(Bundle extras) {
        int senderUserId = extras.getInt(QBServiceConsts.EXTRA_USER_ID);
        QMUser senderUser = QMUserService.getInstance().getUserCache().get((long) senderUserId);
        DialogOccupant dialogOccupant = DataManager.getInstance().getDialogOccupantDataManager().getDialogOccupantForPrivateChat(senderUserId);

        if (dialogOccupant != null && senderUser != null) {
            String dialogId = dialogOccupant.getDialog().getDialogId();
            if (isChatDialogExist(dialogId)) {
                String message = baseActivity.getString(R.string.snackbar_new_contact_request_title, senderUser.getFullName());
                if (!TextUtils.isEmpty(message)) {
                    showNewNotification(getChatDialogForNotification(dialogId), senderUser, message);
                }
            }
        }
    }

    private void showNewNotification(final QBChatDialog chatDialog, final QMUser senderUser, String message) {
        baseActivity.hideSnackBar();
        baseActivity.showSnackbar(message, Snackbar.LENGTH_LONG, R.string.dialog_reply,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showDialog(chatDialog, senderUser);
                    }
                });
    }

    //chka
//    public void showNewNotification() {
//        baseActivity.hideSnackBar();
//        baseActivity.showSnackbar(message, Snackbar.LENGTH_LONG, R.string.dialog_reply,
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showDialog();
//                    }
//                });
//    }

    private void showDialog(QBChatDialog chatDialog, QMUser senderUser) {
        if (baseActivity instanceof BaseDialogActivity) {
            baseActivity.finish();
        }

        if (QBDialogType.PRIVATE.equals(chatDialog.getType())) {
            baseActivity.startPrivateChatActivity(senderUser, chatDialog);
        } else {
            baseActivity.startGroupChatActivity(chatDialog);
        }
    }

    //chka
//    private void showDialog() {
//        if (baseActivity instanceof BaseDialogActivity) {
//            baseActivity.finish();
//        }
//
////        if (isPrivateMessage) {
////            baseActivity.startPrivateChatActivity(senderUser, messagesDialog);
////        } else {
////            baseActivity.startGroupChatActivity(messagesDialog);
////        }
//    }
}