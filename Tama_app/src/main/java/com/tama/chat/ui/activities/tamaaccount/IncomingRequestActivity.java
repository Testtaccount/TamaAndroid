package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.method.Methods.convertDpToPixel;
import static com.tama.chat.ui.activities.tamaaccount.IncomingRequestActivity.RequestType.INCOMING_REQUEST;
import static com.tama.chat.ui.activities.tamaaccount.IncomingRequestActivity.RequestType.OK_DENIED_REQUEST;
import static com.tama.chat.ui.activities.tamaaccount.IncomingRequestActivity.RequestType.OUTGOING_REQUEST;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.IncomingRequestAdapter;
import com.tama.chat.tamaAccount.IncomingRequestElement;
import com.tama.chat.tamaAccount.OutGoingRequestAdapter;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.utils.helpers.SharedHelper;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IncomingRequestActivity extends TamaAccountBaseActivity {

    private List<IncomingRequestElement> incomingRequestElements;
    private RequestType requestType;
    private LinearLayout.LayoutParams paramSelected, paramUnSelected;
    private String ALL = "all";
    private String MY_TAMA = "mytama";
    private String TAMA_TOPUP = "tama-topup";
    private String TAMA_EXPRESS = "tamaexpress";


    @Bind(R.id.request_item_show_layout)
    RelativeLayout requestItemShowLayout;

    @Bind(R.id.title_layout)
    LinearLayout titleLayout;

    @Bind(R.id.incoming_request_list)
    ListView incomingRequestList;

    @Bind(R.id.item_show_text)
    TextView itemShowText;

    @Bind(R.id.error_message_text)
    TextView errorMessageText;

    @Bind(R.id.all_text_btn)
    TextView allTextBtn;

    @Bind(R.id.my_tama_text_btn)
    TextView myTamaTextBtn;

    @Bind(R.id.tama_express_text_btn)
    TextView tamaExpressTextBtn;

    @Bind(R.id.tama_topup_text_btn)
    TextView tamaTopupTextBtn;

    @Bind(R.id.btn_item_ok)
    Button btnItemOk;

    @Bind(R.id.btn_item_denied)
    Button btnItemDenied;

    @Bind(R.id.btn_incoming_request)
    Button btnIncomingRequest;

    @Bind(R.id.btn_outgoing_request)
    Button btnOutgoingRequest;

    @OnClick(R.id.all_text_btn)
    public void onClickAllTextBtn(){
        setButtonEnable(false);
        setTextColorAndBackGround(allTextBtn);
        initFields(OUTGOING_REQUEST,ALL);
    }

    @OnClick(R.id.my_tama_text_btn)
   public void onClickMyTamaTextBtn(){
        setButtonEnable(false);
        setTextColorAndBackGround(myTamaTextBtn);
        initFields(OUTGOING_REQUEST,MY_TAMA);
    }

    @OnClick(R.id.tama_express_text_btn)
    public void onClickTamaExpressTextBtn(){
        setButtonEnable(false);
        setTextColorAndBackGround(tamaExpressTextBtn);
        initFields(OUTGOING_REQUEST,TAMA_EXPRESS);
    }

    @OnClick(R.id.tama_topup_text_btn)
    public void onClickTamaTopupTextBtn(){
        setButtonEnable(false);
        setTextColorAndBackGround(tamaTopupTextBtn);
        initFields(OUTGOING_REQUEST,TAMA_TOPUP);
    }

    @OnClick(R.id.btn_incoming_request)
    public void onClickIncomingBtn(){
        btnIncomingRequest.setBackgroundResource(R.drawable.selector_button_red_rect);
        btnOutgoingRequest.setBackgroundResource(R.drawable.selector_button_grey_rect);
        btnIncomingRequest.setLayoutParams(paramSelected);
        btnOutgoingRequest.setLayoutParams(paramUnSelected);
        titleLayout.setVisibility(View.GONE);
        incomingRequestList.setAdapter(null);
        initFields(INCOMING_REQUEST,null);
        setButtonEnable(false);
    }

    @OnClick(R.id.btn_outgoing_request)
    public void onClickOutGoingBtn(){
        btnIncomingRequest.setBackgroundResource(R.drawable.selector_button_grey_rect);
        btnOutgoingRequest.setBackgroundResource(R.drawable.selector_button_red_rect);
        btnIncomingRequest.setLayoutParams(paramUnSelected);
        btnOutgoingRequest.setLayoutParams(paramSelected);
        titleLayout.setVisibility(View.VISIBLE);
        incomingRequestList.setAdapter(null);
        initFields(OUTGOING_REQUEST,ALL);
        setButtonEnable(false);
    }

//    @Bind(R.id.btn_back)
//    Button btnBack;

    @Override
    protected int getContentResId() {
        return R.layout.activity_incoming_request;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTamaToolbar(R.string.incoming_request_one_line,R.string.tama_request);
        createLayoutParams();
        initFields(INCOMING_REQUEST,null);

    }

    private void createLayoutParams() {
        paramSelected = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,convertDpToPixel(23,this));
        paramSelected.weight = 1;
        paramSelected.gravity = Gravity.CENTER;
        paramUnSelected = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,convertDpToPixel(19,this));
        paramUnSelected.weight = 1;
        paramUnSelected.gravity = Gravity.BOTTOM;

    }

    private void initFields(RequestType _requestType, String type) {
        String user_id =new SharedHelper(this).getTamaAccountId();
        errorMessageText.setVisibility(View.GONE);
        incomingRequestList.setVisibility(View.VISIBLE);
        incomingRequestList.setAdapter(null);
        requestItemShowLayout.setVisibility(View.GONE);
        if(isNetworkAvailable()) {
            requestType = _requestType;
            if(requestType==INCOMING_REQUEST) {
                new TamaAccountHelper().getIncomingRequest(this, user_id);
            }else if(requestType==OUTGOING_REQUEST){
                new TamaAccountHelper().getOutGoingRequest(this, user_id,type);
            }
        }else{
            Toast.makeText(this, getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void requestError(String data) {
        super.requestError(data);
        errorMessageText.setVisibility(View.VISIBLE);
        errorMessageText.setText(data);
        incomingRequestList.setVisibility(View.GONE);
        requestItemShowLayout.setVisibility(View.GONE);
        setButtonEnable(true);
    }

    @Override
    public void requestSuccess(String data) {
        if(requestType == INCOMING_REQUEST) {
            parseJSonIncomingRequestElements(data);
            if (incomingRequestElements != null) {
                incomingRequestList.setAdapter(new IncomingRequestAdapter(this, incomingRequestElements));
                incomingRequestList.setOnItemClickListener(itemClickListener);
            }
        }else if(requestType == OUTGOING_REQUEST){
            parseJSonIncomingRequestElements(data);
            if (incomingRequestElements != null) {
                incomingRequestList.setAdapter(new OutGoingRequestAdapter(this, incomingRequestElements));
                incomingRequestList.setOnItemClickListener(null);
//                incomingRequestList.setOnItemClickListener(itemClickListener);
            }
        }else{
            createDialog("",data,"");
        }
        setButtonEnable(true);
    }

    @Override
    public void alertDialogCancelListener() {
        super.alertDialogCancelListener();
//        incomingRequestList.setVisibility(View.VISIBLE);
//        requestItemShowLayout.setVisibility(View.GONE);
//        onResume();
        initFields(requestType,null);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                incomingRequestList.setVisibility(View.GONE);
                requestItemShowLayout.setVisibility(View.VISIBLE);
                IncomingRequestElement element = incomingRequestElements.get(position);
                itemShowText.setText(element.getMessage());
                btnItemOk.setOnClickListener(getOnClickListener(element,"accept"));
                btnItemDenied.setOnClickListener(getOnClickListener(element,"deny"));
            }
        };



        private View.OnClickListener getOnClickListener(IncomingRequestElement element,final String answer){
            final String user_id = new SharedHelper(this).getTamaAccountId();
            final String request_id = element.getRequestId();
            final TamaAccountHelperListener listener = this;
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()) {
                        requestType = OK_DENIED_REQUEST;
                        setButtonEnable(false);
                        new TamaAccountHelper().setOkDeniedRequest(listener,user_id,request_id,answer);
                    }else{
                        Toast.makeText(getApplicationContext(), getString(R.string.no_internet_conection), Toast.LENGTH_SHORT).show();
                    }
                }
            };
        }

    private void setButtonEnable(boolean b) {
        btnItemOk.setEnabled(b);
        btnItemDenied.setEnabled(b);
        btnIncomingRequest.setEnabled(b);
        btnOutgoingRequest.setEnabled(b);
        allTextBtn.setEnabled(b);
        myTamaTextBtn.setEnabled(b);
        tamaExpressTextBtn.setEnabled(b);
        tamaTopupTextBtn.setEnabled(b);
    }

    private void setTextColorAndBackGround(TextView currentTextView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            allTextBtn.setTextColor(getColor(R.color.button_red_normal));
            myTamaTextBtn.setTextColor(getColor(R.color.button_red_normal));
            tamaExpressTextBtn.setTextColor(getColor(R.color.button_red_normal));
            tamaTopupTextBtn.setTextColor(getColor(R.color.button_red_normal));
            currentTextView.setTextColor(getColor(R.color.text_dark));
        }else{
            allTextBtn.setTextColor(getResources().getColor(R.color.button_red_normal));
            myTamaTextBtn.setTextColor(getResources().getColor(R.color.button_red_normal));
            tamaExpressTextBtn.setTextColor(getResources().getColor(R.color.button_red_normal));
            tamaTopupTextBtn.setTextColor(getResources().getColor(R.color.button_red_normal));
            currentTextView.setTextColor(getResources().getColor(R.color.text_dark));
        }
        allTextBtn.setBackgroundResource(0);
        myTamaTextBtn.setBackgroundResource(0);
        tamaExpressTextBtn.setBackgroundResource(0);
        tamaTopupTextBtn.setBackgroundResource(0);
        currentTextView.setBackgroundResource(R.drawable.selector_button_red_under_line);
    }

    private void parseJSonIncomingRequestElements(String data) {
        if (data == null)
            return;
        incomingRequestElements = new ArrayList<>();
        try {
            JSONObject jsonData = new JSONObject(data);
            JSONArray jsonElements = jsonData.getJSONArray("data");

            for (int i = 0; i < jsonElements.length(); i++) {
                JSONObject jsonElement = jsonElements.getJSONObject(i);
                IncomingRequestElement incomingRequestElement = new IncomingRequestElement();
                incomingRequestElement.setRequestId(jsonElement.getString("request_id"));
                incomingRequestElement.setRequestFor(jsonElement.getString("request_for"));
                incomingRequestElement.setRequestFrom(jsonElement.optString("request_from"));
                incomingRequestElement.setRequestAmount(jsonElement.getString("request_amount"));
                incomingRequestElement.setMessage(jsonElement.getString("message"));
                incomingRequestElement.setImageUrl(jsonElement.optString("image"));
                incomingRequestElement.setRequestStatus(jsonElement.optString("request_status"));
                incomingRequestElements.add(incomingRequestElement);
            }
            JSONObject jsonMeta = jsonData.getJSONObject("meta");
        } catch (JSONException e) {
            requestError(e.getMessage());
            e.printStackTrace();
        }
    }

//    @OnClick(R.id.btn_back)
//    void onClickBackButton(){
//        onBackPressed();
//    }

    enum RequestType{
        INCOMING_REQUEST, OK_DENIED_REQUEST, OUTGOING_REQUEST
    }
}
