package com.assignment.nv.novopayassignment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    AlertDialog dialog_register;
    ListView lv_data;
    TextView tv_countryName,tv_cityName,tv_temp,tv_sunset,tv_sunrise,tv_latlng,tv_humidity;
    Button btn_dismiss;
    private  MyDataBase mdb;
    List<WeatherData> al_data;
    final String TAG = HistoryActivity.class.getName();
    Cursor c;
    DataAdapter adapter;
    private String country,city,temp,sunset,sunrise,humidity,latlng;
    static String HUMIDITY_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        setContentView(R.layout.activity_history);

        lv_data = (ListView) findViewById(R.id.lv_data);
        mdb = new MyDataBase(HistoryActivity.this);
        mdb.open();
        al_data = new ArrayList<>();

        adapter = new DataAdapter(this,al_data);

        settingData();
        lv_data.setAdapter(adapter);


        lv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String id_database = Integer.toString(position+1);
                Log.d(TAG,id_database);
                Cursor c1 = mdb.getDataById(id_database);
                if(c!=null)
                {
                    while (c1.moveToNext()){

                          country = c1.getString(1);
                          city = c1.getString(2);
                          sunrise = c1.getString(3);
                          sunset = c1.getString(4);
                          temp = c1.getString(5);
                          humidity = c1.getString(6);
                          latlng = c1.getString(7);

                    Log.d(TAG,city+" "+country+" "+temp);


                    }
                }
                openDialog();

            }
        });

    }

    {


    }

    private void settingData() throws SQLException
    {
        c = mdb.getData();



        if(c!=null)
        {
            while (c.moveToNext()){

                String city = c.getString(2);
                String country = c.getString(1);
                String temp = c.getString(5);

                Log.d(TAG,city+" "+country+" "+temp);
                WeatherData weatherData = new WeatherData(city,country,temp);
                al_data.add(weatherData);

            }
        }
    }

    private void openDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.historydialog, null);

        tv_countryName = (TextView) alertLayout.findViewById(R.id.tv_country1);
        tv_cityName = (TextView) alertLayout.findViewById(R.id.tv_city1);
        tv_sunrise = (TextView) alertLayout.findViewById(R.id.tv_sunrise1);
        tv_sunset = (TextView) alertLayout.findViewById(R.id.tv_sunset1);
        tv_temp = (TextView) alertLayout.findViewById(R.id.tv_temp1);
        tv_humidity = (TextView) alertLayout.findViewById(R.id.tv_humidity1);
        tv_latlng = (TextView) alertLayout.findViewById(R.id.tv_latlng1);
        btn_dismiss = (Button) alertLayout.findViewById(R.id.btn_cancel);

        HUMIDITY_VALUE = humidity+"%";

        tv_countryName.setText(country);
        tv_cityName.setText(city);
        tv_sunrise.setText(sunrise);
        tv_sunset.setText(sunset);
        tv_temp.setText(temp);
        tv_humidity.setText(HUMIDITY_VALUE);
        tv_latlng.setText(latlng);

        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
        builder.setView(alertLayout);
        builder.setCancelable(false);

        if (dialog_register == null) {
            dialog_register = builder.create();
            dialog_register.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            dialog_register.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialog_register = null;
                }
            });
        }

           if (!dialog_register.isShowing()) {
               dialog_register.show();
           }
        else {
               Log.i(TAG,"Dialog is already showing");
           }


        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_register.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
