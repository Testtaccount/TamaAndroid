package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.app.PhoneNumber.FIRST;
import static com.tama.chat.app.PhoneNumber.SECOND;
import static com.tama.chat.countryCode.BaseMultiFlagActivity.RequestType.PHONE_NUMBER_FIRST;
import static com.tama.chat.countryCode.BaseMultiFlagActivity.RequestType.PHONE_NUMBER_SECOND;
import static com.tama.chat.countryCode.BaseMultiFlagActivity.RequestType.SEND_REQUEST;

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
import com.tama.chat.countryCode.BaseMultiFlagActivity;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.ui.fragments.chats.ContactsListFragment;
import com.tama.chat.utils.helpers.SharedHelper;

public class MyTamaMobileTopUpRequestActivity extends BaseMultiFlagActivity {

    private boolean isPhoneAmountChecked = false;
    private RequestType requestType;

    @Bind(R.id.amount_number_layout)
    LinearLayout amountNumberLayout;

    @Bind(R.id.body_layout)
    RelativeLayout bodyLayout;

    @Bind(R.id.btn_send_request)
    Button btnSendRequest;

    @Bind(R.id.amount_text)
    EditText amountText;

    @Bind(R.id.phone_error_text_first)
    TextView phoneErrorTextFirst;

    @Bind(R.id.phone_error_text_second)
    TextView phoneErrorTextSecond;

    @Bind(R.id.amount_error_text)
    TextView amountErrorText;

    @Bind(R.id.checkbox)
    CheckBox checkBox;

    @Bind(R.id.open_contacts_list_first)
    Button openContactsListFirst;

    @Bind(R.id.open_contacts_list_second)
    Button openContactsListSecond;

    @OnClick(R.id.open_contacts_list_first)
    public void clickOpenContactsListFirst(){
        editFirstNumber = true;
        editSecondNumber = true;
        setCurrentFragment(ContactsListFragment.newInstance(true,FIRST));
        bodyLayout.setVisibility(View.GONE);
        if(mLastEnteredPhoneFirst!=null&&!mLastEnteredPhoneFirst.isEmpty()) {
            mLastEnteredPhoneFirst = null;
            setFirstPhoneNumber(enterPhoneNumberTextFirst.getText().toString());
        }
        if(mLastEnteredPhoneSecond!=null&&!mLastEnteredPhoneSecond.isEmpty()) {
            mLastEnteredPhoneSecond = null;
            setSecondPhoneNumber(enterPhoneNumberTextSecond.getText().toString());
        }
    }

