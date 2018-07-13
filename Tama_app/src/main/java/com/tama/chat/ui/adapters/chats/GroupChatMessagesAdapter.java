package com.tama.chat.ui.adapters.chats;

import static com.tama.chat.utils.DateUtils.SECOND_IN_MILLIS;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBMessageStatusesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.exception.QBResponseException;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.views.recyclerview.WrapContentLinearLayoutManager;
import com.tama.chat.utils.ColorUtils;
import com.tama.chat.utils.DateUtils;
import com.tama.q_municate_core.models.CombinationMessage;
import com.tama.q_municate_core.qb.commands.chat.QBUpdateStatusMessageCommand;
import com.tama.q_municate_core.utils.DbUtils;
import com.tama.q_municate_db.managers.DataManager;
import com.tama.q_municate_db.models.Attachment.Type;
import com.tama.q_municate_db.models.State;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GroupChatMessagesAdapter extends BaseChatMessagesAdapter {
    private static final String TAG = GroupChatMessagesAdapter.class.getSimpleName();
    private static final String SNAP = "SNAP";
    private ColorUtils colorUtils;
    protected DataManager dataManager;
    private QBMessageStatusesManager messageStatusesManager;
    private QBMessageStatusListener messageStatusListener;
    private WrapContentLinearLayoutManager wrapContentLinearLayoutManager;

    public static Map<String,LeftTimerAsyncTask> leftMapTimerTask = new ArrayMap<>();
    public static Map<String,RightTimerAsyncTask> rightMapTimerTask = new ArrayMap<>();
//    public static Map<String,Integer> seenCountMap = new ArrayMap<>();
    public  int usersCount;
    public  int seenUsersCount=0;
    private boolean isSeenAll=false;

    public GroupChatMessagesAdapter(BaseActivity baseActivity, QBChatDialog chatDialog,
                                    List<CombinationMessage> chatMessages) {
        super(baseActivity, chatDialog, chatMessages);
        this.dataManager = DataManager.getInstance();
        this.wrapContentLinearLayoutManager=new WrapContentLinearLayoutManager(context);
        this.messageStatusesManager = QBChatService.getInstance().getMessageStatusesManager();
        usersCount=chatDialog.getOccupants().size()-1;
        colorUtils = new ColorUtils();
    }

    @Override
    protected void onBindViewCustomHolder(QBMessageViewHolder holder, CombinationMessage chatMessage, int position) {
        RequestsViewHolder viewHolder = (RequestsViewHolder) holder;
        boolean notificationMessage = chatMessage.getNotificationType() != null;

        if (notificationMessage) {
            viewHolder.messageTextView.setText(chatMessage.getBody());
            viewHolder.timeTextMessageTextView.setText(getDate(chatMessage.getCreatedDate()));
        } else {
            Log.d(TAG, "onBindViewCustomHolder else");
        }

        if (!State.READ.equals(chatMessage.getState()) && isIncoming(chatMessage) && baseActivity.isNetworkAvailable()) {
//            updateMessageState(chatMessage, chatDialog);
            if (baseActivity.isNetworkAvailable()) {
                chatMessage.setState(State.READ);
                QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, false);
            }        }
    }

    @Override
    protected QBMessageViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_REQUEST_MESSAGE ? new RequestsViewHolder(inflater.inflate(R.layout.item_notification_message, parent, false)) : null;
    }

    ///gnacox msg
    @Override
    protected void onBindViewMsgRightHolder(TextMessageHolder holder,
        final CombinationMessage chatMessage,int position) {

        setViewVisibility(holder.avatar, View.VISIBLE);

        ImageView messageStatusView = (ImageView) holder.itemView.findViewById(R.id.message_status_image_view);
        TextView msgTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
        TextView timeTextMessageTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_time_message);
        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
        timeTextMessageTextView.setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate() * SECOND_IN_MILLIS));


        fillTextMessageHolder(holder,chatMessage,position,false);

//        if (chatMessage.getSnapMessage() && !seenCountMap.containsKey(chatMessage.getMessageId())) {
//            seenCountMap.put(chatMessage.getMessageId(), seenUsersCount);
//        }

        if (chatMessage.getState() != null) {

//            if (State.READ.equals(chatMessage.getState()) && chatMessage.getSnapMessage()){
//                seenCountMap.put(chatMessage.getMessageId(), seenCountMap.get(chatMessage.getMessageId())+1);
//            }

//            if(seenCountMap.get(chatMessage.getMessageId())==usersCount){
//                isSeenAll=true;
//                seenCountMap.remove(chatMessage.getMessageId());
//            }else {
//                isSeenAll=false;
//            }



//            if (State.READ.equals(chatMessage.getState()) && chatMessage.getSnapMessage() && seenCountMap.containsKey(chatMessage.getMessageId())) {
//                    seenCountMap.put(chatMessage.getMessageId(),seenCountMap.get(chatMessage.getMessageId()) + 1);
//                }


//            setMessageStatus(messageStatusView, State.DELIVERED.equals(
//                chatMessage.getState()), State.READ.equals(chatMessage.getState()));


            //uxarkac
            if(chatMessage.getSnapMessage() && State.READ.equals(chatMessage.getState())){
//                seenCountMap.remove(chatMessage.getMessageId());
                timerTextView.setVisibility(View.VISIBLE);
                if(rightMapTimerTask!=null) {
                    RightTimerAsyncTask timerAsyncTask = rightMapTimerTask.get(chatMessage.getMessageId());
                    if(timerAsyncTask!=null) {
                        timerAsyncTask.setTimerTextView(timerTextView);
                        msgTextView.setText(chatMessage.getBody());
                    }else{
                        RightTimerAsyncTask timerTask  = new RightTimerAsyncTask(chatMessage, (RightTextMessageViewHolder) holder,position, true);
                        AsyncTaskCompat.executeParallel(timerTask);
                        rightMapTimerTask.put(chatMessage.getMessageId(), timerTask);
                        timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));
                        msgTextView.setText(chatMessage.getBody());
                    }
                }
            } else{
                msgTextView.setText(chatMessage.getBody());
                timerTextView.setVisibility(View.GONE);
            }

        } else {
            timerTextView.setVisibility(View.GONE);
            messageStatusView.setImageResource(android.R.color.transparent);
        }


