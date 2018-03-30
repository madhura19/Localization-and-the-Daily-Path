package com.example.madhura.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.identity.intents.Address;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    Geocoder geocoder;
    List<Address> address;
    public DatabaseHelper dbHelper;

    private Button addressButton;
    private Button viewMap;
    private TextView loc_display;
    private TextView lat_display;
    private TextView long_display;
    private EditText enterLoc;
    private Button addCustName;
    //private ListView checkInList;

    Double latitude;
    Double longitude;
    ArrayList<String> checkIn_List;
    String full_addr;
    String loc_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dbHelper = new DatabaseHelper(this);
        addressButton = (Button) findViewById(R.id.button_address);
        viewMap = (Button) findViewById(R.id.view_map);
        //checkIn = (Button) findViewById(R.id.check_in);
        loc_display = (TextView) findViewById(R.id.address);
        lat_display = (TextView) findViewById(R.id.latitude);
        long_display = (TextView) findViewById(R.id.longitude);
        enterLoc = (EditText) findViewById(R.id.enterLoc);
        addCustName = (Button) findViewById(R.id.addCN);
        //checkInList = (ListView) findViewById(R.id.checkIn_list);

        //Initializing string
        checkIn_List = new ArrayList<String>();

        geocoder = new Geocoder(this, Locale.getDefault());
        dbHelper = new DatabaseHelper(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                float Accuracy = location.getAccuracy();
                Toast.makeText(MainActivity.this, "Accuracy: " + Accuracy, Toast.LENGTH_SHORT).show();

                try {
                    address = geocoder.getFromLocation(latitude, longitude, 1);
                    String addr = address.get(0).getAddressLine(0);
                    String area = address.get(0).getLocality();
                    String city = address.get(0).getAdminArea();
                    String country = address.get(0).getCountryName();
                    String zipcode = address.get(0).getPostalCode();

                    full_addr = addr + ", " + area + ", " + ", " + city + ", " + country + ", " + zipcode;
                    lat_display.setText("Latitude: " + latitude);
                    long_display.setText("Longitude: " + longitude);
                    loc_display.setText("Full Address: " + full_addr);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };



        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            return;
        } else {
            configureButton();
            /*addressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
                }
            });*/
        }

        addCustName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loc_name = enterLoc.getText().toString();
                Toast.makeText(MainActivity.this, "Location Entered!", Toast.LENGTH_SHORT).show();
            }
        });
        //loc_name = enterLoc.getText().toString();

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
                mapIntent.putExtra("latitude", latitude);
                mapIntent.putExtra("longitude", longitude);
                mapIntent.putExtra("locationName", loc_name);
                startActivity(mapIntent);
            }
        });

        //checkIn();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;


        }
    }

    private void configureButton() {
        /*addressButton.stener(new View.OnClickListener() {
            @OverrideetOnClickLis
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
                //addData(view);
            }
        });*/
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

        /*checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

    }

    public void addData(View view){

        Toast.makeText(this, "addData Called", Toast.LENGTH_SHORT).show();
        //customLocation
        DateFormat dfDate = new SimpleDateFormat("yyyy/mm/dd");
        String date = dfDate.format(Calendar.getInstance().getTime());
        DateFormat dfTime = new SimpleDateFormat("hh:mm");
        String time = dfTime.format(Calendar.getInstance().getTime());
        time = time.toString();
        long id = dbHelper.insertData(time, latitude, longitude, full_addr, loc_name);
        
        if (id < 0){
            Toast.makeText(this, "Transfer failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Transfer Successful", Toast.LENGTH_SHORT).show();
            checkIn();
        }

    }

    private void checkIn(){
        ListView checkInList = (ListView) findViewById(R.id.checkIn_list);
        checkIn_List = dbHelper.getData();
        //checkIn_List.add("Latitude: " + latitude + ", Longitude: " + longitude + "");
        ArrayAdapter<String> name = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, checkIn_List);
        checkInList.setAdapter(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(locationListener);
        }
    }

    /*private class DownloadTask extends AsyncTask<Object, Object, Void> {

        //Initializations
        Context dbcontext;
        DatabaseHelper dbHelper;

        public DownloadTask(Context dbcontext) {
            this.dbcontext = dbcontext;
        }

        @Override
        protected Void doInBackground(Object... links) {

            dbHelper = new DatabaseHelper(dbcontext);
            Cursor res = dbHelper.getData();
            if(res.getCount() == 0){
                return null;
            }
            return null;
        }

    }*/
}