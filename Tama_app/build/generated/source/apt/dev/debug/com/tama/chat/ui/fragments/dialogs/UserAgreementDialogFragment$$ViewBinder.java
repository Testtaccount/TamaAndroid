// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.dialogs;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserAgreementDialogFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.dialogs.UserAgreementDialogFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755338, "method 'openUserAgreement'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.openUserAgreement(p0);
        }
      });
  }

  @Override public void unbind(T target) {
  }
}
