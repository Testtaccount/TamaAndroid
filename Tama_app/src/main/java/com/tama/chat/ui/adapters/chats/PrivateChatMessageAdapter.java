package com.tama.chat.ui.adapters.chats;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.exception.QBResponseException;
import com.tama.chat.R;
import com.tama.chat.ui.activities.base.BaseActivity;
import com.tama.chat.ui.views.recyclerview.WrapContentLinearLayoutManager;
import com.tama.chat.utils.DateUtils;
import com.tama.chat.utils.listeners.FriendOperationListener;
import com.tama.q_municate_core.models.CombinationMessage;
import com.tama.q_municate_core.utils.DbUtils;
import com.tama.q_municate_db.managers.DataManager;
import com.tama.q_municate_db.models.Attachment.Type;
import com.tama.q_municate_db.models.DialogNotification;
import com.tama.q_municate_db.models.State;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PrivateChatMessageAdapter extends BaseChatMessagesAdapter implements
    StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

  private static final String TAG = PrivateChatMessageAdapter.class.getSimpleName();
  private static final int SECOND_IN_MILLIS = 1000;
  private static final String SNAP = "SNAP";

  private static int EMPTY_POSITION = -1;
  private int lastRequestPosition = EMPTY_POSITION;
  private int lastInfoRequestPosition = EMPTY_POSITION;
  private FriendOperationListener friendOperationListener;
  private WrapContentLinearLayoutManager wrapContentLinearLayoutManager;

  public static Map<String,LeftTimerAsyncTask> leftMapTimerTask = new ArrayMap<>();
  public static Map<String,RightTimerAsyncTask> rightMapTimerTask = new ArrayMap<>();

  private CombinationMessage saveMsg;
  private boolean isMessageShowing = true;

  protected DataManager dataManager;


  public PrivateChatMessageAdapter(BaseActivity baseActivity, List<CombinationMessage> chatMessages,
      FriendOperationListener friendOperationListener, QBChatDialog chatDialog) {
    super(baseActivity, chatDialog, chatMessages);
    this.friendOperationListener = friendOperationListener;
    this.wrapContentLinearLayoutManager=new WrapContentLinearLayoutManager(context);
    this.dataManager = DataManager.getInstance();
    this.chatDialog = chatDialog;
  }


  @Override
  protected void onBindViewCustomHolder(QBMessageViewHolder holder, CombinationMessage chatMessage,
      int position) {
    Log.d(TAG, "onBindViewCustomHolder chatMessage getBody= " + chatMessage.getBody());
    FriendsViewHolder friendsViewHolder = (FriendsViewHolder) holder;

    boolean friendsRequestMessage = DialogNotification.Type.FRIENDS_REQUEST.equals(
        chatMessage.getNotificationType());
    boolean friendsInfoRequestMessage =
        chatMessage.getNotificationType() != null && !friendsRequestMessage;
    TextView textView = (TextView) holder.itemView.findViewById(R.id.message_textview);
    TextView timeTextMessageTextView = (TextView) holder.itemView
        .findViewById(R.id.time_text_message_textview);

    if (friendsRequestMessage) {
      textView.setText(chatMessage.getBody());
      timeTextMessageTextView
          .setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate() * SECOND_IN_MILLIS));

      setVisibilityFriendsActions(friendsViewHolder, View.GONE);
    } else if (friendsInfoRequestMessage) {
      Log.d(TAG, "friendsInfoRequestMessage onBindViewCustomHolder chatMessage getBody= "
          + chatMessage.getBody());
      textView.setText(chatMessage.getBody());
      timeTextMessageTextView
          .setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate() * SECOND_IN_MILLIS));

      setVisibilityFriendsActions(friendsViewHolder, View.GONE);

      lastInfoRequestPosition = position;
    } else {
      Log.d(TAG,
          "else onBindViewCustomHolderr chatMessage getBody= " + chatMessage.getBody());
      textView.setText(chatMessage.getBody());
      timeTextMessageTextView
          .setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate() * SECOND_IN_MILLIS));
    }

    if (!State.READ.equals(chatMessage.getState()) && isIncoming(chatMessage) && baseActivity
        .isNetworkAvailable()) {
      updateMessageState(chatMessage, chatDialog);
    }
    // check if last messageCombination is request messageCombination
    boolean lastRequestMessage = (position == getItemCount() - 1 && friendsRequestMessage);
    if (lastRequestMessage) {
      findLastFriendsRequest();
    }

    // check if friend was rejected/deleted.
    if (lastRequestPosition != EMPTY_POSITION && lastRequestPosition < lastInfoRequestPosition) {
      lastRequestPosition = EMPTY_POSITION;
    } else if ((lastRequestPosition != EMPTY_POSITION
        && lastRequestPosition == position)) { // set visible friends actions
      setVisibilityFriendsActions((FriendsViewHolder) holder, View.VISIBLE);
      initListeners((FriendsViewHolder) holder, position,
          chatMessage.getDialogOccupant().getUser().getId());
    }
  }

  @Override
  protected void onBindViewMsgRightHolder(TextMessageHolder holder, CombinationMessage chatMessage,
      int position) {
//
//    Log.d("mytest", "onBindViewMsgRightHolder ============> position: "+position);
//    Log.d("mytest", "onBindViewMsgRightHolder ============> chatMessage.getBody(): "+chatMessage.getBody()+" , chatMessage.getMessageId(): "+chatMessage.getMessageId()+" , chatMessage.getId(): "+chatMessage.getId());
//    Log.d("mytest", "onBindViewMsgRightHolder ============> holder.getItemId(): "+holder.getItemId()+" , holder.itemView.getId(): "+holder.itemView.getId());
//


      ImageView messageStatusView = (ImageView) holder.itemView.findViewById(R.id.message_status_image_view);
      TextView msgTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
      TextView timeTextMessageTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_time_message);
      TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
      setViewVisibility(holder.avatar, View.GONE);
      timeTextMessageTextView.setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate() * SECOND_IN_MILLIS));




    if (chatMessage.getState() != null) {
      setMessageStatus(messageStatusView, State.DELIVERED.equals(
          chatMessage.getState()), State.READ.equals(chatMessage.getState()));


    //uxarkac
    if(chatMessage.getSnapMessage() && State.READ.equals(chatMessage.getState())){
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
      isMessageShowing = true;
    }

    } else {
      timerTextView.setVisibility(View.GONE);
      messageStatusView.setImageResource(android.R.color.transparent);
    }



