package com.tama.chat.ui.fragments.chats;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.tama.chat.R;
import com.tama.chat.app.PhoneNumber;
import com.tama.chat.ui.activities.profile.UserProfileActivity;
import com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity;
import com.tama.chat.ui.adapters.friends.FriendsAdapter;
import com.tama.chat.ui.fragments.base.BaseFragment;
import com.tama.chat.utils.KeyboardUtils;
import com.tama.chat.utils.listeners.simple.SimpleOnRecycleItemClickListener;
import com.tama.q_municate_core.service.QBService;
import com.tama.q_municate_core.utils.UserFriendUtils;
import com.tama.q_municate_db.managers.DataManager;
import com.tama.q_municate_db.models.Friend;
import java.util.List;
//chka
public class ContactsListFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    @Bind(R.id.friends_recyclerview)
    RecyclerView friendsRecyclerView;

    private DataManager dataManager;
    private FriendsAdapter friendsAdapter;
    private QMUser selectedUser;
    private boolean isSelectedUser = false;
    private PhoneNumber phoneNumber;
    private static final String ARG_PARAM_1 = "param_1";
    private static final String ARG_PARAM_2 = "param_2";
    private TamaAccountBaseActivity mListener;

    public static ContactsListFragment newInstance() {
        ContactsListFragment fragment = new ContactsListFragment();
        return fragment;
    }

    public static ContactsListFragment newInstance(boolean isSelectedUser, PhoneNumber phoneNumber) {
        ContactsListFragment fragment = new ContactsListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_1, isSelectedUser);
        args.putSerializable(ARG_PARAM_2, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isSelectedUser = bundle.getBoolean(ARG_PARAM_1);
            phoneNumber = (PhoneNumber) bundle.getSerializable(ARG_PARAM_2);
            mListener = (TamaAccountBaseActivity)getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        activateButterKnife(view);

        initFields();
        initRecyclerView();
        initCustomListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        baseActivity.setActionBarTitle(getString(R.string.action_bar_contacts));
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof TamaExpressActivity) {
//            mListener = (TamaExpressActivity) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchManager searchManager = (SearchManager) baseActivity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;

        if (searchMenuItem != null) {
            searchView = (SearchView) searchMenuItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(baseActivity.getComponentName()));
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onClose() {
        cancelSearch();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String searchQuery) {
        KeyboardUtils.hideKeyboard(baseActivity);
        search(searchQuery);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchQuery) {
        search(searchQuery);
        return true;
    }

    @Override
    public void onConnectedToService(QBService service) {
        super.onConnectedToService(service);
        if (friendListHelper != null) {
            friendsAdapter.setFriendListHelper(friendListHelper);
        }
    }

    @Override
    public void onChangedUserStatus(int userId, boolean online) {
        super.onChangedUserStatus(userId, online);
        friendsAdapter.notifyDataSetChanged();
    }

    private void initFields() {
        baseActivity.setActionBarIcon(null);
        baseActivity.setActionBarTitle(getString(R.string.action_bar_contacts));
        dataManager = DataManager.getInstance();
    }

    private void initRecyclerView() {
        List<Friend> friendsList = dataManager.getFriendDataManager().getAllSorted();
        friendsAdapter = new FriendsAdapter(baseActivity, UserFriendUtils.getUsersFromFriends(friendsList), true);
        friendsAdapter.setFriendListHelper(friendListHelper);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        friendsRecyclerView.setAdapter(friendsAdapter);
    }

    private void initCustomListeners() {
        friendsAdapter.setOnRecycleItemClickListener(new SimpleOnRecycleItemClickListener<QMUser>(){
            @Override
            public void onItemClicked(View view, QMUser entity, int position) {
                super.onItemClicked(view, entity, position);
                if(isSelectedUser){
                    switch (phoneNumber){
                        case FIRST:
                            mListener.setFirstPhoneNumber(entity.getPhone(),entity.getFullName());
                            break;
                        case SECOND:
                            mListener.setSecondPhoneNumber(entity.getPhone(),entity.getFullName());
                            break;
                        case THIRD:
                            mListener.setThirdPhoneNumber(entity.getPhone(),entity.getFullName());
                            break;
                    }
                    mListener.onResume();
                    getActivity().onBackPressed();
                }else {
                    selectedUser = entity;
                    UserProfileActivity.start(baseActivity, selectedUser.getId());
                }
            }
        });
    }

    private void search(String searchQuery) {
        if (friendsAdapter != null) {
            friendsAdapter.setFilter(searchQuery);
        }
    }

    private void cancelSearch() {
        if (friendsAdapter != null) {
            friendsAdapter.flushFilter();
        }
    }
}