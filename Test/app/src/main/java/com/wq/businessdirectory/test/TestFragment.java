package com.wq.businessdirectory.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wq.businessdirectory.R;
import com.wq.businessdirectory.receiver.PhoneMessage;
import com.wq.businessdirectory.receiver.SMSUtil;
import com.wq.support.uibase.BaseFragment;
import com.wq.support.utils.log.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.EXTRA_MESSAGE;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.EXTRA_PHONE_NUMBER;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.EXTRA_PHONE_STATE;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.getMessageFilter;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.getPhoneFilter;

/**
 * Created by WQ on 2017/4/7.
 */

public class TestFragment extends BaseFragment {
    protected LocalBroadcastManager mLocalBroadcastManager;
    @Bind(R.id.tv_content)
    TextView tvContent;
    BroadcastReceiver smsReceiver, phoneReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        ButterKnife.bind(this, view);
        registerSMSReceiver();
        registerPhoneReceiver();
        return view;
    }
    public static Fragment newInstance(){
        return new TestFragment();
    }
    void registerSMSReceiver() {

        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                PhoneMessage message = (PhoneMessage) intent.getSerializableExtra(EXTRA_MESSAGE);
                tvContent.setText(message + "\n" + tvContent.getText());
            }
        };
        mLocalBroadcastManager.registerReceiver(smsReceiver, getMessageFilter());
    }

    void registerPhoneReceiver() {

        phoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra(EXTRA_PHONE_STATE, -1);
                String incomingNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        tvContent.setText("挂断: " + incomingNumber + "\n" + tvContent.getText());
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        tvContent.setText("接听: " + incomingNumber + "\n" + tvContent.getText());
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        tvContent.setText("响铃:来电号码: " + incomingNumber + "\n" + tvContent.getText());
                        //输出来电号码
                        break;
                }
                tvContent.setText(incomingNumber + "\n" + tvContent.getText());
            }
        };
        mLocalBroadcastManager.registerReceiver(phoneReceiver, getPhoneFilter());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mLocalBroadcastManager.unregisterReceiver(smsReceiver);
        mLocalBroadcastManager.unregisterReceiver(phoneReceiver);
    }
}
