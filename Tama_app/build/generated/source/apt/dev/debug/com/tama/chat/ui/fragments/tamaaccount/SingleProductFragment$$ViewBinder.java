// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.fragments.tamaaccount;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SingleProductFragment$$ViewBinder<T extends com.tama.chat.ui.fragments.tamaaccount.SingleProductFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755522, "field 'itemProductsImage'");
    target.itemProductsImage = finder.castView(view, 2131755522, "field 'itemProductsImage'");
    view = finder.findRequiredView(source, 2131755521, "field 'productNameText'");
    target.productNameText = finder.castView(view, 2131755521, "field 'productNameText'");
    view = finder.findRequiredView(source, 2131755525, "field 'productDescription'");
    target.productDescription = finder.castView(view, 2131755525, "field 'productDescription'");
    view = finder.findRequiredView(source, 2131755524, "field 'productPrice'");
    target.productPrice = finder.castView(view, 2131755524, "field 'productPrice'");
    view = finder.findRequiredView(source, 2131755526, "field 'btnAddToCart' and method 'OnClickAddToCart'");
    target.btnAddToCart = finder.castView(view, 2131755526, "field 'btnAddToCart'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OnClickAddToCart();
        }
      });
    view = finder.findRequiredView(source, 2131755527, "field 'btnBack' and method 'OnClickBack'");
    target.btnBack = finder.castView(view, 2131755527, "field 'btnBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OnClickBack();
        }
      });
  }

  @Override public void unbind(T target) {
    target.itemProductsImage = null;
    target.productNameText = null;
    target.productDescription = null;
    target.productPrice = null;
    target.btnAddToCart = null;
    target.btnBack = null;
  }
}