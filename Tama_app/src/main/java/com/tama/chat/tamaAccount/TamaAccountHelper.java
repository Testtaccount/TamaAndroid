package com.tama.chat.tamaAccount;

import static com.tama.chat.app.MyContextWrapper.getLanguage;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.utils.helpers.SharedHelper;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class TamaAccountHelper {

  private static final String TAG = TamaAccountHelper.class.getSimpleName();

  private static final String MY_TAMA_URL = "http://tamaexpress.com:585/api/";

  private static final String REGISTER_USER = MY_TAMA_URL + "register";
  private static final String REFRESH_TOKEN = MY_TAMA_URL + "refresh-token";
  private static final String GET_BALANCE = MY_TAMA_URL + "v1/balance/";
  private static final String GET_HEARTBEAT = MY_TAMA_URL + "heartbeat/";
  private static final String GET_COUTRIES_LIST = MY_TAMA_URL + "sendtama";
  private static final String GET_COTEGORIES_LIST = MY_TAMA_URL + "sendtama/sn";
  private static final String GET_INCOMING_REQUEST = MY_TAMA_URL + "requests/";
  private static final String GET_OUTGOING_REQUEST = MY_TAMA_URL + "request/sent/";
  private static final String CONFIRM = MY_TAMA_URL + "sendtama/confirm";
  private static final String REVOKE_ACCOUNT = MY_TAMA_URL + "revoke/access";
  private static final String CONTACT_US = MY_TAMA_URL + "contact-us";
  //    private static final String TOPUP_ACCOUNT = MY_TAMA_URL + "topup";
  private static final String TOPUP_ACCOUNT_BY_VAUCHER = MY_TAMA_URL + "recharge";
  private static final String ACCEPT_DENIED_REQUEST = MY_TAMA_URL + "request/";
  private static final String MYTAMA_ACCOUNT_TOPUP_REQUEST = MY_TAMA_URL + "requests/mytama";
  private static final String PAY_TAMA_EXPRESS_REQUEST = MY_TAMA_URL + "requests/tamaexpress";
  private static final String TOPUP_MY_MOBILE_REQUEST = MY_TAMA_URL + "requests/tama-topup";
  private static final String CHECK_TAMA_USER = MY_TAMA_URL + "check/account";
  private static final String BALANCE_TRANSFER = MY_TAMA_URL + "transfer";
  private static final String MOBILE_TOPUP = MY_TAMA_URL + "tama-topup";
  private static final String CONFIRM_TOPUP = MY_TAMA_URL + "tama-topup/confirm";
  private static final String GET_HISTORY = MY_TAMA_URL + "history/";
  private static final String GET_SINGLE_HISTORY = MY_TAMA_URL + "history/view/";
  private static final String PAY_BY = "voucher";

  //    private static final String accessToken = "KK5XzdmmYkCYVnM0p5HdTmSwlNHW0RZg3pIIXfKb";

  private static Context context;
  private TamaAccountHelperListener listener;
  private String error;
  private boolean isRequestOrder;


  public TamaAccountHelper() {
  }

  private String getAccessToken() {

//    if (System.currentTimeMillis() < App.getInstance().getAppSharedHelper()
//        .getTamaAccountExpiresIn()) {
    return App.getInstance().getAppSharedHelper().getTamaAccountAccessToken();
//    return "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjE1MGUxM2FiMTBlYWU3YzZjMGNlMTc1MWUzMjZmMWIzMDNkYmFjOGQ4YmYyOGEyNDZhNzE4ZjI0N2ZmY2NhMjIxMzljZTk0ZmRjNWMxMGU3In0.eyJhdWQiOiIyIiwianRpIjoiMTUwZTEzYWIxMGVhZTdjNmMwY2UxNzUxZTMyNmYxYjMwM2RiYWM4ZDhiZjI4YTI0NmE3MThmMjQ3ZmZjY2EyMjEzOWNlOTRmZGM1YzEwZTciLCJpYXQiOjE1MzEzNzk1MzQsIm5iZiI6MTUzMTM3OTUzNCwiZXhwIjoxNTYyOTE1NTM0LCJzdWIiOiI3OSIsInNjb3BlcyI6WyIqIl19.qGwD8IMT9wwkrpUCpN0VIclEc6jClxoeT9TZq3onmdDl-nQkgKyGUbcKMCusgr2UGvdMCT6rlTSbKBZpKgSyMQx8JXfeTx6kj6XjhCbXaD35TbCJgy8whGwrzpNnk9KrrJjPHD8p4RXFTC2dnkQp038VtYhIF0ahIt_e4SioK1JC8w3oKhmupbcMHGhGE1vXk7ghyqWeJdwjXupAfJwl5skAEZ8VqepwEXdqshLsGOWH0GjzYtqAcM3Otq1Osdf874itm5qRpK55AvViRNRODCcpTqUuxuYYIbPhJArKY9L8FylYpv6G54SW5eotgG1RNmi-KG5rlQwkEftan79KsgUkX2o64aQJnY2qe0DW24Ho1ZUtEB6jeDpXSeb__t1F2UdODTaubUY6Qwl0JANgt7eWTr6W_Mb4GDyEcGqj6jGtxMvLeuNjcYnOz41q-rkeZZdhZ9RFhxUA45m_5m9xqz-DItqvS4egfJc3O7IqO1gazf2AlTIgVFQ12EW9aN-e9GN6GCB-0DEpWs8ws_NFum0W_K09tn9h3Oz-aHQJA6rit7tLBwbB0yamo91cC_2Xss_wWkw9bVfMXbvuoEC-JfCfy5ArGbiv5yFFAHnZn9mpJ-eirzt42YmZS2ZzzdbCal37iyVz3Hn7SGYaR99gMZWIMNAOiT_wVv2uwjUHJDM";
//    } else {
//      new RefreshAccessTokenTask(new AsyncResponse() {
//        @Override
//        public void processFinish(String output) {
//          getAccessToken();
//        }
//      }).execute();
//    }
//    return App.getInstance().getAppSharedHelper().getTamaAccountAccessToken();

  }


  private String getLanguages() {
    return "?lang=" + getLanguage(listener.getAppContext());
  }

  public void register(Context context, TamaAccountHelperListener listener, String phoneNumber,
      String first_name, String last_name, String avatar)
      throws UnsupportedEncodingException {
    this.context = context;
    this.listener = listener;

    Log.d(TAG, "phoneNumber = " + phoneNumber);

    new SharedHelper(context).saveTamaAccountPhoneNumber(phoneNumber);
    new RegisterRequestTask()
        .execute(REGISTER_USER + getLanguages(), phoneNumber, first_name, last_name, avatar);
  }

  public void sendTrasferRequest(TamaAccountHelperListener listener, String user_id,
      String country_code, String transfer_to, String amount) {
    this.listener = listener;
    new BalanceTransferTask()
        .execute(BALANCE_TRANSFER + getLanguages(), user_id, country_code, transfer_to, amount);
  }

  public void getListOfCountries(TamaAccountHelperListener listener) {
    this.listener = listener;
    new GetCountriesOrCotegoriesListTask().execute(GET_COUTRIES_LIST + getLanguages());
  }

  public void getListOfCategories(TamaAccountHelperListener listener, String url) {
    this.listener = listener;
//    new GetCountriesOrCotegoriesListTask().execute(GET_COTEGORIES_LIST + getLanguages());
    new GetCountriesOrCotegoriesListTask().execute(url + getLanguages());
  }

  public void getListOfProducts(TamaAccountHelperListener listener, String url,
      String category_id) {
    this.listener = listener;
    new GetCountriesOrCotegoriesListTask()
        .execute(url + "/" + category_id + "/products" + getLanguages());
  }

  public void checkTamaUserOrNot(TamaAccountHelperListener listener, String country_code,
      String phone_no) {
    this.listener = listener;
    new CheckTamaUserTask().execute(CHECK_TAMA_USER + getLanguages(), country_code, phone_no);
  }

  public void confirmTamaTopUp(TamaAccountHelperListener listener, String topup_no,
      String country_code,
      String euro_amount, String local_amount, String dest_currency, String pay_by,
      String use_promo) {
    this.listener = listener;
    new ConfirmTamaTopUpTask()
        .execute(CONFIRM_TOPUP + getLanguages(), topup_no, country_code, euro_amount, local_amount,
            dest_currency, pay_by, use_promo);
  }

  public void sendMyTamaAccountTopUpRqt(TamaAccountHelperListener listener, String user_id,
      String country_code, String payer_no, String amount) {
    this.listener = listener;
    new MyTamaAccountTopUpRqtTask()
        .execute(MYTAMA_ACCOUNT_TOPUP_REQUEST + getLanguages(), user_id, country_code, payer_no,
            amount);
  }

  public void sendPayTamaExpressRqt(TamaAccountHelperListener listener, String user_id,
      String country_code, String payer_no, String tama_pin) {
    this.listener = listener;
    new PayTamaExpressRequestTask()
        .execute(PAY_TAMA_EXPRESS_REQUEST + getLanguages(), user_id, country_code, payer_no,
            tama_pin);
  }

  public void setOkDeniedRequest(TamaAccountHelperListener listener, String user_id,
      String request_id, String answer) {
    this.listener = listener;
    new AcceptDeniedIncomingRqtTask()
        .execute(ACCEPT_DENIED_REQUEST + answer + getLanguages(), user_id, request_id);
  }

  public void sendMyTamaMobileTopUpRqt(TamaAccountHelperListener listener, String user_id,
      String payer_country_code, String payer_no,
      String topup_country_code, String topup_no, String amount) {
    this.listener = listener;
    new MyTamaMobileTopUpRqtTask()
        .execute(TOPUP_MY_MOBILE_REQUEST + getLanguages(), user_id, payer_country_code,
            payer_no, topup_country_code, topup_no, amount);
  }

  public void setConfirm(TamaAccountHelperListener listener, String productIds, String sender_name,
      String sender_mobile, String receiver_name, String receiver_mobile, String pay_by,
      String use_promo) {
    this.listener = listener;
    new ConfirmTask().execute(CONFIRM + getLanguages(), productIds, sender_name, sender_mobile,
        receiver_name, receiver_mobile, pay_by, use_promo);
  }

  public void setRevokeAccount(TamaAccountHelperListener listener, String user_id) {
    this.listener = listener;
    new RevokeAccountTask().execute(REVOKE_ACCOUNT, user_id);
  }

  public void setContactUs(TamaAccountHelperListener listener) {
    this.listener = listener;
    new ContactUSTask().execute(CONTACT_US + getLanguages());
  }

  public void getDenominations(TamaAccountHelperListener listener, String topup_no,
      String country_code) {
    this.listener = listener;
    new MobileTopUpGetDenominations()
        .execute(MOBILE_TOPUP + getLanguages(), topup_no, country_code);
  }

  public void getBalance(Context context, TamaAccountHelperListener listener, String user_id) {
    this.context = context;
    this.listener = listener;
    Log.d(TAG, "user_id = " + user_id);
    new GetBalanceTask().execute(GET_BALANCE + getLanguages());
  }

  public void getHeartbeat(Context context, TamaAccountHelperListener listener, String user_id) {
    this.context = context;
    this.listener = listener;
    Log.d(TAG, "user_id = " + user_id);
    new GetHeartbeatTask().execute(GET_HEARTBEAT + getLanguages());
  }

  public void sendTopupRequest(Context context, TamaAccountHelperListener listener,
      String voucherNo) {
    this.context = context;
    this.listener = listener;
    String user_id = new SharedHelper(context).getTamaAccountId();
    new TopupRequestTask().execute(TOPUP_ACCOUNT_BY_VAUCHER + getLanguages(), voucherNo, PAY_BY);
  }

  public void getHistory(TamaAccountHelperListener listener, String history_type) {
    this.listener = listener;

//        new GetHistoryTask().execute(GET_HISTORY + 1 + "/" + history_type);
    new GetHistoryTask().execute(GET_HISTORY  + "/" + history_type + getLanguages());
  }

  public void getSingleHistory(TamaAccountHelperListener listener, String history_id) {
    this.listener = listener;
    new GetHistoryTask().execute(GET_SINGLE_HISTORY +  "/" + history_id + getLanguages());
//        new GetHistoryTask().execute(GET_SINGLE_HISTORY + 1 + "/" + history_id);
  }

  public void getIncomingRequest(TamaAccountHelperListener listener, String user_id) {
    this.listener = listener;
    new GetIncomingRequestTask().execute(GET_INCOMING_REQUEST + user_id + getLanguages());
//        new GetIncomingRequestTask().execute(GET_INCOMING_REQUEST+"1");
  }

  public void getOutGoingRequest(TamaAccountHelperListener listener, String user_id, String type) {
    this.listener = listener;
    new GetOutGoingRequestTask()
        .execute(GET_OUTGOING_REQUEST + user_id + "/" + type + getLanguages());
  }

  private class MobileTopUpGetDenominations extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("topup_no", params[1]);
        json.put("country_code", params[2]);
        StringEntity paramsStr = new StringEntity(json.toString());
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());
        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);
        return responseBody;
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String json) {
      super.onPostExecute(json);
      if (json != null) {
        JSONObject object = null;
        Map<String, String> map = new HashMap<>();
        try {
          object = new JSONObject(json);
          JSONObject jsonObject = object.getJSONObject("data");
          parse(jsonObject, map);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        String code = map.get("code");
        String http_code = map.get("http_code");
        String message = map.get("message");

        String country_code = map.get("country_code");
        String topup_no = map.get("topup_no");

        String mobile_number = map.get("mobile_number");
        String country = map.get("country");
        String operator = map.get("operator");
        String destination_currency = map.get("destination_currency");
        String product_list = map.get("product_list");
        String retail_price_list = map.get("retail_price_list");

        if (http_code.equals("400")) {
          listener.requestError(message);
        } else if (http_code.equals("200") && code.equals("0")) {
          listener.requestSuccess(json);
        } else {
          listener.requestError(message);
        }
//
//
//        JSONObject jsonObject = null;
//        try {
//          jsonObject = new JSONObject(s);
//        } catch (JSONException e) {
//          e.printStackTrace();
//        }
//        if (jsonObject != null) {
//          JSONObject jsonData = jsonObject.optJSONObject("error");
//          if (jsonData != null) {
//            error = jsonData.optString("message");
//            if (error != null) {
//              listener.requestError(error);
//            }
//          } else {
//            listener.requestSuccess(s);
//          }
//        }
      } else {
        listener.requestError("The given data failed to pass validation.");
      }
    }
  }

  private class ConfirmTamaTopUpTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
      try {
        JSONObject json = new JSONObject();
        json.put("topup_no", params[1]);
        json.put("country_code", params[2]);
        json.put("euro_amount", params[3]);
        json.put("local_amount", params[4]);
        json.put("dest_currency", params[5]);
        json.put("pay_by", params[6]);
        json.put("use_promo", params[7]);
        StringEntity paramsStr = new StringEntity(json.toString());
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());
        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);
        return responseBody;
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    /*{
  "data": {
    "code": "1",
    "http_code": 200,
    "message": "Request Timeout",
    "result": []
  }
}*/
    /*{
  "data": {
    "code": "0",
    "http_code": 200,
    "message": "Your Recharge was successful!",
    "result": {
      "balance": "€3.53",
      "history_id": ""
    }
  }
}*/
    /*{
"data": {
"code": "1",
"http_code": 200,
"message": "Your account does not a have enough balance to make this order!",
"result": [],
}
}*/

    /*{
"data": {
"code": "1",
"http_code": 200,
"message": "Unable to confirm you order, try again later",
"result": [],
}
}*/


    @Override
    protected void onPostExecute(String json) {
      super.onPostExecute(json);
      if (json != null) {

        JSONObject object = null;
        Map<String, String> map = new HashMap<>();
        try {
          object = new JSONObject(json);
          JSONObject jsonObject = object.getJSONObject("data");
          parse(jsonObject, map);
        } catch (JSONException e) {
          e.printStackTrace();
        }

        String code = map.get("code");
        String http_code = map.get("http_code");
        String message = map.get("message");

        String balance = map.get("balance");
        String history_id = map.get("history_id");

        if (http_code.equals("400")) {
          listener.requestError(message);
        }
        else if (http_code.equals("200") && code.equals("0") && balance!=null) {
          listener.requestSuccess(json);
        } else {
          listener.requestError(message);
        }
//

        ///////////////////
//        if (str != null) {
//          JSONObject jsonObject = null;
//          try {
//            jsonObject = new JSONObject(str);
//          } catch (JSONException e) {
//            e.printStackTrace();
//          }
//          if (jsonObject != null) {
//            JSONObject jsonData = jsonObject.optJSONObject("error");
//            if (jsonData != null) {
//              error = jsonData.optString("message");
//              if (error != null) {
//                listener.requestError(error);
//              }
//            } else {
//              listener.requestSuccess(str);
//            }
//          }
//        }
      } else {
        listener.requestError("Unable to confirm you order, try again later");
      }
    }
  }


  private class TopupRequestTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("voucher_no", params[1]);
        json.put("pay_by", params[2]);
        StringEntity paramsStr = new StringEntity(json.toString());
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());
        httppost.setEntity(paramsStr);
        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);
        String vaucherErrorString = "{ \"data\": { \"code\": 0, \"http_code\": 200, \"message\": \"Your Promotion voucher topup has been successful!\", \"result\": { \"balance\": \"€5.00\", \"tamaexpress\": 1, \"tamatopup\": 1, \"tamavoucher\": 1, \"min_order_amount_tamaexpress\": \"10\", \"min_order_amount_tamatopup\": \"2\", \"shipping_free_amount\": \"20\", \"promo_tamaexpress_balance\": \"€18.00\", \"promo_tamatopup_balance\": \"€2.00\", \"promotion_txt\": \"Promo Balance: TamaExpress: €18.00 / TamaTopup: €2.00\" } } }";
