package com.example.martin.moretesting;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

/**
 * Created by Martin on 21.08.2016.
 */

public class WeatherProfile {
    private MainActivity mainActivity;

    private double temperature, humidity, pressure;
    private int batteryLevel;

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    private BluetoothGattService weatherService;
    private BluetoothGattCharacteristic charTemperature;
    private BluetoothGattCharacteristic charHumidity;
    private BluetoothGattCharacteristic charPressure;

    private BluetoothGattService batteryService;
    private BluetoothGattCharacteristic charBattery;

    public BluetoothGattService getBatteryService() {
        return batteryService;
    }

    public void setBatteryService(BluetoothGattService batteryService) {
        this.batteryService = batteryService;
    }

    public BluetoothGattCharacteristic getCharBattery() {
        return charBattery;
    }

    public void setCharBattery(BluetoothGattCharacteristic charBattery) {
        this.charBattery = charBattery;
    }

    public WeatherProfile(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
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

