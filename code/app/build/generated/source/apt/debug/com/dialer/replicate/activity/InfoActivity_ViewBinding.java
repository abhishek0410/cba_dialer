// Generated code from Butter Knife. Do not modify!
package com.dialer.replicate.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.dialer.replicate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class InfoActivity_ViewBinding implements Unbinder {
  private InfoActivity target;

  private View view2131296355;

  @UiThread
  public InfoActivity_ViewBinding(InfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public InfoActivity_ViewBinding(final InfoActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.iv_back, "field 'ivBack' and method 'onViewClicked'");
    target.ivBack = Utils.castView(view, R.id.iv_back, "field 'ivBack'", ImageView.class);
    view2131296355 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    InfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivBack = null;

    view2131296355.setOnClickListener(null);
    view2131296355 = null;
  }
}
