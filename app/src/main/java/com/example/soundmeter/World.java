package com.example.soundmeter;

public class World {
    public static float dbCount = 50.0f ;
    public static float MIN = 140 ;
    public static float MAX = 0 ;
    public static float lastDB = dbCount;
    private static float mindb = 0.5f;
    private static float value = 0;

    public static void setDbCount(float mValue) {
        if (mValue>lastDB){
            value = mValue - lastDB > mindb ? mValue - lastDB : mindb;
        }else {
            value = mValue - lastDB < -mindb ? mValue - lastDB : mindb;
        }
        dbCount  = lastDB + value * 0.2f;
        lastDB = dbCount;
        if (dbCount<MIN)MIN =dbCount;
        if (dbCount>MAX)MAX =dbCount;
    }
}
