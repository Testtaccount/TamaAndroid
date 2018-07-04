// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.adapters.mapBlink;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MapBlinkElementAdapterTwoo$MapBlinkElementViewHolder$$ViewBinder<T extends com.tama.chat.ui.adapters.mapBlink.MapBlinkElementAdapterTwoo.MapBlinkElementViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755631, "field 'mapBlinkImageView'");
    target.mapBlinkImageView = finder.castView(view, 2131755631, "field 'mapBlinkImageView'");
    view = finder.findRequiredView(source, 2131755630, "field 'nameTextView'");
    target.nameTextView = finder.castView(view, 2131755630, "field 'nameTextView'");
    view = finder.findRequiredView(source, 2131755632, "field 'mapBlinkRatingText'");
    target.mapBlinkRatingText = finder.castView(view, 2131755632, "field 'mapBlinkRatingText'");
    view = finder.findRequiredView(source, 2131755634, "field 'mapBlinkDescription'");
    target.mapBlinkDescription = finder.castView(view, 2131755634, "field 'mapBlinkDescription'");
    view = finder.findRequiredView(source, 2131755635, "field 'mapBlinkIsOpen'");
    target.mapBlinkIsOpen = finder.castView(view, 2131755635, "field 'mapBlinkIsOpen'");
    view = finder.findRequiredView(source, 2131755636, "field 'mapBlink'");
    target.mapBlink = finder.castView(view, 2131755636, "field 'mapBlink'");
    view = finder.findRequiredView(source, 2131755633, "field 'mapBlinkRatingBar'");
    target.mapBlinkRatingBar = finder.castView(view, 2131755633, "field 'mapBlinkRatingBar'");
  }

  @Override public void unbind(T target) {
    target.mapBlinkImageView = null;
    target.nameTextView = null;
    target.mapBlinkRatingText = null;
    target.mapBlinkDescription = null;
    target.mapBlinkIsOpen = null;
    target.mapBlink = null;
    target.mapBlinkRatingBar = null;
  }
}
