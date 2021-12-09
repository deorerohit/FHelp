package be.project.farmhelp.provideservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import be.project.farmhelp.R;

public class YouAreNotProvidingService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_are_not_providing_service);
    }


    public void openAddService(View view) {
        startActivity(new Intent(YouAreNotProvidingService.this, AddService.class));
        finish();
    }
}