//        String vaucherErrorString = "{ \"data\": { \"code\": \"0\", \"http_code\": 200, \"message\": \"Your Voucher recharge was successful!\", \"result\": { \"balance\": \"€60.78\", \"balance_ws\": \"60.78\" } } }";
//        return vaucherErrorString;
        return responseBody;
      } catch (Exception e) {
        error = "Error, Voucher is not valid!";
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("data");
          if (jsonData != null && jsonData.optString("http_code").equals("400")) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
//            try {
            listener.requestSuccess(str);
//              listener.requestSuccess(parseJSon(str, "balance"));
//            } catch (JSONException e) {
//              error = e.getMessage();
//              listener.requestError(error);
//              e.printStackTrace();
//            }
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class BalanceTransferTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("user_id", params[1]);
        json.put("country_code", params[2]);
        json.put("transfer_to", params[3]);
        json.put("amount", params[4]);
        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);

        String balance = parseJSon(responseBody, "balance");
        return balance;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class ConfirmTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("products", params[1]);
        json.put("sender_name", params[2]);
        json.put("sender_mobile", params[3]);
        json.put("receiver_name", params[4]);
        json.put("receiver_mobile", params[5]);
        json.put("pay_by", params[6]);
        json.put("use_promo", params[7]);

        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);
//                String mobile_no = parseJSon(responseBody,"message");
        return responseBody;//mobile_no;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String json) {
      super.onPostExecute(json);
      if (json != null) {

        JSONObject object = null;
        Map<String, String> map = new HashMap<>();
        try {
          object = new JSONObject(json);
          JSONObject jsonObject = object.getJSONObject("data");
          parse(jsonObject, map);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        String code = map.get("code");
        String http_code = map.get("http_code");
        String message = map.get("message");
        String balance = map.get("balance");
        String history_id = map.get("history_id");

        if (http_code.equals("400")) {
          listener.requestError(message);
        } else if (code.equals("1")) {
          listener.requestError(message);
        } else if (code.equals("0")) {
          listener.requestSuccess(message);
        }
      } else {
        listener.requestError("The given data failed to pass validation.");
      }
    }
  }

  private class RevokeAccountTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("user_id", params[1]);
        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);
        return responseBody;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null && !str.isEmpty()) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        assert jsonObject != null;
        JSONObject jsonData = jsonObject.optJSONObject("error");
        if (jsonData != null) {
          if (jsonData.optInt("code") == 200) {
            listener.requestSuccess(jsonData.optString("message"));
          } else if (jsonData.optInt("code") == 404) {
            listener.requestError(jsonData.optString("message"));
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class ContactUSTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        return (String) httpclient.execute(httpGet, responseHandler);
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class MyTamaMobileTopUpRqtTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("user_id", params[1]);
        json.put("payer_country_code", params[2]);
        json.put("payer_no", params[3]);
        json.put("topup_country_code", params[4]);
        json.put("topup_no", params[5]);
        json.put("amount", params[6]);
        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);

        String mobile_no = parseJSon(responseBody, "message");
        return mobile_no;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class AcceptDeniedIncomingRqtTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("user_id", params[1]);
        json.put("request_id", params[2]);
        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);

        String mobile_no = parseJSon(responseBody, "message");
        return mobile_no;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class PayTamaExpressRequestTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("user_id", params[1]);
        json.put("country_code", params[2]);
        json.put("payer_no", params[3]);
        json.put("tama_pin", params[4]);
        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);

        String mobile_no = parseJSon(responseBody, "message");
        return mobile_no;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class MyTamaAccountTopUpRqtTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("user_id", params[1]);
        json.put("country_code", params[2]);
        json.put("payer_no", params[3]);
        json.put("amount", params[4]);
        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);

        String mobile_no = parseJSon(responseBody, "message");
        return mobile_no;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class CheckTamaUserTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("country_code", params[1]);
        json.put("mobile_no", params[2]);
        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);

        String mobile_no = parseJSon(responseBody, "mobile_no");
        return mobile_no;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class RegisterRequestTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String phone = "+" + params[1];
        int countryCode = 0;
        try {
          // phone must begin with '+'
          Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phone, "");
          countryCode = numberProto.getCountryCode();
        } catch (NumberParseException e) {
          System.err.println("NumberParseException was thrown: " + e.toString());
        }

