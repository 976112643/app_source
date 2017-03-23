package com.widget;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 分块显示文字的TextView
 * 
 * @author WQ 下午1:44:33
 */
public class SpanTextView extends TextView {
	public boolean dontConsumeNonUrlClicks = true;
	public boolean linkHit;

	public SpanTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SpanTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SpanTextView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		linkHit = false;
//		boolean res = super.onTouchEvent(event);
//
//		if (dontConsumeNonUrlClicks)
//			return linkHit;
//		return res;
		
		 // 二、有误触textview内链接BUG  
        if (getMovementMethod() == null) {  
            boolean result = super.onTouchEvent(event);  
            return result;  
        }  
        MovementMethod m = getMovementMethod();  
        setMovementMethod(null);  
        boolean mt = m.onTouchEvent(this, (Spannable) getText(), event);  
        if (mt && event.getAction() == MotionEvent.ACTION_DOWN) {  
            event.setAction(MotionEvent.ACTION_UP);  
            mt = m.onTouchEvent(this, (Spannable) getText(), event);  
            event.setAction(MotionEvent.ACTION_DOWN);  
        }  
        boolean st = super.onTouchEvent(event);  
        setMovementMethod(m);  
        setFocusable(false);  
        return mt || st;  

	}

	public static class LocalLinkMovementMethod extends LinkMovementMethod {
		static LocalLinkMovementMethod sInstance;
		private NoLineClickSpan mPressedSpan;

		public static LocalLinkMovementMethod getInstance() {
			if (sInstance == null)
				sInstance = new LocalLinkMovementMethod();
			return sInstance;
		}

		@Override
		public boolean onTouchEvent(TextView widget, Spannable buffer,
				MotionEvent event) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_DOWN) {
				mPressedSpan = getPressedSpan(widget, buffer, event);
				if (mPressedSpan != null) {
					mPressedSpan.setPressed(true);
					Selection.setSelection(buffer,
							buffer.getSpanStart(mPressedSpan),
							buffer.getSpanEnd(mPressedSpan));
				}
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				NoLineClickSpan touchedSpan = getPressedSpan(widget, buffer,
						event);
				if (mPressedSpan != null && touchedSpan != mPressedSpan) {
					mPressedSpan.setPressed(false);
					mPressedSpan = null;
					Selection.removeSelection(buffer);
				}
			} else {
				if (mPressedSpan != null) {
					mPressedSpan.setPressed(false);
					super.onTouchEvent(widget, buffer, event);
				}
				mPressedSpan = null;
				Selection.removeSelection(buffer);
			}

			if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_DOWN) {
				int x = (int) event.getX();
				int y = (int) event.getY();

				x -= widget.getTotalPaddingLeft();
				y -= widget.getTotalPaddingTop();

				x += widget.getScrollX();
				y += widget.getScrollY();

				Layout layout = widget.getLayout();
				int line = layout.getLineForVertical(y);
				int off = layout.getOffsetForHorizontal(line, x);

				ClickableSpan[] link = buffer.getSpans(off, off,
						ClickableSpan.class);

				if (link.length != 0) {
					if (action == MotionEvent.ACTION_UP) {
						link[0].onClick(widget);
					} else if (action == MotionEvent.ACTION_DOWN) {
						Selection.setSelection(buffer,
								buffer.getSpanStart(link[0]),
								buffer.getSpanEnd(link[0]));
					}

					if (widget instanceof SpanTextView) {
						((SpanTextView) widget).linkHit = true;
					}
					return true;
				} else {
					Selection.removeSelection(buffer);
					Touch.onTouchEvent(widget, buffer, event);
					return false;
				}
			}
			return Touch.onTouchEvent(widget, buffer, event);
		}

		private NoLineClickSpan getPressedSpan(TextView textView,
				Spannable spannable, MotionEvent event) {

			int x = (int) event.getX();
			int y = (int) event.getY();

			x -= textView.getTotalPaddingLeft();
			y -= textView.getTotalPaddingTop();

			x += textView.getScrollX();
			y += textView.getScrollY();

			Layout layout = textView.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);

			NoLineClickSpan[] link = spannable.getSpans(off, off,
					NoLineClickSpan.class);
			NoLineClickSpan touchedSpan = null;
			if (link.length > 0) {
				touchedSpan = link[0];
			}
			return touchedSpan;
		}
	}

	public static class SpanEditable {
		SpannableString span;
		String content;
		String tempStr;

		public SpanEditable(String content) {
			span = new SpannableString(content);
			this.content = content;
		}

		public SpanEditable setSpan(String str, Object what) {
			int index = content.indexOf(str);
			if (index != -1)
				setSpan(what, index, index + str.length());
			return this;
		}
		public SpanEditable setColorSpanAll(String str, int color){
			int start=0;
			for (; start!=-1;) {
				int index = content.indexOf(str,start);
				if (index != -1) {
					setSpan(new ForegroundColorSpan(color), index, index + str.length());
					start= index + str.length();
				}else {
					start = index;
				}
			}
			return  this;
		}
		public SpanEditable setLasSpan(String str, Object what) {
			int index = content.lastIndexOf(str);
			if (index != -1)
				setSpan(what, index, index + str.length());
			return this;
		}

		public SpanEditable setSpan(Object what, int start, int end) {
			span.setSpan(what, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			return this;
		}

		public SpannableString commit() {
			return span;
		}
	}
	/**
	 * 自定义可点击块,去下划线
	 * 
	 * @author WQ 上午11:26:11
	 */
	public abstract static class NoLineClickSpan extends ClickableSpan {
		private boolean mIsPressed;
		int color;

		/**
		 * 指定可点击块文字颜色,默认蓝色
		 * 
		 * @param linkColor
		 */
		public NoLineClickSpan(int linkColor) {
			super();
			this.color = linkColor;
		}

		public NoLineClickSpan() {
			super();
		}

		// 评论项

		public void setPressed(boolean isSelected) {
			mIsPressed = isSelected;
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(color == 0 ? ds.linkColor : color);
			ds.bgColor = mIsPressed ? 0x33000000 : 0x00000000;
			ds.setUnderlineText(false); // 去掉下划线

		}

		@Override
		final public void onClick(View widget) {
			if(!isQuck(400)){
				click(widget);
			}
		}
		/**
		 * 判断是否快速点击
		 * 
		 * @param mm
		 *            间隔时间(毫秒)
		 * @return
		 */
		private  long time;
		public  boolean isQuck(long mm) {
			long temp = System.currentTimeMillis();
			if (temp - time <= mm)
				return true;
			time = temp;
			return false;
		}

		public abstract void click(View widget);
	}
}
