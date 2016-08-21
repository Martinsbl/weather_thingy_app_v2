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

        mBleModel = new BluetoothModel();
        mBleModel.bleDevice = bleDevice;

        currentWeather = new WeatherProfile(mainActivity);

        BluetoothGattCallbackHandler bleCallbackHandler = new BluetoothGattCallbackHandler(mainActivity, mBleModel, currentWeather);
        bleCallbackHandler.setGattCallback();


        mBleModel.bleGatt = bleDevice.connectGatt(mainActivity, false, mBleModel.bleGattCallback);

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

        mBleModel.bleCallbackHandler = null;
        mBleModel.bleDevice = null;
        deviceNumber--;
        if (deviceNumber < 0) {
            deviceNumber = 0;
        }
    }
}
