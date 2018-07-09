// Generated code from Butter Knife. Do not modify!
package com.tama.chat.countryCode;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BaseMultiFlagFragment$$ViewBinder<T extends com.tama.chat.countryCode.BaseMultiFlagFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755301, "field 'countryCodeSpinnerFirst'");
    target.countryCodeSpinnerFirst = finder.castView(view, 2131755301, "field 'countryCodeSpinnerFirst'");
    view = finder.findRequiredView(source, 2131755307, "field 'countryCodeSpinnerSecond'");
    target.countryCodeSpinnerSecond = finder.castView(view, 2131755307, "field 'countryCodeSpinnerSecond'");
    view = finder.findRequiredView(source, 2131755302, "field 'enterPhoneNumberTextFirst'");
    target.enterPhoneNumberTextFirst = finder.castView(view, 2131755302, "field 'enterPhoneNumberTextFirst'");
    view = finder.findRequiredView(source, 2131755308, "field 'enterPhoneNumberTextSecond'");
    target.enterPhoneNumberTextSecond = finder.castView(view, 2131755308, "field 'enterPhoneNumberTextSecond'");
  }

  @Override public void unbind(T target) {
    target.countryCodeSpinnerFirst = null;
    target.countryCodeSpinnerSecond = null;
    target.enterPhoneNumberTextFirst = null;
    target.enterPhoneNumberTextSecond = null;
  }
}
