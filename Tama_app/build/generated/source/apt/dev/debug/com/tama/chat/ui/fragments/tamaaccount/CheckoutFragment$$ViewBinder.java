// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CheckoutFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.tamaaccount.CheckoutFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755491, "field 'senderName'");
    target.senderName = finder.castView(view, 2131755491, "field 'senderName'");
    view = finder.findRequiredView(source, 2131755495, "field 'beneficiaryName'");
    target.beneficiaryName = finder.castView(view, 2131755495, "field 'beneficiaryName'");
    view = finder.findRequiredView(source, 2131755307, "field 'enterPhoneNumberTextFirst'");
    target.enterPhoneNumberTextFirst = finder.castView(view, 2131755307, "field 'enterPhoneNumberTextFirst'");
    view = finder.findRequiredView(source, 2131755313, "field 'enterPhoneNumberTextSecond'");
    target.enterPhoneNumberTextSecond = finder.castView(view, 2131755313, "field 'enterPhoneNumberTextSecond'");
    view = finder.findRequiredView(source, 2131755496, "field 'phoneNumberTextSecond'");
    target.phoneNumberTextSecond = finder.castView(view, 2131755496, "field 'phoneNumberTextSecond'");
    view = finder.findRequiredView(source, 2131755498, "field 'btnConfirm' and method 'OnClickConfirmBtn'");
    target.btnConfirm = finder.castView(view, 2131755498, "field 'btnConfirm'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OnClickConfirmBtn();
        }
      });
    view = finder.findRequiredView(source, 2131755499, "field 'btnRequestOrder' and method 'OnClickRequestOrder'");
    target.btnRequestOrder = finder.castView(view, 2131755499, "field 'btnRequestOrder'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OnClickRequestOrder();
        }
      });
    view = finder.findRequiredView(source, 2131755497, "field 'expressCheckBox' and method 'usePromoCheckBoxCheck'");
    target.expressCheckBox = finder.castView(view, 2131755497, "field 'expressCheckBox'");
    ((android.widget.CompoundButton) view).setOnCheckedChangeListener(
      new android.widget.CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(
          android.widget.CompoundButton p0,
          boolean p1
        ) {
          target.usePromoCheckBoxCheck(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131755306, "field 'countryCodeSpinnerFirst'");
    target.countryCodeSpinnerFirst = finder.castView(view, 2131755306, "field 'countryCodeSpinnerFirst'");
    view = finder.findRequiredView(source, 2131755312, "field 'countryCodeSpinnerSecond'");
    target.countryCodeSpinnerSecond = finder.castView(view, 2131755312, "field 'countryCodeSpinnerSecond'");
    view = finder.findRequiredView(source, 2131755308, "field 'openContactsListFirst' and method 'clickOpenContactsListFirst'");
    target.openContactsListFirst = finder.castView(view, 2131755308, "field 'openContactsListFirst'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOpenContactsListFirst();
        }
      });
    view = finder.findRequiredView(source, 2131755314, "field 'openContactsListSecond' and method 'clickOpenContactsListSecond'");
    target.openContactsListSecond = finder.castView(view, 2131755314, "field 'openContactsListSecond'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOpenContactsListSecond();
        }
      });
    view = finder.findRequiredView(source, 2131755500, "method 'OnClickPayOnline'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OnClickPayOnline();
        }
      });
  }

  @Override public void unbind(T target) {
    target.senderName = null;
    target.beneficiaryName = null;
    target.enterPhoneNumberTextFirst = null;
    target.enterPhoneNumberTextSecond = null;
    target.phoneNumberTextSecond = null;
    target.btnConfirm = null;
    target.btnRequestOrder = null;
    target.expressCheckBox = null;
    target.countryCodeSpinnerFirst = null;
    target.countryCodeSpinnerSecond = null;
    target.openContactsListFirst = null;
    target.openContactsListSecond = null;
  }
}
