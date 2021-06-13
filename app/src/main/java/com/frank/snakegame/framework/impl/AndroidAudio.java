package com.frank.snakegame.framework.impl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.frank.snakegame.framework.Audio;
import com.frank.snakegame.framework.Music;
import com.frank.snakegame.framework.Sound;

import java.io.IOException;

public class AndroidAudio implements Audio {

    private AssetManager assetManager;
    private SoundPool soundPool;

    public AndroidAudio(Activity activity)
    {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        assetManager = activity.getAssets();
        soundPool = new SoundPool(20,AudioManager.STREAM_MUSIC,0);
    }

    @Override
    public Music newMusic(String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(fileName);
            return new AndroidMusic(assetFileDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("couldn't load music "+fileName);
        }
    }

    @Override
    public Sound newSound(String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor,0);
            return new AndroidSound(soundPool,soundId);
        } catch (IOException e) {
            throw new RuntimeException("couldn't load sound "+fileName);
        }
    }
}
