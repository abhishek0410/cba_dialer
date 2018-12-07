package com.dialer.replicate.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dialer.replicate.R;

import java.util.ArrayList;
import java.util.List;


public class DiallerAdapter extends BaseAdapter {
    Context mContext;
    AlphaAnimation alphaAnimation;

    public static List<String> stringList;

    public DiallerAdapter(Context mContext) {
        this.mContext = mContext;
        this.stringList = new ArrayList<>();
        alphaAnimation = new AlphaAnimation(1F, 0.8F);
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(mContext);
            gridView = inflater.inflate(R.layout.keypad_number_item, null);
            final TextView txtNumber = (TextView) gridView.findViewById(R.id.tv_number);
            TextView txtChar = (TextView) gridView.findViewById(R.id.tv_char);
            final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.effect_tick);
            gridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(alphaAnimation);
                    try {
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stringList.add(txtNumber.getText().toString());
                    String test = "";
                    for (int i = 0; i < stringList.size(); i++) {
                        test = test + stringList.get(i);
                    }
                    DiallerViewActivity.etPhoneNo.setText(test);

                }
            });

            if (i == 0) {
                txtNumber.setText(String.valueOf(1));
                txtChar.setVisibility(View.GONE);
            } else if (i == 1) {
                txtNumber.setText(String.valueOf(2));
                txtChar.setText("ABC");
            } else if (i == 2) {
                txtNumber.setText(String.valueOf(3));
                txtChar.setText("DEF");
            } else if (i == 3) {
                txtNumber.setText(String.valueOf(4));
                txtChar.setText("GHI");
            } else if (i == 4) {
                txtNumber.setText(String.valueOf(5));
                txtChar.setText("JKL");
            } else if (i == 5) {
                txtNumber.setText(String.valueOf(6));
                txtChar.setText("MNO");
            } else if (i == 6) {
                txtNumber.setText(String.valueOf(7));
                txtChar.setText("PQRS");
            } else if (i == 7) {
                txtNumber.setText(String.valueOf(8));
                txtChar.setText("TUV");
            } else if (i == 8) {
                txtNumber.setText(String.valueOf(9));
                txtChar.setText("WXYZ");
            } else if (i == 9) {
                txtChar.setVisibility(View.GONE);
                txtNumber.setText(String.valueOf("*"));
            } else if (i == 10) {
                txtChar.setVisibility(View.GONE);
                txtNumber.setText(String.valueOf(0));
            } else if (i == 11) {
                txtChar.setVisibility(View.GONE);
                txtNumber.setText(String.valueOf("#"));
            }
        } else {
            gridView = (View) view;
        }

        return gridView;
    }
}