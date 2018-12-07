package com.dialer.replicate.AppUtils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dialer.replicate.R;
import com.dialer.replicate.activity.DiallerViewActivity;
import com.dialer.replicate.app.AppController;


public class Utils {
    static Dialog emergencyDailog = null;

    public static void enterPhoneNumberDialog(final Activity context) {
        if (emergencyDailog == null || !emergencyDailog.isShowing()) {
            emergencyDailog = new Dialog(context);
            emergencyDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            emergencyDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            emergencyDailog.setCanceledOnTouchOutside(false);
            emergencyDailog.setCancelable(false);
            emergencyDailog.setContentView(R.layout.dialog_phone_number);
            emergencyDailog.show();
            TextView tvSubmit = (TextView) emergencyDailog.findViewById(R.id.tv_submit);
            EditText edtPhoneNumber = (EditText) emergencyDailog.findViewById(R.id.tv_phoneNumber);
            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = edtPhoneNumber.getText().toString();
                    if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
                        Toast.makeText(context, "Please enter valid phone number.", Toast.LENGTH_SHORT).show();
                    } else {
                        AppController.getmAppPreferences().setPrefrenceString("agentNumber", edtPhoneNumber.getText().toString());
                        DiallerViewActivity.agentNumber = edtPhoneNumber.getText().toString();
                        emergencyDailog.dismiss();
                    }
                }

            });
        }

    }

}