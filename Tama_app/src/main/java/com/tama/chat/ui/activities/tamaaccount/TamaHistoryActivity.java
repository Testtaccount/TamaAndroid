package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.rest.util.APIUtil.getLanguages;
import static com.tama.chat.ui.activities.tamaaccount.TamaSingleHistoryActivity.EXTRA_HISTORY_SINGLE_ELEMENT_ID;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryData;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryResult;
import com.tama.chat.tamaAccount.entry.historyPojos.TamaHistoryElement;
import com.tama.chat.ui.fragments.tamaaccount.TamaHistoryFragment;
import com.tama.chat.utils.ToastUtils;
import java.util.List;

public class TamaHistoryActivity extends TamaAccountBaseActivity implements  ViewPager.OnPageChangeListener ,TamaHistoryFragment.OnHistoryFragmentInteractionListener{

  private final String HISTORY_ALL = "all";
  private final String HISTORY_MYTAMA = "mytama";
  private final String HISTORY_TAMA_TOPUP = "tama-topup";
  private final String HISTORY_TAMAEXPRESS = "tamaexpress";
  private final String HISTORY_TRANSFER = "transfer";

  @Bind(R.id.progress_bar)
  public ProgressBar mProgressBar;

  @Bind(R.id.ctl)
  TabLayout mTabLayout;

  @Bind(R.id.vp_history_list)
  ViewPager mViewPager;


  private TabFragmentAdapter mFragmentAdapter;

  @Bind(R.id.error_message_text)
  TextView errorMessageText;

  @Override
  protected int getContentResId() {
    return R.layout.activity_tama_history;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTamaToolbar(R.string.mytama_history, R.string.history);
//        initFields();
    setupTabs();
    loadHistory(0);
  }

  private void loadHistory(int position) {
    new TamaHistoryElementsAsyncTask().execute(String.valueOf(position));
  }

    private void initFields() {
//        user_id = new SharedHelper(this).getTamaAccountId();
//        new TamaAccountHelper().getHistory(this,HISTORY_ALL);
    }

