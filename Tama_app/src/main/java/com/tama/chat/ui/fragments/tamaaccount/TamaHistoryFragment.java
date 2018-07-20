package com.tama.chat.ui.fragments.tamaaccount;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.HistoryAdapter;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryData;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryResult;
import com.tama.chat.tamaAccount.entry.historyPojos.TamaHistoryElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TamaHistoryFragment extends Fragment implements HistoryAdapter.OnHistoryItemClickListener {

//    private ArrayList<TamaHistoryElement> mHistoryResultList = new ArrayList<>();

    private static final String ARG_PARAM = "param";
//    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<HistoryResult> mHistoryResultList;
    private OnHistoryFragmentInteractionListener mListener;

//    @Bind(R.id.list_view)
//    ListView mRecyclerView;


    @Bind(R.id.history_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.error_message_text)
    TextView errorMessageText;

//    @Bind(R.id.history_all)
//    TextView historyAll;
//
//    @Bind(R.id.history_my_tama)
//    TextView historyMyTama;
//
//    @Bind(R.id.history_tama_express)
//    TextView historyTamaExpress;
//
//    @Bind(R.id.history_tama_topup)
//    TextView historyTamaTopup;

//    @Bind(R.id.history_balance_transfer)
//    TextView historyBalanceTransfer;

//    @OnClick(R.id.history_all)
//    public void onClickHistoryAll(){
//        onClickHistoryType(historyAll,HISTORY_ALL);
//    }
//
//    @OnClick(R.id.history_my_tama)
//    public void onClickHistoryMyTama(){
//        onClickHistoryType(historyMyTama,HISTORY_MYTAMA);
//    }
//
//    @OnClick(R.id.history_tama_express)
//    public void onClickHistoryTamaExpress(){
//        onClickHistoryType(historyTamaExpress,HISTORY_TAMAEXPRESS);
//    }
//
//    @OnClick(R.id.history_tama_topup)
//    public void onClickHistoryTamaTopup(){
//        onClickHistoryType(historyTamaTopup,HISTORY_TAMATOPUP);
//    }

//    @OnClick(R.id.history_balance_transfer)
//    public void onClickHistoryBalanceTransfer(){
//        onClickHistoryType(historyBalanceTransfer,HISTORY_TRANSFER);
//    }

    public static TamaHistoryFragment newInstance() {
        return new TamaHistoryFragment();
    }

    public static Fragment newInstance(List<HistoryResult> historyResultList) {
        TamaHistoryFragment fragment = new TamaHistoryFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, (ArrayList) historyResultList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tama_history, container, false);
        ButterKnife.bind(this, view);
        mHistoryResultList =new ArrayList<>();
        getData();
        initRecyclerView();
        setListeners();

//        mRecyclerView.setOnItemClickListener(getItemClickListener());
//        mRecyclerView.setAdapter(new TamaHistoryAdapter(mHistoryResultList));
        return view;
    }

    private void initRecyclerView() {
        // Set up the recycler view
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HistoryAdapter(mHistoryResultList, this);
//        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
//        if(mHistoryResultList.isEmpty()){
//            errorMessageText.setVisibility(View.VISIBLE);
//            errorMessageText.setText(getString(R.string.you_have_no_history));
//        }else {
//            errorMessageText.setVisibility(View.GONE);
//            errorMessageText.setText(getString(R.string.you_have_no_history));
//        }
    }


    public void getData() {
        if (getArguments() != null) {
            mHistoryResultList = getArguments().getParcelableArrayList(ARG_PARAM);
//            mHistoryResultList = getHistoryElementListFromJson(mParam);
        }
    }

    private void setListeners() {

    }

    public void setHistoryResultList(List<HistoryResult> historyResultList) {
//        Collections.sort(historyResultList, new Comparator<HistoryResult>(){
//            public int compare(HistoryResult o1, HistoryResult o2){
//                if(o1.getHistoryId() == o2.getHistoryId())
//                    return 0;
//                return o1.getHistoryId() < o2.getHistoryId() ? 1 : -1;
//            }
//        });
        Collections.reverse(historyResultList);
        mAdapter.set(historyResultList);
        if(mHistoryResultList.isEmpty()){
            errorMessageText.setVisibility(View.VISIBLE);
            errorMessageText.setText(getString(R.string.you_have_no_history));
        }else {
            errorMessageText.setVisibility(View.GONE);
            errorMessageText.setText(getString(R.string.you_have_no_history));
        }
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof TamaHistoryActivity) {
//            activity = (TamaHistoryActivity) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    private void onClickHistoryType(TextView textView, String history_type) {
        setTextColorAndBackGround(textView);
        mRecyclerView.setAdapter(null);
        errorMessageText.setVisibility(View.GONE);
//        new TamaAccountHelper().getHistory(this,history_type);
    }


//    @Override
//    public void alertDialogCancelListener() {
//
//    }


    private AdapterView.OnItemClickListener getItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryResult element = mHistoryResultList.get(position);
//                activity.setCurrentFragment(TamaSingleHistoryActivity.newInstance(user_id,element.getHistoryId()));
            }
        };
    }

    private void setTextColorAndBackGround(TextView currentTextView) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            historyAll.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
