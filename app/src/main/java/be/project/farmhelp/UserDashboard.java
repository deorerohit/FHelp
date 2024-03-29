package be.project.farmhelp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import be.project.farmhelp.authentication.SessionManager;
import be.project.farmhelp.authentication.SignIn;
import be.project.farmhelp.provideservice.AddService;
import be.project.farmhelp.provideservice.YouAreNotProvidingService;
import be.project.farmhelp.youroffers.DisplayReceivedRequest;
import be.project.farmhelp.yoursendrequest.DisplaySendRequest;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager currentUser;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView menuButton;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient client;
    String mobNumber;
    TextView headerNameTV, headerServiceTV;
    public static final String NUMBER_INTENT_KEY = "numberIntentKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);


        headerNameTV = findViewById(R.id.header_name);
        headerServiceTV = findViewById(R.id.header_service);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menu_button);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.small_map);
        client = LocationServices.getFusedLocationProviderClient(UserDashboard.this);

        currentUser = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        mobNumber = userDetails.get(SessionManager.KEY_MOBILE);

        headerNameTV.setText(userDetails.get(SessionManager.KEY_NAME));

        fillDataInHeader(userDetails);
        loadLocationOnMap();
        navigationDrawer();
    }

    public void fillDataInHeader(HashMap<String, String> userDetails) {
        final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobNo").equalTo(mobNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String service = snapshot.child(mobNumber).child("addServiceClassToFirebase/service").getValue(String.class);
                headerServiceTV.setText(service + " | " + userDetails.get(SessionManager.KEY_MOBILE));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDashboard.this, "Sorry!! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadLocationOnMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your Location");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                            googleMap.addMarker(markerOptions).showInfoWindow();
                        }
                    });
                }
            }
        });
    }

    public void updateLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");

                    dbReference.child(mobNumber).child("latitude").setValue(location.getLatitude());
                    dbReference.child(mobNumber).child("longitude").setValue(location.getLongitude());
                    Toast.makeText(UserDashboard.this, "Location Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserDashboard.this, "Sorry!! unable to update", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sign_out:
                currentUser.logOutUserFromSession();
                startActivity(new Intent(UserDashboard.this, SignIn.class));
                finish();
                break;
        }
        return true;
    }

    public void callMapActivity(View view) {
        startActivity(new Intent(UserDashboard.this, GoogleMapActivity.class));
    }

    public void callAddServiceActivity(View view) {
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        String mobNumber = userDetails.get(SessionManager.KEY_MOBILE);

        final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobNo").equalTo(mobNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean isServiceProvider = snapshot.child(mobNumber).child("serviceProvider").getValue(Boolean.class);
                    if (isServiceProvider) {
                        Intent intent = new Intent(UserDashboard.this, AddService.class);
                        intent.putExtra("IS_SERVICE_PROVIDER", true);
                        startActivity(intent);

                    } else {
                        startActivity(new Intent(UserDashboard.this, YouAreNotProvidingService.class));
                    }
                } else {
                    Toast.makeText(UserDashboard.this, "No user exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDashboard.this, "Sorry!! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displayYourOffers(View view) {
        startActivity(new Intent(UserDashboard.this, DisplayReceivedRequest.class));
    }

    public void callDisplaySendRequest(View view) {
        startActivity(new Intent(UserDashboard.this, DisplaySendRequest.class));
    }

}