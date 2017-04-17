package com.wq.businessdirectory.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wq.businessdirectory.R;
import com.wq.businessdirectory.common.db.DBHelper;
import com.wq.businessdirectory.receiver.mode.PhoneMessage;
import com.wq.support.uibase.BaseFragment;
import com.wq.support.utils.ToastUtil;
import com.wq.support.utils.log.Logger;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Component;
import io.realm.Realm;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.CALL_STATE_OUT;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.EXTRA_MESSAGE;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.EXTRA_PHONE_NUMBER;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.EXTRA_PHONE_STATE;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.getMessageFilter;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.getPhoneFilter;
import static com.wq.support.utils.EmptyDeal._EMPTY;

/**
 * Created by WQ on 2017/4/7.
 */

public class TestFragment extends BaseFragment {

    @Bind(R.id.tv_content)
    TextView tvContent;
    BroadcastReceiver smsReceiver, phoneReceiver;
    String phoneNumber;
    @Bind(R.id.btn_clear)
    Button btnClear;
    @Inject
    protected LocalBroadcastManager mLocalBroadcastManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        DaggerAppComponet.
                builder().
                appModel(new AppModel(getContext())).
                build().
                inject(this);

        registerSMSReceiver();
        registerPhoneReceiver();
        return view;
    }


    public static Fragment newInstance() {
        return new TestFragment();
    }


    @PermissionSuccess(requestCode = 100)
    public void doSuccess() {
        Logger.D("成功");
    }

    @PermissionFail(requestCode = 100)
    public void doFail() {
        Logger.D("失败");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    void registerSMSReceiver() {

        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                PhoneMessage message = DBHelper.getMode(intent, PhoneMessage.class);
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
                if (!_EMPTY(incomingNumber)) {
                    phoneNumber = incomingNumber;
                }

                switch (state) {
                    case CALL_STATE_OUT:
                        tvContent.setText("呼叫: " + phoneNumber + "\n" + tvContent.getText());
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        tvContent.setText("挂断: " + phoneNumber + "\n" + tvContent.getText());
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        tvContent.setText("接听: " + phoneNumber + "\n" + tvContent.getText());
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        tvContent.setText("响铃:来电号码: " + phoneNumber + "\n" + tvContent.getText());
                        //输出来电号码
                        break;
                }
            }
        };
        mLocalBroadcastManager.registerReceiver(phoneReceiver, getPhoneFilter());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        try {
            mLocalBroadcastManager.unregisterReceiver(smsReceiver);
            mLocalBroadcastManager.unregisterReceiver(phoneReceiver);
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.btn_clear)
    public void onClick() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        ToastUtil.shortM("数据已清除");
    }
}
