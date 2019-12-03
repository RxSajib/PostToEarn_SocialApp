package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DeletePostActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView name, descptrion, time;
    private Button deletebutton;
    private String clickID, postimageget, dateget, usernameget, post_descptrionget, userID;
    private DatabaseReference MpostDatabase;
    private TextView goback;
    private ImageView ediButton;
    private FirebaseAuth mauth;
    private String CurrntUserID;
    private DatabaseReference Mpost;
    private TextView Totalpost;
    private long postcount;
    private String uidget;
    private ImageView postactivity;
    private BottomSheetDialog btdiolog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_post);

        Totalpost = findViewById(R.id.TotalPostID);
        Mpost = FirebaseDatabase.getInstance().getReference().child("Post");
        clickID = getIntent().getStringExtra("key");

        postactivity = findViewById(R.id.postimage);
        postactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Intent intent = new Intent(getApplicationContext(), MyPostListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("key", clickID);
                startActivity(intent); */
            }
        });




        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mauth = FirebaseAuth.getInstance();
        CurrntUserID = mauth.getCurrentUser().getUid();


        MpostDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        imageView = findViewById(R.id.DImageID);
        name = findViewById(R.id.nameID);
        descptrion = findViewById(R.id.DescptrionID);
        time = findViewById(R.id.TimeID);
        deletebutton = findViewById(R.id.ButtonDeleteID);
        goback = findViewById(R.id.GobackButtinID);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        deletebutton.setVisibility(View.INVISIBLE);

        startdeleting();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //


                androidx.appcompat.app.AlertDialog.Builder Mbuilder = new androidx.appcompat.app.AlertDialog.Builder(DeletePostActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                View Mview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.imagefull_screen, null, false);
                ImageView imageView = Mview.findViewById(R.id.sampleimageID);

                Picasso.with(getApplicationContext()).load(postimageget).into(imageView);


                AlertDialog alertDialog = Mbuilder.create();
                alertDialog.setView(Mview);
                alertDialog.show();

            }
        });

    }

    private void startdeleting(){

      deletebutton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              MpostDatabase.child(clickID).removeValue()
                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {

                              if(task.isSuccessful()){
                                  Toast.makeText(DeletePostActivity.this, "Data is deleted success", Toast.LENGTH_LONG).show();
                                  Intent intent = new Intent(DeletePostActivity.this, HomeActivity.class);
                                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                  startActivity(intent);
                              }
                              else {
                                  String erormessage =task.getException().getMessage().toString();
                                  Toast.makeText(DeletePostActivity.this, erormessage, Toast.LENGTH_LONG).show();
                              }
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {

                      String errormessage = e.getMessage().toString();
                      Toast.makeText(DeletePostActivity.this, errormessage, Toast.LENGTH_LONG).show();
                  }
              });
          }
      });
    }


    @Override
    protected void onStart() {


        MpostDatabase.child(clickID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("postimage")){
                        postimageget = dataSnapshot.child("postimage").getValue().toString();
                        Picasso.with(getApplicationContext()).load(postimageget).placeholder(R.drawable.default_galleryicon).into(imageView);
                    }
                    if(dataSnapshot.hasChild("date")){
                         dateget = dataSnapshot.child("date").getValue().toString();
                        time.setText(dateget);
                    }
                    if(dataSnapshot.hasChild("username")){
                         usernameget = dataSnapshot.child("username").getValue().toString();
                        name.setText(usernameget);
                    }
                    if(dataSnapshot.hasChild("post_descptrion")){
                         post_descptrionget = dataSnapshot.child("post_descptrion").getValue().toString();
                        descptrion.setText(post_descptrionget);
                    }
                    if(dataSnapshot.hasChild("uid")){
                         uidget = dataSnapshot.child("uid").getValue().toString();

                         startreading_post(uidget);



                        if(uidget.equals(CurrntUserID)){
                            deletebutton.setVisibility(View.VISIBLE);
                    //        ediButton.setVisibility(View.VISIBLE);
                        }



                    }
                }
            }

            private void startreading_post(final String id) {

                Mpost.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot data : dataSnapshot.getChildren()){

                            if(data.hasChild("uid")){

                                String uid = data.child("uid").getValue().toString();

                                if(uid.equals(id)){
                                    postcount++;
                                    Totalpost.setText(Long.toString(postcount));
                                }


                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onStart();
    }
}

