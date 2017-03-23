package com.widget;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import com.wq.uicore.R;

public class FlowLayout extends ViewGroup {
	private static final int DEFAULT_HORIZONTAL_SPACING = 5;
	private static final int DEFAULT_VERTICAL_SPACING = 5;
	//
	private int mVerticalSpacing;
	private int mHorizontalSpacing;
	private final ConfigDefinition config;
	List<LineDefinition> lines = new ArrayList<LineDefinition>();
	List<ViewDefinition> views = new ArrayList<ViewDefinition>();

	public FlowLayout(Context context) {
		super(context);
		this.config = new ConfigDefinition();
		readStyleParameters(context, null);
	}

	public FlowLayout(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.config = new ConfigDefinition();
		readStyleParameters(context, attributeSet);
	}

	public FlowLayout(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
		this.config = new ConfigDefinition();
		readStyleParameters(context, attributeSet);
	}

	private void readStyleParameters(Context context, AttributeSet attributeSet) {
		TypedArray a = context.obtainStyledAttributes(attributeSet,
				R.styleable.FlowLayout);
		try {
			this.config.setOrientation(a.getInteger(
					R.styleable.FlowLayout_android_orientation,
					CommonLogic.HORIZONTAL));
			this.config.setDebugDraw(a.getBoolean(
					R.styleable.FlowLayout_debugDraw, false));
			this.config.setWeightDefault(a.getFloat(
					R.styleable.FlowLayout_weightDefault, 0.0f));
			this.config
					.setGravity(a.getInteger(
							R.styleable.FlowLayout_android_gravity,
							Gravity.NO_GRAVITY));
			this.config.setLayoutDirection(a.getInteger(
					R.styleable.FlowLayout_layoutDirection,
					View.LAYOUT_DIRECTION_LTR));
		} finally {
			a.recycle();
		}
	}

	public void setHorizontalSpacing(int pixelSize) {
		mHorizontalSpacing = pixelSize;
	}

	public void setVerticalSpacing(int pixelSize) {
		mVerticalSpacing = pixelSize;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int count = this.getChildCount();
		views.clear();
		lines.clear();
		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}

			final LayoutParams lp = (LayoutParams) child.getLayoutParams();

