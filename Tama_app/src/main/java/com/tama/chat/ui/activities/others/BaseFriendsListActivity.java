package com.tama.chat.ui.activities.others;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseLoggableActivity;
import com.tama.chat.ui.adapters.friends.FriendsAdapter;
import com.tama.q_municate_core.service.QBService;
import com.tama.q_municate_core.utils.UserFriendUtils;
import com.tama.q_municate_db.managers.DataManager;
import com.tama.q_municate_db.models.Friend;
import java.util.List;

public abstract class BaseFriendsListActivity extends BaseLoggableActivity {

    @Bind(R.id.friends_recyclerview)
    protected RecyclerView friendsRecyclerView;

    protected FriendsAdapter friendsAdapter;
    protected DataManager dataManager;

    @Override
    protected void onStart() {
        super.onStart();
        initFields();
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (checkNetworkAvailableWithError()) {
                    performDone();
                }
                break;
            default:
                super.onOptionsItemSelected(item);
        }
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
        dataManager = DataManager.getInstance();
    }

    protected void initRecyclerView() {
        friendsAdapter = getFriendsAdapter();
        friendsAdapter.setFriendListHelper(friendListHelper);
        friendsRecyclerView.setAdapter(friendsAdapter);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected List<QMUser> getFriendsList() {
        List<Friend> friendsList = dataManager.getFriendDataManager().getAllSorted();
        return UserFriendUtils.getUsersFromFriends(friendsList);
    }

    protected abstract FriendsAdapter getFriendsAdapter();

    protected abstract void performDone();
}