package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.method.Methods.loadImageByUri;
import static com.tama.chat.rest.util.APIUtil.getLanguages;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.google.gson.Gson;
import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.rest.HttpRequestManager;
import com.tama.chat.rest.Logger;
import com.tama.chat.rest.RestHttpClient;
import com.tama.chat.rest.entity.HttpConnection;
import com.tama.chat.rest.util.APIUtil;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryProduct;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryResult;
import com.tama.chat.tamaAccount.entry.historyPojos.historySinglePojos.HistorySingle;
import com.tama.chat.tamaAccount.entry.historyPojos.historySinglePojos.HistorySingleData;
import java.util.List;

public class TamaSingleHistoryActivity extends
    TamaAccountBaseActivity {// implements TamaAccountHelperListener {

  @Bind(R.id.progress_bar)
  public ProgressBar mProgressBar;

  @Bind(R.id.activity_tama_history_id)
  public RelativeLayout contentRl;

//  private HistoryResult element;
  private String  historyId;
  public static final String EXTRA_HISTORY_SINGLE_ELEMENT_ID = "EXTRA_HISTORY_SINGLE_ELEMENT_ID";
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

  @Bind(R.id.order_status)
  TextView orderStatus;

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

  @Bind(R.id.amount_title_view)
  TextView amountTitleTv;


  @Bind(R.id.image_view_promo)
  ImageView imageViewPromo;

//    public static TamaSingleHistoryActivity newInstance(HistoryResult historyResult) {
//        TamaSingleHistoryActivity fragment = new TamaSingleHistoryActivity();
//        Bundle args = new Bundle();
//        args.putParcelable(ARG_PARAM_1, historyResult);
//        fragment.setArguments(args);
//        return fragment;
//    }

  @Override
  protected int getContentResId() {
    return R.layout.activity_tama_single_history;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//        new TamaAccountHelper().getSingleHistory(this,String.valueOf(history_id));
    setTamaToolbar(R.string.history, R.string.history);

    contentRl.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.VISIBLE);

    if (getIntent() != null) {
      historyId = getIntent().getStringExtra(EXTRA_HISTORY_SINGLE_ELEMENT_ID);
    }
    if (historyId != null) {
      new TamaHistorySingleAsyncTask().execute(historyId);
    } else {
      errorMessageText.setVisibility(View.VISIBLE);
      errorMessageText.setText(R.string.error_unauthorized);
//      contentRl.setVisibility(View.GONE);
      mProgressBar.setVisibility(View.GONE);
    }
  }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_tama_single_history, container, false);
