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

public class RegistationActivity extends AppCompatActivity {

    private EditText email, password, cpassword;
    private Button registerbutton;
    private ImageView desgerimage;
    private TextView dengertext, alredyhaveacc;
    private ProgressBar Mprogress;
    private TextView progresstext;
    private FirebaseAuth Mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);

        Mauth = FirebaseAuth.getInstance();
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        alredyhaveacc = findViewById(R.id.AlreaddyHvaeAccSignUp);
        alredyhaveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistationActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        email = findViewById(R.id.REmailID);
        password = findViewById(R.id.RPasswordID);
        cpassword = findViewById(R.id.RCPasswordID);
        registerbutton = findViewById(R.id.SingUpButtonID);
        Mauth = FirebaseAuth.getInstance();

        Mprogress = findViewById(R.id.progressBarReg);
        progresstext = findViewById(R.id.RegintextID);
        desgerimage = findViewById(R.id.DangerRIconID);
        dengertext = findViewById(R.id.DangerRTextID);

        Mprogress.setVisibility(View.INVISIBLE);
        progresstext.setVisibility(View.INVISIBLE);

        dengertext.setVisibility(View.INVISIBLE);
        desgerimage.setVisibility(View.INVISIBLE);



        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailtex = email.getText().toString();
                String passwordtext = password.getText().toString();
                String cpasswordtex = cpassword.getText().toString();

                if(emailtex.isEmpty()){
                    email.setError("Email empty");
                }
                if(passwordtext.isEmpty()){
                    password.setError("Password empty");
                }
                if(cpasswordtex.isEmpty()){
                    cpassword.setError("Password empty");
                }
                if(!passwordtext.equals(cpasswordtex)){
                    password.setError("Password didn't match");
                    cpassword.setError("Password didn't match");
                }
                if(passwordtext.length() <=7){
                    password.setError("Password need atlist 8 char");
                }
                else {
                    Mprogress.setVisibility(View.VISIBLE);
                    progresstext.setVisibility(View.VISIBLE);

                    Mauth.createUserWithEmailAndPassword(emailtex, passwordtext)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){
                                        dengertext.setVisibility(View.INVISIBLE);
                                        desgerimage.setVisibility(View.INVISIBLE);
                                        Mprogress.setVisibility(View.INVISIBLE);
                                        progresstext.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "Account is created", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegistationActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    else {
                                        Mprogress.setVisibility(View.INVISIBLE);
                                        progresstext.setVisibility(View.INVISIBLE);
                                        dengertext.setVisibility(View.VISIBLE);
                                        desgerimage.setVisibility(View.VISIBLE);
                                        String error = task.getException().getMessage().toString();
                                        dengertext.setText(error);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Mprogress.setVisibility(View.INVISIBLE);
                            progresstext.setVisibility(View.INVISIBLE);
                            dengertext.setVisibility(View.VISIBLE);
                            desgerimage.setVisibility(View.VISIBLE);
                            String error = e.getMessage().toString();
                            dengertext.setText(error);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser Muser = Mauth.getCurrentUser();
        if(Muser != null){
            Intent intent = new Intent(RegistationActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
