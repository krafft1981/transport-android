package com.rental.transport.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.model.Event;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class TimeView extends View {

    @AllArgsConstructor
    private class BusyBox {
        private Integer type;
        private String text;
    }

    @Getter
    @Setter
    private class Coordinate {
        float left = 0;
        float right = 0;
        float top = 0;
        float bottom = 0;
    }

    public void click(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                break;
            case MotionEvent.ACTION_MOVE: // движение
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: { // отпускание

                float x = event.getX();
                float y = event.getY();

                for (Map.Entry<Integer, Coordinate> entry : pos.entrySet()) {

                    Boolean myX = false;
                    Boolean myY = false;

                    if ((entry.getValue().left < x) && (x < entry.getValue().right)) myX = true;
                    if ((entry.getValue().top < y) && (y < entry.getValue().bottom)) myY = true;

                    if (myX && myY) {
                        if (box.get(entry.getKey()).type == EventTypeEnum.FREE.getId()) {
                            box.get(entry.getKey()).type = EventTypeEnum.REQUEST.getId();

                            view.invalidate();
                            break;
                        }

                        if (box.get(entry.getKey()).type == EventTypeEnum.REQUEST.getId()) {
                            box.get(entry.getKey()).type = EventTypeEnum.FREE.getId();
                            view.invalidate();
                            break;
                        }
                    }
                }

                break;
            }
        }
    }

    private Map<Integer, BusyBox> box = new HashMap();
    private Map<Integer, Coordinate> pos = new HashMap();

    public void setData(Map<Integer, Event> data) {

        box.clear();

        for (Integer hour = 0; hour < 24; hour++) {
            if (data.get(hour).getType() != EventTypeEnum.GENERATED.getId())
                box.put(
                        hour,
                        new BusyBox(
                                data.get(hour).getType(),
                                hour.toString() + ":00"
                        )
                );
        }
    }

    public TimeView(Context context) {
        super(context);
        init(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Integer min = Integer.MAX_VALUE;
        Integer max = Integer.MIN_VALUE;

        for (Map.Entry<Integer, BusyBox> entry : box.entrySet()) {
            min = Math.min(min, entry.getKey());
            max = Math.max(max, entry.getKey());
        }

        Integer height = this.getHeight();
        Integer width = this.getWidth();

        Paint textPaint = new Paint();
        textPaint.setTextSize(30);
        textPaint.setColor(Color.RED);

        Integer sequence = 0;

        Integer delimiter = box.size() / 2;
        if (box.size() % 2 != 0)
            delimiter++;

        for (Integer hour = min; hour <= max; hour++) {
            Paint paint = new Paint();
            BusyBox busyBox = box.get(hour);
            switch (busyBox.type) {
                case 1: { //"Generated"
                    paint.setColor(Color.GRAY);
                    break;
                }

                case 2:   //"Unavailable"
                case 4:   //"Order"
                case 5: { //"Busy"
                    paint.setColor(Color.RED);
                    break;
                }

                case 3: { //"Request"
                    paint.setColor(Color.YELLOW);
                    break;
                }

                case 6: { //"Free"
                    paint.setColor(Color.GREEN);
                    break;
                }
            }

            Coordinate coordinate = new Coordinate();

            float textHeight = 0;
            Integer size = width / delimiter;

            // Рассчитываем координаты основываясь на delimiter
            if (sequence < delimiter) {

                coordinate.left = size * (sequence + 0);
                coordinate.right = size * (sequence + 1) - 2;
                coordinate.top = 0;
                coordinate.bottom = height / 2 - 1;

                textHeight = height / 4;
            }
            else {
                coordinate.left = size * (sequence - delimiter + 0);
                coordinate.right = size * (sequence - delimiter + 1) - 2;

                coordinate.top = height / 2 + 1;
                coordinate.bottom = height;

                textHeight = height / 4 * 3;
            }

            canvas.drawRect(
                    coordinate.left,
                    coordinate.top,
                    coordinate.right,
                    coordinate.bottom,
                    paint
            );

            canvas.drawText(busyBox.text,
                    coordinate.left,
                    textHeight,
                    textPaint
            );

            pos.put(hour, coordinate);
            sequence++;
        }
    }
}
