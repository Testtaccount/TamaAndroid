// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.invitefriends;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class InviteFriendsActivity$$ViewBinder<T extends com.tama.chat.ui.activities.invitefriends.InviteFriendsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755261, "field 'friendsListView'");
    target.friendsListView = finder.castView(view, 2131755261, "field 'friendsListView'");
  }

  @Override public void unbind(T target) {
    target.friendsListView = null;
  }
}
