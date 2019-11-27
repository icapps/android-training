package com.worldline.nicolaldi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author Nicola Verbeeck
 */
public class SignatureView extends View {

    private Path path = new Path();
    private Paint paint = new Paint();

    public SignatureView(Context context) {
        this(context, null);
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint.setColor(0xFFFF0000);
        paint.setStrokeWidth(context.getResources().getDisplayMetrics().density * 2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public Bitmap saveSignatureToFile() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawPath(path, paint);

        return bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(0, 0);
            return;
        }

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int requestedWidth = heightSize * 2;

        if (widthMode == MeasureSpec.EXACTLY) {
            requestedWidth = widthSize;
            if (heightMode == MeasureSpec.EXACTLY) {
                setMeasuredDimension(requestedWidth, heightSize);
                return;
            } else if (heightSize != requestedWidth / 2) {
                setMeasuredDimension(requestedWidth, requestedWidth / 2);
                return;
            }
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Blablablablabla
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
    }

    private float previousX;
    private float previousY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            path.moveTo(event.getX(), event.getY());
            previousX = event.getX();
            previousY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            path.quadTo(previousX, previousY, event.getX(), event.getY());
            invalidate();
            previousX = event.getX();
            previousY = event.getY();
        }

        return true;
    }
}
