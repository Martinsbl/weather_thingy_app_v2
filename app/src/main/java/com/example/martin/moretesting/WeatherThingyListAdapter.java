package com.example.martin.moretesting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WeatherThingyListAdapter extends RecyclerView.Adapter<WeatherThingyListAdapter.ViewHolder> {

    private HashMap<String, Integer> mIdMap = new HashMap<>();
    private Context context;
    private List<WeatherThingy> listWeatherThingies;


    public WeatherThingyListAdapter(Context context, List<WeatherThingy> objects) {
        this.context = context;
        this.listWeatherThingies = objects;


        for (int i = 0; i < objects.size(); i++) {
            mIdMap.put(objects.get(i).getAddress(), i);
        }
    }

    public void clear(){
        listWeatherThingies = null;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_thingy_list_item, null);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.txtTemperature.setText(String.format(Locale.ENGLISH, "%.1f" + (char) 0x00B0 + "C", listWeatherThingies.get(position).getCurrentWeather().getTemperature()));

        holder.txtHumidity.setText(String.format(Locale.ENGLISH, "%.1f%%", listWeatherThingies.get(position).getCurrentWeather().getHumidity()));

        holder.txtPressure.setText(String.format(Locale.ENGLISH, "%.1fhPa", listWeatherThingies.get(position).getCurrentWeather().getPressure()));

        holder.txtDeviceAddress.setText(listWeatherThingies.get(position).getAddress());

        holder.txtRssi.setText(String.format(Locale.ENGLISH, "%d dBm", listWeatherThingies.get(position).getmBleModel().rssi));

        holder.txtBattery.setText(String.format(Locale.ENGLISH, "%d%%", listWeatherThingies.get(position).getCurrentWeather().getBatteryLevel()));

        holder.dataHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout container = holder.dataContainer;
                container.setVisibility(container.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                int iconReference = container.getVisibility() == View.GONE ? R.drawable.ic_expand_more_black : R.drawable.ic_expand_less_black;
                Drawable expandDrawable = ContextCompat.getDrawable(context, iconReference);
                holder.expandIcon.setImageDrawable(expandDrawable);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listWeatherThingies.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTemperature, txtHumidity, txtPressure, txtDeviceAddress, txtRssi, txtBattery;
        LinearLayout dataContainer;
        LinearLayout parentLayout;
        LinearLayout dataHeaderView;
        ImageView expandIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTemperature = (TextView) itemView.findViewById(R.id.txtListTemperature);
            txtHumidity = (TextView) itemView.findViewById(R.id.txtListHumidity);
            txtPressure = (TextView) itemView.findViewById(R.id.txtListPressure);
            txtDeviceAddress = (TextView) itemView.findViewById(R.id.txtListDeviceAddress);
            txtRssi = (TextView) itemView.findViewById(R.id.txtListDeviceRssi);
            txtBattery = (TextView) itemView.findViewById(R.id.txtListBattery);

            dataContainer = (LinearLayout) itemView.findViewById(R.id.sensor_data_container);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parent_view);
            dataHeaderView = (LinearLayout) itemView.findViewById(R.id.data_header_view);

            expandIcon = (ImageView) itemView.findViewById(R.id.expand_icon);
        }
    }
}
