package com.assignment.nv.novopayassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by navneet on 5/21/2017.
 */

public class DataAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<WeatherData> al_weatherData;
    private final LayoutInflater inflater;

    public DataAdapter(Context mContext, List<WeatherData> al_weatherData) {
        this.mContext = mContext;
        this.al_weatherData = al_weatherData;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder
    {
        TextView tv_city,tv_country,tv_temp;
    }

    @Override
    public int getCount() {
        return al_weatherData.size();
    }

    @Override
    public Object getItem(int position) {
        return al_weatherData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.tv_city = (TextView) convertView.findViewById(R.id.tv_cityName);
            holder.tv_country = (TextView) convertView.findViewById(R.id.tv_countryName);
            holder.tv_temp = (TextView) convertView.findViewById(R.id.tv_temp);

            convertView.setTag(holder);
        }
            else {
            holder = (ViewHolder) convertView.getTag();
        }

        WeatherData data = (WeatherData) getItem(position);
        holder.tv_city.setText(data.getCity());
        holder.tv_country.setText(data.getCountry());
        holder.tv_temp.setText(data.getTempreture());
        return convertView;
        }

    }

