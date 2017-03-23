package com.wq.support.ui.selectpicture;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.wq.support.adapter.ViewHodler;
import com.wq.support.adapter.ViewHolderAdapter;
import com.wq.support.utils.ToastUtil;
import com.wq.uicore.R;

public class MyAdapter extends ViewHolderAdapter<String>
{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static ArrayList<String> mSelectedImage = new ArrayList<String>();
	public static int max,selNum;
	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	MulitImageAct act;
	public MyAdapter(MulitImageAct context, List<String> mDatas, int itemLayoutId,
			String dirPath)
	{
		super(context, mDatas, itemLayoutId);
		this.act=context;
		this.mDirPath = dirPath;
	}


	@Override
	public void fillView(ViewHodler helper,final String item, int position) {
		//设置no_pic
				helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no); 
				//设置no_selected
						helper.setImageResource(R.id.id_item_select,
								R.drawable.picture_unselected);
				//设置图片
				helper.setImageURL(R.id.id_item_image, mDirPath + "/" + item);
				
				final ImageView mImageView = helper.getView(R.id.id_item_image);
				final ImageView mSelect = helper.getView(R.id.id_item_select);
				
				mImageView.setColorFilter(null);
				//设置ImageView的点击事件
				mImageView.setOnClickListener(new OnClickListener()
				{
					//选择，则将图片变暗，反之则反之
					@Override
					public void onClick(View v)
					{

						// 已经选择过该图片
						if (mSelectedImage.contains(mDirPath + "/" + item))
						{
							mSelectedImage.remove(mDirPath + "/" + item);
							mSelect.setImageResource(R.drawable.picture_unselected);
							mImageView.setColorFilter(null);
						} else
						// 未选择该图片
						{
							if(mSelectedImage.size()+selNum<max){
							mSelectedImage.add(mDirPath + "/" + item);
							mSelect.setImageResource(R.drawable.pictures_selected);
							mImageView.setColorFilter(Color.parseColor("#77000000"));
							}else {
								ToastUtil.shortM("你最多只能选"+max+"张图片"); 
							}
						}
						act.updateSelectNum(mSelectedImage.size()+selNum);

					}
				});
				
				/**
				 * 已经选择过的图片，显示出选择过的效果
				 */
				if (mSelectedImage.contains(mDirPath + "/" + item))
				{
					mSelect.setImageResource(R.drawable.pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}
	}
}
