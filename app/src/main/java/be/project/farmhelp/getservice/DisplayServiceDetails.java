package be.project.farmhelp.getservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import be.project.farmhelp.R;
import be.project.farmhelp.authentication.SessionManager;
import be.project.farmhelp.dataholders.ServiceRequests;

public class DisplayServiceDetails extends AppCompatActivity {

    TextView textViewName;
    TextInputEditText editTextService;
    TextInputEditText editTextRate;
    TextInputEditText editTextContact;
    TextInputEditText editTextDescription;

    SessionManager currentUser;

    String receiversMobNum;
    ServiceRequests serviceRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_service_details);

        serviceRequests = getCurrentUserDetails();

        textViewName = findViewById(R.id.disp_ser_det_name);
        editTextService = findViewById(R.id.disp_ser_det_service);
        editTextRate = findViewById(R.id.disp_ser_det_rate);
        editTextContact = findViewById(R.id.disp_ser_det_contact);
        editTextDescription = findViewById(R.id.disp_ser_det_desc);

        Intent intent = getIntent();
        textViewName.setText(intent.getStringExtra("name"));
        editTextService.setText(intent.getStringExtra("service"));
        editTextRate.setText(intent.getStringExtra("rate"));
        receiversMobNum = intent.getStringExtra("contact");
        editTextContact.setText(receiversMobNum);
        editTextDescription.setText(intent.getStringExtra("desc"));
    }

    public ServiceRequests getCurrentUserDetails() {
        ServiceRequests serviceRequest = new ServiceRequests();
        currentUser = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();

        serviceRequest.setNumber(userDetails.get(SessionManager.KEY_MOBILE));
        serviceRequest.setName(userDetails.get(SessionManager.KEY_NAME));

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobNo").equalTo(serviceRequest.getNumber());

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceRequest.setLatitude(snapshot.child(serviceRequest.getNumber()).child("latitude").getValue(Double.class));
                serviceRequest.setLongitude(snapshot.child(serviceRequest.getNumber()).child("longitude").getValue(Double.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return serviceRequest;
    }

    public void sendRequestToServiceProv(View view) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
        dbReference.child(receiversMobNum).child("receivedRequests/" + serviceRequests.getNumber()).setValue(serviceRequests); //receivedRequests
        dbReference.child(serviceRequests.getNumber()).child("sendRequests/").child(receiversMobNum).setValue(receiversMobNum); //sendRequests
        Toast.makeText(DisplayServiceDetails.this, "Request Send Successfully!", Toast.LENGTH_LONG).show();
    }
}