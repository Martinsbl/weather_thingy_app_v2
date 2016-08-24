package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

import java.util.UUID;

/**
 * Created by Martin on 21.08.2016.
 */

public class WeatherThingy {



    private MainActivity mainActivity;

    private WeatherProfile currentWeather;

    private BluetoothModel mBleModel;

    private static int deviceNumber = 0;

    public WeatherThingy(MainActivity mainActivity, BluetoothDevice bleDevice) {
        deviceNumber++;
        this.mainActivity = mainActivity;

        currentWeather = new WeatherProfile(mainActivity);


        mBleModel = new BluetoothModel();
        mBleModel.bleDevice = bleDevice;
        BluetoothGattCallbackHandler bleCallbackHandler = new BluetoothGattCallbackHandler(mainActivity, mBleModel, currentWeather);
        mBleModel.bleGattCallback = bleCallbackHandler.getmBleGattCallback();
        mBleModel.bleGatt = bleDevice.connectGatt(mainActivity, false, mBleModel.bleGattCallback);

    }

    public WeatherProfile getCurrentWeather() {
        return currentWeather;
    }

    public String getAddress() {
        return mBleModel.bleDevice.getAddress();
    }

    public String getName() {
        return mBleModel.bleDevice.getName();
    }

    public BluetoothDevice getBleDevice() {
        return mBleModel.bleDevice;
    }

    public void destroyWeatherThingy() {
        mBleModel.bleGatt.disconnect();
        mBleModel.bleGatt.close();

        mBleModel.bleDevice = null;
        deviceNumber--;
        if (deviceNumber < 0) {
            deviceNumber = 0;
        }
    }
}
