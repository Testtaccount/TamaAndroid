// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CreateGroupDialogActivity$$ViewBinder<T extends com.tama.chat.ui.activities.chats.CreateGroupDialogActivity> extends com.tama.chat.ui.activities.others.BaseFriendsListActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755232, "field 'photoImageView' and method 'selectPhoto'");
    target.photoImageView = finder.castView(view, 2131755232, "field 'photoImageView'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectPhoto(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755233, "field 'groupNameEditText'");
    target.groupNameEditText = finder.castView(view, 2131755233, "field 'groupNameEditText'");
    view = finder.findRequiredView(source, 2131755234, "field 'participantsCountTextView'");
    target.participantsCountTextView = finder.castView(view, 2131755234, "field 'participantsCountTextView'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.photoImageView = null;
    target.groupNameEditText = null;
    target.participantsCountTextView = null;
  }
}
