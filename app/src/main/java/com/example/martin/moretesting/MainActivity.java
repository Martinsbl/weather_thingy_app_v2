package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.R.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import android.Manifest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";

    private Button btnScan,btnReset;
    private TextView txtBattery, txtTemperature, txtHumidity, txtPressure;

    private ListView listViewWeatherThingy;

    private BluetoothScanner bleScanner;

    private HashMap<String, WeatherThingy> bleDeviceHashMap;
    private ArrayList<WeatherThingy> weatherThingiesList;
    private WeatherThingyListAdapter listAdapter;

    private Handler scannerTimer;

    final private int BLE_PERMISSION = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        promtForPermission();

    }


    private void continueWithLogic() {
        initGuiElements();
        initList();
        initBle();
    }

    private void initGuiElements() {
        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtPressure = (TextView) findViewById(R.id.txtPressure);
    }


    private void initList() {
        bleDeviceHashMap = new HashMap<>();
        weatherThingiesList = new ArrayList<>();

        listViewWeatherThingy = (ListView) findViewById(R.id.listWeatherThingies);
        listAdapter = new WeatherThingyListAdapter(this, layout.simple_list_item_1, weatherThingiesList);
        listViewWeatherThingy.setAdapter(listAdapter);
    }


    private void initBle() {
        bleScanner = new BluetoothScanner(this);
        bleScanner.startScanning();

        scannerTimer = new Handler();
        runRepeatedScanner(true);
    }

    private void promtForPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        BLE_PERMISSION);
            }
        } else {
            continueWithLogic();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
     switch (requestCode) {
         case BLE_PERMISSION: {
             // If request is cancelled, the result arrays are empty.
             if (grantResults.length > 0
                     && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                 Log.i(TAG, "onRequestPermissionsResult: permission was granted, yay! Do the");
                 // permission was granted, yay! Do the
                 // contacts-related task you need to do.
                 continueWithLogic();

             } else {

                 Log.i(TAG, "onRequestPermissionsResult: // permission denied, boo! Disable the functionality that depends on this permission.");
                 // permission denied, boo! Disable the
                 // functionality that depends on this permission.
             }
             return;
         }
         default:
             Log.i(TAG, "onRequestPermissionsResult: Balle.");

         // other 'case' lines to check for other
         // permissions this app might request
     }
    }

    public void runRepeatedScanner(boolean doRepeatedScanning) {
        if (doRepeatedScanning) {
            repeatedScanner.run();
        } else {
            scannerTimer.removeCallbacks(repeatedScanner);
        }
    }

    private Runnable repeatedScanner = new Runnable() {
        @Override
        public void run() {
            try{
                bleScanner.startScanning();
            }finally {
                scannerTimer.postDelayed(repeatedScanner, 15000);
            }
        }
    };

    public void addDeviceToList(BluetoothDevice bleDevice) {
        String deviceAddress = bleDevice.getAddress();
        if (!bleDeviceHashMap.containsKey(deviceAddress)) {
            WeatherThingy weatherThingy = new WeatherThingy(this, bleDevice);
            bleDeviceHashMap.put(deviceAddress, weatherThingy);
            weatherThingiesList.add(weatherThingy);
            //listAdapter.add(weatherThingy);
            Log.i(TAG, "createWeatherThingy: Added " + deviceAddress + " to list");
        }
    }

    public void removeDeviceFromList(BluetoothDevice bleDevice) {
        String deviceAddress = bleDevice.getAddress();
        WeatherThingy wt = bleDeviceHashMap.get(deviceAddress);
        Log.i(TAG, "removeDeviceFromHash: before list size : " + weatherThingiesList.size() + ", hash size: " + bleDeviceHashMap.size());
        if (weatherThingiesList.contains(wt)) {
            wt.destroyWeatherThingy();
            weatherThingiesList.remove(wt);
        }

        if (bleDeviceHashMap.containsKey(deviceAddress)) {
            bleDeviceHashMap.remove(deviceAddress);
        }
        Log.i(TAG, "removeDeviceFromHash: after list size : " + weatherThingiesList.size() + ", hash size: " + bleDeviceHashMap.size());
    }

    public void resetApplication() {
        runRepeatedScanner(false);
        bleScanner.terminateScanning();
        for (WeatherThingy wt : weatherThingiesList) {
            wt.destroyWeatherThingy();
        }
        bleDeviceHashMap.clear();
        weatherThingiesList.clear();
        listAdapter.clear();
    }

    public void printHashMap() {
        Log.i(TAG, "printHashMap:");
        for (String key : bleDeviceHashMap.keySet()) {
            Log.i(TAG, key + " - " + bleDeviceHashMap.get(key).getName());
        }
        Log.i(TAG, "printArrayList:");
        for (WeatherThingy wt : weatherThingiesList) {
            Log.i(TAG, wt.getAddress() + " - " + wt.getName());
        }
    }

    public void updateTemperatureValue(double temperature) {
//        txtTemperature.setText(String.format(Locale.ENGLISH, "%.1f" + (char) 0x00B0 + "C", temperature));
    }

    public void updateHumidityValue(double humidity) {
//        txtHumidity.setText(String.format(Locale.ENGLISH, "%.1f%%", humidity));
    }

    public void updatePressureValue(double pressure) {
//        txtPressure.setText(String.format(Locale.ENGLISH, "%.1fhPa", pressure));
        listAdapter.notifyDataSetChanged();
    }

    public void updateBatteryLevel(int batteryLevel) {
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                bleScanner.startScanning();
                break;
            case R.id.btnReset:
                resetApplication();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        resetApplication();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetApplication();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetApplication();
    }
}
