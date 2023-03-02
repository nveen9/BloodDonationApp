package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.blooddonation.databinding.ActivityLocationPickBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class locationPick extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityLocationPickBinding binding;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLocationPickBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        client = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(locationPick.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Bundle bundle = getIntent().getExtras();
            String ABC = bundle.getString("Donor");

            if (ABC.equals("ABC")){
                RealtimeLocationInRegister();
            }else {
                RealtimeLocation();
            }
        } else {
            ActivityCompat.requestPermissions(locationPick.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void RealtimeLocationInRegister() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull @NotNull LatLng latLng) {
                                    MarkerOptions options = new MarkerOptions().position(latLng);
                                    googleMap.addMarker(options);
                                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                                            try {
                                                Geocoder geocoder = new Geocoder(locationPick.this, Locale.getDefault());
                                                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                                double latitude = addresses.get(0).getLatitude();
                                                double longitude = addresses.get(0).getLongitude();
                                                String locality = addresses.get(0).getLocality();
                                                String address = addresses.get(0).getAddressLine(0);

                                                Intent intent = new Intent(locationPick.this,registerFragMain.class);
                                                intent.putExtra("latitude",latitude);
                                                intent.putExtra("longitude",longitude);
                                                intent.putExtra("locality",locality);
                                                intent.putExtra("Address",address);
                                                setResult(1000,intent);
                                                finish();
                                            }catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            return true;
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void RealtimeLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull @NotNull LatLng latLng) {
                                    MarkerOptions options = new MarkerOptions().position(latLng);
                                    googleMap.addMarker(options);
                                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                                            try {
                                                Geocoder geocoder = new Geocoder(locationPick.this, Locale.getDefault());
                                                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                                double latitude = addresses.get(0).getLatitude();
                                                double longitude = addresses.get(0).getLongitude();
                                                String locality = addresses.get(0).getLocality();
                                                String address = addresses.get(0).getAddressLine(0);

                                                FirebaseUser uid = FirebaseAuth.getInstance().getCurrentUser();
                                                String userID = uid.getUid();
                                                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("donor").document(userID);
                                                Map<String,Object> userData = new HashMap<>();
                                                userData.put("latitude",latitude);
                                                userData.put("longitude",longitude);
                                                if (addresses.get(0).getLocality()==null){
                                                    userData.put("locality","");
                                                }else {
                                                    userData.put("locality",locality);
                                                }
                                                userData.put("address",address);
                                                documentReference.update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(locationPick.this, "Location set Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(locationPick.this, donorAccount.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            return true;
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
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
    }
}