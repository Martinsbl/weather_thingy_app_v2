package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;

/**
 * Created by Martin on 21.08.2016.
 */

public class BluetoothGattCallbackHandler {

    private final String TAG = "GattCallbackHandler";

    private enum DeviceInitiatorStates {
        STATE_IDLE,
        STATE_ENABLE_CCCD_TEMPERATURE,
        STATE_ENABLE_CCCD_HUMIDITY,
        STATE_ENABLE_CCCD_PRESSURE,
    }
    private DeviceInitiatorStates initiatorState = DeviceInitiatorStates.STATE_IDLE;


    private MainActivity mainActivity;
    private BluetoothDevice bleDevice;
    private BluetoothGattCallback gattCallback;
    private WeatherProfile mCurrentWeather;
    private BluetoothGatt mBleGatt;

    public BluetoothGattCallbackHandler(MainActivity mainActivity, BluetoothGatt bleGatt, BluetoothDevice bleDevice, WeatherProfile currentWeather) {
        this.mainActivity = mainActivity;
        this.bleDevice = bleDevice;
        this.mBleGatt = bleGatt;
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

                if (mCurrentWeather.getWeatherService() != null) {
                    for (BluetoothGattCharacteristic characteristic : mCurrentWeather.getWeatherService().getCharacteristics()) {
                        if (WeatherThingy.WEATHER_THINGY_CHAR_TEMPERATURE_UUID.equals(characteristic.getUuid())) {
                            mCurrentWeather.setCharTemperature(characteristic);
                            Log.i(TAG, "onServicesDiscovered: Temperature char set");
                        } else if (WeatherThingy.WEATHER_THINGY_CHAR_HUMIDITY_UUID.equals(characteristic.getUuid())) {
                            mCurrentWeather.setCharHumidity(characteristic);
                            Log.i(TAG, "onServicesDiscovered: Humidity char set");
                        } else if (WeatherThingy.WEATHER_THINGY_CHAR_PRESSURE_SERVICE_UUID.equals(characteristic.getUuid())) {
                            mCurrentWeather.setCharPressure(characteristic);
                            Log.i(TAG, "onServicesDiscovered: Pressure char set");
                        } else {
                            Log.i(TAG, "onServicesDiscovered: Found wrong char: " + characteristic.getUuid());
                        }
                    }
                }
                initiatorState = DeviceInitiatorStates.STATE_ENABLE_CCCD_TEMPERATURE;
                deviceInitiateStateMachine();
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.i(TAG, "onCharacteristicChanged: ") ;

            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                Log.i(TAG, "onDescriptorWrite: " + descriptor.getUuid());
            }
        };
    }

    private void deviceInitiateStateMachine() {

        switch (initiatorState) {
            case STATE_ENABLE_CCCD_TEMPERATURE:
                if (mCurrentWeather.getCharTemperature() != null) {
                    if (mBleGatt.setCharacteristicNotification(mCurrentWeather.getCharTemperature(), true)) {
                        BluetoothGattDescriptor cccdDescriptor = mCurrentWeather.getCharTemperature().getDescriptor(WeatherThingy.BLE_UUID_CCCD);
                        cccdDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mBleGatt.writeDescriptor(cccdDescriptor);
                    }
                }
                break;
            default:
                break;
        }
    }
}
