// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.about;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AboutActivity$$ViewBinder<T extends com.tama.chat.ui.activities.about.AboutActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755206, "field 'appVersionTextView'");
    target.appVersionTextView = finder.castView(view, 2131755206, "field 'appVersionTextView'");
    view = finder.findRequiredView(source, 2131755208, "method 'openUserAgreement'");
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
    target.appVersionTextView = null;
  }
}
