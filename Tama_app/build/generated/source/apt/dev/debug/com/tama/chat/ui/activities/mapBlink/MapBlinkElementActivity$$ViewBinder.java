// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.mapBlink;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MapBlinkElementActivity$$ViewBinder<T extends com.tama.chat.ui.activities.mapBlink.MapBlinkElementActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755284, "field 'ratingTextView'");
    target.ratingTextView = finder.castView(view, 2131755284, "field 'ratingTextView'");
    view = finder.findRequiredView(source, 2131755286, "field 'descriptionText'");
    target.descriptionText = finder.castView(view, 2131755286, "field 'descriptionText'");
    view = finder.findRequiredView(source, 2131755287, "field 'addressText'");
    target.addressText = finder.castView(view, 2131755287, "field 'addressText'");
    view = finder.findRequiredView(source, 2131755288, "field 'openNowText'");
    target.openNowText = finder.castView(view, 2131755288, "field 'openNowText'");
    view = finder.findRequiredView(source, 2131755289, "field 'weekDayText'");
    target.weekDayText = finder.castView(view, 2131755289, "field 'weekDayText'");
    view = finder.findRequiredView(source, 2131755282, "field 'scrollingLinLayout'");
    target.scrollingLinLayout = finder.castView(view, 2131755282, "field 'scrollingLinLayout'");
    view = finder.findRequiredView(source, 2131755283, "field 'firstPhoto'");
    target.firstPhoto = finder.castView(view, 2131755283, "field 'firstPhoto'");
    view = finder.findRequiredView(source, 2131755285, "field 'ratingView'");
    target.ratingView = finder.castView(view, 2131755285, "field 'ratingView'");
    view = finder.findRequiredView(source, 2131755291, "field 'directionButton' and method 'sendRequest'");
    target.directionButton = finder.castView(view, 2131755291, "field 'directionButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendRequest();
        }
      });
    view = finder.findRequiredView(source, 2131755290, "field 'getOfferCode'");
    target.getOfferCode = finder.castView(view, 2131755290, "field 'getOfferCode'");
  }

  @Override public void unbind(T target) {
    target.ratingTextView = null;
    target.descriptionText = null;
    target.addressText = null;
    target.openNowText = null;
    target.weekDayText = null;
    target.scrollingLinLayout = null;
    target.firstPhoto = null;
    target.ratingView = null;
    target.directionButton = null;
    target.getOfferCode = null;
  }
}