  @Override
  public void onBackPressed() {
    if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
      finish();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void requestError(String data) {
    super.requestError(data);
    errorMessageText.setVisibility(View.VISIBLE);
    errorMessageText.setText(data);
//        setButtonEnable(true);
  }

  @Override
  public Context getAppContext() {
    return this;
  }

  @Override
  public void requestSuccess(String data) {
//    setCurrentFragment(TamaHistoryFragment.newInstance(data));
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    Log.d(TAG, "onPageScrolled - " + position);

  }

  @Override
  public void onPageSelected(int position) {
    Log.d(TAG, "onPageSelected - " + position);
//    setActionBarTitle(String.valueOf(mFragmentAdapter.getPageTitle(position)));
    loadHistory(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    Log.d(TAG, "onPageScrollStateChanged - " + state);
  }

  @Override
  public void onHistoryItemViewClickListener(HistoryResult result) {
    if (!isNetworkAvailable()) {
      ToastUtils.longToast(R.string.no_internet_conection);
      return;
    }
    if(result!=null)
    openSingleHistoryActivity(result.getHistoryId());
  }


  private class TamaHistoryElementsAsyncTask extends AsyncTask<String, Void, List<HistoryResult>> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      TamaHistoryFragment fragment=((TamaHistoryFragment)mFragmentAdapter.getItem(position));
      if (fragment!=null) {
        mProgressBar.setVisibility(View.VISIBLE);
      }
    }

    int position;
    @Override
    protected  List<HistoryResult> doInBackground(String... params) {
      String jsonResponse = "";
      String url="";

      position = Integer.valueOf(params[0]);
      switch (position){
        case 0:
           url = APIUtil.getURL(HttpRequestManager.RequestType.HISTORIES,getLanguages(getAppContext()));
          break;
        case 1:
           url = APIUtil.getURL(HttpRequestManager.RequestType.HISTORIES_MYTAMA,getLanguages(getAppContext()));
          break;
        case 2:
           url = APIUtil.getURL(HttpRequestManager.RequestType.HISTORIES_TAMAEXPRESS,getLanguages(getAppContext()));
          break;
        case 3:
           url = APIUtil.getURL(HttpRequestManager.RequestType.HISTORIES_TAMA_TOPUP,getLanguages(getAppContext()));
          break;
        case 4:
           url = APIUtil.getURL(HttpRequestManager.RequestType.HISTORIES_TRANSFER,getLanguages(getAppContext()));
          break;
      }

      HttpConnection httpConnection =HttpRequestManager
          .executeRequest(getAppContext(),
              RestHttpClient.RequestMethod.GET,
              url,
              App.getInstance().getAppSharedHelper().getTamaAccountAccessToken(),
              null);


      if (httpConnection.isHttpConnectionSucceeded()) {
        StringBuilder jsonResponseStringBuilder =httpConnection.getHttpResponseBody();
        jsonResponse=jsonResponseStringBuilder.toString();

      } else {
        Logger.e(TAG, httpConnection.getHttpConnectionMessage());
        HttpRequestManager.handleFailedRequest(httpConnection);
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

      TamaHistoryElement historyElement = new Gson().fromJson(jsonResponse, TamaHistoryElement.class);
//          extractFeatureFromJson(jsonResponse.toString());

      HistoryData historyData = historyElement.getData();
      List<HistoryResult> historyHistoryResults = historyData.getResult();
      return historyHistoryResults;
    }

    @Override
    protected void onPostExecute(List<HistoryResult> historyHistoryResultList) {
      if (historyHistoryResultList == null) {
        return;
      }
      updateUi(position,historyHistoryResultList);

    }
  }


  private void openSingleHistoryActivity(long historyId) {
    Intent intent=new Intent(this, TamaSingleHistoryActivity.class);
    intent.putExtra(EXTRA_HISTORY_SINGLE_ELEMENT_ID,String.valueOf(historyId));
    startActivity(intent);
  }


  private void setupTabs() {
    if (mViewPager != null && mTabLayout != null) {
      mFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());

//      for (HistoryResult product : historyHistoryResultList) {
        mFragmentAdapter.addFragment(TamaHistoryFragment.newInstance(), "All");
        mFragmentAdapter.addFragment(TamaHistoryFragment.newInstance(), "Tama Family");
        mFragmentAdapter.addFragment(TamaHistoryFragment.newInstance(), "Tama Express");
        mFragmentAdapter.addFragment(TamaHistoryFragment.newInstance(), "Tama Topup");
        //        mFragmentAdapter.addFragment(TamaHistoryFragment.newInstance(), "Tama Transfer");
//      }

      mViewPager.setAdapter(mFragmentAdapter);

//      mTabLayout.post(new Runnable() {
//        @Override
//        public void run() {
//          mTabLayout.setupWithViewPager(mViewPager);
//        }
//      });
      mTabLayout.setupWithViewPager(mViewPager);
      mViewPager.addOnPageChangeListener(this);

//      getTabLayout().getTabAt(0).setIcon(R.drawable.icon1);
//      getTabLayout().getTabAt(0).setText("ALL");
    }
  }

  private void updateUi(int position,List<HistoryResult> historyHistoryResults) {
    TamaHistoryFragment fragment=((TamaHistoryFragment)mFragmentAdapter.getItem(position));
    if (fragment!=null){
      mProgressBar.setVisibility(View.GONE);
      fragment.setHistoryResultList(historyHistoryResults);
    }


//    customizeActionBar();
//    loadHistoryResult(mFragmentAdapter.getItem(0).getId());
//    setCurrentFragment(TamaHistoryFragment.newInstance(historyHistoryResults));
  }

//  private void loadHistoryResult(long productId) {
//    PLIntentService.start(
//        this,
//        Constant.API.PRODUCT_ITEM + String.valueOf(productId)
//            + Constant.API.PRODUCT_ITEM_POSTFIX,
//        HttpRequestManager.RequestType.PRODUCT_ITEM
//    );
//  }
}
