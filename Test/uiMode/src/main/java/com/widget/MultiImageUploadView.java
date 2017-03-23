package com.widget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.wq.base.Config;
import com.wq.support.dialogs.DialogFactory;
import com.wq.support.ui.selectpicture.PhotoSelActivity;
import com.wq.support.util.BitmapUtil;
import com.wq.support.util.ImageLoadOptions;
import com.wq.support.util.UiUtils;
import com.wq.support.utils.AHandler;
import com.wq.support.utils.AHandler.Task;
import com.wq.support.utils.ToastUtil;
import com.wq.support.utils.log.Logger;
import com.wq.uicore.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 图片上传+展示控件
 * 
 * @author WQ 2015年11月12日
 */
public class MultiImageUploadView extends FlowLayout implements OnClickListener {
	ViewGroup containView;
	ImageView handlerView;// 加号图片
	int numCol = 3;// 图片列数
	int width = 0;// 宽度
	int max = 9;
	int closeRes = R.drawable.ic_del;// 删除按钮的资源
	Activity attachAct;
	List<File> files;
	List<File> compress_files;
	String suffix[] =
	{ "png", "jpg", "jpeg", "gif" };
	OnClickListener listener;
	public static final int REQUEST_PHOTO = 0x0099;
	ImageAdapter imageAdapter=new BaseImageAdapter(this){

		@Override
		public View getView(ViewGroup parent, Bitmap bit, String url) {
			return null;
		}

	};
	public int getMax() {
		return max;
	}

	public int getNumCol() {
		return numCol;
	}

	public void setNumCol(int numCol) {
		this.numCol = numCol;
		init();// 重新初始化
	}

	public void setMax(int max) {
		this.max = max;
	}

	public MultiImageUploadView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MultiImageUploadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MultiImageUploadView(Context context) {
		super(context);
		init();
	}

	public void attachActivity(Activity attachAct) {
		this.attachAct = attachAct;
		handlerView.setOnClickListener(this);
	}

	public void setAddHandlerImage(int res) {
		handlerView.setImageResource(res);
	}

	public void setCloseHandlerImage(int res) {
		closeRes = res;
	}

	public void setAddClickListener(OnClickListener listener) {
		this.listener = listener;
		handlerView.setOnClickListener(this.listener);
	}
	public void setDeleteListener(){
		
	}
	public List<File> getFiles() {
		// return files;
		return compress_files;
	}

	void updateHandlerStatus() {

		handlerView.setVisibility(compress_files.size() < max ? View.VISIBLE
				: View.INVISIBLE);
		handlerView.setEnabled(compress_files.size() < max);
	}

	public void addImg(Intent data) {
		if (data != null) {
			String rst=null;
			final ArrayList<String> paths=new ArrayList<String>();
			try{
				rst=data.getStringExtra(PhotoSelActivity.RESULT);
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				paths.addAll(data.getStringArrayListExtra(PhotoSelActivity.RESULT));
			}catch(Exception e){
				e.printStackTrace();
			}
			if (rst != null) {
				addFile(new File(rst));
			} else if (paths.size()!=0) {
		final	Dialog dialog=	DialogFactory.createLoadDialog(getContext(), "载入中.");
				AHandler.runTask(new Task() {
					@Override
					public void task() {
						for (String string : paths) {
							addFile(new File(string), false);
						}
					}
					@Override
					public void update() {
						dialog.cancel();
					}

				});

			}
		}
	}

	public void addFile(final File file, boolean hasToast) {
		if (files.contains(file)) {
			if (hasToast)
				ToastUtil.shortM("图片已存在");
			return;
		}
		if(!file.toString().startsWith("http") &&( file.length()==0 ||!file.exists())){
			if (hasToast)
				ToastUtil.shortM("无效图片");
			return;
		}
		// if (file.length() > 10 * 1024 * 1024) {
		// UiUtils.shortM("图片不能超过2M");
		// return;
		// }
		if (!checkSuffix(file.getName())) {
			if (hasToast) {
				ToastUtil.shortM("图片类型不允许!");
			}
			return;
		}
		try {
			final String imgPath = file.toString();
			if (!imgPath.startsWith("http")) {
				String aimPath = new File(Config.IMG_SAVE_DIR, file.getName())
						.getAbsolutePath();
				String compress_path = BitmapUtil.saveBitmapToFile(imgPath,
						aimPath, 30);// 压缩上传的图片质量

				File compress_file = new File(compress_path);
				compress_files.add(compress_file);
			} else {
				compress_files.add(file);
			}
			files.add(file);
			final int index=compress_files.size()-1;
			if (imgPath.startsWith("http")){
					post(new Runnable() {

						@Override
						public void run() {
							String tmp=imgPath;
							if(!tmp.startsWith("http://")){
								tmp=tmp.replace("http:/", "http://");
								tmp=tmp.replace("https:/", "https://");
							}
							View img = getImage(null,tmp);
							addView(img, index);
							img.setId(index);
							updateHandlerStatus();
						}
					});
					
			}else {
				post(new Runnable() {

					@Override
					public void run() {
							View img = getImage(BitmapUtil.getImageThumbnail(
									file.toString(), width, width),null);
							addView(img, index);
							img.setId(index);
							updateHandlerStatus();
					}
				});
			}
			

		} catch (Exception e) {
			ToastUtil.shortM("图片添加出错!");
		}
	}

