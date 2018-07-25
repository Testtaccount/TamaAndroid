// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.adapters.search;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GlobalSearchAdapter$ViewHolder$$ViewBinder<T extends com.tama.chat.ui.adapters.search.GlobalSearchAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755246, "field 'avatarImageView'");
    target.avatarImageView = finder.castView(view, 2131755246, "field 'avatarImageView'");
    view = finder.findRequiredView(source, 2131755247, "field 'fullNameTextView'");
    target.fullNameTextView = finder.castView(view, 2131755247, "field 'fullNameTextView'");
    view = finder.findRequiredView(source, 2131755637, "field 'statusTextView'");
    target.statusTextView = finder.castView(view, 2131755637, "field 'statusTextView'");
    view = finder.findRequiredView(source, 2131755636, "field 'addFriendImageView'");
    target.addFriendImageView = finder.castView(view, 2131755636, "field 'addFriendImageView'");
  }

  @Override public void unbind(T target) {
    target.avatarImageView = null;
    target.fullNameTextView = null;
    target.statusTextView = null;
    target.addFriendImageView = null;
  }
}
