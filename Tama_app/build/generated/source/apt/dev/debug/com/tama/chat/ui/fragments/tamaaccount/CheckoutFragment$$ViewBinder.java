// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CheckoutFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.tamaaccount.CheckoutFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755457, "field 'btnConfirm' and method 'OnClickConfirmBtn'");
    target.btnConfirm = finder.castView(view, 2131755457, "field 'btnConfirm'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OnClickConfirmBtn();
        }
      });
    view = finder.findRequiredView(source, 2131755458, "field 'btnRequestOrder' and method 'OnClickRequestOrder'");
    target.btnRequestOrder = finder.castView(view, 2131755458, "field 'btnRequestOrder'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OnClickRequestOrder();
        }
      });
    view = finder.findRequiredView(source, 2131755181, "field 'checkBox'");
    target.checkBox = finder.castView(view, 2131755181, "field 'checkBox'");
    view = finder.findRequiredView(source, 2131755303, "field 'countryCodeSpinnerFirst'");
    target.countryCodeSpinnerFirst = finder.castView(view, 2131755303, "field 'countryCodeSpinnerFirst'");
    view = finder.findRequiredView(source, 2131755309, "field 'countryCodeSpinnerSecond'");
    target.countryCodeSpinnerSecond = finder.castView(view, 2131755309, "field 'countryCodeSpinnerSecond'");
    view = finder.findRequiredView(source, 2131755304, "field 'enterPhoneNumberTextFirst'");
    target.enterPhoneNumberTextFirst = finder.castView(view, 2131755304, "field 'enterPhoneNumberTextFirst'");
    view = finder.findRequiredView(source, 2131755310, "field 'enterPhoneNumberTextSecond'");
    target.enterPhoneNumberTextSecond = finder.castView(view, 2131755310, "field 'enterPhoneNumberTextSecond'");
    view = finder.findRequiredView(source, 2131755456, "field 'phoneNumberTextSecond'");
    target.phoneNumberTextSecond = finder.castView(view, 2131755456, "field 'phoneNumberTextSecond'");
    view = finder.findRequiredView(source, 2131755451, "field 'senderName'");
    target.senderName = finder.castView(view, 2131755451, "field 'senderName'");
    view = finder.findRequiredView(source, 2131755455, "field 'beneficiaryName'");
    target.beneficiaryName = finder.castView(view, 2131755455, "field 'beneficiaryName'");
    view = finder.findRequiredView(source, 2131755305, "field 'openContactsListFirst' and method 'clickOpenContactsListFirst'");
    target.openContactsListFirst = finder.castView(view, 2131755305, "field 'openContactsListFirst'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOpenContactsListFirst();
        }
      });
    view = finder.findRequiredView(source, 2131755311, "field 'openContactsListSecond' and method 'clickOpenContactsListSecond'");
    target.openContactsListSecond = finder.castView(view, 2131755311, "field 'openContactsListSecond'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOpenContactsListSecond();
        }
      });
  }

  @Override public void unbind(T target) {
    target.btnConfirm = null;
    target.btnRequestOrder = null;
    target.checkBox = null;
    target.countryCodeSpinnerFirst = null;
    target.countryCodeSpinnerSecond = null;
    target.enterPhoneNumberTextFirst = null;
    target.enterPhoneNumberTextSecond = null;
    target.phoneNumberTextSecond = null;
    target.senderName = null;
    target.beneficiaryName = null;
    target.openContactsListFirst = null;
    target.openContactsListSecond = null;
  }
}
