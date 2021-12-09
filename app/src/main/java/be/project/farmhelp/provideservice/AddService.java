package be.project.farmhelp.provideservice;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import be.project.farmhelp.R;
import be.project.farmhelp.authentication.SessionManager;

public class AddService extends AppCompatActivity {

    SessionManager currentUser;

    AppCompatSpinner appCompatSpinner;
    TextInputEditText addServiceRate;
    TextInputEditText addServiceContact;
    TextInputEditText addServiceDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        currentUser = new SessionManager(getApplicationContext());
        appCompatSpinner = findViewById(R.id.add_serivce_spinner);
        addServiceRate = findViewById(R.id.add_serivce_rate);
        addServiceContact = findViewById(R.id.add_serivce_contact);
        addServiceDescription = findViewById(R.id.add_serivce_desc);

    }

    public void addService(View view) {
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        String mobNumber = userDetails.get(SessionManager.KEY_MOBILE);

        DatabaseReference checkUser = FirebaseDatabase.getInstance().getReference("Users");

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snapshot.getRef().child(mobNumber).child("serviceProvider").setValue(true);

                    snapshot.getRef().child(mobNumber).child("addServiceClassToFirebase/contact").setValue(addServiceContact.getText().toString());
                    snapshot.getRef().child(mobNumber).child("addServiceClassToFirebase/description").setValue(addServiceDescription.getText().toString());
                    snapshot.getRef().child(mobNumber).child("addServiceClassToFirebase/rate").setValue(addServiceRate.getText().toString());
                    snapshot.getRef().child(mobNumber).child("addServiceClassToFirebase/service").setValue(appCompatSpinner.getSelectedItem().toString());

                } else {
                    Toast.makeText(AddService.this, "No user exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddService.this, "Sorry!! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}