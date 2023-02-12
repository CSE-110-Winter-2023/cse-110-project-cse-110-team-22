package edu.ucsd.cse110.cse110lab4part5;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserOrientationService implements SensorEventListener {
    private static UserOrientationService instance;
    private final SensorManager sensorManager;
    private float[] accelerometerReading;
    private float[] magnetometerReading;
    private MutableLiveData<Float> azimuth;

    /**
     * Constructor for Orientation Service
     *
     * @param activity Context needed to initiate SensorManager
     */
    protected UserOrientationService(Activity activity) {
        this.azimuth = new MutableLiveData<>();
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
// Register sensor listeners
        this.registerSensorListeners();
    }

    private void registerSensorListeners() {
// Register our listener to the accelerometer and magnetometer.
// We need both pieces of data to compute the orientation!
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static UserOrientationService singleton(Activity activity) {
        if (instance == null) {
            instance = new UserOrientationService(activity);
        }
        return instance;
    }

    /**
     * This method is called when the sensor detects a change in value.
     *
     * @param event the event containing the values we need.
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // If we only have this sensor, we can't compute the orientation with it alone.
            // But we should still save it for later.
            accelerometerReading = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // If we only have this sensor, we can't compute the orientation with it alone.
            // But we should still save it for later.
            magnetometerReading = event.values;
        }
        if (accelerometerReading != null && magnetometerReading != null) {
            // We have both sensors, so we can compute the orientation!
            onBothSensorDataAvailable();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Called when we have readings for both sensors.
     */
    private void onBothSensorDataAvailable() {
        // Discount contract checking. Think Design by Contract!
        if (accelerometerReading == null || magnetometerReading == null) {
            throw new IllegalStateException("Both sensors must be available to compute orientation.");
        }

        float[] r = new float[9];
        float[] i = new float[9];
        // Now we do some linear algebra magic using the two sensor readings.
        boolean success = SensorManager.getRotationMatrix(r, i, accelerometerReading, magnetometerReading);
        // Did it work?
        if(success == true) {
            // Ok we're good to go!
            float[] orientation = new float[3];
            SensorManager.getOrientation(r, orientation);
            // Orientation now contains in order: azimuth, pitch and roll.
            // These are coordinates in a 3D space commonly used by aircraft...
            // but we only care about azimuth.
            // Azimuth is the angle between the magnetic north pole and the y-axis,
            // around the z-axis (- to π).
            // An azimuth of 0 means that the device is pointed north, and means it's pointed south.
            // π/2 means it's pointed east, and 3π/2 means it's pointed west.
            this.azimuth.postValue(orientation[0]);
        }
    }

    public void unregisterSensorListeners() {
        sensorManager.unregisterListener(this);
    }
    public LiveData<Float> getOrientation() {
        return this.azimuth;
    }
    public void setMockOrientationSource(MutableLiveData<Float> mockDataSource) {
        unregisterSensorListeners();
        this.azimuth = mockDataSource;
    }
}
