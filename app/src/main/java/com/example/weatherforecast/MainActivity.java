package com.example.weatherforecast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherforecast.Models.FWeather;
import com.example.weatherforecast.retrofit.RetrofitClient;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    EditText editTextTemp;
    TextView tempertaure;
    TextView dateTime;
    ImageView location;
    TextView type;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView locationImage;
    ImageView detectLocation;

    ImageView popupMenu;
    String metrics = "celcius";
    String temp;

    LocationManager locationServices;

    InternetBoadcastReceiver internetBoadcastReceiver;

//Permissions Callback for Android 13
    ActivityResultLauncher coarselocation = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {

            Toast.makeText(this, "Pemissions Granted", Toast.LENGTH_SHORT).show();


        }

    });


    ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),

            isGranted -> {

                if (isGranted) {

                    coarselocation.launch(Manifest.permission.ACCESS_COARSE_LOCATION);


                }


            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing FusedLocationProviderClient for getting live location

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        //Initializing Views.

        editTextTemp = (EditText) findViewById(R.id.edittextlocation);
        dateTime = (TextView) findViewById(R.id.datetime);
        tempertaure = (TextView) findViewById(R.id.textviewtemp);
        locationImage = (ImageView) findViewById(R.id.location_image);
        detectLocation = (ImageView) findViewById(R.id.locationimage);
        type = (TextView) findViewById(R.id.type);
        popupMenu = (ImageView) findViewById(R.id.popupmenu);

        //Initializing locationManager to check if GPS is Present or not

        locationServices = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Registering Broadcast receiver for detecting connectivity/noconnectivity
        internetBoadcastReceiver = new InternetBoadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(internetBoadcastReceiver , intentFilter);

        //PopupMenu for Celcius/Farheneit
        popupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu1 = new PopupMenu(MainActivity.this, v);
                Menu menu = popupMenu1.getMenu();

                popupMenu1.getMenuInflater().inflate(R.menu.celcius_farenheit, menu);
                popupMenu1.show();

                popupMenu1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int itemId = item.getItemId();

                        if (itemId == R.id.celcius) {

                            metrics = "celcius";

                            tempertaure.setText(temp + "C");

                        } else {

                            metrics = "farheneit";

                            String farhen = Integer.toString(((Integer.parseInt(temp) * 9) / 5) + 32);
                            tempertaure.setText(farhen + "F");

                        }


                        return false;
                    }
                });
            }
        });

        detectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                    //if GPS not enabled show dialog and go to settings
                    if (!locationServices.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        showDialog();

                    } else {

                        //Detecting my location Weather

                        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY , new CancellationTokenSource().getToken()).addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {

                                Location location1 = task.getResult();

                                if (Geocoder.isPresent()) {

                                    Geocoder geocoder = new Geocoder(MainActivity.this);

                                    try {

                                        if (location1 != null) {

                                            List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 5);
                                            //Api Call(Retrofit)
                                            getData(addresses.get(0).getLocality().trim(), location1.getLatitude(), location1.getLongitude());
                                            editTextTemp.setText(addresses.get(0).getLocality().trim());


                                        } else {

                                            Toast.makeText(MainActivity.this, "Failed to fetch Data", Toast.LENGTH_SHORT).show();
                                        }


                                        ;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                            }
                        });


                    }


                }


            }
        });

        editTextTemp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                getData(s.toString(), 0.0, 0.0);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }


        } else {

            //if GPS not enabled show dialog and go to settings
            if (!locationServices.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                showDialog();


            } else {

                //Detecting my location Weather

                fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY , new CancellationTokenSource().getToken()).addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        Location location1 = task.getResult();

                        if (Geocoder.isPresent()) {

                            Geocoder geocoder = new Geocoder(MainActivity.this);

                            try {

                                if (location1 != null) {

                                    List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 5);
                                    //Api Call(Retrofit)
                                    getData(addresses.get(0).getLocality().trim(), location1.getLatitude(), location1.getLongitude());
                                    editTextTemp.setText(addresses.get(0).getLocality().trim());


                                } else {

                                    Toast.makeText(MainActivity.this, "Failed to fetch Data", Toast.LENGTH_SHORT).show();
                                }


                                ;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                });



            }
        }
    }














    private void getData(String locality, double latitude, double longitude) {

        //Toast.makeText(this, "Locaity:" + locality, Toast.LENGTH_SHORT).show();

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        Api api = retrofitClient.getApi();

        api.authorizedzation(URls.KEY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    Map map = new HashMap();
                    map.put("access_key" , URls.KEY);
                    map.put("query" , locality);

                    api.weatherCall(map).enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {

                            if (response.isSuccessful()) {

                               // Toast.makeText(MainActivity.this, "Success Called", Toast.LENGTH_SHORT).show();
                                JsonObject jsonObject = (JsonObject) response.body();
                                if (jsonObject != null) {

                                    Log.d(TAG, "onResponse: " + jsonObject.toString());

                                    JsonObject current = jsonObject.getAsJsonObject("current");
                                    JsonObject location = jsonObject.getAsJsonObject("location");

                                    if (location != null) {

                                        dateTime.setText("Date: " +location.get("localtime").getAsString().split(" ")[0]
                                        +"\n" + "Time: " + location.get("localtime").getAsString().split(" ")[1]);
                                    }
//                                Log.d(TAG, "onResponse: " + jsonObject.toString());

                                    if (current != null) {

                                        JsonElement element = current.get("temperature");
                                        if (metrics.equals("celcius")) {

                                            temp = element.toString();

                                            tempertaure.setText(temp + "C");

                                        } else {

                                            temp = element.toString();

                                            String farhen = Integer.toString(((Integer.parseInt(temp)*9)/5)+32);
                                            tempertaure.setText(farhen + "F");
                                        }


                                        JsonArray jsonArray = current.getAsJsonArray("weather_icons");
                                        JsonArray type = current.getAsJsonArray("weather_descriptions");

                                        JsonElement element1 = jsonArray.get(0).getAsJsonPrimitive();
                                        JsonElement element2 = type.get(0).getAsJsonPrimitive();

                                        Log.d(TAG, "onResponse: " + element1.getAsString());

                                        MainActivity.this.type.setText(element2.getAsString());

                                        //Toast.makeText(MainActivity.this, "Pic: " + element1.toString(), Toast.LENGTH_SHORT).show();

                                        Glide.with(MainActivity.this).load(element1.getAsString()).
                                                placeholder(R.mipmap.ic_launcher)
                                                .into(locationImage);

                                    }







                                }


                            } else {

                                Toast.makeText(MainActivity.this, "ErrorCall", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                            Toast.makeText(MainActivity.this, "Error:" + t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {

                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }
        });



       
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            if (grantResults.length>0) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {



                }
            }
        }
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage("App requires Location Services to be enabled.Do you want to enable it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Unregistering broadcast receiver for connectivity change.
        unregisterReceiver(internetBoadcastReceiver);
    }
}