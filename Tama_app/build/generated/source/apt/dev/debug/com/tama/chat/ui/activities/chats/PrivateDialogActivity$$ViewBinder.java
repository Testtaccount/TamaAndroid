// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.chats;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PrivateDialogActivity$$ViewBinder<T extends com.tama.chat.ui.activities.chats.PrivateDialogActivity> extends com.tama.chat.ui.activities.chats.BaseDialogActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755753, "method 'openProfile'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.openProfile(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    super.unbind(target);

  }
}
