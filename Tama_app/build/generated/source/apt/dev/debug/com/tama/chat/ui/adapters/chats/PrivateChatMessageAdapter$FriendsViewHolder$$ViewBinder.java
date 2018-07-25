// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.adapters.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PrivateChatMessageAdapter$FriendsViewHolder$$ViewBinder<T extends com.tama.chat.ui.adapters.chats.PrivateChatMessageAdapter.FriendsViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131755642, null);
    target.messageTextView = finder.castView(view, 2131755642, "field 'messageTextView'");
    view = finder.findOptionalView(source, 2131755641, null);
    target.timeTextMessageTextView = finder.castView(view, 2131755641, "field 'timeTextMessageTextView'");
    view = finder.findOptionalView(source, 2131755645, null);
    target.acceptFriendImageView = finder.castView(view, 2131755645, "field 'acceptFriendImageView'");
    view = finder.findOptionalView(source, 2131755644, null);
    target.dividerView = view;
    view = finder.findOptionalView(source, 2131755643, null);
    target.rejectFriendImageView = finder.castView(view, 2131755643, "field 'rejectFriendImageView'");
  }

  @Override public void unbind(T target) {
    target.messageTextView = null;
    target.timeTextMessageTextView = null;
    target.acceptFriendImageView = null;
    target.dividerView = null;
    target.rejectFriendImageView = null;
  }
}
