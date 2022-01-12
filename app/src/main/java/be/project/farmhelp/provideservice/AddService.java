package be.project.farmhelp.provideservice;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.NavUtils;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

import be.project.farmhelp.R;
import be.project.farmhelp.authentication.SessionManager;

public class AddService extends AppCompatActivity {

    SessionManager currentUser;

    AppCompatSpinner appCompatSpinner;
    TextInputEditText addServiceRate;
    TextInputEditText addServiceContact;
    TextInputEditText addServiceDescription;
    private String mobNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        currentUser = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = currentUser.getUsersDetailsFromSession();
        mobNumber = userDetails.get(SessionManager.KEY_MOBILE);


        appCompatSpinner = findViewById(R.id.add_serivce_spinner);
        addServiceRate = findViewById(R.id.add_serivce_rate);
        addServiceContact = findViewById(R.id.add_serivce_contact);
        addServiceDescription = findViewById(R.id.add_serivce_desc);
        addServiceContact.setText(mobNumber);

        boolean isServiceProvider = getIntent().getBooleanExtra("IS_SERVICE_PROVIDER", false);

        if (isServiceProvider) {
            String[] services = getApplicationContext().getResources().getStringArray(R.array.services_arrays);
            fillDataInViews(services);
        }
    }

    private void fillDataInViews(String[] allServices) {
        final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobNo").equalTo(mobNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String service = snapshot.child(mobNumber).child("addServiceClassToFirebase/service").getValue(String.class);
                    String rate = snapshot.child(mobNumber).child("addServiceClassToFirebase/rate").getValue(String.class);
                    String contact = snapshot.child(mobNumber).child("addServiceClassToFirebase/contact").getValue(String.class);
                    String description = snapshot.child(mobNumber).child("addServiceClassToFirebase/description").getValue(String.class);

                    appCompatSpinner.setSelection(Arrays.asList(allServices).indexOf(service));
                    addServiceRate.setText(rate);
                    addServiceContact.setText(contact);
                    addServiceDescription.setText(description);
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

    public void removeService(View view) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");

        dbReference.child(mobNumber).child("serviceProvider").setValue(false);

        dbReference.child(mobNumber).child("addServiceClassToFirebase/contact").setValue("");
        dbReference.child(mobNumber).child("addServiceClassToFirebase/description").setValue("");
        dbReference.child(mobNumber).child("addServiceClassToFirebase/rate").setValue("");
        dbReference.child(mobNumber).child("addServiceClassToFirebase/service").setValue("User");

        NavUtils.navigateUpFromSameTask(AddService.this);
        Toast.makeText(AddService.this, "Service removed", Toast.LENGTH_LONG).show();
    }

    public void addUpdateService(View view) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");

        dbReference.child(mobNumber).child("serviceProvider").setValue(true);

        dbReference.child(mobNumber).child("addServiceClassToFirebase/contact").setValue(addServiceContact.getText().toString());
        dbReference.child(mobNumber).child("addServiceClassToFirebase/description").setValue(addServiceDescription.getText().toString());
        dbReference.child(mobNumber).child("addServiceClassToFirebase/rate").setValue(addServiceRate.getText().toString());
        dbReference.child(mobNumber).child("addServiceClassToFirebase/service").setValue(appCompatSpinner.getSelectedItem().toString());

        NavUtils.navigateUpFromSameTask(AddService.this);
        Toast.makeText(AddService.this, "This service is added to your profile", Toast.LENGTH_LONG).show();

    }
}