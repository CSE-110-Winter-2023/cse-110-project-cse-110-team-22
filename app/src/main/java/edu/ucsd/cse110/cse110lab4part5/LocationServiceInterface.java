package edu.ucsd.cse110.cse110lab4part5;

import android.app.Activity;
import android.location.Location;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public interface LocationServiceInterface {
    void onLocationChanged(@NonNull Location location);
    LiveData<Pair<Double,Double>> getLocation();
    void setMockOrientationSource(MutableLiveData<Pair<Double,Double>> mockDataSource);
}