//        chatMessage.setState(State.DELIVERED);
//
//
//        Log.d("mytest", "onBindViewMsgRightHolder ============> position: "+position);
//        Log.d("mytest", "onBindViewMsgRightHolder ============> chatMessage.getBody(): "+chatMessage.getBody()+" , chatMessage.getMessageId(): "+chatMessage.getMessageId()+" , chatMessage.getId(): "+chatMessage.getId());
//        Log.d("mytest", "onBindViewMsgRightHolder ============> holder.getItemId(): "+holder.getItemId()+" , holder.itemView.getId(): "+holder.itemView.getId());

//        Log.d("mytest", " ============> : "+chatDialog);
//        Log.d("mytest", " getQBToken "+ SharedHelper.getInstance().getQBToken());
//        Log.d("mytest", " getFirebaseToken "+ SharedHelper.getInstance().getFirebaseToken());


//        ImageView messageStatusView = (ImageView) holder.itemView.findViewById(R.id.message_status_image_view);
//        TextView msgTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
//        TextView timeTextMessageTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_time_message);
//        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
////        setViewVisibility(holder.avatar, View.GONE);
//        timeTextMessageTextView.setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate() * SECOND_IN_MILLIS));
//
////        Log.d("testt", "chatMessage.getRecipientId() : : " +chatMessage.getRecipientId());
//
//
//
//        if (chatMessage.getState() != null) {
//
//            if (State.READ.equals(chatMessage.getState()) && chatMessage.getSnapMessage()) {
////                if (baseActivity.isConnected()) {
////                    chatMessage.setState(State.SYNC);
////                    QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, false);
////                }
//                if (seenCountMap.containsKey(chatMessage.getMessageId())) {
//                    seenCountMap.put(chatMessage.getMessageId(),seenCountMap.get(chatMessage.getMessageId()) + 1);
//                    if(seenCountMap.get(chatMessage.getMessageId())==usersCount){
//                        isSeenAll=true;
//
//                    }else {
//                        isSeenAll=false;
//                    }
//                }
//                else {
//                    seenCountMap.put(chatMessage.getMessageId(), seenUsersCount+1);
//                }
//            }
//
////            ArrayList<Integer> readUsers= (ArrayList<Integer>) chatMessage.getReadIds();
//
//            //uxarkac
//            if(isSeenAll ){
////                seenUsersCount++;
////                chatMessage.setState(State.SYNC);
////                if (seenUsersCount==usersCount) {
//                    setMessageStatus(messageStatusView, State.DELIVERED.equals(chatMessage.getState()), State.READ.equals(chatMessage.getState()));
//                    timerTextView.setVisibility(View.VISIBLE);
//                    if(rightMapTimerTask!=null) {
//                        RightTimerAsyncTask timerAsyncTask = rightMapTimerTask.get(chatMessage.getMessageId());
//                        if(timerAsyncTask!=null) {
//                            timerAsyncTask.setTimerTextView(timerTextView);
//                            msgTextView.setText(chatMessage.getBody());
//                        }else{
//                            RightTimerAsyncTask timerTask  = new RightTimerAsyncTask(chatMessage, (RightTextMessageViewHolder) holder,position, true);
//                            AsyncTaskCompat.executeParallel(timerTask);
//                            rightMapTimerTask.put(chatMessage.getMessageId(), timerTask);
//                            timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));
//                            msgTextView.setText(chatMessage.getBody());
//                        }
//                    }
////                }else{
////                    msgTextView.setText(chatMessage.getBody());
////                    timerTextView.setVisibility(View.GONE);
////                }
//            } else{
//                msgTextView.setText(chatMessage.getBody());
//                timerTextView.setVisibility(View.GONE);
//            }
//
//        } else {
//            timerTextView.setVisibility(View.GONE);
//            msgTextView.setText(chatMessage.getBody());
//            messageStatusView.setImageResource(android.R.color.transparent);
//        }
//
//
////        QBRestChatService.markMessagesAsRead(chatDialog.getDialogId(), null).performAsync(new QBEntityCallback<Void>() {
////            @Override
////            public void onSuccess(Void aVoid, Bundle bundle) {
////
////            }
////
////            @Override
////            public void onError(QBResponseException e) {
////
////            }
////        });
//
////        chatMessage.setState(State.SYNC);
//        super.onBindViewMsgRightHolder(holder, chatMessage, position);
    }


    //ekac msg
    @Override
    protected void onBindViewMsgLeftHolder(TextMessageHolder holder, CombinationMessage chatMessage, int position) {
//        Log.d("mytest", "onBindViewMsgLeftHolder <============ position: "+position);
//        Log.d("mytest", "onBindViewMsgLeftHolder <============ chatMessage.getBody(): "+chatMessage.getBody()+" , chatMessage.getMessageId(): "+chatMessage.getMessageId()+" , chatMessage.getId(): "+chatMessage.getId());
//        Log.d("mytest", "onBindViewMsgLeftHolder <============ holder.getItemId(): "+holder.getItemId()+" , holder.itemView.getId(): "+holder.itemView.getId());
        setViewVisibility(holder.avatar, View.VISIBLE);


        //uxarkaciny       ekac
        fillTextMessageHolder(holder,chatMessage,position,true);
//        super.onBindViewMsgLeftHolder(holder, chatMessage, position);
        TextView msgTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
        TextView timeTextMessageTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_time_message);
        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);

        timerTextView.setVisibility(View.GONE);
        timeTextMessageTextView.setVisibility(View.GONE);


        TextView opponentNameTextView = (TextView) holder.itemView.findViewById(R.id.opponent_name_text_view);
        TextView customMessageTimeTextView = (TextView) holder.itemView.findViewById(R.id.custom_msg_text_time_message);
        opponentNameTextView.setVisibility(View.VISIBLE);
        customMessageTimeTextView.setVisibility(View.VISIBLE);
        String senderName;
        senderName = chatMessage.getDialogOccupant().getUser().getFullName();
        opponentNameTextView.setTextColor(colorUtils.getRandomTextColorById(chatMessage.getDialogOccupant().getUser().getId()));
        opponentNameTextView.setText(senderName);
        customMessageTimeTextView.setText(getDate(chatMessage.getDateSent()));


        if (!chatMessage.getSnapMessage()) {
//            holder.itemView.setEnabled(false);

            timerTextView.setVisibility(View.GONE);
//            updateMessageState(chatMessage, chatDialog);
            if (baseActivity.isNetworkAvailable()) {
                chatMessage.setState(State.READ);
                QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, false);
            }
            msgTextView.setText(chatMessage.getBody());
            return;
        }

        if (chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()) {
            timerTextView.setVisibility(View.GONE);
            msgTextView.setText(SNAP);

            if(!timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){
                msgTextView.setOnTouchListener(
                    onSnapMsgTouchListener(chatMessage, (LeftTextMessageViewHolder) holder, position));
            }

        } else if (chatMessage.getSnapMessage() && chatMessage.getIsSnapInProgress()) {

            if(timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){

                if (leftMapTimerTask != null && !leftMapTimerTask.isEmpty()) {
                    LeftTimerAsyncTask leftTimerAsyncTask = leftMapTimerTask.get(chatMessage.getMessageId());
                    if (leftTimerAsyncTask != null) {
                        timerTextView.setVisibility(View.VISIBLE);
                        timerTextView.setText("");
                        leftTimerAsyncTask.setTimerTextView(timerTextView);
                    }
                    msgTextView.setText(chatMessage.getBody());
                }

            }else {
                msgTextView.setText(chatMessage.getBody());

                chatMessage.setSnapMessageTime(0);
                new LeftTimerAsyncTask(chatMessage, (LeftTextMessageViewHolder) holder,position,false).execute();
            }


        }

