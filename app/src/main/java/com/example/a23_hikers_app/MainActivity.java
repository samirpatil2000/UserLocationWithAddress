package com.example.a23_hikers_app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitude,longitude,accuracy,altitude,addressView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude=findViewById(R.id.lattitude);
        longitude=findViewById(R.id.longitude);
        accuracy=findViewById(R.id.accuracy);
        altitude=findViewById(R.id.altitude);
        addressView=findViewById(R.id.address);

        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                progressBar.setVisibility(View.INVISIBLE);
               // Toast.makeText(getApplicationContext(),"the location is "+location,Toast.LENGTH_SHORT).show();
                updateLocationInfo(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnowLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnowLocation!=null){
                updateLocationInfo(lastKnowLocation);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {

            startListening();

        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    public void updateLocationInfo(Location location){
       // Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_SHORT).show();

        latitude.setText("Latitude: "+Double.toString(location.getLatitude()));
        longitude.setText("Longitude: "+Double.toString(location.getLongitude()));
        accuracy.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        altitude.setText("Altitude: "+Double.toString(location.getAltitude()));


        String address=" Could not find address :( ";

        Geocoder geocoder=new Geocoder(this, Locale.getDefault());

        try{
           List<Address> listAddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

           if (listAddress != null && listAddress.size()>0){
               address="Address:\n";

               if(listAddress.get(0).getThoroughfare() !=null) {
                   address += listAddress.get(0).getThoroughfare() +" Thoroughfare"+ "\n";
               }

               if (listAddress.get(0).getSubLocality() !=null){
                   address+= listAddress.get(0).getLocality()+" SubLocality"+"\n";
               }

               if (listAddress.get(0).getLocality() !=null){
                   address+= listAddress.get(0).getLocality()+" Locality"+"\n";
               }


               if (listAddress.get(0).getSubAdminArea() !=null){
                   address+= listAddress.get(0).getLocality()+" SubAdminArea"+"\n";
               }

               if (listAddress.get(0).getPostalCode() !=null){
                   address+= listAddress.get(0).getPostalCode()+" Postal Code"+"\n";
               }

               if (listAddress.get(0).getAdminArea() !=null){
                   address+= listAddress.get(0).getAdminArea()+" AdminArea"+"\n";
               }






           }
        }catch (Exception e){
            e.printStackTrace();
        }


        addressView.setText(address);
    }
}
