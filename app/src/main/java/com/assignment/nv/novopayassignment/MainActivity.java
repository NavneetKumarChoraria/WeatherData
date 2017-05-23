package com.assignment.nv.novopayassignment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

   private EditText et_lat, et_lng;
    Button btn_submit;
    private final static String url = "http://api.openweathermap.org/data/2.5/weather?";
    AlertDialog  dialog_register ;
    private String  city,sunrise_time,sunset_time,temp,humidity,latln, countryName;
    TextView tv_country,tv_city ,tv_sunrise,tv_sunset,tv_temp,tv_humidity,tv_latln;
    private String finalUrl;
    private IntentFilter intentFilter;
    private  SimpleDateFormat sdf;
    private ProgressDialog loading;
    private MyReceiver receiver;
    private  MyDataBase mdb;
    private final static String LOADING_MESSAGE = "Lodaing Data";
    private final static String WAITING_MESSAGE = "Please Wait...";
    private final static String JSONTAG_NAME = "name";
    private final static String JSONTAG_SYS = "sys";
    private final static String JSONTAG_COUNTRY = "country";
    private final static String JSONTAG_SUNRISE = "sunrise";
    private final static String JSONTAG_SUNSET = "sunset";
    private final static String JSONTAG_MAIN = "main";
    private final static String JSONTAG_TEMPERATURE = "temp";
    private final static String JSONTAG_HUMIDITY = "humidity";
    private final static String JSONTAG_COORDINATE = "coord";
    private final static String JSONTAG_LATITUDE = "lat";
    private final static String JSONTAG_LONGITUDE = "lon";
    private final static String TAG = MainActivity.class.getName();
    private final static Float FAHRENHEIT=273.15F;
    private final static String MESSAGE = "Invalid Latitude or Longitude";
    private final static String ERROR_MESSAGE = "Please fill this field";
    static String HUMIDITY_VALUE;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        et_lat = (EditText) findViewById(R.id.et_latitude);
        et_lng = (EditText) findViewById(R.id.et_longitude);

        et_lat.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(6), new NumberFilter(null , null) });
       /* et_lat.setFilters(new InputFilter[] { new NumberFilter(null , null) });*/
        et_lng.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(6), new NumberFilter(null , null) });




        btn_submit = (Button) findViewById(R.id.btn_submit);
        sdf = new SimpleDateFormat("h:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        receiver = new MyReceiver();
        mdb = new MyDataBase(MainActivity.this);
        mdb.open();





        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* MyTask mtask = new MyTask();
                mtask.execute(finalUrl);*/


                if (!et_lat.getText().toString().trim().equals("") && !et_lng.getText().toString().equals("")) {


                Double lat = Double.parseDouble(et_lat.getText().toString());
                Double lng = Double.parseDouble(et_lng.getText().toString());
                boolean valid = isValidLatLng(lat,lng);


                    if (valid) {
                        finalUrl = url + "lat=" + lat + "&lon=" + lng + "&appid=78e31a77650c9beed93e60e90554e583";
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        Log.d("Log", finalUrl);
                        et_lng.setText("");
                        et_lat.setText("");
                        getData();
                    } else {
                        Toast.makeText(MainActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (et_lat.getText().toString().trim().equals("")) {
                        et_lat.setError(ERROR_MESSAGE);
                    }
                    else {
                        et_lng.setError(ERROR_MESSAGE);
                    }
                }
            }
        });







    }

    @Override
    protected void onStart() {
        super.onStart();
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search)
        {
            Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
            startActivity(intent);


        }
        return true;
    }



    private void getData(){
        //Showing a progress dialog
          loading = ProgressDialog.show(this,LOADING_MESSAGE, WAITING_MESSAGE,false,false);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(finalUrl,null,new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {


                Log.d(TAG,response.toString());
               parseData(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }


    private void parseData(JSONObject array){

        try {
            if (array.has(JSONTAG_NAME)  ) {
                if (!array.getString(JSONTAG_NAME).equals("")) {
                    city = array.getString(JSONTAG_NAME);
                }
                else {
                    city = "Not Mentioned";
                }
            }
            else {
                city = "Not Mentioned";
            }

            JSONObject object = array.getJSONObject(JSONTAG_SYS);
            if (object.has(JSONTAG_COUNTRY) )
            {
                if (!object.getString(JSONTAG_COUNTRY).equals("")) {
                    Locale loc = new Locale("", object.getString(JSONTAG_COUNTRY));
                    countryName = loc.getDisplayCountry();
                }
                else {
                    countryName =  "Not Mentioned";
                }
            }
            else {
               countryName =  "Not Mentioned";
            }
            String sunriseTime1 = object.getString(JSONTAG_SUNRISE);
            Log.d("Log",sunriseTime1);
            long timeSunRise = Long.parseLong(sunriseTime1);
              date = new Date(timeSunRise*1000L);
            sunrise_time = sdf.format(date);

            String sunsetTime1 = object.getString(JSONTAG_SUNSET);
            long timeSunSet = Long.parseLong(sunsetTime1);
            date = new Date(timeSunSet*1000L);
            sunset_time = sdf.format(date);
            Log.d(TAG,sunrise_time+" "+sunset_time);

            JSONObject object1 = array.getJSONObject(JSONTAG_MAIN);
            String tempKelvin = object1.getString(JSONTAG_TEMPERATURE);
            float kelvin = Float.parseFloat(tempKelvin);
            float celsiusTemp = kelvin - FAHRENHEIT;
            int tempC =(int) celsiusTemp;
            String tempCelsiuc = Integer.toString(tempC);
            Log.d(TAG,tempCelsiuc+"\u2103");
            temp = tempCelsiuc+"\u2103";

            humidity = object1.getString(JSONTAG_HUMIDITY);

            JSONObject object2 = array.getJSONObject(JSONTAG_COORDINATE);
            latln = object2.getString(JSONTAG_LATITUDE)+"     "+object2.getString(JSONTAG_LONGITUDE);
            Log.d(TAG,temp+" "+humidity+"%  "+latln);
            openDialog();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void openDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.datadialog, null);
        tv_country = (TextView) alertLayout.findViewById(R.id.tv_country);
        tv_city = (TextView) alertLayout.findViewById(R.id.tv_city);
        tv_sunrise = (TextView) alertLayout.findViewById(R.id.tv_sunrise);
        tv_sunset = (TextView) alertLayout.findViewById(R.id.tv_sunset);
        tv_temp = (TextView) alertLayout.findViewById(R.id.tv_temp);
        tv_humidity = (TextView) alertLayout.findViewById(R.id.tv_humidity);
        tv_latln = (TextView) alertLayout.findViewById(R.id.tv_latlng);
        HUMIDITY_VALUE = humidity+"%";
        tv_city.setText(city);
        tv_country.setText(countryName);
        tv_sunset.setText(sunset_time);
        tv_sunrise.setText(sunrise_time);
        tv_temp.setText(temp);
        tv_humidity.setText(HUMIDITY_VALUE);
        tv_latln.setText(latln);

        mdb.insertData(countryName,city,sunrise_time,sunset_time,temp,humidity,latln);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertLayout);
        //builder.setCancelable(false);
        dialog_register = builder.create();
        dialog_register.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        loading.dismiss();
        dialog_register.show();



    }

    public boolean isValidLatLng(double lat, double lng){
        if(lat < -90 || lat > 90)
        {
            return false;
        }
        else if(lng < -180 || lng > 180)
        {
            return false;
        }
        return true;
    }

    public class MyReceiver extends BroadcastReceiver {


        MainActivity obj;


        @Override
        public void onReceive(final Context context, Intent intent) {

            obj = new MainActivity();


            if(isConnected(context)) {
                Log.i(TAG,"onnected");




            }
            else {

                //Toast.makeText(context, "Lost connect.", Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Internet Connection Lost")
                        .setMessage("Please Connect to internet and press OK!")
                        .setCancelable(false)
                        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ViewGroup vg = (ViewGroup) findViewById(R.id.main);
                                registerReceiver(receiver, intentFilter);
                                vg.invalidate();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }

        public boolean isConnected(Context context)
        {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = conn.getActiveNetworkInfo();
            boolean isConnected = info != null && info.isConnected();
            return isConnected;
        }
    }


}