////        holder.timeTextMessageTextView.setVisibility(View.GONE);
//
//        TextView msgTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
//        TextView timeTextMessageTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_time_message);
//        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
//
//        timerTextView.setVisibility(View.GONE);
//        timeTextMessageTextView.setVisibility(View.GONE);
//
//        TextView opponentNameTextView = (TextView) holder.itemView.findViewById(R.id.opponent_name_text_view);
//        TextView customMessageTimeTextView = (TextView) holder.itemView.findViewById(R.id.custom_msg_text_time_message);
//
//        opponentNameTextView.setVisibility(View.VISIBLE);
//        customMessageTimeTextView.setVisibility(View.VISIBLE);
//
//        String senderName;
//        senderName = chatMessage.getDialogOccupant().getUser().getFullName();
//
////        TextView opponentNameTextView = (TextView) holder.itemView.findViewById(R.id.opponent_name_text_view);
//        opponentNameTextView.setTextColor(colorUtils.getRandomTextColorById(chatMessage.getDialogOccupant().getUser().getId()));
//        opponentNameTextView.setText(senderName);
//
////        TextView customMessageTimeTextView = (TextView) holder.itemView.findViewById(R.id.custom_msg_text_time_message);
//        customMessageTimeTextView.setText(getDate(chatMessage.getDateSent()));
//
//        updateMessageState(chatMessage, chatDialog);
//
//        View customViewTopLeft = holder.itemView.findViewById(R.id.custom_view_top_left);
////        ViewGroup.LayoutParams layoutParams = customViewTopLeft.getLayoutParams();
//
////        final List<String> urlsList = LinkUtils.extractUrls(chatMessage.getBody());
////        if (!urlsList.isEmpty()) {
////            layoutParams.width = (int) context.getResources().getDimension(com.quickblox.ui.kit.chatmessage.adapter.R.dimen.link_preview_width);
////        } else {
////            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
////        }
//
////        customViewTopLeft.setLayoutParams(layoutParams);
//
//

    }

    //uxarkac nkar
    @Override
    protected void onBindViewAttachRightHolder(ImageAttachHolder holder,CombinationMessage chatMessage, int position) {
        setViewVisibility(holder.avatar, View.VISIBLE);

//        setViewVisibility(holder.itemView.findViewById(R.id.msg_image_avatar), View.VISIBLE);
//        final boolean seenAll=false;
//
//// call it after chat login
//        messageStatusListener = new QBMessageStatusListener() {
//            @Override
//            public void processMessageDelivered(String messageId, String dialogId, Integer userId) {
//
//            }
//
//            @Override
//            public void processMessageRead(String messageId, String dialogId, Integer userId) {
//
//                List<Integer> occupants=chatDialog.getOccupants();
//
//                for(int c :occupants){
//                    if(currentUser.getId()==c){
//                        seenAll
//                    }
//                }
//            }
//        };
//
//        messageStatusesManager.addMessageStatusListener(messageStatusListener);

        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);

        if(!chatMessage.getSnapMessage()){
            timerTextView.setVisibility(View.GONE);
        }

        if(!State.READ.equals(chatMessage.getState())){
            timerTextView.setVisibility(View.GONE);
        }

        if(chatMessage.getSnapMessage() && State.READ.equals(chatMessage.getState())){
            timerTextView.setVisibility(View.VISIBLE);
            if(rightMapTimerTask!=null) {
                RightTimerAsyncTask timerAsyncTask = rightMapTimerTask.get(chatMessage.getMessageId());
                if(timerAsyncTask!=null) {
                    timerAsyncTask.setTimerTextView(timerTextView);
                }else{
                    RightTimerAsyncTask timerTask  = new RightTimerAsyncTask(chatMessage, holder,position, true);
                    AsyncTaskCompat.executeParallel(timerTask);
                    rightMapTimerTask.put(chatMessage.getMessageId(), timerTask);
                    timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));
                }
            }
        } else {
            timerTextView.setVisibility(View.GONE);
        }


