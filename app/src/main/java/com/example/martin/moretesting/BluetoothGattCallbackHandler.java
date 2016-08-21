package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;

/**
 * Created by Martin on 21.08.2016.
 */

public class BluetoothGattCallbackHandler {

    private final String TAG = "GattCallbackHandler";

    private MainActivity mainActivity;
    private BluetoothDevice bleDevice;
    private BluetoothGattCallback gattCallback;
    private WeatherProfile mCurrentWeather;

    public BluetoothGattCallbackHandler(MainActivity mainActivity, BluetoothGatt bleGatt, BluetoothDevice bleDevice, WeatherProfile currentWeather) {
        this.mainActivity = mainActivity;
        this.bleDevice = bleDevice;
        this.mCurrentWeather = currentWeather;
    }

    public BluetoothGattCallback getGattCallback() {
        return gattCallback;
    }

    public void setGattCallback(){
        gattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    Log.i(TAG, "onConnectionStateChange: STATE_CONNECTED to " + gatt.getDevice().getAddress());
                    gatt.discoverServices();
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    Log.i(TAG, "onConnectionStateChange: STATE_DISCONNECTED from " + gatt.getDevice().getAddress());
                } else {
                    Log.i(TAG, "onConnectionStateChange: newState: " + newState + " @ " + gatt.getDevice().getAddress());
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                Log.i(TAG, "onServicesDiscovered: ");

                for (BluetoothGattService service : gatt.getServices() ) {
                    if (WeatherThingy.WEATHER_THINGY_SERVICE_UUID.equals(service.getUuid())) {
                        mCurrentWeather.setWeatherService(service);
                        Log.i(TAG, "onServicesDiscovered: Found FOOD service.");
                    } else {
                        Log.i(TAG, "onServicesDiscovered: Unknown service found: " + service.getUuid());
                    }
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
            }
        };
    }
}
