package com.tama.q_municate_core.qb.commands.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.quickblox.chat.model.QBDialogType;
import com.tama.q_municate_core.core.command.ServiceCommand;
import com.tama.q_municate_core.qb.helpers.QBChatHelper;
import com.tama.q_municate_core.service.QBService;
import com.tama.q_municate_core.service.QBServiceConsts;

public class QBDeleteChatCommand extends ServiceCommand {

    private QBChatHelper chatHelper;

    public QBDeleteChatCommand(Context context, QBChatHelper chatHelper, String successAction,
                               String failAction) {
        super(context, successAction, failAction);
        this.chatHelper = chatHelper;
    }

    //chka
    public static void start(Context context, String dialogId, int dialogType, String messageId) {
        Intent intent = new Intent(QBServiceConsts.DELETE_DIALOG_ACTION, null, context, QBService.class);
        intent.putExtra(QBServiceConsts.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra(QBServiceConsts.EXTRA_DIALOG_TYPE, dialogType);
        intent.putExtra(QBServiceConsts.EXTRA_MESSAGE_ID, messageId);
        context.startService(intent);
    }

    public static void start(Context context, String dialogId, int dialogType) {
        Intent intent = new Intent(QBServiceConsts.DELETE_DIALOG_ACTION, null, context, QBService.class);
        intent.putExtra(QBServiceConsts.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra(QBServiceConsts.EXTRA_DIALOG_TYPE, dialogType);
        context.startService(intent);
    }

    @Override
    protected Bundle perform(Bundle extras) throws Exception {
        String dialogId = extras.getString(QBServiceConsts.EXTRA_DIALOG_ID);
        String messageId = extras.getString(QBServiceConsts.EXTRA_MESSAGE_ID);
        QBDialogType dialogType = QBDialogType.parseByCode(extras.getInt(QBServiceConsts.EXTRA_DIALOG_TYPE));
        if(dialogId==null) {
            chatHelper.deleteMessage(messageId);
        }else{
            chatHelper.deleteDialog(dialogId, dialogType);
        }
        return extras;
//        String dialogId = extras.getString(QBServiceConsts.EXTRA_DIALOG_ID);
//        QBDialogType dialogType = QBDialogType.parseByCode(extras.getInt(QBServiceConsts.EXTRA_DIALOG_TYPE));
//        chatHelper.deleteDialog(dialogId, dialogType);
//        return extras;
    }


}