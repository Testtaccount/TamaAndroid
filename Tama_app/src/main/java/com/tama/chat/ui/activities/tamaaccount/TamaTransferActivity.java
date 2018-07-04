package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.countryCode.BaseFlagActivity.RequestType.BALANCE;
import static com.tama.chat.countryCode.BaseFlagActivity.RequestType.PHONE_NUMBER;
import static com.tama.chat.countryCode.BaseFlagActivity.RequestType.TRANSFER;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.tama.chat.R;
import com.tama.chat.countryCode.BaseFlagActivity;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.helpers.SharedHelper;

public class TamaTransferActivity extends BaseFlagActivity {

    private static final String TAG = "myLogs";
    private RequestType requestType;
    private Double balance = null;
    private boolean isPhoneAmountChecked = false;

    @Bind(R.id.amount_number_layout)
    LinearLayout amountNumberLayout;

    @Bind(R.id.body_layout)
    RelativeLayout bodyLayout;

    @Bind(R.id.transfer_button)
    Button transferButton;

    @Bind(R.id.checkbox)
    CheckBox checkBox;

    @Bind(R.id.amount_text)
    EditText amountText;

    @Bind(R.id.amount_error_text)
    TextView amountErrorText;

    @Bind(R.id.phone_error_text)
    TextView phoneErrorText;

    @Override
    protected int getContentResId() {
        return R.layout.activity_tama_transfer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTamaToolbar(R.string.transfer,R.string.balance_transfer);

        initUI();
        initCodes();
        requestType = BALANCE;
        if(isNetworkAvailable()) {
            String user_id = new SharedHelper(this).getTamaAccountId();
            new TamaAccountHelper().getBalance(this, this, user_id);
        }else{
            Toast.makeText(this, getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
        }

        amountText.addTextChangedListener(getTextChangListener());
        enterPhoneNumberText.addTextChangedListener(getTextChangListener());
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @OnClick(R.id.amount_number_layout)
    void clickOnAmountLayout(){
        if(amountText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(amountText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @OnClick(R.id.transfer_button)
    void clickOnTransferButton(){
        if(isPhoneAmountChecked){
            String user_id = new SharedHelper(this).getTamaAccountId();
            String country_code = getCountryCode();
            String transfer_to = getPhoneNumber();
            String amount = amountText.getText().toString();
            requestType = TRANSFER;
            new TamaAccountHelper().sendTrasferRequest(this,user_id,country_code,transfer_to,amount);
        }else{
            checkPhoneAndAmount();
        }
    }

    private void checkPhoneAndAmount() {
        phoneErrorText.setText("");
        amountErrorText.setText("");
        transferButton.setEnabled(false);
        requestType = PHONE_NUMBER;
        if(isNetworkAvailable()) {
            new TamaAccountHelper().checkTamaUserOrNot(this, getCountryCode(), getPhoneNumber());
        }else{
            Toast.makeText(this, getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void requestSuccess(String data) {
        super.requestSuccess(data);
        if(requestType==BALANCE){
            balance = getDouble(data);
            transferButton.setEnabled(true);
        }else if(requestType==PHONE_NUMBER){
            checkBalance();
        }else{
            createDialog(String.valueOf(getDouble(amountText.getText().toString())),getString(R.string.you_have_transfered),
                    getString(R.string.to_the_number,getFullPhoneNumber()));
        }
    }

    private void checkBalance(){
        if(balance==null){
            if(isNetworkAvailable()){
                transferButton.setEnabled(false);
                requestType = BALANCE;
                String user_id = new SharedHelper(this).getTamaAccountId();
                new TamaAccountHelper().getBalance(this, this, user_id);
            }else{
                Toast.makeText(this, getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
            }
        }else {
            if (balance >= getDouble(amountText.getText().toString())) {
                transferButton.setEnabled(false);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setText(getString(R.string.transfer_confirm_text,amountText.getText().toString(),getFullPhoneNumber()));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isPhoneAmountChecked = isChecked;
                        transferButton.setEnabled(isChecked);
                    }
                });
            } else {
                amountErrorText.setText(R.string.higher_amount);
            }
        }
    }

    @Override
    public void requestError(String data) {
        super.requestError(data);
        if(requestType==PHONE_NUMBER){
            phoneErrorText.setText(data);
            transferButton.setEnabled(true);
        }else{
            ToastUtils.longToast(data);
        }
    }

    @Override
    public void alertDialogCancelListener() {
        super.alertDialogCancelListener();
        isPhoneAmountChecked = false;
        checkBox.setChecked(false);
        checkBox.setText("");
        checkBox.setVisibility(View.GONE);
        transferButton.setEnabled(true);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    private TextWatcher getTextChangListener(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBox.setChecked(false);
                checkBox.setText("");
                checkBox.setVisibility(View.GONE);
                isPhoneAmountChecked = false;
                transferButton.setEnabled(true);
            }
        };
    }


}
