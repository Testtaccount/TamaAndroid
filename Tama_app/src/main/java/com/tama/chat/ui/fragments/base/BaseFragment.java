package com.tama.chat.ui.fragments.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.tama.chat.App;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.utils.bridges.ActionBarBridge;
import com.tama.chat.utils.bridges.ConnectionBridge;
import com.tama.chat.utils.bridges.LoadingBridge;
import com.tama.chat.utils.bridges.SnackbarBridge;
import com.tama.chat.utils.listeners.ServiceConnectionListener;
import com.tama.chat.utils.listeners.UserStatusChangingListener;
import com.tama.q_municate_core.qb.helpers.QBChatHelper;
import com.tama.q_municate_core.qb.helpers.QBFriendListHelper;
import com.tama.q_municate_core.service.QBService;
import com.tama.q_municate_core.utils.NetworkUtil;

public abstract class BaseFragment extends Fragment implements UserStatusChangingListener, ServiceConnectionListener {

    protected App app;
    protected BaseActivity baseActivity;
    protected BaseActivity.FailAction failAction;
    protected ConnectionBridge connectionBridge;
    protected ActionBarBridge actionBarBridge;
    protected LoadingBridge loadingBridge;
    protected SnackbarBridge snackbarBridge;

    protected QBFriendListHelper friendListHelper;
    protected QBChatHelper chatHelper;

    protected QBService service;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("BaseFragment", "onAttach");

        if (activity instanceof BaseActivity) {
            Log.d("BaseFragment", "activity instanceof BaseActivity");
            baseActivity = (BaseActivity) activity;
            service = baseActivity.getService();
            friendListHelper = baseActivity.getFriendListHelper();
            chatHelper = baseActivity.getChatHelper();
        }

        if (activity instanceof ConnectionBridge) {
            connectionBridge = (ConnectionBridge) activity;
        }

        if (activity instanceof ActionBarBridge) {
            actionBarBridge = (ActionBarBridge) activity;
        }

        if (activity instanceof LoadingBridge) {
            loadingBridge = (LoadingBridge) activity;
        }

        if (activity instanceof SnackbarBridge) {
            snackbarBridge = (SnackbarBridge) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseFragment", "onCreate");

        addListeners();

        app = App.getInstance();
        failAction = baseActivity.getFailAction();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("BaseFragment", "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("BaseFragment", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    public void initActionBar() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("BaseFragment", "onResume()");
        initActionBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BaseFragment", "onDestroy()");
        removeListeners();
    }

    private void addListeners() {
        baseActivity.addFragmentUserStatusChangingListener(this);
        baseActivity.addFragmentServiceConnectionListener(this);
    }

    private void removeListeners() {
        baseActivity.removeFragmentUserStatusChangingListener(this);
        baseActivity.removeFragmentServiceConnectionListener(this);
    }

    public boolean isNetworkAvailable() {
        return NetworkUtil.isConnected(baseActivity);
    }

    protected boolean isExistActivity() {
        return ((!isDetached()) && (baseActivity != null));
    }

    protected void activateButterKnife(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onChangedUserStatus(int userId, boolean online) {
        // nothing by default
    }

    @Override
    public void onConnectedToService(QBService service) {
        // nothing by default
    }
}