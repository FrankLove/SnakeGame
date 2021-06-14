package com.frank.snakegame.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.socks.library.KLog;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    AndroidGame game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    
    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }

    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }      
    
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while(running) {  
            if(!holder.getSurface().isValid())
                continue;           
            
            float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();

//            KLog.i("deltaTime "+deltaTime);
            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().present(deltaTime);
            
            Canvas canvas = holder.lockCanvas();
//            KLog.i(getWidth()+" "+getHeight());
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);
//            KLog.i(canvas.getWidth()+" "+canvas.getHeight());
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }        
}