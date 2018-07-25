package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.app.PhoneNumber.FIRST;
import static com.tama.chat.tamaAccount.TamaAccountHelper.parse;
import static com.tama.chat.ui.activities.tamaaccount.TamaSingleHistoryActivity.EXTRA_HISTORY_SINGLE_ELEMENT_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.countryCode.BaseFlagActivity;
import com.tama.chat.tamaAccount.Denominations;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaDenominationsAdapter;
import com.tama.chat.ui.fragments.chats.ContactsListFragment;
import com.tama.chat.utils.KeyboardUtils;
import com.tama.chat.utils.ToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class TamaTopUpActivity extends BaseFlagActivity implements OnItemClickListener {

  private Denominations denominations;
  private String country_code;
  private String topup_no;
  private String euro_amount;
  private String local_amount;
  private String dest_currency;
  private boolean usePromo;
  private View selectedView;
  private long historyId;



  @Bind(R.id.country_code_spinner)
  Spinner mSpinner;

  @Bind(R.id.enter_phone_number_text)
  EditText mEditText;

  @Bind(R.id.tama_topup_first_page)
  LinearLayout tamaTopupFirstPage;

  @Bind(R.id.tama_topup_second_page)
  RelativeLayout tamaTopupSecondPage;

  @Bind(R.id.topup_title)
  TextView topupTitle;

  @Bind(R.id.topup_error_text)
  TextView topupErrorText;

  @Bind(R.id.topup_info_text)
  TextView topupInfoText;

  @Bind(R.id.grid_view)
  GridView gridView;

  @Bind(R.id.topup_button_first_page)
  Button topupButtonFirst;

  @Bind(R.id.topup_button_seconds_page)
  Button topupButtonSeconds;

  @Bind(R.id.open_contacts_list)
  Button openContactsList;

  @Bind(R.id.checkbox_promo)
  CheckBox promoCheckBox;

  @Override
  protected int getContentResId() {
    return R.layout.activity_tama_top_up;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setTamaToolbar(R.string.mobile_topup, R.string.tama_topup);
    createFirstPage();
    gridView.setOnItemClickListener(this);
//    mSpinner.setVisibility(View.GONE);
//    mEditText.setText("+");
//    mEditText.addTextChangedListener(new TextWatcher() {
//      @Override
//      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//      }
//
//      @Override
//      public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//      }
//
//      @Override
//      public void afterTextChanged(Editable s) {
//        mSpinner.setVisibility(View.VISIBLE);
//      }
//    });
  }

  @Override
  public void onResume() {
    super.onResume();
    if (editNumber && getFirstNumber() != null && !getFirstNumber().isEmpty()) {
      mLastEnteredPhone = getFirstNumber();
      enterPhoneNumberText.setText(mLastEnteredPhone);
      textWatcher.setTextToTextEdit(mLastEnteredPhone);
      textWatcher.setEditabale();
      editNumber = false;
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (tamaTopupFirstPage.getVisibility() == View.GONE
        && tamaTopupSecondPage.getVisibility() == View.GONE) {
      tamaTopupFirstPage.setVisibility(View.VISIBLE);
      topupTitle.setVisibility(View.VISIBLE);
    }
  }

  private void createFirstPage() {
    tamaTopupSecondPage.setVisibility(View.GONE);
    tamaTopupFirstPage.setVisibility(View.VISIBLE);
    enterPhoneNumberText.setText("");
    initUI();
    initCodes();
  }

  public String removeFirstChar(String s){
    return s.substring(1);
  }

  private void createSecondPage(String mobile_number, String country, String operator,
      String destination_currency, List<String> product_list, List<String> retail_price_list) {
    //    promoCheckBox.setVisibility(View.VISIBLE);
    String promoBalance= App.getInstance().getAppSharedHelper().getPromoTamatopupBalance();
    if (promoCheckBox != null && getDouble(removeFirstChar(promoBalance))!=0) {
      promoCheckBox.setVisibility(View.VISIBLE);
      promoCheckBox.setText("Use My Tama Promo Balance "+ promoBalance);
    }else {
      promoCheckBox.setVisibility(View.GONE);
      promoCheckBox.setText("");
    }
    denominations = getDenominationsFromJson(mobile_number,country,operator,destination_currency,product_list,retail_price_list);
    tamaTopupFirstPage.setVisibility(View.GONE);
    tamaTopupSecondPage.setVisibility(View.VISIBLE);
    topupTitle.setText(R.string.select_amunt);
    topupButtonSeconds.setEnabled(false);
    gridView.invalidateViews();
    final TamaDenominationsAdapter tamaDenominationsAdapter=new TamaDenominationsAdapter(this, denominations);
    gridView.setAdapter(tamaDenominationsAdapter);

//    gridView.post(new Runnable() {
//      @Override
//      public void run() {
//
//        View child = gridView.getChildAt(0);
//        child.setSelected(true);
//        onItemClick(gridView,child,0,0);
//        onItemClick(gridView,child,0,0);
//
//      }
//    });

    hideProgress();
  }

  @OnClick(R.id.topup_button_first_page)
  void onClickTopUpFirstButton() {
    if (!isNetworkAvailable()) {
      ToastUtils.longToast(R.string.no_internet_conection);
      return;
    }

    if (getPhoneNumber().isEmpty()) {
      topupErrorText.setText(R.string.enter_number);
    } else if (getPhoneNumber().length() > 12 || getPhoneNumber().length() < 4) {
      topupErrorText.setText(R.string.wrong_number_quantity);
    } else {
      showProgress();
      String country_code = getCountryCode();
      String topup_no = getPhoneNumber();
      topupButtonFirst.setEnabled(false);
      hideSoftKeyboard();            //////////////////////////////////////////////////////
      new TamaAccountHelper().getDenominations(this,topup_no, country_code);
//      new TamaAccountHelper().getDenominations(this,"8122535488", "91");
    }
  }

  @OnClick(R.id.topup_button_seconds_page)
  void onClickTopUpSecondsButton() {
    if (!isNetworkAvailable()) {
      ToastUtils.longToast(R.string.no_internet_conection);
      return;
    }
    showProgress();
    String use_promo = usePromo ? "yes" : "no";
    String pay_by = "balance";

    if (getDouble(local_amount) < getDouble(App.getInstance().getAppSharedHelper().getMinOrderAmountTamatopup())) {
      ToastUtils.longToast("The minimum amount that you can topup is " + App.getInstance().getAppSharedHelper().getMinOrderAmountTamatopup());
      return;
    }
    new TamaAccountHelper().confirmTamaTopUp(this, topup_no, country_code, euro_amount, local_amount, dest_currency, pay_by, use_promo);
  }

  @OnCheckedChanged(R.id.checkbox_promo)
  public void usePromoCheckBoxCheck(CompoundButton button, boolean checked) {
    if (checked) {
      usePromo = true;
    } else {
      usePromo = false;
    }
  }

  private Denominations getDenominationsFromJson(String mobile_number, String country,
      String operator, String destination_currency, List<String> product_list,
      List<String> retail_price_list) {

      Denominations denominations = new Denominations(mobile_number,country, operator, destination_currency);
        denominations.product_list.addAll(product_list);
        denominations.retail_price_list.addAll(retail_price_list);

      return denominations;
  }

  private List<String> getListFromSubJson(JSONObject data) throws JSONException {
    List<String> items = new ArrayList<>();
      for (int i = 0; i < data.length(); i++) {
        String s = data.getString(String.valueOf(i));
        items.add(s);
      }
    return items;
  }

  @OnClick(R.id.open_contacts_list)
  public void clickOpenContactsList() {
    KeyboardUtils.hideKeyboard(this);
    editNumber = true;
    setCurrentFragment(ContactsListFragment.newInstance(true, FIRST));
    tamaTopupFirstPage.setVisibility(View.GONE);
    topupTitle.setVisibility(View.GONE);
    if (mLastEnteredPhone != null && !mLastEnteredPhone.isEmpty()) {
      mLastEnteredPhone = null;
      setFirstPhoneNumber(enterPhoneNumberText.getText().toString());
    }
  }

  @Override
  public void alertDialogCancelListener() {
    clearRequestData();
    topupButtonSeconds.setEnabled(false);
//    super.alertDialogCancelListener();
    openSingleHistoryActivity(historyId);
    finish();
  }

  private void clearRequestData() {
    country_code = "";
    topup_no = "";
    euro_amount = "";
    local_amount = "";
    dest_currency = "";
  }

  private void openSingleHistoryActivity(long historyId) {
    Intent intent=new Intent(this, TamaSingleHistoryActivity.class);
    intent.putExtra(EXTRA_HISTORY_SINGLE_ELEMENT_ID,String.valueOf(historyId));
    startActivity(intent);
  }

  @Override
  public void requestSuccess(String data) {
    super.requestSuccess(data);
    String message = null;
    String mobile_number=null;
    String country=null;
    String operator=null;
    String destination_currency=null;
    List<String> product_list=null;
    List<String> retail_price_list=null;
    String balance=null;
    String history_id=null;

    JSONObject object = null;
      Map<String, String> map = new HashMap<>();
      try {
        object = new JSONObject(data);
        JSONObject jsonObject = object.getJSONObject("data");
        parse(jsonObject, map);



      String code = map.get("code");
      String http_code = map.get("http_code");
        message = map.get("message");

        mobile_number = map.get("mobile_number");
        country = map.get("country");
        operator = map.get("operator");
        destination_currency = map.get("destination_currency");


        balance = map.get("balance");
        history_id = map.get("history_id");
        if(history_id!=null) {
          historyId= Long.parseLong(history_id);
        }
        JSONObject json_object_product_list = jsonObject.getJSONObject("result").getJSONObject("product_list");
      JSONObject json_object_retail_price_list = jsonObject.getJSONObject("result").getJSONObject("retail_price_list");
        product_list = getListFromSubJson(json_object_product_list);
        retail_price_list = getListFromSubJson(json_object_retail_price_list);
  } catch (JSONException e) {
    e.printStackTrace();
  }


      if (tamaTopupFirstPage.getVisibility() == View.VISIBLE) {
      createSecondPage(mobile_number,country,operator,destination_currency,product_list,retail_price_list);
    } else {
        if(balance!=null){
          createDialog("",getString(R.string.you_topup_done_with),"");
          topupInfoText.setText("");
        } else {
          clearRequestData();
          topupInfoText.setText(message);
          topupButtonSeconds.setEnabled(false);
        }
        hideProgress();

      }
    if (selectedView!=null) {
      selectedView.setSelected(false);
    }

  }

  @Override
  public void requestError(String message) {
    super.requestError(message);
    if (tamaTopupFirstPage.getVisibility() == View.VISIBLE) {
      topupErrorText.setText(message);
      topupButtonFirst.setEnabled(true);

    } else {
      topupInfoText.setText(message);
      topupButtonSeconds.setEnabled(false);
      clearRequestData();
    }

    if (selectedView!=null) {
      selectedView.setSelected(false);
    }
    hideProgress();
  }

  @Override
  public Context getAppContext() {
    return this;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    view.setSelected(true);
    this.selectedView=view;
    TextView textView1=view.findViewById(R.id.denomination_text_1);
    TextView textView2=view.findViewById(R.id.denomination_text_2);
//        textView1.setTextColor(getResources().getColor(R.color.white));
//        textView2.setTextColor(getResources().getColor(R.color.white));
    String str_local = textView1.getText().toString();
    String str_euro =textView2.getText().toString();
    topupInfoText.setText(getString(R.string.topup_info_text, str_euro, getFullPhoneNumber()));
    topupButtonSeconds.setEnabled(true);
    country_code = getCountryCode();
    topup_no = getPhoneNumber();
    euro_amount = str_euro.replace(" €", "");
    dest_currency = denominations.destination_currency;
    local_amount = str_local.replace(" " + dest_currency, "");
  }
}

/*package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.app.PhoneNumber.FIRST;
import static com.tama.chat.rest.util.APIUtil.getLanguages;
import static com.tama.chat.tamaAccount.TamaAccountHelper.parse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.countryCode.BaseFlagActivity;
import com.tama.chat.rest.HttpRequestManager;
import com.tama.chat.rest.Logger;
import com.tama.chat.rest.RestHttpClient.RequestMethod;
import com.tama.chat.rest.entity.HttpConnection;
import com.tama.chat.rest.util.APIUtil;
import com.tama.chat.rest.util.PostEntityUtil;
import com.tama.chat.tamaAccount.Denominations;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaDenominationsAdapter;
import com.tama.chat.ui.fragments.chats.ContactsListFragment;
import com.tama.chat.utils.ToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class TamaTopUpActivity extends BaseFlagActivity implements OnItemClickListener {

  private Denominations denominations;
  private String country_code;
  private String topup_no;
  private String euro_amount;
  private String local_amount;
  private String dest_currency;
  private boolean usePromo;
  private View selectedView;
  private long historyId;

  @Bind(R.id.tama_topup_first_page)
  LinearLayout tamaTopupFirstPage;

  @Bind(R.id.tama_topup_second_page)
  RelativeLayout tamaTopupSecondPage;

  @Bind(R.id.topup_title)
  TextView topupTitle;

  @Bind(R.id.topup_error_text)
  TextView topupErrorText;

  @Bind(R.id.topup_info_text)
  TextView topupInfoText;

  @Bind(R.id.grid_view)
  GridView gridView;

  @Bind(R.id.topup_button_first_page)
  Button topupButtonFirst;

  @Bind(R.id.topup_button_seconds_page)
  Button topupButtonSeconds;

  @Bind(R.id.open_contacts_list)
  Button openContactsList;

  @Bind(R.id.checkbox_promo)
  CheckBox promoCheckBox;

  @Override
  protected int getContentResId() {
    return R.layout.activity_tama_top_up;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setTamaToolbar(R.string.mobile_topup, R.string.tama_topup);

    createFirstPage();
    gridView.setOnItemClickListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    if (editNumber && getFirstNumber() != null && !getFirstNumber().isEmpty()) {
      mLastEnteredPhone = getFirstNumber();
      enterPhoneNumberText.setText(mLastEnteredPhone);
      textWatcher.setTextToTextEdit(mLastEnteredPhone);
      textWatcher.setEditabale();
      editNumber = false;
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (tamaTopupFirstPage.getVisibility() == View.GONE
        && tamaTopupSecondPage.getVisibility() == View.GONE) {
      tamaTopupFirstPage.setVisibility(View.VISIBLE);
      topupTitle.setVisibility(View.VISIBLE);
    }
  }

  private void createFirstPage() {
    tamaTopupSecondPage.setVisibility(View.GONE);
    tamaTopupFirstPage.setVisibility(View.VISIBLE);
    enterPhoneNumberText.setText("");
    initUI();
    initCodes();
  }

  public String removeFirstChar(String s){
    return s.substring(1);
  }

  private void createSecondPage(String mobile_number, String country, String operator,
      String destination_currency, List<String> product_list, List<String> retail_price_list) {
    //    promoCheckBox.setVisibility(View.VISIBLE);
    String promoBalance= App.getInstance().getAppSharedHelper().getPromoTamatopupBalance();
    if (promoCheckBox != null && getDouble(removeFirstChar(promoBalance))!=0) {
      promoCheckBox.setVisibility(View.VISIBLE);
      promoCheckBox.setText("Use My Tama Promo Balance "+ promoBalance);
    }else {
      promoCheckBox.setVisibility(View.GONE);
      promoCheckBox.setText("");
    }
    denominations = getDenominationsFromJson(mobile_number,country,operator,destination_currency,product_list,retail_price_list);
    tamaTopupFirstPage.setVisibility(View.GONE);
    tamaTopupSecondPage.setVisibility(View.VISIBLE);
    topupTitle.setText(R.string.select_amunt);
    topupButtonSeconds.setEnabled(false);
    gridView.invalidateViews();
    final TamaDenominationsAdapter tamaDenominationsAdapter=new TamaDenominationsAdapter(this, denominations);
    gridView.setAdapter(tamaDenominationsAdapter);

//    gridView.post(new Runnable() {
//      @Override
//      public void run() {
//
//        View child = gridView.getChildAt(0);
//        child.setSelected(true);
//        onItemClick(gridView,child,0,0);
//        onItemClick(gridView,child,0,0);
//
//      }
//    });

    hideProgress();
  }

  @OnClick(R.id.topup_button_first_page)
  void onClickTopUpFirstButton() {
//    if (getPhoneNumber().isEmpty()) {
//      topupErrorText.setText(R.string.enter_number);
//    } else if (getPhoneNumber().length() > 12 || getPhoneNumber().length() < 4) {
//      topupErrorText.setText(R.string.wrong_number_quantity);
//    } else {
      showProgress();
      String user_id = getTamaUserId();
      String country_code = getCountryCode();
      String topup_no = getPhoneNumber();
      topupButtonFirst.setEnabled(false);
      hideSoftKeyboard();            //////////////////////////////////////////////////////
    new GetDenominationsAsyncTask().execute(topup_no, country_code);

      new TamaAccountHelper().getDenominations(this,topup_no, country_code);
//      new TamaAccountHelper().getDenominations(this,"8122535488", "91");
//    }
  }

  @OnClick(R.id.topup_button_seconds_page)
  void onClickTopUpSecondsButton() {
    showProgress();

    String use_promo = usePromo ? "yes" : "no";
    String pay_by = "balance";

    if (getDouble(local_amount) < getDouble(App.getInstance().getAppSharedHelper().getMinOrderAmountTamatopup())) {
      ToastUtils.longToast("The minimum amount that you can topup is " + App.getInstance().getAppSharedHelper().getMinOrderAmountTamatopup());
      return;
    }
    new TamaAccountHelper().confirmTamaTopUp(this, topup_no, country_code, euro_amount, local_amount, dest_currency, pay_by, use_promo);
  }

  @OnCheckedChanged(R.id.checkbox_promo)
  public void usePromoCheckBoxCheck(CompoundButton button, boolean checked) {
    if (checked) {
      usePromo = true;
    } else {
      usePromo = false;
    }
  }

  private Denominations getDenominationsFromJson(String mobile_number, String country,
      String operator, String destination_currency, List<String> product_list,
      List<String> retail_price_list) {

      Denominations denominations = new Denominations(mobile_number,country, operator, destination_currency);
        denominations.product_list.addAll(product_list);
        denominations.retail_price_list.addAll(retail_price_list);

      return denominations;
  }

  private List<String> getListFromSubJson(JSONObject data) throws JSONException {
    List<String> items = new ArrayList<>();
      for (int i = 0; i < data.length(); i++) {
        String s = data.getString(String.valueOf(i));
        items.add(s);
      }
    return items;
  }

  @OnClick(R.id.open_contacts_list)
  public void clickOpenContactsList() {
    editNumber = true;
    setCurrentFragment(ContactsListFragment.newInstance(true, FIRST));
    tamaTopupFirstPage.setVisibility(View.GONE);
    topupTitle.setVisibility(View.GONE);
    if (mLastEnteredPhone != null && !mLastEnteredPhone.isEmpty()) {
      mLastEnteredPhone = null;
      setFirstPhoneNumber(enterPhoneNumberText.getText().toString());
    }
  }

  @Override
  public void alertDialogCancelListener() {
    clearRequestData();
    topupButtonSeconds.setEnabled(false);
    topupInfoText.setText("");
    super.alertDialogCancelListener();
  }

  private void clearRequestData() {
    country_code = "";
    topup_no = "";
    euro_amount = "";
    local_amount = "";
    dest_currency = "";
  }



  @Override
  public Context getAppContext() {
    return this;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    view.setSelected(true);
    this.selectedView=view;
    TextView textView1=view.findViewById(R.id.denomination_text_1);
    TextView textView2=view.findViewById(R.id.denomination_text_2);
//        textView1.setTextColor(getResources().getColor(R.color.white));
//        textView2.setTextColor(getResources().getColor(R.color.white));
    String str_local = textView1.getText().toString();
    String str_euro =textView2.getText().toString();
    topupInfoText.setText(getString(R.string.topup_info_text, str_euro, getFullPhoneNumber()));
    topupButtonSeconds.setEnabled(true);
    country_code = getCountryCode();
    topup_no = getPhoneNumber();
    euro_amount = str_euro.replace(" €", "");
    dest_currency = denominations.destination_currency;
    local_amount = str_local.replace(" " + dest_currency, "");
  }


  private class GetDenominationsAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
//        mProgressBar.setVisibility(View.VISIBLE);

    }

    int position;

    @Override
    protected String doInBackground(String... params) {
      String jsonResponse = "";
      String url="";
      String topup_no = params[0];
      String country_code = params[1];

      String postEntity = PostEntityUtil.composeGetDenominationsPostEntity(
          topup_no,
          country_code
      );

      HttpConnection httpConnection = HttpRequestManager
          .executeRequest(TamaTopUpActivity.this,
              RequestMethod.POST,
              APIUtil.getURL(HttpRequestManager.RequestType.TAMA_TOPUP_DENOMINATIONS,getLanguages(TamaTopUpActivity.this)),
              App.getInstance().getAppSharedHelper().getTamaAccountAccessToken(),
              postEntity);


      if (httpConnection.isHttpConnectionSucceeded()) {
        StringBuilder jsonResponseStringBuilder =httpConnection.getHttpResponseBody();
        jsonResponse=jsonResponseStringBuilder.toString();

      } else {
        Logger.e(TAG, httpConnection.getHttpConnectionMessage());
        HttpRequestManager.handleFailedRequest(httpConnection);
      }


      return jsonResponse;
    }
    //{"data":{"code":"0","http_code":200,"message":"TamaExpress order was successful, your pin was  251093486","result":{"balance":"\u20ac101.40","history_id":101}}}
    @Override
    protected void onPostExecute(String jsonResponse) {
//      mProgressBar.setVisibility(View.GONE);
        hideProgress();
      if (jsonResponse == null) {
        errorRequest("Something is wrong");
        return;
      }
      String message = null;
      String mobile_number=null;
      String country=null;
      String operator=null;
      String destination_currency=null;
      List<String> product_list=null;
      List<String> retail_price_list=null;


      JSONObject object = null;
      Map<String, String> map = new HashMap<>();
      try {
        object = new JSONObject(jsonResponse);
        JSONObject jsonObject = object.getJSONObject("data");
        parse(jsonObject, map);


        String code = map.get("code");
        String http_code = map.get("http_code");
        message = map.get("message");

        if (http_code.equals("400")) {
          errorRequest(message);
        } else if (http_code.equals("200") && code.equals("0")) {
          mobile_number = map.get("mobile_number");
          country = map.get("country");
          operator = map.get("operator");
          destination_currency = map.get("destination_currency");
          JSONObject json_object_product_list = jsonObject.getJSONObject("result").getJSONObject("product_list");
          JSONObject json_object_retail_price_list = jsonObject.getJSONObject("result").getJSONObject("retail_price_list");
          product_list = getListFromSubJson(json_object_product_list);
          retail_price_list = getListFromSubJson(json_object_retail_price_list);
          if (tamaTopupFirstPage.getVisibility() == View.VISIBLE) {
            createSecondPage(mobile_number,country,operator,destination_currency,product_list,retail_price_list);
          }
        } else {
         errorRequest(message);
        }


      } catch (JSONException e) {
        e.printStackTrace();
        errorRequest("Something is wrong");
      }

      if (selectedView!=null) {
        selectedView.setSelected(false);
      }


//      if (tamaTopupFirstPage.getVisibility() == View.VISIBLE) {
//        createSecondPage(mobile_number,country,operator,destination_currency,product_list,retail_price_list);
//      } else {
//        if(balance!=null){
//          createDialog("",getString(R.string.you_topup_done_with),"");
//        } else {
//          clearRequestData();
//          topupInfoText.setText(message);
//          topupButtonSeconds.setEnabled(false);
//        }
//        hideProgress();

//      }


    }
  }

  public void successRequest(){

  }

//  @Override
//  public void requestSuccess(String data) {
//    super.requestSuccess(data);
//    String message = null;
//    String mobile_number=null;
//    String country=null;
//    String operator=null;
//    String destination_currency=null;
//    List<String> product_list=null;
//    List<String> retail_price_list=null;
//    String balance=null;
//    String history_id=null;
//
//    JSONObject object = null;
//    Map<String, String> map = new HashMap<>();
//    try {
//      object = new JSONObject(data);
//      JSONObject jsonObject = object.getJSONObject("data");
//      parse(jsonObject, map);
//
//
//
//      String code = map.get("code");
//      String http_code = map.get("http_code");
//      message = map.get("message");
//
//      mobile_number = map.get("mobile_number");
//      country = map.get("country");
//      operator = map.get("operator");
//      destination_currency = map.get("destination_currency");
//
//
//      balance = map.get("balance");
//      history_id = map.get("history_id");
//      if(history_id!=null) {
//        historyId= Long.parseLong(history_id);
//      }
//
//      JSONObject json_object_product_list = jsonObject.getJSONObject("result").getJSONObject("product_list");
//      JSONObject json_object_retail_price_list = jsonObject.getJSONObject("result").getJSONObject("retail_price_list");
//      product_list = getListFromSubJson(json_object_product_list);
//      retail_price_list = getListFromSubJson(json_object_retail_price_list);
//    } catch (JSONException e) {
//      errorRequest("Something is wrong");
//      e.printStackTrace();
//    }
//
//
//    if (tamaTopupFirstPage.getVisibility() == View.VISIBLE) {
//      createSecondPage(mobile_number,country,operator,destination_currency,product_list,retail_price_list);
//    } else {
//      if(balance!=null){
//        createDialog("",getString(R.string.you_topup_done_with),"");
//      } else {
//        clearRequestData();
//        topupInfoText.setText(message);
//        topupButtonSeconds.setEnabled(false);
//      }
//      hideProgress();
//
//    }
//    if (selectedView!=null) {
//      selectedView.setSelected(false);
//    }
//
//  }

  public void errorRequest(String message) {
    super.requestError(message);
    if (tamaTopupFirstPage.getVisibility() == View.VISIBLE) {
      topupErrorText.setText(message);
      topupButtonFirst.setEnabled(true);

    } else {
      topupInfoText.setText(message);
      topupButtonSeconds.setEnabled(false);
      clearRequestData();
    }

    if (selectedView!=null) {
      selectedView.setSelected(false);
    }
    hideProgress();
  }
}
*/