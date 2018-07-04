package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.app.PhoneNumber.FIRST;
import static com.tama.chat.countryCode.BaseFlagActivity.RequestType.PHONE_NUMBER;
import static com.tama.chat.countryCode.BaseFlagActivity.RequestType.SEND_REQUEST;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.tama.chat.R;
import com.tama.chat.countryCode.BaseFlagActivity;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.ui.fragments.chats.ContactsListFragment;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.helpers.SharedHelper;

public class PayTamaExpressRequestActivity extends BaseFlagActivity {

    private boolean isPhoneAmountChecked = false;
    private TamaTransferActivity.RequestType requestType;

    @Bind(R.id.btn_send_request)
    Button btnSendRequest;

    @Bind(R.id.tama_express_text)
    EditText tamaExpressText;

    @Bind(R.id.phone_error_text)
    TextView phoneErrorText;

    @Bind(R.id.tama_express_error_text)
    TextView tamaExpressErrorText;

    @Bind(R.id.checkbox)
    CheckBox checkBox;

    @Bind(R.id.open_contacts_list)
    Button openContactsList;

    @Bind(R.id.body_layout)
    RelativeLayout bodyLayout;

    @OnClick(R.id.open_contacts_list)
    public void clickOpenContactsList(){
        editNumber = true;
        setCurrentFragment(ContactsListFragment.newInstance(true, FIRST));
        bodyLayout.setVisibility(View.GONE);
        if(mLastEnteredPhone!=null&&!mLastEnteredPhone.isEmpty()) {
            mLastEnteredPhone = null;
            setFirstPhoneNumber(enterPhoneNumberText.getText().toString());
        }
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_pay_tama_express_request;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTamaToolbar(R.string.pay_tama_express_rqt,R.string.tama_request);
        initUI();
        initCodes();

        enterPhoneNumberText.addTextChangedListener(getTextChangListener());
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
        if(editNumber && getFirstNumber()!=null&&!getFirstNumber().isEmpty()){
            mLastEnteredPhone =getFirstNumber();
            enterPhoneNumberText.setText(mLastEnteredPhone);
            textWatcher.setTextToTextEdit(mLastEnteredPhone);
            textWatcher.setEditabale();
            editNumber = false;
        }
    }

    @OnClick(R.id.btn_send_request)
    void clickOnSendRequestButton(){
        if(isPhoneAmountChecked){
            String user_id = new SharedHelper(this).getTamaAccountId();
            String country_code = getCountryCode();
            String payer_no = getPhoneNumber();
            String tama_pin = tamaExpressText.getText().toString();
            requestType = SEND_REQUEST;
            new TamaAccountHelper().sendPayTamaExpressRqt(this,user_id,country_code,payer_no,tama_pin);
        }else{
            checkPhoneAndAmount();
        }
    }

    private void checkPhoneAndAmount() {
        phoneErrorText.setText("");
        tamaExpressErrorText.setText("");
        btnSendRequest.setEnabled(false);
        requestType = PHONE_NUMBER;
        String user_phone_number = new SharedHelper(this).getTamaAccountPhoneNumber();
        if(user_phone_number!=null&&user_phone_number.equals(getCountryCode()+getPhoneNumber())){
            phoneErrorText.setText(R.string.its_must_not_your_number);
            return;
        }
        if(isNetworkAvailable()) {
            new TamaAccountHelper().checkTamaUserOrNot(this, getCountryCode(), getPhoneNumber());
        }else{
            Toast.makeText(this, getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void requestSuccess(String data) {
        super.requestSuccess(data);
        if(requestType==PHONE_NUMBER){
            checkBalance();
        }else{
            createDialog("",data+"\n"+tamaExpressText.getText().toString(),
                    getString(R.string.from_the_number,getFullPhoneNumber()));
        }
    }

    private void checkBalance(){
        btnSendRequest.setEnabled(false);
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setText(getString(R.string.tama_express_confirm_text,getFullPhoneNumber()));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPhoneAmountChecked = isChecked;
                btnSendRequest.setEnabled(isChecked);
            }
        });
    }

    @Override
    public void requestError(String data) {
        super.requestError(data);
        if(requestType==PHONE_NUMBER){
            phoneErrorText.setText(data);
            btnSendRequest.setEnabled(true);
        }else if(requestType==SEND_REQUEST){
            tamaExpressErrorText.setText(data);
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
        btnSendRequest.setEnabled(true);
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
                btnSendRequest.setEnabled(true);
            }
        };
    }
}
