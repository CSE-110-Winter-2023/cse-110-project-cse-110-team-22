package edu.ucsd.cse110.cse110lab4part5;

import android.hardware.SensorEvent;

import androidx.lifecycle.LiveData;

public interface IUserOrientationService {
    public LiveData<Float> getOrientation();
}
