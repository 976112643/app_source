/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.widget.banner;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wq.uicore.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


/**
 * @Description: 图片适配�?
 * @param <T>
 */
public class ImagePagerAdapter<T> extends PagerAdapter implements
		OnClickListener {

	protected Context mContext;
	private List<T> imageIdList;
	private int size;
	private boolean isInfiniteLoop;
	// private ImageLoader imageLoader;
	private DisplayImageOptions options;
	OnBannerClickListener onBannerClickListener;

	public ImagePagerAdapter(Context context, List<T> imageIdList) {
		this.mContext = context;
		this.imageIdList = imageIdList;
		if (imageIdList != null) {
			this.size = imageIdList.size();
		}
		isInfiniteLoop = false;
	}

	public ImagePagerAdapter<T> setOnBannerClickListener(
			OnBannerClickListener onBannerClickListener) {
		this.onBannerClickListener = onBannerClickListener;
		return this;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	public int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	public int getSize() {
		return imageIdList == null ? 0 : imageIdList.size();
	}

	public View getView(final int index, View view, ViewGroup container) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(mContext, R.layout.my_viewpage, null);
			holder.imageView = (ImageView) view.findViewById(R.id.img);
			view.setTag(holder);
			holder.imageView.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		int position = getPosition(index);
		holder.position = position;
		setImage(imageIdList.get(position), holder.imageView);
		holder.imageView.setOnClickListener(this);
		view.setOnClickListener(this);
		// holder.imageView.setTag(position);
		return view;
	}

	public void setImage(T mItem, ImageView img) {
		if (mItem instanceof Integer) {
			img.setImageResource((Integer) mItem);
		} else if (String.valueOf(mItem).startsWith("http")) {// 网络地址
			if (options == null) {
				options = new DisplayImageOptions.Builder()
						.resetViewBeforeLoading(false)
						.showImageOnLoading(R.drawable.new_nopic)
						.showImageForEmptyUri(R.drawable.new_nopic)
						.displayer(
								new FadeInBitmapDisplayer(200, true, false,
										false))
						.showImageOnFail(R.drawable.new_nopic)
						.cacheInMemory(true)
						.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc(true)
						.build();
			}
			ImageLoader.getInstance().displayImage(String.valueOf(mItem), img,
					options);
		} else {
			try {
				img.setImageBitmap(BitmapFactory.decodeStream(mContext.getAssets()
						.open(String.valueOf(mItem))));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static class ViewHolder {
		int position;
		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public final Object instantiateItem(ViewGroup container, int position) {
		View view = null;
		view = getView(position, view, container);
		container.addView(view);
		return view;
	}

	@Override
	public final void destroyItem(ViewGroup container, int position,
			Object object) {
		View view = (View) object;
		container.removeView(view);
	}

	@Override
	public final boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void onClick(View v) {
		ViewHolder holder = (ViewHolder) v.getTag();
		if (holder != null && onBannerClickListener != null) {
			onBannerClickListener.onBannerClick(holder.position,
					imageIdList.get(holder.position));
		}
	}

	public interface OnBannerClickListener {
		public void onBannerClick(int position, Object t);
	}
}
