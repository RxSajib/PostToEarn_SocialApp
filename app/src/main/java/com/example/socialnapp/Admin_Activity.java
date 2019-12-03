package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Activity extends AppCompatActivity {

    private Toolbar admintoolbar;
    private EditText adminemail, adminpassword;
    private Button adminlogin;
    private DatabaseReference MAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_);

        admintoolbar = findViewById(R.id.AdminToolbarID);
        setSupportActionBar(admintoolbar);
        getSupportActionBar().setTitle("Admin Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        adminemail= findViewById(R.id.AdminEmialID);
        adminpassword = findViewById(R.id.AdminPasswordID);
        adminlogin = findViewById(R.id.AdminLoginID);

        MAdmin = FirebaseDatabase.getInstance().getReference().child("Admin");

        adminlogin.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    adminlogin.setBackgroundResource(R.color.colorPrimary);
                    adminlogin.setTextColor(R.color.clickup);

                    startfatching_data();
                }
                else {
                    adminlogin.setBackgroundResource(R.drawable.adminedittet_design);
                }

                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startfatching_data(){

        final String emailtext = adminemail.getText().toString();
        final String passwordtext = adminpassword.getText().toString();

        MAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String emailaddress = dataSnapshot.child("email").getValue().toString();
                    String password = dataSnapshot.child("password").getValue().toString();

                    if(emailtext.isEmpty() && passwordtext.isEmpty()){
                        Toast.makeText(getApplicationContext(), "enter your email and password", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if(emailtext.equals(emailaddress) || passwordtext.equals(password)){
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Admin_MoneyList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            adminemail.setText("");
                            adminpassword.setText("");
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "error login", Toast.LENGTH_LONG).show();
                        }
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
