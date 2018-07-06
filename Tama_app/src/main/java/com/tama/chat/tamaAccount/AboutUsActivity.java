package com.tama.chat.tamaAccount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

  private static final String LOG_TAG = AboutUsActivity.class.getSimpleName();
  public static final String URL = "http://tamaexpress.com:585/app-about-us";
//  public static final String URL = "http://tamaexpress.com:585/app-checkout";

  private Bundle mArgumentData;
  private WebView mWv;
  private SwipeRefreshLayout mSwipeRefreshLayout;


  @Override
  protected int getContentResId() {
    return R.layout.activity_about;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    findViews();
    setListeners();
    customizeActionBar();
    setupWebView();
    loadUrl();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    }
  }

  // ===========================================================
  // Other Listeners, methods for/from Interfaces
  // ===========================================================

  @Override
  public void onRefresh() {
    mWv.reload();
  }

  // ===========================================================
  // Methods
  // ===========================================================

  private void setListeners() {
    mSwipeRefreshLayout.setOnRefreshListener(this);
  }

  private void findViews() {
    mWv = (WebView) findViewById(R.id.wv_about);
    mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_about);
  }

  private void setupWebView() {
    mWv.setWebChromeClient(new CustomWebChromeClient());
    mWv.setWebViewClient(new CustomWebViewClient());
    mWv.getSettings().setJavaScriptEnabled(true);
    mWv.getSettings().setBuiltInZoomControls(true);
    mWv.getSettings().setDisplayZoomControls(false);
  }

  private void loadUrl() {
    mSwipeRefreshLayout.setRefreshing(true);
    mWv.loadUrl(URL);
  }


  private void customizeActionBar() {

  }


  private class CustomWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            /*if (!view.getUrl().contains(URL)) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(view.getUrl()));
                startActivity(i);
            }*/
      return true;
    }
  }


  private class CustomWebChromeClient extends WebChromeClient {

    @Override
    public void onReceivedTitle(WebView view, String title) {
      super.onReceivedTitle(view, title);
      Log.d(LOG_TAG, title);
    }

    @Override
    public void onProgressChanged(WebView view, int progress) {
      Log.d(LOG_TAG, String.valueOf(progress));
      if (progress >= 90) {
        mSwipeRefreshLayout.setRefreshing(false);
      }
    }
  }
}