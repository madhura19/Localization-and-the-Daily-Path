package com.example.madhura.location;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQ_CODE = 123;
    //public static Double lat;
    //public static Double lon;
    ArrayList<Double> list;
    DatabaseHelper dbHelper;
    Cursor cursor;
    String name;

    Polygon polygon;
    PolygonOptions polygonOptions = new PolygonOptions();

    //ArrayList<DatabaseHelper> mDB;
    Button add_name;
    private Marker mark;
    private Marker clickAdd;
    private Circle circle;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        add_name = (Button) findViewById(R.id.add_loc);
        //dbHelper = new DatabaseHelper(this);

        //creating a map fragment
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        //getting latitude and longitude from MainActivity
        Intent intent = this.getIntent();
        double lat = intent.getDoubleExtra("latitude", 0.00);
        double lon = intent.getDoubleExtra("longitude", 0.00);
        String loc = intent.getStringExtra("locationName");
        Toast.makeText(this, "Intent Recieved", Toast.LENGTH_SHORT).show();

        dbHelper = new DatabaseHelper(this);
        cursor = dbHelper.getLocData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No enteries in the db", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            do {
                Toast.makeText(this, "Displaying locations", Toast.LENGTH_SHORT).show();

                //Adding markers
                LatLng point = new LatLng(cursor.getDouble(2), cursor.getDouble(3));
                map.addMarker(new MarkerOptions().position(point).title(cursor.getString(5)));
                polygonOptions.add(point);

                //Adding polyline
                /*map.addPolyline(new PolylineOptions()
                        .add(new LatLng(lat,lon), new LatLng(cursor.getDouble(2), cursor.getDouble(3)))
                        .width(5)
                        .color(Color.RED));*/

            } while (cursor.moveToNext());
            polygon = map.addPolygon((polygonOptions).strokeColor(Color.RED));
        }

        //LatLng sydney = new LatLng(-33.867, 151.206);
        LatLng location = new LatLng(lat, lon);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

        //Add a Marker for current location
        map.addMarker(new MarkerOptions()
                .title(loc)
                //.snippet("The most populous city in Australia.")
                .position(location));

        //Add a red circle around the location
        map.addCircle(new CircleOptions()
                .center(location)
                .radius(30)
                .strokeColor(Color.RED)
                .fillColor(0x44ff0000));

        //Add a polyline
        /*map.addPolyline(new PolylineOptions()
                .add(new LatLng(lat,lon), new LatLng(40.6, -74.4))
                .width(5)
                .color(Color.RED));*/

        //on clicking add button
        add_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent markerIntent = new Intent(MapActivity.this, MarkerNameActivity.class);
                startActivityForResult(markerIntent, REQ_CODE);
            }
        });


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //clears the previously touched positions
                //map.clear();
                //add new marker
                clickAdd = map.addMarker(new MarkerOptions()
                        .title(name)
                        //.snippet("The most populous city in Australia.")
                        .position(latLng)
                        .draggable(true));
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                /*map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        //get Marker Name
                        Intent markerIntent = new Intent(MapActivity.this, MarkerNameActivity.class);
                        startActivityForResult(markerIntent, REQ_CODE);

                        return true;
                    }

                    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
                        MapActivity.super.onActivityResult(requestCode, resultCode, intent);
                        if (requestCode == REQ_CODE){
                            //data from MarkerNameActivity
                            name = intent.getStringExtra("name");
                            Toast.makeText(MapActivity.this, "Got Marker Name!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
            }
        });

        /*
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Toast.makeText(MapActivity.this, "onLocationChanged!", Toast.LENGTH_SHORT).show();

                float[] distance = new float[2];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), circle.getCenter().latitude, circle.getCenter().longitude, distance);
                if (distance[0] > circle.getRadius()){
                    Toast.makeText(MapActivity.this, "Outside!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MapActivity.this, "Inside!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    /*
    private void drawMarkerWithCircle(LatLng pos){
        double radius = 30.00;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000;  //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(pos).radius(radius).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        circle = map.addCircle(circleOptions);
        MarkerOptions markerOptions = new MarkerOptions().position(pos);
        mark = map.addMarker(markerOptions);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        MapActivity.super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE) {
            //data from MarkerNameActivity
            name = intent.getStringExtra("name");
            Toast.makeText(MapActivity.this, "Got Marker Name!", Toast.LENGTH_SHORT).show();

        }
    }
}