//    //uxarkoxiny gnacox
//
////    if(chatMessage!=null){
//      ImageView messageStatusView = (ImageView) holder.itemView.findViewById(R.id.message_status_image_view);
//      TextView msgTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
//      TextView timeTextMessageTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_time_message);
//      TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
//      setViewVisibility(holder.avatar, View.GONE);
//
//      timeTextMessageTextView.setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate() * SECOND_IN_MILLIS));
//
//    if (!chatMessage.getSnapMessage()) {
//      holder.itemView.setEnabled(false);
//
//      timerTextView.setVisibility(View.GONE);
//      msgTextView.setText(chatMessage.getBody());
//
//      if (chatMessage.getState() != null) {
//        setMessageStatus(messageStatusView, State.DELIVERED.equals(
//            chatMessage.getState()), State.READ.equals(chatMessage.getState()));
//      } else {
//        messageStatusView.setImageResource(android.R.color.transparent);
//      }
//
//      return;
//    }
//
//    if(chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()){
//
//      msgTextView.setText(chatMessage.getBody());
//
//      if (chatMessage.getState() != null) {
//        setMessageStatus(messageStatusView, State.DELIVERED.equals(chatMessage.getState()), State.READ.equals(chatMessage.getState()));
//
//        if(State.READ.equals(chatMessage.getState())) {
//          timerTextView.setVisibility(View.VISIBLE);
//
////          if (timerTaskExistByMessageId(rightMapTimerTask, chatMessage.getMessageId())) {
//
//            if (rightMapTimerTask != null) {
//              RightTimerAsyncTask timerAsyncTask = rightMapTimerTask.get(chatMessage.getMessageId());
//              if (timerAsyncTask != null) {
//                timerAsyncTask.setTimerTextView(timerTextView);
//              }  else {
//                RightTimerAsyncTask rightTimerAsyncTask = new RightTimerAsyncTask(chatMessage,
//                    (RightTextMessageViewHolder) holder, position, true);
//                AsyncTaskCompat.executeParallel(rightTimerAsyncTask);
//                rightMapTimerTask.put(chatMessage.getMessageId(), rightTimerAsyncTask);
////                timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));
//              }
//            }
//          }
//
////        }else {
////          msgTextView.setText(chatMessage.getBody());
////          chatMessage.setSnapMessageTime(0);
////          new RightTimerAsyncTask(chatMessage, (RightTextMessageViewHolder) holder,position,false).execute();
////        }
//
//
//        }else {
//          messageStatusView.setImageResource(android.R.color.transparent);
//        }
//      // uxxel mi qaisi depqy
//      }else if(chatMessage.getSnapMessage() && chatMessage.getIsSnapInProgress()) {
//      timerTextView.setVisibility(View.GONE);
////      timerTextView.setText("0");
//      msgTextView.setText(chatMessage.getBody());
//      chatMessage.setSnapMessageTime(0);
//      new RightTimerAsyncTask(chatMessage, (RightTextMessageViewHolder) holder,position,false).execute();
//    }


