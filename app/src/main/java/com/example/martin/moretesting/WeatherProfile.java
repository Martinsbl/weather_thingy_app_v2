package com.example.martin.moretesting;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

/**
 * Created by Martin on 21.08.2016.
 */

public class WeatherProfile {
    private MainActivity mainActivity;

    private double temperature, temperatureMin, temperatureMax;
    private double humidity, humidityMin, humidityMax;
    private double pressure, pressureMin, pressureMax;


    private int batteryLevel;
    private BluetoothGattService weatherService;
    private BluetoothGattCharacteristic charTemperature;
    private BluetoothGattCharacteristic charHumidity;
    private BluetoothGattCharacteristic charPressure;

    private BluetoothGattService batteryService;
    private BluetoothGattCharacteristic charBattery;

    public double getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(double pressureMax) {
        this.pressureMax = pressureMax;
    }

    public double getPressureMin() {
        return pressureMin;
    }

    public void setPressureMin(double pressureMin) {
        this.pressureMin = pressureMin;
    }

    public double getHumidityMax() {
        return humidityMax;
    }

    public void setHumidityMax(double humidityMax) {
        this.humidityMax = humidityMax;
    }

    public double getHumidityMin() {
        return humidityMin;
    }

    public void setHumidityMin(double humidityMin) {
        this.humidityMin = humidityMin;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }



    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }


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

