package be.project.farmhelp.Authentication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import be.project.farmhelp.ActivityAfterLogIn;
import be.project.farmhelp.R;

public class SignIn extends AppCompatActivity {

    TextInputEditText mobNo, password;
    SessionManager currentUser;
    private boolean isNktConnected = false;
    String enteredNumber, enteredPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mobNo = findViewById(R.id.sing_in_mob_no);
        password = findViewById(R.id.sign_in_pass);

        currentUser = new SessionManager(SignIn.this);

        if (currentUser.isLoggedIn()) {
            startActivity(new Intent(SignIn.this, ActivityAfterLogIn.class));
            finish();
        }

        if (!isNktConnected()) {
            showDialogForNotConnected();
        }
    }

    public void loginButton(View view) {
        enteredNumber = mobNo.getText().toString().trim();
        enteredPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(enteredNumber)) {
            mobNo.setError("Cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(enteredPassword)) {
            password.setError("Cannot be empty");
            return;
        }

        enteredNumber = "+91 " + enteredNumber;
        final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobNo").equalTo(enteredNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mobNo.setError(null);
                    String savedPassword = snapshot.child(enteredNumber).child("password").getValue(String.class);
                    if (savedPassword.equals(enteredPassword)) {
                        password.setError(null);
                        Toast.makeText(SignIn.this, "Successfull", Toast.LENGTH_SHORT).show();
                        String _name = snapshot.child(enteredNumber).child("name").getValue(String.class);
                        String _number = snapshot.child(enteredNumber).child("mobNo").getValue(String.class);
                        String _password = snapshot.child(enteredNumber).child("password").getValue(String.class);


                        currentUser.createLoginSession(_name, _number, _password);

                        startActivity(new Intent(SignIn.this, ActivityAfterLogIn.class));
                        finish();
                    } else {
                        Toast.makeText(SignIn.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignIn.this, "No user exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignIn.this, "Sorry!! Try again", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showDialogForNotConnected() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
        builder.setMessage("Connect to internet!!").setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isNktConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi != null && wifi.isConnected()) || (mobileData != null && mobileData.isConnected()))
            return true;
        else
            return false;

    }

    public void callSingUp(View view) {
        startActivity(new Intent(SignIn.this, SignUp.class));
    }
}