//      ////////----------
//      if (chatMessage.getState() != null) {
//        setMessageStatus(messageStatusView, State.DELIVERED.equals(chatMessage.getState()), State.READ.equals(chatMessage.getState()));
//        msgTextView.setText(chatMessage.getBody());
//
//        if (State.READ.equals(chatMessage.getState()) && chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()) {
//
//
////          if (mapTimerTask != null) {
//
////            TimerAsyncTask timerAsyncTask = mapTimerTask.get(chatMessage.getMessageId());
////            if (timerAsyncTask != null) {
////              timerAsyncTask.setTimerTextView(timerTextView);
////            } else {
//              //stexa
//          RightTimerAsyncTask rightTimerAsyncTask = new RightTimerAsyncTask(chatMessage,
//              (RightTextMessageViewHolder) holder, position, true);
//              AsyncTaskCompat.executeParallel(rightTimerAsyncTask);
////              mapTimerTask.put(chatMessage.getMessageId(), timerTask);
////            }
//
////          }
//
//
//        } else {
//          msgTextView.setText(chatMessage.getBody());
//          timerTextView.setVisibility(View.GONE);
//        }
//        ////////////////////////---------------------
//      } else {
//        messageStatusView.setImageResource(android.R.color.transparent);
//      }
//
//      ///

//    }else {
////      WrapContentLinearLayoutManager wrapContentLinearLayoutManager=new WrapContentLinearLayoutManager(context);
////      wrapContentLinearLayoutManager.removeViewAt(position);
////      holder.itemView.setVisibility(View.GONE);
////      notifyItemRemoved(position);
//      notifyDataSetChanged();
//    }



    ///////////////////////////////////////////////////////////////////////

