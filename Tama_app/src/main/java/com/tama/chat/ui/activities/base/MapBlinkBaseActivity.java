package com.tama.chat.ui.activities.base;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.tama.chat.App;
import com.tama.chat.R;
import com.tama.chat.ui.activities.call.CallActivity;
import com.tama.chat.ui.fragments.dialogs.base.ProgressDialogFragment;
import com.tama.chat.utils.ToastUtils;
import com.tama.chat.utils.bridges.ActionBarBridge;
import com.tama.chat.utils.bridges.LoadingBridge;
import com.tama.chat.utils.helpers.SharedHelper;
import com.tama.chat.utils.listeners.ServiceConnectionListener;
import com.tama.chat.utils.listeners.UserStatusChangingListener;
import com.tama.q_municate_core.core.command.Command;
import com.tama.q_municate_core.qb.helpers.QBChatHelper;
import com.tama.q_municate_core.qb.helpers.QBFriendListHelper;
import com.tama.q_municate_core.qb.helpers.QBGroupChatHelper;
import com.tama.q_municate_core.qb.helpers.QBPrivateChatHelper;
import com.tama.q_municate_core.service.QBService;
import com.tama.q_municate_core.service.QBServiceConsts;
import com.tama.q_municate_db.utils.ErrorUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import butterknife.ButterKnife;
//ckka
public abstract class MapBlinkBaseActivity extends AppCompatActivity implements ActionBarBridge, LoadingBridge {

    protected App app;
    protected Toolbar toolbar;
    protected SharedHelper appSharedHelper;
    protected Fragment currentFragment;
    protected FailAction failAction;
    protected SuccessAction successAction;
    protected QBFriendListHelper friendListHelper;
    protected QBPrivateChatHelper privateChatHelper;
    protected QBChatHelper chatHelper;
    protected QBGroupChatHelper groupChatHelper;
    protected QBService service;
    protected LocalBroadcastManager localBroadcastManager;
    protected String title;


    private ProgressBar toolbarProgressBar;
    private ActionBar actionBar;
    private Set<UserStatusChangingListener> fragmentsStatusChangingSet;
    private Set<ServiceConnectionListener> fragmentsServiceConnectionSet;



    protected abstract int getContentResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        Log.d("MapBlinkBaseActivity", "onCreate");

        initFields();

        activateButterKnife();
    }

    @Override
    public void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarProgressBar = (ProgressBar) findViewById(R.id.toolbar_progressbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        actionBar = getSupportActionBar();
    }

    @Override
    public void setActionBarTitle(String title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void setActionBarTitle(@StringRes int title) {
        setActionBarTitle(getString(title));
    }

    @Override
    public void setActionBarSubtitle(String subtitle) {
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    @Override
    public void setActionBarSubtitle(@StringRes int subtitle) {
        setActionBarSubtitle(getString(subtitle));
    }

    @Override
    public void setActionBarIcon(Drawable icon) {
        if (actionBar != null) {
            // In appcompat v21 there will be no icon if we don't add this display option
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(icon);
        }
    }

    @Override
    public void setActionBarIcon(@DrawableRes int icon) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getDrawable(icon);
        } else {
            drawable = getResources().getDrawable(icon);
        }

        setActionBarIcon(drawable);
    }

    @Override
    public void setActionBarUpButtonEnabled(boolean enabled) {
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(enabled);
            actionBar.setDisplayHomeAsUpEnabled(enabled);
        }
    }

    @Override
    public synchronized void showProgress() {
        ProgressDialogFragment.show(getSupportFragmentManager());
    }

    @Override
    public synchronized void hideProgress() {
        ProgressDialogFragment.hide(getSupportFragmentManager());
    }

    @Override
    public void showActionBarProgress() {
        toolbarProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideActionBarProgress() {
        toolbarProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateToParent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MapBlinkBaseActivity", "onActivityResult");
        if(requestCode == CallActivity.CALL_ACTIVITY_CLOSE){
            if (resultCode == CallActivity.CALL_ACTIVITY_CLOSE_WIFI_DISABLED) {
                ToastUtils.longToast(R.string.wifi_disabled);
            }
        }
    }

    private void initFields() {
        Log.d("MapBlinkBaseActivity", "initFields()");
        app = App.getInstance();
        appSharedHelper = App.getInstance().getAppSharedHelper();
        failAction = new FailAction();
        successAction = new SuccessAction();
        fragmentsStatusChangingSet = new HashSet<>();
        fragmentsServiceConnectionSet = new HashSet<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    protected void setUpActionBarWithUpButton() {
        initActionBar();
        setActionBarUpButtonEnabled(true);
        setActionBarTitle(title);
    }

    public void onChangedUserStatus(int userId, boolean online) {
        notifyChangedUserStatus(userId, online);
    }

    public void notifyChangedUserStatus(int userId, boolean online) {
        if (!fragmentsStatusChangingSet.isEmpty()) {
            Iterator<UserStatusChangingListener> iterator = fragmentsStatusChangingSet.iterator();
            while (iterator.hasNext()) {
                iterator.next().onChangedUserStatus(userId, online);
            }
        }
    }

    public void notifyConnectedToService() {
        if (!fragmentsServiceConnectionSet.isEmpty()) {
            Iterator<ServiceConnectionListener> iterator = fragmentsServiceConnectionSet.iterator();
            while (iterator.hasNext()) {
                iterator.next().onConnectedToService(service);
            }
        }
    }

    public void onConnectedToService(QBService service) {
        if (friendListHelper == null) {
            friendListHelper = (QBFriendListHelper) service.getHelper(QBService.FRIEND_LIST_HELPER);
        }

//        if (privateChatHelper == null) {
//            privateChatHelper = (QBPrivateChatHelper) service.getHelper(QBService.chPRIVATE_CHAT_HELPER);
//        }
//
//        if (groupChatHelper == null) {
//            groupChatHelper = (QBGroupChatHelper) service.getHelper(QBService.GROUP_CHAT_HELPER);
//        }


        if (chatHelper == null) {
            chatHelper = (QBChatHelper) service.getHelper(QBService.CHAT_HELPER);
        }
        notifyConnectedToService();
    }

    private void navigateToParent() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        if (intent == null) {
            finish();
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    private void setCurrentFragment(Fragment fragment, String tag) {
        currentFragment = fragment;
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = buildTransaction();
        transaction.replace(R.id.container_fragment, fragment, tag);
        transaction.commit();
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

    public FailAction getFailAction() {
        return failAction;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.d("BaseActivity", "onAttachFragment");
    }

    private void activateButterKnife() {
        ButterKnife.bind(this);
    }

    public class FailAction implements Command {

        @Override
        public void execute(Bundle bundle) {
            Exception e = (Exception) bundle.getSerializable(QBServiceConsts.EXTRA_ERROR);
            ErrorUtils.showError(MapBlinkBaseActivity.this, e);
            hideProgress();
        }
    }

    public class SuccessAction implements Command {

        @Override
        public void execute(Bundle bundle) {
            hideProgress();
        }
    }
}