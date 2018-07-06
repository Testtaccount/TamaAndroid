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
import com.tama.chat.tamaAccount.AboutUsActivity;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.ContactUsActivity;
import com.tama.chat.ui.activities.tamaaccount.MyTamaRequestActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaExpressActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaHistoryActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaTopUpActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaTransferActivity;
import com.tama.chat.ui.activities.tamaaccount.TopupMyAccountActivity;
import com.tama.chat.ui.fragments.base.BaseFragment;
import com.tama.chat.utils.AppUtil;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.helpers.SharedHelper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Avo on 12-May-17.
 */

public class MyTamaAccountFragment extends BaseFragment implements TamaAccountHelperListener {

  private String balance = "0.0";

  @Bind(R.id.tama_account_balance)
  TextView tamaAccountBalance;

  @Bind(R.id.tv_promotion_txt)
  TextView promotionTxtTv;

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

  private boolean canTopup;
  private boolean canTopupByVoucher;

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

  private void setButtonEnable(boolean bool) {
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
    if (isNetworkAvailable()) {
//            updateBalance();
      updateHeartbeat();
    } else {
      Toast.makeText(app, app.getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
    }
    setButtonEnable(true);
  }

  private void updateBalance() {
    String user_id = new SharedHelper(baseActivity).getTamaAccountId();
    new TamaAccountHelper().getBalance(this.getContext(), this, user_id);
  }

  private void updateHeartbeat() {
    String user_id = new SharedHelper(baseActivity).getTamaAccountId();
    new TamaAccountHelper().getHeartbeat(this.getContext(), this, user_id);
  }

  protected void createMessageDialog(String textMessage, String title, String subTitle) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = this.getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.topup_dialog_view, null);
    dialogBuilder.setView(dialogView);
//        dialogBuilder.setCancelable(false);
    final AlertDialog alertDialog = dialogBuilder.create();

    TextView textTitle = (TextView) dialogView.findViewById(R.id.topup_dialog_title_text);
    TextView textBig = (TextView) dialogView.findViewById(R.id.topup_dialog_big_text);
    TextView textView = (TextView) dialogView.findViewById(R.id.topup_dialog_subtitle_text);
    Button topupDialogButton = (Button) dialogView.findViewById(R.id.topup_dialog_button);

    textBig.setText(textMessage);
    textBig.setTextSize(18);
    textBig.setTextColor(Color.BLACK);
    topupDialogButton.setText(getString(R.string.ok));
    topupDialogButton.setBackground(
        ContextCompat.getDrawable(getActivity(), R.drawable.selector_button_red_oval));

    topupDialogButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alertDialog.cancel();
      }
    });

    alertDialog.show();
  }


  @OnClick(R.id.button_topup_myacc)
  public void startTopupMyAccountActivity() {

//    if (result[@"tamatopup"] != [NSNull null] && result[@"tamatopup"] != nil) {
//      weakSelf.canTopup = [result[@"tamatopup"] boolValue];
//    }
//    if (result[@"tamavoucher"] != [NSNull null] && result[@"tamavoucher"] != nil) {
//      weakSelf.canTopupByVoucher = [result[@"tamavoucher"] boolValue];
//    }

    if (!canTopup && !canTopupByVoucher) {
      createMessageDialog(getString(R.string.message_available_european_users), "", "");
      return;
    }
    setButtonEnable(false);
    Intent intent = new Intent(baseActivity, TopupMyAccountActivity.class);
    intent.putExtra(CURRENT_BALANCE, balance);
    startActivity(intent);
  }

  @OnClick(R.id.button_tama_topup)
  public void startTamaTopUpActivity() {
//    if (!new SharedHelper(baseActivity).getTamaIsEurope()) {
//      createMessageDialog("These options are only available to European users", "", "");
//      return;
//    }

    if (!canTopup && !canTopupByVoucher) {
      createMessageDialog(getString(R.string.message_available_european_users), "", "");
      return;
    }
    setButtonEnable(false);
    Intent intent = new Intent(baseActivity, TamaTopUpActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.button_tama_history)
  public void startTamaHistoryActivity() {
    setButtonEnable(false);
    Intent intent = new Intent(baseActivity, TamaHistoryActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.button_tama_about_us)
  public void startAboutUsActivity() {
    setButtonEnable(false);
    Intent intent = new Intent(baseActivity, AboutUsActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.button_send_credit)
  public void startTamaBalanceTransfer() {
    setButtonEnable(false);
    Intent intent = new Intent(baseActivity, TamaTransferActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.button_tama_request)
  public void startRequestActivity() {
    setButtonEnable(false);
    Intent intent = new Intent(baseActivity, MyTamaRequestActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.button_tama_express)
  public void startTamaExpressAcitvity() {
    setButtonEnable(false);
    Intent intent = new Intent(baseActivity, TamaExpressActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.button_tama_contact_us)
  public void startTamaContactUs() {
    setButtonEnable(false);
    Intent intent = new Intent(baseActivity, ContactUsActivity.class);
    startActivity(intent);
  }

  @Override
  public void alertDialogCancelListener() {

  }

  @Override
  public void requestSuccess(String json) {
          balance =  getValueByKeyFromParseJSon(json, "balance");
    if (tamaAccountBalance != null) {
      tamaAccountBalance.setText(balance);
    }

    if (promotionTxtTv != null) {
      promotionTxtTv.setText( getValueByKeyFromParseJSon(json, "promotion_txt"));
    }

    canTopup= AppUtil.intToBoolean(Integer.valueOf(getValueByKeyFromParseJSon(json,"tamatopup")));
    canTopupByVoucher= AppUtil.intToBoolean(Integer.valueOf(getValueByKeyFromParseJSon(json,"tamavoucher")));

  }

  @Override
  public void requestError(String data) {
    ToastUtils.longToast(data);
  }

  @Override
  public Context getAppContext() {
    return getContext();
  }


  private String getValueByKeyFromParseJSon(String json, String key) {
    if (json == null) {
      return "";
    }
    JSONObject object = null;
    Map<String, String> out = new HashMap<>();
    try {
      object = new JSONObject(json);
      JSONObject info = object.getJSONObject("data");
      parse(info, out);
  } catch (JSONException e) {
    e.printStackTrace();
  }
    String balance = out.get("balance");
    String balance_ws = out.get("balance_ws");
    String user_image = out.get("user_image");
    String tamaexpress = out.get("tamaexpress");
    String tamatopup = out.get("tamatopup");
    String tamavoucher = out.get("tamavoucher");
    String pay_to_retailer = out.get("pay_to_retailer");
    String pay_by_balance = out.get("pay_by_balance");
    String pay_by_cards = out.get("pay_by_cards");
    String pay_by_paypal = out.get("pay_by_paypal");
    String min_order_amount_tamaexpress = out.get("min_order_amount_tamaexpress");
    String min_order_amount_tamatopup = out.get("min_order_amount_tamatopup");
    String promo_tamaexpress_balance = out.get("promo_tamaexpress_balance");
    String promo_tamatopup_balance = out.get("promo_tamatopup_balance");
    String promotion_txt = out.get("promotion_txt");

//
//    String latitude = out.get("latitude");
//    String longitude = out.get("longitude");
//    String city = out.get("city");
//    String state = out.get("state");
//    String country = out.get("country");
//    String postal = out.get("postal_code");

//    System.out.println(
//        "Latitude : " + latitude + " LongiTude : " + longitude + " City : " + city + " State : "
//            + state + " Country : " + country + " postal " + postal);

//    System.out.println("ALL VALUE " + out);







//    String value = null;
//    JSONObject jsonObject = new JSONObject(data);
////        JSONObject jsonData = jsonObject.optJSONObject("data"); //data
//    if (jsonObject != null) {
//      value = jsonObject.getString(key);
//    } else {
//      jsonObject = jsonObject.optJSONObject("error");
//      if (jsonObject != null) {
//        value = jsonObject.getString("message");
//      }
//    }

    return out.get(key);
  }

  public static Map<String, String> parse(JSONObject json, Map<String, String> out)
      throws JSONException {
    Iterator<String> keys = json.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      String val = null;
      try {
        JSONObject value = json.getJSONObject(key);
        parse(value, out);
      } catch (Exception e) {
        val = json.getString(key);
      }

      if (val != null) {
        out.put(key, val);
      }
    }
    return out;
  }


}