//            historyMyTama.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
////            historyBalanceTransfer.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
//            historyTamaExpress.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
//            historyTamaTopup.setTextColor(ContextCompat.getColor(getContext(),R.color.button_red_normal));
//            currentTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.text_dark));
//        }else{
//            historyAll.setTextColor(getResources().getColor(R.color.button_red_normal));
//            historyMyTama.setTextColor(getResources().getColor(R.color.button_red_normal));
////            historyBalanceTransfer.setTextColor(getResources().getColor(R.color.button_red_normal));
//            historyTamaExpress.setTextColor(getResources().getColor(R.color.button_red_normal));
//            historyTamaTopup.setTextColor(getResources().getColor(R.color.button_red_normal));
//            currentTextView.setTextColor(getResources().getColor(R.color.text_dark));
//        }
//        historyAll.setBackgroundResource(0);
//        historyMyTama.setBackgroundResource(0);
////        historyBalanceTransfer.setBackgroundResource(0);
//        historyTamaExpress.setBackgroundResource(0);
//        historyTamaTopup.setBackgroundResource(0);
        currentTextView.setBackgroundResource(R.drawable.selector_button_red_under_line);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryFragmentInteractionListener) {
            mListener = (OnHistoryFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnHistoryFragmentInteractionListener");
        }
    }


    private ArrayList<TamaHistoryElement> getHistoryElementListFromJson(String data) {
        if (data == null)
            return null;

        TamaHistoryElement data1 = new Gson().fromJson(data, TamaHistoryElement.class);

        if (data1 != null) {

           HistoryData historyData2 =data1.getData();
           List<HistoryResult> historyHistoryResults = historyData2.getResult();

        }

        ArrayList<TamaHistoryElement> elementList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(data);
            JSONObject jsonData = root.getJSONObject("data");

//    String code = jsonData.getString("code");
//    String http_code = jsonData.getString("http_code");
//    String message = jsonData.getString("message");
            JSONArray resultArray = jsonData.getJSONArray("result");


//            for (int i = 0; i < resultArray.length(); i++) {
//                JSONObject jsonElement = resultArray.getJSONObject(i);
//                TamaHistoryElement historyElement = new TamaHistoryElement();
//                historyElement.setUser_id(jsonElement.getString("user_id"));
//                historyElement.setHistory_id(jsonElement.getString("history_id"));
//                historyElement.setHistory_name(jsonElement.getString("history_name"));
//                historyElement.setAmount(jsonElement.getString("amount"));
//                historyElement.setMobile_no(jsonElement.getString("mobile_no"));
//                historyElement.setImage(jsonElement.getString("image"));
//                historyElement.setHeader_status(jsonElement.getString("header_status"));
//                historyElement.setStatus(jsonElement.getString("status"));
//                historyElement.setOrder_status(jsonElement.getString("order_status"));
//                JSONArray productArray = jsonElement.getJSONArray("products");
//                for (int j = 0; j < productArray.length(); j++) {
//                    JSONObject productElement = productArray.getJSONObject(j);
//                    ProductHistoryElement productHistoryElement=new ProductHistoryElement();
//                    productHistoryElement.setProduct_id(productElement.getString("product_id"));
//                    productHistoryElement.setProduct_name(productElement.getString("product_name"));
//                    productHistoryElement.setProduct_desc(productElement.getString("product_desc"));
//                    productHistoryElement.setQty(productElement.getString("qty"));
//                    productHistoryElement.setTotal(productElement.getString("total"));
//                    productHistoryElement.setProduct_tags(productElement.getString("product_tags"));
//                    productHistoryElement.setCategory_name(productElement.getString("category_name"));
//                    productHistoryElement.setProduct_image(productElement.getString("product_image"));
//                    productHistoryElement.setProduct_country(productElement.getString("product_country"));
//                    productHistoryElement.setCurrency(productElement.getString("currency"));
//                    productHistoryElement.setProduct_cost(productElement.getString("product_cost"));
//                    productHistoryElement.setProduct_cost_ws(productElement.getString("product_cost_ws"));
//                    productHistoryElement.setShipping_available(productElement.getString("shipping_available"));
//                    productHistoryElement.setFree_shipping(productElement.getString("free_shipping"));
//                    productHistoryElement.setMin_to_order(productElement.getString("min_to_order"));
//                    productHistoryElement.setMax_to_order(productElement.getString("max_to_order"));
//                    productHistoryElement.setShipping_cost(productElement.getString("shipping_cost"));
//                    productHistoryElement.setShipping_cost_ws(productElement.getString("shipping_cost_ws"));
//                    productHistoryElement.setLang(productElement.getString("lang"));
//                }
//
//                historyElement.setUpdated_at(jsonElement.getString("updated_at"));
//                historyElement.setTimestamp(jsonElement.getString("timestamp"));
//                elementList.add(historyElement);
//            }


        } catch (JSONException e) {
//            requestError(e.getMessage());
            e.printStackTrace();
        }

        return elementList;
    }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (!isVisibleToUser) {
        if(mAdapter!=null) {
            mAdapter.clear();
        }
    }
  }

  @Override
    public void onOilHistoryItemClick(HistoryResult result) {
        if(mListener!=null){
            mListener.onHistoryItemViewClickListener(result);
        }
    }


  public interface OnHistoryFragmentInteractionListener {
        void onHistoryItemViewClickListener(HistoryResult result);

    }
}
