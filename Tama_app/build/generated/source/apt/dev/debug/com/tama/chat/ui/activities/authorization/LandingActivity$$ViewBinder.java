// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.authorization;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LandingActivity$$ViewBinder<T extends com.tama.chat.ui.activities.authorization.LandingActivity> extends com.tama.chat.ui.activities.authorization.BaseAuthActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755206, "field 'appVersionTextView'");
    target.appVersionTextView = finder.castView(view, 2131755206, "field 'appVersionTextView'");
    view = finder.findRequiredView(source, 2131755267, "field 'phoneNumberConnectButton' and method 'phoneNumberConnect'");
    target.phoneNumberConnectButton = finder.castView(view, 2131755267, "field 'phoneNumberConnectButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.phoneNumberConnect(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.appVersionTextView = null;
    target.phoneNumberConnectButton = null;
  }
}
