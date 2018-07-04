package com.tama.chat.ui.activities.authorization;

import static com.tama.chat.app.MyContextWrapper.getLanguage;
import static com.tama.chat.ui.fragments.settings.SettingsFragment.CURRENT_PAGE_NUMBER;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import com.quickblox.auth.model.QBProvider;
import com.quickblox.auth.session.QBSessionManager;
import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.app.MyContextWrapper;
import com.tama.chat.ui.activities.main.MainActivity;
import com.tama.chat.utils.helpers.ServiceManager;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.service.QBServiceConsts;
import com.tama.q_municate_core.utils.helpers.CoreSharedHelper;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;

public class SplashActivity extends BaseAuthActivity{

    // Deutsch Español Français Italiano English

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int DELAY_FOR_OPENING_LANDING_ACTIVITY = 1000;
    int currentPage;
    @Override
    protected void attachBaseContext(Context newBase) {
        Locale myLocale = new Locale(getLanguage(newBase));
        Resources res = newBase.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, myLocale));
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        currentPage = getIntent().getIntExtra(CURRENT_PAGE_NUMBER, 0);

        //TODO VT temp code for correct migration from Twitter Digits to Firebase Phone Auth
        //should be removed in next release
        if(QBSessionManager.getInstance().getSessionParameters() != null
                && QBProvider.TWITTER_DIGITS.equals(QBSessionManager.getInstance().getSessionParameters().getSocialProvider())){
            restartAppWithFirebaseAuth();
            return;
        }
        //TODO END

        appInitialized = true;
        AppSession.load();

        processPushIntent();

        if (QBSessionManager.getInstance().getSessionParameters() != null && appSharedHelper.isSavedRememberMe()) {
            startLastOpenActivityOrMain();
//            startMainActivity();
        } else {
            startLandingActivity();
        }
    }

    private void processPushIntent() {
        boolean openPushDialog = getIntent().getBooleanExtra(QBServiceConsts.EXTRA_SHOULD_OPEN_DIALOG, false);
        CoreSharedHelper.getInstance().saveNeedToOpenDialog(openPushDialog);
    }

    //chkan Hayk
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isLoggedInToServer()) {
//            startMainActivity(true);
//        }
//    }
//
//    @Override
//    public void onStartSessionSuccess() {
//        appSharedHelper.saveSavedRememberMe(true);
//        startMainActivity(true);
//    }
//
////    @Override
////    protected void startMainActivity(QBUser user) {
////        try {
////            new TamaAccountHelper(this,appSharedHelper,user).register();
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        }
//////        String tamaAccountId = appSharedHelper.getTamaAccountId();
//////        if(tamaAccountId==null){
//////            try {
//////                new TamaAccountHelper(this,appSharedHelper,user).register();
//////            } catch (UnsupportedEncodingException e) {
//////                e.printStackTrace();
//////            }
//////        }else {
//////            AppSession.getSession().setTamaAccountId(tamaAccountId);
//////            super.startMainActivity(user);
//////        }
////
////    }
//
//    @Override
//    public void onStartSessionFail() {
//        startLandingActivity();
////        startMainActivity();
//
//    }
//
//    @Override
//    public void checkShowingConnectionError() {
//        // nothing. Toolbar is missing.
//    }

    private void startLandingActivity() {
        Log.v(TAG, "startLandingActivity();");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LandingActivity.start(SplashActivity.this);
                finish();
            }
        }, DELAY_FOR_OPENING_LANDING_ACTIVITY);
    }

    private void startLastOpenActivityOrMain() {
        Class<?> lastActivityClass;
        boolean needCleanTask = false;
        String lastActivityName = appSharedHelper.getLastOpenActivity();
        if (lastActivityName != null) {
//                lastActivityClass = Class.forName(appSharedHelper.getLastOpenActivity());
            lastActivityClass=MainActivity.class;
        } else {
//            needCleanTask = true;
            lastActivityClass = MainActivity.class;
        }
        Log.v(TAG, "start " + lastActivityClass.getSimpleName());
        startActivityByName(lastActivityClass, needCleanTask,currentPage);
    }

    private void restartAppWithFirebaseAuth(){
        ServiceManager.getInstance().logout(new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Intent intent = new Intent(App.getInstance(), LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                LandingActivity.start(App.getInstance(), intent);
                finish();
            }

            @Override
            public void onNext(Void aVoid) {
                Intent intent = new Intent(App.getInstance(), LandingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                LandingActivity.start(App.getInstance(), intent);
                finish();
            }
        });
    }
}