// Generated code from Butter Knife. Do not modify!
package com.tama.chat.countryCode;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BaseMultiFlagActivity$$ViewBinder<T extends com.tama.chat.countryCode.BaseMultiFlagActivity> extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755305, "field 'countryCodeSpinnerFirst'");
    target.countryCodeSpinnerFirst = finder.castView(view, 2131755305, "field 'countryCodeSpinnerFirst'");
    view = finder.findRequiredView(source, 2131755311, "field 'countryCodeSpinnerSecond'");
    target.countryCodeSpinnerSecond = finder.castView(view, 2131755311, "field 'countryCodeSpinnerSecond'");
    view = finder.findRequiredView(source, 2131755306, "field 'enterPhoneNumberTextFirst'");
    target.enterPhoneNumberTextFirst = finder.castView(view, 2131755306, "field 'enterPhoneNumberTextFirst'");
    view = finder.findRequiredView(source, 2131755312, "field 'enterPhoneNumberTextSecond'");
    target.enterPhoneNumberTextSecond = finder.castView(view, 2131755312, "field 'enterPhoneNumberTextSecond'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.countryCodeSpinnerFirst = null;
    target.countryCodeSpinnerSecond = null;
    target.enterPhoneNumberTextFirst = null;
    target.enterPhoneNumberTextSecond = null;
  }
}
