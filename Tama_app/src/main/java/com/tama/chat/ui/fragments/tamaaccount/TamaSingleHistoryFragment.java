package com.tama.chat.ui.fragments.tamaaccount;

import static com.tama.chat.method.Methods.loadImageByUri;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.tamaaccount.TamaHistoryActivity;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TamaSingleHistoryFragment extends Fragment implements TamaAccountHelperListener {

//    private SingleHistoryElement element;

    private String user_id;
    private String history_id;
    private static final String ARG_PARAM_1 = "param_1";
    private static final String ARG_PARAM_2 = "param_2";
//    private TamaHistoryActivity activity;


    @Bind(R.id.error_message_text)
    TextView errorMessageText;

    @Bind(R.id.single_history_id)
    TextView singleHistoryId;

    @Bind(R.id.single_history_timestamp)
    TextView singleHistoryTimestamp;

    @Bind(R.id.amount_text_view)
    TextView amountTextView;

    @Bind(R.id.status_text)
    TextView statusText;

    @Bind(R.id.history_name)
    TextView historyName;

    @Bind(R.id.mobile_no)
    TextView mobileNo;

    @Bind(R.id.status_btn)
    TextView statusBtn;

    @Bind(R.id.sender_name_text)
    TextView senderNameText;

    @Bind(R.id.sender_number_text)
    TextView senderNumberText;

    @Bind(R.id.receiver_name_text)
    TextView receiverNameText;

    @Bind(R.id.receiver_number_text)
    TextView receiverNumberText;

    @Bind(R.id.products_list)
    ListView productsList;

    @Bind(R.id.products_list_layout)
    RelativeLayout productsListLayout;

    @Bind(R.id.sender_layout)
    RelativeLayout senderLayout;

    @Bind(R.id.line_3)
    LinearLayout line_3;

    @Bind(R.id.image_view)
    ImageView imageView;



    public static TamaSingleHistoryFragment newInstance(String user_id, String history_id) {
        TamaSingleHistoryFragment fragment = new TamaSingleHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_1, user_id);
        args.putString(ARG_PARAM_2, history_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getString(ARG_PARAM_1);
            history_id = getArguments().getString(ARG_PARAM_2);
        }
        new TamaAccountHelper().getSingleHistory(this,user_id,history_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tama_single_history, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void requestError(String data) {
        errorMessageText.setVisibility(View.VISIBLE);
        errorMessageText.setText(data);
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
        SingleHistoryElement element = parseJSon(data);
        initFields(element);
    }

    private void initFields(SingleHistoryElement element) {
        singleHistoryId.setText(element.history_id);
        singleHistoryTimestamp.setText(element.timestamp);
        amountTextView.setText(element.amount);
        statusText.setText(element.status);
        historyName.setText(element.history_name);
        mobileNo.setText(element.mobile_no);
        if(element.status.contains("Accepted")||element.status.contains("successful")){
            statusBtn.setText(getString(R.string.succes));
            statusBtn.setBackgroundResource(R.drawable.background_green_oval);
        }else if(element.status.contains("In Progress")){
            statusBtn.setText(getString(R.string.progress));
            statusBtn.setBackgroundResource(R.drawable.background_orange_oval);
        }else{
            statusBtn.setText(getString(R.string.denied));
            statusBtn.setBackgroundResource(R.drawable.background_red_oval);
        }
        loadImageByUri(element.image,imageView);
        if(!element.sender_name.isEmpty()) {
            senderLayout.setVisibility(View.VISIBLE);
            senderNameText.setText(element.sender_name);
            senderNumberText.setText(element.sender_mobile);
            receiverNameText.setText(element.receiver_name);
            receiverNumberText.setText(element.receiver_mobile);
        }
        if(element.productsList!=null&&element.productsList.size()>0) {
            productsListLayout.setVisibility(View.VISIBLE);
            line_3.setVisibility(View.VISIBLE);
            productsList.setAdapter(new ProductsListAdapter((TamaHistoryActivity) getActivity(), element.productsList));
        }
    }

    private SingleHistoryElement parseJSon(String data) {
        if (data == null)
            return null;
        SingleHistoryElement historyElement = new SingleHistoryElement();
        try {
            JSONObject jsonData = new JSONObject(data);
            JSONObject jsonElement = jsonData.getJSONObject("data");
            historyElement.user_id = jsonElement.getString("user_id");
            historyElement.history_id = jsonElement.getString("history_id");
            historyElement.history_name = jsonElement.getString("history_name");
            historyElement.amount = jsonElement.getString("amount");
            historyElement.mobile_no = jsonElement.getString("mobile_no");
            historyElement.image = jsonElement.getString("image");
            historyElement.header_status = jsonElement.getString("header_status");
            historyElement.status = jsonElement.getString("status");
            historyElement.sender_name = jsonElement.getString("sender_name");
            historyElement.sender_mobile = jsonElement.getString("sender_mobile");
            historyElement.receiver_name = jsonElement.getString("receiver_name");
            historyElement.receiver_mobile = jsonElement.getString("receiver_mobile");
            historyElement.updated_at = jsonElement.getString("updated_at");
            historyElement.timestamp = jsonElement.getString("timestamp");
            JSONArray jsonProducts = jsonElement.optJSONArray("products");
            if(jsonProducts!=null && jsonProducts.length()>0) {
                historyElement.productsList = new ArrayList<>();
                for (int i = 0; i < jsonProducts.length(); i++) {
                    JSONObject jsonProdEl = (JSONObject)jsonProducts.get(i);
                    HistoryElementProduct elementProd = new HistoryElementProduct();
                    elementProd.product_name = jsonProdEl.getString("product_name");
                    elementProd.price = jsonProdEl.getString("price");
                    elementProd.quantity = jsonProdEl.getString("quantity");
                    historyElement.productsList.add(elementProd);
                }
            }
        } catch (JSONException e) {
            requestError(e.getMessage());
            e.printStackTrace();
        }

        return historyElement;
    }

    public class SingleHistoryElement{
        String user_id;
        String history_id;
        String history_name;
        String amount;
        String mobile_no;
        String image;
        String header_status;
        String status;
        String sender_name;
        String sender_mobile;
        String receiver_name;
        String receiver_mobile;
        String updated_at;
        String timestamp;
        List<HistoryElementProduct> productsList;

    }

    public class HistoryElementProduct{
        String product_name;
        String quantity;
        String price;
    }

    public class ProductsListAdapter extends BaseAdapter {

        TamaHistoryActivity activity;
        List<HistoryElementProduct> data;

        private LayoutInflater inflater = null;

        public ProductsListAdapter(TamaHistoryActivity activity, List<HistoryElementProduct> data) {
            this.activity = activity;
            this.data = data;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.item_new_tama_historey_products_list, null);
            final HistoryElementProduct element = data.get(position);

            ((TextView) vi.findViewById(R.id.product_name)).setText(element.product_name);
            ((TextView) vi.findViewById(R.id.product_quantity)).setText(element.quantity);
            ((TextView) vi.findViewById(R.id.product_price)).setText(element.price);

            return vi;
        }
    }
}
