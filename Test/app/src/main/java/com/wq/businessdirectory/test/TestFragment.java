package com.wq.businessdirectory.test;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.bumptech.glide.Glide;
import com.widget.MultiImageUploadView;
import com.wq.businessdirectory.R;
import com.wq.businessdirectory.receiver.PhoneMessage;
import com.wq.support.ui.selectpicture.PhotoSelActivity;
import com.wq.support.uibase.BaseFragment;
import com.wq.support.utils.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.widget.MultiImageUploadView.REQUEST_PHOTO;
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
    protected LocalBroadcastManager mLocalBroadcastManager;
    @Bind(R.id.tv_content)
    TextView tvContent;
    BroadcastReceiver smsReceiver, phoneReceiver;
    String phoneNumber;
    Map<String, Integer> phoneSet = new HashMap<>();
    @Bind(R.id.imgview)
    ImageView imgview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        ButterKnife.bind(this, view);
        registerSMSReceiver();
        registerPhoneReceiver();
        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cachePath = BoxingFileHelper.getCacheDir(getContext());
                if (TextUtils.isEmpty(cachePath)) {
                    Toast.makeText(that, R.string.storage_deny, Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri destUri = new Uri.Builder()
                        .scheme("file")
                        .appendPath(cachePath)
                        .appendPath(String.format(Locale.US, "%s.jpg", System.currentTimeMillis()))
                        .build();
                BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG); // Mode：Mode.SINGLE_IMG, Mode.MULTI_IMG, Mode.VIDEO
                config.needCamera().needGif().withCropOption(new BoxingCropOption(destUri).withMaxResultSize(200, 200)).withMaxCount(9); // camera, gif support, set selected images count
// start thumbnails Activity, need boxing-impl.
                Boxing.of(config).withIntent(that, BoxingActivity.class).start(TestFragment.this, 0x0019);
            }
        });
        imgview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //授权
                Intent intent = new Intent(getActivity(), PhotoSelActivity.class);
                intent.putExtra("isMuit", true);
                intent.putExtra("max", 9);
                intent.putExtra("selNum", 0);
                startActivityForResult(intent, REQUEST_PHOTO);
//                PermissionGen.with(TestFragment.this)
//                        .addRequestCode(100)
//                        .permissions(
//                                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .request();
                return true;
            }
        });
        return view;
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
        PermissionGen.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<BaseMedia> medias = Boxing.getResult(data);
        if (medias != null) {
            Glide.with(this).load(medias.get(0).getPath()).asBitmap().into(imgview);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Fragment newInstance() {
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
        mLocalBroadcastManager.unregisterReceiver(smsReceiver);
        mLocalBroadcastManager.unregisterReceiver(phoneReceiver);
    }
}