//        showSendStatusView(holder, chatMessage);
        super.onBindViewAttachRightHolder(holder, chatMessage, position);

    }

    /////////////ekac nkar
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onBindViewAttachLeftHolder(ImageAttachHolder holder,CombinationMessage chatMessage, int position) {
        super.onBindViewAttachLeftHolder(holder, chatMessage, position);
        setViewVisibility(holder.avatar, View.VISIBLE);

        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);
        TextView snapAttachImageView = (TextView) holder.itemView.findViewById(R.id.snap_image_view);

        if (!chatMessage.getSnapMessage()) {
//            holder.itemView.setEnabled(false);

            timerTextView.setVisibility(View.GONE);
            snapAttachImageView.setVisibility(View.GONE);
//            updateMessageState(chatMessage, chatDialog);
            if (baseActivity.isNetworkAvailable()) {
                chatMessage.setState(State.READ);
                QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, false);
            }
            return;
        }

        if(chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()){
            Log.d(TAG,"stega mtel  1");
            timerTextView.setVisibility(View.GONE);
            snapAttachImageView.setVisibility(View.VISIBLE);
            snapAttachImageView.setText(SNAP);
//      timerTextView.setVisibility(View.VISIBLE);
//      timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));

            if(!timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){
                snapAttachImageView.setOnTouchListener(onSnapImageTouchListener(chatMessage, holder, position));
            }

        }else if(chatMessage.getSnapMessage() && chatMessage.getIsSnapInProgress()) {

            if (timerTaskExistByMessageId(leftMapTimerTask, chatMessage.getMessageId())) {
                if (leftMapTimerTask != null && !leftMapTimerTask.isEmpty()) {
                    LeftTimerAsyncTask timerAsyncTask = leftMapTimerTask.get(chatMessage.getMessageId());
                    if (timerAsyncTask != null) {
                        snapAttachImageView.setVisibility(View.GONE);
                        timerTextView.setVisibility(View.VISIBLE);
                        timerTextView.setText("");
                        timerAsyncTask.setTimerTextView(timerTextView);
                    }
                }

            }else {
                chatMessage.setSnapMessageTime(0);
                new LeftTimerAsyncTask(chatMessage,holder,position,false).execute();
            }
        }
//      isMessageShowing = true;
//      snapAttachImageView.setVisibility(View.GONE);
//      timerTextView.setVisibility(View.GONE);



//    setViewVisibility(viewHolder.progressRelativeLayout, View.VISIBLE);
//    viewHolder.timeAttachMessageTextView.setText(DateUtils.formatDateSimpleTime(combinationMessage.getCreatedDate()));
//    UrlType urlType = combinationMessage.getBody().equals("Attachment video") ? UrlType.VIDEO : UrlType.IMAGE;



//    displayAttachImageById(combinationMessage.getAttachment().getAttachmentId(), urlType, viewHolder,combinationMessage.getAttachment().getName());//Avetik.

    }


    ///uxarkac video
    @Override
    protected void onBindViewAttachRightVideoHolder(VideoAttachHolder holder,
        CombinationMessage chatMessage, int position) {
        setViewVisibility(holder.avatar, View.VISIBLE);

//        setViewVisibility(holder.itemView.findViewById(R.id.msg_image_avatar), View.VISIBLE);
        super.onBindViewAttachRightVideoHolder(holder, chatMessage, position);

        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);

        if(!chatMessage.getSnapMessage()){
            timerTextView.setVisibility(View.GONE);
        }

        if(!State.READ.equals(chatMessage.getState())){
            timerTextView.setVisibility(View.GONE);
        }

        if(chatMessage.getSnapMessage() && State.READ.equals(chatMessage.getState())){
            timerTextView.setVisibility(View.VISIBLE);
            if(rightMapTimerTask!=null) {
                RightTimerAsyncTask timerAsyncTask = rightMapTimerTask.get(chatMessage.getMessageId());
                if(timerAsyncTask!=null) {
                    timerAsyncTask.setTimerTextView(timerTextView);
                }else{
                    chatMessage.setSnapMessageTime(
                        (int) Math.max(chatMessage.getAttachment().getDuration(),chatMessage.getSnapMessageTime()));
                    RightTimerAsyncTask timerTask  = new RightTimerAsyncTask(chatMessage, holder,position, true);
                    AsyncTaskCompat.executeParallel(timerTask);
                    rightMapTimerTask.put(chatMessage.getMessageId(), timerTask);
                    timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));
                }
            }
        } else {
            timerTextView.setVisibility(View.GONE);
        }


