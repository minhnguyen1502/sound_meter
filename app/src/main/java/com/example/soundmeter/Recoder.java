package com.example.soundmeter;

import android.media.MediaRecorder;

import java.io.File;

public class Recoder {
     private File mFile;
     private MediaRecorder mediaRecorder;
     private boolean isRecording = false;

     public float getMax(){
          if (mediaRecorder != null){
               try {
                    return mediaRecorder.getMaxAmplitude();
               }catch (IllegalStateException e){
                    e.getStackTrace();
                    return 0;
               }
          }else {
               return 5;
          }
     }

     public File getmFile() {
          return mFile;
     }
}
