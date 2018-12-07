package com.dialer.replicate.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dialer.replicate.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SearchActivity extends Activity {

    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.tv_no_search)
    TextView tvNoSearch;
    @BindView(R.id.rv_contact_list)
    ListView rvContactList;
    ContactAdapter contactAdapter;
    public static String[] contactNumber, contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        contactNumber = getResources().getStringArray(R.array.contactNumberWithoutCode);
        contactName = getResources().getStringArray(R.array.contactName);
        contactAdapter = new ContactAdapter(this, contactName, contactNumber);
        rvContactList.setAdapter(contactAdapter);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText2.getWindowToken(), 0);
        editText2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() == 0) {
                    tvNoSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                if (arg0.length() == 0) {
                    tvNoSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.length() == 0) {
                    tvNoSearch.setVisibility(View.GONE);
                }
                filter(arg0.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<String> contactList = new ArrayList<String>();
        String[] filterdNames = getResources().getStringArray(R.array.contactName);
        for (String s : filterdNames) {
            if (StringUtils.containsIgnoreCase(s, text)) {
                contactList.add(s);
                tvNoSearch.setVisibility(View.GONE);
            }
        }
        if (contactList.size() == 0) {
            contactList.clear();
            tvNoSearch.setVisibility(View.VISIBLE);
        }
        contactAdapter.filterList(contactList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
