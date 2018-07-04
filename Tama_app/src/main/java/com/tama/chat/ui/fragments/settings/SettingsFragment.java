package com.tama.chat.ui.fragments.settings;

import static android.app.Activity.RESULT_OK;
import static com.tama.chat.app.MyContextWrapper.getLanguage;
import static com.tama.chat.app.MyContextWrapper.isChangeCurrentLocale;
import static com.tama.chat.app.MyContextWrapper.saveLanguage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.users.QBUsers;
import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.authorization.LandingActivity;
import com.tama.chat.ui.activities.authorization.SplashActivity;
import com.tama.chat.ui.activities.feedback.FeedbackActivity;
import com.tama.chat.ui.activities.profile.MyProfileActivity;
import com.tama.chat.ui.fragments.base.BaseFragment;
import com.tama.chat.ui.fragments.dialogs.base.TwoButtonsDialogFragment;
import com.tama.chat.ui.views.roundedimageview.RoundedImageView;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.helpers.FacebookHelper;
import com.tama.chat.utils.helpers.FirebaseAuthHelper;
import com.tama.chat.utils.helpers.ServiceManager;
import com.tama.chat.utils.helpers.SharedHelper;
import com.tama.chat.utils.helpers.TwitterDigitsHelper;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.q_municate_core.core.command.Command;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.service.QBServiceConsts;
import com.tama.q_municate_core.utils.UserFriendUtils;
import com.tama.q_municate_db.managers.DataManager;
import com.tama.q_municate_db.utils.ErrorUtils;
import java.util.Locale;
import rx.Subscriber;

//chka avelacrac
public class SettingsFragment extends BaseFragment implements TamaAccountHelperListener {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private static final String TAG = "myLogs";
  private static final String SAVED_SNAP_TIME = "save_snap_time";
  public static final int REQUEST_CODE_LOGOUT = 300;
  public static final String CURRENT_PAGE_NUMBER =  "current_page_number";

  @Bind(R.id.avatar_imageview)
  RoundedImageView avatarImageView;

  @Bind(R.id.full_name_edittext)
  TextView fullNameTextView;

  @Bind(R.id.push_notification_switch)
  SwitchCompat pushNotificationSwitch;

  @Bind(R.id.save_photo_video_switch)
  SwitchCompat savePhotoVideoSwitch;

  @Bind(R.id.snap_chat_spinner)
  Spinner snapChatSpinner;

//  @Bind(R.id.change_password_view)
//  RelativeLayout changePasswordView;

  @Bind(R.id.app_language_spinner)
  Spinner appLanguageSpinner;

  private QMUser user;
  private SharedHelper sharedHelper;
  private FacebookHelper facebookHelper;
  private FirebaseAuthHelper firebaseAuthHelper;

  private TwitterDigitsHelper twitterDigitsHelper;
  protected SharedHelper appSharedHelper;
  private String[] data = {"3 seconds", "5 seconds", "10 seconds", "20 seconds", "30 seconds", "1 minutes", "5 minutes"};
  private String[] dataLanguages = {"English","Deutsch", "Español" ,"Français", "Italiano" };

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;
  private Locale myLocale;

  public SettingsFragment() {
    // Required empty public constructor
  }

  public static SettingsFragment newInstance(String param1, String param2) {
    SettingsFragment fragment = new SettingsFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  public static SettingsFragment newInstance() {
    SettingsFragment fragment = new SettingsFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedHelper = new SharedHelper(getContext());
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, container, false);
    // Inflate the layout for this fragment
    activateButterKnife(view);
    initFields();
    addActions();

    return view;
  }

  @Override
  public void onResume() {
    Log.d(TAG, "Fragment onResume");
    super.onResume();
    updateUIData();
  }

  private void updateUIData() {
    user = UserFriendUtils.createLocalUser(AppSession.getSession().getUser());
    fillUI();
    baseActivity.setActionBarIcon(null);
    baseActivity.setActionBarTitle(getString(R.string.settings_title));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    removeActions();
  }

  @OnClick(R.id.edit_profile_imagebutton)
  void editProfile() {
    MyProfileActivity.start(getActivity());
  }

  @OnCheckedChanged(R.id.push_notification_switch)
  void enablePushNotification(boolean enable) {
    appSharedHelper.saveEnablePushNotifications(enable);
    QBSettings.getInstance().setEnablePushNotification(enable);
  }