    @OnClick(R.id.open_contacts_list_second)
    public void clickOpenContactsListSecond(){
        editFirstNumber = true;
        editSecondNumber = true;

        setCurrentFragment(ContactsListFragment.newInstance(true,SECOND));
        bodyLayout.setVisibility(View.GONE);
        if(mLastEnteredPhoneFirst!=null&&!mLastEnteredPhoneFirst.isEmpty()) {
            mLastEnteredPhoneFirst = null;
            setFirstPhoneNumber(enterPhoneNumberTextFirst.getText().toString());
        }
        if(mLastEnteredPhoneSecond!=null&&!mLastEnteredPhoneSecond.isEmpty()) {
            mLastEnteredPhoneSecond = null;
            setSecondPhoneNumber(enterPhoneNumberTextSecond.getText().toString());
        }
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_my_tama_mobile_top_up_request;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTamaToolbar(R.string.topup_my_mobile_request,R.string.tama_request);

        initUI();
        initCodes();

//        amountText.addTextChangedListener(getTextChangListener());
        enterPhoneNumberTextFirst.addTextChangedListener(getTextChangListener());
        enterPhoneNumberTextSecond.addTextChangedListener(getTextChangListener());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(bodyLayout.getVisibility()==View.GONE){
            bodyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(editFirstNumber && getFirstNumber()!=null&&!getFirstNumber().isEmpty()) {
            mLastEnteredPhoneFirst = getFirstNumber();
            enterPhoneNumberTextFirst.setText(mLastEnteredPhoneFirst);
            textWatcherFirst.setTextToTextEdit(mLastEnteredPhoneFirst);
            textWatcherFirst.setEditabale();
            editFirstNumber = false;
        }
        if(editSecondNumber && getSecondNumber()!=null&&!getSecondNumber().isEmpty()){
            mLastEnteredPhoneSecond =getSecondNumber();
            enterPhoneNumberTextSecond.setText(mLastEnteredPhoneSecond);
            textWatcherSecond.setTextToTextEdit(mLastEnteredPhoneSecond);
            textWatcherSecond.setEditabale();
            editSecondNumber = false;
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @OnClick(R.id.amount_number_layout)
    void clickOnAmountLayout(){
        if(amountText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(amountText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @OnClick(R.id.btn_send_request)
    void clickOnSendRequestButton(){
        if(isPhoneAmountChecked){
            String user_id = new SharedHelper(this).getTamaAccountId();
            String payer_country_code = getCountryCodeFirst();
            String payer_no = getPhoneNumberFirst();
            String topup_country_code = getCountryCodeSecond();
            String topup_no = getPhoneNumberSecond();
            String amount = amountText.getText().toString();
            requestType = SEND_REQUEST;
            new TamaAccountHelper().sendMyTamaMobileTopUpRqt(this,user_id,payer_country_code,payer_no,topup_country_code,topup_no,amount);
        }else{
            checkPhoneAndAmount();
        }
    }

    private void checkPhoneAndAmount() {
        phoneErrorTextFirst.setText("");
        phoneErrorTextSecond.setText("");
        amountErrorText.setText("");
        btnSendRequest.setEnabled(false);
        requestType = PHONE_NUMBER_FIRST;
        String user_phone_number = new SharedHelper(this).getTamaAccountPhoneNumber();
        if(user_phone_number!=null&&user_phone_number.equals(getCountryCodeFirst()+getPhoneNumberFirst())){
            phoneErrorTextFirst.setText(R.string.its_must_not_your_number);
            return;
        }
        if(isNetworkAvailable()) {
            new TamaAccountHelper().checkTamaUserOrNot(this, getCountryCodeFirst(), getPhoneNumberFirst());
        }else{
            Toast.makeText(this, getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void requestSuccess(String data) {
        super.requestSuccess(data);
        if(requestType==PHONE_NUMBER_FIRST){
            if(isNetworkAvailable()) {
                requestType = PHONE_NUMBER_SECOND;
                new TamaAccountHelper().checkTamaUserOrNot(this, getCountryCodeSecond(), getPhoneNumberSecond());
            }else{
                Toast.makeText(this, getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
            }
        } else if(requestType==PHONE_NUMBER_SECOND){
            checkBalance();
        }else{
            createDialog(amountText.getText().toString(),data, getString(R.string.to_the_number,getFullPhoneNumberFirst()));
        }
    }

    @Override
    public void requestError(String data) {
        super.requestError(data);
        if(requestType==PHONE_NUMBER_FIRST){
            phoneErrorTextFirst.setText(data);
            btnSendRequest.setEnabled(true);
        }else if(requestType==PHONE_NUMBER_SECOND){
            phoneErrorTextSecond.setText(data);
            btnSendRequest.setEnabled(true);
        }else {
            amountErrorText.setText(data);
        }
    }

    @Override
    public void alertDialogCancelListener() {
        super.alertDialogCancelListener();
        isPhoneAmountChecked = false;
        checkBox.setChecked(false);
        checkBox.setText("");
        checkBox.setVisibility(View.GONE);
        btnSendRequest.setEnabled(true);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    private void checkBalance(){
        btnSendRequest.setEnabled(false);
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setText(getString(R.string.confirm_text_topup_my_mobile_request,amountText.getText().toString(),getFullPhoneNumberFirst(),getFullPhoneNumberSecond()));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPhoneAmountChecked = isChecked;
                btnSendRequest.setEnabled(isChecked);
            }
        });
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
                btnSendRequest.setEnabled(true);
            }
        };
    }
}
