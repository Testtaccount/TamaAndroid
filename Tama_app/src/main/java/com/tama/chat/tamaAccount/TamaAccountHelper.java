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

    private static final String MY_TAMA_URL = "http://tamaexpress.com:585/api/";

    private static final String REGISTER_USER = MY_TAMA_URL + "register";
    private static final String GET_BALANCE = MY_TAMA_URL + "heartbeat/";
    private static final String GET_COUTRIES_LIST = MY_TAMA_URL + "sendtama";
    private static final String GET_COTEGORIES_LIST = MY_TAMA_URL + "sendtama/categories";
    private static final String GET_INCOMING_REQUEST = MY_TAMA_URL + "requests/";
    private static final String GET_OUTGOING_REQUEST = MY_TAMA_URL + "request/sent/";
    private static final String CONFIRM = MY_TAMA_URL + "sendtama/confirm";
    private static final String REVOKE_ACCOUNT = MY_TAMA_URL + "revoke/access";
    private static final String CONTACT_US = MY_TAMA_URL + "contact-us";
    //    private static final String TOPUP_ACCOUNT = MY_TAMA_URL + "topup";
    private static final String TOPUP_ACCOUNT = MY_TAMA_URL + "topup";
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

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "access_token";

    //    private static final String accessToken = "KK5XzdmmYkCYVnM0p5HdTmSwlNHW0RZg3pIIXfKb";
    private static String accessToken = "";
    private static String refreshToken = "";
    private static final String TAG = "myLogs";

    private static Context context;
    private TamaAccountHelperListener listener;
    private String error;
    private boolean isRequestOrder;



    private static String getAccessToken() {
//        context = getActivity();
//        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

//        accessToken = preferences.getString(ACCESS_TOKEN, "DEFAULT");
        return App.getInstance().getAppSharedHelper().getTamaAccountAccessToken();
    }
