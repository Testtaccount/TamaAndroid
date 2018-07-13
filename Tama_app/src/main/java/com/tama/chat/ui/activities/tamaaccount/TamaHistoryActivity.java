package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.rest.util.APIUtil.getLanguages;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
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
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryData;
import com.tama.chat.tamaAccount.entry.historyPojos.HistoryResult;
import com.tama.chat.tamaAccount.entry.historyPojos.TamaHistoryElement;
import com.tama.chat.ui.fragments.tamaaccount.TamaHistoryFragment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class TamaHistoryActivity extends TamaAccountBaseActivity implements  ViewPager.OnPageChangeListener {

  private String HISTORY_ALL = "all";

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
    TamaHistoryElementAsyncTask asyncTask = new TamaHistoryElementAsyncTask();
    asyncTask.execute();
  }

    private void initFields() {
//        user_id = new SharedHelper(this).getTamaAccountId();
        new TamaAccountHelper().getHistory(this,HISTORY_ALL);
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
    setCurrentFragment(TamaHistoryFragment.newInstance(data));
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    Log.d(TAG, "onPageScrolled - " + position);

  }

  @Override
  public void onPageSelected(int position) {
    Log.d(TAG, "onPageSelected - " + position);
//    setActionBarTitle(String.valueOf(mFragmentAdapter.getPageTitle(position)));
//    loadProduct(mProductArrayList.get(position).getId());
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    Log.d(TAG, "onPageScrollStateChanged - " + state);

  }

  private class TamaHistoryElementAsyncTask extends AsyncTask<URL, Void, List<HistoryResult>> {

    @Override
    protected  List<HistoryResult> doInBackground(URL... urls) {
      String jsonResponse = "";

      HttpConnection httpConnection =HttpRequestManager
          .executeRequest(getAppContext(),
              RestHttpClient.RequestMethod.GET,
              APIUtil.getURL(HttpRequestManager.RequestType.HISTORIES,getLanguages(getAppContext())),
              App.getInstance().getAppSharedHelper().getTamaAccountAccessToken(),
              null);


      if (httpConnection.isHttpConnectionSucceeded()) {
        String token = httpConnection.getHttpResponseHeader().getToken();
        if (token != null) {
          Logger.i(TAG, token);

          // Save necessary historyData after success login
        } else {
//          BusProvider.getInstance().post(new ApiEvent(Event.EventType.Api.Error.UNKNOWN,
//              subscriber));
        }

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

      TamaHistoryElement historyElement = new Gson().fromJson(jsonResponse, TamaHistoryElement.class);
//          extractFeatureFromJson(jsonResponse.toString());

      HistoryData historyData = historyElement.getData();
      List<HistoryResult> historyHistoryResults = historyData.getResult();
      return historyHistoryResults;
    }


    @Override
    protected void onPostExecute( List<HistoryResult> historyHistoryResultList) {
      if (historyHistoryResultList == null) {
        return;
      }

        updateUi(historyHistoryResultList);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
      URL url = null;
      try {
        url = new URL(stringUrl);
      } catch (MalformedURLException exception) {
//        Log.e(TAG, "Error with creating URL", exception);
        return null;
      }
      return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
      String jsonResponse = "";
      HttpURLConnection urlConnection = null;
      InputStream inputStream = null;
      try {
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.connect();
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      } catch (IOException e) {
        // TODO: Handle the exception
      } finally {
        if (urlConnection != null) {
          urlConnection.disconnect();
        }
        if (inputStream != null) {
          // function must handle java.io.IOException here
          inputStream.close();
        }
      }
      return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
      StringBuilder output = new StringBuilder();
      if (inputStream != null) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
            Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while (line != null) {
          output.append(line);
          line = reader.readLine();
        }
      }
      return output.toString();
    }


    private TamaHistoryElement extractFeatureFromJson(String dataJSON) {

      TamaHistoryElement data1 = new Gson().fromJson(dataJSON, TamaHistoryElement.class);

      return data1;

    }
  }

  private void setupTabs(List<HistoryResult> historyHistoryResultList) {
    if (mViewPager != null && getTabLayout() != null) {
      mFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());

//      for (HistoryResult product : historyHistoryResultList) {
        mFragmentAdapter.addFragment(TamaHistoryFragment.newInstance(historyHistoryResultList), "ALL");
//      }

      mViewPager.setAdapter(mFragmentAdapter);
//      getTabLayout().post(new Runnable() {
//        @Override
//        public void run() {
//          getTabLayout().setupWithViewPager(mViewPager);
//        }
//      });
//      getTabLayout().setupWithViewPager(mViewPager);

//      getTabLayout().getTabAt(0).setIcon(R.drawable.icon1);
//      getTabLayout().getTabAt(0).setText("ALL");
    }
  }

  private void updateUi(List<HistoryResult> historyHistoryResults) {
    setupTabs(historyHistoryResults);
    if (getTabLayout() != null) {
      getTabLayout().setupWithViewPager(mViewPager);
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
