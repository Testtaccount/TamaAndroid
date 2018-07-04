// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.feedback;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FeedbackActivity$$ViewBinder<T extends com.tama.chat.ui.activities.feedback.FeedbackActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755240, "field 'feedbackTypesRadioGroup'");
    target.feedbackTypesRadioGroup = finder.castView(view, 2131755240, "field 'feedbackTypesRadioGroup'");
  }

  @Override public void unbind(T target) {
    target.feedbackTypesRadioGroup = null;
  }
}
