package edu.ucsd.cse110.cse110lab4part5;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MockUserOrientationService implements IUserOrientationService{

    private MutableLiveData<Float> azimuth;

    public MockUserOrientationService(double azimuth) {
        this.azimuth.postValue((float)azimuth);
    }

    @Override
    public LiveData<Float> getOrientation() {
        return azimuth;
    }
}
