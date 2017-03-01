package usst.knowmorse.morsecode;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import usst.knowmorse.R;

public class FocusArea
        extends View
{
    String TAG = "focusarea";
    Paint border_paint = new Paint();
    public float coefficient = 2.5F;
    Rect cropFrame;

    public FocusArea(Context paramContext)
    {
        super(paramContext);
        int i = getWidth() / 2;
        int j = getHeight() / 2;
        float f = 20.0F * this.coefficient / 2.0F;
        int k = (int)(i - f);
        int m = (int)(f + i);
        this.cropFrame = new Rect(k, (int)(j - f), m, (int)(f + j));
        this.border_paint.setAntiAlias(true);
        this.border_paint.setStrokeWidth(1.0F);
        this.border_paint.setAlpha(200);
        this.border_paint.setColor(getResources().getColor(R.color.titleVenetianRed));
        this.border_paint.setStyle(Paint.Style.STROKE);
        this.border_paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public FocusArea(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        int i = getWidth() / 2;
        int j = getHeight() / 2;
        float f = 20.0F * this.coefficient / 2.0F;
        int k = (int)(i - f);
        int m = (int)(f + i);
        this.cropFrame = new Rect(k, (int)(j - f), m, (int)(f + j));
        this.border_paint.setAntiAlias(true);
        this.border_paint.setStrokeWidth(1.0F);
        this.border_paint.setAlpha(200);
        this.border_paint.setColor(getResources().getColor(R.color.titleVenetianRed));
        this.border_paint.setStyle(Paint.Style.STROKE);
        this.border_paint.setStrokeJoin(Paint.Join.ROUND);
    }

    protected void onDraw(Canvas paramCanvas)
    {
        super.onDraw(paramCanvas);
        int i = getWidth() / 2;
        int j = getHeight() / 2;
        float f = 20.0F * this.coefficient / 2.0F;
        int k = (int)(i - f);
        int m = (int)(f + i);
        int h=(int)(j - f);
        int w=(int)(f + j);
        this.cropFrame = new Rect(k, (int)(j - f), m, (int)(f + j));
        this.border_paint.setAntiAlias(true);
        this.border_paint.setStrokeWidth(1.0F);
        this.border_paint.setAlpha(200);
        this.border_paint.setColor(getResources().getColor(R.color.titleVenetianRed));
        this.border_paint.setStyle(Paint.Style.STROKE);
        this.border_paint.setStrokeJoin(Paint.Join.ROUND);
        paramCanvas.drawRect(this.cropFrame, this.border_paint);
        Log.d(TAG, "width1=" + (m-k) + " height=" + (w-h));
    }
}

