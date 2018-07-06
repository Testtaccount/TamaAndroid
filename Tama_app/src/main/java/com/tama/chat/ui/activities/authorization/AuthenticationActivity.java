package com.tama.chat.ui.activities.authorization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.users.model.QBUser;
import com.soundcloud.android.crop.Crop;
import com.tama.chat.BuildConfig;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelper;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.base.BaseLoggableActivity;
import com.tama.chat.utils.KeyboardUtils;
import com.tama.chat.utils.MediaUtils;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.ValidationUtils;
import com.tama.chat.utils.helpers.MediaPickHelper;
import com.tama.chat.utils.helpers.ServiceManager;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.chat.utils.listeners.OnMediaPickedListener;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.UserCustomData;
import com.tama.q_municate_core.utils.Utils;
import com.tama.q_municate_db.models.Attachment;
import com.tama.q_municate_db.utils.ErrorUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;


public class AuthenticationActivity extends BaseLoggableActivity implements OnMediaPickedListener,
    TamaAccountHelperListener {


  public static final int REQUEST_CODE_USER_DATA = 1011;
  public static final String EXTRA_PHONE_NUMBER = "phone_number";
  public static final String EXTRA_QB_USER = "extra_qb_user";

  public QBUser qbUser;

  public String userPhone;
  @Bind(R.id.first_name_textinputlayout)
  TextInputLayout firstNameTextInputLayout;

  @Bind(R.id.first_name_edittext)
  EditText firstNameEditText;

  @Bind(R.id.last_name_textinputlayout)
  TextInputLayout lastNameTextInputLayout;

  @Bind(R.id.last_name_edittext)
  EditText lastNameEditText;

  @Bind(R.id.userImageView)
  ImageView avatarImageView;

  @Bind(R.id.chooseImage)
  Button chooseImage;

  @Bind(R.id.finishAuthentication)
  Button finishAuthentication;
//  private boolean isNeedUpdateImage;

  private MediaPickHelper mediaPickHelper;
  private Uri imageUri;
  private UserCustomData userCustomData;
  private String currentFullName;

  public AuthenticationActivity() {

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authentication);
    ButterKnife.bind(this);
    mediaPickHelper = new MediaPickHelper();
    if (getIntent().getExtras() != null) {
      qbUser = (QBUser) getIntent().getSerializableExtra(EXTRA_QB_USER);
      this.userPhone = qbUser.getPhone();
      firstNameEditText.setText(qbUser.getFullName());
    }

    initCustomData();
    loadAvatar();
    updateOldData();

  }

  private void loadAvatar() {
    if (userCustomData != null && !TextUtils.isEmpty(userCustomData.getAvatarUrl())) {
      ImageLoader.getInstance().displayImage(userCustomData.getAvatarUrl(),
          avatarImageView, ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Crop.REQUEST_CROP) {
      handleCrop(resultCode, data);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected int getContentResId() {
    return R.layout.activity_authentication;
  }

  private void startCropActivity(Uri originalUri) {
    String extensionOriginalUri = originalUri.getPath().substring(originalUri.getPath().lastIndexOf(".")).toLowerCase();

    imageUri = MediaUtils.getValidUri(new File(getCacheDir(), extensionOriginalUri), this);
    Crop.of(originalUri, imageUri).asSquare().start(this);
  }

  private void handleCrop(int resultCode, Intent result) {
    if (resultCode == RESULT_OK) {
//      isNeedUpdateImage = true;
      avatarImageView.setImageURI(imageUri);
    } else if (resultCode == Crop.RESULT_ERROR) {
      ToastUtils.longToast(Crop.getError(result).getMessage());
    }
  }

  @OnClick(R.id.chooseImage)
  public void clickChooseImage() {
    mediaPickHelper.pickAnMedia(this, MediaUtils.IMAGE_REQUEST_CODE);
  }

  @Override
  public void onMediaPicked(int requestCode, Attachment.Type attachmentType, Object attachment) {
    if (Attachment.Type.IMAGE.equals(attachmentType)) {
      startCropActivity(MediaUtils.getValidUri((File) attachment, this));
    }
  }

  @Override
  public void onMediaPickError(int requestCode, Exception e) {
    ErrorUtils.showError(this, e);
  }

  @Override
  public void onMediaPickClosed(int requestCode) {
  }


  @OnClick(R.id.finishAuthentication)
  public void clickfinishAuthentication() {
    KeyboardUtils.hideKeyboard(this);
    if (checkNetworkAvailableWithError()) {
        updateUser();
    }

  }
//
//  public static final Uri getUriToDrawable(@NonNull Context context,
//      @AnyRes int drawableId) {
//    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
//        "://" + context.getResources().getResourcePackageName(drawableId)
//        + '/' + context.getResources().getResourceTypeName(drawableId)
//        + '/' + context.getResources().getResourceEntryName(drawableId) );
//    return imageUri;
//  }

  private void updateUser() {
    initCurrentData();

    if (isDataChanged()) {
      saveChanges();
    }
  }

  private void saveChanges() {
    if (new ValidationUtils(this)
        .isFirstNameValid(firstNameTextInputLayout, firstNameEditText.getText().toString().trim()) && new ValidationUtils(this)
        .isLastNameValid(lastNameTextInputLayout, lastNameEditText.getText().toString().trim()) ) {
      showProgress();


      QBUser newUser = createUserForUpdating();
      File file = null;
      if (imageUri != null) {
        file = MediaUtils.getCreatedFileFromUri(imageUri);
      }

      Observable<QMUser> qmUserObservable;

      if (file != null) {
        qmUserObservable = ServiceManager.getInstance().updateUser(newUser, file);
      } else {
        qmUserObservable = ServiceManager.getInstance().updateUser(newUser);
      }

      qmUserObservable.subscribe(new Subscriber<QMUser>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
          hideProgress();

          if (e != null) {
            ToastUtils.longToast(e.getMessage());
          }

          resetUserData();
        }

        @Override
        public void onNext(QMUser qmUser) {
          AppSession.getSession().updateUser(qmUser);
//          updateOldData();
          try {

          @Nullable
          byte[] inputData;
            InputStream iStream = getContentResolver().openInputStream(imageUri==null?Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.drawable.placeholder_rect_user):imageUri);
            inputData = getBytes(iStream);

            new TamaAccountHelper().register(AuthenticationActivity.this, AuthenticationActivity.this, userPhone, firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(), toBase64(inputData));
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }

        }
      });
    }
  }

  private void resetUserData() {
    qbUser.setFullName(firstNameEditText.getText().toString());
//    isNeedUpdateImage = false;
    initCurrentData();
  }

  private void updateOldData() {
//    isNeedUpdateImage = false;
  }


  private QBUser createUserForUpdating() {
    QBUser newUser = new QBUser();
    newUser.setId(qbUser.getId());
    newUser.setPassword(qbUser.getPassword());
    newUser.setOldPassword(qbUser.getOldPassword());
    qbUser.setFullName(firstNameEditText.getText().toString());
    newUser.setFullName(firstNameEditText.getText().toString());
    newUser.setFacebookId(qbUser.getFacebookId());
    newUser.setTwitterId(qbUser.getTwitterId());
    newUser.setTwitterDigitsId(qbUser.getTwitterDigitsId());
    newUser.setCustomData(Utils.customDataToString(userCustomData));
    return newUser;
  }

  private void initCurrentData() {
    currentFullName = firstNameEditText.getText().toString();
    initCustomData();
  }

  private void initCustomData() {
    userCustomData = Utils.customDataToObject(qbUser.getCustomData());
    if (userCustomData == null) {
      userCustomData = new UserCustomData();
    }
  }

  private boolean isDataChanged() {
    return true;
  }

  private static String[] parseJSon(String data) throws JSONException {
    if (data == null) {
      return null;
    }
    JSONObject jsonObject = new JSONObject(data);
//        JSONObject jsonData = jsonObject.getJSONObject("data");
    String user_id = jsonObject.getString("user_id");//0
    String token_type = jsonObject.getString("token_type");//1
    String expires_in = jsonObject.getString("expires_in");//2
    String access_token = jsonObject.getString("access_token");//3
    String refresh_token = jsonObject.getString("refresh_token");//4

    return new String[]{user_id, token_type, expires_in, access_token, refresh_token};
  }
