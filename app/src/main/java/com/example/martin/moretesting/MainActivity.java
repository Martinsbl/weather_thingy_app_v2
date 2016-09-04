package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";

    private Button btnScan,btnReset;
    private TextView txtHello, txtTemperature, txtHumidity, txtPressure;

    private ListView listViewWeatherThingy;

    private BluetoothScanner bleScanner;

    private HashMap<String, WeatherThingy> bleDeviceHashMap;
    private ArrayList<WeatherThingy> weatherThingiesList;
    private WeatherThingyListAdapter listAdapter;

    private Handler scannerTimer;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        txtHello = (TextView) findViewById(R.id.txtHello);
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtPressure = (TextView) findViewById(R.id.txtPressure);

        bleDeviceHashMap = new HashMap<>();
        weatherThingiesList = new ArrayList<>();

        listViewWeatherThingy = (ListView) findViewById(R.id.listWeatherThingies);
        listAdapter = new WeatherThingyListAdapter(this, layout.simple_list_item_1, weatherThingiesList);
        listViewWeatherThingy.setAdapter(listAdapter);


        bleScanner = new BluetoothScanner(this);
        bleScanner.startScanning();

        scannerTimer = new Handler();
        runRepeatedScanner(true);
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
                scannerTimer.postDelayed(repeatedScanner, 120000);
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
        txtTemperature.setText(String.format(Locale.ENGLISH, "%.1f" + (char) 0x00B0 + "C", temperature));
    }

    public void updateHumidityValue(double humidity) {
        txtHumidity.setText(String.format(Locale.ENGLISH, "%.1f%%", humidity));
    }

    public void updatePressureValue(double pressure) {
        txtPressure.setText(String.format(Locale.ENGLISH, "%.1fhPa", pressure));
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScan:
                txtHello.setText("Hei");
                bleScanner.startScanning();
                break;
            case R.id.btnReset:
                resetApplication();
                txtHello.setText("Reset");
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
