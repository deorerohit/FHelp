package be.project.farmhelp.getservice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import be.project.farmhelp.R;

public class DisplayServiceDetails extends AppCompatActivity {

    TextView textViewName;
    TextInputEditText editTextService;
    TextInputEditText editTextRate;
    TextInputEditText editTextContact;
    TextInputEditText editTextDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_service_details);

        textViewName = findViewById(R.id.disp_ser_det_name);
        editTextService = findViewById(R.id.disp_ser_det_service);
        editTextRate = findViewById(R.id.disp_ser_det_rate);
        editTextContact = findViewById(R.id.disp_ser_det_contact);
        editTextDescription = findViewById(R.id.disp_ser_det_desc);

        Intent intent = getIntent();
        textViewName.setText(intent.getStringExtra("name"));
        editTextService.setText(intent.getStringExtra("service"));
        editTextRate.setText(intent.getStringExtra("rate"));
        editTextContact.setText(intent.getStringExtra("contact"));
        editTextDescription.setText(intent.getStringExtra("desc"));


    }
}