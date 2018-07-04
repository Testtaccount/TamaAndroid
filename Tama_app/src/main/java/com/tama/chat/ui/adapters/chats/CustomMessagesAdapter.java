package com.tama.chat.ui.adapters.chats;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import com.quickblox.ui.kit.chatmessage.adapter.QBMessagesAdapter;
import com.tama.chat.R;
import com.tama.q_municate_core.models.CombinationMessage;
import java.util.List;

/**
 * Created by Avetik on 02-Nov-17.
 */

public class CustomMessagesAdapter extends QBMessagesAdapter<CombinationMessage> {

//  @Bind(R.id.message_timer_textview)
//  protected TextView timerTextView;




  private static final String TAG = "aaa";
  private SparseIntArray containerLayoutRes = new SparseIntArray() {
    {
      this.put(1, R.layout.my_list_item_text_right);
      this.put(2, R.layout.my_list_item_text_left);
      this.put(3, R.layout.my_list_item_attach_right);
      this.put(4, R.layout.my_list_item_attach_left);
      this.put(5, R.layout.my_list_item_attach_right_audio);
      this.put(6, R.layout.my_list_item_attach_left_audio);
      this.put(7, R.layout.my_list_item_attach_right_video);
      this.put(8, R.layout.my_list_item_attach_left_video);
    }
  };


  public CustomMessagesAdapter(Context context,
      List<CombinationMessage> chatMessages) {
    super(context, chatMessages);

  }


  @Override
  public QBMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//    return super.onCreateViewHolder(parent, viewType);


    switch (viewType) {
      case 1: {
//        Log.d("mytest", "onCreateViewHolder ========================== viewType: "+viewType);

        this.qbViewHolder = new RightTextMessageViewHolder(
            this.inflater.inflate(this.containerLayoutRes.get(viewType), parent, false),
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_message,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_time_message,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_link_preview);

        return this.qbViewHolder;
      }
      case 2: {
//        Log.d("mytest", "onCreateViewHolder ========================== viewType: "+viewType);

        this.qbViewHolder = new LeftTextMessageViewHolder(
            this.inflater.inflate(this.containerLayoutRes.get(viewType), parent, false),
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_message,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_time_message,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_link_preview);
        return this.qbViewHolder;
      }
      case 3:
        this.qbViewHolder = new QBMessagesAdapter.ImageAttachHolder(
            this.inflater.inflate(this.containerLayoutRes.get(viewType), parent, false),
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_image_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_progressbar_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_time_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_signs_attach);
        return this.qbViewHolder;
      case 4:
        this.qbViewHolder = new QBMessagesAdapter.ImageAttachHolder(
            this.inflater.inflate(this.containerLayoutRes.get(viewType), parent, false),
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_image_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_progressbar_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_time_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_signs_attach);
        return this.qbViewHolder;
      case 5:
        this.qbViewHolder = new QBMessagesAdapter.AudioAttachHolder(
            this.inflater.inflate(this.containerLayoutRes.get(viewType), parent, false),
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_audio_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_attach_duration,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_time_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_signs_attach);
        return this.qbViewHolder;
      case 6:
        this.qbViewHolder = new QBMessagesAdapter.AudioAttachHolder(
            this.inflater.inflate(this.containerLayoutRes.get(viewType), parent, false),
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_audio_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_attach_duration,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_time_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_signs_attach);
        return this.qbViewHolder;
      case 7:
        this.qbViewHolder = new QBMessagesAdapter.VideoAttachHolder(
            this.inflater.inflate(this.containerLayoutRes.get(viewType), parent, false),
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_video_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_progressbar_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_attach_duration,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_time_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_signs_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_video_play_icon);
        return this.qbViewHolder;
      case 8:
        this.qbViewHolder = new QBMessagesAdapter.VideoAttachHolder(
            this.inflater.inflate(this.containerLayoutRes.get(viewType), parent, false),
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_video_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_progressbar_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_attach_duration,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_text_time_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_signs_attach,
            com.quickblox.ui.kit.chatmessage.adapter.R.id.msg_video_play_icon);
        return this.qbViewHolder;
      default:
        Log.d(TAG, "onCreateViewHolder case default");
        return this.onCreateCustomViewHolder(parent, viewType);
    }
  }


  protected static class RightTextMessageViewHolder extends TextMessageHolder {


    @Nullable
    @Bind(R.id.message_timer_textview)
    TextView messageTimerView;

    public RightTextMessageViewHolder(View itemView, int msgId, int timeId,
        int linkPreviewLayoutId) {
      super(itemView, msgId, timeId, linkPreviewLayoutId);
    }
  }

  protected static class LeftTextMessageViewHolder extends TextMessageHolder {


    @Nullable
    @Bind(R.id.message_timer_textview)
    TextView messageTimerView;

    public LeftTextMessageViewHolder(View itemView, int msgId, int timeId,
        int linkPreviewLayoutId) {
      super(itemView, msgId, timeId, linkPreviewLayoutId);
    }
  }

}