//
//    private static String getRefreshToken() {
//        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//
//        refreshToken = preferences.getString(REFRESH_TOKEN, "DEFAULT");
//        return refreshToken;
//    }

    public TamaAccountHelper() {}

    private String Languages(){
        return "?lang=" + getLanguage(listener.getAppContext());
    }

    public void register(Context context,TamaAccountHelperListener listener, String phoneNumber, String first_name, String last_name, String avatar)
            throws UnsupportedEncodingException {
        this.context = context;
        this.listener = listener;

        Log.d(TAG,"phoneNumber = " + phoneNumber);

        new SharedHelper(context).saveTamaAccountPhoneNumber(phoneNumber);
        new RegisterRequestTask().execute(REGISTER_USER + Languages(),phoneNumber, first_name, last_name, avatar);
    }

    public void sendTrasferRequest(TamaAccountHelperListener listener, String user_id, String country_code, String transfer_to, String amount){
        this.listener = listener;
        new BalanceTransferTask().execute(BALANCE_TRANSFER+ Languages(),user_id,country_code,transfer_to,amount);
    }

    public void getListOfCountries(TamaAccountHelperListener listener){
        this.listener = listener;
        new GetCountriesOrCotegoriesListTask().execute(GET_COUTRIES_LIST + Languages());
    }

    public void getListOfCategories(TamaAccountHelperListener listener){
        this.listener = listener;
        new GetCountriesOrCotegoriesListTask().execute(GET_COTEGORIES_LIST + Languages());
    }

    public void getListOfProducts(TamaAccountHelperListener listener, String country,String category_id){
        this.listener = listener;
        new GetCountriesOrCotegoriesListTask().execute(GET_COUTRIES_LIST +"/"+country + "/"+category_id+"/products"+Languages());
    }

    public void checkTamaUserOrNot(TamaAccountHelperListener listener, String country_code, String phone_no){
        this.listener = listener;
        new CheckTamaUserTask().execute(CHECK_TAMA_USER+Languages(),country_code,phone_no);
    }

    public void confirmTamaTopUp(TamaAccountHelperListener listener, String user_id, String country_code, String topup_no,
                                 String euro_amount, String local_amount, String dest_currency){
        this.listener = listener;
        new ConfirmTamaTopUpTask().execute(CONFIRM_TOPUP+Languages(), user_id, country_code, topup_no, euro_amount,
                local_amount,dest_currency);
    }

    public void sendMyTamaAccountTopUpRqt(TamaAccountHelperListener listener,String user_id, String country_code, String payer_no, String amount){
        this.listener = listener;
        new MyTamaAccountTopUpRqtTask().execute(MYTAMA_ACCOUNT_TOPUP_REQUEST+Languages(),user_id,country_code,payer_no, amount);
    }

    public void sendPayTamaExpressRqt(TamaAccountHelperListener listener,String user_id, String country_code, String payer_no, String tama_pin){
        this.listener = listener;
        new PayTamaExpressRequestTask().execute(PAY_TAMA_EXPRESS_REQUEST+Languages(),user_id,country_code,payer_no, tama_pin);
    }

    public void setOkDeniedRequest(TamaAccountHelperListener listener,String user_id, String request_id, String answer){
        this.listener = listener;
        new AcceptDeniedIncomingRqtTask().execute(ACCEPT_DENIED_REQUEST+answer+Languages(),user_id,request_id);
    }

    public void sendMyTamaMobileTopUpRqt(TamaAccountHelperListener listener,String user_id, String payer_country_code, String payer_no,
                                         String topup_country_code, String topup_no,  String amount){
        this.listener = listener;
        new MyTamaMobileTopUpRqtTask().execute(TOPUP_MY_MOBILE_REQUEST+Languages(),user_id,payer_country_code,
                payer_no, topup_country_code,topup_no,amount);
    }

    public void setConfirm(TamaAccountHelperListener listener,String user_id, String products, String sender_name,
                           String sender_mobile, String receiver_name,  String receiver_mobile,String requestType){
        this.listener = listener;
        new ConfirmTask().execute(CONFIRM+Languages(),user_id,products,sender_name, sender_mobile,receiver_name,receiver_mobile,requestType);
    }

    public void setRevokeAccount(TamaAccountHelperListener listener,String user_id){
        this.listener = listener;
        new RevokeAccountTask().execute(REVOKE_ACCOUNT,user_id);
    }

    public void setContactUs(TamaAccountHelperListener listener){
        this.listener = listener;
        new ContactUSTask().execute(CONTACT_US+Languages());
    }

    public void getDenominations(TamaAccountHelperListener listener, String user_id,String country_code, String topup_no){
        this.listener = listener;
        new MobileTopUpGetDenominations().execute(MOBILE_TOPUP+Languages(),user_id, country_code, topup_no);
    }

    public void getBalance(Context context, TamaAccountHelperListener listener, String user_id){
        this.context = context;
        this.listener = listener;
        Log.d(TAG,"user_id = " + user_id);
        new GetBalanceTask().execute(GET_BALANCE+Languages());
    }

    public void sendTopupRequest(Context context, TamaAccountHelperListener listener, String voucher){
        this.context = context;
        this.listener = listener;
        String user_id = new SharedHelper(context).getTamaAccountId();
        new TopupRequestTask().execute(TOPUP_ACCOUNT+Languages(),user_id,voucher);
    }

    public void getHistory(TamaAccountHelperListener listener, String user_id, String history_type){
        this.listener = listener;

//        new GetHistoryTask().execute(GET_HISTORY + 1 + "/" + history_type);
        new GetHistoryTask().execute(GET_HISTORY + user_id + "/" + history_type+Languages());
    }

    public void getSingleHistory(TamaAccountHelperListener listener, String user_id, String history_id){
        this.listener = listener;
        new GetHistoryTask().execute(GET_SINGLE_HISTORY + user_id + "/" + history_id+Languages());
//        new GetHistoryTask().execute(GET_SINGLE_HISTORY + 1 + "/" + history_id);
    }

    public void getIncomingRequest(TamaAccountHelperListener listener, String user_id){
        this.listener = listener;
        new GetIncomingRequestTask().execute(GET_INCOMING_REQUEST+user_id+Languages());
//        new GetIncomingRequestTask().execute(GET_INCOMING_REQUEST+"1");
    }

    public void getOutGoingRequest(TamaAccountHelperListener listener, String user_id, String type){
        this.listener = listener;
        new GetOutGoingRequestTask().execute(GET_OUTGOING_REQUEST + user_id + "/" + type+Languages());
    }

    private class ConfirmTamaTopUpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                JSONObject json = new JSONObject();
                json.put("user_id", params[1]);
                json.put("country_code", params[2]);
                json.put("topup_no", params[3]);
                json.put("euro_amount", params[4]);
                json.put("local_amount", params[5]);
                json.put("dest_currency", params[6]);
                StringEntity paramsStr = new StringEntity(json.toString());
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(paramsStr);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());
                ResponseHandler responseHandler = new BasicResponseHandler();
                return (String) httpclient.execute(httppost, responseHandler);
