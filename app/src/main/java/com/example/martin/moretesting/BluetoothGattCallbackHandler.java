package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;
import java.util.Locale;

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
        STATE_ENABLE_CCCD_BATTERY,
    }
    private DeviceInitiatorStates initiatorState = DeviceInitiatorStates.STATE_IDLE;

    private MainActivity mainActivity;
    private BluetoothModel mBleModel;
    private WeatherProfile mCurrentWeather;
    private BluetoothGattCallback mBleGattCallback;

    public BluetoothGattCallbackHandler(MainActivity mainActivity, BluetoothModel bleModel, WeatherProfile currentWeather) {
        this.mainActivity = mainActivity;
        this.mBleModel = bleModel;
        this.mCurrentWeather = currentWeather;

        setGattCallback();
    }

    public BluetoothGattCallback getmBleGattCallback() {
        return mBleGattCallback;
    }

    private void setGattCallback(){
        mBleGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    Log.i(TAG, "onConnectionStateChange: STATE_CONNECTED to " + gatt.getDevice().getAddress());
                    gatt.discoverServices();
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    mainActivity.removeDeviceFromList(gatt.getDevice());
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
                    if (BluetoothModel.WEATHER_THINGY_SERVICE_UUID.equals(service.getUuid())) {
                        mCurrentWeather.setWeatherService(service);
                        Log.i(TAG, "onServicesDiscovered: Found FOOD service.");
                    } else if (BluetoothModel.WEATHER_THINGY_BATTERY_SERVICE_UUID.equals(service.getUuid())) {
                        mCurrentWeather.setBatteryService(service);
                        Log.i(TAG, "onServicesDiscovered: Found BATTERY service.");
                    } else {
                        Log.i(TAG, "onServicesDiscovered: Unknown service found: " + service.getUuid());
                    }
                }

                if (mCurrentWeather.getWeatherService() != null) {
                    for (BluetoothGattCharacteristic characteristic : mCurrentWeather.getWeatherService().getCharacteristics()) {
                        if (BluetoothModel.WEATHER_THINGY_CHAR_TEMPERATURE_UUID.equals(characteristic.getUuid())) {
                            mCurrentWeather.setCharTemperature(characteristic);
                            Log.i(TAG, "onServicesDiscovered: Temperature char set");
                        } else if (BluetoothModel.WEATHER_THINGY_CHAR_HUMIDITY_UUID.equals(characteristic.getUuid())) {
                            mCurrentWeather.setCharHumidity(characteristic);
                            Log.i(TAG, "onServicesDiscovered: Humidity char set");
                        } else if (BluetoothModel.WEATHER_THINGY_CHAR_PRESSURE_UUID.equals(characteristic.getUuid())) {
                            mCurrentWeather.setCharPressure(characteristic);
                            Log.i(TAG, "onServicesDiscovered: Pressure char set");
                        } else {
                            Log.i(TAG, "onServicesDiscovered: Found wrong char: " + characteristic.getUuid());
                        }
                    }
                }

                if (mCurrentWeather.getBatteryService() != null) {
                    for (BluetoothGattCharacteristic characteristic : mCurrentWeather.getBatteryService().getCharacteristics()) {
                        if (BluetoothModel.WEATHER_THINGY_CHAR_BATTERY_UUID.equals(characteristic.getUuid())) {
                            mCurrentWeather.setCharBattery(characteristic);
                            Log.i(TAG, "onServicesDiscovered: Battery char set");
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

                if (BluetoothModel.WEATHER_THINGY_CHAR_TEMPERATURE_UUID.equals(characteristic.getUuid())) {
                    final double temperature = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0) / 100.0;
                    final double temperatureMax  = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 4) / 100.0;
                    final double temperatureMin  = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 8) / 100.0;
                    Log.i(TAG, "occ: dev:" + gatt.getDevice() + String.format(Locale.ENGLISH, ", Temp: %.1f,\t\t max: %.1f,\t min: %.1f", temperature, temperatureMax, temperatureMin));
                    mCurrentWeather.setTemperature(temperature);
                    mCurrentWeather.setTemperatureMax(temperatureMax);
                    mCurrentWeather.setTemperatureMin(temperatureMin);
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.updateTemperatureValue(temperature);
                        }
                    });
                } else if (BluetoothModel.WEATHER_THINGY_CHAR_HUMIDITY_UUID.equals(characteristic.getUuid())) {
                    final double humidity = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0) / 1024.0;
                    final double humidityMax  = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 4) / 1024.0;
                    final double humidityMin  = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 8) / 1024.0;
                    Log.i(TAG, "occ: dev:" + gatt.getDevice() + String.format(Locale.ENGLISH, ", Hum: %.1f,\t\t max: %.1f,\t min: %.1f", humidity, humidityMax, humidityMin));
                    mCurrentWeather.setHumidity(humidity);
                    mCurrentWeather.setHumidityMax(humidityMax);
                    mCurrentWeather.setHumidityMin(humidityMin);
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.updateHumidityValue(humidity);
                        }
                    });
                }else if (BluetoothModel.WEATHER_THINGY_CHAR_PRESSURE_UUID.equals(characteristic.getUuid())) {
                    final double pressure = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0) / 100.0;
                    final double pressureMax  = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 4) / 100.0;
                    final double pressureMin  = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 8) / 100.0;
                    Log.i(TAG, "occ: dev:" + gatt.getDevice() + String.format(Locale.ENGLISH, ", Pres: %.1f,\t max: %.1f,\t min: %.1f", pressure, pressureMax, pressureMin));
                    mCurrentWeather.setPressure(pressure);
                    mCurrentWeather.setPressureMax(pressureMax);
                    mCurrentWeather.setPressureMin(pressureMin);
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.updatePressureValue(pressure);
                        }
                    });
                } else if (BluetoothModel.WEATHER_THINGY_CHAR_BATTERY_UUID.equals(characteristic.getUuid())) {
                    final int batteryLevel = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                    mCurrentWeather.setBatteryLevel(batteryLevel);
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.updateBatteryLevel(batteryLevel);
                        }
                    });
                } else {
                    Log.i(TAG, "onCharacteristicChanged: " + characteristic.getUuid() + ". New value = " + characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0));
                }

            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);

                byte[] cccdTemperature = mCurrentWeather.getCharTemperature().getDescriptor(BluetoothModel.BLE_UUID_CCCD).getValue();
                byte[] cccdHumidity = mCurrentWeather.getCharHumidity().getDescriptor(BluetoothModel.BLE_UUID_CCCD).getValue();
                byte[] cccdPressure = mCurrentWeather.getCharPressure().getDescriptor(BluetoothModel.BLE_UUID_CCCD).getValue();
                byte[] cccdBattery = null;
                if (mCurrentWeather.getCharBattery() != null) {
                    cccdBattery = mCurrentWeather.getCharBattery().getDescriptor(BluetoothModel.BLE_UUID_CCCD).getValue();
                }

                if (cccdTemperature != BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) {
                    initiatorState = DeviceInitiatorStates.STATE_ENABLE_CCCD_TEMPERATURE;
                    deviceInitiateStateMachine();
                    Log.i(TAG, "onDescriptorWrite: Enabling Temp CCCD ");
                } else if (cccdHumidity != BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) {
                    initiatorState = DeviceInitiatorStates.STATE_ENABLE_CCCD_HUMIDITY;
                    deviceInitiateStateMachine();
                    Log.i(TAG, "onDescriptorWrite: Enabling Humnidity CCCD ");
                } else if (cccdPressure != BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) {
                    initiatorState = DeviceInitiatorStates.STATE_ENABLE_CCCD_PRESSURE;
                    deviceInitiateStateMachine();
                    Log.i(TAG, "onDescriptorWrite: Enabling Pressure CCCD ");
                }else if (cccdBattery != BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) {
                    if (mCurrentWeather.getCharBattery() != null) {
                        initiatorState = DeviceInitiatorStates.STATE_ENABLE_CCCD_BATTERY;
                        deviceInitiateStateMachine();
                        Log.i(TAG, "onDescriptorWrite: Enabling Battery CCCD ");
                    }
                }

            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
                Log.i(TAG, "onReadRemoteRssi: " + rssi);
                mBleModel.rssi = rssi;

            }
        };
    }

    private void deviceInitiateStateMachine() {

        switch (initiatorState) {
            case STATE_ENABLE_CCCD_TEMPERATURE:
                if (mCurrentWeather.getCharTemperature() != null) {
                    if (mBleModel.bleGatt.setCharacteristicNotification(mCurrentWeather.getCharTemperature(), true)) {
                        BluetoothGattDescriptor cccdDescriptor = mCurrentWeather.getCharTemperature().getDescriptor(BluetoothModel.BLE_UUID_CCCD);
                        cccdDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mBleModel.bleGatt.writeDescriptor(cccdDescriptor);
                    }
                }
                break;
            case STATE_ENABLE_CCCD_HUMIDITY:
                if (mCurrentWeather.getCharHumidity() != null) {
                    if (mBleModel.bleGatt.setCharacteristicNotification(mCurrentWeather.getCharHumidity(), true)) {
                        BluetoothGattDescriptor cccdDescriptor = mCurrentWeather.getCharHumidity().getDescriptor(BluetoothModel.BLE_UUID_CCCD);
                        cccdDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mBleModel.bleGatt.writeDescriptor(cccdDescriptor);
                    }
                }
                break;
            case STATE_ENABLE_CCCD_PRESSURE:
                if (mCurrentWeather.getCharPressure() != null) {
                    if (mBleModel.bleGatt.setCharacteristicNotification(mCurrentWeather.getCharPressure(), true)) {
                        BluetoothGattDescriptor cccdDescriptor = mCurrentWeather.getCharPressure().getDescriptor(BluetoothModel.BLE_UUID_CCCD);
                        cccdDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mBleModel.bleGatt.writeDescriptor(cccdDescriptor);
                    }
                }
                break;
            case STATE_ENABLE_CCCD_BATTERY:
                if (mCurrentWeather.getCharBattery() != null) {
                    if (mBleModel.bleGatt.setCharacteristicNotification(mCurrentWeather.getCharBattery(), true)) {
                        BluetoothGattDescriptor cccdDescriptor = mCurrentWeather.getCharBattery().getDescriptor(BluetoothModel.BLE_UUID_CCCD);
                        cccdDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        mBleModel.bleGatt.writeDescriptor(cccdDescriptor);
                    }
                }
                break;

            default:
                break;
        }
    }
}
