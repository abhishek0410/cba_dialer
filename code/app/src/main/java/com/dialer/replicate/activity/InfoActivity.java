package com.dialer.replicate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.dialer.replicate.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InfoActivity extends Activity {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
