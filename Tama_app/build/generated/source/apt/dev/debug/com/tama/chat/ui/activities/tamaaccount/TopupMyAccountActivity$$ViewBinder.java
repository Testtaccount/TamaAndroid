// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class TopupMyAccountActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.TopupMyAccountActivity> extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755356, "field 'topupVoucherText'");
    target.topupVoucherText = finder.castView(view, 2131755356, "field 'topupVoucherText'");
    view = finder.findRequiredView(source, 2131755343, "field 'topupErrorText'");
    target.topupErrorText = finder.castView(view, 2131755343, "field 'topupErrorText'");
    view = finder.findRequiredView(source, 2131755357, "field 'topupButton' and method 'sendTopupRequest'");
    target.topupButton = finder.castView(view, 2131755357, "field 'topupButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendTopupRequest();
        }
      });
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.topupVoucherText = null;
    target.topupErrorText = null;
    target.topupButton = null;
  }
}
