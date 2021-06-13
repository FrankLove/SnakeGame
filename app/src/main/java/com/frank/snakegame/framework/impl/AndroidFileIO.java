package com.frank.snakegame.framework.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.frank.snakegame.framework.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AndroidFileIO implements FileIO {

    private Context context;
    private AssetManager assetManager;
    private String storagePath;

    public AndroidFileIO(Context context) {
        this.context = context;
        assetManager = context.getAssets();
        storagePath = Environment.getStorageDirectory().getAbsolutePath()+ File.separator;
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return assetManager.open(fileName);
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(storagePath+fileName);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(storagePath+fileName);
    }

    public SharedPreferences getSharedPreferences()
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
