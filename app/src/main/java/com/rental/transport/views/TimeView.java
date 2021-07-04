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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import lombok.AllArgsConstructor;

public class TimeView extends View {

    private Map<Integer, BusyBox> box = new HashMap();

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

        for (Map.Entry<Integer, BusyBox> entry : box.entrySet()) {
            if (box.get(entry.getKey()).type == EventTypeEnum.REQUEST)
                result.add(entry.getKey());
        }

        return result;
    }

    @AllArgsConstructor
    private class BusyBox {

        private float left = 0;
        private float right = 0;
        private float top = 0;
        private float bottom = 0;

        private EventTypeEnum type;
        private String text;
        private Calendar calendar;
        private Long object;

        public BusyBox(EventTypeEnum type, String text, Calendar calendar, Long object) {

            this.type = type;
            this.text = text;
            this.calendar = calendar;
            this.object = object;
        }
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

                for (Map.Entry<Integer, BusyBox> entry : box.entrySet()) {

                    Boolean myX = false;
                    Boolean myY = false;

                    if ((entry.getValue().left < x) && (x < entry.getValue().right))
                        myX = true;
                    if ((entry.getValue().top < y) && (y < entry.getValue().bottom))
                        myY = true;

                    if (myX && myY) {

                        if (box.get(entry.getKey()).type == EventTypeEnum.FREE) {
                            box.get(entry.getKey()).type = EventTypeEnum.REQUEST;
                            view.invalidate();
                            return box.get(entry.getKey()).type;
                        }

                        if (box.get(entry.getKey()).type == EventTypeEnum.REQUEST) {
                            box.get(entry.getKey()).type = EventTypeEnum.FREE;
                            view.invalidate();
                            return box.get(entry.getKey()).type;
                        }

                        if (box.get(entry.getKey()).type == EventTypeEnum.ORDER) {
                            MemoryService.getInstance().setOrderId(box.get(entry.getKey()).object);
                            MemoryService.getInstance().setCalendar(box.get(entry.getKey()).calendar);
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

        if (data == null)
            return;

        for (Integer hour = 0; hour < 24; hour++) {
            for (Event event : data) {
                if (event.getCalendar().getHours().contains(hour)) {
                    if (event.getType() == EventTypeEnum.GENERATED.getId())
                        break;

                    box.put(hour, new BusyBox(
                            EventTypeEnum.byId(event.getType()),
                            hour.toString() + ":00",
                            event.getCalendar(),
                            event.getObjectId()
                    ));
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
        textPaint.setTextSize(40);
        textPaint.setColor(Color.BLACK);

        Integer sequense = 0;

        Integer delimiter = box.size() / 2;
        if (box.size() % 2 != 0)
            delimiter++;

        Date date = new Date();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        Integer currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        Long currentDay = calendar.getTimeInMillis();

        Paint paintFrame = new Paint();
        paintFrame.setColor(Color.BLACK);

        for (Integer hour = min; hour <= max; hour++) {
            Paint paintBody = new Paint();
            BusyBox busyBox = box.get(hour);
            Long paintintedDay = box.get(hour).calendar.getDay();

            float textHeight = 0;
            Integer size = width / delimiter;

            // Рассчитываем координаты основываясь на delimiter
            if (sequense < delimiter) {
                busyBox.left = size * (sequense + 0);
                busyBox.right = size * (sequense + 1) - 2;
                busyBox.top = 2;
                busyBox.bottom = height / 2 - 1;
                textHeight = height / 4;
            } else {
                busyBox.left = size * (sequense - delimiter + 0);
                busyBox.right = size * (sequense - delimiter + 1) - 2;
                busyBox.top = height / 2 + 1;
                busyBox.bottom = height - 2;
                textHeight = height / 4 * 3;
            }

            if (paintintedDay < currentDay)
                paintBody.setColor(Color.GRAY);

            else if ((paintintedDay.equals(currentDay)) && (hour <= currentHour))
                paintBody.setColor(Color.GRAY);

            else {
                switch (busyBox.type) {
                    case GENERATED: {
                        paintBody.setColor(Color.GRAY);
                        break;
                    }

                    case ORDER: {
                        paintBody.setColor(Color.WHITE);
                        break;
                    }

                    case NOTEBOOK:
                    case BUSY: {
                        paintBody.setColor(Color.RED);
                        break;
                    }

                    case REQUEST: {
                        paintBody.setColor(Color.YELLOW);
                        break;
                    }

                    case FREE: {
                        paintBody.setColor(Color.GREEN);
                        break;
                    }
                }

                if (busyBox.calendar != null) {
                    switch (busyBox.type) {
                        case REQUEST:
                        case ORDER: {

                            if (hour == busyBox.calendar.getMinHour())
                                canvas.drawRect(busyBox.left - 2, busyBox.top - 2, busyBox.right, busyBox.bottom + 2, paintFrame);

                            if (hour == busyBox.calendar.getMaxHour())
                                canvas.drawRect(busyBox.left, busyBox.top - 2, busyBox.right + 2, busyBox.bottom + 2, paintFrame);

                            if ((busyBox.calendar.getMinHour() < hour) && (hour < busyBox.calendar.getMaxHour()))
                                canvas.drawRect(busyBox.left, busyBox.top - 2, busyBox.right, busyBox.bottom + 2, paintFrame);
                            break;
                        }

                        default:
                            break;
                    }
                }
            }

            canvas.drawRect(
                    busyBox.left,
                    busyBox.top,
                    busyBox.right,
                    busyBox.bottom,
                    paintBody
            );

            canvas.drawText(busyBox.text,
                    busyBox.left,
                    textHeight,
                    textPaint
            );

            sequense++;
        }
    }
}
