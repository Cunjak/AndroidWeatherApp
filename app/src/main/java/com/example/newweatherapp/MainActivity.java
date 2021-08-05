package com.example.newweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appiId = "08b8f3fe2bc9f670357d991d0703a031";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
    }



    public void getWeatherDetails(View view) {

        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        if(city.equals(""))
            tvResult.setText("City field cannot be empty");
        else {
            if (!country.equals("")) {
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appiId;
            }
            else {
                tempUrl = url + "?q=" + city + "&appid=" + appiId;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject main = jsonObject.getJSONObject("main");
                        double temp = main.getDouble("temp") - 273.15;
                        double feels_like = main.getDouble("feels_like") - 273.15;
                        float pressure = main.getInt("pressure");
                        int humidity = main.getInt("humidity");
                        JSONObject wind = jsonObject.getJSONObject("wind");
                        String windString = wind.getString("speed");
                        JSONObject clouds = jsonObject.getJSONObject("clouds");
                        String cloudsString = clouds.getString("all");
                        JSONObject sys = jsonObject.getJSONObject("sys");
                        String countryString = sys.getString("country");
                        String cityString = jsonObject.getString("name");

                        output += "The weather of " + cityString + "(" + countryString + ")"
                                + "\n Temp: " + String.format("%.2f", temp) + " °C"
                                + "\n Feels like: " + String.format("%.2f", feels_like) + " °C"
                                + "\n Humidity: " + humidity + "%"
                                + "\n Description: " + description
                                + "\n Wind: " + windString + "m/s"
                                + "\n Cloudiness: " + cloudsString + "%"
                                + "\n Pressure: " + pressure + "hPa";
                        tvResult.setText(output);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT);
                }
            }
        );

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
            }

    }
}