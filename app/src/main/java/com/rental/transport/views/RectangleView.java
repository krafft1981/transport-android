package com.rental.transport.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;

public class RectangleView extends View {

    private Paint paint = new Paint();

    public RectangleView(Context context, int color) {
        super(context);
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Toast
                .makeText(getContext(), String.valueOf(this.getHeight()) + " " + String.valueOf(this.getWidth()), Toast.LENGTH_LONG)
                .show();

        canvas.drawRect(0, this.getHeight(), this.getWidth(), 0, paint);
    }
}
