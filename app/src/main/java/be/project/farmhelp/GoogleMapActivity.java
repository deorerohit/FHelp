package be.project.farmhelp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import be.project.farmhelp.authentication.SessionManager;
import be.project.farmhelp.dataholders.DataFromFirebase;
import be.project.farmhelp.getservice.DisplayServiceDetails;

public class GoogleMapActivity extends AppCompatActivity {

    SessionManager currentUser;
    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private static final int REQUEST_CODE = 111;
    private String sessionMobNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        currentUser = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        sessionMobNumber = userDetails.get(SessionManager.KEY_MOBILE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        Map<Marker, DataFromFirebase> firebaseDataMap = new LinkedHashMap<>();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                final Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobNo");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {

                                String mobNumber = ds.child("mobNo").getValue(String.class);
                                boolean isServiceProvider = ds.child("serviceProvider").getValue(Boolean.class);

                                if (sessionMobNumber.equals(mobNumber) || isServiceProvider) {
                                    String name = ds.child("name").getValue(String.class);
                                    String service = ds.child("addServiceClassToFirebase/service").getValue(String.class);
                                    String rate = ds.child("addServiceClassToFirebase/rate").getValue(String.class);
                                    String contact = ds.child("addServiceClassToFirebase/contact").getValue(String.class);
                                    String description = ds.child("addServiceClassToFirebase/description").getValue(String.class);

                                    DataFromFirebase dataFromFirebase = new DataFromFirebase(name, service, description, contact, rate);

                                    LatLng latLng = new LatLng(ds.child("latitude").getValue(Double.class), ds.child("longitude").getValue(Double.class));
                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(ds.child("name").getValue(String.class));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                                    //googleMap.addMarker(markerOptions).showInfoWindow();
                                    Marker marker = googleMap.addMarker(markerOptions);

                                    firebaseDataMap.put(marker, dataFromFirebase);
                                }
                            }
                        } else {
                            Toast.makeText(GoogleMapActivity.this, "No user exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GoogleMapActivity.this, "Oops! An error occured.", Toast.LENGTH_SHORT).show();
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(GoogleMapActivity.this, DisplayServiceDetails.class);
                        DataFromFirebase dataFromFirebase = firebaseDataMap.get(marker);
                        intent.putExtra("name", dataFromFirebase.getName());
                        intent.putExtra("service", dataFromFirebase.getService());
                        intent.putExtra("rate", dataFromFirebase.getRate());
                        intent.putExtra("contact", dataFromFirebase.getContact());
                        intent.putExtra("desc", dataFromFirebase.getDescription());
                        startActivity(intent);
                        return true;
                    }
                });
            }
        });
    }
}