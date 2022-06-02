package com.tieutech.highlyadvancedtrucksharingapp;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.tieutech.highlyadvancedtrucksharingapp.databinding.ActivityMapsBinding;
import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.util.DirectionsJSONParser;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Google Map view variables
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String url;
    private DownloadTask downloadTask = new DownloadTask();

    //Google Map data
    final int REQUEST_PHONE_CALL = 200;
    private double destinationLatitude, destinationLongitude;
    private double pickupLocationLatitude, pickupLocationLongitude;
    private String pickupLocation, destination, duration;

    //View variables
    TextView pickupLocationTextView;
    TextView destinationTextView;
    TextView fareTextView;
    TextView travelTimeTextView;
    ImageView messageDriverImageView;

    //Data variables
    byte[] senderImageByteArray;
    String senderName;
    String pickupTime;
    String pickupDate;
    String receiverName;
    String goodType;
    String vehicleType;
    String weight;
    String height;
    String width;
    String length;

    byte[] goodImage;
    String goodClassification;
    double goodClassificationConfidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pickupLocationTextView = findViewById(R.id.pickupLocationTextView);
        destinationTextView = findViewById(R.id.destinationTextView);
        fareTextView = findViewById(R.id.fareTextView);
        travelTimeTextView = findViewById(R.id.travelTimeTextView);
        messageDriverImageView = findViewById(R.id.messageDriverImageView);

        messageDriverImageView.setImageResource(R.drawable.ic_message);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Obtain data passed from OrderDetailsActivity
        Intent intent = getIntent();
        senderImageByteArray = intent.getByteArrayExtra(Util.DATA_SENDER_IMAGE);
        senderName = intent.getStringExtra(Util.DATA_SENDER_NAME);
        pickupDate = intent.getStringExtra(Util.DATA_PICKUP_DATE);
        pickupTime = intent.getStringExtra(Util.DATA_PICKUP_TIME);
        pickupLocation = intent.getStringExtra(Util.DATA_PICKUP_LOCATION);
        pickupLocationLatitude = intent.getDoubleExtra(Util.DATA_PICKUP_LOCATION_LATITUDE, 0);
        pickupLocationLongitude = intent.getDoubleExtra(Util.DATA_PICKUP_LOCATION_LONGITUDE, 0);
        destination = intent.getStringExtra(Util.DATA_DESTINATION);
        destinationLatitude = intent.getDoubleExtra(Util.DATA_DESTINATION_LATITUDE, 0);
        destinationLongitude = intent.getDoubleExtra(Util.DATA_DESTINATION_LONGITUDE, 0);
        receiverName = intent.getStringExtra(Util.DATA_RECEIVER_NAME);
        goodType = intent.getStringExtra(Util.DATA_GOOD_TYPE);
        vehicleType = intent.getStringExtra(Util.DATA_VEHICLE_TYPE);
        weight = intent.getStringExtra(Util.DATA_WEIGHT);
        height = intent.getStringExtra(Util.DATA_HEIGHT);
        width = intent.getStringExtra(Util.DATA_WIDTH);
        length = intent.getStringExtra(Util.DATA_LENGTH);

        goodImage = intent.getByteArrayExtra(Util.DATA_GOOD_IMAGE);
        goodClassification = intent.getStringExtra(Util.DATA_GOOD_CLASSIFICATION);
        goodClassificationConfidence = intent.getDoubleExtra(Util.DATA_GOOD_CLASSIFICATION_CONFIDENCE, 0);

        //Configure views
        pickupLocationTextView.setText(pickupLocation);
        destinationTextView.setText(destination);

        //Obtain Directions URL
        url = getDirectionUrl(pickupLocationLatitude, pickupLocationLongitude, destinationLatitude, destinationLongitude);

        //Set the duration of the trip
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray routesArray = response.getJSONArray("routes");
                    JSONArray legsArray = ((JSONObject)routesArray.get(0)).getJSONArray("legs");
                    JSONObject duration = legsArray.getJSONObject(0).getJSONObject("duration");

                    int fare = (int) ((int) (duration.getDouble("value")/60) * 1.5);

                    travelTimeTextView.setText(duration.getString("text"));
                    fareTextView.setText("$" + Integer.toString(fare));
                } catch (JSONException e) {
                    Log.i("masuk", e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error with API");
            }
        }
        );

        requestQueue.add(jsonObjectRequest);

        downloadTask.execute(url);
    }

    //Download the URL
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = "";

            try {
                data = downloadUrl(strings[0].toString()).toString();
            } catch (Exception e)
            {
                Log.i("Background task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    //Parse the URL
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap point = path.get(j);

                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(16);
                lineOptions.color(Color.parseColor("#800fd1"));
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    //Download the URL
    private String downloadUrl(String url) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            java.net.URL strUrl = new URL(url);
            urlConnection = (HttpURLConnection) strUrl.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //Get the Direction URL
    private String getDirectionUrl(double locationLatitude, double locationLongitude, double destinationLatitude, double destinationLongitude)
    {
        // origin of route
        String originLatLng = "?origin=" + locationLatitude + "," + locationLongitude;

        // destination of route
        String destinationLatLng = "destination=" + destinationLatitude + "," + destinationLongitude;

        // transportation mode
        String mode = "mode=driving";

        // building parameters to web service
        String parameters = originLatLng + "&" + destinationLatLng + "&" + mode;

        // output format
        String output = "json";

        // complete url
        return "https://maps.googleapis.com/maps/api/directions/" + output + parameters + "&key=" + Util.API_KEY;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in origin and move the camera
        LatLng origin = new LatLng(pickupLocationLatitude, pickupLocationLongitude);
        LatLng destination = new LatLng(destinationLatitude, destinationLongitude);
        mMap.addMarker(new MarkerOptions().position(origin).title("Origin").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(destination).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.addMarker(new MarkerOptions().position(destination).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 13));

        List<LatLng> lstLatLngRoute = Arrays.asList(origin, destination);

        zoomRoute(mMap, lstLatLngRoute);
    }

    //Zoom the Google Map to the route
    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    //Listener for the Book Now Button
    public void bookNowClick(View view)
    {
//        Intent checkoutIntent = new Intent(MapsActivity.this, CheckoutActivity.class);
        Intent checkoutIntent = new Intent(MapsActivity.this, FingerprintActivity.class);

        //Send data to the OrderDetailsActivity
        checkoutIntent.putExtra(Util.DATA_SENDER_IMAGE, senderImageByteArray);
        checkoutIntent.putExtra(Util.DATA_SENDER_NAME, senderName);
        checkoutIntent.putExtra(Util.DATA_RECEIVER_NAME, receiverName);
        checkoutIntent.putExtra(Util.DATA_PICKUP_DATE, pickupDate);
        checkoutIntent.putExtra(Util.DATA_PICKUP_TIME, pickupTime);
        checkoutIntent.putExtra(Util.DATA_PICKUP_LOCATION, pickupLocation);
        checkoutIntent.putExtra(Util.DATA_PICKUP_LOCATION_LATITUDE, pickupLocationLatitude);
        checkoutIntent.putExtra(Util.DATA_PICKUP_LOCATION_LONGITUDE, pickupLocationLongitude);
        checkoutIntent.putExtra(Util.DATA_DESTINATION, destination);
        checkoutIntent.putExtra(Util.DATA_DESTINATION_LATITUDE, destinationLatitude);
        checkoutIntent.putExtra(Util.DATA_DESTINATION_LONGITUDE, destinationLongitude);
        checkoutIntent.putExtra(Util.DATA_GOOD_TYPE, goodType);
        checkoutIntent.putExtra(Util.DATA_WEIGHT, weight);
        checkoutIntent.putExtra(Util.DATA_WIDTH, width);
        checkoutIntent.putExtra(Util.DATA_LENGTH, length);
        checkoutIntent.putExtra(Util.DATA_HEIGHT, height);
        checkoutIntent.putExtra(Util.DATA_VEHICLE_TYPE, vehicleType);

        checkoutIntent.putExtra(Util.DATA_GOOD_IMAGE, goodImage);
        checkoutIntent.putExtra(Util.DATA_GOOD_CLASSIFICATION, goodClassification);
        checkoutIntent.putExtra(Util.DATA_GOOD_CLASSIFICATION_CONFIDENCE, goodClassificationConfidence);

        startActivity(checkoutIntent);
    }

    //Listener for Call Driver Button
    public void callDriverClick(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL); }
        else
        {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel: 0401020304"));
            startActivity(intent);
        }
    }

    //Listener for Message Driver ImageView
    public void messageDriverClick(View view) {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

}