//
//    if (chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()) {
//      TimerAsyncTask timerTask = new TimerAsyncTask(chatMessage, holder, isIncoming(chatMessage));
//      mapTimerTask.put(chatMessage.getMessageId(), timerTask);
//      AsyncTaskCompat.executeParallel(timerTask);
//
//    } else if (!isIncoming(chatMessage) && chatMessage.getSnapMessage() && chatMessage
//        .getIsSnapInProgress()) {
//
//      if (mapTimerTask != null && !mapTimerTask.isEmpty()) {
//        TimerAsyncTask timerAsyncTask = mapTimerTask.get(chatMessage.getMessageId());
//        if (timerAsyncTask != null) {
//          timerAsyncTask.setTimerTextView(timerTextView);
//        }
//      }
//    } else if (!isIncoming(chatMessage)) {
//
//    }
//
//    if (isIncoming(chatMessage) && chatMessage.getSnapMessage() && State.READ
//        .equals(chatMessage.getState())) {
//      setViewVisibility(messageTimerView, View.VISIBLE);
//      if (mapTimerTask != null) {
//        TimerAsyncTask timerAsyncTask = mapTimerTask.get(chatMessage.getMessageId());
//        if (timerAsyncTask != null) {
//          timerAsyncTask.setTimerTextView(timerTextView);
//        } else {
//          TimerAsyncTask timerTask = new TimerAsyncTask(chatMessage, holder,
//              isIncoming(chatMessage));
//          AsyncTaskCompat.executeParallel(timerTask);
//          mapTimerTask.put(chatMessage.getMessageId(), timerTask);
//          timerTextView.setText(String.valueOf(chatMessage.getSnapMessageTime()));
//        }
//      }
//    }
//
//    if (ownMessage && chatMessage.getState() != null) {
//      setMessageStatus(holder.attachDeliveryStatusImageView, State.DELIVERED.equals(
//          chatMessage.getState()), State.READ.equals(chatMessage.getState()));
//    }
//  } else
//
//  {
//    resetUI(holder);
//    setViewVisibility(holder.textMessageView, View.VISIBLE);
//    if (!ownMessage && chatMessage.getSnapMessage() && !chatMessage
//        .getIsSnapInProgress()) {
//      holder.textMessageView.setEnabled(true);
//
//      isMessageShowing = false;
//      holder.messageTextView.setText(SNAP);
//      holder.messageTimerView.setVisibility(View.VISIBLE);
//      holder.messageTimerView.setText(String.valueOf(chatMessage.getSnapMessageTime()));
//
//      TimerAsyncTask timerTask = new TimerAsyncTask(chatMessage, holder, ownMessage);
//
//      mapTimerTask.put(chatMessage.getMessageId(), timerTask);
//
//      holder.textMessageView.setOnClickListener(onSnapMsgTouchListener(timerTask));
//    } else if (!ownMessage && chatMessage.getSnapMessage() && chatMessage
//        .getIsSnapInProgress()) {
//      isMessageShowing = false;
//      holder.messageTimerView.setVisibility(View.VISIBLE);
//      holder.messageTimerView.setText("");
//      if (mapTimerTask != null && !mapTimerTask.isEmpty()) {
//        TimerAsyncTask timerAsyncTask = mapTimerTask.get(chatMessage.getMessageId());
//        if (timerAsyncTask != null) {
//          timerAsyncTask.setTimerTextView(holder.messageTimerView);
//        }
//      }
//      holder.messageTextView.setText(chatMessage.getBody());
//    } else if (ownMessage && chatMessage.getSnapMessage() && State.READ
//        .equals(chatMessage.getState())) {
//      holder.messageTimerView.setVisibility(View.VISIBLE);
//      if (mapTimerTask != null) {
//        TimerAsyncTask timerAsyncTask = mapTimerTask.get(chatMessage.getMessageId());
//        if (timerAsyncTask != null) {
//          timerAsyncTask.setTimerTextView(holder.messageTimerView);
//        } else {
//          TimerAsyncTask timerTask = new TimerAsyncTask(chatMessage, holder, ownMessage);
//          AsyncTaskCompat.executeParallel(timerTask);
//          mapTimerTask.put(chatMessage.getMessageId(), timerTask);
//          holder.messageTimerView
//              .setText(String.valueOf(chatMessage.getSnapMessageTime()));
//        }
//      }
//    } else {
//      holder.messageTextView.setText(chatMessage.getBody());
//      holder.messageTimerView.setVisibility(View.GONE);
//      isMessageShowing = true;
//    }
//    holder.timeTextMessageTextView
//        .setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate()));
//
//    if (ownMessage && chatMessage.getState() != null) {
//      setMessageStatus(holder.messageDeliveryStatusImageView, State.DELIVERED.equals(
//          chatMessage.getState()), State.READ.equals(chatMessage.getState()));
//    } else if (ownMessage && chatMessage.getState() == null) {
//      holder.messageDeliveryStatusImageView.setImageResource(android.R.color.transparent);
//    }
//  }
//
//        if(!State.READ.equals(chatMessage.getState())&&!ownMessage &&baseActivity.isConnected()&&isMessageShowing)
//
//  {
//    chatMessage.setState(State.READ);
//    QBUpdateStatusMessageCommand
//        .start(baseActivity, ChatUtils.createQBDialogFromLocalDialog(dataManager, dialog),
//            chatMessage, true);

    ///////////////////////////////////////////////////////////////////

