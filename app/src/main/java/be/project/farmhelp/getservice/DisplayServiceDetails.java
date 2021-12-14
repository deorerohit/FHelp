package be.project.farmhelp.getservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import be.project.farmhelp.R;

public class DisplayServiceDetails extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_service_details);

        textView = findViewById(R.id.dispser_text);
        Intent intent = getIntent();
        textView.setText(intent.getStringExtra("service")+" "+intent.getStringExtra("desc"));

    }
}