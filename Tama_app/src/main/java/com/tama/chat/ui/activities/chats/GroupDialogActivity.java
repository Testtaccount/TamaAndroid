package com.tama.chat.ui.activities.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.tama.chat.R;
import com.tama.chat.ui.activities.call.CallActivity;
import com.tama.chat.ui.adapters.chats.GroupChatMessagesAdapter;
import com.tama.chat.utils.ChatDialogUtils;
import com.tama.chat.utils.ToastUtils;
import com.tama.q_municate_core.models.AppSession;
import com.tama.q_municate_core.models.CombinationMessage;
import com.tama.q_municate_core.qb.commands.chat.QBLoadDialogByIdsCommand;
import com.tama.q_municate_core.qb.commands.chat.QBUpdateStatusMessageCommand;
import com.tama.q_municate_core.service.QBService;
import com.tama.q_municate_core.service.QBServiceConsts;
import com.tama.q_municate_core.utils.UserFriendUtils;
import com.tama.q_municate_db.models.DialogOccupant;
import com.tama.q_municate_db.models.State;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupDialogActivity extends BaseDialogActivity {

    private static final String TAG = GroupDialogActivity.class.getSimpleName();
    private List<DialogOccupant> dialogOccupantList; //Avetik

    public static void start(Context context, ArrayList<QMUser> friends) {
        Intent intent = new Intent(context, GroupDialogActivity.class);
        intent.putExtra(QBServiceConsts.EXTRA_FRIENDS, friends);
        context.startActivity(intent);
    }

    public static void start(Context context, QBChatDialog chatDialog) {
        Intent intent = new Intent(context, GroupDialogActivity.class);
        intent.putExtra(QBServiceConsts.EXTRA_DIALOG, chatDialog);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        context.startActivity(intent);
    }

    public static void startForResult(Fragment context, QBChatDialog chatDialog, int requestCode) {
        Intent intent = new Intent(context.getActivity(), GroupDialogActivity.class);
        intent.putExtra(QBServiceConsts.EXTRA_DIALOG, chatDialog);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initFields();

        actualizeCurrentDialogInfo();
    }

    private void actualizeCurrentDialogInfo() {
        if (currentChatDialog != null) {
            QBLoadDialogByIdsCommand.start(this, new ArrayList<>(Collections.singletonList(currentChatDialog.getDialogId())));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updateData();

        if (isNetworkAvailable()) {
            startLoadDialogMessages(true);
        }

        checkMessageSendingPossibility();
    }

    @Override
    protected void initChatAdapter() {
        messagesAdapter = new GroupChatMessagesAdapter(this, currentChatDialog, combinationMessagesList);
    }

    @Override
    protected void initMessagesRecyclerView() {
        super.initMessagesRecyclerView();
//        messagesAdapter = new GroupDialogMessagesAdapter(this, combinationMessagesList, this, dialog);

        messagesRecyclerView.addItemDecoration( new StickyRecyclerHeadersDecoration(messagesAdapter));
        messagesRecyclerView.setAdapter(messagesAdapter);

        scrollMessagesToBottom(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_dialog_menu, menu);
        return true;
    }

    @Override
    protected void onConnectServiceLocally(QBService service) {
        onConnectServiceLocally();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GroupDialogDetailsActivity.UPDATE_DIALOG_REQUEST_CODE == requestCode && GroupDialogDetailsActivity.RESULT_DELETE_GROUP == resultCode) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //chka
//    @Override
//    protected void onFileLoaded(QBFile file, String dialogId) {
//        if(!dialogId.equals(dialog.getDialogId())){
//            return;
//        }
//
//        try {
//            ((QBGroupChatHelper) baseChatHelper).sendGroupMessageWithAttachImage(dialog.getRoomJid(), file, isSnapChat, loadSnapTime());
//        } catch (QBResponseException e) {
//            ErrorUtils.showError(this, e);
//        }
//    }

    @Override
    protected Bundle generateBundleToInitDialog() {
        return null;
    }

    @Override
    protected void updateMessagesList() {
//        int oldMessagesCount = messagesAdapter.getAllItems().size();
//
//        this.combinationMessagesList = createCombinationMessagesList();
////        dialogOccupantList = createDialogOccupantList();//Avetik
//        processCombinationMessages();
//        messagesAdapter.setList(combinationMessagesList);
//
//        checkForScrolling(oldMessagesCount);
    }

    @Override
    protected void checkMessageSendingPossibility() {
        checkMessageSendingPossibility(isNetworkAvailable());
    }

//    @OnClick(R.id.toolbar)//Avetik
//    void openProfile(View view) {
//        GroupDialogDetailsActivity.start(this, dialog.getDialogId());
//    } //Hayk

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_group_audio_call:
                callToUser(createDialogOccupantList(), QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO);
                break;
            case R.id.switch_group_camera_toggle:
                callToUser(createDialogOccupantList(), QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO);
                break;
            case R.id.action_group_details:
                GroupDialogDetailsActivity.start(this, currentChatDialog.getDialogId());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    //chka
//    @Override
//    public void notifyChangedUserStatus(int userId, boolean online) {
//        super.notifyChangedUserStatus(userId, online);
//
//        if (opponentUser != null && opponentUser.getUserId() == userId) {
//            setOnlineStatus(opponentUser);
//        }
//    }
//
    //chka
//    private void setOnlineStatus(User user) {
//        if (user != null) {
//            if (friendListHelper != null) {
//                String offlineStatus = getString(R.string.last_seen, DateUtils.toTodayYesterdayShortDateWithoutYear2(user.getLastLogin()),
//                        DateUtils.formatDateSimpleTime(user.getLastLogin()));
//                setActionBarSubtitle(
//                        OnlineStatusUtils.getOnlineStatus(this, friendListHelper.isUserOnline(user.getUserId()), offlineStatus));
//            }
//        }
//    }
//

    //chka Hayk+
    private void callToUser(List<DialogOccupant> dialogOccupants, QBRTCTypes.QBConferenceType qbConferenceType) {//Avetik
        if (!isChatInitializedAndUserLoggedIn()) {
            ToastUtils.longToast(R.string.call_chat_service_is_initializing);
            return;
        }
        //last
        List<QBUser> qbUserList = new ArrayList<>();
        for(DialogOccupant dialogOccupant : dialogOccupants) {
            qbUserList.add(UserFriendUtils.createQbUser(dialogOccupant.getUser()));
        }

        //qm
        Integer myId=AppSession.getSession().getUser().getId();

        List<QMUser> qmUserList = new ArrayList<>();
        for(DialogOccupant dialogOccupant : dialogOccupants) {
            if (!dialogOccupant.getUser().getId().equals( myId)) {
                qmUserList.add(dialogOccupant.getUser());
            }

        }

//        qmUserList.remove(AppSession.getSession().getUser());
        CallActivity.start(GroupDialogActivity.this, qbUserList, qbConferenceType, null,currentChatDialog.getType(),qmUserList);
    }

//    private void callToUser(List<QBUser> qbUserList, QBRTCTypes.QBConferenceType qbConferenceType) {
//        if (!isChatInitializedAndUserLoggedIn()) {
//            ToastUtils.longToast(R.string.call_chat_service_is_initializing);
//            return;
//        }
//        CallActivity.start(GroupDialogActivity.this, qbUserList, qbConferenceType, null);
//    }

    @Override
    protected void updateActionBar() {
        if (isNetworkAvailable() && currentChatDialog != null) {
            setActionBarTitle(ChatDialogUtils.getTitleForChatDialog(currentChatDialog, dataManager));
            checkActionBarLogo(currentChatDialog.getPhoto(), R.drawable.placeholder_group);
        }
    }

    @Override
    protected void initFields() {
        super.initFields();

        if (currentChatDialog != null) {
            title = ChatDialogUtils.getTitleForChatDialog(currentChatDialog, dataManager);
        }
    }

    //chka
    private void processCombinationMessages(){
        QBUser currentUser = AppSession.getSession().getUser();
        for (CombinationMessage cm :combinationMessagesList){
            boolean ownMessage = !cm.isIncoming(currentUser.getId());
            if (!State.READ.equals(cm.getState()) && !ownMessage && isNetworkAvailable()) {
                cm.setState(State.READ);
                QBUpdateStatusMessageCommand.start(this,currentChatDialog,cm,false);
            } else if (ownMessage) {
//                cm.setState(State.READ); Avetik
                dataManager.getMessageDataManager().update(cm.toMessage(), false);
            }
        }
    }

    public void sendMessage(View view) {
        sendMessage(false);
    }

}