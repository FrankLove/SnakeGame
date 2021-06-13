package com.frank.snakegame.framework.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements  Runnable {

    private AndroidGame game;
    private Bitmap framebuffer;
    private Thread renderThread = null;
    private SurfaceHolder holder;
    private volatile boolean running = false;

    public AndroidFastRenderView(AndroidGame game,Bitmap framebuffer) {
        super(game);
        holder = getHolder();
        this.game = game;
        this.framebuffer = framebuffer;
    }


    public void resume()
    {
        running = true;
        renderThread = new Thread();
        renderThread.start();
    }

    @Override
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while (running)
        {
            if (!holder.getSurface().isValid())
            {
                continue;
            }

            float deltaTime = (System.nanoTime() - startTime) /1000000000.0f;
            startTime = System.nanoTime();

            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().present(deltaTime);

            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer,null,dstRect,null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause()   {
        running = false;
        while (true)
        {
            try {
                renderThread.join();
                break;
            }
            catch (InterruptedException e)
            {

            }
        }
    }
}
