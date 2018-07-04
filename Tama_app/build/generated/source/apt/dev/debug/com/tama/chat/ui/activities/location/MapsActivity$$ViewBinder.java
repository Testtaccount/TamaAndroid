// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.location;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MapsActivity$$ViewBinder<T extends com.tama.chat.ui.activities.location.MapsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755279, "field 'sendTextView'");
    target.sendTextView = finder.castView(view, 2131755279, "field 'sendTextView'");
    view = finder.findRequiredView(source, 2131755277, "field 'sendLocationPanel' and method 'sendLocationButtonClicked'");
    target.sendLocationPanel = finder.castView(view, 2131755277, "field 'sendLocationPanel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendLocationButtonClicked();
        }
      });
  }

  @Override public void unbind(T target) {
    target.sendTextView = null;
    target.sendLocationPanel = null;
  }
}