  @OnCheckedChanged(R.id.save_photo_video_switch)
  void enableSavePhotoVideo(boolean enable) {
    appSharedHelper.saveEnableSavePhotoVideo(enable);
  }

  @OnClick(R.id.invite_friends_button)
  void inviteFriends() {


    try {
      final String appPackageName = getContext().getPackageName();

//      Bitmap b=((TabsActivity)getActivity()).contentBitmap;
//      Uri pictureUri = getImageUri(getContext(),b);

      String sAux = "\nLet me recommend you this application\n\n";
      sAux = sAux + "https://play.google.com/store/apps/details?id="+appPackageName+" \n\n";
//
      Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType("text/plain");
      shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tama Family");
      shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);
//      shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
//      shareIntent.setType("image/*");
//      shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      startActivity(Intent.createChooser(shareIntent, "Share via"));
    } catch(Exception e) {
      e.printStackTrace();
    }

//    InviteFriendsActivity.start(getActivity());

  }

  @OnClick(R.id.give_feedback_button)
  void giveFeedback() {
    FeedbackActivity.start(getActivity());
  }

//  @OnClick(R.id.change_password_button)
//  void changePassword() {
//    ChangePasswordActivity.start(getActivity());
//  }



//  @OnClick(R.id.logout_button)
//  void logout() {
//    if (baseActivity.checkNetworkAvailableWithError()) {
//      TwoButtonsDialogFragment
//          .show(baseActivity.getSupportFragmentManager(), R.string.dlg_logout, R.string.dlg_confirm,
//              new MaterialDialog.ButtonCallback() {
//                @Override
//                public void onPositive(MaterialDialog dialog) {
//                  super.onPositive(dialog);
//                  baseActivity.showProgress();
//
//                  facebookHelper.logout();
////                                    twitterDigitsHelper.logout();
//                  AppSession.getSession().closeAndClear();
//                  DataManager.getInstance().clearAllTables();
//                  QBLogoutCompositeCommand.start(getContext());
//                }
//              });
//    }
//  }



  @OnItemSelected(R.id.snap_chat_spinner)
  void snapChatTimeChange(AdapterView<?> parent, View view,
      int position, long id) {
    Log.d(TAG, "snapChatTimeChange = " + position);
    int temp;
    switch (position) {
      case 0:
        temp = 3;
        break;
      case 1:
        temp = 5;
        break;
      case 2:
        temp = 10;
        break;
      case 3:
        temp = 20;
        break;
      case 4:
        temp = 30;
        break;
      case 5:
        temp = 60;
        break;
      case 6:
        temp = 300;
        break;
      default:
        temp = 3;
        break;
    }
    saveSnapTime(temp);
  }

  @OnItemSelected(R.id.app_language_spinner)
  void setAppLanguageSpinnerChange(AdapterView<?> parent, View view, int position, long id) {
    Log.d(TAG, "snapChatTimeChange = " + position);
//    int temp;
    switch (position) {
      case 0:
        if(isChangeCurrentLocale(getActivity(),getString(R.string.en)))
          changeLanguages(getString(R.string.en));
        break;
      case 1:
        if(isChangeCurrentLocale(getActivity(),getString(R.string.de)))
          changeLanguages(getString(R.string.de));
        break;
      case 2:
        if(isChangeCurrentLocale(getActivity(),getString(R.string.es)))
          changeLanguages(getString(R.string.es));
        break;
      case 3:
        if(isChangeCurrentLocale(getActivity(),getString(R.string.fr)))
          changeLanguages(getString(R.string.fr));
        break;
      case 4:
        if(isChangeCurrentLocale(getActivity(),getString(R.string.it)))
          changeLanguages(getString(R.string.it));
        break;
      default:
        if(isChangeCurrentLocale(getActivity(),getString(R.string.en)))
          changeLanguages(getString(R.string.en));
        break;
    }
//    saveSnapTime(temp);
  }

  private void changeLanguages(String sLocale){
    saveLanguage(getActivity(),sLocale);

    Bundle outState = new Bundle();
//    outState.putParcelable("baby", baby);
//    outState.putBoolean("isPersonChange", isPersonChange);
//    outState.putInt("monthCount", monthCount);
    Intent intent = new Intent(getActivity(), SplashActivity.class);
    intent.putExtra(CURRENT_PAGE_NUMBER,3);
//    intent.putExtra(BUNDLE, outState);
    startActivity(intent);
    onPause();
    getActivity().finish();

//    Locale myLocale = new Locale(sLocale);
//    Resources res = getResources();
//    DisplayMetrics dm = res.getDisplayMetrics();
//    Configuration conf = res.getConfiguration();
//    conf.locale = myLocale;
//    res.updateConfiguration(conf, dm);

//    myLocale = new Locale(sLocale);
//    Resources res = getResources();
//    DisplayMetrics dm = res.getDisplayMetrics();
//    Configuration conf = res.getConfiguration();
//    conf.locale = myLocale;
//    res.updateConfiguration(conf, dm);
//    onConfigurationChanged(conf);

//    SplashActivity.start(getActivity());
//    getActivity().finish();
  }

