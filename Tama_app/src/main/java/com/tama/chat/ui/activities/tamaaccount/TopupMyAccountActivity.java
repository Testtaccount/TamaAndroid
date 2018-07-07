package com.tama.chat.ui.activities.tamaaccount;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class TopupMyAccountActivity extends TamaAccountBaseActivity implements
    TamaAccountHelperListener {

    private double currentBalance = 0, topupBalance = 0;

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
        setTamaToolbar(R.string.account_topup, R.string.topup_my_account_one_line);
    }

    @OnClick(R.id.topup_button)
    public void sendTopupRequest() {
        topupErrorText.setText("");
        if (checkNumberCount()) {
            if (isNetworkAvailable()) {
                topupButton.setEnabled(false);
                new TamaAccountHelper().sendTopupRequest(this, this, topupVoucherText.getText().toString());
            } else {
                topupErrorText.setText(getString(R.string.no_internet_conection));
            }
            hideSoftKeyboard();
        } else {
            topupErrorText.setText(getString(R.string.number_count_error));
            topupErrorText.setTextColor(Color.RED);
        }
    }

    private void initFields() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String message = bundle.getString(CURRENT_BALANCE);
            currentBalance = getDouble(message);
        }
    }

    private boolean checkNumberCount() {
        String number = topupVoucherText.getText().toString();
        return number.length() == 9;
    }

    @Override
    public void requestSuccess(String data) {

        String result = getValueByKeyFromParseJSon(data, "result");
        if (result != null) {
            String message = getValueByKeyFromParseJSon(data, "message");
            topupErrorText.setText(message);
            topupButton.setEnabled(true);
            return;
        } else {
            String promotionTxt = getValueByKeyFromParseJSon(data, "promotion_txt");
            if (promotionTxt != null) {
//        Promo Voucher Card Transaction Response
                JSONObject object = null;
                Map<String, String> out = new HashMap<>();
                try {
                    object = new JSONObject(data);
                    JSONObject info = object.getJSONObject("data");
                    parse(info, out);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String message = out.get("message");
                String balance = out.get("balance");
                String tamaexpress = out.get("tamaexpress");
                String tamatopup = out.get("tamatopup");
                String tamavoucher = out.get("tamavoucher");
                String min_order_amount_tamaexpress = out.get("min_order_amount_tamaexpress");
                String min_order_amount_tamatopup = out.get("min_order_amount_tamatopup");
                String shipping_free_amount = out.get("shipping_free_amount");
                String promo_tamaexpress_balance = out.get("promo_tamaexpress_balance");
                String promo_tamatopup_balance = out.get("promo_tamatopup_balance");
                String promotion_txt = out.get("promotion_txt");

                topupErrorText.setTextColor(Color.GREEN);
                topupErrorText.setText(getResources().getString(R.string.success));
//        topupBalance = getDouble(data) - currentBalance;
                createDialog("", message, "");
//        currentBalance = getDouble(data);
                topupVoucherText.setText("");
                topupButton.setEnabled(true);
            } else {
                //        Successful Response
                JSONObject object = null;
                Map<String, String> out = new HashMap<>();
                try {
                    object = new JSONObject(data);
                    JSONObject info = object.getJSONObject("data");
                    parse(info, out);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String message = out.get("message");
                String balance = out.get("balance");
                String balance_ws = out.get("balance_ws");

                topupErrorText.setText(message);
                topupErrorText.setTextColor(Color.GREEN);
                topupBalance = getDouble(balance_ws) - currentBalance;
                createDialog(String.valueOf(topupBalance), getString(R.string.d_m_account_with), "");
                currentBalance = getDouble(balance_ws);
                topupVoucherText.setText("");
                topupButton.setEnabled(true);

            }
        }
    }

    @Override
    public void requestError(String data) {
        topupErrorText.setText(data);
        topupErrorText.setTextColor(Color.RED);
        topupButton.setEnabled(true);
    }

    @Override
    public void alertDialogCancelListener() {
        super.alertDialogCancelListener();
        topupVoucherText.setText("");
//        topupErrorText.setTextColor(Color.RED);
    }

    @Override
    public Context getAppContext() {
        return this;
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

//        Validation Response
//        {
//            "data": {
//            "code": "1",
//                "http_code": 400,
//                "message": "The given data failed to pass validation.",
//                "result": {
//                "voucher_no": [
//                "The voucher no field is required."
//      ]
//            }
//        }
//        }
//        Invalid Voucher Response
//        {
//            "data": {
//            "code": "0",
//            "http_code": 200,
//            "message": "Invalid voucher!",
//            "result": []
//        }
//        }
//        Promo Voucher Card Transaction Response
//        {
//            "data": {
//            "code": 0,
//            "http_code": 200,
//            "message": "Your Promotion voucher topup has been successful!",
//            "result": {
//            "balance": "€5.00",
//            "tamaexpress": 1,
//            "tamatopup": 1,
//            "tamavoucher": 1,
//            "min_order_amount_tamaexpress": "10",
//            "min_order_amount_tamatopup": "2",
//            "shipping_free_amount": "20",
//            "promo_tamaexpress_balance": "€18.00",
//            "promo_tamatopup_balance": "€2.00",
//            "promotion_txt": "Promo Balance: TamaExpress: €18.00 / TamaTopup: €2.00"
//            }
//        }
//        }
//
//        Successful Response
//        {
//            "data": {
//            "code": "0",
//            "http_code": 200,
//            "message": "Your Voucher recharge was successful!",
//            "result": {
//            "balance": "€60.78",
//            "balance_ws": "60.78"
//            }
//        }
//        }

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