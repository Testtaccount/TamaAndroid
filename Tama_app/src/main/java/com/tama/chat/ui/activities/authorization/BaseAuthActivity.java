package com.tama.chat.ui.activities.authorization;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.OnTextChanged;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.users.model.QBUser;
import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.activities.main.MainActivity;
import com.tama.chat.ui.fragments.dialogs.UserAgreementDialogFragment;
import com.tama.chat.utils.AuthUtils;
import com.tama.chat.utils.StringObfuscator;
import com.tama.chat.utils.helpers.FacebookHelper;
import com.tama.chat.utils.helpers.FirebaseAuthHelper;
import com.tama.chat.utils.helpers.FlurryAnalyticsHelper;
import com.tama.chat.utils.helpers.GoogleAnalyticsHelper;
import com.tama.chat.utils.helpers.ServiceManager;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.LoginType;
import rx.Observer;


public abstract class BaseAuthActivity extends BaseActivity {

    private static String TAG = BaseAuthActivity.class.getSimpleName();

    protected static final String STARTED_LOGIN_TYPE = "started_login_type";

    @Nullable
    @Bind(R.id.email_textinputlayout)
    protected TextInputLayout emailTextInputLayout;

    @Nullable
    @Bind(R.id.email_edittext)
    protected EditText emailEditText;

    @Nullable
    @Bind(R.id.password_textinputlayout)
    protected TextInputLayout passwordTextInputLayout;

    @Nullable
    @Bind(R.id.password_edittext)
    protected EditText passwordEditText;

    protected FacebookHelper facebookHelper;
//    protected TwitterDigitsHelper twitterDigitsHelper;
    private FirebaseAuthHelper firebaseAuthHelper;
    private FirebaseAuthCallback firebaseAuthCallback;

    protected LoginType loginType = LoginType.EMAIL;
    protected Resources resources;

    private ServiceManager serviceManager;


    //chka
//    protected LoginSuccessAction loginSuccessAction;
//    //chka
//    protected SocialLoginSuccessAction socialLoginSuccessAction;
//    //chka
//    protected FailAction failAction;
////    private TwitterDigitsAuthCallback twitterDigitsAuthCallback;

    public static void start(Context context) {
        Intent intent = new Intent(context, BaseAuthActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFields(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        facebookHelper.onActivityStart();
    }

    //chka
//    @Override
//    protected void onResume() {
//        super.onResume();
//        addActions();
//    }
//
//    //chka
//    @Override
//    protected void onPause() {
//        super.onPause();
//        removeActions();
//    }

//    public void tellSecondObjectExecuteLater(){
//        authenticationActivity.closeActivity( new AutheenticationSuccess() {
//            public void registerSuccess(){
//                startMainActivity();
//            } });
//    }


    @Override
    public void onStop() {
        super.onStop();
        facebookHelper.onActivityStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STARTED_LOGIN_TYPE, loginType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult, result code = " + requestCode);
        if (requestCode == FirebaseAuthHelper.RC_SIGN_IN){
            onReceiveFirebaseAuthResult(resultCode, data);
        }
        facebookHelper.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AuthenticationActivity.REQUEST_CODE_USER_DATA) {
            startMainActivity();
        }
    }

    private void onReceiveFirebaseAuthResult(int resultCode, Intent data) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                FirebaseAuthHelper.getIdTokenForCurrentUser(firebaseAuthCallback);
                return;
            } else {
                 //Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.i(TAG, "BACK button pressed");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK || response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.dlg_internet_connection_error, Snackbar.LENGTH_INDEFINITE);
                    return;
                }
            }
    }

    @Nullable
    @OnTextChanged(R.id.email_edittext)
    void onTextChangedEmail(CharSequence text) {
        emailTextInputLayout.setError(null);
    }

    @Nullable
    @OnTextChanged(R.id.password_edittext)
    void onTextChangedPassword(CharSequence text) {
        passwordTextInputLayout.setError(null);
    }

    private void initFields(Bundle savedInstanceState) {
        resources = getResources();
        if (savedInstanceState != null && savedInstanceState.containsKey(STARTED_LOGIN_TYPE)) {
            loginType = (LoginType) savedInstanceState.getSerializable(STARTED_LOGIN_TYPE);
        }
        facebookHelper = new FacebookHelper(this);
//        twitterDigitsHelper = new TwitterDigitsHelper();
//        twitterDigitsAuthCallback = new TwitterDigitsAuthCallback();
//        loginSuccessAction = new LoginSuccessAction();
//        socialLoginSuccessAction = new SocialLoginSuccessAction();

        firebaseAuthHelper = new FirebaseAuthHelper();
        firebaseAuthCallback = new FirebaseAuthCallback();
        failAction = new FailAction();
        serviceManager = ServiceManager.getInstance();
    }

    //chka
