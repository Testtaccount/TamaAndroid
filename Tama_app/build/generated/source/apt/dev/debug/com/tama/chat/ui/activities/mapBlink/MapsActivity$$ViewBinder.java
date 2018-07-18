// Generated code from Butter Knife. Do not modify!
package com.tama.chat.ui.activities.mapBlink;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MapsActivity$$ViewBinder<T extends com.tama.chat.ui.activities.mapBlink.MapsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131755295, "field 'tvDistance'");
    target.tvDistance = finder.castView(view, 2131755295, "field 'tvDistance'");
    view = finder.findRequiredView(source, 2131755296, "field 'tvDuration'");
    target.tvDuration = finder.castView(view, 2131755296, "field 'tvDuration'");
    view = finder.findRequiredView(source, 2131755297, "field 'mapTypeNormal'");
    target.mapTypeNormal = finder.castView(view, 2131755297, "field 'mapTypeNormal'");
    view = finder.findRequiredView(source, 2131755298, "field 'mapTypeSatellite'");
    target.mapTypeSatellite = finder.castView(view, 2131755298, "field 'mapTypeSatellite'");
    view = finder.findRequiredView(source, 2131755299, "field 'mapTypeHybrid'");
    target.mapTypeHybrid = finder.castView(view, 2131755299, "field 'mapTypeHybrid'");
    view = finder.findRequiredView(source, 2131755294, "field 'modeSpinner' and method 'snapChatTimeChange'");
    target.modeSpinner = finder.castView(view, 2131755294, "field 'modeSpinner'");
    ((android.widget.AdapterView<?>) view).setOnItemSelectedListener(
      new android.widget.AdapterView.OnItemSelectedListener() {
        @Override public void onItemSelected(
          android.widget.AdapterView<?> p0,
          android.view.View p1,
          int p2,
          long p3
        ) {
          target.snapChatTimeChange(p2);
        }
        @Override public void onNothingSelected(
          android.widget.AdapterView<?> p0
        ) {
          
        }
      });
  }

  @Override public void unbind(T target) {
    target.tvDistance = null;
    target.tvDuration = null;
    target.mapTypeNormal = null;
    target.mapTypeSatellite = null;
    target.mapTypeHybrid = null;
    target.modeSpinner = null;
  }
}