	public void clearCache() {
		for (File file : compress_files) {
			file.delete();
		}
	}

	public void addFile(File file) {
		addFile(file, true);
	}

	private boolean checkSuffix(String filename) {
		filename=filename.toLowerCase();
		for (String suffix : this.suffix) {
			if (filename.endsWith(suffix))
				return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		Logger.E("添加图片");
		if (attachAct != null) {
			Intent intent = new Intent(attachAct, PhotoSelActivity.class);
			intent.putExtra("isMuit", true);
			intent.putExtra("max", max);
			intent.putExtra("selNum", compress_files.size());
			attachAct.startActivityForResult(intent, REQUEST_PHOTO);
		}
	}

	private void init() {
		if (getChildCount() != 0) {
			handlerView = (ImageView) findViewById(R.id.handlerView);
			removeView(handlerView);
		}
		if (handlerView == null) {
			handlerView = new ImageView(getContext());
		}

		setHorizontalSpacing(UiUtils.dp2px(getContext(), 8));
		setVerticalSpacing(UiUtils.dp2px(getContext(), 8));
		int WH[] = UiUtils.WHD(getContext());
		width = (WH[0] - UiUtils.dp2px(getContext(), 8 * (numCol + 1)))
				/ numCol;
		setPadding(UiUtils.dp2px(getContext(), 8), 0, 0, 0);
		handlerView.setImageResource(R.drawable.ic_add_img);
		addView(handlerView, getParams());
		handlerView.setOnClickListener(this);
		files = new ArrayList<File>();
		compress_files = new ArrayList<File>();

	}

	public View getImage(Bitmap bit,String url) {
		RelativeLayout layout = new RelativeLayout(getContext());
		LayoutParams params = new LayoutParams(width, width);
		layout.setLayoutParams(params);
		
		ImageView view = new ImageView(getContext());
		RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		view.setScaleType(ScaleType.CENTER_CROP);
		if(bit!=null){
		view.setImageBitmap(bit);
		}else {
			ImageLoader.getInstance().displayImage(url, view, ImageLoadOptions.getExactlyOption(R.drawable.new_nopic));
		}
		layout.addView(view, p2);
		// 删除按钮相关
		RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(45, 45);
		ImageView close = new ImageView(getContext());
		close.setImageResource(closeRes);
		layout.setId(files.size() - 1);
		p1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		p1.topMargin = 5;
		p1.rightMargin = 5;
		layout.addView(close, p1);

		
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RelativeLayout lay = (RelativeLayout) v.getParent();
				ViewGroup parent = (ViewGroup) lay.getParent();
				files.remove(lay.getId());
				compress_files.remove(lay.getId());
				parent.removeView(lay);
				updateHandlerStatus();
				// // 重新设置子控件的索引位置
				for (int i = 0; i < parent.getChildCount() - 1; i++) {
					View child = parent.getChildAt(i);
					child.setId(i);
				}
			}
		});

		return layout;
	}
	
	public LayoutParams getParams() {
		LayoutParams params = new LayoutParams(width, width);
		return params;
	}
	
	public abstract static class ImageAdapter {
		public abstract  int getWidth();//获取宽度
		public abstract  int getHeight();//获取高度
		public abstract View getView(ViewGroup parent,Bitmap bit,String url);
	}
	public abstract static class BaseImageAdapter  extends ImageAdapter{
		int width=-1,height=-1;
		protected  MultiImageUploadView uploadView;
		protected  int WH[];
		public BaseImageAdapter(MultiImageUploadView uploadView){
			this.uploadView
					=uploadView;
			WH=UiUtils.WHD(uploadView.getContext());
		}
		@Override
		public int getHeight() {
			return getWidth();
		}

		@Override
		public int getWidth() {
			int numCol=uploadView.getNumCol();
			width = (WH[0] - UiUtils.dp2px(uploadView.getContext(), 8 * (numCol + 1)))
					/ numCol;
			return width;
		}

	}
}
