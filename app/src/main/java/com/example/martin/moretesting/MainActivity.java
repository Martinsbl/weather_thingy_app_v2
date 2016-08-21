package com.example.martin.moretesting;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";

    private Button btnScan,btnReset;
    private TextView txtHello, txtTemperature, txtHumidity;
    private BluetoothScanner bleScanner;

    private HashMap<String, WeatherThingy> bleDeviceHashMap;
    private ArrayList<WeatherThingy> weatherThingiesList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        txtHello = (TextView) findViewById(R.id.txtHello);
        txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);

        bleDeviceHashMap = new HashMap<>();
        weatherThingiesList = new ArrayList<>();

        bleScanner = new BluetoothScanner(this);
        bleScanner.startScanning();

    }

    public void addDeviceToList(BluetoothDevice bleDevice) {
        String deviceAddress = bleDevice.getAddress();
        if (!bleDeviceHashMap.containsKey(deviceAddress)) {
            WeatherThingy weatherThingy = new WeatherThingy(this, bleDevice);
            bleDeviceHashMap.put(deviceAddress, weatherThingy);
            weatherThingiesList.add(weatherThingy);
            Log.i(TAG, "createWeatherThingy: Added " + deviceAddress + " to list");
        }
    }

    public void resetApplication() {
        bleScanner.terminateScanning();
        for (WeatherThingy wt : weatherThingiesList) {
            wt.destroyWeatherThingy();
        }

        bleDeviceHashMap.clear();
        weatherThingiesList.clear();
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
        txtTemperature.setText(String.format(Locale.ENGLISH, "%.02f" + (char) 0x00B0 + "C", temperature));
    }

    public void updateHumidityValue(double humidity) {
        txtHumidity.setText(String.format(Locale.ENGLISH, "%.02f%%", humidity));
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
