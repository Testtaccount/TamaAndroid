// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class TamaTransferActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.TamaTransferActivity> extends com.tama.chat.countryCode.BaseFlagActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755314, "field 'amountNumberLayout' and method 'clickOnAmountLayout'");
    target.amountNumberLayout = finder.castView(view, 2131755314, "field 'amountNumberLayout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOnAmountLayout();
        }
      });
    view = finder.findRequiredView(source, 2131755300, "field 'bodyLayout'");
    target.bodyLayout = finder.castView(view, 2131755300, "field 'bodyLayout'");
    view = finder.findRequiredView(source, 2131755352, "field 'transferButton' and method 'clickOnTransferButton'");
    target.transferButton = finder.castView(view, 2131755352, "field 'transferButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOnTransferButton();
        }
      });
    view = finder.findRequiredView(source, 2131755181, "field 'checkBox'");
    target.checkBox = finder.castView(view, 2131755181, "field 'checkBox'");
    view = finder.findRequiredView(source, 2131755315, "field 'amountText'");
    target.amountText = finder.castView(view, 2131755315, "field 'amountText'");
    view = finder.findRequiredView(source, 2131755316, "field 'amountErrorText'");
    target.amountErrorText = finder.castView(view, 2131755316, "field 'amountErrorText'");
    view = finder.findRequiredView(source, 2131755328, "field 'phoneErrorText'");
    target.phoneErrorText = finder.castView(view, 2131755328, "field 'phoneErrorText'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.amountNumberLayout = null;
    target.bodyLayout = null;
    target.transferButton = null;
    target.checkBox = null;
    target.amountText = null;
    target.amountErrorText = null;
    target.phoneErrorText = null;
  }
}
