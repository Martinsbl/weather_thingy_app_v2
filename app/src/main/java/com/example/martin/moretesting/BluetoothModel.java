package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

import java.util.UUID;

/**
 * Created by Martin on 21.08.2016.
 */

public class BluetoothModel {

    final public static UUID WEATHER_THINGY_ADVERTISING_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    final public static UUID WEATHER_THINGY_SERVICE_UUID = UUID.fromString("0000f00d-1212-efde-1523-785fef13d123");
    final public static UUID WEATHER_THINGY_CHAR_TEMPERATURE_UUID = UUID.fromString("0000eeae-0000-1000-8000-00805f9b34fb");
    final public static UUID WEATHER_THINGY_CHAR_HUMIDITY_UUID = UUID.fromString("000001d1-0000-1000-8000-00805f9b34fb");
    final public static UUID WEATHER_THINGY_CHAR_PRESSURE_UUID = UUID.fromString("0000e55e-0000-1000-8000-00805f9b34fb");
    public static final UUID BLE_UUID_CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    BluetoothDevice bleDevice;
    BluetoothGatt bleGatt;
    BluetoothGattCallback bleGattCallback;

    int rssi;



}
