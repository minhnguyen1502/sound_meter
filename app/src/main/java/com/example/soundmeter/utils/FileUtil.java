package com.example.soundmeter.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static final String LOCAL = "SoundMeter";
    private static final String LOCAL_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator;
    private static final String REC_PATH = LOCAL_PATH + LOCAL + File.separator;

    static {
        File dirRootFile = new File(LOCAL_PATH);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File recFile = new File(REC_PATH);
        if (!recFile.exists()) {
            recFile.mkdirs();
        }
    }

    public static File createFile(String fileName, Context context) {
        File mFile;
        if (Build.VERSION.SDK_INT == 29) {
            // save file android 10
            mFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + LOCAL + fileName);
        } else {
            // all android
            mFile = new File(REC_PATH + fileName);
        }
        if (mFile.exists()) {
            mFile.delete();
        }
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return mFile;

    }
}
