package be.project.farmhelp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class signUp extends AppCompatActivity {

    TextInputEditText name, mobNo, pass, confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.sign_up_name);
        mobNo = findViewById(R.id.sign_up_mob);
        pass = findViewById(R.id.sign_up_pass);
        confirmPass = findViewById(R.id.sign_up_confirm_pass);
    }

    public void createAccount(View view) {
        String _name = name.getText().toString().trim();
        String _mobNo = mobNo.getText().toString().trim();
        String _pass = pass.getText().toString().trim();
        String _confirmPass = confirmPass.getText().toString().trim();

        if(TextUtils.isEmpty(_name)) {
            name.setError("Cannot be Empty");
            return;
        }
        if(TextUtils.isEmpty(_mobNo)){
            mobNo.setError("Cannot be Empty");
            return;
        }
        if(TextUtils.isEmpty(_pass)){
            pass.setError("Cannot be Empty");
            return;
        }
        if(TextUtils.isEmpty(_confirmPass)){
            confirmPass.setError("Cannot be Empty");
            return;
        }

        if(!_pass.equals(_confirmPass)){
            pass.setError("Password not matching");
            confirmPass.setError("Password not matching");
            return;
        }

        Intent intent = new Intent(signUp.this, otpPage.class);
        intent.putExtra("name",_name);
        intent.putExtra("monNo",_mobNo);
        intent.putExtra("password",_pass);
        startActivity(intent);
    }
}