//                return "{\"data\":{\"user_id\":\"16\",\"balance\":\"371.22 \\u20ac\",\"history_id\":246}}";
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class MobileTopUpGetDenominations extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                JSONObject json = new JSONObject();
                json.put("user_id", params[1]);
                json.put("country_code", params[2]);
                json.put("topup_no", params[3]);
                StringEntity paramsStr = new StringEntity(json.toString());
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(paramsStr);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());
                ResponseHandler responseHandler = new BasicResponseHandler();
                return (String) httpclient.execute(httppost, responseHandler);
//                return "{\"data\":{\"mobile_number\":\"918122535488\\r\",\"country\":\"India\\r\",\"operator\":\"Vodafone Chennai India\\r\",\"destination_currency\":\"INR\\r\",\"product_list\":{\"0\":\"10\",\"1\":\"20\",\"2\":\"30\",\"3\":\"50\",\"4\":\"100\",\"5\":\"112\",\"6\":\"351\",\"7\":\"500\\r\"},\"retail_price_list\":{\"0\":\"0.30\",\"1\":\"0.50\",\"2\":\"0.70\",\"3\":\"1.10\",\"4\":\"2.10\",\"5\":\"2.30\",\"6\":\"7.00\",\"7\":\"10.00\\r\"}}}";
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(s);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class TopupRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                JSONObject json = new JSONObject();
                json.put("user_id", params[1]);
                json.put("voucher_no", params[2]);
                StringEntity paramsStr = new StringEntity(json.toString());
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());
                httppost.setEntity(paramsStr);
                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httppost, responseHandler);
                String vaucherErrorString= "{ \"error\": { \"code\": 404, \"http_code\": \"404\", \"message\": \"Error, Voucher is not valid!\" } }";
                return responseBody;//"90.00 €";//parseJSon(responseBody,"balance");//{"data":{"user_id":"68","balance":"60.00 \u20ac"}}
            } catch (Exception e) {
                error = "Error, Voucher is not valid!";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        try {
                            listener.requestSuccess(parseJSon(str,"balance"));
                        } catch (JSONException e) {
                            error = e.getMessage();
                            listener.requestError(error);
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class BalanceTransferTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
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
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httppost, responseHandler);


                String balance = parseJSon(responseBody,"balance");
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class ConfirmTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                JSONObject json = new JSONObject();
                json.put("user_id", params[1]);
                json.put("products", params[2]);
                json.put("sender_name", params[3]);
                json.put("sender_mobile", params[4]);
                json.put("receiver_name", params[5]);
                json.put("receiver_mobile", params[6]);
                json.put("pay_by", params[7]);

                StringEntity paramsStr = new StringEntity(json.toString());

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(paramsStr);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

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
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        try {
                            listener.requestSuccess(parseJSon(str,"message"));
                        } catch (JSONException e) {
                            error = e.getMessage();
                            listener.requestError(error);
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class RevokeAccountTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try{
                JSONObject json = new JSONObject();
                json.put("user_id", params[1]);
                StringEntity paramsStr = new StringEntity(json.toString());

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(paramsStr);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

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
            if (str != null && !str.isEmpty()){
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
                    }
                    else if (jsonData.optInt("code") == 404)
                    {
                        listener.requestError(jsonData.optString("message"));
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class ContactUSTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer "+getAccessToken());

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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class MyTamaMobileTopUpRqtTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
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
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httppost, responseHandler);


                String mobile_no = parseJSon(responseBody,"message");
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class AcceptDeniedIncomingRqtTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                JSONObject json = new JSONObject();
                json.put("user_id", params[1]);
                json.put("request_id", params[2]);
                StringEntity paramsStr = new StringEntity(json.toString());

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(paramsStr);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httppost, responseHandler);


                String mobile_no = parseJSon(responseBody,"message");
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class PayTamaExpressRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
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
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httppost, responseHandler);


                String mobile_no = parseJSon(responseBody,"message");
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class MyTamaAccountTopUpRqtTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
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
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httppost, responseHandler);


                String mobile_no = parseJSon(responseBody,"message");
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class CheckTamaUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                JSONObject json = new JSONObject();
                json.put("country_code", params[1]);
                json.put("mobile_no", params[2]);
                StringEntity paramsStr = new StringEntity(json.toString());

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(paramsStr);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httppost, responseHandler);


                String mobile_no = parseJSon(responseBody,"mobile_no");
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class RegisterRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                JSONObject json = new JSONObject();

                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                String phone ="+" + params[1];
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

                String mobile_no = (params[1]).replace(country_code,"");

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
                httppost.setHeader("Authorization", "Bearer "+getAccessToken());

                ResponseHandler responseHandler = new BasicResponseHandler();
                String responseBody = (String) httpclient.execute(httppost, responseHandler);