//    token_type": "Bearer",
//        "expires_in": 31535998,
//        "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjZlMGJjZjBkMWY3NWM0NDRhZWQyNGY5ZWQ1ZTI4MWUxZjYyNDZmOTBkNWQwOTkyZDBiN2QzNWZmMDM4NTM3ZDJiYTFmNTY5NWU2NGIyY2Q4In0.eyJhdWQiOiIyIiwianRpIjoiNmUwYmNmMGQxZjc1YzQ0NGFlZDI0ZjllZDVlMjgxZTFmNjI0NmY5MGQ1ZDA5OTJkMGI3ZDM1ZmYwMzg1MzdkMmJhMWY1Njk1ZTY0YjJjZDgiLCJpYXQiOjE1Mjc1OTk2NTksIm5iZiI6MTUyNzU5OTY1OSwiZXhwIjoxNTU5MTM1NjU4LCJzdWIiOiI1OSIsInNjb3BlcyI6WyIqIl19.Ivx2u_FQriC6RQq_iCWF-Z9aTLYGC1k3W_KnSCkcjJxaCLvzjNuaaeiZ2SJ7sUKp6XUNQV5ZKXmZLvwJtZoLaYZUDZlDvRpi3Ck8lEDmbbPbtkNbDVR3Tv0gbQkSvXA-Vrr_NHFDtMFR8fcYcbGEa1gWy089r0DUK5azplv6v53Uc6uTWESn35nVxdblg-Rlsi5ldl8RefnIO4n5DydnQ8Y6YJ89EsW98u3LPiL7R3GV3JnmyagrBuorAjt7uVtDn9lr5ZBzfF5Gdw3u9OmaAd0SLRbNkDy_U0clxu0GUmcKnsjp3bMU_pqC330GoidrSgNCjjnZ4jybmW1IiJs2-urReCwUdLyl2LNm0TCBPlNxFwX4aU7n4tKOtIWto4GMUNZwU7j10M2VnXowS0lJ6TSHkblz-idTom717vrXumwFOWqblTRB6VY_uL08SzqNwNNQQu803s5MMCrsln0BBDOaIoHwpv39cFLzNya6AXV_Dt6F6sKSIeKIERmuUii8-S1rsmOg8T-NiZy7OlQwbA36JcOkW5BxZxiQPor1GVUR6QpcQBFPO0RNq0eK5eBcN4u1h09Jq1iaCqkgeZ5eIh8IBMW0y4m4dmRexa3LsOF3Ko7R2HDFSH_EQPa_MTMgaHghzhMGA4GERh7ujXPzW5IhJeQRf9Gs8GQDA6dDPXE",
//        "refresh_token"

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
    appSharedHelper.saveTamaAccountTokenType(response[1]);
    appSharedHelper.saveTamaAccountExpiresIn(response[2]);
    appSharedHelper.saveTamaAccountAccessToken(response[3]);
    appSharedHelper.saveTamaAccountRefreshToken(response[4]);
    //appSharedHelper.saveTamaIsEurope(response[1].equals("1"));
//        startMainActivity();
    hideProgress();

    setResult(Activity.RESULT_OK);
    finish();
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

  private String toBase64(byte[] bytes) {
    return Base64.encodeToString(bytes, Base64.DEFAULT);
  }

  public byte[] getBytes(InputStream inputStream) throws IOException {
    if (inputStream == null) {
      return null;
    }
    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
    int bufferSize = 1024;
    byte[] buffer = new byte[bufferSize];

    int len = 0;
    while ((len = inputStream.read(buffer)) != -1) {
      byteBuffer.write(buffer, 0, len);
    }
    return byteBuffer.toByteArray();
  }

}