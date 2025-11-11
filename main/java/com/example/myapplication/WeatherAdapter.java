package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<WeatherItem> weatherList;

    public WeatherAdapter(List<WeatherItem> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherItem item = weatherList.get(position);
        holder.cityText.setText(item.getCity());
        holder.tempText.setText("🌡 " + item.getTemperature());
        holder.conditionText.setText("Condition: " + item.getCondition());
        holder.humidityText.setText("💧 Humidity: " + item.getHumidity());
        holder.windText.setText("💨 Wind: " + item.getWindSpeed());
        holder.pressureText.setText("📊 Pressure: " + item.getPressure());
        holder.cloudText.setText("☁️ Clouds: " + item.getClouds());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tempText, conditionText, cityText, humidityText, windText, pressureText, cloudText;

        public ViewHolder(View itemView) {
            super(itemView);
            cityText = itemView.findViewById(R.id.cityText);
            tempText = itemView.findViewById(R.id.tempText);
            conditionText = itemView.findViewById(R.id.conditionText);
            humidityText = itemView.findViewById(R.id.humidityText);
            windText = itemView.findViewById(R.id.windText);
            pressureText = itemView.findViewById(R.id.pressureText);
            cloudText = itemView.findViewById(R.id.cloudText);
        }
    }
}