//    protected void startSocialLogin(final Activity activity) {
//        if (!appSharedHelper.isShownUserAgreement()) {
//            UserAgreementDialogFragment
//                    .show(getSupportFragmentManager(), new MaterialDialog.ButtonCallback() {
//                                @Override
//                                public void onPositive(MaterialDialog dialog) {
//                                    super.onPositive(dialog);
//                                    appSharedHelper.saveShownUserAgreement(true);
//                                    loginWithSocial(activity);
//
//                                }
//                            });
//        } else {
//            loginWithSocial(activity);
//        }
//    }

    protected void startSocialLogin() {
        if (!appSharedHelper.isShownUserAgreement()) {
            UserAgreementDialogFragment
                    .show(getSupportFragmentManager(), new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            appSharedHelper.saveShownUserAgreement(true);
                            loginWithSocial();
                        }
                    });
        } else {
            loginWithSocial();
        }
    }

    private void loginWithSocial() {
        appSharedHelper.saveFirstAuth(true);
        appSharedHelper.saveSavedRememberMe(true);
        if (loginType.equals(LoginType.FACEBOOK)) {
            facebookHelper.login(new FacebookLoginCallback());
        } else if (loginType.equals(LoginType.FIREBASE_PHONE)) {
            if (App.getInstance().isSimulator) {
                onReceiveFirebaseAuthResult(-1, new Intent(Intent.ACTION_ALL_APPS));
            } else {
                firebaseAuthHelper.loginByPhone(BaseAuthActivity.this);
            }
        }
    }

    //chka Hayk
