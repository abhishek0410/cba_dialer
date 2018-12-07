package com.dialer.replicate.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.dialer.replicate.AppUtils.PhoneStateService;
import com.dialer.replicate.AppUtils.Utils;
import com.dialer.replicate.R;
import com.dialer.replicate.app.AppController;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DiallerViewActivity extends Activity {
    private static Dialog emergencyDailog;
    @BindView(R.id.iv_contact)
    ImageView ivContact;
    @BindView(R.id.tv_emergency)
    TextView tvEmergency;
    boolean fromMenu;
    RxPermissions rxPermissions;
    Handler handler;
    protected static boolean isVisible = false;
    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.ll_call)
    LinearLayout llCall;
    @BindView(R.id.iv_clear_no)
    ImageView ivClearNo;
    public static String codeNumber = "0290372817";
    public static String agentNumber = "";
    public static EditText etPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialler_detail);
        ButterKnife.bind(this);

        etPhoneNo = (EditText) findViewById(R.id.et_phone_no);
        handler = new Handler();
        rxPermissions = new RxPermissions(this);
        if (Build.VERSION.SDK_INT >= 23) {
            permission();
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPhoneNo.getWindowToken(), 0);
        gridView.setAdapter(new DiallerAdapter(this));
        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        ivContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContactClick(view);
            }
        });
        etPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (etPhoneNo.length() == 0) {
                    ivClearNo.setVisibility(View.INVISIBLE);
                    etPhoneNo.setEnabled(false);
                } else {
                    etPhoneNo.requestFocus();
                    etPhoneNo.setEnabled(true);
                    ivClearNo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPhoneNo.length() == 0) {
                    ivClearNo.setVisibility(View.INVISIBLE);
                    etPhoneNo.setEnabled(false);
                } else {
                    etPhoneNo.requestFocus();
                    etPhoneNo.setEnabled(true);
                    ivClearNo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPhoneNo.length() == 0) {
                    ivClearNo.setVisibility(View.INVISIBLE);
                    etPhoneNo.setEnabled(false);
                } else {
                    etPhoneNo.requestFocus();
                    etPhoneNo.setEnabled(true);
                    ivClearNo.setVisibility(View.VISIBLE);
                }
            }
        });
        etPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etPhoneNo.getWindowToken(), 0);
            }
        });
        ivClearNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DiallerAdapter.stringList != null && DiallerAdapter.stringList.size() > 0) {
                    DiallerAdapter.stringList.remove(DiallerAdapter.stringList.size() - 1);
                    String test = "";
                    for (int i = 0; i < DiallerAdapter.stringList.size(); i++) {
                        test = test + DiallerAdapter.stringList.get(i);
                    }
                    etPhoneNo.setText(test);
                }
            }
        });
        ivClearNo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                vibrator.vibrate(100);
                DiallerAdapter.stringList.clear();
                etPhoneNo.setText("");
                return false;
            }
        });
        llCall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (DiallerAdapter.stringList != null && DiallerAdapter.stringList.size() > 0) {
                    Intent intent = null;
                    intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + codeNumber));
                    startActivity(intent);
                    serviceStart("true", etPhoneNo.getText().toString(), agentNumber);
                    finishAffinity();
                } else {
                    Toast.makeText(DiallerViewActivity.this, "Please enter no", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxPermissions.request(Manifest.permission.CALL_PHONE)
                        .subscribe(granted -> {
                            if (granted) {
                                emergencyDialog(DiallerViewActivity.this);
                            } else {
                                Toast.makeText(DiallerViewActivity.this, "To use this app you need grant these permissions.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        callPermission();
    }

    public String getPhoneNumber() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
            if (subscription != null) {
                SubscriptionInfo info = subscription.get(0);
                String userNumber = info.getNumber();
                if (userNumber != null && !userNumber.isEmpty()) {
                    agentNumber = userNumber.substring(3);
                }
            }
        } else {
            TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            try {
                @SuppressLint("MissingPermission") String userNumber = telemamanger.getLine1Number();
                if (userNumber != null && !userNumber.isEmpty()) {
                    agentNumber = userNumber.substring(3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return agentNumber;
    }

    public void dialogToEnterPhoneNumber() {
        if (getPhoneNumber() == null || getPhoneNumber().isEmpty()) {
            if (AppController.getmAppPreferences().getPrefrenceString("agentNumber").isEmpty()) {
                Utils.enterPhoneNumberDialog(this);
            } else {
                agentNumber = AppController.getmAppPreferences().getPrefrenceString("agentNumber");
            }
        }
    }


    public void permission() {
        serviceStart("false", "", agentNumber);
    }

    @SuppressLint("MissingPermission")
    public void serviceStart(String value, String dialNumber, String agentNumber) {
        String action = "START";
        if (isServiceRunning(PhoneStateService.class)) {
            stopService(new Intent(this, PhoneStateService.class));
        }
        final Intent intent = new Intent(this, PhoneStateService.class);
        intent.putExtra("showPopUp", value);
        intent.putExtra("dialNumber", dialNumber);
        intent.setAction(action);
        startService(intent);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void callPermission() {
        rxPermissions.requestEach(Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_CALL_LOG)
                .subscribe(permission -> {
                    if (permission.granted) {
                        dialogToEnterPhoneNumber();
                    } else {
                        Toast.makeText(DiallerViewActivity.this, "To use this app you need grant these permissions.", Toast.LENGTH_SHORT).show();
                        callPermission();
                    }
                });
    }


    public void onContactClick(View view) {
        fromMenu = false;
        PopupMenu popup = new PopupMenu(DiallerViewActivity.this, ivContact);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("MissingPermission")
            public boolean onMenuItemClick(MenuItem item) {
                fromMenu = true;
                switch (item.getItemId()) {
                    case R.id.btnAllContacts:
                        startActivity(new Intent(DiallerViewActivity.this, SearchActivity.class));
                        break;
                    case R.id.btnCBABranch:
                        String cbaBranchNumber = "01300728403";
                        onClickItem(cbaBranchNumber);
                        break;
                    case R.id.btnGroupEmergency:
                        String groupNo = "01800643410";
                        onClickItem(groupNo);
                        break;
                    case R.id.btnSecurityOperation:
                        String securityNo = "01300701251";
                        onClickItem(securityNo);
                        break;
                    case R.id.btnGroupSecurity:
                        String groupsecurityNo = "01800023919";
                        onClickItem(groupsecurityNo);
                        break;
                    case R.id.btnFrontLine:
                        String frontLineNo = "01300137762";
                        onClickItem(frontLineNo);
                        break;
                    case R.id.btnCustomerBanking:
                        String customerNo = "0132221";
                        onClickItem(customerNo);
                        break;
                    case R.id.btnVoiceMail:
                        String voiceMailNo = "0228099";
                        onClickItem(voiceMailNo);
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    @SuppressLint("MissingPermission")
    public void onClickItem(String number) {
        rxPermissions.request(Manifest.permission.CALL_PHONE)
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + codeNumber));
                        startActivity(intent);
                        serviceStart("true", number, agentNumber);
                        finishAffinity();
                    } else {
                        Toast.makeText(DiallerViewActivity.this, "To use this app you need grant these permissions.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void emergencyDialog(final Activity context) {
        emergencyDailog = new Dialog(context);
        emergencyDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        emergencyDailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        emergencyDailog.show();
        emergencyDailog.setCancelable(false);
        emergencyDailog.setContentView(R.layout.dialog_emergency_call);
        final TextView tvConfirm = (TextView) emergencyDailog.findViewById(R.id.tv_confirm);
        final TextView tvCancel = (TextView) emergencyDailog.findViewById(R.id.tv_cancel);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emergencyNumber = "0290372817,000%23";
                onClickItem(emergencyNumber);
                emergencyDailog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergencyDailog.dismiss();
            }
        });
    }

    @OnClick(R.id.iv_info)
    public void onViewClicked() {
        Intent intent = new Intent(DiallerViewActivity.this, InfoActivity.class);
        startActivity(intent);
    }
}