//                String user_id = parseJSon(responseBody,"user_id");
//                accessToken = parseJSon(responseBody,"access_token");
//
//                SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString(ACCESS_TOKEN, accessToken);
//                editor.commit();
//
//                refreshToken = parseJSon(responseBody,"refresh_token");
//                editor.putString(REFRESH_TOKEN, refreshToken);
//                editor.commit();
//                return parseJSon(responseBody,"user_id");
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class GetCountriesOrCotegoriesListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer "+getAccessToken());

                ResponseHandler responseHandler = new BasicResponseHandler();
                return (String) httpclient.execute(httpGet, responseHandler);
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            super.onPostExecute(s);
            if(s!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(s);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class GetBalanceTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{

                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer "+getAccessToken());

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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        try {
                            listener.requestSuccess(parseJSon(str,"balance"));
                        } catch (JSONException e) {
                            error = e.getMessage();
                            listener.requestError(error);
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class GetIncomingRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer "+getAccessToken());
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class GetOutGoingRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer "+getAccessToken());
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private class GetHistoryTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                httpGet.setHeader("Accept", "application/json");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer "+getAccessToken());
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
            if(str!=null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null) {
                    JSONObject jsonData = jsonObject.optJSONObject("error");
                    if(jsonData!=null){
                        error = jsonData.optString("message");
                        if(error!=null)
                            listener.requestError(error);
                    }else{
                        listener.requestSuccess(str);
                    }
                }
            }else{
                listener.requestError(error);
            }
        }
    }

    private String parseJSon(String data, String key) throws JSONException {
        if (data == null)
            return null;
        String value = null;
        JSONObject jsonObject = new JSONObject(data);
//        JSONObject jsonData = jsonObject.optJSONObject("data"); //data
        if(jsonObject!=null) {
            value = jsonObject.getString(key);
        }else{
            jsonObject = jsonObject.optJSONObject("error");
            if(jsonObject!=null) {
                value = jsonObject.getString("message");
            }
        }

        return value;
    }

    private static String parseJSonNew(String data, String key) throws JSONException {
        if (data == null)
            return null;
        String newData = data.substring(3,data.length());
        JSONObject jsonObject = new JSONObject("{"+newData);
        JSONObject jsonData = jsonObject.getJSONObject("data");
        String value = jsonData.getString(key);

        return value;
    }

    private String GetCountryZipCode(Context context){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    public static HttpResponse makeRequest(String path, JSONObject holder) throws Exception{
        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpPost httpost = new HttpPost(path);

        StringEntity se = new StringEntity(holder.toString());

        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "application/json");
        httpost.setHeader("Content-type", "application/json");
        httpost.setHeader("Authorization", "Bearer "+getAccessToken());

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        httpclient.execute(httpost, responseHandler);
        return null;
    }

    private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {
        Iterator iter = params.entrySet().iterator();

        JSONObject holder = new JSONObject();
        while (iter.hasNext())
        {
            Map.Entry pairs = (Map.Entry)iter.next();
            String key = (String)pairs.getKey();
            Map m = (Map)pairs.getValue();
            JSONObject data = new JSONObject();
            Iterator iter2 = m.entrySet().iterator();
            while (iter2.hasNext())
            {
                Map.Entry pairs2 = (Map.Entry)iter2.next();
                data.put((String)pairs2.getKey(), (String)pairs2.getValue());
            }

            holder.put(key, data);
        }
        return holder;
    }
}
