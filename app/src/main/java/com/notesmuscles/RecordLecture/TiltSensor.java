package com.notesmuscles.RecordLecture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class TiltSensor implements Runnable{

    private SensorManager sensorManager;
    private Sensor acce_sensor, magnetic_sensor;

    private float orientation_x;
    private boolean isCalibrated;
    private boolean tiltValid;

    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private double xValue,yValue, zValue;
    //private final double CALIBRATED_X, CALIBRATED_Y, CALIBRATED_Z;

    private CameraActivity myCameraActivity;

    SensorEventListener sensor_listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float[] values = sensorEvent.values;
                xValue = values[0];
                yValue = values[1];
                zValue =values[2];
                System.arraycopy(sensorEvent.values, 0, accelerometerReading, 0, accelerometerReading.length);
            }else if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(sensorEvent.values, 0, magnetometerReading, 0, magnetometerReading.length);
            }
            updateOrientationAngles();
            updateStatus();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) { }
    };

    public TiltSensor(CameraActivity myCameraActivity){
        this.myCameraActivity = myCameraActivity;
        sensorManager.registerListener(sensor_listener, acce_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensor_listener, magnetic_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        isCalibrated = true;
        sensorManager = (SensorManager)  myCameraActivity.getSystemService(Context.SENSOR_SERVICE);
        acce_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    //call these methods when in cameraActivity onResume/onPause
    protected void onResume(){
        sensorManager.registerListener(sensor_listener, acce_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensor_listener, magnetic_sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        sensorManager.unregisterListener(sensor_listener);
    }

    private void recordingStartedCallback(){
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
        orientation_x = orientationAngles[0] * 10;

    }

    private void updateOrientationAngles(){
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }

    private boolean updateStatus(){
        tiltValid = false;
        if(!isCalibrated){
            return tiltValid;
        }
        if(!((xValue > 7 && xValue < 11) && (yValue > -1 && yValue < 2) && (zValue > -1 && zValue < 1) && (orientationAngles[0]* 10 > (orientation_x - 2) && orientationAngles[0] * 10 < (orientation_x + 2)))){
            tiltValid = false;
        }else{
            tiltValid = true;
        }
        return tiltValid;
    }

    @Override
    public void run(){
        isCalibrated = true;
        sensorManager = (SensorManager)  myCameraActivity.getSystemService(Context.SENSOR_SERVICE);
        acce_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        orientation_x = orientationAngles[0] * 10;
    }
}
