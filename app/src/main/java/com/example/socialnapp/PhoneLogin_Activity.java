package com.example.socialnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class PhoneLogin_Activity extends AppCompatActivity {

    private Button loginbutton;
    private EditText code, number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login_);

        code = findViewById(R.id.CountryCodeTextIDID);
        number = findViewById(R.id.NumberID);


        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        loginbutton = findViewById(R.id.PhoneLoginID);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codetext = code.getText().toString();
                String numbertext = number.getText().toString();
                if(codetext.isEmpty()){
                    code.setError("Code Require");
                }
                else if(numbertext.isEmpty()){
                    number.setError("Number Require");
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), code_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("number", codetext+numbertext);
                    startActivity(intent);
                }


            }
        });
    }
}
