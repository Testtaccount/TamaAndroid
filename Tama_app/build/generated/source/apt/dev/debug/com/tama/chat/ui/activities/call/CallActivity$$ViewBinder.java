// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.call;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CallActivity$$ViewBinder<T extends com.tama.chat.ui.activities.call.CallActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755757, "field 'timerChronometer'");
    target.timerChronometer = finder.castView(view, 2131755757, "field 'timerChronometer'");
  }

  @Override public void unbind(T target) {
    target.timerChronometer = null;
  }
}
