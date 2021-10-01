package be.project.farmhelp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GoogleMapActivity extends AppCompatActivity {


    private FusedLocationProviderClient client;
    private SupportMapFragment mapFragment;
    private static final int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        client = LocationServices.getFusedLocationProviderClient(GoogleMapActivity.this);
        getCurrentLocation();
    }

    private void getCurrentLocation() {

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                final Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobNo");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                LatLng latLng = new LatLng(ds.child("latitude").getValue(Double.class), ds.child("longitude").getValue(Double.class));
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(ds.child("name").getValue(String.class));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                                googleMap.addMarker(markerOptions).showInfoWindow();

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
            }
        });
    }
}