package com.tama.chat.ui.fragments.tamaaccount;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.tamaAccount.TamaHistoryAdapter;
import com.tama.chat.tamaAccount.TamaHistoryElement;
import com.tama.chat.ui.activities.tamaaccount.TamaHistoryActivity;
import com.tama.chat.utils.helpers.SharedHelper;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TamaHistoryFragment extends Fragment implements TamaAccountHelperListener {

    private ArrayList<TamaHistoryElement> elements = new ArrayList<>();
    private String HISTORY_ALL = "all";
    private String HISTORY_MYTAMA = "mytama";
    private String HISTORY_TAMATOPUP = "tama-topup";
    private String HISTORY_TAMAEXPRESS = "tamaexpress";
    private String HISTORY_TRANSFER = "transfer";
    private String user_id;
    private static final String ARG_PARAM = "param";
    private TamaHistoryActivity activity;

    @Bind(R.id.list_view)
    ListView historyElementList;

    @Bind(R.id.error_message_text)
    TextView errorMessageText;

    @Bind(R.id.history_all)
    TextView historyAll;

    @Bind(R.id.history_my_tama)
    TextView historyMyTama;

    @Bind(R.id.history_tama_express)
    TextView historyTamaExpress;

    @Bind(R.id.history_tama_topup)
    TextView historyTamaTopup;

//    @Bind(R.id.history_balance_transfer)
//    TextView historyBalanceTransfer;

    @OnClick(R.id.history_all)
    public void onClickHistoryAll(){
        onClickHistoryType(historyAll,HISTORY_ALL);
    }

    @OnClick(R.id.history_my_tama)
    public void onClickHistoryMyTama(){
        onClickHistoryType(historyMyTama,HISTORY_MYTAMA);
    }

    @OnClick(R.id.history_tama_express)
    public void onClickHistoryTamaExpress(){
        onClickHistoryType(historyTamaExpress,HISTORY_TAMAEXPRESS);
    }

    @OnClick(R.id.history_tama_topup)
    public void onClickHistoryTamaTopup(){
        onClickHistoryType(historyTamaTopup,HISTORY_TAMATOPUP);
    }

//    @OnClick(R.id.history_balance_transfer)
//    public void onClickHistoryBalanceTransfer(){
//        onClickHistoryType(historyBalanceTransfer,HISTORY_TRANSFER);
//    }

    public static TamaHistoryFragment newInstance(String param) {
        TamaHistoryFragment fragment = new TamaHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam = getArguments().getString(ARG_PARAM);
            elements = getHistoryElementListByString(mParam);
        }
        user_id = new SharedHelper(getActivity()).getTamaAccountId();
    }

    @Override
    public void onResume() {
        super.onResume();
        onClickHistoryType(historyAll,HISTORY_ALL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tama_history, container, false);
        ButterKnife.bind(this, view);
//        historyElementList.setOnItemClickListener(getItemClickListener());
        historyElementList.setAdapter(new TamaHistoryAdapter(activity, elements));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TamaHistoryActivity) {
            activity = (TamaHistoryActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void onClickHistoryType(TextView textView, String history_type) {
        setTextColorAndBackGround(textView);
        setButtonEnable(false);
        historyElementList.setAdapter(null);
        errorMessageText.setVisibility(View.GONE);
        new TamaAccountHelper().getHistory(this,user_id,history_type);
    }

    @Override
    public void requestError(String data) {
        errorMessageText.setVisibility(View.VISIBLE);
//        errorMessageText.setText(data);
        errorMessageText.setText(R.string.you_have_no_history);
        setButtonEnable(true);
    }

    @Override
    public void alertDialogCancelListener() {

    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    @Override
    public void requestSuccess(String data) {
        elements = getHistoryElementListByString(data);
//        historyElementList.setOnItemClickListener(getItemClickListener());
        historyElementList.setAdapter(new TamaHistoryAdapter(activity, elements));
        setButtonEnable(true);
    }

    private AdapterView.OnItemClickListener getItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TamaHistoryElement element = elements.get(position);
                activity.setCurrentFragment(TamaSingleHistoryFragment
                        .newInstance(user_id,element.getHistoryId()));
            }
        };
    }

    private void setTextColorAndBackGround(TextView currentTextView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            historyAll.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
            historyMyTama.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
//            historyBalanceTransfer.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
            historyTamaExpress.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
            historyTamaTopup.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
            currentTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.text_dark));
        }else{
            historyAll.setTextColor(getResources().getColor(R.color.button_red_normal));
            historyMyTama.setTextColor(getResources().getColor(R.color.button_red_normal));
//            historyBalanceTransfer.setTextColor(getResources().getColor(R.color.button_red_normal));
            historyTamaExpress.setTextColor(getResources().getColor(R.color.button_red_normal));
            historyTamaTopup.setTextColor(getResources().getColor(R.color.button_red_normal));
            currentTextView.setTextColor(getResources().getColor(R.color.text_dark));
        }
        historyAll.setBackgroundResource(0);
        historyMyTama.setBackgroundResource(0);
//        historyBalanceTransfer.setBackgroundResource(0);
        historyTamaExpress.setBackgroundResource(0);
        historyTamaTopup.setBackgroundResource(0);
        currentTextView.setBackgroundResource(R.drawable.selector_button_red_under_line);
    }

    private void setButtonEnable(boolean b){
        historyAll.setEnabled(b);
        historyMyTama.setEnabled(b);
        historyTamaExpress.setEnabled(b);
        historyTamaTopup.setEnabled(b);
//        historyBalanceTransfer.setEnabled(b);
    }

    private ArrayList<TamaHistoryElement> getHistoryElementListByString(String data) {
        return parseJSon(data);
    }

    private ArrayList<TamaHistoryElement> parseJSon(String data) {
        if (data == null)
            return null;
        ArrayList<TamaHistoryElement> elementList = new ArrayList<>();

        try {
            JSONObject jsonData = new JSONObject(data);
            JSONArray jsonElements = jsonData.getJSONArray("data");

            for (int i = 0; i < jsonElements.length(); i++) {
                JSONObject jsonElement = jsonElements.getJSONObject(i);
                TamaHistoryElement historyElement = new TamaHistoryElement();
                historyElement.setUser_id(jsonElement.getString("user_id"));
                historyElement.setHistoryID(jsonElement.getString("history_id"));
                historyElement.setHistoryName(jsonElement.getString("history_name"));
                historyElement.setAmount(jsonElement.getString("amount"));
                historyElement.setPhoneNumber(jsonElement.getString("mobile_no"));
                historyElement.setImageUrl(jsonElement.getString("image"));
                historyElement.setHeaderStatus(jsonElement.getString("header_status"));
                historyElement.setStatus(jsonElement.getString("status"));
                historyElement.setUpdatedAt(jsonElement.getString("updated_at"));
                historyElement.setTimeStamp(jsonElement.getString("timestamp"));
                elementList.add(historyElement);
            }


        } catch (JSONException e) {
            requestError(e.getMessage());
            e.printStackTrace();
        }

        return elementList;
    }
}