//    private void loginWithSocial(Activity activity) {
//        appSharedHelper.saveFirstAuth(true);
//        appSharedHelper.saveSavedRememberMe(true);
////        if (loginType.equals(LoginType.FACEBOOK)){
////            facebookHelper.login(new FacebookLoginCallback());
////        } else if (loginType.equals(LoginType.TWITTER_DIGITS)){
////     pagac twitter       twitterDigitsHelper.login(twitterDigitsAuthCallback);
////        }
////       startMainActivity();
//        if (loginType.equals(LoginType.FACEBOOK)) {
//            facebookHelper.login(new FacebookLoginCallback());
//        } else if (loginType.equals(LoginType.FIREBASE_PHONE)) {
//            firebaseAuthHelper.loginByPhone(BaseAuthActivity.this);
//        }
//    }

    protected void startMainActivity(QBUser user) {
        AppSession.getSession().updateUser(user);

//        authenticationActivity = new AuthenticationActivity();
//        authenticationActivity.user = user;
//        startActivityForResult(new Intent(BaseAuthActivity.this, authenticationActivity.getClass()),138);


//        startMainActivity();
        /////////////////////////////////////////////////

//        AppSession.getSession().updateUser(user);
//        try {
//            new TamaAccountHelper().register(this, this, user.getPhone());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    //chka
//    protected void startMainActivity(boolean importInitialized) {
//        appSharedHelper.saveUsersImportInitialized(importInitialized);
//        startMainActivity();
//    }

    protected void startMainActivity() {
        MainActivity.start(BaseAuthActivity.this);
        finish();
    }

    //chka
//    protected void parseExceptionMessage(Exception exception) {
//        hideProgress();
//
//        String errorMessage = exception.getMessage();
//
//        if (errorMessage != null) {
//            if (errorMessage.equals(getString(R.string.error_bad_timestamp))) {
//                errorMessage = getString(R.string.error_bad_timestamp_from_app);
//            } else if (errorMessage.equals(getString(R.string.error_login_or_email_required))) {
//                errorMessage = getString(R.string.error_login_or_email_required_from_app);
//            } else if (errorMessage.equals(getString(R.string.error_email_already_taken))
//                    && loginType.equals(LoginType.FACEBOOK)) {
//                errorMessage = getString(R.string.error_email_already_taken_from_app);
//            } else if (errorMessage.equals(getString(R.string.error_unauthorized))) {
//                errorMessage = getString(R.string.error_unauthorized_from_app);
//            }
//
//            ErrorUtils.showError(this, errorMessage);
//        }
//    }
//
//    //chka
//    protected void parseFailException(Bundle bundle) {
//        Exception exception = (Exception) bundle.getSerializable(QBServiceConsts.EXTRA_ERROR);
//        int errorCode = bundle.getInt(QBServiceConsts.EXTRA_ERROR_CODE);
//        parseExceptionMessage(exception);
//    }
//
//    //chka
//    protected void performLoginSuccessAction(Bundle bundle) {
//        QBUser user = (QBUser) bundle.getSerializable(QBServiceConsts.EXTRA_USER);
//
//        startMainActivity(user);
//
//        // send analytics data
//        GoogleAnalyticsHelper.pushAnalyticsData(BaseAuthActivity.this, user, "User Sign In");
//        FlurryAnalyticsHelper.pushAnalyticsData(BaseAuthActivity.this);
//    }

    //chka
    //    protected void login(String userEmail, String userPassword) {
//        appSharedHelper.saveFirstAuth(true);
//        appSharedHelper.saveSavedRememberMe(true);
//        appSharedHelper.saveUsersImportInitialized(true);
//        QBUser user = new QBUser(null, userPassword, userEmail);
//        AppSession.getSession().closeAndClear();
//        QBLoginCompositeCommand.start(this, user);
//    }

    protected void login(String userEmail, final String userPassword) {
        appSharedHelper.saveFirstAuth(true);
        appSharedHelper.saveSavedRememberMe(true);
        appSharedHelper.saveUsersImportInitialized(true);
        QBUser user = new QBUser(null, userPassword, userEmail);

        serviceManager.login(user).subscribe(new Observer<QBUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError" + e.getMessage());
                hideProgress();
                AuthUtils.parseExceptionMessage(BaseAuthActivity.this, e.getMessage());
            }

            @Override
            public void onNext(QBUser qbUser) {
//                performLoginSuccessAction(qbUser);
            }
        });
    }

    protected void startLandingScreen() {
        LandingActivity.start(this);
        finish();
    }

    private void performLoginSuccessAction(QBUser user) {


//        startMainActivity(user);


        AppSession.getSession().updateUser(user);

        Intent intent = new Intent(BaseAuthActivity.this, AuthenticationActivity.class);
        intent.putExtra(AuthenticationActivity.EXTRA_QB_USER, user);
        startActivityForResult(intent,AuthenticationActivity.REQUEST_CODE_USER_DATA);


//        String phone = user.getPhone();
//        authenticationActivity.userPhone = user.getPhone();
//        startActivityForResult(intent,138);

//        startActivityForResult(new Intent(BaseAuthActivity.this, AuthenticationActivity.class),138138);


//        startMainActivity();
        /////////////////////////////////////////////////

//        AppSession.getSession().updateUser(user);
//        try {
//            new TamaAccountHelper().register(this, this, user.getPhone());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


        // send analytics data
        GoogleAnalyticsHelper.pushAnalyticsData(this, user, "User Sign In");
        FlurryAnalyticsHelper.pushAnalyticsData(this);
    }

    //chka
//    protected boolean isLoggedInToServer() {
//        return AppSession.getSession().isLoggedIn();
//    }
//
//    //chka
//    private void addActions() {
//        addAction(QBServiceConsts.LOGIN_SUCCESS_ACTION, loginSuccessAction);
//        addAction(QBServiceConsts.LOGIN_FAIL_ACTION, failAction);
//
//        addAction(QBServiceConsts.SOCIAL_LOGIN_SUCCESS_ACTION, socialLoginSuccessAction);
//        addAction(QBServiceConsts.SOCIAL_LOGIN_FAIL_ACTION, failAction);
//
//        addAction(QBServiceConsts.SIGNUP_FAIL_ACTION, failAction);
//
//        updateBroadcastActionList();
//    }
//
    //chka