//  @Override
//  public void onConfigurationChanged(Configuration newConfig) {
//    // refresh your views here
//
////    finishButton.setText(R.string.finish);
//    baseActivity.recreate();
//    super.onConfigurationChanged(newConfig);
//    // Checks the active language
////    if (newConfig.locale == Locale.ENGLISH) {
////      Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
////    } else if (newConfig.locale == Locale.FRENCH){
////      Toast.makeText(this, "French", Toast.LENGTH_SHORT).show();
////    }
//  }

  @Override
  public void initActionBar() {
    super.initActionBar();
    actionBarBridge.setActionBarUpButtonEnabled(false);
  }

  private void initFields() {
    baseActivity.setActionBarIcon(null);
    baseActivity.setActionBarTitle(getString(R.string.settings_title));

    user = UserFriendUtils.createLocalUser(AppSession.getSession().getUser());
    appSharedHelper = App.getInstance().getAppSharedHelper();
    facebookHelper = new FacebookHelper(getActivity());
    firebaseAuthHelper = new FirebaseAuthHelper();
    twitterDigitsHelper = new TwitterDigitsHelper();
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(baseActivity,
        android.R.layout.simple_spinner_item, data);
    ArrayAdapter<String> adapterLanguages = new ArrayAdapter<String>(baseActivity,
        android.R.layout.simple_spinner_item, dataLanguages);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    adapterLanguages.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    snapChatSpinner.setAdapter(adapter);
    snapChatSpinner.setSelection(getSpinerPositionByTime(loadSnapTime()));
    appLanguageSpinner.setAdapter(adapterLanguages);
    appLanguageSpinner.setSelection(getAppLanguageSpinerPositionByLanguage());
  }

  private void saveSnapTime(int snapTime) {
    SharedPreferences sPref = baseActivity
        .getSharedPreferences(getResources().getString(R.string.snap_pref),
            baseActivity.MODE_PRIVATE);
    SharedPreferences.Editor ed = sPref.edit();
    ed.putInt(SAVED_SNAP_TIME, snapTime);
    ed.apply();
  }

  private int loadSnapTime() {
    SharedPreferences sPref = baseActivity.getSharedPreferences(getResources().getString(R.string.snap_pref),baseActivity.MODE_PRIVATE);
    return sPref.getInt(getResources().getString(R.string.save_snap_time_pref), 3);
  }

  private int loadAppLanguage() {
return 0;
  }


  private int getSpinerPositionByTime(int time) {
    int temp;
    switch (time) {
      case 3:
        temp = 0;
        break;
      case 5:
        temp = 1;
        break;
      case 10:
        temp = 2;
        break;
      case 20:
        temp = 3;
        break;
      case 30:
        temp = 4;
        break;
      case 60:
        temp = 5;
        break;
      case 300:
        temp = 6;
        break;
      default:
        temp = 0;
        break;

    }
    return temp;
  }

  private int getAppLanguageSpinerPositionByLanguage() {
    int temp;
    switch (getLanguage(getActivity())) {
      case "en":
        temp = 0;
        break;
      case "de":
        temp = 1;
        break;
      case "es":
        temp = 2;
        break;
      case "fr":
        temp = 3;
        break;
      case "it":
        temp = 4;
        break;
      default:
        temp = 0;
        break;

    }
    return temp;
  }

  private void fillUI() {
    pushNotificationSwitch.setChecked(appSharedHelper.isEnablePushNotifications());
    savePhotoVideoSwitch.setChecked(appSharedHelper.isEnableSavePhotoVideo());
//    changePasswordView.setVisibility(
//        LoginType.EMAIL.equals(AppSession.getSession().getLoginType()) ? View.VISIBLE : View.GONE);
    fullNameTextView.setText(user.getFullName());
    showUserAvatar();
  }

  private void showUserAvatar() {
    ImageLoader.getInstance().displayImage(
        user.getAvatar(),
        avatarImageView,
        ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
  }

  private void addActions() {
    baseActivity.addAction(QBServiceConsts.LOGOUT_SUCCESS_ACTION, new LogoutSuccessAction());
    baseActivity.addAction(QBServiceConsts.LOGOUT_FAIL_ACTION, failAction);

    baseActivity.updateBroadcastActionList();
  }

  private void removeActions() {
    baseActivity.removeAction(QBServiceConsts.LOGOUT_SUCCESS_ACTION);
    baseActivity.removeAction(QBServiceConsts.LOGOUT_FAIL_ACTION);

    baseActivity.updateBroadcastActionList();
  }

  protected void startLandingScreen() {
    Intent intent = new Intent(getContext(), LandingActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    LandingActivity.start(baseActivity, intent);
    baseActivity.finish();
  }

  @OnClick(R.id.logout_button)
  void logout() {
    if (baseActivity.checkNetworkAvailableWithError()) {
      TwoButtonsDialogFragment
          .show(baseActivity.getSupportFragmentManager(), R.string.dlg_logout, R.string.dlg_confirm,
              new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                  super.onPositive(dialog);
                  baseActivity.showProgress();

                  facebookHelper.logout();
                  firebaseAuthHelper.logout();


                  ServiceManager.getInstance().logout(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                      ErrorUtils.showError(getActivity(), e);
                      baseActivity.hideProgress();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                      getActivity().setResult(RESULT_OK);
                      baseActivity.hideProgress();
                      baseActivity.finish();
                    }
                  });

                }
              });
    }
  }

  @OnClick(R.id.delete_my_account_button)
  void deleteAccount() {

    if (baseActivity.checkNetworkAvailableWithError()) {
      TwoButtonsDialogFragment
          .show(baseActivity.getSupportFragmentManager(), R.string.dlg_delete, R.string.dlg_confirm,
              new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                  super.onPositive(dialog);
                  baseActivity.showProgress();

                  facebookHelper.logout();
                  firebaseAuthHelper.logout();



                  ServiceManager.getInstance().logout(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                      ErrorUtils.showError(getActivity(), e);
                      baseActivity.hideProgress();
                    }

                    @Override
                    public void onNext(Void aVoid) {


                      baseActivity.hideProgress();
//                      baseActivity.finish();
                    }
                  });

                  new TamaAccountHelper().setRevokeAccount(SettingsFragment.this, sharedHelper.getTamaAccountId());

                }
              });



    }


  }

  @Override
  public void requestSuccess(final String data) {

    QBUsers.deleteUser(AppSession.getSession().getUser().getId()).performAsync(
        new QBEntityCallback<Void>() {
          @Override
          public void onSuccess(Void aVoid, Bundle bundle) {
//            ToastUtils.longToast("user del 1 success");
          }

          @Override
          public void onError(QBResponseException e) {
//            ToastUtils.longToast("user del 1 error");

          }
        });

//    QBUsers.deleteByExternalId(AppSession.getSession().getUser().getExternalId()).performAsync(
//        new QBEntityCallback<Void>() {
//          @Override
//          public void onSuccess(Void aVoid, Bundle bundle) {
//            ToastUtils.longToast("user del 2 success");
//
//          }
//
//          @Override
//          public void onError(QBResponseException e) {
//            ToastUtils.longToast("user del 2 error");
//
//          }
//        });
//        Thread thread = new Thread(new Runnable() {
//      @Override
//      public void run() {
//        try {
//          QBUsers.deleteUser(AppSession.getSession().getUser().getId()).perform();
//          QBUsers.deleteByExternalId(AppSession.getSession().getUser().getExternalId()).perform();
//        } catch (QBResponseException e) {
//          e.printStackTrace();
//        }
//      }
//    });
//    thread.start();
    AppSession.getSession().closeAndClear();
    DataManager.getInstance().clearAllTables();
//    QBLogoutCompositeCommand.start(getContext());
    ToastUtils.longToast(data);
    sharedHelper.removeUserId();
    baseActivity.finish();
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
    return getActivity();
  }

  private class LogoutSuccessAction implements Command {

    @Override
    public void execute(Bundle bundle) {
      startLandingScreen();
    }
  }
}