//        ButterKnife.bind(this, view);
//        if (getArguments() != null) {
//            element = getArguments().getParcelable(ARG_PARAM_1);
//        }
//      if (element != null) {
//        initFields(element);
//      }else {
//        errorMessageText.setVisibility(View.VISIBLE);
//        errorMessageText.setText(R.string.error_unauthorized);
//      }
//      return view;
//    }
public String removeFirstChar(String s){
  return s.substring(1);
}

  private void initFields(HistoryResult historyResult) {
    singleHistoryId.setText(String.valueOf(historyResult.getHistoryId()));
    singleHistoryTimestamp.setText(historyResult.getTimestamp());
    String amount= historyResult.getAmount();
    if (getDouble(removeFirstChar(amount))!=0) {
      amountTextView.setText(amount);
      amountTitleTv.setVisibility(View.VISIBLE);
      amountTextView.setVisibility(View.VISIBLE);
      imageViewPromo.setVisibility(View.VISIBLE);
    }else {
      amountTextView.setText("");
      amountTitleTv.setVisibility(View.GONE);
      amountTextView.setVisibility(View.GONE);
      imageViewPromo.setVisibility(View.GONE);
    }

    statusText.setText(historyResult.getStatus());
    historyName.setText(historyResult.getHistoryName());
    if (historyResult.getMobileNo() != null) {
      mobileNo.setText(historyResult.getMobileNo().toString());
    }

    orderStatus.setText(historyResult.getOrderStatus());

//    if (element.getOrderStatus().contains("Accepted") || element.getOrderStatus().contains("successful")) {
//      statusBtn.setText(getString(R.string.succes));
//      statusBtn.setBackgroundResource(R.drawable.background_green_oval);
//    } else if (element.getOrderStatus().contains("In Progress")) {
//      statusBtn.setText(getString(R.string.progress));
//      statusBtn.setBackgroundResource(R.drawable.background_orange_oval);
//    } else {
//      statusBtn.setText(getString(R.string.denied));
//      statusBtn.setBackgroundResource(R.drawable.background_red_oval);
//    }


    if (historyResult.getImage() != null) {
      loadImageByUri(historyResult.getImage(), imageView);
    }
    if (historyResult.getPromoUsed().equals("yes")) {
      if (historyResult.getPromoImage() != null) {
        loadImageByUri(historyResult.getPromoImage(), imageViewPromo);
      }
    }
    if (historyResult.getSenderName() != null && !historyResult.getSenderName().equals("")) {
      senderLayout.setVisibility(View.VISIBLE);
      senderNameText.setText(historyResult.getSenderName());
      senderNumberText.setText(historyResult.getSenderMobile());
      receiverNameText.setText(historyResult.getReceiverName());
      receiverNumberText.setText(historyResult.getReceiverMobile());
    }
    if (historyResult.getProducts() != null && historyResult.getProducts().size() > 0) {
      productsListLayout.setVisibility(View.VISIBLE);
      line_3.setVisibility(View.VISIBLE);
      productsList.setAdapter(new ProductsListAdapter(historyResult.getProducts()));
    }
  }

  @Override
  public Context getAppContext() {
    return this;
  }

//    private SingleHistoryElement parseJSon(String data) {
//        if (data == null)
//            return null;
//        SingleHistoryElement historyElement = new SingleHistoryElement();
//        try {
//            JSONObject jsonData = new JSONObject(data);
//            JSONObject jsonElement = jsonData.getJSONObject("data");
//            historyElement.user_id = jsonElement.getString("user_id");
//            historyElement.history_id = jsonElement.getString("history_id");
//            historyElement.history_name = jsonElement.getString("history_name");
//            historyElement.amount = jsonElement.getString("amount");
//            historyElement.mobile_no = jsonElement.getString("mobile_no");
//            historyElement.image = jsonElement.getString("image");
//            historyElement.header_status = jsonElement.getString("header_status");
//            historyElement.status = jsonElement.getString("status");
//            historyElement.sender_name = jsonElement.getString("sender_name");
//            historyElement.sender_mobile = jsonElement.getString("sender_mobile");
//            historyElement.receiver_name = jsonElement.getString("receiver_name");
//            historyElement.receiver_mobile = jsonElement.getString("receiver_mobile");
//            historyElement.updated_at = jsonElement.getString("updated_at");
//            historyElement.timestamp = jsonElement.getString("timestamp");
//            JSONArray jsonProducts = jsonElement.optJSONArray("products");
//            if(jsonProducts!=null && jsonProducts.length()>0) {
//                historyElement.productsList = new ArrayList<>();
//                for (int i = 0; i < jsonProducts.length(); i++) {
//                    JSONObject jsonProdEl = (JSONObject)jsonProducts.get(i);
//                    HistoryElementProduct elementProd = new HistoryElementProduct();
//                    elementProd.product_name = jsonProdEl.getString("product_name");
//                    elementProd.price = jsonProdEl.getString("price");
//                    elementProd.quantity = jsonProdEl.getString("quantity");
//                    historyElement.productsList.add(elementProd);
//                }
//            }
//        } catch (JSONException e) {
////            requestError(e.getMessage());
//            e.printStackTrace();
//        }
//
//        return historyElement;
//    }

//    public class SingleHistoryElement{
//        String user_id;
//        String history_id;
//        String history_name;
//        String amount;
//        String mobile_no;
//        String image;
//        String header_status;
//        String status;
//        String sender_name;
//        String sender_mobile;
//        String receiver_name;
//        String receiver_mobile;
//        String updated_at;
//        String timestamp;
//        List<HistoryElementProduct> productsList;
//
//    }

