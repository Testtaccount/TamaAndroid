// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TamaAccountBaseActivity$$ViewBinder<T extends com.tama.chat.ui.activities.tamaaccount.TamaAccountBaseActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755223, "field 'toolbarTamaView'");
    target.toolbarTamaView = finder.castView(view, 2131755223, "field 'toolbarTamaView'");
    view = finder.findRequiredView(source, 2131755735, "field 'tamaToolbarUserIcon'");
    target.tamaToolbarUserIcon = finder.castView(view, 2131755735, "field 'tamaToolbarUserIcon'");
    view = finder.findRequiredView(source, 2131755734, "field 'tamaToolbarTitle'");
    target.tamaToolbarTitle = finder.castView(view, 2131755734, "field 'tamaToolbarTitle'");
    view = finder.findRequiredView(source, 2131755736, "field 'tamaToolbarSubtitle'");
    target.tamaToolbarSubtitle = finder.castView(view, 2131755736, "field 'tamaToolbarSubtitle'");
    view = finder.findRequiredView(source, 2131755737, "field 'shoppingButton' and method 'onClickOnShoppingCart'");
    target.shoppingButton = finder.castView(view, 2131755737, "field 'shoppingButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickOnShoppingCart();
        }
      });
    view = finder.findRequiredView(source, 2131755738, "field 'productCount'");
    target.productCount = finder.castView(view, 2131755738, "field 'productCount'");
  }

  @Override public void unbind(T target) {
    target.toolbarTamaView = null;
    target.tamaToolbarUserIcon = null;
    target.tamaToolbarTitle = null;
    target.tamaToolbarSubtitle = null;
    target.shoppingButton = null;
    target.productCount = null;
  }
}
