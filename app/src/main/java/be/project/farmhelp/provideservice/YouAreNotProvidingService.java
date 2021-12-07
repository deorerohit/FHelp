package be.project.farmhelp.provideservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import be.project.farmhelp.R;
import be.project.farmhelp.authentication.SignIn;
import be.project.farmhelp.authentication.SignUp;

public class YouAreNotProvidingService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_are_not_providing_service);
    }


    public void addService(View view){
        startActivity(new Intent(YouAreNotProvidingService.this, AddService.class));
    }
}