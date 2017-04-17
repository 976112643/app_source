package com.wq.businessdirectory.common.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.wq.businessdirectory.R;
import com.wq.businessdirectory.test.TestFragment;

import java.util.Locale;

/**
 * Created by WQ on 2017/4/17.
 */

public class BoxingUtils {
    public static void startBoxing(Activity  context,int max){
//        String cachePath = BoxingFileHelper.getCacheDir(context);
//        if (TextUtils.isEmpty(cachePath)) {
//            Toast.makeText(context, R.string.storage_deny, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Uri destUri = new Uri.Builder()
//                .scheme("file")
//                .appendPath(cachePath)
//                .appendPath(String.format(Locale.US, "%s.jpg", System.currentTimeMillis()))
//                .build();
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG); // Mode：Mode.SINGLE_IMG, Mode.MULTI_IMG, Mode.VIDEO
        config.needCamera().needGif().withMaxCount(max); // camera, gif support, set selected images count
// start thumbnails Activity, need boxing-impl.
        Boxing.of(config).withIntent(context, BoxingActivity.class).start(context, 0x0019);
    }
}
