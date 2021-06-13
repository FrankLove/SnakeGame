package com.frank.snakegame.framework.impl;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.frank.snakegame.framework.Graphics;
import com.frank.snakegame.framework.PixMap;

import java.io.IOException;
import java.io.InputStream;

public class AndroiGraphics implements Graphics {

    private AssetManager assetManager;
    private Bitmap frameBuffer;
    private Canvas canvas;
    private Paint paint;
    private Rect scrRect = new Rect();
    private Rect dstRect = new Rect();

    public AndroiGraphics(AssetManager assetManager,Bitmap frameBuffer)
    {
        this.assetManager = assetManager;
        this.frameBuffer = frameBuffer;
    }

    @Override
    public PixMap newPixmap(String fileName, PixmapForamt pixmapForamt) {
        Bitmap.Config config = null;
        if (pixmapForamt == PixmapForamt.RGB565)
        {
            config = Bitmap.Config.RGB_565;
        }
        else if (pixmapForamt == PixmapForamt.ARGB4444)
        {
            config = Bitmap.Config.ARGB_4444;
        }
        else
        {
            config = Bitmap.Config.ARGB_8888;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = config;

        InputStream inputStream = null;
        Bitmap bitmap = null;

        try {
            inputStream = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null)
            {
                throw new RuntimeException("couldn't not load bitmap from asset "+fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("couldn't not load bitmap from asset "+fileName);
        }
        finally {
            if (inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (bitmap.getConfig() == Bitmap.Config.RGB_565)
        {
            pixmapForamt = PixmapForamt.RGB565;
        }
        else  if (bitmap.getConfig() == Bitmap.Config.ARGB_4444)
        {
            pixmapForamt = PixmapForamt.ARGB4444;
        }
        else
        {
            pixmapForamt = PixmapForamt.ARGB8888;
        }
        return new AndroidPixmap(bitmap,pixmapForamt);
    }

    @Override
    public void clear(int color) {

        canvas.drawRGB((color & 0xff0000)>>16,(color & 0xff00)>>8,(color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x,y,paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x,y,x2,y2,paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x,y,x+width-1,y+width-1,paint);
    }

    @Override
    public void drawPixmap(PixMap pixMap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        scrRect.left = srcX;
        scrRect.top = srcY;
        scrRect.right = srcX + srcWidth - 1;
        scrRect.bottom = srcY + srcHeight - 1;
        canvas.drawBitmap(((AndroidPixmap)pixMap).bitmap,scrRect,dstRect,null);
    }

    @Override
    public void drawPixmap(PixMap pixMap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap)pixMap).bitmap,x,y,null);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
