package com.tama.chat.ui.activities.tamaaccount;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ContactUsActivity extends TamaAccountBaseActivity implements TamaAccountHelperListener{

    private List<Pair<String,String>> numberBook;
    private String[] emails;
    private String phoneFrance, phoneGermany;

    @Bind(R.id.france_number)
    TextView franceNumber;

    @Bind(R.id.germany_number)
    TextView germanyNumber;

    @Bind(R.id.email_text)
    TextView emailText;

    @Override
    protected int getContentResId() {
        return R.layout.activity_contact_us;
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTamaToolbar(R.string.contact_us_uppercase,R.string.contact_us_uppercase);
        new TamaAccountHelper().setContactUs(this);
    }

    @Override
    public void requestSuccess(String data) {
        super.requestSuccess(data);
        try {
            parseJSon(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(phoneFrance!=null){
            franceNumber.setText(phoneFrance);
        }
        if(phoneGermany!=null){
            germanyNumber.setText(phoneGermany);
        }
        if(emails!=null && emails.length>0){
            emailText.setText(emails[0]);
        }
    }

    @Override
    public void requestError(String data) {
        super.requestError(data);
        ToastUtils.longToast(data);
    }

    @Override
    public void alertDialogCancelListener() {
        super.alertDialogCancelListener();
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;
        numberBook = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonData = jsonObject.optJSONObject("data");
        JSONArray jsonArrayMail;
        JSONObject jsonObjectPhone;
        if(jsonData!=null) {
            jsonArrayMail = jsonData.optJSONArray("email");
            if(jsonArrayMail!=null && jsonArrayMail.length()>0) {
                emails = new String[jsonArrayMail.length()];
                for (int i = 0; i < jsonArrayMail.length(); ++i) {
                    emails[i] = (String) jsonArrayMail.get(i);
                }
            }
            jsonObjectPhone = jsonData.optJSONObject("phone");
            if(jsonObjectPhone!=null){
                phoneFrance = jsonObjectPhone.optString("France");
                phoneGermany = jsonObjectPhone.optString("Germany");
            }
        }
    }
}
