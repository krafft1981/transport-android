package com.rental.transport.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.model.Calendar;
import com.rental.transport.model.Event;
import com.rental.transport.service.MemoryService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;

public class TimeView extends View {

    private Map<Integer, BusyBox> box = new HashMap();
    private Map<Integer, Coordinate> pos = new HashMap();

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

    public Set<Integer> getHours() {

        Set<Integer> result = new HashSet();

        for (Map.Entry<Integer, Coordinate> entry : pos.entrySet()) {
            if (box.get(entry.getKey()).type == EventTypeEnum.REQUEST)
                result.add(entry.getKey());
        }

        return result;
    }

    public void clearHours() {

        for (Map.Entry<Integer, Coordinate> entry : pos.entrySet()) {
            if (box.get(entry.getKey()).type == EventTypeEnum.REQUEST)
                box.get(entry.getKey()).type = EventTypeEnum.FREE;
        }

        this.invalidate();
    }

    public Calendar getCalendar(Integer hour) {
        return box.get(hour).calendar;
    }

    @AllArgsConstructor
    private class BusyBox {
        private EventTypeEnum type;
        private String text;
        private Calendar calendar;
        private Long object;
    }

    private class Coordinate {
        float left = 0;
        float right = 0;
        float top = 0;
        float bottom = 0;
    }

    public EventTypeEnum click(View view, MotionEvent event) {

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
                        if (box.get(entry.getKey()).type == EventTypeEnum.FREE) {
                            box.get(entry.getKey()).type = EventTypeEnum.REQUEST;
                            this.invalidate();
                            return box.get(entry.getKey()).type;
                        }

                        if (box.get(entry.getKey()).type == EventTypeEnum.REQUEST) {
                            box.get(entry.getKey()).type = EventTypeEnum.FREE;
                            this.invalidate();
                            return box.get(entry.getKey()).type;
                        }

                        if (box.get(entry.getKey()).type == EventTypeEnum.ORDER) {
                            Calendar calendar = box.get(entry.getKey()).calendar;
                            clearHours();
                            MemoryService.getInstance().setCalendar(calendar);
                        }

                        if (box.get(entry.getKey()).type == EventTypeEnum.NOTEBOOK) {
                            Calendar calendar = box.get(entry.getKey()).calendar;
                            clearHours();
                            MemoryService.getInstance().setCalendar(calendar);
                        }

                        return box.get(entry.getKey()).type;
                    }
                }

                break;
            }
        }

        return EventTypeEnum.byId(0);
    }

    public void setData(List<Event> data) {

        box.clear();
        pos.clear();

        if (data == null)
            return;

        for (Integer hour = 0; hour < 24; hour++) {

            for (Event event : data) {
                if (event.getCalendar().getHours().contains(hour)) {
                    if (event.getType() != EventTypeEnum.GENERATED.getId()) {
                        EventTypeEnum type = EventTypeEnum.byId(event.getType());
                        String value = hour.toString() + ":00";
                        Long object = event.getObjectId();
                        Calendar calendar = event.getCalendar();
                        box.put(hour, new BusyBox(type, value, calendar, object));
                    }
                }
            }
        }
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
        textPaint.setColor(Color.BLACK);

        Integer sequence = 0;

        Integer delimiter = box.size() / 2;
        if (box.size() % 2 != 0)
            delimiter++;

        for (Integer hour = min; hour <= max; hour++) {
            Paint paint = new Paint();
            BusyBox busyBox = box.get(hour);
            switch (busyBox.type) {
                case ORDER:
                case NOTEBOOK: {
                    paint.setColor(Color.WHITE);
                    break;
                }

                case FREE: {
                    paint.setColor(Color.GREEN);
                    break;
                }

                case REQUEST: {
                    paint.setColor(Color.YELLOW);
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
            } else {
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
