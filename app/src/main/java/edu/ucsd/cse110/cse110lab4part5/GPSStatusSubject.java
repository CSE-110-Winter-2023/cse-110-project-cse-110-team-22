package edu.ucsd.cse110.cse110lab4part5;

public interface GPSStatusSubject {
    void registerObserver(GPSStatusObserver observer);
    void notifyObservers();
}
