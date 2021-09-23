package be.project.farmhelp.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import be.project.farmhelp.R;

public class OtpPage extends AppCompatActivity {

    TextView noToDisplay;
    PinView pinView;
    String codeInFirebase;
    private String name, mobNo, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);

        noToDisplay = findViewById(R.id.otp_noToDisplay);
        pinView = findViewById(R.id.otp_pinView);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mobNo = intent.getStringExtra("monNo");
        mobNo = "+91 " + mobNo;
        password = intent.getStringExtra("password");

        noToDisplay.setText(mobNo);
        sendVerificationCodeToUser(mobNo);
    }

    private void sendVerificationCodeToUser(String number) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    codeInFirebase = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinView.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(OtpPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            };


    public void verifyOtp(View view) {
        String code = pinView.getText().toString().trim();
        if (code.isEmpty()) {
            Toast.makeText(OtpPage.this, "Enter OTP", Toast.LENGTH_LONG).show();
        } else {
            verifyCode(code);
        }
    }

    private void verifyCode(String code) {
        try {
            PhoneAuthCredential crediantials = PhoneAuthProvider.getCredential(codeInFirebase, code);
            signInWithPhoneAuthCrediantials(crediantials);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void signInWithPhoneAuthCrediantials(PhoneAuthCredential crediantials) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(crediantials).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(OtpPage.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                    writeDataToFirebase();
                    startActivity(new Intent(OtpPage.this, SignIn.class));
                    finish();
                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(OtpPage.this, "Verification not completed!! Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeDataToFirebase() {

        WriteThisClassToFirebase userDataClass = new WriteThisClassToFirebase(name, mobNo, password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.child(mobNo).setValue(userDataClass);
    }

}