// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MyTamaMobileTopUpRequestActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.MyTamaMobileTopUpRequestActivity> extends com.tama.chat.countryCode.BaseMultiFlagActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755317, "field 'amountNumberLayout' and method 'clickOnAmountLayout'");
    target.amountNumberLayout = finder.castView(view, 2131755317, "field 'amountNumberLayout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOnAmountLayout();
        }
      });
    view = finder.findRequiredView(source, 2131755303, "field 'bodyLayout'");
    target.bodyLayout = finder.castView(view, 2131755303, "field 'bodyLayout'");
    view = finder.findRequiredView(source, 2131755320, "field 'btnSendRequest' and method 'clickOnSendRequestButton'");
    target.btnSendRequest = finder.castView(view, 2131755320, "field 'btnSendRequest'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOnSendRequestButton();
        }
      });
    view = finder.findRequiredView(source, 2131755318, "field 'amountText'");
    target.amountText = finder.castView(view, 2131755318, "field 'amountText'");
    view = finder.findRequiredView(source, 2131755309, "field 'phoneErrorTextFirst'");
    target.phoneErrorTextFirst = finder.castView(view, 2131755309, "field 'phoneErrorTextFirst'");
    view = finder.findRequiredView(source, 2131755315, "field 'phoneErrorTextSecond'");
    target.phoneErrorTextSecond = finder.castView(view, 2131755315, "field 'phoneErrorTextSecond'");
    view = finder.findRequiredView(source, 2131755319, "field 'amountErrorText'");
    target.amountErrorText = finder.castView(view, 2131755319, "field 'amountErrorText'");
    view = finder.findRequiredView(source, 2131755181, "field 'checkBox'");
    target.checkBox = finder.castView(view, 2131755181, "field 'checkBox'");
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
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.amountNumberLayout = null;
    target.bodyLayout = null;
    target.btnSendRequest = null;
    target.amountText = null;
    target.phoneErrorTextFirst = null;
    target.phoneErrorTextSecond = null;
    target.amountErrorText = null;
    target.checkBox = null;
    target.openContactsListFirst = null;
    target.openContactsListSecond = null;
  }
}
