package com.frank.snakegame.framework.impl;

import android.view.MotionEvent;
import android.view.View;

import com.frank.snakegame.framework.Input;
import com.frank.snakegame.framework.Pool;

import java.util.ArrayList;
import java.util.List;

public class SingleTouchHandler implements TouchHandler {

    private boolean isTouched;
    private int touchX;
    private int touchY;
    private Pool<Input.TouchEvent> touchEventPool;
    private List<Input.TouchEvent> touchEvents = new ArrayList<>();
    private List<Input.TouchEvent> touchEventBuffer = new ArrayList<>();
    private float scaleX;
    private float scaleY;

    public SingleTouchHandler(View view,float scaleX,float scaleY)
    {
        Pool.PoolObjectFactory<Input.TouchEvent> factory = new Pool.PoolObjectFactory<Input.TouchEvent>() {
            @Override
            public Input.TouchEvent createObject() {
                return new Input.TouchEvent();
            }
        };
        touchEventPool = new Pool<Input.TouchEvent>(factory,100);
        view.setOnTouchListener(this);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized (this)
        {
            if (pointer == 0)
            {
                return isTouched;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this)
        {
            return touchX;
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this)
        {
            return touchY;
        }
    }

    @Override
    public List<Input.TouchEvent> getTouchEvents() {
        synchronized (this)
        {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();
            touchEvents.addAll(touchEventBuffer);
            touchEventBuffer.clear();
            return touchEvents;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        synchronized (this)
        {
            Input.TouchEvent touchEvent = touchEventPool.newObject();
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchEvent.type = Input.TouchEvent.TOUCH_DRAGGED;
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent.type = Input.TouchEvent.TOUCH_UP;
                    isTouched = false;
                    break;
            }
            touchEvent.x = touchX = (int) (motionEvent.getX()*scaleX);
            touchEvent.y = touchY = (int) (motionEvent.getY()*scaleY);
            touchEventBuffer.add(touchEvent);
        }
        return true;
    }
}
