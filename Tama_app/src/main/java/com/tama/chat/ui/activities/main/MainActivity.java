package com.tama.chat.ui.activities.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.q_municate_user_service.QMUserService;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.tama.chat.R;
import com.tama.chat.app.Config;
import com.tama.chat.gcm.GSMHelper;
import com.tama.chat.ui.activities.base.BaseLoggableActivity;
import com.tama.chat.ui.fragments.chats.ContactsListFragment;
import com.tama.chat.ui.fragments.chats.DialogsListFragment;
import com.tama.chat.ui.fragments.map.MapBlinkFragment;
import com.tama.chat.ui.fragments.settings.SettingsFragment;
import com.tama.chat.ui.fragments.tamaaccount.MyTamaAccountFragment;
import com.tama.chat.utils.MediaUtils;
import com.tama.chat.utils.NotificationUtils;
import com.tama.chat.utils.helpers.FacebookHelper;
import com.tama.chat.utils.helpers.ImportFriendsHelper;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.chat.utils.image.ImageUtils;
import com.tama.q_municate_core.core.command.Command;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.UserCustomData;
import com.tama.q_municate_core.service.QBServiceConsts;
import com.tama.q_municate_core.utils.Utils;
import com.tama.q_municate_core.utils.helpers.CoreSharedHelper;
import com.tama.q_municate_db.managers.DataManager;

public class MainActivity extends BaseLoggableActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TTT = "myLogs";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 00001;
    private static final int PERMISSION_READ_STATE = 102;
    private static final int PERMISSION_READ_CONTACTS = 101;

    private FacebookHelper facebookHelper;
//    private GSMHelper gsmHelper;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private ImportFriendsSuccessAction importFriendsSuccessAction;
    private ImportFriendsFailAction importFriendsFailAction;

    private GSMHelper gsmHelper;
    private int currentNavigationItemId = R.id.navigation_chats;
    private boolean showUserIcon = true;

    @Bind(R.id.navigation)
    BottomNavigationView navigation;

    @Bind(R.id.toolbar_view)
    LinearLayout toolbarView;

    @Bind(R.id.toolbar_tama_view)
    LinearLayout toolbarTamaView;

    @Bind(R.id.tama_toolbar_logo)
    ImageView tamaToolbarLogo;

    @Bind(R.id.tama_toolbar_user_icon)
    ImageView tamaToolbarUserIcon;

    @Bind(R.id.tama_toolbar_title)
    TextView tamaToolbarTitle;

    @Bind(R.id.tama_toolbar_subtitle)
    TextView tamaToolbarSubtitle;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_main;
    }

