package com.example.martin.moretesting;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.List;

/**
 * Created by Martin on 21.08.2016.
 */

public class BluetoothScanner {

    private final String TAG = "BluetoothScanner";

    private MainActivity mainActivity;
    private BluetoothLeScanner mBleScanner;
    private BluetoothAdapter mBleAdapter;
    private boolean mIsScanning;
    private Handler mHandler;

    public BluetoothScanner(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        final BluetoothManager bleManager = (BluetoothManager) mainActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBleAdapter = bleManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBleAdapter == null || !mBleAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mainActivity.startActivityForResult(enableBtIntent, 1);
        }

        mHandler = new Handler();
        mBleScanner = mBleAdapter.getBluetoothLeScanner();
    }

    public void terminateScanning() {
        if (isScanning()) {
            stopScanning();
        }
    }

    public void startScanning() {
        scanLeDevice(true);
    }

    public void stopScanning() {
        scanLeDevice(false);
    }

    public boolean isScanning() {
        return mIsScanning;
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsScanning = false;
                    mBleScanner.stopScan(mScanCallback);
                    mainActivity.printHashMap();
                }
            }, 3000);

            mIsScanning = true;
            mBleScanner.startScan(mScanCallback);
        } else {
            mIsScanning = false;
            mBleScanner.stopScan(mScanCallback);
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.i(TAG, "onScanResult. Device found: " + result.getDevice().getName());
            try {
                List<ParcelUuid> uuidList = result.getScanRecord().getServiceUuids();
                for (int i = 0; i < uuidList.size(); i++) {
                    if (uuidList.get(i).getUuid().equals(WeatherThingy.WEATHER_THINGY_ADVERTISING_UUID)) {
                        Log.i("BLE", "Found WeatherThingy");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.addDeviceToList(result.getDevice());
                            }
                        });
                    }
                }
            } catch (Exception exception) {
                Log.i("BLE", "Found unknown device.");
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };
}
