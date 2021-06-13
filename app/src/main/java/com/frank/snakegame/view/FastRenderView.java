package com.frank.snakegame.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class FastRenderView extends SurfaceView implements  Runnable {
    private Thread renderThread = null;
    private SurfaceHolder holder;
    private volatile boolean running = false;

    public FastRenderView(Context context) {
        super(context);
        holder = getHolder();
    }


    public void resume()
    {
        running = true;
        renderThread = new Thread();
        renderThread.start();
    }

    @Override
    public void run() {
        while (running)
        {
            if (!holder.getSurface().isValid())
            {
                continue;
            }

            Canvas canvas = holder.lockCanvas();
            canvas.drawRGB(255,0,0);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause()   {
        running = false;
        while (true)
        {
            try {
                renderThread.join();
            }
            catch (InterruptedException e)
            {

            }
        }
    }
}
