package com.tama.q_municate_core.qb.commands.rest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tama.q_municate_core.core.command.ServiceCommand;
import com.tama.q_municate_core.qb.helpers.QBAuthHelper;
import com.tama.q_municate_core.service.QBService;
import com.tama.q_municate_core.service.QBServiceConsts;
//chka
public class QBLogoutRestCommand extends ServiceCommand {

    private final QBAuthHelper authHelper;

    public QBLogoutRestCommand(Context context, QBAuthHelper authHelper, String successAction,
            String failAction) {
        super(context, successAction, failAction);
        this.authHelper = authHelper;
    }

    public static void start(Context context) {
        Intent intent = new Intent(QBServiceConsts.LOGOUT_REST_ACTION, null, context, QBService.class);
        context.startService(intent);
    }

    @Override
    public Bundle perform(Bundle extras) throws Exception {
        authHelper.logout();
        return extras;
    }
}