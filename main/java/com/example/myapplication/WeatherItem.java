package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherItem implements Parcelable {
    private String city;
    private String temperature;
    private String condition;
    private String humidity;
    private String windSpeed;
    private String pressure;
    private String clouds;

    public WeatherItem(String city, String temperature, String condition,
                       String humidity, String windSpeed, String pressure, String clouds) {
        this.city = city;
        this.temperature = temperature;
        this.condition = condition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.clouds = clouds;
    }

    protected WeatherItem(Parcel in) {
        city = in.readString();
        temperature = in.readString();
        condition = in.readString();
        humidity = in.readString();
        windSpeed = in.readString();
        pressure = in.readString();
        clouds = in.readString();
    }

    public static final Creator<WeatherItem> CREATOR = new Creator<WeatherItem>() {
        @Override
        public WeatherItem createFromParcel(Parcel in) {
            return new WeatherItem(in);
        }

        @Override
        public WeatherItem[] newArray(int size) {
            return new WeatherItem[size];
        }
    };

    public String getCity() { return city; }
    public String getTemperature() { return temperature; }
    public String getCondition() { return condition; }
    public String getHumidity() { return humidity; }
    public String getWindSpeed() { return windSpeed; }
    public String getPressure() { return pressure; }
    public String getClouds() { return clouds; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(temperature);
        dest.writeString(condition);
        dest.writeString(humidity);
        dest.writeString(windSpeed);
        dest.writeString(pressure);
        dest.writeString(clouds);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
