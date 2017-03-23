package com.wq.support.utils;

import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 手势工具类,目前只提供控件双击事件
 * @author WQ 下午4:23:58
 */
public class GestureUtils {
	public static abstract class DoubleClickListener implements OnTouchListener, OnGestureListener, OnDoubleTapListener {
		GestureDetector gestureScanner;

		public DoubleClickListener(View view) {
			gestureScanner = new GestureDetector(view.getContext(), this);
			gestureScanner.setOnDoubleTapListener(this);
			view.setOnTouchListener(this);

		}

		public abstract boolean onDoubleClick() ;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return gestureScanner.onTouchEvent(event);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			
			return onDoubleClick();
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
