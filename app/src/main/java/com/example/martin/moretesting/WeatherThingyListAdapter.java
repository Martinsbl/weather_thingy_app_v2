package com.example.martin.moretesting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Martin on 23.08.2016.
 */

public class WeatherThingyListAdapter extends ArrayAdapter<WeatherThingy> {

    private final static String TAG = "WTListAdapter";

    private HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
    private Context context;
    private List<WeatherThingy> listWeatherThingies;

    public WeatherThingyListAdapter(Context context, int resource, List<WeatherThingy> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listWeatherThingies = objects;


        for (int i = 0; i < objects.size(); i++) {
            mIdMap.put(objects.get(i).getAddress(), i);
        }
    }

//    @Override
//    public long getItemId(int position) {
//        try {
//            String item = getItem(position).getAddress();
//            return mIdMap.get(item);
//        } catch (Exception e) {
//            Log.i(TAG, "getItemId: Failed to get item ID.");
//        }
//        return 0;
//    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.weather_thingy_list_item, parent, false);
        }

        TextView txtTemperature = (TextView) view.findViewById(R.id.txtListTemperature);
        txtTemperature.setText(String.format(Locale.ENGLISH, "%.1f" + (char) 0x00B0 + "C", listWeatherThingies.get(position).getCurrentWeather().getTemperature()));

        TextView txtHumidity = (TextView) view.findViewById(R.id.txtListHumidity);
        txtHumidity.setText(String.format(Locale.ENGLISH, "%.1f%%", listWeatherThingies.get(position).getCurrentWeather().getHumidity()));

        TextView txtPressure = (TextView) view.findViewById(R.id.txtListPressure);
        txtPressure.setText(String.format(Locale.ENGLISH, "%.1fhPa", listWeatherThingies.get(position).getCurrentWeather().getPressure()));

        TextView txtDeviceAddress = (TextView) view.findViewById(R.id.txtListDeviceAddress);
        txtDeviceAddress.setText(listWeatherThingies.get(position).getAddress());

        TextView txtRssi = (TextView) view.findViewById(R.id.txtListDeviceRssi);
        txtRssi.setText(String.format(Locale.ENGLISH, "%d dBm", listWeatherThingies.get(position).getmBleModel().rssi));

        TextView txtBattery = (TextView) view.findViewById(R.id.txtListBattery);
        txtBattery.setText(String.format(Locale.ENGLISH, "%d%%", listWeatherThingies.get(position).getCurrentWeather().getBatteryLevel()));

        return view;
    }
}
