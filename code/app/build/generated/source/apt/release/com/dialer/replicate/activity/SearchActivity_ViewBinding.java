// Generated code from Butter Knife. Do not modify!
package com.dialer.replicate.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dialer.replicate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SearchActivity_ViewBinding implements Unbinder {
  private SearchActivity target;

  @UiThread
  public SearchActivity_ViewBinding(SearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SearchActivity_ViewBinding(SearchActivity target, View source) {
    this.target = target;

    target.editText2 = Utils.findRequiredViewAsType(source, R.id.editText2, "field 'editText2'", EditText.class);
    target.tvNoSearch = Utils.findRequiredViewAsType(source, R.id.tv_no_search, "field 'tvNoSearch'", TextView.class);
    target.rvContactList = Utils.findRequiredViewAsType(source, R.id.rv_contact_list, "field 'rvContactList'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SearchActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.editText2 = null;
    target.tvNoSearch = null;
    target.rvContactList = null;
  }
}
