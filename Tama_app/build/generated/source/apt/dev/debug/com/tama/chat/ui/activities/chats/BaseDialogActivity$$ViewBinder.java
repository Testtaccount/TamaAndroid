// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BaseDialogActivity$$ViewBinder<T extends com.tama.chat.ui.activities.chats.BaseDialogActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755236, "field 'messageSwipeRefreshLayout'");
    target.messageSwipeRefreshLayout = finder.castView(view, 2131755236, "field 'messageSwipeRefreshLayout'");
    view = finder.findRequiredView(source, 2131755237, "field 'messagesRecyclerView'");
    target.messagesRecyclerView = finder.castView(view, 2131755237, "field 'messagesRecyclerView'");
    view = finder.findRequiredView(source, 2131755779, "field 'messageEditText', method 'messageEditTextChanged', and method 'touchMessageEdit'");
    target.messageEditText = finder.castView(view, 2131755779, "field 'messageEditText'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.messageEditTextChanged(p0);
        }
        @Override public void beforeTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void afterTextChanged(
          android.text.Editable p0
        ) {
          
        }
      });
    view.setOnTouchListener(
      new android.view.View.OnTouchListener() {
        @Override public boolean onTouch(
          android.view.View p0,
          android.view.MotionEvent p1
        ) {
          return target.touchMessageEdit();
        }
      });
    view = finder.findRequiredView(source, 2131755777, "field 'attachButton' and method 'attachFile'");
    target.attachButton = finder.castView(view, 2131755777, "field 'attachButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.attachFile(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755778, "field 'recordAudioButton'");
    target.recordAudioButton = finder.castView(view, 2131755778, "field 'recordAudioButton'");
    view = finder.findRequiredView(source, 2131755772, "field 'recordChronometer'");
    target.recordChronometer = finder.castView(view, 2131755772, "field 'recordChronometer'");
    view = finder.findRequiredView(source, 2131755771, "field 'bucketView'");
    target.bucketView = finder.castView(view, 2131755771, "field 'bucketView'");
    view = finder.findRequiredView(source, 2131755770, "field 'audioLayout'");
    target.audioLayout = finder.castView(view, 2131755770, "field 'audioLayout'");
    view = finder.findRequiredView(source, 2131755773, "field 'audioRecordTextView'");
    target.audioRecordTextView = finder.castView(view, 2131755773, "field 'audioRecordTextView'");
    view = finder.findRequiredView(source, 2131755776, "field 'sendButton'");
    target.sendButton = finder.castView(view, 2131755776, "field 'sendButton'");
    view = finder.findRequiredView(source, 2131755781, "field 'smilePanelImageButton' and method 'smilePanelImageButtonClicked'");
    target.smilePanelImageButton = finder.castView(view, 2131755781, "field 'smilePanelImageButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.smilePanelImageButtonClicked();
        }
      });
    view = finder.findRequiredView(source, 2131755782, "field 'snapChatImageButton' and method 'snapChatOnOff'");
    target.snapChatImageButton = finder.castView(view, 2131755782, "field 'snapChatImageButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.snapChatOnOff();
        }
      });
  }

  @Override public void unbind(T target) {
    target.messageSwipeRefreshLayout = null;
    target.messagesRecyclerView = null;
    target.messageEditText = null;
    target.attachButton = null;
    target.recordAudioButton = null;
    target.recordChronometer = null;
    target.bucketView = null;
    target.audioLayout = null;
    target.audioRecordTextView = null;
    target.sendButton = null;
    target.smilePanelImageButton = null;
    target.snapChatImageButton = null;
  }
}