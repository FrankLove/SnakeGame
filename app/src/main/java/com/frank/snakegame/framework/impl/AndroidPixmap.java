package com.frank.snakegame.framework.impl;

import android.graphics.Bitmap;

import com.frank.snakegame.framework.Graphics;
import com.frank.snakegame.framework.PixMap;

public class AndroidPixmap implements PixMap {

    protected Bitmap bitmap;
    protected Graphics.PixmapForamt foramt;

    public AndroidPixmap(Bitmap bitmap, Graphics.PixmapForamt pixmapForamt)
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
    public Graphics.PixmapForamt getFormat() {
        return foramt;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}
