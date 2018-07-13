// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ShoppingCartFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.tamaaccount.ShoppingCartFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755538, "field 'grandTotalView'");
    target.grandTotalView = finder.castView(view, 2131755538, "field 'grandTotalView'");
    view = finder.findRequiredView(source, 2131755536, "field 'productListView'");
    target.productListView = finder.castView(view, 2131755536, "field 'productListView'");
    view = finder.findRequiredView(source, 2131755539, "field 'continueShoppingBtn' and method 'OnClickContinueShopping'");
    target.continueShoppingBtn = finder.castView(view, 2131755539, "field 'continueShoppingBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OnClickContinueShopping();
        }
      });
    view = finder.findRequiredView(source, 2131755540, "field 'checkoutBtn' and method 'onClickCheckOutBtn'");
    target.checkoutBtn = finder.castView(view, 2131755540, "field 'checkoutBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickCheckOutBtn();
        }
      });
  }

  @Override public void unbind(T target) {
    target.grandTotalView = null;
    target.productListView = null;
    target.continueShoppingBtn = null;
    target.checkoutBtn = null;
  }
}
