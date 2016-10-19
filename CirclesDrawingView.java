package com.rogulya.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;

/**
 * Created by Chack on 20.10.2016.
 */

public class CirclesDrawingView extends View {

    private Paint mPaint;
    private HashSet<Rect> mRects = new HashSet<>();
    private SparseArray<Rect> mRectPointer = new SparseArray<>();

    public CirclesDrawingView(final Context ct) {
        super(ct);
        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);
        init(ct);
    }

    private void init(final Context ct) {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(40);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(final Canvas canv) {
        for (Rect rect : mRects) {
            canv.drawRect(rect, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = false;

        Rect touchedRect;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                touchedRect = obtainTouchedRect(xTouch, yTouch);
                mRectPointer.put(event.getPointerId(0), touchedRect);

                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                touchedRect = obtainTouchedRect(xTouch, yTouch);
                mRectPointer.put(pointerId, touchedRect);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    touchedRect = mRectPointer.get(pointerId);

                    if (null != touchedRect) {
                        touchedRect.top = yTouch - 30;
                        touchedRect.bottom = yTouch + 30;
                        touchedRect.left = xTouch - 30;
                        touchedRect.right = xTouch + 30;
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                pointerId = event.getPointerId(actionIndex);
                mRectPointer.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;

            default:
                // do nothing
                break;
        }

        return super.onTouchEvent(event) || handled;
    }

    private Rect obtainTouchedRect(final int xTouch, final int yTouch) {
        Rect touchedRect = getTouchedRect(xTouch, yTouch);
        if (null == touchedRect) {
            touchedRect = new Rect(xTouch - 30, yTouch - 30, xTouch + 30, yTouch + 30);
        }
        mRects.add(touchedRect);
        return touchedRect;
    }

    private Rect getTouchedRect(final int xTouch, final int yTouch) {
        Rect touched = null;

        for (Rect rect : mRects) {
            if (rect.left < xTouch && rect.right > xTouch && rect.top < yTouch && rect.bottom > yTouch) {
                touched = rect;
                break;
            }
        }
        return touched;
    }
}