			child.measure(
					getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft()
							+ this.getPaddingRight(), lp.width),
					getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop()
							+ this.getPaddingBottom(), lp.height));
			if (i != count - 1 && mHorizontalSpacing != 0) {
				lp.rightMargin = mHorizontalSpacing;
			}
			if (mVerticalSpacing != 0) {
				lp.topMargin = mVerticalSpacing;
			}
			ViewDefinition view = new ViewDefinition(this.config, child);
			view.setWidth(child.getMeasuredWidth());
			view.setHeight(child.getMeasuredHeight());
			view.setNewLine(lp.isNewLine());
			view.setGravity(lp.getGravity());
			view.setWeight(lp.getWeight());
			view.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin,
					lp.bottomMargin);
			views.add(view);
		}
		
		

		this.config.setMaxWidth(MeasureSpec.getSize(widthMeasureSpec)
				- this.getPaddingRight() - this.getPaddingLeft());
		this.config.setMaxHeight(MeasureSpec.getSize(heightMeasureSpec)
				- this.getPaddingTop() - this.getPaddingBottom());
		this.config.setWidthMode(MeasureSpec.getMode(widthMeasureSpec));
		this.config.setHeightMode(MeasureSpec.getMode(heightMeasureSpec));
		this.config
				.setCheckCanFit(this.config.getLengthMode() != View.MeasureSpec.UNSPECIFIED);

		CommonLogic.fillLines(views, lines, config);
		CommonLogic.calculateLinesAndChildPosition(lines);

		int contentLength = 0;
		final int linesCount = lines.size();
		for (int i = 0; i < linesCount; i++) {
			LineDefinition l = lines.get(i);
			contentLength = Math.max(contentLength, l.getLineLength());
		}

		LineDefinition currentLine = lines.get(lines.size() - 1);
		int contentThickness = currentLine.getLineStartThickness()
				+ currentLine.getLineThickness();
		int realControlLength = CommonLogic.findSize(
				this.config.getLengthMode(), this.config.getMaxLength(),
				contentLength);
		int realControlThickness = CommonLogic.findSize(
				this.config.getThicknessMode(), this.config.getMaxThickness(),
				contentThickness);

		CommonLogic.applyGravityToLines(lines, realControlLength,
				realControlThickness, config);

		for (int i = 0; i < linesCount; i++) {
			LineDefinition line = lines.get(i);
			applyPositionsToViews(line);
		}

		/* need to take padding into account */
		int totalControlWidth = this.getPaddingLeft() + this.getPaddingRight();
		int totalControlHeight = this.getPaddingBottom() + this.getPaddingTop();
		if (this.config.getOrientation() == CommonLogic.HORIZONTAL) {
			totalControlWidth += contentLength;
			totalControlHeight += contentThickness;
		} else {
			totalControlWidth += contentThickness;
			totalControlHeight += contentLength;
		}
		this.setMeasuredDimension(
				resolveSize(totalControlWidth, widthMeasureSpec),
				resolveSize(totalControlHeight, heightMeasureSpec));
	}

	private void applyPositionsToViews(LineDefinition line) {
		final List<ViewDefinition> childViews = line.getViews();
		final int childCount = childViews.size();
		for (int i = 0; i < childCount; i++) {
			final ViewDefinition child = childViews.get(i);
			final View view = child.getView();
			view.measure(MeasureSpec.makeMeasureSpec(child.getWidth(),
					MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
					child.getHeight(), MeasureSpec.EXACTLY));
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int linesCount = this.lines.size();
		for (int i = 0; i < linesCount; i++) {
			final LineDefinition line = this.lines.get(i);

			final int count = line.getViews().size();
			for (int j = 0; j < count; j++) {
				ViewDefinition child = line.getViews().get(j);
				View view = child.getView();
				LayoutParams lp = (LayoutParams) view.getLayoutParams();
				view.layout(
						this.getPaddingLeft() + line.getX()
								+ child.getInlineX() + lp.leftMargin,
						this.getPaddingTop() + line.getY() + child.getInlineY()
								+ lp.topMargin,
						this.getPaddingLeft() + line.getX()
								+ child.getInlineX() + lp.leftMargin
								+ child.getWidth(),
						this.getPaddingTop() + line.getY() + child.getInlineY()
								+ lp.topMargin + child.getHeight());
			}
		}
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		boolean more = super.drawChild(canvas, child, drawingTime);
		this.drawDebugInfo(canvas, child);
		return more;
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
		return new LayoutParams(this.getContext(), attributeSet);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	private void drawDebugInfo(Canvas canvas, View child) {
		if (!isDebugDraw()) { return; }

		Paint childPaint = this.createPaint(0xffffff00);
		Paint newLinePaint = this.createPaint(0xffff0000);

		LayoutParams lp = (LayoutParams) child.getLayoutParams();

		if (lp.rightMargin > 0) {
			float x = child.getRight();
			float y = child.getTop() + child.getHeight() / 2.0f;
			canvas.drawLine(x, y, x + lp.rightMargin, y, childPaint);
			canvas.drawLine(x + lp.rightMargin - 4.0f, y - 4.0f, x
					+ lp.rightMargin, y, childPaint);
			canvas.drawLine(x + lp.rightMargin - 4.0f, y + 4.0f, x
					+ lp.rightMargin, y, childPaint);
		}

		if (lp.leftMargin > 0) {
			float x = child.getLeft();
			float y = child.getTop() + child.getHeight() / 2.0f;
			canvas.drawLine(x, y, x - lp.leftMargin, y, childPaint);
			canvas.drawLine(x - lp.leftMargin + 4.0f, y - 4.0f, x
					- lp.leftMargin, y, childPaint);
			canvas.drawLine(x - lp.leftMargin + 4.0f, y + 4.0f, x
					- lp.leftMargin, y, childPaint);
		}

		if (lp.bottomMargin > 0) {
			float x = child.getLeft() + child.getWidth() / 2.0f;
			float y = child.getBottom();
			canvas.drawLine(x, y, x, y + lp.bottomMargin, childPaint);
			canvas.drawLine(x - 4.0f, y + lp.bottomMargin - 4.0f, x, y
					+ lp.bottomMargin, childPaint);
			canvas.drawLine(x + 4.0f, y + lp.bottomMargin - 4.0f, x, y
					+ lp.bottomMargin, childPaint);
		}

		if (lp.topMargin > 0) {
			float x = child.getLeft() + child.getWidth() / 2.0f;
			float y = child.getTop();
			canvas.drawLine(x, y, x, y - lp.topMargin, childPaint);
			canvas.drawLine(x - 4.0f, y - lp.topMargin + 4.0f, x, y
					- lp.topMargin, childPaint);
			canvas.drawLine(x + 4.0f, y - lp.topMargin + 4.0f, x, y
					- lp.topMargin, childPaint);
		}

		if (lp.isNewLine()) {
			if (this.config.getOrientation() == CommonLogic.HORIZONTAL) {
				float x = child.getLeft();
				float y = child.getTop() + child.getHeight() / 2.0f;
				canvas.drawLine(x, y - 6.0f, x, y + 6.0f, newLinePaint);
			} else {
				float x = child.getLeft() + child.getWidth() / 2.0f;
				float y = child.getTop();
				canvas.drawLine(x - 6.0f, y, x + 6.0f, y, newLinePaint);
			}
		}
	}

	private Paint createPaint(int color) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		paint.setStrokeWidth(2.0f);
		return paint;
	}

	public int getOrientation() {
		return this.config.getOrientation();
	}

	public void setOrientation(int orientation) {
		this.config.setOrientation(orientation);
		this.requestLayout();
	}

	public boolean isDebugDraw() {
		return this.config.isDebugDraw() || debugDraw();
	}

	public void setDebugDraw(boolean debugDraw) {
		this.config.setDebugDraw(debugDraw);
		this.invalidate();
	}

	private boolean debugDraw() {
		try {
			// android add this method at 4.1
			Method m = ViewGroup.class.getDeclaredMethod("debugDraw",
					(Class[]) null);
			m.setAccessible(true);
			return  (Boolean) m.invoke(this, new Object[]
			{ null });
		} catch (Exception e) {
			// if no such method (android not support this at lower api level),
			// return false
			// ignore this, it's safe here
		}

		return false;
	}

	public float getWeightDefault() {
		return this.config.getWeightDefault();
	}

	public void setWeightDefault(float weightDefault) {
		this.config.setWeightDefault(weightDefault);
		this.requestLayout();
	}

	public int getGravity() {
		return this.config.getGravity();
	}

	public void setGravity(int gravity) {
		this.config.setGravity(gravity);
		this.requestLayout();
	}

	public int getLayoutDirection() {
		if (this.config == null) {
			// Workaround for android sdk that wants to use virtual methods
			// within constructor.
			return View.LAYOUT_DIRECTION_LTR;
		}

		return this.config.getLayoutDirection();
	}

	public void setLayoutDirection(int layoutDirection) {
		this.config.setLayoutDirection(layoutDirection);
		this.requestLayout();
	}
	public static class ViewDefinition {
	    private final ConfigDefinition config;
	    private final View view;
	    private int inlineStartLength;
	    private float weight;
	    private int gravity;
	    private boolean newLine;
	    private int inlineStartThickness;
	    private int width;
	    private int height;
	    private int leftMargin;
	    private int topMargin;
	    private int rightMargin;
	    private int bottomMargin;

	    public ViewDefinition(ConfigDefinition config, View child) {
	        this.config = config;
	        this.view = child;
	    }

	    public int getLength() {
	        return this.config.getOrientation() == CommonLogic.HORIZONTAL ? width : height;
	    }

	    public void setLength(int length) {
	        if (this.config.getOrientation() == CommonLogic.HORIZONTAL) {
	            width = length;
	        } else {
	            height = length;
	        }
	    }

	    public int getSpacingLength() {
	        return this.config.getOrientation() == CommonLogic.HORIZONTAL ? this.leftMargin + this.rightMargin : this.topMargin + this.bottomMargin;
	    }

	    public int getThickness() {
	        return this.config.getOrientation() == CommonLogic.HORIZONTAL ? height : width;
	    }

	    public void setThickness(int thickness) {
	        if (this.config.getOrientation() == CommonLogic.HORIZONTAL) {
	            height = thickness;
	        } else {
	            width = thickness;
	        }
	    }

	    public int getSpacingThickness() {
	        return this.config.getOrientation() == CommonLogic.HORIZONTAL ? this.topMargin + this.bottomMargin : this.leftMargin + this.rightMargin;
	    }

	    public float getWeight() {
	        return weight;
	    }

	    public void setWeight(float weight) {
	        this.weight = weight;
	    }

	    public boolean weightSpecified() {
	        return this.weight >= 0;
	    }

	    public int getInlineStartLength() {
	        return inlineStartLength;
	    }

	    public void setInlineStartLength(int inlineStartLength) {
	        this.inlineStartLength = inlineStartLength;
	    }

	    public boolean gravitySpecified() {
	        return gravity != Gravity.NO_GRAVITY;
	    }

	    public int getGravity() {
	        return gravity;
	    }

	    public void setGravity(int gravity) {
	        this.gravity = gravity;
	    }

	    public boolean isNewLine() {
	        return newLine;
	    }

	    public void setNewLine(boolean newLine) {
	        this.newLine = newLine;
	    }

	    public View getView() {
	        return view;
	    }

	    public int getWidth() {
	        return width;
	    }

	    public void setWidth(int width) {
	        this.width = width;
	    }

	    public int getHeight() {
	        return height;
	    }

	    public void setHeight(int height) {
	        this.height = height;
	    }

	    public int getInlineStartThickness() {
	        return inlineStartThickness;
	    }

	    public void setInlineStartThickness(int inlineStartThickness) {
	        this.inlineStartThickness = inlineStartThickness;
	    }

	    public void setMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
	        this.leftMargin = leftMargin;
	        this.topMargin = topMargin;
	        this.rightMargin = rightMargin;
	        this.bottomMargin = bottomMargin;
	    }

	    public int getInlineX() {
	        return this.config.getOrientation() == CommonLogic.HORIZONTAL ? this.inlineStartLength : this.inlineStartThickness;
	    }
	    public int getInlineY() {
	        return this.config.getOrientation() == CommonLogic.HORIZONTAL ? this.inlineStartThickness : this.inlineStartLength;
	    }
	}
	public static class LineDefinition {
	    private final List<ViewDefinition> views = new ArrayList<ViewDefinition>();
	    private final ConfigDefinition config;
	    private int lineLength;
	    private int lineThickness;
	    private int lineStartThickness;
	    private int lineStartLength;

	    public LineDefinition(ConfigDefinition config) {
	        this.config = config;
	        this.lineStartThickness = 0;
	        this.lineStartLength = 0;
	    }

	    public void addView(ViewDefinition child) {
	        this.addView(this.views.size(), child);
	    }

	    public void addView(int i, ViewDefinition child) {
	        this.views.add(i, child);

	        this.lineLength = this.lineLength + child.getLength() + child.getSpacingLength();
	        this.lineThickness = Math.max(this.lineThickness, child.getThickness() + child.getSpacingThickness());
	    }

	    public boolean canFit(ViewDefinition child) {
	        return lineLength + child.getLength() + child.getSpacingLength() <= config.getMaxLength();
	    }

	    public int getLineStartThickness() {
	        return lineStartThickness;
	    }

	    public void setLineStartThickness(int lineStartThickness) {
	        this.lineStartThickness = lineStartThickness;
	    }

	    public int getLineThickness() {
	        return lineThickness;
	    }

	    public int getLineLength() {
	        return lineLength;
	    }

	    public int getLineStartLength() {
	        return lineStartLength;
	    }

	    public void setLineStartLength(int lineStartLength) {
	        this.lineStartLength = lineStartLength;
	    }

	    public List<ViewDefinition> getViews() {
	        return views;
	    }

	    public void setThickness(int thickness) {
	        this.lineThickness = thickness;
	    }

	    public void setLength(int length) {
	        this.lineLength = length;
	    }

	    public int getX() {
	        return this.config.getOrientation() == CommonLogic.HORIZONTAL ? this.lineStartLength : this.lineStartThickness;
	    }
	    public int getY() {
	        return this.config.getOrientation() == CommonLogic.HORIZONTAL ? this.lineStartThickness : this.lineStartLength;
	    }
	}
	public class ConfigDefinition {
	    private int orientation = CommonLogic.HORIZONTAL;
	    private boolean debugDraw = false;
	    private float weightDefault = 0;
	    private int gravity = Gravity.LEFT | Gravity.TOP;
	    private int layoutDirection = View.LAYOUT_DIRECTION_LTR;
	    private int maxWidth;
	    private int maxHeight;
	    private boolean checkCanFit;
	    private int widthMode;
	    private int heightMode;

	    public ConfigDefinition() {
	        this.setOrientation(CommonLogic.HORIZONTAL);
	        this.setDebugDraw(false);
	        this.setWeightDefault(0.0f);
	        this.setGravity(Gravity.NO_GRAVITY);
	        this.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
	        this.setCheckCanFit(true);
	    }

	    public int getOrientation() {
	        return this.orientation;
	    }

	    public void setOrientation(int orientation) {
	        if (orientation == CommonLogic.VERTICAL) {
	            this.orientation = orientation;
	        } else {
	            this.orientation = CommonLogic.HORIZONTAL;
	        }
	    }

	    public boolean isDebugDraw() {
	        return this.debugDraw;
	    }

	    public void setDebugDraw(boolean debugDraw) {
	        this.debugDraw = debugDraw;
	    }

	    public float getWeightDefault() {
	        return this.weightDefault;
	    }

	    public void setWeightDefault(float weightDefault) {
	        this.weightDefault = Math.max(0, weightDefault);
	    }

	    public int getGravity() {
	        return this.gravity;
	    }

	    public void setGravity(int gravity) {
	        this.gravity = gravity;
	    }

	    public int getLayoutDirection() {
	        return layoutDirection;
	    }

	    public void setLayoutDirection(int layoutDirection) {
	        if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
	            this.layoutDirection = layoutDirection;
	        } else {
	            this.layoutDirection = View.LAYOUT_DIRECTION_LTR;
	        }
	    }

	    public void setMaxWidth(int maxWidth) {
	        this.maxWidth = maxWidth;
	    }

	    public void setMaxHeight(int maxHeight) {
	        this.maxHeight = maxHeight;
	    }

	    public int getMaxLength() {
	        return this.orientation == CommonLogic.HORIZONTAL ? this.maxWidth : this.maxHeight;
	    }

	    public int getMaxThickness() {
	        return this.orientation == CommonLogic.HORIZONTAL ? this.maxHeight : this.maxWidth;
	    }

	    public void setCheckCanFit(boolean checkCanFit) {
	        this.checkCanFit = checkCanFit;
	    }

	    public boolean isCheckCanFit() {
	        return checkCanFit;
	    }

	    public void setWidthMode(int widthMode) {
	        this.widthMode = widthMode;
	    }

	    public void setHeightMode(int heightMode) {
	        this.heightMode = heightMode;
	    }

	    public int getLengthMode() {
	        return this.orientation == CommonLogic.HORIZONTAL ? this.widthMode : this.heightMode;
	    }

	    public int getThicknessMode() {
	        return this.orientation == CommonLogic.HORIZONTAL ? this.heightMode : this.widthMode;
	    }
	}
	
	public static class CommonLogic {
	    public static final int HORIZONTAL = 0;
	    public static final int VERTICAL = 1;

	    public static void calculateLinesAndChildPosition(List<LineDefinition> lines) {
	        int prevLinesThickness = 0;
	        final int linesCount = lines.size();
	        for (int i = 0; i < linesCount; i++) {
	            final LineDefinition line = lines.get(i);
	            line.setLineStartThickness(prevLinesThickness);
	            prevLinesThickness += line.getLineThickness();
	            int prevChildThickness = 0;
	            final List<ViewDefinition> childViews = line.getViews();
	            final int childCount = childViews.size();
	            for (int j = 0; j < childCount; j++) {
	                ViewDefinition child = childViews.get(j);
	                child.setInlineStartLength(prevChildThickness);
	                prevChildThickness += child.getLength() + child.getSpacingLength();
	            }
	        }
	    }

	    public static void applyGravityToLines(List<LineDefinition> lines, int realControlLength, int realControlThickness, ConfigDefinition config) {
	        final int linesCount = lines.size();
	        if (linesCount <= 0) {
	            return;
	        }

	        final int totalWeight = linesCount;
	        LineDefinition lastLine = lines.get(linesCount - 1);
	        int excessThickness = realControlThickness - (lastLine.getLineThickness() + lastLine.getLineStartThickness());

	        if (excessThickness < 0) {
	            excessThickness = 0;
	        }

	        int excessOffset = 0;
	        for (int i = 0; i < linesCount; i++) {
	            final LineDefinition child = lines.get(i);
	            int weight = 1;
	            int gravity = getGravity(null, config);
	            int extraThickness = Math.round(excessThickness * weight / totalWeight);

	            final int childLength = child.getLineLength();
	            final int childThickness = child.getLineThickness();

	            Rect container = new Rect();
	            container.top = excessOffset;
	            container.left = 0;
	            container.right = realControlLength;
	            container.bottom = childThickness + extraThickness + excessOffset;

	            Rect result = new Rect();
	            Gravity.apply(gravity, childLength, childThickness, container, result);

	            excessOffset += extraThickness;
	            child.setLineStartLength(child.getLineStartLength() + result.left);
	            child.setLineStartThickness(child.getLineStartThickness() + result.top);
	            child.setLength(result.width());
	            child.setThickness(result.height());

	            applyGravityToLine(child, config);
	        }
	    }

	    public static void applyGravityToLine(LineDefinition line, ConfigDefinition config) {
	        final List<ViewDefinition> views = line.getViews();
	        final int viewCount = views.size();
	        if (viewCount <= 0) {
	            return;
	        }

	        float totalWeight = 0;
	        for (int i = 0; i < viewCount; i++) {
	            final ViewDefinition child = views.get(i);
	            totalWeight += getWeight(child, config);
	        }

	        ViewDefinition lastChild = views.get(viewCount - 1);
	        int excessLength = line.getLineLength() - (lastChild.getLength() + lastChild.getSpacingLength() + lastChild.getInlineStartLength());
	        int excessOffset = 0;
	        for (int i = 0; i < viewCount; i++) {
	            final ViewDefinition child = views.get(i);
	            float weight = getWeight(child, config);
	            int gravity = getGravity(child, config);
	            int extraLength;
	            if (totalWeight == 0) {
	                extraLength = excessLength / viewCount;
	            } else {
	                extraLength = Math.round(excessLength * weight / totalWeight);
	            }

	            final int childLength = child.getLength() + child.getSpacingLength();
	            final int childThickness = child.getThickness() + child.getSpacingThickness();

	            Rect container = new Rect();
	            container.top = 0;
	            container.left = excessOffset;
	            container.right = childLength + extraLength + excessOffset;
	            container.bottom = line.getLineThickness();

	            Rect result = new Rect();
	            Gravity.apply(gravity, childLength, childThickness, container, result);

	            excessOffset += extraLength;
	            child.setInlineStartLength(result.left + child.getInlineStartLength());
	            child.setInlineStartThickness(result.top);
	            child.setLength(result.width() - child.getSpacingLength());
	            child.setThickness(result.height() - child.getSpacingThickness());
	        }
	    }

	    public static int findSize(int modeSize, int controlMaxSize, int contentSize) {
	        int realControlSize;
	        switch (modeSize) {
	            case View.MeasureSpec.UNSPECIFIED:
	                realControlSize = contentSize;
	                break;
	            case View.MeasureSpec.AT_MOST:
	                realControlSize = Math.min(contentSize, controlMaxSize);
	                break;
	            case View.MeasureSpec.EXACTLY:
	                realControlSize = controlMaxSize;
	                break;
	            default:
	                realControlSize = contentSize;
	                break;
	        }
	        return realControlSize;
	    }

	    private static float getWeight(ViewDefinition child, ConfigDefinition config) {
	        return child.weightSpecified() ? child.getWeight() : config.getWeightDefault();
	    }


	    private static int getGravity(ViewDefinition child, ConfigDefinition config) {
	        int parentGravity = config.getGravity();

	        int childGravity;
	        // get childGravity of child view (if exists)
	        if (child != null && child.gravitySpecified()) {
	            childGravity = child.getGravity();
	        } else {
	            childGravity = parentGravity;
	        }

	        childGravity = getGravityFromRelative(childGravity, config);
	        parentGravity = getGravityFromRelative(parentGravity, config);

	        // add parent gravity to child gravity if child gravity is not specified
	        if ((childGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == 0) {
	            childGravity |= parentGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
	        }
	        if ((childGravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
	            childGravity |= parentGravity & Gravity.VERTICAL_GRAVITY_MASK;
	        }

	        // if childGravity is still not specified - set default top - left gravity
	        if ((childGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == 0) {
	            childGravity |= Gravity.LEFT;
	        }
	        if ((childGravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
	            childGravity |= Gravity.TOP;
	        }

	        return childGravity;
	    }


	    public static int getGravityFromRelative(int childGravity, ConfigDefinition config) {
	        // swap directions for vertical non relative view
	        // if it is relative, then START is TOP, and we do not need to switch it here.
	        // it will be switched later on onMeasure stage when calculations will be with length and thickness
	        if (config.getOrientation() == CommonLogic.VERTICAL && (childGravity & Gravity.RELATIVE_LAYOUT_DIRECTION) == 0) {
	            int horizontalGravity = childGravity;
	            childGravity = 0;
	            childGravity |= (horizontalGravity & Gravity.HORIZONTAL_GRAVITY_MASK) >> Gravity.AXIS_X_SHIFT << Gravity.AXIS_Y_SHIFT;
	            childGravity |= (horizontalGravity & Gravity.VERTICAL_GRAVITY_MASK) >> Gravity.AXIS_Y_SHIFT << Gravity.AXIS_X_SHIFT;
	        }

	        // for relative layout and RTL direction swap left and right gravity
	        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL && (childGravity & Gravity.RELATIVE_LAYOUT_DIRECTION) != 0) {
	            int ltrGravity = childGravity;
	            childGravity = 0;
	            childGravity |= (ltrGravity & Gravity.LEFT) == Gravity.LEFT ? Gravity.RIGHT : 0;
	            childGravity |= (ltrGravity & Gravity.RIGHT) == Gravity.RIGHT ? Gravity.LEFT : 0;
	        }

	        return childGravity;
	    }

	    public static void fillLines(List<ViewDefinition> views, List<LineDefinition> lines, ConfigDefinition config) {
	        LineDefinition currentLine = new LineDefinition(config);
	        lines.add(currentLine);
	        final int count = views.size();
	        for (int i = 0; i < count; i++) {
	            final ViewDefinition child = views.get(i);

	            boolean newLine = child.isNewLine() || (config.isCheckCanFit() && !currentLine.canFit(child));
	            if (newLine) {
	                currentLine = new LineDefinition(config);
	                if (config.getOrientation() == CommonLogic.VERTICAL && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
	                    lines.add(0, currentLine);
	                } else {
	                    lines.add(currentLine);
	                }
	            }

	            if (config.getOrientation() == CommonLogic.HORIZONTAL && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
	                currentLine.addView(0, child);
	            } else {
	                currentLine.addView(child);
	            }
	        }
	    }
	}
	public static class LayoutParams extends MarginLayoutParams {
		@ViewDebug.ExportedProperty(mapping =
		{
				@ViewDebug.IntToString(from = Gravity.NO_GRAVITY, to = "NONE"),
				@ViewDebug.IntToString(from = Gravity.TOP, to = "TOP"),
				@ViewDebug.IntToString(from = Gravity.BOTTOM, to = "BOTTOM"),
				@ViewDebug.IntToString(from = Gravity.LEFT, to = "LEFT"),
				@ViewDebug.IntToString(from = Gravity.RIGHT, to = "RIGHT"),
				@ViewDebug.IntToString(from = Gravity.CENTER_VERTICAL, to = "CENTER_VERTICAL"),
				@ViewDebug.IntToString(from = Gravity.FILL_VERTICAL, to = "FILL_VERTICAL"),
				@ViewDebug.IntToString(from = Gravity.CENTER_HORIZONTAL, to = "CENTER_HORIZONTAL"),
				@ViewDebug.IntToString(from = Gravity.FILL_HORIZONTAL, to = "FILL_HORIZONTAL"),
				@ViewDebug.IntToString(from = Gravity.CENTER, to = "CENTER"),
				@ViewDebug.IntToString(from = Gravity.FILL, to = "FILL") }) private boolean newLine = false;
		private int gravity = Gravity.NO_GRAVITY;
		private float weight = -1.0f;

		public LayoutParams(Context context, AttributeSet attributeSet) {
			super(context, attributeSet);
			this.readStyleParameters(context, attributeSet);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(ViewGroup.LayoutParams layoutParams) {
			super(layoutParams);
		}

		private void readStyleParameters(Context context,
				AttributeSet attributeSet) {
			TypedArray a = context.obtainStyledAttributes(attributeSet,
					R.styleable.FlowLayout_LayoutParams);
			try {
				this.newLine = a.getBoolean(
						R.styleable.FlowLayout_LayoutParams_layout_newLine,
						false);
				this.gravity = a
						.getInt(R.styleable.FlowLayout_LayoutParams_android_layout_gravity,
								Gravity.NO_GRAVITY);
				this.weight = a.getFloat(
						R.styleable.FlowLayout_LayoutParams_layout_weight,
						-1.0f);
			} finally {
				a.recycle();
			}
		}

		public int getGravity() {
			return gravity;
		}

		public void setGravity(int gravity) {
			this.gravity = gravity;
		}

		public float getWeight() {
			return weight;
		}

		public void setWeight(float weight) {
			this.weight = weight;
		}

		public boolean isNewLine() {
			return newLine;
		}

		public void setNewLine(boolean newLine) {
			this.newLine = newLine;
		}
	}
}