//                String country_code = GetCountryZipCode(context);
        String country_code = String.valueOf(countryCode);

        String mobile_no = (params[1]).replace(country_code, "");

        json.put("country_code", country_code);
        json.put("mobile_no", mobile_no);
        json.put("first_name", params[2]);
        json.put("last_name", params[3]);
        if (params[4] != null) {
          json.put("avatar", params[4]);
        }
        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
//                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
//                httppost.setHeader("Content-type", "multipart/form-data; boundary=WebKitFormBoundary7MA4YWxkTrZu0gW");
        httppost.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);

        return responseBody;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class RefreshAccessTokenTask extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;

    public RefreshAccessTokenTask(AsyncResponse delegate) {
      this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {

      try {
        JSONObject json = new JSONObject();
        json.put("refresh_token",
            App.getInstance().getAppSharedHelper().getTamaAccountRefreshToken());

        StringEntity paramsStr = new StringEntity(json.toString());

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(REFRESH_TOKEN + getLanguages());
        httppost.setEntity(paramsStr);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");

        httppost.setHeader("Authorization",
            "Bearer " + App.getInstance().getAppSharedHelper().getTamaAccountAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httppost, responseHandler);

        return responseBody;

      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }

//        {
//          "token_type": "Bearer",
//          "expires_in": 31536000,
//          "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjkyNzA3OTc3Y2IyMmVlY2RhMTA4MWNlY2Q0MzNiZTkwNGEyMjg3NGZjOGZlMzBjMGM2NTBkMTFlZGFjODEwMzUwMDIxZTI2NTI3NWQ2ODEzIn0.eyJhdWQiOiIyIiwianRpIjoiOTI3MDc5NzdjYjIyZWVjZGExMDgxY2VjZDQzM2JlOTA0YTIyODc0ZmM4ZmUzMGMwYzY1MGQxMWVkYWM4MTAzNTAwMjFlMjY1Mjc1ZDY4MTMiLCJpYXQiOjE1MjA5MzkzMzgsIm5iZiI6MTUyMDkzOTMzOCwiZXhwIjoxNTUyNDc1MzM4LCJzdWIiOiIxNCIsInNjb3BlcyI6WyIqIl19.G4h_2Bn5pIUxg3ny1avVQ_-98HAQ_ECowxHfxeeRKj8wdai3ARlIGB6YVINhXWG6ZAAtPvcOkQt7-w8lN1AaX8O9K6lyXUdQZSidMpf6vPCRZvG0oR4ZFUKUNsjmK8O2kfT2jE990ewteSFxXi7KV3LQ2hRHvAX3-YquWZ6KnmvQbM_Tz-U4NvXx3ZqZfub9VIBdgowahpL0MYE_05q8A6KuwvH9WFc4b5P0eJhRycXq2PARBVMCum1mUXBcZLXq9iclbFDoUbm7sXVMVIwAa9msFaIQnk-r4qzw2p7BqcZYw_vpGhoxo1wpwGhdXYry0c4P6NPndZnIcxfSsxJZPP-nCe9CRfFSP1FF0fOzUEblaFEEjRj_Rb54qfW8tOX9kpWWbZA5uzJLJy3AweaTP83HcWupqM8PQeKiVkKboCvKo12_uXzq04OumBHih98BrRuv4pHx77uWYj4np2GtF-8wp4Vs-GtWEa5PaLb-KAzktg2bOVG-GF0RFgDRQDrpx94LUvXBRCGHFN-nJoqEq6lOvba0bztFQwEp-9EeVfwch_aiqYv2AGzKgniWQExMnzQExTVVvrYDznooW2GIZrhla5MjIhOMKagE4zpmZd0aHx0bj4MFZ9wYYpP1QTIrXxMlmWZP4OMs_HZPAkZK_98xBb38O37AkKMY26C4xUs",
//          "refresh_token": "def50200ddb6c935ad133d554977a192743f4f1c4186442cd976e238283d510825847d4cc19353f502a0a9db672461d4fcd332b981806bd43612a3d06c2225e20645934be6f7e978ee5a55fd34adbd2e226973d47fa29d5606514b00161c542b5203ba3a7fdd5c0400a05c6fa56988cd1beeed1f41d43863958b8a45577a5c1bcc50b578e71664de2a5ffea987aef01432866a1ea2bf1b94df4bbdbb2b66e54326a3d3652ad64996837304c25dd842517f366c8e93a91e025d3e065f1565d2e0999382444176cd8ecc089d69c0844f0cf165ebd5d4b3a8febfa653b0a8f7d8255e3a500121619b616737ea499943140434f938579f139052f7ed2197ac8dd169190ed124caf7ef4a64f60c287c6efa014e0964f372bbb76d8f7fa7542e024f304bb5306c40c5279c1d48583ace96edd379aa35af04d36f79c2bda8f789d6cb894f82c09aa8920ed77c911b91195f996d048bd84d22b2460f3f7e9d1bba468da77bf083"
//        }

        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {

            JSONObject object = null;
            Map<String, String> out = new HashMap<>();
            try {
              object = new JSONObject(str);
              JSONObject info = object.getJSONObject("data");
              parse(info, out);
            } catch (JSONException e) {
              e.printStackTrace();
            }
            App.getInstance().getAppSharedHelper().saveTamaAccountTokenType(out.get("token_type"));
            App.getInstance().getAppSharedHelper().saveTamaAccountExpiresIn(
                System.currentTimeMillis() + Long.valueOf(out.get("expires_in")));
            App.getInstance().getAppSharedHelper()
                .saveTamaAccountAccessToken(out.get("access_token"));
            App.getInstance().getAppSharedHelper()
                .saveTamaAccountRefreshToken(out.get("refresh_token"));

            delegate.processFinish(out.get("access_token"));

//            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class GetCountriesOrCotegoriesListTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httpGet, responseHandler);
        return responseBody;
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      if (s != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(s);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(s);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class GetBalanceTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httpGet, responseHandler);

        return responseBody;
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            try {
              listener.requestSuccess(parseJSon(str, "balance"));
            } catch (JSONException e) {
              error = e.getMessage();
              listener.requestError(error);
              e.printStackTrace();
            }
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class GetHeartbeatTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getAccessToken());

        ResponseHandler responseHandler = new BasicResponseHandler();
        String responseBody = (String) httpclient.execute(httpGet, responseHandler);

        return responseBody;
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            /*
            * {
 "data":{
    "code":"0",
    "http_code":200,
    "message":"",
    "result":{
       "balance":"\u20ac0.00",
       "balance_ws":"0.00",
       "user_image":"http:\/\/tamaexpress.com:585\/uploads\/images\/users",
       "tamaexpress":1,
       "tamatopup":1,
       "tamavoucher":1,
       "pay_to_retailer":"1",
       "pay_by_balance":"1",
       "pay_by_cards":"0",
       "pay_by_paypal":"0",
       "min_order_amount_tamaexpress":"20",
       "min_order_amount_tamatopup":"2",
       "promo_tamaexpress_balance":"\u20ac0.00",
       "promo_tamatopup_balance":"\u20ac0.00",
       "promotion_txt":"Promo Balance: TamaExpress: \u20ac0.00 \/ TamaTopup: \u20ac0.00"
    }
 }
}

*/
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class GetIncomingRequestTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getAccessToken());
        ResponseHandler responseHandler = new BasicResponseHandler();
        return (String) httpclient.execute(httpGet, responseHandler);
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class GetOutGoingRequestTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getAccessToken());
        ResponseHandler responseHandler = new BasicResponseHandler();
        return (String) httpclient.execute(httpGet, responseHandler);
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private class GetHistoryTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      try {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getAccessToken());
        ResponseHandler responseHandler = new BasicResponseHandler();
        return (String) httpclient.execute(httpGet, responseHandler);
      } catch (Exception e) {
        error = e.getMessage();
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(String str) {
      super.onPostExecute(str);
      if (str != null) {
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject(str);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        if (jsonObject != null) {
          JSONObject jsonData = jsonObject.optJSONObject("error");
          if (jsonData != null) {
            error = jsonData.optString("message");
            if (error != null) {
              listener.requestError(error);
            }
          } else {
            listener.requestSuccess(str);
          }
        }
      } else {
        listener.requestError(error);
      }
    }
  }

  private String parseJSon(String data, String key) throws JSONException {
    if (data == null) {
      return null;
    }
    String value = null;
    JSONObject jsonObject = new JSONObject(data);
//        JSONObject jsonData = jsonObject.optJSONObject("data"); //data
    if (jsonObject != null) {
      value = jsonObject.getString(key);
    } else {
      jsonObject = jsonObject.optJSONObject("error");
      if (jsonObject != null) {
        value = jsonObject.getString("message");
      }
    }

    return value;
  }

  private static String parseJSonNew(String data, String key) throws JSONException {
    if (data == null) {
      return null;
    }
    String newData = data.substring(3, data.length());
    JSONObject jsonObject = new JSONObject("{" + newData);
    JSONObject jsonData = jsonObject.getJSONObject("data");
    String value = jsonData.getString(key);

    return value;
  }

  private String GetCountryZipCode(Context context) {
    String CountryID = "";
    String CountryZipCode = "";

    TelephonyManager manager = (TelephonyManager) context
        .getSystemService(Context.TELEPHONY_SERVICE);
    //getNetworkCountryIso
    CountryID = manager.getSimCountryIso().toUpperCase();
    String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
    for (int i = 0; i < rl.length; i++) {
      String[] g = rl[i].split(",");
      if (g[1].trim().equals(CountryID.trim())) {
        CountryZipCode = g[0];
        break;
      }
    }
    return CountryZipCode;
  }

  public HttpResponse makeRequest(String path, JSONObject holder) throws Exception {
    DefaultHttpClient httpclient = new DefaultHttpClient();

    HttpPost httpost = new HttpPost(path);

    StringEntity se = new StringEntity(holder.toString());

    httpost.setEntity(se);
    //sets a request header so the page receving the request
    //will know what to do with it
    httpost.setHeader("Accept", "application/json");
    httpost.setHeader("Content-type", "application/json");
    httpost.setHeader("Authorization", "Bearer " + getAccessToken());

    //Handles what is returned from the page
    ResponseHandler responseHandler = new BasicResponseHandler();
    httpclient.execute(httpost, responseHandler);
    return null;
  }

  private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {
    Iterator iter = params.entrySet().iterator();

    JSONObject holder = new JSONObject();
    while (iter.hasNext()) {
      Map.Entry pairs = (Map.Entry) iter.next();
      String key = (String) pairs.getKey();
      Map m = (Map) pairs.getValue();
      JSONObject data = new JSONObject();
      Iterator iter2 = m.entrySet().iterator();
      while (iter2.hasNext()) {
        Map.Entry pairs2 = (Map.Entry) iter2.next();
        data.put((String) pairs2.getKey(), (String) pairs2.getValue());
      }

      holder.put(key, data);
    }
    return holder;
  }

  public interface AsyncResponse {
    void processFinish(String output);
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