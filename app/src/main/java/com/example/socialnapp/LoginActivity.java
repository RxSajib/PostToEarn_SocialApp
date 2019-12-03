package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginbutton;
    private ProgressBar Mprogress;
    private TextView progresstext, dangertext, haveaccount;
    private FirebaseAuth Mauth;
    private ImageView dangerimage;
    private TextView restpassworbuttonID;
    private Button phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Mauth = FirebaseAuth.getInstance();
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        restpassworbuttonID = findViewById(R.id.LoginFPassword);
        restpassworbuttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        phone = findViewById(R.id.PhoneSiginIDButtonID);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneLogin_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        haveaccount = findViewById(R.id.haveAccountI);
        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        email = findViewById(R.id.LoginEmailID);
        password = findViewById(R.id.LoginPasswordID);
        loginbutton = findViewById(R.id.LoginButtonID);
        Mprogress = findViewById(R.id.progressBarlogin);
        progresstext = findViewById(R.id.logintextID);
        Mprogress.setVisibility(View.INVISIBLE);
        progresstext.setVisibility(View.INVISIBLE);

        dangerimage = findViewById(R.id.DangerIconID);
        dangertext = findViewById(R.id.DangerTextID);


        dangerimage.setVisibility(View.INVISIBLE);
        dangertext.setVisibility(View.INVISIBLE);

        Mprogress.setVisibility(View.INVISIBLE);
        progresstext.setVisibility(View.INVISIBLE);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailtext = email.getText().toString();
                String passwordtext = password.getText().toString();

                if(emailtext.isEmpty()){
                    email.setError("Email is empty");
                }
                if(passwordtext.isEmpty()){
                    password.setError("Password is empty");
                }
                else {
                    progresstext.setVisibility(View.VISIBLE);
                    Mprogress.setVisibility(View.VISIBLE);
                  Mauth.signInWithEmailAndPassword(emailtext, passwordtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful()){

                              Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_LONG).show();
                              email.setText("");
                              password.setText("");
                              Mprogress.setVisibility(View.INVISIBLE);
                              progresstext.setVisibility(View.INVISIBLE);
                              Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                              startActivity(intent);
                              finish();
                          }
                          else {
                              Mprogress.setVisibility(View.INVISIBLE);
                              progresstext.setVisibility(View.INVISIBLE);
                              String errormessage = task.getException().getMessage().toString();
                              dangerimage.setVisibility(View.VISIBLE);
                              dangertext.setVisibility(View.VISIBLE);
                              dangertext.setText(errormessage);
                          }
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Mprogress.setVisibility(View.INVISIBLE);
                          progresstext.setVisibility(View.INVISIBLE);
                          String errormessage = e.getMessage().toString();
                          dangerimage.setVisibility(View.VISIBLE);
                          dangertext.setVisibility(View.VISIBLE);
                          dangertext.setText(errormessage);
                      }
                  })  ;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser Muser =Mauth.getCurrentUser();
        if(Muser != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
