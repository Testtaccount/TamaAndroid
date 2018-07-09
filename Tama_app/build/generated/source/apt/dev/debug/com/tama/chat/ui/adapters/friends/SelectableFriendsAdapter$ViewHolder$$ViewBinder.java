// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.adapters.friends;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SelectableFriendsAdapter$ViewHolder$$ViewBinder<T extends com.tama.chat.ui.adapters.friends.SelectableFriendsAdapter.ViewHolder> extends com.tama.chat.ui.adapters.friends.FriendsAdapter$ViewHolder$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755633, "field 'selectFriendCheckBox'");
    target.selectFriendCheckBox = finder.castView(view, 2131755633, "field 'selectFriendCheckBox'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.selectFriendCheckBox = null;
  }
}
