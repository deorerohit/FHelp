package be.project.farmhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import be.project.farmhelp.Authentication.SessionManager;
import be.project.farmhelp.Authentication.SignIn;

public class ActivityAfterLogIn extends AppCompatActivity {

    SessionManager currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_log_in);
    }

    public void logOut(View view) {
        currentUser = new SessionManager(getApplicationContext());
        currentUser.logOutUserFromSession();
        startActivity(new Intent(ActivityAfterLogIn.this, SignIn.class));
        finish();
    }
}