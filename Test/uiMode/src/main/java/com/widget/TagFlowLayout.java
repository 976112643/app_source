package com.widget;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.wq.uicore.R;

/**
 * Created by zhy on 15/9/10.
 */
public class TagFlowLayout extends FlowLayout implements TagAdapter.OnDataChangedListener, OnClickListener {
	private TagAdapter mTagAdapter;
	private boolean mSupportMulSelected = true;
	private int mSelectedMax = -1;// -1为不限制数量
	private static final String TAG = "TagFlowLayout";
	private MotionEvent mMotionEvent;
	private boolean hasLimitLine = true;
	int margin = 20;

	public boolean isHasLimitLine() {
		return hasLimitLine;
	}

	public void setItemSpace(int marg) {
		margin = marg;;
	}

	public void setHasLimitLine(boolean hasLimitLine) {
		this.hasLimitLine = hasLimitLine;
	}

	private Set<Integer> mSelectedView = new HashSet<Integer>();

	public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
		mSupportMulSelected = ta.getBoolean(R.styleable.TagFlowLayout_multi_suppout, true);
		mSelectedMax = ta.getInt(R.styleable.TagFlowLayout_max_select, -1);
		ta.recycle();

		if (mSupportMulSelected) {
			setClickable(true);
		}
	}

	public TagFlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TagFlowLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			TagView tagView = (TagView) getChildAt(i);
			if (tagView.getVisibility() == View.GONE)
				continue;
			if (tagView.getTagView().getVisibility() == View.GONE) {
				tagView.setVisibility(View.GONE);
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public interface OnSelectListener {
		void onSelected(Set<Integer> selectPosSet);
	}

	private OnSelectListener mOnSelectListener;

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
		if (mOnSelectListener != null)
			setClickable(true);
	}

	public interface OnTagClickListener {
		boolean onTagClick(View view, int position, FlowLayout parent);
	}

	private OnTagClickListener mOnTagClickListener;

	public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
		mOnTagClickListener = onTagClickListener;
		if (onTagClickListener != null)
			setClickable(true);
	}

	public void setAdapter(TagAdapter adapter) {
		// if (mTagAdapter == adapter)
		// return;
		mTagAdapter = adapter;
		mTagAdapter.setOnDataChangedListener(this);
		changeAdapter();

	}

	private void changeAdapter() {
		removeAllViews();
		TagAdapter adapter = mTagAdapter;
		TagView tagViewContainer = null;
		HashSet preCheckedList = mTagAdapter.getPreCheckedList();
		for (int i = 0, len = adapter.getCount(); i < len; i++) {
			View tagView = adapter.getView(this, i, adapter.getItem(i));

			tagViewContainer = new TagView(getContext());
			tagViewContainer.position = i;
			ViewGroup.MarginLayoutParams clp = (ViewGroup.MarginLayoutParams) tagView.getLayoutParams();
			// ViewGroup.MarginLayoutParams lp = new
			// ViewGroup.MarginLayoutParams(clp);
			// lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
			// lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			// lp.topMargin = clp.topMargin;
			// lp.bottomMargin = clp.bottomMargin;
			// lp.leftMargin = clp.leftMargin;
			// lp.rightMargin = clp.rightMargin;
			clp.rightMargin = margin;
			tagViewContainer.setOnClickListener(this);
			tagView.setDuplicateParentStateEnabled(false);
			tagViewContainer.setLayoutParams(clp);
			tagViewContainer.addView(tagView);
			if (hasLimitLine && i != len - 1) {// 添加分割线
				View view = new View(getContext());
				view.setBackgroundColor(getResources().getColor(Color.parseColor("#dddddd")));
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, LayoutParams.MATCH_PARENT);
				params.leftMargin = 10;
				params.rightMargin = 10;
				tagViewContainer.addView(view, params);
			}
			tagViewContainer.setPadding(0, 10, 0, 10);
			addView(tagViewContainer);

			if (preCheckedList.contains(i)) {
				tagViewContainer.setChecked(true);
			}
		}
		mSelectedView.addAll(preCheckedList);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			mMotionEvent = MotionEvent.obtain(event);
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean performClick() {
		if (mMotionEvent == null)
			return super.performClick();

		int x = (int) mMotionEvent.getX();
		int y = (int) mMotionEvent.getY();
		mMotionEvent = null;

		TagView child = findChild(x, y);
		int pos = findPosByView(child);
		if (child != null) {
			doSelect(child, pos);
			if (mOnTagClickListener != null) { return mOnTagClickListener.onTagClick(child.getTagView(), pos, this); }
		}
		return super.performClick();
	}

	public void setMaxSelectCount(int count) {
		if (mSelectedView.size() > count) {
			Log.w(TAG, "you has already select more than " + count + " views , so it will be clear .");
			mSelectedView.clear();
		}
		mSelectedMax = count;
	}

	public Set<Integer> getSelectedList() {
		return new HashSet<Integer>(mSelectedView);
	}

	private void doSelect(TagView child, int position) {
		if (mSupportMulSelected) {
			if (!child.isChecked()) {
				// 处理max_select=1的情况
				if (mSelectedMax == 1 && mSelectedView.size() == 1) {
					Iterator<Integer> iterator = mSelectedView.iterator();
					Integer preIndex = iterator.next();
					TagView pre = (TagView) getChildAt(preIndex);
					pre.setChecked(false);
					child.setChecked(true);
					mSelectedView.remove(preIndex);
					mSelectedView.add(position);
				} else {
					if (mSelectedMax > 0 && mSelectedView.size() >= mSelectedMax)
						return;
					child.setChecked(true);
					mSelectedView.add(position);
				}
			} else {
				child.setChecked(false);
				mSelectedView.remove(position);
			}
			if (mOnSelectListener != null) {
				mOnSelectListener.onSelected(new HashSet<Integer>(mSelectedView));
			}
		}
	}

	private static final String KEY_CHOOSE_POS = "key_choose_pos";
	private static final String KEY_DEFAULT = "key_default";

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState());

		String selectPos = "";
		if (mSelectedView.size() > 0) {
			for (int key : mSelectedView) {
				selectPos += key + "|";
			}
			selectPos = selectPos.substring(0, selectPos.length() - 1);
		}
		bundle.putString(KEY_CHOOSE_POS, selectPos);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			String mSelectPos = bundle.getString(KEY_CHOOSE_POS);
			if (!TextUtils.isEmpty(mSelectPos)) {
				String[] split = mSelectPos.split("\\|");
				for (String pos : split) {
					int index = Integer.parseInt(pos);
					mSelectedView.add(index);

					TagView tagView = (TagView) getChildAt(index);
					tagView.setChecked(true);
				}

			}
			super.onRestoreInstanceState(bundle.getParcelable(KEY_DEFAULT));
			return;
		}
		super.onRestoreInstanceState(state);
	}

	private int findPosByView(View child) {
		final int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			View v = getChildAt(i);
			if (v == child)
				return i;
		}
		return -1;
	}

	private TagView findChild(int x, int y) {
		final int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			TagView v = (TagView) getChildAt(i);
			if (v.getVisibility() == View.GONE)
				continue;
			Rect outRect = new Rect();
			v.getHitRect(outRect);
			if (outRect.contains(x, y)) { return v; }
		}
		return null;
	}

	@Override
	public void onChanged() {
		changeAdapter();
	}

	@Override
	public void onClick(View v) {
		if (mOnTagClickListener != null && v instanceof TagView) {
			TagView child = (TagView) v;
			mOnTagClickListener.onTagClick(child.getTagView(),child.position, this);
		}

	}

}

class TagView extends LinearLayout implements Checkable {
	private boolean isChecked;
	public int position = 0;
	private static final int[] CHECK_STATE = new int[]
	{ android.R.attr.state_checked };

	public TagView(Context context) {
		super(context);
		setOrientation(HORIZONTAL);
	}

	public View getTagView() {
		return getChildAt(0);
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		int[] states = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(states, CHECK_STATE);
		}
		return states;
	}

	/**
	 * Change the checked state of the view
	 *
	 * @param checked
	 *            The new checked state
	 */
	@Override
	public void setChecked(boolean checked) {
		if (this.isChecked != checked) {
			this.isChecked = checked;
			refreshDrawableState();
		}
	}

	/**
	 * @return The current checked state of the view
	 */
	@Override
	public boolean isChecked() {
		return isChecked;
	}

	/**
	 * Change the checked state of the view to the inverse of its current state
	 */
	@Override
	public void toggle() {
		setChecked(!isChecked);
	}

}
