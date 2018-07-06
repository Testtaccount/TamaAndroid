// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ContactsListFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.chats.ContactsListFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755232, "field 'friendsRecyclerView'");
    target.friendsRecyclerView = finder.castView(view, 2131755232, "field 'friendsRecyclerView'");
  }

  @Override public void unbind(T target) {
    target.friendsRecyclerView = null;
  }
}
