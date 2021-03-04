package com.rental.transport.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.rental.transport.model.Calendar;

import java.util.List;

public class FreeTime extends View {

    private Paint busy = new Paint();
    private Paint free = new Paint();
    private Integer current;

    private List<Calendar> data;
    private int height = 100;

    public FreeTime(Context context, List<Calendar> data, int busyColor, int freeColor) {
        super(context);
        this.data = data;
        busy.setColor(busyColor);
        free.setColor(freeColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRect(0, height, this.getWidth(), 0, free);

        current = 0;

//        for(Calendar calendar : data)
//            appendTime(canvas, calendar.getStartAt(), calendar.getStopAt(), busy);
    }

    private void appendTime(Canvas canvas, Long start, Long stop, Paint paint) {

        int daySeconds = 60 * 60 * 24;

//        int percent = second;
//        int length = this.getWidth() / 100 * percent;
//        canvas.drawRect(current, height, current + length, 0, paint);
//        current += length;
    }
}
