package com.example.martin.moretesting;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

/**
 * Created by Martin on 21.08.2016.
 */

public class WeatherProfile {
    private MainActivity mainActivity;

    private BluetoothGattService weatherService;
    private BluetoothGattCharacteristic charTemperature;
    private BluetoothGattCharacteristic charHumidity;
    private BluetoothGattCharacteristic charPressure;

    public WeatherProfile(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public BluetoothGattService getWeatherService() {
        return weatherService;
    }

    public void setWeatherService(BluetoothGattService weatherService) {
        this.weatherService = weatherService;
    }

    public BluetoothGattCharacteristic getCharTemperature() {
        return charTemperature;
    }

    public void setCharTemperature(BluetoothGattCharacteristic charTemperature) {
        this.charTemperature = charTemperature;
    }

    public BluetoothGattCharacteristic getCharHumidity() {
        return charHumidity;
    }

    public void setCharHumidity(BluetoothGattCharacteristic charHumidity) {
        this.charHumidity = charHumidity;
    }

    public BluetoothGattCharacteristic getCharPressure() {
        return charPressure;
    }

    public void setCharPressure(BluetoothGattCharacteristic charPressure) {
        this.charPressure = charPressure;
    }
}

