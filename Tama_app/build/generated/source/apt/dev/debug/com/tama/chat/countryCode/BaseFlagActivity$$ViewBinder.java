// Generated code from Butter Knife. Do not modify!
package com.tama.chat.countryCode;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BaseFlagActivity$$ViewBinder<T extends com.tama.chat.countryCode.BaseFlagActivity> extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755327, "field 'countryCodeSpinner'");
    target.countryCodeSpinner = finder.castView(view, 2131755327, "field 'countryCodeSpinner'");
    view = finder.findRequiredView(source, 2131755328, "field 'enterPhoneNumberText'");
    target.enterPhoneNumberText = finder.castView(view, 2131755328, "field 'enterPhoneNumberText'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.countryCodeSpinner = null;
    target.enterPhoneNumberText = null;
  }
}
