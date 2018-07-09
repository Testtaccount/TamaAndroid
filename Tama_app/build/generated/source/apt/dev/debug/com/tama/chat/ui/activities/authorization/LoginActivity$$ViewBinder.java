// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.authorization;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewBinder<T extends com.tama.chat.ui.activities.authorization.LoginActivity> extends com.tama.chat.ui.activities.authorization.BaseAuthActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755266, "field 'rememberMeSwitch' and method 'rememberMeCheckedChanged'");
    target.rememberMeSwitch = finder.castView(view, 2131755266, "field 'rememberMeSwitch'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.rememberMeCheckedChanged(p1);
        }
      });
    view = finder.findRequiredView(source, 2131755267, "method 'loginQB'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.loginQB(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755268, "method 'loginFB'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.loginFB(p0);
        }
      });
    view = finder.findRequiredView(source, 2131755269, "method 'forgotPassword'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.forgotPassword(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.rememberMeSwitch = null;
  }
}
