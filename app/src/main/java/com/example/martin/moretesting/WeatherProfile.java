package com.example.martin.moretesting;

import android.bluetooth.BluetoothGattService;

/**
 * Created by Martin on 21.08.2016.
 */

public class WeatherProfile {
    private MainActivity mainActivity;

    private BluetoothGattService weatherService;

    public WeatherProfile(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public BluetoothGattService getWeatherService() {
        return weatherService;
    }

    public void setWeatherService(BluetoothGattService weatherService) {
        this.weatherService = weatherService;
    }
}
