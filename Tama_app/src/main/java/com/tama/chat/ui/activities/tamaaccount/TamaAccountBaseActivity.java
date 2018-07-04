package com.tama.chat.ui.activities.tamaaccount;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.tama.chat.R;
import com.tama.chat.tamaAccount.TamaAccountHelperListener;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.fragments.dialogs.base.ProgressDialogFragment;
import com.tama.chat.utils.helpers.SharedHelper;
import com.tama.chat.utils.image.ImageLoaderUtils;
import com.tama.chat.utils.image.ImageUtils;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.UserCustomData;
import com.tama.q_municate_core.utils.ConnectivityUtils;
import com.tama.q_municate_core.utils.Utils;

public abstract class TamaAccountBaseActivity extends BaseActivity implements TamaAccountHelperListener {

    protected Fragment currentFragment;
    private String phoneNumberFirst=null;
    private String phoneNumberSeconds=null;
    private String phoneNumberThird=null;
    private String nameFirstPhoneNumber=null;
    private String nameSecondPhoneNumber=null;
    private String nameThirdPhoneNumber=null;

    @Bind(R.id.toolbar_tama_view)
    LinearLayout toolbarTamaView;

    @Bind(R.id.tama_toolbar_user_icon)
    ImageView tamaToolbarUserIcon;

    @Bind(R.id.tama_toolbar_title)
    TextView tamaToolbarTitle;

    @Bind(R.id.tama_toolbar_subtitle)
    TextView tamaToolbarSubtitle;

    @Bind(R.id.shopping_btn)
    public RelativeLayout shoppingButton;

    @Bind(R.id.product_count)
    TextView productCount;

    protected abstract int getContentResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected String getTamaUserId(){
        SharedHelper sharedHelper = new SharedHelper(this);
        return sharedHelper.getTamaAccountId();
    }

    protected void setTamaToolbar(int title, int subTitle){
//        checkVisibilityTamaUserIcon(tamaToolbarUserIcon);
        tamaToolbarTitle.setText(getString(title));
        tamaToolbarSubtitle.setText(getString(subTitle));
    }

    public boolean isNetworkAvailable() {
        return ConnectivityUtils.isNetworkAvailable(this);
    }

    private void checkVisibilityTamaUserIcon(ImageView v) {
        UserCustomData userCustomData = Utils.customDataToObject(AppSession.getSession().getUser().getCustomData());
        if (!TextUtils.isEmpty(userCustomData.getAvatarUrl())) {
            loadLogoTamaActionBar(userCustomData.getAvatarUrl(), v);
        }
    }