//    private void removeActions() {
//        removeAction(QBServiceConsts.LOGIN_SUCCESS_ACTION);
//        removeAction(QBServiceConsts.LOGIN_FAIL_ACTION);
//
//        removeAction(QBServiceConsts.SOCIAL_LOGIN_SUCCESS_ACTION);
//        removeAction(QBServiceConsts.SOCIAL_LOGIN_FAIL_ACTION);
//
//        removeAction(QBServiceConsts.SIGNUP_FAIL_ACTION);
//
//        updateBroadcastActionList();
//    }
//
//
//
//    //chka
//    private class LoginSuccessAction implements Command {
//
//        @Override
//        public void execute(Bundle bundle) throws Exception {
//            performLoginSuccessAction(bundle);
//        }
//    }
//
//    //chka
//    private class SocialLoginSuccessAction implements Command {
//
//        @Override
//        public void execute(Bundle bundle) throws Exception {
//            QBUser user = (QBUser) bundle.getSerializable(QBServiceConsts.EXTRA_USER);
//            QBUpdateUserCommand.start(BaseAuthActivity.this, user, null);
//
//            performLoginSuccessAction(bundle);//registr anel tamaAccountum
//        }
//    }


    //qcaca Base
//    private class FailAction implements Command {
//
//        @Override
//        public void execute(Bundle bundle) throws Exception {
//            parseFailException(bundle);
//        }
//    }


    private Observer<QBUser> socialLoginObserver = new Observer<QBUser>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError " + e.getMessage());
            hideProgress();
            AuthUtils.parseExceptionMessage(BaseAuthActivity.this, e.getMessage());
        }

        @Override
        public void onNext(QBUser qbUser) {
            performLoginSuccessAction(qbUser);
        }
    };

    private class FacebookLoginCallback implements FacebookCallback<LoginResult> {

        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "+++ FacebookCallback call onSuccess from BaseAuthActivity +++");
            showProgress();
//            serviceManager.login(QBProvider.FACEBOOK, loginResult.getAccessToken().getToken(), null)        .subscribe(socialLoginObserver);
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "+++ FacebookCallback call onCancel from BaseAuthActivity +++");
            hideProgress();
        }

        @Override
        public void onError(FacebookException error) {
            Log.d(TAG, "+++ FacebookCallback call onCancel BaseAuthActivity +++");
            hideProgress();
        }
    }

    private class FirebaseAuthCallback implements FirebaseAuthHelper.RequestFirebaseIdTokenCallback {

        @Override
        public void onSuccess(String authToken) {
            Log.d(TAG, "FirebaseAuthCallback onSuccess()");
            showProgress();//,tama-7aeab  def502007f76de74681894346a098feb954009548fd84854bb427b70f9d69a468cd2dc35e0c575da6cc02ac1e48015c95c79dec0ce0e4990be5dc90a36961528b63c776a126da28dc624dfb8bb2cf4a0cd9500a284a8ef5340b461cc15d230bc7e6d1ba468f0be92e647016734fb78d8aea1eafda34123ba39ca1acdb74a844134e338e73af4150e15f9d3b42be6bfc5fffda58a888c382382c06093c9e01cf394157a12131db6ccb27e7ae236d911c59d966915f86fb7c0ef821788ef0696501c002ae11f4b37c9c51a0aa728368dffc4aba51f64071dbe94abcb7b220e772a8ec1531966293f3560241bf0d9c2e80b3b4a40d16b885e68335144e7e3de8edc230dd05df2473b2feecd1ebb2af8b620667f2f83fc1979e92fc076634f9467d0240a90fefc63ab7e435bc168e199240caa5763451af4dddfff6aa1ad57f154b7020af0cd07ee54388a87b4df8df47ab81aea35d395a22e717d3ed8cb7c3acd02856d8f
//            if(App.getInstance().isSimulator){  eyJhbGciOiJSUzI1NiIsImtpZCI6IjAwOTZhZDZmZjdjMTIwMzc5MzFiMGM0Yzk4YWE4M2U2ZmFkOTNlMGEifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGFtYS03YWVhYiIsImF1ZCI6InRhbWEtN2FlYWIiLCJhdXRoX3RpbWUiOjE1MzA2MTI1MzUsInVzZXJfaWQiOiI2OFVXdDBVOWdrWm4wUkNQZmtVQnVIdU04TVcyIiwic3ViIjoiNjhVV3QwVTlna1puMFJDUGZrVUJ1SHVNOE1XMiIsImlhdCI6MTUzMDYxMjYwOSwiZXhwIjoxNTMwNjE2MjA5LCJwaG9uZV9udW1iZXIiOiIrMzc0Nzc5Mzk3MzMiLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7InBob25lIjpbIiszNzQ3NzkzOTczMyJdfSwic2lnbl9pbl9wcm92aWRlciI6InBob25lIn19.Cwyzbg9ikDITltrIJS0-8FkiAiFclzSTo3sB-ymxBo2il7t9Z0lmHco2YFsMncIobfFypX7tenvg4lCJgZZxPak0UAclZ--02psvx5gkyFBS6aCZnuAk4Tswa8GIf2J5RQtjX9UaRrT2E9NMVzICqEuWQRiZDA_I8mkOumbFVu7xA6Y5kX2doFahdDjgv3gDqBTvejzXwixjyrHJbN8cOJQ5PEtPJ9A5_hW7iR-sb8YhYojWKrFCKllABwC2V_B9rA69ffrC7kmXbQp_x1j4hgOuZzrUUr9_yLz0C00yCQ4v6LM7cIPNCoA651pbuShtTYbnHUiih2Qm2fN7-4eFbw
                serviceManager.login(QBProvider.FIREBASE_PHONE, authToken, StringObfuscator.getFirebaseAuthProjectId())
                    .subscribe(socialLoginObserver);
//            }else {
//                serviceManager.login(QBProvider.FIREBASE_PHONE, authToken, StringObfuscator.getFirebaseAuthProjectId())
//                    .subscribe(socialLoginObserver);
//            }

        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "FirebaseAuthCallback onError()");
            hideProgress();
        }
    }

    //chka
