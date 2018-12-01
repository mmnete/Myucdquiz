package com.companywesbite.myucdquiz.utils;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.content.Context;
import java.lang.UnsupportedOperationException;


/***
 *
 * Team: Flashcards Pro
 * Date: 2018-11-30
 * Name: ShakeListener
 * Functionality: This is a class is responsible of detecting if a user has shaked his/her phone
 *                Using some threshold parameters we are able to detect a shake. It takes advanatge of android
 *                sensors service
 *
 *
 *
 *
 */




public class ShakeListener implements SensorListener
{

    private static final int SHAKE_FORCE_LIMIT = 350;
    private static final int SHAKE_TIME_LIMIT = 100;
    private static final int SHAKE_TIMEOUT_NUMBER = 500;
    private static final int SHAKE_DURATION_NUMBER = 1000;
    private static final int SHAKE_COUNT_NUMBER = 3;

    private SensorManager mSensorManager;
    private float mPrevX=-1.0f, mPrevY=-1.0f, mPrevZ=-1.0f;
    private long mPrevTime;
    private OnShakeListener onShakeListener;
    private Context context;
    private int shakeCounter = 0;
    private long lastShake;
    private long lastForce;

    public interface OnShakeListener
    {
        public void onShake();
    }

    public ShakeListener(Context context)
    {
        this.context = context;
        resume();
    }

    public void setOnShakeListener(OnShakeListener listener)
    {
        onShakeListener = listener;
    }

    public void resume() {
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) {
            throw new UnsupportedOperationException("Sensors not supported");
        }
        boolean checkSupport = mSensorManager.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
        if (!checkSupport) {
            mSensorManager.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
            throw new UnsupportedOperationException("Accelerometer not supported");
        }
    }

    public void pause() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
            mSensorManager = null;
        }
    }

    public void onAccuracyChanged(int sensor, int accuracy) { }

    public void onSensorChanged(int sensor, float[] values)
    {
        if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
        long currTime = System.currentTimeMillis();

        if ((currTime  - lastForce) > SHAKE_TIMEOUT_NUMBER) {
            shakeCounter = 0;
        }

        if ((currTime  - mPrevTime) > SHAKE_TIME_LIMIT) {
            long timeDiff = currTime  - mPrevTime;
            float currSpeed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mPrevX - mPrevY - mPrevZ) / timeDiff * 10000;
            if (currSpeed > SHAKE_FORCE_LIMIT) {
                if ((++shakeCounter >= SHAKE_COUNT_NUMBER) && (currTime  - lastShake > SHAKE_DURATION_NUMBER)) {
                    lastShake = currTime ;
                    shakeCounter = 0;
                    if (onShakeListener != null) {
                        onShakeListener.onShake();
                    }
                }
                lastForce = currTime;
            }
            mPrevTime = currTime;
            mPrevX = values[SensorManager.DATA_X];
            mPrevY = values[SensorManager.DATA_Y];
            mPrevZ = values[SensorManager.DATA_Z];
        }
    }

}
