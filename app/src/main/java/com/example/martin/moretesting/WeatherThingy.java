package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Martin on 21.08.2016.
 */

public class WeatherThingy {
    MainActivity mainActivity;
    BluetoothDevice bleDevice;

    private static int deviceNumber = 0;

    public WeatherThingy(MainActivity mainActivity, BluetoothDevice bleDevice) {
        this.mainActivity = mainActivity;
        this.bleDevice = bleDevice;
    }

    public String getAddress() {
        return bleDevice.getAddress();
    }

    public String getName() {
        return bleDevice.getName();
    }

    public BluetoothDevice getBleDevice() {
        return bleDevice;
    }
}
