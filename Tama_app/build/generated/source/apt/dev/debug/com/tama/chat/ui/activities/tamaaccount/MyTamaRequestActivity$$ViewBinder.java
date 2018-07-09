// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MyTamaRequestActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.MyTamaRequestActivity> extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755316, "field 'btnMyTamaTopUp' and method 'startMytamaTopUpRqt'");
    target.btnMyTamaTopUp = finder.castView(view, 2131755316, "field 'btnMyTamaTopUp'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startMytamaTopUpRqt();
        }
      });
    view = finder.findRequiredView(source, 2131755318, "field 'btnMobileTopUp' and method 'startMyTamaMobileTopUpRequestActivity'");
    target.btnMobileTopUp = finder.castView(view, 2131755318, "field 'btnMobileTopUp'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startMyTamaMobileTopUpRequestActivity();
        }
      });
    view = finder.findRequiredView(source, 2131755319, "field 'btnPayTamaExpress' and method 'startPayTamaExpressRequestActivity'");
    target.btnPayTamaExpress = finder.castView(view, 2131755319, "field 'btnPayTamaExpress'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startPayTamaExpressRequestActivity();
        }
      });
    view = finder.findRequiredView(source, 2131755320, "field 'btnIncomingRqt' and method 'startIncomingRequestActivity'");
    target.btnIncomingRqt = finder.castView(view, 2131755320, "field 'btnIncomingRqt'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startIncomingRequestActivity();
        }
      });
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.btnMyTamaTopUp = null;
    target.btnMobileTopUp = null;
    target.btnPayTamaExpress = null;
    target.btnIncomingRqt = null;
  }
}
