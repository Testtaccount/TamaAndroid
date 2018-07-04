package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.app.PhoneNumber.FIRST;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.tama.chat.R;
import com.tama.chat.countryCode.BaseFlagActivity;
import com.tama.chat.tamaAccount.Denominations;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaDenominationsAdapter;
import com.tama.chat.ui.fragments.chats.ContactsListFragment;
import org.json.JSONException;
import org.json.JSONObject;

public class TamaTopUpActivity extends BaseFlagActivity {

    private Denominations denominations;
    private String user_id;
    private String country_code;
    private String topup_no;
    private String euro_amount;
    private String local_amount;
    private String dest_currency;

    @Bind(R.id.tama_topup_second_page)
    RelativeLayout tamaTopupSecondPage;

    @Bind(R.id.grid_view)
    GridView gridView;

    @Bind(R.id.tama_topup_first_page)
    LinearLayout tamaTopupFirstPage;

    @Bind(R.id.topup_title)
    TextView topupTitle;

    @Bind(R.id.topup_error_text)
    TextView topupErrorText;

    @Bind(R.id.topup_info_text)
    TextView topupInfoText;

    @Bind(R.id.topup_button_first_page)
    Button topupButtonFirst;

    @Bind(R.id.topup_button_seconds_page)
    Button topupButtonSeconds;

    @Bind(R.id.open_contacts_list)
    Button openContactsList;

    @OnClick(R.id.open_contacts_list)
    public void clickOpenContactsList(){
        editNumber = true;
        setCurrentFragment(ContactsListFragment.newInstance(true, FIRST));
        tamaTopupFirstPage.setVisibility(View.GONE);
        topupTitle.setVisibility(View.GONE);
        if(mLastEnteredPhone!=null&&!mLastEnteredPhone.isEmpty()) {
            mLastEnteredPhone = null;
            setFirstPhoneNumber(enterPhoneNumberText.getText().toString());
        }
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_tama_top_up;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTamaToolbar(R.string.mobile_topup,R.string.tama_topup);

        createFirstPage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(tamaTopupFirstPage.getVisibility()==View.GONE
                && tamaTopupSecondPage.getVisibility()==View.GONE){
            tamaTopupFirstPage.setVisibility(View.VISIBLE);
            topupTitle.setVisibility(View.VISIBLE);
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

    private void createFirstPage(){
        tamaTopupSecondPage.setVisibility(View.GONE);
        tamaTopupFirstPage.setVisibility(View.VISIBLE);
        enterPhoneNumberText.setText("");
        initUI();
        initCodes();
    }

    private void createSecondPage(String data){
        denominations = getDenominationsFromJson(data);
        tamaTopupFirstPage.setVisibility(View.GONE);
        tamaTopupSecondPage.setVisibility(View.VISIBLE);
        topupTitle.setText(R.string.select_amunt);
        topupButtonSeconds.setEnabled(false);
        gridView.setAdapter(new TamaDenominationsAdapter(this,denominations));
        hideProgress();
        gridView.setOnItemClickListener(getGridViewItemClickListener());
    }

    @Override
    public void requestSuccess(String data) {
        super.requestSuccess(data);
        if(tamaTopupFirstPage.getVisibility()==View.VISIBLE){
            createSecondPage(data);
        }else{
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
                JSONObject jsonData = jsonObject.optJSONObject("data");
                if(jsonData!=null){
                    if(!jsonData.getString("balance").isEmpty()){
                        String balance = jsonData.getString("balance");
                        createDialog(String.valueOf(getDouble(balance)),getString(R.string.you_topup_done_with),"");
                    }
//                    JSONObject jsonData = jsonObject.getJSONObject("data");

                }else{
                    JSONObject jsonError = jsonObject.getJSONObject("error");
                    String errorMessage = jsonError.getString("message");
                    clearRequestData();
                    topupInfoText.setText(errorMessage);
                    topupButtonSeconds.setEnabled(false);
                }
            } catch (JSONException e) {
                clearRequestData();
                topupInfoText.setText(e.getMessage());
                topupButtonSeconds.setEnabled(false);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestError(String data) {
        super.requestError(data);
        if(tamaTopupFirstPage.getVisibility()==View.VISIBLE) {
            topupErrorText.setText(data);
            topupButtonFirst.setEnabled(true);

        }else{
            topupInfoText.setText(data);
            topupButtonSeconds.setEnabled(false);
            clearRequestData();
        }
        hideProgress();
    }

    @Override
    public void alertDialogCancelListener() {
        clearRequestData();
        topupButtonSeconds.setEnabled(false);
        topupInfoText.setText("");
        super.alertDialogCancelListener();
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    private void clearRequestData(){
        user_id = "";
        country_code = "";
        topup_no = "";
        euro_amount = "";
        local_amount = "";
        dest_currency = "";
    }

    @OnClick(R.id.topup_button_first_page)
    void onClickTopUpFirstButton(){
        if(getPhoneNumber().isEmpty()){
            topupErrorText.setText(R.string.enter_number);
        }else if(getPhoneNumber().length()>12 || getPhoneNumber().length()<4){
            topupErrorText.setText(R.string.wrong_number_quantity);
        }else{
            showProgress();
            String user_id = getTamaUserId();
            String country_code = getCountryCode();
            String topup_no = getPhoneNumber();
            topupButtonFirst.setEnabled(false);
            hideSoftKeyboard();
            new TamaAccountHelper().getDenominations(this,user_id,country_code,topup_no);//getDenominations(this,"16","91","9842499367");//
        }
    }

    @OnClick(R.id.topup_button_seconds_page)
    void onClickTopUpSecondsButton(){
//                new TamaAccountHelper().confirmTamaTopUp(this,"16","91","9842499367",euro_amount,local_amount,dest_currency);
        new TamaAccountHelper().confirmTamaTopUp(this,user_id,country_code,topup_no,euro_amount,local_amount,dest_currency);
    }

    private Denominations getDenominationsFromJson(String data) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            JSONObject jsonData = jsonObject.getJSONObject("data");
            JSONObject jsonProductList = jsonData.getJSONObject("product_list");
            JSONObject jsonRetilPriceList = jsonData.getJSONObject("retail_price_list");
            String mobile_number = jsonData.getString("mobile_number");
            String operator = jsonData.getString("operator");
            String destination_currency = jsonData.getString("destination_currency");
            String country = jsonData.getString("country");

            Denominations denominations = new Denominations(country, destination_currency,operator,mobile_number);
            for(int i = 0 ; i < jsonProductList.length(); ++i){
                denominations.productList.add(jsonProductList.getString(String.valueOf(i)));
                denominations.retilPriceList.add(jsonRetilPriceList.getString(String.valueOf(i)));
            }
            return denominations;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AdapterView.OnItemClickListener getGridViewItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str_local = ((TextView)view.findViewById(R.id.denomination_text_1)).getText().toString();
                String str_euro = ((TextView)view.findViewById(R.id.denomination_text_2)).getText().toString();
                topupInfoText.setText(getString(R.string.topup_info_text, str_euro, getFullPhoneNumber()));
                topupButtonSeconds.setEnabled(true);
                user_id = getTamaUserId();
                country_code = getCountryCode();
                topup_no = getPhoneNumber();
                euro_amount = str_euro.replace(" €","");
                dest_currency = denominations.destination_currency;
                local_amount = str_local.replace(" " + dest_currency, "");
            }
        };
    }
}
