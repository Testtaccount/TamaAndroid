package com.tama.chat.ui.activities.authorization;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.quickblox.users.model.QBUser;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.utils.StringObfuscator;
import com.tama.chat.utils.ToastUtils;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.LoginType;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

public class LandingActivity extends BaseAuthActivity implements TamaAccountHelperListener {

  private static final int PERMISSION_RECEIVE_SMS = 97;
  private static final int REQUEST_READ_PHONE_STATE = 7788;
  @Bind(R.id.app_version_textview)
  TextView appVersionTextView;

  @Bind(R.id.phone_number_connect_button)
  Button phoneNumberConnectButton;

  public static void start(Context context) {
    Intent intent = new Intent(context, LandingActivity.class);
    context.startActivity(intent);
  }

  public static void start(Context context, Intent intent) {
    context.startActivity(intent);
  }

  @Override
  protected int getContentResId() {
    return R.layout.activity_landing;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initVersionName();
  }

//    @OnClick(R.id.login_button)
//    void login(View view) {
//        LoginActivity.start(LandingActivity.this);
//        finish();
//    }

  @OnClick(R.id.phone_number_connect_button)
  void phoneNumberConnect(View view) {
    if (checkNetworkAvailableWithError()) {

      if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
          != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSION_RECEIVE_SMS);
      } else {
        loginType = LoginType.FIREBASE_PHONE;
        startSocialLogin();
      }
    }
  }

//  public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
////    showProgress();
//    // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
//    if (requestCode == RC_SIGN_IN) {
//      IdpResponse response = IdpResponse.fromResultIntent(data);
//      // Successfully signed in
//      if (resultCode == ResultCodes.OK) {
////    by meeeeee    AppSession.startSession(LoginType.TWITTER_DIGITS, qbUser, qbToken);
//        final String projectId = "am.gsoft.authjnjelu";
//
//        QBSocialLoginCommand.start(this,QBProvider.TWITTER_DIGITS,currentUser.getUid(),projectId);
//
////        startActivity(new Intent(LandingActivity.this, MainActivity.class));
////        finish();
//        return;
//      } else {
//        // Sign in failed
//        if (response == null) {
//          // User pressed back button
//          Log.e("Login", "Login canceled by User");
////          hideProgress();
//          return;
//        }
//        if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
//          Log.e("Login", "No Internet Connection");
////          hideProgress();
//          return;
//        }
//        if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
//          Log.e("Login", "Unknown Error");
////          hideProgress();
//          return;
//        }
//      }
//      Log.e("Login", "Unknown sign in response");
//    }
//  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {

    if (requestCode == PERMISSION_RECEIVE_SMS) {

      if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        loginType = LoginType.FIREBASE_PHONE;
        startSocialLogin();
      }
    } else if (requestCode == REQUEST_READ_PHONE_STATE) {
      if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
        //TODO
      }
    }
  }

//    public  void start(){
//
//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(
//                    Arrays.asList(
//                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
//                    ))
//                .build(),
//            RC_SIGN_IN);
//    }


  @Override
  public void checkShowingConnectionError() {
    // nothing. Toolbar is missing.
  }

  private void startSignUpActivity() {
    SignUpActivity.start(LandingActivity.this);
    finish();
  }

  private void initVersionName() {
    appVersionTextView.setText(StringObfuscator.getAppVersionName());
  }


  @Override //nael!!!
  protected void startMainActivity(QBUser user) {
    AppSession.getSession().updateUser(user);
//    try {
////      new TamaAccountHelper().register(this, this, user.getPhone());
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
//        super.startMainActivity(user);//stega galis
  }

  private static String[] parseJSon(String data) throws JSONException {
    if (data == null) {
      return null;
    }
    JSONObject jsonObject = new JSONObject(data);
    JSONObject jsonData = jsonObject.getJSONObject("data");
    String user_id = jsonData.getString("user_id");
    String is_africa = jsonData.getString("is_africa");
    String is_europe = jsonData.getString("is_europe");

    return new String[]{user_id, is_europe};
  }

  @Override
  public void requestSuccess(String data) {
    String[] response = new String[]{};
    try {
      response = parseJSon(data);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    AppSession.getSession().setTamaAccountId(response[0]);
    appSharedHelper.saveTamaAccountId(response[0]);
    appSharedHelper.saveTamaIsEurope(response[1].equals("1"));
    startMainActivity();

  }

  @Override
  public void requestError(String data) {
    ToastUtils.longToast(data);
  }

  @Override
  public void alertDialogCancelListener() {

  }

  @Override
  public Context getAppContext() {
    return this;
  }
}