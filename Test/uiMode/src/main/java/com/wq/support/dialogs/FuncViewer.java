package com.wq.support.dialogs;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.wq.uicore.R;
import com.wq.support.adapter.ViewHodler;
import com.wq.support.adapter.ViewHolderAdapter;
import com.wq.support.dialogs.FuncViewer.FuncAdapter.FuncItem;

/**
 * 功能弹出框
 * @author WQ 下午6:02:27
 */
public class FuncViewer {
	public Context mContext;
	List<?> data;
	Dialog dialog;
	public void showOperaDialog(OnItemClickListener itemClick) {
		View dialogView = View.inflate(mContext, R.layout.dialog_home, null);
		dialog = DialogFactory.createDialog(mContext, dialogView, true);
		ListView list = (ListView) dialogView.findViewById(R.id.list);
		list.setAdapter(new FuncAdapter(mContext, (List<FuncItem>) data));
		list.setOnItemClickListener(itemClick);
	}
	public void setArgment(List<?> data) {
		this.data = data;
	}
	public FuncViewer(Context mContext) {
		this.mContext = mContext;
	}
	public FuncViewer(Context mContext,List<?> data) {
		this.mContext = mContext;
		this.data=data;
	}
	public void dimiss() {
		dialog.cancel();
	}
	public static class FuncAdapter extends ViewHolderAdapter<FuncViewer.FuncAdapter.FuncItem> {

		/**
		 * @author WQ 下午4:14:18
		 * 
		 */
		public static class FuncItem {
			String text;
			boolean isLimit;
			public static int  defColor=Color.parseColor("#666666");
			int color=defColor;
			
			public int getColor() {
				return color;
			}

			public void setColor(int color) {
				this.color = color;
			}

			public String getText() {
				return text;
			}

			public void setText(String text) {
				this.text = text;
			}

			public FuncItem(String text) {
				super();
				this.text = text;
			}

			public FuncItem(String text, boolean isLimit) {
				super();
				this.text = text;
				this.isLimit = isLimit;
			}

		}

		private List<FuncItem> data;

		public FuncAdapter(Context context, List<FuncItem> data) {
			super(context, data, R.layout.dialog_item_home);
			this.data = data;
		}
		public FuncAdapter(Context context, List<FuncItem> data,int layId) {
			super(context, data, layId);
			this.data = data;
		}

		@Override
		public void fillView(ViewHodler holder, FuncItem mItem, int position) {
			holder.setText(R.id.text, mItem.text);
			View line = holder.getView(R.id.view_line);
			holder.setTextColor(R.id.text, mItem.getColor());
			if(position==this.getCount()-2)
			{
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,px2dip(holder.getmConvertView().getContext(),5));
				line.setLayoutParams(params);
			}
			else {
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,px2dip(holder.getmConvertView().getContext(),1));
				line.setLayoutParams(params);
			}
			holder.setVisibility(R.id.gap_line, false);
		}
		public static int px2dip(Context context, float pxValue) { 
			final float scale = context.getResources().getDisplayMetrics().density; 
			return (int) (pxValue / scale + 0.5f); 
			} 
			

	}
}
