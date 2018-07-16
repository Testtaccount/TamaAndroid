// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.adapters.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PrivateChatMessageAdapter$FriendsViewHolder$$ViewBinder<T extends com.tama.chat.ui.adapters.chats.PrivateChatMessageAdapter.FriendsViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131755632, null);
    target.messageTextView = finder.castView(view, 2131755632, "field 'messageTextView'");
    view = finder.findOptionalView(source, 2131755631, null);
    target.timeTextMessageTextView = finder.castView(view, 2131755631, "field 'timeTextMessageTextView'");
    view = finder.findOptionalView(source, 2131755635, null);
    target.acceptFriendImageView = finder.castView(view, 2131755635, "field 'acceptFriendImageView'");
    view = finder.findOptionalView(source, 2131755634, null);
    target.dividerView = view;
    view = finder.findOptionalView(source, 2131755633, null);
    target.rejectFriendImageView = finder.castView(view, 2131755633, "field 'rejectFriendImageView'");
  }

  @Override public void unbind(T target) {
    target.messageTextView = null;
    target.timeTextMessageTextView = null;
    target.acceptFriendImageView = null;
    target.dividerView = null;
    target.rejectFriendImageView = null;
  }
}
