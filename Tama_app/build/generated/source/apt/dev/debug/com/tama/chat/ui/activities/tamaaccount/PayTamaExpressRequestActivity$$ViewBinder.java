// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PayTamaExpressRequestActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.PayTamaExpressRequestActivity> extends com.tama.chat.countryCode.BaseFlagActivity$$ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    super.bind(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131755315, "field 'btnSendRequest' and method 'clickOnSendRequestButton'");
    target.btnSendRequest = finder.castView(view, 2131755315, "field 'btnSendRequest'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOnSendRequestButton();
        }
      });
    view = finder.findRequiredView(source, 2131755329, "field 'tamaExpressText'");
    target.tamaExpressText = finder.castView(view, 2131755329, "field 'tamaExpressText'");
    view = finder.findRequiredView(source, 2131755326, "field 'phoneErrorText'");
    target.phoneErrorText = finder.castView(view, 2131755326, "field 'phoneErrorText'");
    view = finder.findRequiredView(source, 2131755330, "field 'tamaExpressErrorText'");
    target.tamaExpressErrorText = finder.castView(view, 2131755330, "field 'tamaExpressErrorText'");
    view = finder.findRequiredView(source, 2131755181, "field 'checkBox'");
    target.checkBox = finder.castView(view, 2131755181, "field 'checkBox'");
    view = finder.findRequiredView(source, 2131755325, "field 'openContactsList' and method 'clickOpenContactsList'");
    target.openContactsList = finder.castView(view, 2131755325, "field 'openContactsList'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clickOpenContactsList();
        }
      });
    view = finder.findRequiredView(source, 2131755298, "field 'bodyLayout'");
    target.bodyLayout = finder.castView(view, 2131755298, "field 'bodyLayout'");
  }

  @Override public void unbind(T target) {
    super.unbind(target);

    target.btnSendRequest = null;
    target.tamaExpressText = null;
    target.phoneErrorText = null;
    target.tamaExpressErrorText = null;
    target.checkBox = null;
    target.openContactsList = null;
    target.bodyLayout = null;
  }
}
