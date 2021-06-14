package com.frank.snakegame.framework.impl;

import android.graphics.Bitmap;

import com.frank.snakegame.framework.Graphics;
import com.frank.snakegame.framework.Pixmap;

public class AndroidPixmap implements Pixmap {

    protected Bitmap bitmap;
    protected Graphics.PixmapFormat foramt;

    public AndroidPixmap(Bitmap bitmap, Graphics.PixmapFormat pixmapForamt)
    {
        this.bitmap = bitmap;
        this.foramt = pixmapForamt;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Graphics.PixmapFormat getFormat() {
        return foramt;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}
