// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.adapters.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CustomMessagesAdapter$LeftTextMessageViewHolder$$ViewBinder<T extends com.tama.chat.ui.adapters.chats.CustomMessagesAdapter.LeftTextMessageViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131755625, null);
    target.messageTimerView = finder.castView(view, 2131755625, "field 'messageTimerView'");
  }

  @Override public void unbind(T target) {
    target.messageTimerView = null;
  }
}