//    super.onBindViewMsgRightHolder(holder, chatMessage, position);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onBindViewMsgLeftHolder(TextMessageHolder holder, CombinationMessage chatMessage, int position) {

//    Log.d("mytest", "onBindViewMsgLeftHolder <============ position: "+position);
//    Log.d("mytest", "onBindViewMsgLeftHolder <============ chatMessage.getBody(): "+chatMessage.getBody()+" , chatMessage.getMessageId(): "+chatMessage.getMessageId()+" , chatMessage.getId(): "+chatMessage.getId());
//    Log.d("mytest", "onBindViewMsgLeftHolder <============ holder.getItemId(): "+holder.getItemId()+" , holder.itemView.getId(): "+holder.itemView.getId());

    //uxarkaciny       ekac
//    if(chatMessage!=null){
//      saveMsg=chatMessage;

      LinearLayout linearLayout = (LinearLayout) holder.itemView.findViewById(R.id.msg_custom_widget_frame_top);
      setViewVisibility(holder.avatar, View.GONE);
      setViewVisibility(linearLayout, View.GONE);

      TextView msgTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
      TextView timeTextMessageTextView = (TextView) holder.itemView.findViewById(R.id.msg_text_time_message);
      TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);

      timeTextMessageTextView.setText(DateUtils.formatDateSimpleTime(chatMessage.getCreatedDate() * SECOND_IN_MILLIS));

      if (!chatMessage.getSnapMessage()) {
//        holder.itemView.setEnabled(false);

        timerTextView.setVisibility(View.GONE);
        updateMessageState(chatMessage, chatDialog);

        msgTextView.setText(chatMessage.getBody());
//        holder.messageTextView.setText(SNAP);
        return;
      }

    if (chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()) {
      timerTextView.setVisibility(View.GONE);
      msgTextView.setText(SNAP);
      isMessageShowing = false;

          if(!timerTaskExistByMessageId(leftMapTimerTask,chatMessage.getMessageId())){
            msgTextView.setOnTouchListener(onSnapMsgTouchListener(chatMessage, (LeftTextMessageViewHolder) holder, position));
          }

      } else if (chatMessage.getSnapMessage() && chatMessage.getIsSnapInProgress()) {
      isMessageShowing = true;

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

    //      else if (!chatMessage.getSnapMessage()) {
//        updateMessageState(chatMessage, chatDialog);
//      }
//    }else {
//      holder.itemView.setVisibility(View.GONE);
////      notifyItemRemoved(position);
//      notifyDataSetChanged();
//    }


//        super.onBindViewMsgLeftHolder(holder, chatMessage, position);

  }

  private boolean timerTaskExistByMessageId(Map<String, ? extends AsyncTask> asyncTaskMap,String id){

    return asyncTaskMap.containsKey(id);

  }

  private View.OnLongClickListener onMessageLongClickListner(final CombinationMessage chatMessage,final boolean own){
    return new View.OnLongClickListener() {
      @SuppressLint("StaticFieldLeak")
      @Override
      public boolean onLongClick(View v) {

        return false;
      }
    };
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
//            v.setEnabled(false);
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
//            v.setEnabled(false);
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
//            v.setEnabled(false);
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





  //gnacox nkar
  @Override
  protected void onBindViewAttachRightHolder(ImageAttachHolder holder,CombinationMessage chatMessage, int position) {
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


    showSendStatusView(holder, chatMessage);
    super.onBindViewAttachRightHolder(holder, chatMessage, position);
  }

  /////////////ekac nkar
  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onBindViewAttachLeftHolder(ImageAttachHolder holder,CombinationMessage chatMessage, int position) {
    setViewVisibility(holder.avatar, View.GONE);

    TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);
    TextView snapAttachImageView = (TextView) holder.itemView.findViewById(R.id.snap_image_view);

    if (!chatMessage.getSnapMessage()) {
//      holder.itemView.setEnabled(false);

      timerTextView.setVisibility(View.GONE);
      snapAttachImageView.setVisibility(View.GONE);
      updateMessageState(chatMessage, chatDialog);
    }

    if(chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()){
      isMessageShowing = false;
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
      isMessageShowing = false;

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

    super.onBindViewAttachLeftHolder(holder, chatMessage, position);
  }


  ///uxarkac video
  @Override
  protected void onBindViewAttachRightVideoHolder(VideoAttachHolder holder,
      CombinationMessage chatMessage, int position) {


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


    showSendStatusView(holder, chatMessage);
    super.onBindViewAttachRightVideoHolder(holder, chatMessage, position);
  }


  ////ekac video
  @Override
  protected void onBindViewAttachLeftVideoHolder(VideoAttachHolder holder,
      CombinationMessage chatMessage, int position) {
    setViewVisibility(holder.avatar, View.GONE);


    TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);
    TextView snapAttachImageView = (TextView) holder.itemView.findViewById(R.id.snap_image_view);

    if (!chatMessage.getSnapMessage()) {
//      holder.itemView.setEnabled(false);

      timerTextView.setVisibility(View.GONE);
      snapAttachImageView.setVisibility(View.GONE);
      updateMessageState(chatMessage, chatDialog);

    }

    if(chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()){
      isMessageShowing = false;
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
      isMessageShowing = false;

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
    super.onBindViewAttachLeftVideoHolder(holder, chatMessage, position);
  }

  ///uxarkac audio
  @Override
  protected void onBindViewAttachRightAudioHolder(AudioAttachHolder holder,
      CombinationMessage chatMessage, int position) {

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

    showSendStatusView(holder, chatMessage);
    super.onBindViewAttachRightAudioHolder(holder, chatMessage, position);
  }

  //ekac audio
  @Override
  protected void onBindViewAttachLeftAudioHolder(AudioAttachHolder holder,
      CombinationMessage chatMessage, int position) {
    setViewVisibility(holder.avatar, View.GONE);


    TextView timerTextView = (TextView) holder.itemView.findViewById(R.id.message_timer_textview_attach);
    TextView snapAttachImageView = (TextView) holder.itemView.findViewById(R.id.snap_image_view);

    if (!chatMessage.getSnapMessage()) {
//      holder.itemView.setEnabled(false);

      timerTextView.setVisibility(View.GONE);
      snapAttachImageView.setVisibility(View.GONE);
      updateMessageState(chatMessage, chatDialog);

    }

    if(chatMessage.getSnapMessage() && !chatMessage.getIsSnapInProgress()){
      isMessageShowing = false;
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
      isMessageShowing = false;

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
    super.onBindViewAttachLeftAudioHolder(holder, chatMessage, position);
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

  // komentac bacel ? test anel
//  @Override
//  public long getHeaderId(int position) {
//    ////////ind b e
//    CombinationMessage combinationMessage = getItem(position);
//       return DateUtils.toShortDateLong(combinationMessage.getCreatedDate());
//  }

  @Override
  public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
    View view = inflater.inflate(R.layout.item_chat_sticky_header_date, parent, false);
    return new RecyclerView.ViewHolder(view) {
    };
  }


  @Override
  public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
    View view = holder.itemView;

    TextView headerTextView = (TextView) view.findViewById(R.id.header_date_textview);
//    CombinationMessage combinationMessage = getItem(position);
    CombinationMessage combinationMessage=null;
    for(int i = position; i >= 0; i--){
      if(!indexOutOfBounds(i)){
        Log.d("my","position_ i : "+i);
        combinationMessage = getItem(i);
        break;
      }
    }
    headerTextView
        .setText(DateUtils.toTodayYesterdayFullMonthDate(combinationMessage.getCreatedDate()));
  }

  @Override
  public int getItemViewType(int position) {
//    CombinationMessage combinationMessage = getItem(position);

    CombinationMessage combinationMessage=null;
    for(int i = position; i >= 0; i--){
      if(!indexOutOfBounds(i)){
        Log.d("my","position_ i : "+i);
        combinationMessage = getItem(i);
        break;
      }
    }
    if (combinationMessage.getNotificationType() != null) {
      Log.d(TAG,
          "chatMessage.getNotificationType()" + combinationMessage.getNotificationType());
      return TYPE_REQUEST_MESSAGE;
    }
    return super.getItemViewType(position);
  }

  @Override
  protected QBMessageViewHolder onCreateCustomViewHolder(ViewGroup parent, int viewType) {
    return viewType == TYPE_REQUEST_MESSAGE ? new FriendsViewHolder(
        inflater.inflate(R.layout.item_friends_notification_message, parent, false)) : null;
  }

  @Override
  protected void showAttachUI(ImageAttachHolder viewHolder, boolean isIncoming) {
    setViewVisibility(viewHolder.itemView.findViewById(R.id.msg_bubble_background), View.VISIBLE);
  }

  public void findLastFriendsRequestMessagesPosition() {
    new FindLastFriendsRequestThread().run();
  }

  private void findLastFriendsRequest() {
    for (int i = 0; i < getList().size(); i++) {
      findLastFriendsRequest(i, getList().get(i));
    }
  }

  public void clearLastRequestMessagePosition() {
    lastRequestPosition = EMPTY_POSITION;
  }

  private void findLastFriendsRequest(int position, CombinationMessage combinationMessage) {
    boolean ownMessage;
    boolean friendsRequestMessage;
    boolean isFriend;

    if (combinationMessage.getNotificationType() != null) {
      ownMessage = !combinationMessage.isIncoming(currentUser.getId());
      friendsRequestMessage = DialogNotification.Type.FRIENDS_REQUEST.equals(
          combinationMessage.getNotificationType());

      if (friendsRequestMessage && !ownMessage) {
        isFriend = dataManager.getFriendDataManager().
            getByUserId(combinationMessage.getDialogOccupant().getUser().getId()) != null;
        if (!isFriend) {
          lastRequestPosition = position;
        }
      }
    }
  }

  private void setVisibilityFriendsActions(FriendsViewHolder viewHolder, int visibility) {
    setViewVisibility(viewHolder.acceptFriendImageView, visibility);
    setViewVisibility(viewHolder.dividerView, visibility);
    setViewVisibility(viewHolder.rejectFriendImageView, visibility);
  }

  private void initListeners(FriendsViewHolder viewHolder, final int position, final int userId) {
    viewHolder.acceptFriendImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        friendOperationListener.onAcceptUserClicked(position, userId);
      }
    });

    viewHolder.rejectFriendImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        friendOperationListener.onRejectUserClicked(position, userId);
      }
    });
  }

  private class FindLastFriendsRequestThread extends Thread {

    @Override
    public void run() {
      findLastFriendsRequest();
    }
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

  protected static class FriendsViewHolder extends QBMessageViewHolder {

    @Nullable
    @Bind(R.id.message_textview)
    TextView messageTextView;

    @Nullable
    @Bind(R.id.time_text_message_textview)
    TextView timeTextMessageTextView;

    @Nullable
    @Bind(R.id.accept_friend_imagebutton)
    ImageView acceptFriendImageView;

    @Nullable
    @Bind(R.id.divider_view)
    View dividerView;

    @Nullable
    @Bind(R.id.reject_friend_imagebutton)
    ImageView rejectFriendImageView;


    public FriendsViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, itemView);
    }
  }



  //////////////////  |||||||||||||||||||   \\\\\\\\\\\\\\\\\\\\\\\\\

