// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewGroupDialogActivity$$ViewBinder<T extends com.tama.chat.ui.activities.chats.NewGroupDialogActivity> extends com.tama.chat.ui.activities.others.BaseFriendsListActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755332, "field 'membersEditText'");
    target.membersEditText = finder.castView(view, 2131755332, "field 'membersEditText'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.membersEditText = null;
  }
}
