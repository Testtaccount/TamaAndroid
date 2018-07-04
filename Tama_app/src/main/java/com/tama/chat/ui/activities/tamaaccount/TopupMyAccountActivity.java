package com.tama.chat.ui.activities.tamaaccount;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;

public class TopupMyAccountActivity extends TamaAccountBaseActivity implements TamaAccountHelperListener {

    private double currentBalance = 0, topupBalance =0;

    @Bind(R.id.topup_voucher_text)
    EditText topupVoucherText;

    @Bind(R.id.topup_error_text)
    TextView topupErrorText;

    @Bind(R.id.topup_button)
    Button topupButton;

    @Override
    protected int getContentResId() {
        return R.layout.activity_topup_my_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFields();
        setTamaToolbar(R.string.account_topup,R.string.topup_my_account_one_line);
    }

    @OnClick(R.id.topup_button)
    public void sendTopupRequest(){
        topupErrorText.setText("");
        if(checkNumberCount()) {
            if(isNetworkAvailable()){
                topupButton.setEnabled(false);
                new TamaAccountHelper().sendTopupRequest(this,this, topupVoucherText.getText().toString());
            }else{
                topupErrorText.setText(getString(R.string.no_internet_conection));
            }
            hideSoftKeyboard();
        }else{
            topupErrorText.setText(getString(R.string.number_count_error));
        }
    }

    private void initFields(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String message = bundle.getString(CURRENT_BALANCE);
            currentBalance = getDouble(message);
        }
    }

    private boolean checkNumberCount(){
        String number = topupVoucherText.getText().toString();
        return number.length()==9;
    }

    @Override
    public void requestSuccess(String data) {
        topupBalance = getDouble(data) - currentBalance;
        createDialog(String.valueOf(topupBalance),getString(R.string.d_m_account_with),"");
        currentBalance = getDouble(data);
        topupVoucherText.setText("");
        topupButton.setEnabled(true);
    }

    @Override
    public void requestError(String data) {
        topupErrorText.setText(data);
        topupButton.setEnabled(true);
    }

    @Override
    public void alertDialogCancelListener() {
        super.alertDialogCancelListener();
        topupVoucherText.setText("");
    }

    @Override
    public Context getAppContext() {
        return this;
    }
}