    private void loadLogoTamaActionBar(String logoUrl,final ImageView v) {
        ImageLoader.getInstance().loadImage(logoUrl,
            ImageLoaderUtils.UIL_USER_AVATAR_DISPLAY_OPTIONS,
            new SimpleImageLoadingListener(){
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    Drawable icon = ImageUtils.getRectIconDrawable(loadedImage, dpToPixel(48),
                        dpToPixel(4));
                    v.setImageDrawable(icon);

                }
            });
    }

    public void setVisibleShoppingButton(int visible){
        shoppingButton.setVisibility(visible);
    }

    public void setVisibleProductsCount(int visible){
        productCount.setVisibility(visible);
    }

    public void setProductCountInButton(String str){
        productCount.setText(str);
    }

    public int dpToPixel(int dp){
        Resources resources = getBaseContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)(dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public double getDouble(String source) {
        if (TextUtils.isEmpty(source)) {
            return 0;
        }

        String number = "0";
        int length = source.length();

        boolean cutNumber = false;
        for (int i = 0; i < length; i++) {
            char c = source.charAt(i);
            if (cutNumber) {
                if (Character.isDigit(c) || c == '.' || c == ',') {
                    c = (c == ',' ? '.' : c);
                    number += c;
                } else {
                    cutNumber = false;
                    break;
                }
            } else {
                if (Character.isDigit(c)) {
                    cutNumber = true;
                    number += c;
                }
            }
        }
        return Double.parseDouble(number);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    protected String getCountryZipCode(Context context){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    protected void createDialog(String topupBalance, String title, String text){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.topup_dialog_view, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        TextView textTitle = (TextView) dialogView.findViewById(R.id.topup_dialog_title_text);
        textTitle.setText(title);

        TextView textBalance = (TextView) dialogView.findViewById(R.id.topup_dialog_big_text);
        if(!topupBalance.isEmpty()) {
            textBalance.setText(String.valueOf(topupBalance) + " €");
        }

        TextView textView = (TextView) dialogView.findViewById(R.id.topup_dialog_subtitle_text);
        textView.setText(text);

        Button topupDialogButton = (Button)dialogView.findViewById(R.id.topup_dialog_button);
        topupDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                alertDialogCancelListener();
            }
        });

        alertDialog.show();
    }

    public void setCurrentFragment(Fragment fragment) {
        currentFragment = fragment;

        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container_fragment, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @OnClick(R.id.shopping_btn)
    public void onClickOnShoppingCart(){

    }

    public void removeFragment() {
        if(!isFinishing()) {
            getSupportFragmentManager().beginTransaction().remove(
                    getSupportFragmentManager().findFragmentById(R.id.container_fragment)).commitAllowingStateLoss();
        }
    }

    private FragmentTransaction buildTransaction() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        return transaction;
    }

    public synchronized void showProgress() {
        ProgressDialogFragment.show(getSupportFragmentManager());
    }

    public synchronized void hideProgress() {
        ProgressDialogFragment.hide(getSupportFragmentManager());
    }

    public void setFirstPhoneNumber(String fullPhoneNumber){
        if(fullPhoneNumber.startsWith("+"))
            phoneNumberFirst = fullPhoneNumber;
        else
            phoneNumberFirst ="+" + fullPhoneNumber;
    }

    public void setFirstPhoneNumber(String fullPhoneNumber, String fullName){
        nameFirstPhoneNumber = fullName;
        if(fullPhoneNumber.startsWith("+"))
            phoneNumberFirst = fullPhoneNumber;
        else
            phoneNumberFirst ="+" + fullPhoneNumber;
    }

    public void setSecondPhoneNumber(String fullPhoneNumber){
        if(fullPhoneNumber.startsWith("+"))
            phoneNumberSeconds =fullPhoneNumber;
        else
            phoneNumberSeconds ="+" + fullPhoneNumber;
    }

    public void setSecondPhoneNumber(String fullPhoneNumber, String fullName){
        nameSecondPhoneNumber=fullName;
        if(fullPhoneNumber.startsWith("+"))
            phoneNumberSeconds =fullPhoneNumber;
        else
            phoneNumberSeconds ="+" + fullPhoneNumber;
    }

    public void setThirdPhoneNumber(String fullPhoneNumber){
        if(fullPhoneNumber.startsWith("+"))
            phoneNumberThird = fullPhoneNumber;
        else
            phoneNumberThird = "+" + fullPhoneNumber;
    }

    public void setThirdPhoneNumber(String fullPhoneNumber, String fullName){
        nameThirdPhoneNumber = fullName;
        if(fullPhoneNumber.startsWith("+"))
            phoneNumberThird = fullPhoneNumber;
        else
            phoneNumberThird = "+" + fullPhoneNumber;
    }

    public String getFirstNumber(){
        return phoneNumberFirst==null ? null:phoneNumberFirst;
    }

    public String getFirstNumberName(){
        return nameFirstPhoneNumber==null ? null:nameFirstPhoneNumber;
    }

    public String getSecondNumber(){
        return phoneNumberSeconds==null ? null:phoneNumberSeconds;
    }

    public String getSecondsNumberName(){
        return nameSecondPhoneNumber==null ? null:nameSecondPhoneNumber;
    }

    public String getThirdNumber(){
        return phoneNumberThird==null ? null:phoneNumberThird;
    }

    @Override
    public void requestSuccess(String data) {

    }

    @Override
    public void requestError(String data) {

    }

    @Override
    public void alertDialogCancelListener() {

    }
}