//  private class TimerAsyncTask extends AsyncTask<Void, Integer, Void> implements Serializable {
//
//    private static final String TAG = "myLogs";
//    private int time;
//    private CombinationMessage chatMessage;
//    private LeftTextMessageViewHolder holder;
//    private int position;
//    private boolean isOwnMessage;
//    private TextView timerText;
//    private TextView messageText;
//
//
//    public TimerAsyncTask(CombinationMessage chatMessage, LeftTextMessageViewHolder holder, int position,
//        boolean isOwnMessage) {
//      super();
//      this.chatMessage = chatMessage;
//      this.holder = holder;
//      this.position = position;
//      this.isOwnMessage = isOwnMessage;
//      time = (int) chatMessage.getSnapMessageTime();
//      timerText = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
//      messageText = (TextView) holder.itemView.findViewById(R.id.msg_text_message);
//    }
//
//    public void setTimerTextView(TextView textView) {
//      timerText = textView;
//    }
//
//    @Override
//    protected void onPreExecute() {
//      super.onPreExecute();
//      chatMessage.setIsSnapInProgress(true);
//
////      chatMessage.setState(State.READ);
////      QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, true);
//      messageText.setText(chatMessage.getBody());
//
//      timerText.setVisibility(View.VISIBLE);
//      timerText.setText(String.valueOf(chatMessage.getSnapMessageTime()));
////        timerText = (TextView) holder.itemView.findViewById(R.id.message_timer_textview);
////        rootlv=(LinearLayout)holder.itemView.findViewById(R.id.msg_message_text_view_left);
////        rootrv=(LinearLayout)holder.itemView.findViewById(R.id.msg_message_text_view_right);
//      Log.d("testtsnap","TimerAsyncTask_ onPreExecute_ "+holder.toString()+" ___ "+chatMessage);
//
//    }
//
//    @Override
//    protected Void doInBackground(Void... params) {
//      try {
//        while (time != 0) {
//          TimeUnit.SECONDS.sleep(1);
//          publishProgress(--time);
//        }
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//      return null;
//    }
//
//
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//      super.onProgressUpdate(values);
//      timerText.setText(String.valueOf(values[0]));
//      Log.d("testtsnap", "TimerAsyncTask_ onProgressUpdate_ = " + String.valueOf(values[0]));
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//      super.onPostExecute(aVoid);
//      timerText.setVisibility(View.GONE);
//
////      Thread thread = new Thread(new Runnable() {
////
////        @Override
////        public void run() {
////          try  {
////            //Your code goes here
////
////            try {
////              QBRestChatService.deleteMessage(chatMessage.getMessageId(),false).perform();
////              DbUtils.deleteMessageLocal(dataManager, chatMessage.getMessageId());
////
////            } catch (QBResponseException e) {
////              e.printStackTrace();
////            }
////
////          } catch (Exception e) {
////            e.printStackTrace();
////          }
////        }
////      });
////      thread.start();
//
////      Async(
////          new QBEntityCallback<Void>() {
////            @Override
////            public void onSuccess(Void aVoid, Bundle bundle) {
////              ToastUtils.longToast("Deleted!!!!!!!!!");
////              Log.d("testt","Deleted!!!!!!!!!");
////              DbUtils.deleteMessageLocal(dataManager, chatMessage.getMessageId());
////
////            }
////
////            @Override
////            public void onError(QBResponseException e) {
////              ToastUtils.longToast("Not Deleted!!!");
////              Log.d("testt","Not Deleted!!!");
////            }
////          });
////        rootlv.setVisibility(View.GONE);
////        onBindViewMsgLeftHolder(holder,null,position);
////        notifyDataSetChanged();
//
////      wrapContentLinearLayoutManager.removeViewAt(position);
////      wrapContentLinearLayoutManager.scrollToPosition(position);
////      leftMapTimerTask.remove(chatMessage.getMessageId());
//
//      Log.d("testtsnap","TimerAsyncTask_ onPostExecute_ "+holder.toString()+" ___ "+chatMessage);
//
////      }
////
////      DbUtils.deleteMessageLocal(dataManager, chatMessage.getMessageId());
//////      rootrv.setVisibility(View.GONE);
////
////      notifyDataSetChanged();
//
//    }
//
//
//  }





  ///////////////////////////////////////////////////////////////

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


      updateMessageState(chatMessage, chatDialog);
//      chatMessage.setState(State.READ);
//      QBUpdateStatusMessageCommand.start(baseActivity, chatDialog, chatMessage, true);

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
//          chatMessage.setSnapMessageTime(--time);
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
//      chatMessages.remove(position);
//      timerText.setVisibility(View.GONE);
//      wrapContentLinearLayoutManager.scrollToPosition(position);
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
//      chatMessages.remove(position);
//      wrapContentLinearLayoutManager.scrollToPosition(position);
      if(rightMapTimerTask.containsKey(chatMessage.getMessageId())){
        rightMapTimerTask.remove(chatMessage.getMessageId());
      }


    }
  }

  ////////////////////////////////////////////////////////////////////

}