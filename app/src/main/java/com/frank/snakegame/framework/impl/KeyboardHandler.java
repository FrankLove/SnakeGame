package com.frank.snakegame.framework.impl;

import android.view.KeyEvent;
import android.view.View;

import com.frank.snakegame.framework.Input;
import com.frank.snakegame.framework.Pool;

import java.util.ArrayList;
import java.util.List;

public class KeyboardHandler implements View.OnKeyListener {

    private boolean[] pressedKyes = new boolean[128];
    private Pool<Input.KeyEvent> keyEventPool;
    private List<Input.KeyEvent> keyEventBuffer = new ArrayList<>();
    private List<Input.KeyEvent> keyEvents = new ArrayList<>();

    public KeyboardHandler(View view)
    {
        Pool.PoolObjectFactory<Input.KeyEvent> factory = new Pool.PoolObjectFactory<Input.KeyEvent>() {
            @Override
            public Input.KeyEvent createObject() {
                return new Input.KeyEvent();
            }
        };
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }

    public boolean isKeyPressed(int keyCode)
    {
        if (keyCode < 0 || keyCode > 127)
            return false;
        return pressedKyes[keyCode];
    }
}