//        showSendStatusView(holder, chatMessage);

    }


    ////ekac video
    @Override
    protected void onBindViewAttachLeftVideoHolder(VideoAttachHolder holder,
        CombinationMessage chatMessage, int position) {
        setViewVisibility(holder.avatar, View.VISIBLE);
//        setViewVisibility(holder.itemView.findViewById(R.id.msg_image_avatar), View.VISIBLE);
        super.onBindViewAttachLeftVideoHolder(holder, chatMessage, position);

        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);
        TextView snapAttachImageView = (TextView) holder.itemView.findViewById(R.id.snap_image_view);

        if (!chatMessage.getSnapMessage()) {
//            holder.itemView.setEnabled(false);

            timerTextView.setVisibility(View.GONE);
            snapAttachImageView.setVisibility(View.GONE);
//            updateMessageState(chatMessage, chatDialog);
            if (baseActivity.isNetworkAvailable()) {
                chatMessage.setState(State.READ);
                QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, false);
            }
        }

        if(chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()){
            Log.d(TAG,"stega mtel  1");
            timerTextView.setVisibility(View.GONE);
            snapAttachImageView.setVisibility(View.VISIBLE);
            snapAttachImageView.setText(SNAP);
//      timerTextView.setVisibility(View.VISIBLE);
//      timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));

            if(!timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){
                chatMessage.setSnapMessageTime(
                    (int) Math.max(chatMessage.getAttachment().getDuration(),chatMessage.getSnapMessageTime()));
                snapAttachImageView.setOnTouchListener(onSnapVideoTouchListener(chatMessage, holder, position));
            }

        }else if(chatMessage.getSnapMessage() && chatMessage.getIsSnapInProgress()) {

            if (timerTaskExistByMessageId(leftMapTimerTask, chatMessage.getMessageId())) {
                if (leftMapTimerTask != null && !leftMapTimerTask.isEmpty()) {
                    LeftTimerAsyncTask timerAsyncTask = leftMapTimerTask.get(chatMessage.getMessageId());
                    if (timerAsyncTask != null) {
                        snapAttachImageView.setVisibility(View.GONE);
                        timerTextView.setVisibility(View.VISIBLE);
                        timerTextView.setText("");
                        timerAsyncTask.setTimerTextView(timerTextView);
                    }
                }

            }else {
                chatMessage.setSnapMessageTime(0);
                new LeftTimerAsyncTask(chatMessage,holder,position,false).execute();
            }
        }



//    updateMessageState(chatMessage, chatDialog);
    }

    ///uxarkac audio
    @Override
    protected void onBindViewAttachRightAudioHolder(AudioAttachHolder holder,
        CombinationMessage chatMessage, int position) {
        setViewVisibility(holder.avatar, View.VISIBLE);

//        setViewVisibility(holder.itemView.findViewById(R.id.msg_image_avatar), View.VISIBLE);
        super.onBindViewAttachRightAudioHolder(holder, chatMessage, position);

        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);

        if(!chatMessage.getSnapMessage()){
            timerTextView.setVisibility(View.GONE);
        }

        if(!State.READ.equals(chatMessage.getState())){
            timerTextView.setVisibility(View.GONE);
        }

        if(chatMessage.getSnapMessage() && State.READ.equals(chatMessage.getState())){
            timerTextView.setVisibility(View.VISIBLE);
            if(rightMapTimerTask!=null) {
                RightTimerAsyncTask timerAsyncTask = rightMapTimerTask.get(chatMessage.getMessageId());
                if(timerAsyncTask!=null) {
                    timerAsyncTask.setTimerTextView(timerTextView);
                }else{
                    chatMessage.setSnapMessageTime(
                        (int) Math.max(chatMessage.getAttachment().getDuration(),chatMessage.getSnapMessageTime()));
                    RightTimerAsyncTask timerTask  = new RightTimerAsyncTask(chatMessage, holder,position, true);
                    AsyncTaskCompat.executeParallel(timerTask);
                    rightMapTimerTask.put(chatMessage.getMessageId(), timerTask);
                    timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));
                }
            }
        } else {
            timerTextView.setVisibility(View.GONE);
        }

//        showSendStatusView(holder, chatMessage);
    }

    //ekac audio
    @Override
    protected void onBindViewAttachLeftAudioHolder(AudioAttachHolder holder,
        CombinationMessage chatMessage, int position) {
        setViewVisibility(holder.avatar, View.VISIBLE);
//        setViewVisibility(holder.itemView.findViewById(R.id.msg_image_avatar), View.VISIBLE);
        super.onBindViewAttachLeftAudioHolder(holder, chatMessage, position);

        TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);
        TextView snapAttachImageView = (TextView) holder.itemView.findViewById(R.id.snap_image_view);

        if (!chatMessage.getSnapMessage()) {
//            holder.itemView.setEnabled(false);

            timerTextView.setVisibility(View.GONE);
            snapAttachImageView.setVisibility(View.GONE);
//            updateMessageState(chatMessage, chatDialog);
            if (baseActivity.isNetworkAvailable()) {
                chatMessage.setState(State.READ);
                QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, false);
            }
        }

        if(chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()){
            Log.d(TAG,"stega mtel  1");
            timerTextView.setVisibility(View.GONE);
            snapAttachImageView.setVisibility(View.VISIBLE);
            snapAttachImageView.setText(SNAP);
//      timerTextView.setVisibility(View.VISIBLE);
//      timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));

            if(!timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){
                chatMessage.setSnapMessageTime(
                    (int) Math.max(chatMessage.getAttachment().getDuration(),chatMessage.getSnapMessageTime()));
                snapAttachImageView.setOnTouchListener(onSnapAudioTouchListener(chatMessage, holder, position));
            }

        }else if(chatMessage.getSnapMessage() && chatMessage.getIsSnapInProgress()) {

            if (timerTaskExistByMessageId(leftMapTimerTask, chatMessage.getMessageId())) {
                if (leftMapTimerTask != null && !leftMapTimerTask.isEmpty()) {
                    LeftTimerAsyncTask timerAsyncTask = leftMapTimerTask.get(chatMessage.getMessageId());
                    if (timerAsyncTask != null) {
                        snapAttachImageView.setVisibility(View.GONE);
                        timerTextView.setVisibility(View.VISIBLE);
                        timerTextView.setText("");
                        timerAsyncTask.setTimerTextView(timerTextView);
                    }
                }

            }else {
                chatMessage.setSnapMessageTime(0);
                new LeftTimerAsyncTask(chatMessage,holder,position,false).execute();
            }
        }




