package com.tama.chat.ui.activities.agreements;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;

import butterknife.Bind;

//public class UserAgreementActivity extends BaseLoggableActivity {//Avetik
public class UserAgreementActivity extends BaseActivity {

    @Bind(R.id.user_agreement_webview)
    WebView userAgreementWebView;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserAgreementActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_user_agreement;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFields();
        setUpActionBarWithUpButton();
        initUserAgreementWebView();
    }

    private void initFields() {
        title = getString(R.string.user_agreement_title);
    }

    private void initUserAgreementWebView() {

        String policyLink = getString(R.string.app_policy_link);
        WebSettings settings = userAgreementWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setBuiltInZoomControls(true);
        userAgreementWebView.setWebChromeClient(new WebChromeClient());
        userAgreementWebView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + policyLink);
    }
}