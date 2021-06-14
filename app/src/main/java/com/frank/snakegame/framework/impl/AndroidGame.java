package com.frank.snakegame.framework.impl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import com.frank.snakegame.framework.Audio;
import com.frank.snakegame.framework.FileIO;
import com.frank.snakegame.framework.Game;
import com.frank.snakegame.framework.Graphics;
import com.frank.snakegame.framework.Input;
import com.frank.snakegame.framework.Screen;
import com.socks.library.KLog;

public abstract class AndroidGame extends AppCompatActivity implements Game {

    private AndroidFastRenderView renderView;
    private Graphics graphics;
    private Audio audio;
    private Input input;
    private FileIO fileIO;
    private Screen screen;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.i();
        getSupportActionBar().hide();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape?480:320;
        int frameBufferHeight = isLandscape?320:480;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,frameBufferHeight, Bitmap.Config.RGB_565);

        float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();

        renderView = new AndroidFastRenderView(this,frameBuffer);
        graphics = new AndroiGraphics(getAssets(),frameBuffer);
        fileIO = new AndroidFileIO(getApplicationContext());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this,renderView,scaleX,scaleY);
        screen = getStartScreen();
        setContentView(renderView);

//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(powerManager.FULL_WAKE_LOCK,"GLGame");
    }


    @Override
    protected void onResume() {
        super.onResume();
//        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        wakeLock.release();
        renderView.pause();
        screen.pause();
        if (isFinishing())
        {
            screen.dispose();
        }
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {

        KLog.i();
        if (screen == null)
        {
            throw new IllegalArgumentException("screen must not be null");
        }

        this.screen.pause();;
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;

    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

//    @Override
//    public Screen getStartScreen() {
//        return null;
//    }
}