// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.agreements;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserAgreementActivity$$ViewBinder<T extends com.tama.chat.ui.activities.agreements.UserAgreementActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755361, "field 'userAgreementWebView'");
    target.userAgreementWebView = finder.castView(view, 2131755361, "field 'userAgreementWebView'");
  }

  @Override public void unbind(T target) {
    target.userAgreementWebView = null;
  }
}
