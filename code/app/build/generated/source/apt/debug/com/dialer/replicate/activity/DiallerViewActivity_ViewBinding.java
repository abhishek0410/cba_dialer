// Generated code from Butter Knife. Do not modify!
package com.dialer.replicate.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.dialer.replicate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DiallerViewActivity_ViewBinding implements Unbinder {
  private DiallerViewActivity target;

  private View view2131296358;

  @UiThread
  public DiallerViewActivity_ViewBinding(DiallerViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DiallerViewActivity_ViewBinding(final DiallerViewActivity target, View source) {
    this.target = target;

    View view;
    target.ivContact = Utils.findRequiredViewAsType(source, R.id.iv_contact, "field 'ivContact'", ImageView.class);
    target.tvEmergency = Utils.findRequiredViewAsType(source, R.id.tv_emergency, "field 'tvEmergency'", TextView.class);
    target.gridView = Utils.findRequiredViewAsType(source, R.id.grid_view, "field 'gridView'", GridView.class);
    target.llCall = Utils.findRequiredViewAsType(source, R.id.ll_call, "field 'llCall'", LinearLayout.class);
    target.ivClearNo = Utils.findRequiredViewAsType(source, R.id.iv_clear_no, "field 'ivClearNo'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.iv_info, "method 'onViewClicked'");
    view2131296358 = view;
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
    DiallerViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivContact = null;
    target.tvEmergency = null;
    target.gridView = null;
    target.llCall = null;
    target.ivClearNo = null;

    view2131296358.setOnClickListener(null);
    view2131296358 = null;
  }
}