//    private class TwitterDigitsAuthCallback implements AuthCallback {
//
//        @Override
//        public void success(DigitsSession session, String phoneNumber) {
//            Log.d(TAG, "Success login by number: " + phoneNumber);
//
//            showProgress();

//            TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
//            TwitterAuthToken authToken = session.getAuthToken();
//            DigitsOAuthSigning authSigning = new DigitsOAuthSigning(authConfig, authToken);
//            Map<String, String> authHeaders = authSigning.getOAuthEchoHeadersForVerifyCredentials();
//
//            QBSocialLoginCommand.start(BaseAuthActivity.this, QBProvider.TWITTER_DIGITS,
//                    authHeaders.get(TwitterDigitsHelper.PROVIDER),
//                    authHeaders.get(TwitterDigitsHelper.CREDENTIALS));
//        }
//
//        @Override
//        public void failure(DigitsException error) {
//            Log.d(TAG, "Failure!!!! error: " + error.getLocalizedMessage());
//            hideProgress();
//        }
//    }


//    private static String[] parseJSon(String data) throws JSONException {
//        if (data == null) {
//            return null;
//        }
//        JSONObject jsonObject = new JSONObject(data);
//        JSONObject jsonData = jsonObject.getJSONObject("data");
//        String user_id = jsonData.getString("user_id");
//        String is_europe = jsonData.getString("is_europe");
//
//        return new String[]{user_id, is_europe};
//    }
//
//    @Override
//    public void requestSuccess(String data) {
//        String[] response = new String[]{};
//        try {
//            response = parseJSon(data);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        AppSession.getSession().setTamaAccountId(response[0]);
//        appSharedHelper.saveTamaAccountId(response[0]);
//        appSharedHelper.saveTamaIsEurope(response[1].equals("1"));
//        startMainActivity();
//
//    }
//
//    @Override
//    public void requestError(String data) {
//        ToastUtils.longToast(data);
//    }
//
//    @Override
//    public void alertDialogCancelListener() {
//
//    }
//
//    @Override
//    public Context getAppContext() {
//        return this;
//    }

}