//    public class HistoryElementProduct{
//        String product_name;
//        String quantity;
//        String price;
//    }

  public class ProductsListAdapter extends BaseAdapter {

    List<HistoryProduct> data;

    private LayoutInflater inflater = null;

    public ProductsListAdapter(List<HistoryProduct> data) {
      this.data = data;
      inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
      if (vi == null) {
        vi = inflater.inflate(R.layout.item_new_tama_historey_products_list, null);
      }
      final HistoryProduct element = data.get(position);

      ((TextView) vi.findViewById(R.id.product_name)).setText(element.getProductName());
      ((TextView) vi.findViewById(R.id.product_quantity)).setText(String.valueOf(element.getQty()));
      ((TextView) vi.findViewById(R.id.product_price)).setText(element.getProductCost());

      return vi;
    }
  }

//    @Override
//    public void requestError(String data) {
//        errorMessageText.setVisibility(View.VISIBLE);
//        errorMessageText.setText(data);
//    }
//
//    @Override
//    public void alertDialogCancelListener() {
//
//    }
//
//    @Override
//    public Context getAppContext() {
//        return getContext();
//    }
//
//    @Override
//    public void requestSuccess(String data) {
//        SingleHistoryElement element = parseJSon(data);
//        initFields(element);
//    }

  private class TamaHistorySingleAsyncTask extends AsyncTask<String, Void, HistoryResult> {

    @Override
    protected  HistoryResult doInBackground(String... params) {
      String jsonResponse = "";

      int id  = Integer.valueOf(params[0]);


      HttpConnection httpConnection = HttpRequestManager
          .executeRequest(getAppContext(),
              RestHttpClient.RequestMethod.GET,
              APIUtil.getURL(HttpRequestManager.RequestType.HISTORY_SINGLE,id,getLanguages(getAppContext())),
              App.getInstance().getAppSharedHelper().getTamaAccountAccessToken(),
              null);


      if (httpConnection.isHttpConnectionSucceeded()) {
        StringBuilder jsonResponseStringBuilder =httpConnection.getHttpResponseBody();
        jsonResponse=jsonResponseStringBuilder.toString();

      } else {
        Logger.e(TAG, httpConnection.getHttpConnectionMessage());
        HttpRequestManager.handleFailedRequest( httpConnection);
      }



      /////////////////////////////////////////////////////////////////////////////////////////////
//      // Create URL object
//      URL url = createUrl("http://tamaexpress.com:585/api/history?lang=en");
//
//
//      // Perform HTTP request to the URL and receive a JSON response back
//      String jsonResponse = "";
//
//      try {
//        DefaultHttpClient httpclient = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet("http://tamaexpress.com:585/api/history?lang=en");
//        httpGet.setHeader("Accept", "application/json");
//        httpGet.setHeader("Content-type", "application/json");
//        httpGet.setHeader("Authorization", "Bearer " + App.getInstance().getAppSharedHelper().getTamaAccountAccessToken());
//        ResponseHandler responseHandler = new BasicResponseHandler();
//        jsonResponse= (String) httpclient.execute(httpGet, responseHandler);
//
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//      TamaHistoryElement historyElement = extractFeatureFromJson(jsonResponse);
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////




//
//      try {
//        jsonResponse = makeHttpRequest(url);
//      } catch (IOException e) {
//        // TODO Handle the IOException
//      }

      // Extract relevant fields from the JSON response and create an {@link Event} object

      // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}

      HistorySingle historySingle = new Gson().fromJson(jsonResponse, HistorySingle.class);
//          extractFeatureFromJson(jsonResponse.toString());

      HistorySingleData historyData = historySingle.getData();
      HistoryResult historyResult = historyData.getResult();
      return historyResult;
    }


    @Override
    protected void onPostExecute(HistoryResult historyResult) {
      if (historyResult != null) {
        initFields(historyResult);
        contentRl.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
      } else {
        errorMessageText.setVisibility(View.VISIBLE);
        errorMessageText.setText(R.string.error_unauthorized);
//        contentRl.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
      }
//      setCurrentFragment(TamaSingleHistoryActivity.newInstance(historyResult));
    }

  }

}