//    updateMessageState(chatMessage, chatDialog);
    }


    private void showSendStatusView(BaseAttachHolder holder, CombinationMessage chatMessage) {
        ImageView signAttachView = holder.signAttachView;
        if (chatMessage.getState() != null) {
            setMessageStatus(signAttachView, State.DELIVERED.equals(
                chatMessage.getState()), State.READ.equals(chatMessage.getState()));
        } else {
            signAttachView.setImageResource(android.R.color.transparent);
        }
    }

    private boolean timerTaskExistByMessageId(Map<String, ? extends AsyncTask> asyncTaskMap,String id){

        return asyncTaskMap.containsKey(id);

    }

    private View.OnTouchListener onSnapMsgTouchListener( final CombinationMessage chatMessage,final LeftTextMessageViewHolder holder, final int position) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        Log.d("testt", "onTouch =============================");
//            v.setEnabled(false);
                        TextView timerText = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);

                        if (!timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){
                            LeftTimerAsyncTask leftTimerAsyncTask = new LeftTimerAsyncTask(chatMessage, holder, position, false);
                            leftMapTimerTask.put(chatMessage.getMessageId(), leftTimerAsyncTask);

                            if (leftTimerAsyncTask.getStatus() != Status.RUNNING || leftTimerAsyncTask.getStatus() != Status.FINISHED ) {
                                timerText.setVisibility(View.VISIBLE);
                                timerText.setText(String.valueOf(chatMessage.getSnapMessageTime()));
                                AsyncTaskCompat.executeParallel(leftTimerAsyncTask);
                            }
                        }


                        break;
                    case MotionEvent.ACTION_MOVE: // движение

                        break;
                    case MotionEvent.ACTION_UP: // отпускание

                        break;
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return true;

            }
        };

//    return new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Log.d("testt","onClick =============================");
//        v.setEnabled(false);
//        AsyncTaskCompat.executeParallel(timerTask);
//      }
//    };
    }

    private View.OnTouchListener onSnapImageTouchListener(final CombinationMessage chatMessage,final ImageAttachHolder holder, final int position) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        Log.d("testt", "onTouch =============================");
//                        v.setEnabled(false);
                        TextView timerText = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);
                        TextView snapAttachImageView = (TextView) holder.itemView.findViewById(R.id.snap_image_view);


                        if (!timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){
                            LeftTimerAsyncTask leftTimerAsyncTask = new LeftTimerAsyncTask(chatMessage, holder, position, false);
                            leftMapTimerTask.put(chatMessage.getMessageId(), leftTimerAsyncTask);

                            if (leftTimerAsyncTask.getStatus() != Status.RUNNING || leftTimerAsyncTask.getStatus() != Status.FINISHED ) {
                                snapAttachImageView.setVisibility(View.GONE);
                                timerText.setVisibility(View.VISIBLE);
                                timerText.setText(String.valueOf(chatMessage.getSnapMessageTime()));
                                AsyncTaskCompat.executeParallel(leftTimerAsyncTask);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_MOVE: // движение

                        break;
                    case MotionEvent.ACTION_UP: // отпускание

                        break;
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return true;

            }
        };
    }


    private View.OnTouchListener onSnapVideoTouchListener(final CombinationMessage chatMessage,final VideoAttachHolder holder, final int position) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        Log.d("testt", "onTouch =============================");
//                        v.setEnabled(false);
                        TextView timerText = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);
                        TextView snapAttachImageView = (TextView) holder.itemView.findViewById(R.id.snap_image_view);


                        if (!timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){
                            LeftTimerAsyncTask leftTimerAsyncTask = new LeftTimerAsyncTask(chatMessage, holder, position, false);
                            leftMapTimerTask.put(chatMessage.getMessageId(), leftTimerAsyncTask);

                            if (leftTimerAsyncTask.getStatus() != Status.RUNNING || leftTimerAsyncTask.getStatus() != Status.FINISHED ) {
                                snapAttachImageView.setVisibility(View.GONE);
                                timerText.setVisibility(View.VISIBLE);
                                timerText.setText(String.valueOf(chatMessage.getSnapMessageTime()));
                                AsyncTaskCompat.executeParallel(leftTimerAsyncTask);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_MOVE: // движение

                        break;
                    case MotionEvent.ACTION_UP: // отпускание

                        break;
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return true;

            }
        };
    }


    private View.OnTouchListener onSnapAudioTouchListener(final CombinationMessage chatMessage,final AudioAttachHolder holder, final int position) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        Log.d("testt", "onTouch =============================");
