package com.tama.chat.ui.activities.tamaaccount;

import static com.tama.chat.rest.util.APIUtil.getLanguages;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
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
import com.tama.chat.rest.util.PostEntityUtil;
import com.tama.chat.tamaAccount.entry.findARetailerPojos.RetailerData;
import com.tama.chat.tamaAccount.entry.findARetailerPojos.RetailerJson;
import com.tama.chat.tamaAccount.entry.findARetailerPojos.RetailerResult;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.adapters.search.FindARetailersAdapter;
import com.tama.chat.ui.adapters.search.FindARetailersAdapter.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;

public class FindARetailerActivity extends BaseActivity implements
    OnItemClickListener {

  @Bind(R.id.tama_toolbar_title)
  TextView tamaToolbarTitle;

  @Bind(R.id.retailers_recycler_view)
  RecyclerView mRecyclerView;

  @Bind(R.id.search_retailers)
  SearchView sv;

  @Bind(R.id.progress)
  ProgressBar mProgressBar;

  @Bind(R.id.tv_find_text_abel)
  TextView findTxtLabel;

  EditText searchEditText;
  private LinearLayoutManager layoutManager;
  private FindARetailersAdapter mAdapter;
  private ArrayList<RetailerResult> items;

  @Override
  protected int getContentResId() {
    return R.layout.activity_find_a_retailer;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tamaToolbarTitle.setText(getString(R.string.find_a_retailer).toUpperCase());
    findViews();
    initFields();
    setListeners();
  }

  private void findViews() {
    int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
    TextView textView = (TextView) sv.findViewById(id);
    textView.setTextColor(Color.BLACK);
//    searchEditText.setTextColor(getResources().getColor(R.color.BLACK));
//    searchEditText.setHintTextColor(getResources().getColor(R.color.white));
  }

  private void initFields() {
    items = new ArrayList<>();
    initRecyclerView();
  }

  private void initRecyclerView() {
    // Set up the recycler view
    layoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mRecyclerView.setHasFixedSize(true);
    mAdapter = new FindARetailersAdapter(items, this);
//    mAdapter.setHasStableIds(true);
    mRecyclerView.setAdapter(mAdapter);
  }


  private void setListeners() {
    //SEARCH

    sv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sv.setIconified(false);
        findTxtLabel.setVisibility(View.GONE);
      }
    });
//    sv.setQuery("Search..",true);
    sv.setOnQueryTextListener(new OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(String txt) {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        loadRetailers(txt);
        resetSearchView();
        sv.setIconified(true);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String txt) {
        // TODO Auto-generated method stub

//        mAdapter.filter(txt);
//        mAdapter.getFilter().filter(txt);
        return false;
      }
    });

    sv.setOnCloseListener(new OnCloseListener() {
      @Override
      public boolean onClose() {
        resetSearchView();

        return false;
      }
    });
  }

  private void resetSearchView() {
    // Reset SearchView
    sv.clearFocus();
    sv.setQuery("", false);
    findTxtLabel.setVisibility(View.VISIBLE);
  }


  private void loadRetailers(String txt) {
    new RetailersAsyncTask().execute(txt);
  }

  private void setResultList(List<RetailerResult> resultList){
    mAdapter.setList(resultList);
  }

  private class RetailersAsyncTask extends AsyncTask<String, Void, List<RetailerResult>> {

    @Override
    protected List<RetailerResult> doInBackground(String... params) {
      String jsonResponse = "";
//      String zipCode = "600601";
      String zipCode = params[0];

      String postEntity = PostEntityUtil.composeFindARetailersPostEntity(
          zipCode
      );

      HttpConnection httpConnection = HttpRequestManager.executeRequest(
          FindARetailerActivity.this,
          RestHttpClient.RequestMethod.POST,
          APIUtil.getURL(HttpRequestManager.RequestType.FIND_A_RETAILER, getLanguages(FindARetailerActivity.this)),
          App.getInstance().getAppSharedHelper().getTamaAccountAccessToken(),
          postEntity);

      if (httpConnection.isHttpConnectionSucceeded()) {
        StringBuilder jsonResponseStringBuilder = httpConnection.getHttpResponseBody();
        jsonResponse = jsonResponseStringBuilder.toString();

      } else {
        Logger.e(TAG, httpConnection.getHttpConnectionMessage());
        HttpRequestManager.handleFailedRequest(httpConnection);
      }

      RetailerJson retailerJson = new Gson().fromJson(jsonResponse, RetailerJson.class);
      RetailerData retailerData = retailerJson.getData();
      List<RetailerResult> retailerResults = retailerData.getResult();
      return retailerResults;
    }


    @Override
    protected void onPostExecute(List<RetailerResult> retailerResults) {
      if (retailerResults == null) {
        return;
      }
      items = (ArrayList<RetailerResult>) retailerResults;
      setResultList(items);
      mProgressBar.setVisibility(ProgressBar.GONE);

    }
  }

  @Override
  public void onItemClick(RetailerResult retailerResult) {

  }
}
