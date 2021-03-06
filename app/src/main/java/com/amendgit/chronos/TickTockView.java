package com.amendgit.chronos;

/**
 * Created by Ali Muzaffar on 10/03/2016.
 * Modified by amendgit on 03/11/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class TickTockView extends View {
    private Paint mFillPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mEmptyPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMiddlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF mArc    = new RectF();
    private Point mCenter = new Point();

    private Canvas mCanvas;
    private Bitmap mCanvasBitmap;
    private Matrix mMatrix = new Matrix();

    private float mRingRadius    = 100;
    private float mRingThickness = 3;
    private float mDotRadius     = 6;

    private int mEmptyRingColor = Color.WHITE;
    private int mFillRingColor  = Color.BLUE;
    private int mMiddleColor    = Color.TRANSPARENT;
    private int mTextColor      = Color.WHITE;

    private CharSequence mTickTockText = null;
    private float mTextSize    = 80;
    private float mTextPadding = 16;
    private Rect mTextBounds   = new Rect();

    private long mRemainTimeInterval = 0;
    private long mTotalTimeInterval = 0;

    private boolean mCounterClockwise = false;
    private boolean mAutoFitText = true;

    private final int DURATION_MINUTE = 0;
    private final int DURATION_TOTAL = 1;
    private int mCircleDuration = DURATION_MINUTE;

    final String TAG = "TickTockView";

    public TickTockView(Context context) {
        super(context);
        init(context, null);
    }

    public TickTockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TickTockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TickTockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;

        mRingRadius    *= multi;
        mRingThickness *= multi;
        mDotRadius     *= multi;
        mTextPadding   *= multi;

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TickTockView, 0, 0);
        try {
            mEmptyRingColor = styledAttrs.getColor(R.styleable.TickTockView_tickEmptyRingColor, mEmptyRingColor);
            mFillRingColor  = styledAttrs.getColor(R.styleable.TickTockView_tickFillRingColor,  mFillRingColor);
            mMiddleColor    = styledAttrs.getColor(R.styleable.TickTockView_tickMiddleColor,    mMiddleColor);
            mTextColor      = styledAttrs.getColor(R.styleable.TickTockView_tickTextColor,      mTextColor);

            mRingThickness = styledAttrs.getDimension(R.styleable.TickTockView_tickRingThickness, mRingThickness);
            mDotRadius     = styledAttrs.getDimension(R.styleable.TickTockView_tickDotRadius,     mDotRadius);
            mTextSize      = styledAttrs.getDimension(R.styleable.TickTockView_tickTextSize,      mTextSize);

            mTickTockText = styledAttrs.getText(R.styleable.TickTockView_tickText);
            mAutoFitText = styledAttrs.getBoolean(R.styleable.TickTockView_tickAutoFitText, mAutoFitText);

            mCounterClockwise = styledAttrs.getBoolean(R.styleable.TickTockView_tickMoveCounterClockwise, mCounterClockwise);
            mCircleDuration   = styledAttrs.getInt(R.styleable.TickTockView_tickCircleDuration,           mCircleDuration);
        } finally {
            styledAttrs.recycle();
        }
        mEmptyPaint.setColor(mEmptyRingColor);
        mFillPaint.setColor(mFillRingColor);

        mFillPaint.setStrokeWidth(mRingThickness);
        mEmptyPaint.setStrokeWidth(mRingThickness);

        mMiddlePaint.setColor(mMiddleColor);
        if (mMiddleColor == Color.TRANSPARENT) {
            mMiddlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        calculateEverything();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed) {
            return;
        }
        calculateEverything();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRingRadius = ((this.getMeasuredWidth() - this.getPaddingLeft()) / 2) - mDotRadius;
        if (!TextUtils.isEmpty(mTickTockText)) {
            fitText(mTickTockText);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.drawInitialCircle();

        this.calculateArc();

        long ms = mRemainTimeInterval % 60000;
        float angle = (float) (ms * 0.006);

        if (mCircleDuration == DURATION_TOTAL && mRemainTimeInterval > 0) {
            long totalTime = mTotalTimeInterval;
            float percentage = (((float) mRemainTimeInterval) / ((float) totalTime));
            angle = 360f * percentage;
        }

        // The fill
        if (!mCounterClockwise) {
            mCanvas.drawArc(mArc, 270, angle, true, mFillPaint);
        } else {
            mCanvas.drawArc(mArc, 270, 360-angle, true, mFillPaint);
        }

        // Clear the center
        mCanvas.drawCircle(mCenter.x, mCenter.y, mRingRadius - mRingThickness, mMiddlePaint);

        this.drawDot();

        if (!mCounterClockwise) {
            mMatrix.setRotate(-angle, mCanvas.getWidth() / 2, mCanvas.getHeight() / 2);
        } else {
            mMatrix.setRotate(angle, mCanvas.getWidth() / 2, mCanvas.getHeight() / 2);
        }

        canvas.drawBitmap(mCanvasBitmap, mMatrix, null);

        if (!TextUtils.isEmpty(mTickTockText)) {
            canvas.drawText(mTickTockText.toString(),
                    getWidth() / 2 - mTextBounds.width() / 2,
                    getHeight() / 2 + mTextBounds.height() / 2,
                    mTextPaint);
        }
    }

    private void drawDot() {
        float centerX = (mCanvas.getWidth() / 2);
        float centerY = mDotRadius + mRingThickness / 2;
        mCanvas.drawCircle(centerX, centerY, mDotRadius, mFillPaint);
    }

    private void calculateEverything() {
        calculateCenter();
        calculateArc();
    }

    private void drawInitialCircle() {
        if (mCanvas == null ||
                mCanvasBitmap.getWidth() != this.getWidth() ||
                mCanvasBitmap.getHeight() != this.getHeight()) {
            if (mCanvasBitmap != null) {
                mCanvas = null;
                mCanvasBitmap.recycle();
                mCanvasBitmap = null;
            }
            mCanvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mCanvasBitmap);
        }

        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mCanvas.drawCircle(mCenter.x, mCenter.y, mRingRadius, mEmptyPaint);
    }

    private void calculateCenter() {
        mCenter.x = ((getWidth() + getPaddingLeft()) - getPaddingRight()) / 2;
        mCenter.y = ((getHeight() + getPaddingTop()) - getPaddingBottom()) / 2;
    }

    private void calculateArc() {
        mArc.left   = mCenter.x - mRingRadius;
        mArc.top    = mCenter.y - mRingRadius;
        mArc.right  = mCenter.x + mRingRadius;
        mArc.bottom = mCenter.y + mRingRadius;
    }

    private void updateTickTockText(long millis) {
        String text = DateUtils.millisecondToHHHMMSS(millis);
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (mTickTockText != null && mTickTockText.equals(text)) {
            return;
        }

        if (mTickTockText != null && mTickTockText.length() != text.length()) {
            mTextPaint.getTextBounds(text, 0, text.length(), mTextBounds);
        }

        mTickTockText = text;

        this.invalidate();
    }

    public void setRemainTimeInterval(long millis) {
        mRemainTimeInterval = millis;
        this.updateTickTockText(millis);
    }

    public void setTotalTimeInterval(long totalTimeInterval) {
        mTotalTimeInterval = totalTimeInterval;
    }

    private void fitText(CharSequence text) {
        if (TextUtils.isEmpty(text)) { return; }
        if (mAutoFitText) {
            float textWidth = mFillPaint.measureText(text.toString());
            float multi = ((mRingRadius * 2) - mTextPadding * 2) / textWidth;
            mTextPaint.setTextSize(mFillPaint.getTextSize() * multi);
        }
        mTextPaint.getTextBounds(mTickTockText.toString(), 0, mTickTockText.length(), mTextBounds);
    }
}