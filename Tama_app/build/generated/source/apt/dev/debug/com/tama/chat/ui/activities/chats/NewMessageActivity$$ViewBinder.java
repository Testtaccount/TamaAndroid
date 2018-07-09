// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewMessageActivity$$ViewBinder<T extends com.tama.chat.ui.activities.chats.NewMessageActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755230, "field 'friendsRecyclerView'");
    target.friendsRecyclerView = finder.castView(view, 2131755230, "field 'friendsRecyclerView'");
  }

  @Override public void unbind(T target) {
    target.friendsRecyclerView = null;
  }
}
