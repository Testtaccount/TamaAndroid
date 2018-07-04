package com.tama.chat.ui.activities.tamaaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.tama.chat.R;

import butterknife.Bind;
import butterknife.OnClick;

public class MyTamaRequestActivity extends TamaAccountBaseActivity {

    @Bind(R.id.btn_my_tama_topup_rqt)
    LinearLayout btnMyTamaTopUp;

    @Bind(R.id.btn_mobile_topup_request)
    LinearLayout btnMobileTopUp;

    @Bind(R.id.btn_pay_tamaexpress_rqt)
    LinearLayout btnPayTamaExpress;

    @Bind(R.id.btn_incoming_rqt)
    LinearLayout btnIncomingRqt;

//    @Bind(R.id.btn_back)
//    LinearLayout btnBack;

    @Override
    protected int getContentResId() {
        return R.layout.activity_my_tama_request;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTamaToolbar(R.string.my_tama_request,R.string.tama_request);
    }

    @Override
    public void onResume() {
        super.onResume();
        setButtonEnable(true);
    }

    private void setButtonEnable(boolean bool){
        btnMyTamaTopUp.setEnabled(bool);
        btnMobileTopUp.setEnabled(bool);
        btnPayTamaExpress.setEnabled(bool);
        btnIncomingRqt.setEnabled(bool);
//        btnBack.setEnabled(bool);
    }

    @OnClick(R.id.btn_my_tama_topup_rqt)
    void startMytamaTopUpRqt(){
        setButtonEnable(false);
        Intent intent = new Intent(this,MyTamaAccountTopUpRqtActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_mobile_topup_request)
    void startMyTamaMobileTopUpRequestActivity(){
        setButtonEnable(false);
        Intent intent = new Intent(this,MyTamaMobileTopUpRequestActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_pay_tamaexpress_rqt)
    void startPayTamaExpressRequestActivity(){
        setButtonEnable(false);
        Intent intent = new Intent(this,PayTamaExpressRequestActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_incoming_rqt)
    void startIncomingRequestActivity(){
        setButtonEnable(false);
        Intent intent = new Intent(this,IncomingRequestActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

//    @OnClick(R.id.btn_back)
//    void clickOnBackButton(){
//        onBackPressed();
//    }


}
