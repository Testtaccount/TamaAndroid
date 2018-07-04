package com.tama.chat.tamaAccount;

import android.content.Context;

public interface TamaAccountHelperListener {
    String CURRENT_BALANCE = "currentBalance";

//    void balanceTopUpRequestSuccess(String message);
//    void balanceTopUpRequestError(String message);
//    void getBalanceSuccess(String message);

    void requestSuccess(String data);
    void requestError(String data);
    void alertDialogCancelListener();
    Context getAppContext();


}
