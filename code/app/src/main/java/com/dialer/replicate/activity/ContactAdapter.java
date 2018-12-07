package com.dialer.replicate.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dialer.replicate.AppUtils.PhoneStateService;
import com.dialer.replicate.R;

import java.util.ArrayList;


public class ContactAdapter extends BaseAdapter {
    String[] nameList;
    String[] numberList;
    Activity context;
    private static LayoutInflater inflater = null;

    public ContactAdapter(Activity mainActivity, String[] nameList, String[] numberList) {
        this.nameList = nameList;
        context = mainActivity;
        this.numberList = numberList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nameList.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView txtName, txtNumber;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.search_list_item, null);
        holder.txtName = (TextView) rowView.findViewById(R.id.txt_name);
        holder.txtNumber = (TextView) rowView.findViewById(R.id.txt_number);
        holder.txtName.setText(nameList[position]);
        holder.txtNumber.setText(numberList[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + DiallerViewActivity.codeNumber));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                serviceStart("true", context, numberList[position]);
                context.finishAffinity();
            }
        });
        return rowView;
    }

    @SuppressLint("MissingPermission")
    public void serviceStart(String value, Context context, String dialNumber) {
        String action = "START";
        if (isServiceRunning(PhoneStateService.class, context)) {
            context.stopService(new Intent(context, PhoneStateService.class));
        }
        final Intent intent = new Intent(context, PhoneStateService.class);
        intent.putExtra("showPopUp", value);
        intent.putExtra("ht", DiallerViewActivity.agentNumber);
        intent.putExtra("dialNumber", dialNumber);
        intent.setAction(action);
        context.startService(intent);
    }

    public void filterList(ArrayList<String> filterdNames) {
        this.nameList = filterdNames.toArray(new String[0]);
        notifyDataSetChanged();
    }

    private boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}