//                        v.setEnabled(false);
                        TextView timerText = (TextView) holder.itemView
                            .findViewById(R.id.message_timer_textview_attach);
                        TextView snapAttachImageView = (TextView) holder.itemView
                            .findViewById(R.id.snap_image_view);

                        if (!timerTaskExistByMessageId(leftMapTimerTask,
                            chatMessage.getMessageId())) {
                            LeftTimerAsyncTask leftTimerAsyncTask = new LeftTimerAsyncTask(
                                chatMessage, holder, position, false);
                            leftMapTimerTask.put(chatMessage.getMessageId(), leftTimerAsyncTask);

                            if (leftTimerAsyncTask.getStatus() != Status.RUNNING
                                || leftTimerAsyncTask.getStatus() != Status.FINISHED) {
                                snapAttachImageView.setVisibility(View.GONE);
                                timerText.setVisibility(View.VISIBLE);
                                timerText.setText(String.valueOf(chatMessage.getSnapMessageTime()));
                                AsyncTaskCompat.executeParallel(leftTimerAsyncTask);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_MOVE: // движение

                        break;
                    case MotionEvent.ACTION_UP: // отпускание

                        break;
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return true;

            }
        };
    }


    protected void setMessageStatus(ImageView imageView, boolean messageDelivered,
        boolean messageRead) {
        imageView.setImageResource(getMessageStatusIconId(messageDelivered, messageRead));

    }

    protected int getMessageStatusIconId(boolean isDelivered, boolean isRead) {
        int iconResourceId = 0;

        if (isRead) {
            iconResourceId = R.drawable.ic_status_mes_sent_received;
        } else if (isDelivered) {
            iconResourceId = R.drawable.ic_status_mes_sent;
        }

        return iconResourceId;
    }



    ///////////tasks

    public class LeftTimerAsyncTask extends AsyncTask<Void, Integer, Void> implements Serializable {

        private static final String TAG = "myLogs";
        private int time;
        private CombinationMessage chatMessage;
        private LeftTextMessageViewHolder holder;
        private ImageAttachHolder imageHolder;
        private VideoAttachHolder videoHolder;
        private AudioAttachHolder audioHolder;
        private int position;
        private boolean isOwnMessage;
        private TextView timerText;
        private TextView snapAttachImageView;
        private TextView messageText;


        public LeftTimerAsyncTask(CombinationMessage chatMessage, LeftTextMessageViewHolder holder, int position,
            boolean isOwnMessage) {
            super();
            this.chatMessage = chatMessage;
            this.holder = holder;
            this.position = position;
            this.isOwnMessage = isOwnMessage;
            time = (int) chatMessage.getSnapMessageTime();
            timerText = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
            messageText = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
        }

        public LeftTimerAsyncTask(CombinationMessage chatMessage, ImageAttachHolder holder,
            int position, boolean isOwnMessage) {
            this.chatMessage = chatMessage;
            this.imageHolder = holder;
            this.position = position;
            this.isOwnMessage = isOwnMessage;
            time = (int) chatMessage.getSnapMessageTime();
            timerText = (TextView) imageHolder.itemView.findViewById(R.id.message_timer_textview_attach);
            snapAttachImageView = (TextView) imageHolder.itemView.findViewById(R.id.snap_image_view);
        }

        public LeftTimerAsyncTask(CombinationMessage chatMessage, VideoAttachHolder holder,
            int position, boolean b) {
            this.chatMessage = chatMessage;
            this.videoHolder = holder;
            this.position = position;
            this.isOwnMessage = isOwnMessage;
            time = (int) chatMessage.getSnapMessageTime();
            timerText = (TextView) videoHolder.itemView.findViewById(R.id.message_timer_textview_attach);
            snapAttachImageView = (TextView) videoHolder.itemView.findViewById(R.id.snap_image_view);
        }

        public LeftTimerAsyncTask(CombinationMessage chatMessage, AudioAttachHolder holder,
            int position, boolean b) {
            this.chatMessage = chatMessage;
            this.audioHolder = holder;
            this.position = position;
            this.isOwnMessage = isOwnMessage;
            time = (int) chatMessage.getSnapMessageTime();
            timerText = (TextView) audioHolder.itemView.findViewById(R.id.message_timer_textview_attach);
            snapAttachImageView = (TextView) audioHolder.itemView.findViewById(R.id.snap_image_view);
        }

        public void setTimerTextView(TextView textView) {
            timerText = textView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chatMessage.setIsSnapInProgress(true);


            if (chatMessage.getAttachment() != null) {
                if (chatMessage.getAttachment().getType().equals(Type.IMAGE)) {
                    timerText = (TextView) imageHolder.itemView
                        .findViewById(R.id.message_timer_textview_attach);
                    snapAttachImageView.setVisibility(View.GONE);
                } else if (chatMessage.getAttachment().getType().equals(Type.VIDEO)) {
                    timerText = (TextView) videoHolder.itemView
                        .findViewById(R.id.message_timer_textview_attach);
                    snapAttachImageView.setVisibility(View.GONE);
                } else if (chatMessage.getAttachment().getType().equals(Type.AUDIO)) {
                    timerText = (TextView) audioHolder.itemView
                        .findViewById(R.id.message_timer_textview_attach);
                    snapAttachImageView.setVisibility(View.GONE);
                }

            }




//            updateMessageState(chatMessage, chatDialog);
            if (baseActivity.isNetworkAvailable()) {
//                ArrayList<Integer> arrayList = new ArrayList<>();
//                arrayList.add(currentUser.getId());
//                chatMessage.setReadIds(arrayList);
                chatMessage.setState(State.READ);
                QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, false);
//                Log.d("testt", " chatMessage.setReadIds(arrayList) :" + chatMessage.getReadIds() +" chatMessage: " + chatMessage);

//                StringifyArrayList<String> messagesIdsList = new StringifyArrayList<String>();
//                messagesIdsList.add(chatMessage.getMessageId());


//                QBRestChatService.markMessagesAsRead(chatDialog.getDialogId(), messagesIdsList).performAsync(new QBEntityCallback<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid, Bundle bundle) {
//                        Log.d("testt", "onSuccess ============================= onSuccess");
//                    }
//
//                    @Override
//                    public void onError(QBResponseException e) {
//                        Log.d("testt", "onError ============================= onError");
//
//                    }
//                });






            }

            if (chatMessage.getAttachment() == null){
                messageText.setText(chatMessage.getBody());
            }


//      timerText.setVisibility(View.VISIBLE);
//      timerText.setText(String.valueOf(chatMessage.getSnapMessageTime()));
//        timerText = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
//        rootlv=(LinearLayout)holder.itemView.findViewById(R.id.msg_message_text_view_left);
//        rootrv=(LinearLayout)holder.itemView.findViewById(R.id.msg_message_text_view_right);
//      Log.d("testtsnap","LeftTimerAsyncTask_ onPreExecute_ "+holder.toString()+" ___ "+chatMessage);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                while (time != 0) {
                    TimeUnit.SECONDS.sleep(1);
                    publishProgress(--time);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            timerText.setText(String.valueOf(values[0]));
            Log.d("testtsnap", "LeftTimerAsyncTask_ onProgressUpdate_ = " + String.valueOf(values[0]));
        }

        @SuppressLint("StaticFieldLeak")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        //Your code goes here

                        try {
                            QBRestChatService.deleteMessage(chatMessage.getMessageId(),false).perform();
                        } catch (QBResponseException e) {
                            e.printStackTrace();
                        }

                        DbUtils.deleteMessageLocal(dataManager, chatMessage.getMessageId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();


            int position = chatMessages.indexOf(chatMessage);
            if (position != -1) {
                chatMessages.remove(chatMessage);
                notifyItemRemoved(position);
            }
            wrapContentLinearLayoutManager.removeViewAt(position);
//            wrapContentLinearLayoutManager.scrollToPosition(position);
            if(leftMapTimerTask.containsKey(chatMessage.getMessageId())){

                leftMapTimerTask.remove(chatMessage.getMessageId());
            }

        }


    }


    ////////////////////////////////////////////////////////////////

    public class RightTimerAsyncTask extends AsyncTask<Void, Integer, Void> implements Serializable {

        private static final String TAG = "myLogs";
        private int time;
        private CombinationMessage chatMessage;
        private RightTextMessageViewHolder holder;
        private ImageAttachHolder imageHolder;
        private VideoAttachHolder videoHolder;
        private AudioAttachHolder audioHolder;
        private int position;
        private boolean isOwnMessage;
        private TextView timerText;
        private TextView messageText;

        public RightTimerAsyncTask(CombinationMessage chatMessage, RightTextMessageViewHolder holder, int position,
            boolean isOwnMessage) {
            super();
            this.chatMessage = chatMessage;
            this.holder = holder;
            this.position = position;
            this.isOwnMessage = isOwnMessage;
            time = (int) chatMessage.getSnapMessageTime();
            timerText = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
            messageText = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
        }

        public RightTimerAsyncTask(CombinationMessage chatMessage, ImageAttachHolder holder,
            int position, boolean isOwnMessage) {
            this.chatMessage = chatMessage;
            this.imageHolder = holder;
            this.position = position;
            this.isOwnMessage = isOwnMessage;
            time = (int) chatMessage.getSnapMessageTime();
            timerText = (TextView) imageHolder.itemView.findViewById(R.id.message_timer_textview_attach);
        }

        public RightTimerAsyncTask(CombinationMessage chatMessage, VideoAttachHolder holder,
            int position, boolean b) {
            this.chatMessage = chatMessage;
            this.videoHolder = holder;
            this.position = position;
            this.isOwnMessage = isOwnMessage;
            time = (int) chatMessage.getSnapMessageTime();
            timerText = (TextView) videoHolder.itemView.findViewById(R.id.message_timer_textview_attach);
        }

        public RightTimerAsyncTask(CombinationMessage chatMessage, AudioAttachHolder holder,
            int position, boolean b) {
            this.chatMessage = chatMessage;
            this.audioHolder = holder;
            this.position = position;
            this.isOwnMessage = isOwnMessage;
            time = (int) chatMessage.getSnapMessageTime();
            timerText = (TextView) audioHolder.itemView.findViewById(R.id.message_timer_textview_attach);
        }

        public void setTimerTextView(TextView textView) {
            timerText = textView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chatMessage.setIsSnapInProgress(true);

            if (chatMessage.getAttachment() != null) {
                if (chatMessage.getAttachment().getType().equals(Type.IMAGE)) {
                    timerText = (TextView) imageHolder.itemView
                        .findViewById(R.id.message_timer_textview_attach);
                } else if (chatMessage.getAttachment().getType().equals(Type.VIDEO)) {
                    timerText = (TextView) videoHolder.itemView
                        .findViewById(R.id.message_timer_textview_attach);
                } else if (chatMessage.getAttachment().getType().equals(Type.AUDIO)) {
                    timerText = (TextView) audioHolder.itemView
                        .findViewById(R.id.message_timer_textview_attach);
                }


            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                while (time != 0) {
                    TimeUnit.SECONDS.sleep(1);
                    publishProgress(--time);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            timerText.setText(String.valueOf(values[0]));
            Log.d("testtsnap", "RightTimerAsyncTask_ onProgressUpdate_ = " + String.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        //Your code goes here

                        try {
                            QBRestChatService.deleteMessage(chatMessage.getMessageId(),true).perform();
                        } catch (QBResponseException e) {
                            e.printStackTrace();
                        }
                        DbUtils.deleteMessageLocal(dataManager, chatMessage.getMessageId());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            int position = chatMessages.indexOf(chatMessage);
            if (position != -1) {
                chatMessages.remove(chatMessage);
                notifyItemRemoved(position);
            }

            wrapContentLinearLayoutManager.removeViewAt(position);
//            wrapContentLinearLayoutManager.scrollToPosition(position);
            if(rightMapTimerTask.containsKey(chatMessage.getMessageId())){
                rightMapTimerTask.remove(chatMessage.getMessageId());
            }

        }
    }







}