// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MyTamaAccountFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.tamaaccount.MyTamaAccountFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755526, "field 'tamaAccountBalance'");
    target.tamaAccountBalance = finder.castView(view, 2131755526, "field 'tamaAccountBalance'");
    view = finder.findRequiredView(source, 2131755527, "field 'promotionTxtTv'");
    target.promotionTxtTv = finder.castView(view, 2131755527, "field 'promotionTxtTv'");
    view = finder.findRequiredView(source, 2131755528, "field 'buttonTopupMyAcc' and method 'startTopupMyAccountActivity'");
    target.buttonTopupMyAcc = finder.castView(view, 2131755528, "field 'buttonTopupMyAcc'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startTopupMyAccountActivity();
        }
      });
    view = finder.findRequiredView(source, 2131755531, "field 'buttonTamaExpress' and method 'startTamaExpressAcitvity'");
    target.buttonTamaExpress = finder.castView(view, 2131755531, "field 'buttonTamaExpress'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startTamaExpressAcitvity();
        }
      });
    view = finder.findRequiredView(source, 2131755532, "field 'buttonTamaTopUp' and method 'startTamaTopUpActivity'");
    target.buttonTamaTopUp = finder.castView(view, 2131755532, "field 'buttonTamaTopUp'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startTamaTopUpActivity();
        }
      });
    view = finder.findRequiredView(source, 2131755533, "field 'buttonTamaHistory' and method 'startTamaHistoryActivity'");
    target.buttonTamaHistory = finder.castView(view, 2131755533, "field 'buttonTamaHistory'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startTamaHistoryActivity();
        }
      });
    view = finder.findRequiredView(source, 2131755540, "field 'buttonSendCredit' and method 'startTamaBalanceTransfer'");
    target.buttonSendCredit = finder.castView(view, 2131755540, "field 'buttonSendCredit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startTamaBalanceTransfer();
        }
      });
    view = finder.findRequiredView(source, 2131755539, "field 'buttonTamaRequest' and method 'startRequestActivity'");
    target.buttonTamaRequest = finder.castView(view, 2131755539, "field 'buttonTamaRequest'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startRequestActivity();
        }
      });
    view = finder.findRequiredView(source, 2131755538, "field 'buttonTamaContactUs' and method 'startTamaContactUs'");
    target.buttonTamaContactUs = finder.castView(view, 2131755538, "field 'buttonTamaContactUs'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startTamaContactUs();
        }
      });
    view = finder.findRequiredView(source, 2131755537, "method 'startAboutUsActivity'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startAboutUsActivity();
        }
      });
  }

  @Override public void unbind(T target) {
    target.tamaAccountBalance = null;
    target.promotionTxtTv = null;
    target.buttonTopupMyAcc = null;
    target.buttonTamaExpress = null;
    target.buttonTamaTopUp = null;
    target.buttonTamaHistory = null;
    target.buttonSendCredit = null;
    target.buttonTamaRequest = null;
    target.buttonTamaContactUs = null;
  }
}
