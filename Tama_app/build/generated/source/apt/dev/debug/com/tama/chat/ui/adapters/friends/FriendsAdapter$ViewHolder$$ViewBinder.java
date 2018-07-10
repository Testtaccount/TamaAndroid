// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.adapters.friends;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FriendsAdapter$ViewHolder$$ViewBinder<T extends com.tama.chat.ui.adapters.friends.FriendsAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755630, "field 'firstLatterTextView'");
    target.firstLatterTextView = finder.castView(view, 2131755630, "field 'firstLatterTextView'");
    view = finder.findRequiredView(source, 2131755241, "field 'avatarImageView'");
    target.avatarImageView = finder.castView(view, 2131755241, "field 'avatarImageView'");
    view = finder.findRequiredView(source, 2131755242, "field 'nameTextView'");
    target.nameTextView = finder.castView(view, 2131755242, "field 'nameTextView'");
    view = finder.findRequiredView(source, 2131755631, "field 'labelTextView'");
    target.labelTextView = finder.castView(view, 2131755631, "field 'labelTextView'");
  }

  @Override public void unbind(T target) {
    target.firstLatterTextView = null;
    target.avatarImageView = null;
    target.nameTextView = null;
    target.labelTextView = null;
  }
}
