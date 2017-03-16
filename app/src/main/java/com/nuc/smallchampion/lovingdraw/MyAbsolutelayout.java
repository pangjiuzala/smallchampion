package com.nuc.smallchampion.lovingdraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;

public class MyAbsolutelayout extends AbsoluteLayout {
	private static final String TAG = "SelfAbsoluteLayout";
	Rect mr = new Rect();
	Canvas mCanvas;
	public static Canvas mdrawCanvas;
	Bitmap metBitmap;
	public static Bitmap mdrawBitmap;
	public static Bitmap mBitmap;
	boolean isChange;
	boolean isMove;
	float mx;
	float my;
	Path mPath;
	public static Paint mPaint;
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	/**
	 * @param context
	 */
	public MyAbsolutelayout(Context context) {
		super(context);
		init();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MyAbsolutelayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		this.setBackgroundColor(Color.WHITE);
		mBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
		mdrawBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mdrawCanvas = new Canvas(mdrawBitmap);
		mPath = new Path();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFF000000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5);
		mdrawCanvas.save(Canvas.ALL_SAVE_FLAG);
	}

	public Rect changeToPicture() {
		this.removeAllViews();
		isChange = true;
		drawEtBitmap();
		this.invalidate();
		return mr;
	}

	public void changeLayer(int x, int y) {
		Log.e(TAG, "changeLayer");
		mBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		// EditText above self draw layer
		if (mr.contains(x, y)) {

			mCanvas.drawBitmap(mdrawBitmap, 0f, 0f, null);
			if (metBitmap != null) {
				mCanvas.drawBitmap(metBitmap, mx, my, null);
			}
		} else {
			if (metBitmap != null) {
				mCanvas.drawBitmap(metBitmap, mx, my, null);
			}
			mCanvas.drawBitmap(mdrawBitmap, 0f, 0f, null);
		}
		this.invalidate();
	}

	private void drawEtBitmap() {
		mBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_4444);
		mCanvas = new Canvas(mBitmap);
		mCanvas.drawBitmap(mdrawBitmap, 0f, 0f, null);
		if (isChange && metBitmap != null) {

			mCanvas.drawBitmap(metBitmap, mx, my, null);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isChange) {
			canvas.drawBitmap(mdrawBitmap, 0, 0, null);
		} else {

			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
		canvas.drawPath(mPath, mPaint);
	}

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mdrawCanvas.drawPath(mPath, mPaint);
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		isMove = false;
		float x = event.getX();
		float y = event.getY();
		if (isChange) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					touch_start(x, y);

					invalidate();
					break;
				case MotionEvent.ACTION_MOVE:
					isMove = true;
					touch_move(x, y);
					invalidate();
					return true;
				case MotionEvent.ACTION_UP:
					touch_up();
					invalidate();
					break;
			}
			if (!isMove) {
				this.changeLayer((int) x, (int) y);
				return true;
			}
		}
		return super.onTouchEvent(event);
	}

}