//    @Override
//    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//    }

    //    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        initFields();
        setUpActionBarWithUpButton();


        addDialogsAction();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_STATE);
        } else {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            subscribeToPushNotifications(refreshedToken);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_READ_CONTACTS);
            } else {
                if (!isChatInitializedAndUserLoggedIn()) {
                    Log.d("MainActivity", "onCreate. !isChatInitializedAndUserLoggedIn()");
                    loginChat();
                }
            }
        }

        if(getIntent().getIntExtra(PAGE, 0)==0){
            launchDialogsListFragment();
        }
        else {
//            setCurrentSettingsFragment(R.id.navigation_settings);
            navigation.setSelectedItemId(R.id.navigation_settings);
        }


        //chka Hayk
        RegistrationBroadcast();
        openPushDialogIfPossible();
    }

    private void openPushDialogIfPossible() {
        CoreSharedHelper sharedHelper = CoreSharedHelper.getInstance();
        if (sharedHelper.needToOpenDialog()) {
            QBChatDialog chatDialog = DataManager.getInstance().getQBChatDialogDataManager()
                .getByDialogId(sharedHelper.getPushDialogId());
            QMUser user = QMUserService.getInstance().getUserCache()
                .get((long) sharedHelper.getPushUserId());
            if (chatDialog != null) {
                startDialogActivity(chatDialog, user);
            }
        }
    }

    private void startDialogActivity(QBChatDialog chatDialog, QMUser user) {
        if (QBDialogType.PRIVATE.equals(chatDialog.getType())) {
            startPrivateChatActivity(user, chatDialog);
        } else {
            startGroupChatActivity(chatDialog);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_READ_STATE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                subscribeToPushNotifications(refreshedToken);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_READ_CONTACTS);
                } else {
                    if (!isChatInitializedAndUserLoggedIn()) {
                        Log.d("MainActivity", "onCreate. !isChatInitializedAndUserLoggedIn()");
                        loginChat();
                    }
                }
            }
        } else if (requestCode == PERMISSION_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!isChatInitializedAndUserLoggedIn()) {
                    Log.d("MainActivity", "onCreate. !isChatInitializedAndUserLoggedIn()");
                    loginChat();
                }

            }
        }
    }


    //chka
    private void RegistrationBroadcast() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TTT, "mtnuma = " + intent.getAction().equals(Config.REGISTRATION_COMPLETE));
                Log.d(TTT, "mtnuma 2 = " + intent.getAction().equals("pushNotification"));

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals("pushNotification")) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message,
                        Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext()
            .getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
    }

    private void initFields() {
        Log.d(TAG, "initFields()");
        title = " " + AppSession.getSession().getUser().getFullName();
        navigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener());
        gsmHelper = new GSMHelper(this);
        importFriendsSuccessAction = new ImportFriendsSuccessAction();
        importFriendsFailAction = new ImportFriendsFailAction();
        facebookHelper = new FacebookHelper(MainActivity.this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (currentNavigationItemId == item.getItemId()) {
                    return true;
                }
                switch (item.getItemId()) {
                    case R.id.navigation_chats:
                        setCurrentChatsFragment(R.id.navigation_chats);
                        return true;
                    case R.id.navigation_contacts:
                        setCurrentContactsListFragment(R.id.navigation_contacts);
                        return true;
//                    case R.id.navigation_map:
//                        setCurrentMapBlinkFragment(R.id.navigation_map);
//                        return true;
                    case R.id.navigation_account:
                        setCurrentMyTamaAccountFragment(R.id.navigation_account);
                        return true;
                    case R.id.navigation_settings:
                        setCurrentSettingsFragment(R.id.navigation_settings);
                        return true;
                }
                return false;
            }
        };
    }

    private void setTamaToolbar() {
//        checkVisibilityTamaUserIcon(tamaToolbarUserIcon);

        tamaToolbarTitle.setText(getString(R.string.tama_family));
        tamaToolbarSubtitle.setText(getString(R.string.my_account));
    }

    private void setCurrentChatsFragment(int itemId) {
        toolbarView.setVisibility(View.VISIBLE);
        toolbarTamaView.setVisibility(View.GONE);
        checkVisibilityUserIcon();
        showUserIcon = true;
        title = " " + AppSession.getSession().getUser().getFullName();
        setActionBarTitle(title);
        setCurrentFragment(DialogsListFragment.newInstance());
        currentNavigationItemId = itemId;
    }

    private void setCurrentContactsListFragment(int itemId) {
        toolbarView.setVisibility(View.VISIBLE);
        toolbarTamaView.setVisibility(View.GONE);
        setCurrentFragment(ContactsListFragment.newInstance());
        currentNavigationItemId = itemId;
        showUserIcon = false;
    }

    private void setCurrentMapBlinkFragment(int itemId) {
        toolbarView.setVisibility(View.VISIBLE);
        toolbarTamaView.setVisibility(View.GONE);
        setCurrentFragment(MapBlinkFragment.newInstance());
        currentNavigationItemId = itemId;
        showUserIcon = false;
    }

    private void setCurrentSettingsFragment(int itemId) {
        toolbarView.setVisibility(View.VISIBLE);
        toolbarTamaView.setVisibility(View.GONE);
        setCurrentFragment(SettingsFragment.newInstance());
        currentNavigationItemId = itemId;
        showUserIcon = false;
    }

    private void setCurrentMyTamaAccountFragment(int itemId) {
        toolbarView.setVisibility(View.GONE);
        toolbarTamaView.setVisibility(View.VISIBLE);
        setTamaToolbar();
        setCurrentFragment(MyTamaAccountFragment.newInstance());
        currentNavigationItemId = itemId;
        showUserIcon = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookHelper.onActivityResult(requestCode, resultCode, data);
        if (SettingsFragment.REQUEST_CODE_LOGOUT == requestCode && RESULT_OK == resultCode) {
            startLandingScreen();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        navigation.setSelectedItemId(R.id.navigation_chats);
        Log.d(TTT, "MainActivity onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity", "onRestart()");
    }

    @Override
    protected void onResume() {
        Log.d(TTT, "Activity onResume");

        actualizeCurrentTitle();
        super.onResume();
        addActions();

        //chka Hayk
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
            new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
            new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        checkGCMRegistration();//<-
    }

    private void actualizeCurrentTitle() {
        if (AppSession.getSession().getUser().getFullName() != null) {
            title = " " + AppSession.getSession().getUser().getFullName();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        removeActions();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeDialogsAction();
    }

    @Override
    protected void checkShowingConnectionError() {
        if (!isNetworkAvailable()) {
            setActionBarTitle(getString(R.string.dlg_internet_connection_is_missing));
            setActionBarIcon(null);
        } else {
            setActionBarTitle(title);
            if (showUserIcon) {
                checkVisibilityUserIcon();
            }
        }
    }

    @Override
    protected void performLoginChatSuccessAction(Bundle bundle) {
        super.performLoginChatSuccessAction(bundle);
        actualizeCurrentTitle();
    }

    private void addDialogsAction() {
        addAction(QBServiceConsts.LOAD_CHATS_DIALOGS_SUCCESS_ACTION, new LoadChatsSuccessAction());
    }

    private void removeDialogsAction() {
        removeAction(QBServiceConsts.LOAD_CHATS_DIALOGS_SUCCESS_ACTION);
    }

    private void addActions() {
        addAction(QBServiceConsts.IMPORT_FRIENDS_SUCCESS_ACTION, importFriendsSuccessAction);
        addAction(QBServiceConsts.IMPORT_FRIENDS_FAIL_ACTION, importFriendsFailAction);

        updateBroadcastActionList();
    }

    private void removeActions() {
        removeAction(QBServiceConsts.IMPORT_FRIENDS_SUCCESS_ACTION);
        removeAction(QBServiceConsts.IMPORT_FRIENDS_FAIL_ACTION);

        updateBroadcastActionList();
    }

    private void checkVisibilityUserIcon() {
        UserCustomData userCustomData = Utils
            .customDataToObject(AppSession.getSession().getUser().getCustomData());
        if (!TextUtils.isEmpty(userCustomData.getAvatarUrl())) {
            loadLogoActionBar(userCustomData.getAvatarUrl());
        } else {
            setActionBarIcon(MediaUtils.getRoundIconDrawable(this,
                BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_user)));
        }
    }

    private void loadLogoActionBar(String logoUrl) {
        ImageLoader.getInstance()
            .loadImage(logoUrl, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS,
                new SimpleImageLoadingListener() {

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedBitmap) {
                        setActionBarIcon(
                            MediaUtils.getRoundIconDrawable(MainActivity.this, loadedBitmap));
                    }
                });
    }

    private void checkVisibilityTamaUserIcon(ImageView v) {
        UserCustomData userCustomData = Utils
            .customDataToObject(AppSession.getSession().getUser().getCustomData());
        if (!TextUtils.isEmpty(userCustomData.getAvatarUrl())) {
            loadLogoTamaActionBar(userCustomData.getAvatarUrl(), v);
        }
//        else {
//            setTamaCustomActionBarIcon(ImageUtils.getRectIconDrawable(
//                    BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_rect_user),dpToPixel(48), dpToPixel(4)),v);
//        }
    }

    private void loadLogoTamaActionBar(String logoUrl, final ImageView v) {
        ImageLoader.getInstance().loadImage(logoUrl,
            ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS,
            new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedBitmap) {
                    setTamaCustomActionBarIcon(
                        ImageUtils.getRectIconDrawable(loadedBitmap, dpToPixel(48), dpToPixel(4)),
                        v);
                }
            });
    }

    private void performImportFriendsSuccessAction() {
        appSharedHelper.saveUsersImportInitialized(true);
        hideProgress();
    }

    private void checkGCMRegistration() {
        if (gsmHelper.checkPlayServices()) {
            if (!gsmHelper.isDeviceRegisteredWithUser()) {
                gsmHelper.registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }


    //chka Hayk
    @SuppressLint("MissingPermission")
    public void subscribeToPushNotifications(String registrationID) {
        QBSubscription subscription = new QBSubscription(QBNotificationChannel.GCM);
        subscription.setEnvironment(QBEnvironment.DEVELOPMENT);
        //
        String deviceId;
        final TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(
                Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            deviceId = mTelephony.getDeviceId(); //*** use for mobiles
        } else {
            deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID); //*** use for tablets
        }
        subscription.setDeviceUdid(deviceId);
        //
        subscription.setRegistrationID(registrationID);
        //
        QBPushNotifications.createSubscription(subscription);
    }



    private void performImportFriendsFailAction(Bundle bundle) {
        performImportFriendsSuccessAction();
    }

    private void launchDialogsListFragment() {
        Log.d(TAG, "launchDialogsListFragment()");
//        setCurrentFragment(DialogsListFragment.newInstance(), true);
        setCurrentChatsFragment(R.id.navigation_chats);
    }

    private void startImportFriends() {
        ImportFriendsHelper importFriendsHelper = new ImportFriendsHelper(MainActivity.this);

        if (facebookHelper.isSessionOpened()) {
            importFriendsHelper.startGetFriendsListTask(true);
        } else {
            importFriendsHelper.startGetFriendsListTask(false);
        }

        hideProgress();
    }

    private class ImportFriendsSuccessAction implements Command {

        @Override
        public void execute(Bundle bundle) {
            performImportFriendsSuccessAction();
        }
    }

    private class ImportFriendsFailAction implements Command {

        @Override
        public void execute(Bundle bundle) {
            performImportFriendsFailAction(bundle);
        }
    }
}