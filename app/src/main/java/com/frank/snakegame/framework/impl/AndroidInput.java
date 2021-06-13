package com.frank.snakegame.framework.impl;

import android.content.Context;
import android.view.View;

import com.frank.snakegame.framework.Input;

import java.util.List;

public class AndroidInput implements Input {

    private KeyboardHandler keyboardHandler;
    private TouchHandler touchHandler;

    public AndroidInput(Context context, View view,float scaleX,float scaleY)
    {
        keyboardHandler = new KeyboardHandler(view);
        touchHandler = new SingleTouchHandler(view,scaleX,scaleY);
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyboardHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public List<KeyEvent> getKeyEvents() {
        return null;
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
