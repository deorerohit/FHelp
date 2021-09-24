package be.project.farmhelp.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import be.project.farmhelp.R;

public class SignUp extends AppCompatActivity {

    TextInputEditText name, mobNo, pass, confirmPass;
    boolean userExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.sign_up_name);
        mobNo = findViewById(R.id.sign_up_mob);
        pass = findViewById(R.id.sign_up_pass);
        confirmPass = findViewById(R.id.sign_up_confirm_pass);
    }

    public void doesUserExists(String number) {
        final Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobNo").equalTo(number);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mobNo.setText("");
                    mobNo.setError("Users Exists");
                    Toast.makeText(SignUp.this, "Another user exists with the same number.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SignUp.this, OtpPage.class);
                    intent.putExtra("name", name.getText().toString().trim());
                    intent.putExtra("monNo", mobNo.getText().toString().trim());
                    intent.putExtra("password", pass.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUp.this, "Sorry!! Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createAccount(View view) {
        String _name = name.getText().toString().trim();
        String _mobNo = mobNo.getText().toString().trim();
        String _pass = pass.getText().toString().trim();
        String _confirmPass = confirmPass.getText().toString().trim();


        if (TextUtils.isEmpty(_name)) {
            name.setError("Cannot be Empty");
            return;
        }
        if (TextUtils.isEmpty(_mobNo)) {
            mobNo.setError("Cannot be Empty");
            return;
        }

        if (!android.util.Patterns.PHONE.matcher(_mobNo).matches()) {
            mobNo.setError("Not Valid");
            return;
        }

        if (TextUtils.isEmpty(_pass)) {
            pass.setError("Cannot be Empty");
            return;
        }
        if (TextUtils.isEmpty(_confirmPass)) {
            confirmPass.setError("Cannot be Empty");
            return;
        }

        if (!_pass.equals(_confirmPass)) {
            pass.setError("Password not matching");
            confirmPass.setError("Password not matching");
            return;
        }

        doesUserExists("+91 " + _mobNo);


    }
}