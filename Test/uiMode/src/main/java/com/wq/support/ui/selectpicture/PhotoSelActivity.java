package com.wq.support.ui.selectpicture;

import java.io.File;
import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.wq.support.dialogs.DialogFactory;
import com.wq.support.ui.UIBaseActivity;
import com.wq.support.util.BitmapUtil;
import com.wq.support.util.UiUtils;
import com.wq.support.utils.PathDeal;
import com.wq.support.utils.ToastUtil;
import com.wq.uicore.R;

/**
 * 图片选择页面
 * 使用方法:
 * 		Intent intent = new Intent(that, PhotoSelActivity.class);
		intent.putExtra("W", 480);
		intent.putExtra("H", 240);
		startActivityForResult(intent, 101);
		不传宽高数据时,会进入剪裁流程
		调用类在onActivityResult中接收图片文件路径
			data.getStringExtra(PhotoSelActivity.RESULT);
 * @author WQ 2015年11月3日
 */
public class PhotoSelActivity extends UIBaseActivity {
	Uri uri;
	public static final String RESULT = "result";
	Dialog selHead;
	int W, H;
	boolean isMuit=false;
	
	
	protected void initView() {
		W = getIntent().getIntExtra("W", 0);
		H = getIntent().getIntExtra("H", 0);
		isMuit=getIntent().getBooleanExtra("isMuit", false);
//		max=getIntent().getIntExtra("max", 0);
		View dialogView = View.inflate(this, R.layout.picture_select, null);
		selHead = DialogFactory.createDialog(this, dialogView);
		dialogView.findViewById(R.id.camera).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selHead.dismiss();
				checkPermission(Manifest.permission.CAMERA, new PermissionCheckedCallback() {
					@Override
					public void permissionGrant() {
						uri = UiUtils.startCamera(PhotoSelActivity.this);
					}

					@Override
					public void permissionDenied() {
						dealPermissionDenied("拍照权限被拒绝!");
					}
				});

			}
		});
		dialogView.findViewById(R.id.photo_album).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selHead.dismiss();
				if(isMuit){
					//多选模式

					checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionCheckedCallback() {
						@Override
						public void permissionGrant() {
							selectMuitPhoto(PhotoSelActivity.this, getIntent());
						}

						@Override
						public void permissionDenied() {
							dealPermissionDenied("文件读取权限被拒绝!");
						}
					});

				}else {
					UiUtils.selectPhoto(PhotoSelActivity.this);
				}
				
			}
		});
		dialogView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selHead.cancel();
			}
		});
		dialogView.findViewById(R.id.out).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selHead.cancel();
			}
		});
		selHead.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dispathResult(RESULT_CANCELED, null);
			}
		});
	}

	private void dealPermissionDenied(String toastMsg) {
		Toast.makeText(this,toastMsg,Toast.LENGTH_SHORT).show();
		finish();
	}

	/**
	 * 选择图片,多选
	 * 
	 * @param activity
	 */
	public  void selectMuitPhoto(Activity activity, Intent intent) {
		intent.setClass(activity, MulitImageAct.class);
		Log.d("weiquan ", "相册");
		activity.startActivityForResult(intent, 3);
	}
	private void dispathResult(int resultCode, Object path) {
		Intent data = null;
		if (resultCode == RESULT_OK) {
			data = new Intent();
			if(path instanceof String){
			data.putExtra("result", (String)path);
			}else if(path instanceof ArrayList){
				data.putExtra("result", (ArrayList<String>)path);
			}
		}
		setResult(resultCode, data);
		finish();
	}


	PermissionCheckedCallback tmpPermissionCallback;
	public void checkPermission(String permission,PermissionCheckedCallback call){
		if(PackageManager.PERMISSION_GRANTED==ContextCompat.checkSelfPermission(this,permission)){
			call.permissionGrant();
		}else {
			if (Build.VERSION.SDK_INT >= 23) {//6.0及以上
				this.tmpPermissionCallback=call;
				ActivityCompat.requestPermissions(this,new String[]{permission},0x101);
			} else {
				call.permissionDenied();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(tmpPermissionCallback!=null) {
			if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
				tmpPermissionCallback.permissionGrant();
			}else {
				tmpPermissionCallback.permissionDenied();
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	public abstract class   PermissionCheckedCallback{
		public  void permissionGrant(){}
		public  void permissionDenied(){}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//
		if(resultCode!=RESULT_OK){
			ToastUtil.shortM(R.string.toast_operate_cancel);
			dispathResult(RESULT_CANCELED, null);
			return ;
		}
		if (requestCode == 0 || requestCode == 1 || requestCode == 2 || requestCode==3) {
			Object obj = null;
			if (requestCode == 0 && data == null) {
				ToastUtil.shortM(R.string.toast_operate_cancel);
				dispathResult(RESULT_CANCELED, null);
			} else if (requestCode == 1 && data != null) {
				ToastUtil.shortM(R.string.toast_operate_cancel);
				dispathResult(RESULT_CANCELED, null);
			} else if (requestCode == 0)// 剪裁图片之后
			{
				obj = data.getParcelableExtra("data");
				Bitmap bm = (Bitmap) obj;
				File file = new File(getCacheDir().getAbsolutePath() + "/"
						+ System.currentTimeMillis() + ".jpg");
				BitmapUtil.saveBitmapToFile(bm, file.getAbsolutePath());
				uri = Uri.fromFile(file);
				dispathResult(RESULT_OK, file.getAbsolutePath());
			} else if (requestCode == 2)// 照片选择之后
			{
				obj = data == null ? null : data.getData();// 图片地址
				if (obj != null) {
					if (W == 0 || H == 0) {
						dispathResult(RESULT_OK, PathDeal.getPath(PhotoSelActivity.this, (Uri) obj));
						return;
					}
					UiUtils.startPhotoZoom(PhotoSelActivity.this, (Uri) obj,W, H);// 剪裁图片
				}
			}
			else if(requestCode==3){
				dispathResult(RESULT_OK, data.getStringArrayListExtra("imgs"));
			}
			else if (requestCode == 1) {
				if (uri != null && new File(uri.getEncodedPath()).exists()) {
					if (W == 0 || H == 0) {
						dispathResult(RESULT_OK, PathDeal.getPath(PhotoSelActivity.this, uri));
						return;
					}
					UiUtils.startPhotoZoom(PhotoSelActivity.this, uri, W, H);// 剪裁图片
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


}
