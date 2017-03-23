package com.wq.support.dialogs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wq.support.dialogs.FuncViewer.FuncAdapter.FuncItem;
import com.wq.support.dialogs.PhotoViewer.PhotoBean;
import com.wq.support.net.FileDownHandler;
import com.wq.support.util.BitmapUtil;
import com.wq.support.util.ImageFormatUtil;
import com.wq.support.util.ImageLoadOptions;
import com.wq.support.utils.FileUtils;
import com.wq.support.utils.PictureUtil;
import com.wq.support.utils.ToastUtil;
import com.wq.uicore.R;
import com.loopj.android.http.RequestHandle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.widget.imageview.ProcessScalImageView;

/**
 * 图片查看器,看图浮层,已支持普通图片和gif图片
 * 
 * @author WQ 下午5:52:09
 */
public class PhotoViewer<T extends PhotoBean> {

	public Context mContext;
	List<T> data;
	DisplayImageOptions options = ImageLoadOptions.getDefaultOption();
	int mPosition;
	Dialog dialog;
	private AbstractViewAdapter viewAdapter;
	File saveDir = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DCIM).getAbsoluteFile();

	public interface onPhotoLongPressListener<T extends PhotoBean> {
		boolean photoLongPress(T t, int position);
	}

	public void setSaveDir(File saveDir) {
		this.saveDir = saveDir;
	}

	public static abstract class AbstractViewAdapter {
		public abstract void fillContent(View itemView, List<PhotoBean> mItem,
				int position);

		public abstract View getView();

		public abstract View destoryView(ViewGroup container, int position,
				Object object);
	}


	public interface PhotoBean {
		public String getUrl();
		public String getTitle();
	}

	public PhotoViewer(Context mContext) {
		this.mContext = mContext;
		viewAdapter = new DefaultViewAdapter(mContext);
	}

	public PhotoViewer(Context mContext, List<T> imgs,
			DisplayImageOptions options, int position) {
		this(mContext);
		setArgment(imgs, options, position);
	}

	public void setArgment(List<T> imgs, DisplayImageOptions options,
			int position) {
		this.data = imgs;
		this.options = options;
		this.mPosition = position;
	}

	public void setArgment(List<T> imgs, int position) {
		this.data = imgs;
		this.mPosition = position;
	}

	public void setArgment(List<T> imgs) {
		this.data = imgs;
	}


	public void show() {
		View dialogView = createView(mContext, data, options,
				mPosition < data.size() ? mPosition : 0);
		dialog = DialogFactory.createDialog(mContext, dialogView);
	}

	public boolean isShow() {
		return dialog != null && dialog.isShowing();
	}

	public void cancel() {
		dialog.cancel();
	}

	/**
	 * 创建图片查看视图
	 * 
	 * @param context
	 * @param imgs
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public View createView(final Context context, final List<T> imgs,
			final DisplayImageOptions options, int position) {
		if (imgs == null || imgs.size() == 0)
			return null;
		View layout = View.inflate(context, R.layout.ui_show_image, null);
		final TextView text_num = (TextView) layout.findViewById(R.id.text_num);
		ViewPager viewPager = (ViewPager) layout.findViewById(R.id.viewpager);

		viewPager.setAdapter(new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public Object instantiateItem(ViewGroup container,
					final int position) {
				View view = viewAdapter.getView();
				viewAdapter.fillContent(view, (List<PhotoBean>) data, position);
				container.addView(view);
				return view;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				View view = (View) object;
				viewAdapter.destoryView(container, position, object);
				container.removeView(view);
			}

			@Override
			public int getCount() {
				return imgs.size();
			}
		});
		viewPager.setCurrentItem(position);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				mPosition = arg0;
				text_num.setText("" + (arg0 + 1) + "/" + imgs.size());
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		return layout;
	}

	public class DefaultViewAdapter extends AbstractViewAdapter {

		private Context mContext;
		DisplayImageOptions options = ImageLoadOptions.getDefaultOption();

		public DefaultViewAdapter(Context context) {
			super();
			this.mContext = context;
		}

		@Override
		public void fillContent(final View itemView, List<PhotoBean> data,
				int position) {
			final ProcessScalImageView img = (ProcessScalImageView) itemView
					.findViewById(R.id.img_show);
			final ProgressBar imgload = (ProgressBar) itemView
					.findViewById(R.id.img_load);
			if (path(data.get(position)).startsWith("http")) {
				String filename = "" + path(data.get(position)).hashCode();
				final RequestHandle handler = FileDownHandler.startDown(
						mContext,
						path(data.get(position)),
						new FileDownHandler(mContext, filename) {
							@Override
							public void onStart() {
								imgload.setVisibility(View.VISIBLE);
								super.onStart();
							}

							@Override
							public void onProgress(long bytesWritten,
												   long totalSize) {
								if (!isShow()) {
									if (itemView.getTag() instanceof RequestHandle) {
										((RequestHandle) itemView.getTag())
												.cancel(true);
									}
								}
								img.setProgress((int) (bytesWritten * 100f / totalSize));
								imgload.setIndeterminate(false);
								imgload.setProgress((int) bytesWritten);
								imgload.setMax((int) totalSize);
								super.onProgress(bytesWritten, totalSize);
							}

							public void onSuccess(File file) {
								if (ImageFormatUtil.isGifFile(file)) {
									try {
										img.setImageDrawable(new GifDrawable(
												file));
									} catch (IOException e) {
										e.printStackTrace();
									}
								} else {
									// 设置 图片到图片控件上,目前不考虑大图OOM的情况
									img.setImageBitmap(BitmapFactory
											.decodeFile(file.getAbsolutePath()));
								}
								imgload.setVisibility(View.GONE);
							};

							public void onFailure(int status,
									org.apache.http.Header[] arg1,
									Throwable arg2, File arg3) {

								imgload.setVisibility(View.GONE);
							};
						}, false);
				itemView.setTag(handler);
			} else {
				File file = new File(path(data.get(position)));
				if (ImageFormatUtil.isGifFile(file)) {
					try {
						img.setImageDrawable(new GifDrawable(file));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					// 设置 图片到图片控件上,目前不考虑大图OOM的情况
					img.setImageBitmap(BitmapFactory.decodeFile(file
							.getAbsolutePath()));
				}
			}
			bindListener(img, data, position);
		}

		@Override
		public View getView() {
			// TODO Auto-generated method stub
			return View.inflate(mContext, R.layout.item_image, null);
		}

		public void bindListener(ProcessScalImageView img,
				final List<PhotoBean> imgs, final int position) {
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					cancel();
				}
			});
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					cancel();
				}
			});
			img.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					final FuncViewer img = new FuncViewer(mContext);
					img.setArgment(new ArrayList<FuncItem>() {
						{
							add(new FuncItem("保存图片", true));
							add(new FuncItem("取消"));
						}
					});

					img.showOperaDialog(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int pos, long id) {
							if (pos == 0) {
								final Dialog dialog = DialogFactory
										.createLoadDialog(mContext, "图片保存中...");
								final File savePath = new File(saveDir, System
										.currentTimeMillis() + ".jpg");
								File cacheFile = FileDownHandler.getCacheDownFile(
										mContext,
										""
												+ String.valueOf(
														imgs.get(position))
														.hashCode());
								if (cacheFile.exists()) {
									if (savePath.exists()
											|| FileUtils.fileCopy(cacheFile,
													savePath)) {
										PictureUtil.galleryAddPic(mContext, savePath.getAbsolutePath());
										ToastUtil.longM("图片保存在 "
												+ savePath.getAbsolutePath()
												+ " 目录下");
									} else {
										ToastUtil.shortM("图片保存失败");
									}
									dialog.cancel();
									img.dimiss();
								} else {
									imageDown(imgs, position, img, dialog,
											savePath);
									// UiUtils.shortM("图片保存失败");
								}

							} else {
								img.dimiss();
							}
						}

					});
					// UiUtils.shortM("长按,你是要保存图片吗?");
					return true;
				}
			});
		}

		private void imageDown(final List<PhotoBean> imgs, final int position,
				final FuncViewer img, final Dialog dialog, final File savePath) {
			ImageLoader.getInstance().loadImage(path(imgs.get(position)),
					options, new SimpleImageLoadingListener() {
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							if (BitmapUtil.saveBitmapToFile(loadedImage,
									savePath.getAbsolutePath())) {
								ToastUtil.longM("图片保存在 "
										+ savePath.getAbsolutePath() + " 目录下");
							} else {
								ToastUtil.shortM("图片保存失败");
							}
							dialog.cancel();
							img.dimiss();
						};

						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							ToastUtil.shortM("图片保存失败");
							dialog.cancel();
							img.dimiss();
						};
					});
		}

		String path(PhotoBean photo) {
			String path = String.valueOf(photo.getUrl());
			if (!path.startsWith("http")&&!path.startsWith("file")) { return "file:///" + path; }
			return path;
		}

		@Override
		public View destoryView(ViewGroup container, int position, Object object) {
			View view = (View) object;
			if (view.getTag() instanceof RequestHandle) {
				// 停掉网络请求
				RequestHandle handler = (RequestHandle) view.getTag();
				handler.cancel(true);
				view.setTag(null);
			}
			return null;
		}

	}
}
