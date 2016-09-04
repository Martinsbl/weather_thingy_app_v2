package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;

/**
 * Created by Martin on 21.08.2016.
 */

public class WeatherThingy {



    private MainActivity mainActivity;

    private WeatherProfile currentWeather;

    private BluetoothModel mBleModel;

    private static int deviceNumber = 0;

    private Handler mHandlerPeriodicRssiRead;

    public WeatherThingy(MainActivity mainActivity, BluetoothDevice bleDevice) {
        deviceNumber++;
        this.mainActivity = mainActivity;

        currentWeather = new WeatherProfile(mainActivity);


        mBleModel = new BluetoothModel();
        mBleModel.bleDevice = bleDevice;
        BluetoothGattCallbackHandler bleCallbackHandler = new BluetoothGattCallbackHandler(mainActivity, mBleModel, currentWeather);
        mBleModel.bleGattCallback = bleCallbackHandler.getmBleGattCallback();
        mBleModel.bleGatt = bleDevice.connectGatt(mainActivity, false, mBleModel.bleGattCallback);

        mHandlerPeriodicRssiRead = new Handler();
        runRepeatedRssiReads(true);
    }

    public void runRepeatedRssiReads(boolean doRepeatedRssiReads) {
        if (doRepeatedRssiReads) {
            runnableReadRssi.run();
        } else {
            mHandlerPeriodicRssiRead.removeCallbacks(runnableReadRssi);
        }
    }

    private Runnable runnableReadRssi = new Runnable() {
        @Override
        public void run() {
            try{
                mBleModel.bleGatt.readRemoteRssi();
            }finally {
                mHandlerPeriodicRssiRead.postDelayed(runnableReadRssi, 5000);
            }
        }
    };

    public BluetoothModel getmBleModel() {
        return mBleModel;
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
        runRepeatedRssiReads(false);
        mBleModel.bleGatt.disconnect();
        mBleModel.bleGatt.close();


        mBleModel.bleDevice = null;
        deviceNumber--;
        if (deviceNumber < 0) {
            deviceNumber = 0;
        }
    }
}
