// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.others;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PreviewImageActivity$$ViewBinder<T extends com.tama.chat.ui.activities.others.PreviewImageActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755336, "field 'imageTouchImageView'");
    target.imageTouchImageView = finder.castView(view, 2131755336, "field 'imageTouchImageView'");
  }

  @Override public void unbind(T target) {
    target.imageTouchImageView = null;
  }
}
