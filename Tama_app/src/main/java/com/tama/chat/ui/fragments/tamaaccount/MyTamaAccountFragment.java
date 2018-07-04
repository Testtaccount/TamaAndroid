package com.tama.chat.ui.fragments.tamaaccount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.authorization.AuthenticationActivity;
import com.tama.chat.ui.activities.tamaaccount.ContactUsActivity;
import com.tama.chat.ui.activities.tamaaccount.MyTamaRequestActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaHistoryActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaTopUpActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaTransferActivity;
import com.tama.chat.ui.activities.tamaaccount.TopupMyAccountActivity;
import com.tama.chat.ui.fragments.base.BaseFragment;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.helpers.SharedHelper;

/**
 * Created by Avo on 12-May-17.
 */

public class MyTamaAccountFragment extends BaseFragment implements TamaAccountHelperListener {

    private String balance = "0.0";

    @Bind(R.id.tama_account_balance)
    TextView tamaAccountBalance;

    @Bind(R.id.button_topup_myacc)
    LinearLayout buttonTopupMyAcc;

    @Bind(R.id.button_tama_history)
    LinearLayout buttonTamaHistory;

    @Bind(R.id.button_tama_topup)
    LinearLayout buttonTamaTopUp;

    @Bind(R.id.button_send_credit)
    LinearLayout buttonSendCredit;

    @Bind(R.id.button_tama_request)
    LinearLayout buttonTamaRequest;

    @Bind(R.id.button_tama_express)
    LinearLayout buttonTamaExpress;

    @Bind(R.id.button_tama_contact_us)
    LinearLayout buttonTamaContactUs;



    public static MyTamaAccountFragment newInstance() {
        MyTamaAccountFragment fragment = new MyTamaAccountFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tama_account, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void setButtonEnable(boolean bool){
//        buttonTopupMyAcc.setEnabled(bool);
//        if(new SharedHelper(baseActivity).getTamaIsEurope()) {
            buttonTopupMyAcc.setEnabled(bool);
            buttonTamaTopUp.setEnabled(bool);

//        }else{
//            buttonTopupMyAcc.setAlpha(0.5f);
//            buttonTopupMyAcc.setEnabled(false);
//            buttonTamaTopUp.setAlpha(0.5f);
//            buttonTamaTopUp.setEnabled(false);
//        }
        buttonTamaHistory.setEnabled(bool);
        buttonSendCredit.setEnabled(bool);
        buttonTamaRequest.setEnabled(bool);
        buttonTamaExpress.setEnabled(bool);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isNetworkAvailable()) {
            updateBalance();
        }else{
            Toast.makeText(app, app.getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
        }
        setButtonEnable(true);
    }

    private void updateBalance() {
        String user_id = new SharedHelper(baseActivity).getTamaAccountId();
        new TamaAccountHelper().getBalance(this.getContext(),this, user_id);
    }

    protected void createMessageDialog(String textMessage, String title, String subTitle){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.topup_dialog_view, null);
        dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        TextView textTitle = (TextView) dialogView.findViewById(R.id.topup_dialog_title_text);
        TextView textBig = (TextView) dialogView.findViewById(R.id.topup_dialog_big_text);
        TextView textView = (TextView) dialogView.findViewById(R.id.topup_dialog_subtitle_text);
        Button topupDialogButton = (Button)dialogView.findViewById(R.id.topup_dialog_button);

        textBig.setText(textMessage);
        textBig.setTextSize(18);
        textBig.setTextColor(Color.BLACK);
        topupDialogButton.setText(getString(R.string.ok));
        topupDialogButton.setBackground( ContextCompat.getDrawable(getActivity(),R.drawable.selector_button_red_oval));

        topupDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }


    @OnClick(R.id.button_topup_myacc)
    public void startTopupMyAccountActivity(){
        if(!new SharedHelper(baseActivity).getTamaIsEurope()){
            createMessageDialog(getString(R.string.message_available_european_users),"","");
            return;
        }
        setButtonEnable(false);
        Intent intent = new Intent(baseActivity,TopupMyAccountActivity.class);
        intent.putExtra(CURRENT_BALANCE, balance);
        startActivity(intent);
    }

    @OnClick(R.id.button_tama_history)
    public void startTamaHistoryActivity(){
        setButtonEnable(false);
        Intent intent = new Intent(baseActivity,TamaHistoryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_tama_topup)
    public void startTamaTopUpActivity(){
        if(!new SharedHelper(baseActivity).getTamaIsEurope()){
            createMessageDialog("These options are only available to European users","","");
            return;
        }
        setButtonEnable(false);
        Intent intent = new Intent(baseActivity,TamaTopUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_send_credit)
    public void startTamaBalanceTransfer(){
        setButtonEnable(false);
        Intent intent = new Intent(baseActivity,TamaTransferActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_tama_request)
    public void startRequestActivity(){
        setButtonEnable(false);
        Intent intent = new Intent(baseActivity,MyTamaRequestActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_tama_express)
    public void startTamaExpressAcitvity(){
        setButtonEnable(false);
        Intent intent = new Intent(baseActivity,TamaExpressActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_tama_contact_us)
    public void startTamaContactUs(){
        setButtonEnable(false);
        Intent intent = new Intent(baseActivity,ContactUsActivity.class);
        startActivity(intent);
    }

    @Override
    public void requestSuccess(String data) {
        if(data!=null&&tamaAccountBalance!=null) {
            tamaAccountBalance.setText(data);
            balance = data;
        }
    }

    @Override
    public void requestError(String data) {
        ToastUtils.longToast(data);
    }

    @Override
    public void alertDialogCancelListener() {

    }

    @Override
    public Context getAppContext() {
        return getContext();
    }
}
