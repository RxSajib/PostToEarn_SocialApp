package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddpostUpdate_Activity extends AppCompatActivity {

    private Toolbar posttoolbar;
    private ImageView addpostimeg;
    private EditText adddescptron;
    private Button PostButton;
    private ProgressBar progressBar;
    private FirebaseAuth Mauth;
    private DatabaseReference MuserDatabase, Mpost;
    private String CurrentuserID;
    private StorageReference Mstore;
    private String Currenttime, Currentdate, postimageuri;
    private  String RandomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpost_update_);




        posttoolbar = findViewById(R.id.PostToolbarID);
        setSupportActionBar(posttoolbar);
        getSupportActionBar().setTitle("Add A New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        Mauth = FirebaseAuth.getInstance();
        CurrentuserID = Mauth.getCurrentUser().getUid();
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Mpost = FirebaseDatabase.getInstance().getReference().child("Post");

        addpostimeg = findViewById(R.id.PostImageID);
        adddescptron = findViewById(R.id.PostTitleID);
        PostButton  = findViewById(R.id.AddPostButtonID);
        progressBar = findViewById(R.id.MprogressiD);
        progressBar.setVisibility(View.INVISIBLE);
        Mstore = FirebaseStorage.getInstance().getReference();



        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String posttitle = adddescptron.getText().toString();
                if(posttitle.isEmpty()){
                    adddescptron.setError("Post Title is empty");
                }
                if(postimageuri == null){
                    Toast.makeText(AddpostUpdate_Activity.this, "Image must selected", Toast.LENGTH_LONG).show();
                }
                else {


                    progressBar.setVisibility(View.VISIBLE);

                    MuserDatabase.child(CurrentuserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                final String profileimage;
                                String username;

                                profileimage = dataSnapshot.child("image_uri").getValue().toString();

                                username = dataSnapshot.child("name").getValue().toString();


                                Map postmap = new HashMap();
                                postmap.put("profile_image", profileimage);
                                postmap.put("username", username);
                                postmap.put("time", Currenttime);
                                postmap.put("date", Currentdate);
                                postmap.put("uid", CurrentuserID);
                                postmap.put("postimage", postimageuri);
                                postmap.put("post_descptrion", posttitle);

                                Mpost.child(RandomKey).updateChildren(postmap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {

                                        if(task.isSuccessful()){

                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(AddpostUpdate_Activity.this, "post success", Toast.LENGTH_LONG).show();

                                        }
                                        else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            String error = task.getException().getMessage().toString();
                                            Toast.makeText(AddpostUpdate_Activity.this, error, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String errpr = e.getMessage().toString();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(AddpostUpdate_Activity.this, errpr, Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        addpostimeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 511);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 511 && resultCode == RESULT_OK){
            progressBar.setVisibility(View.VISIBLE);
            Uri image = data.getData();
            addpostimeg.setImageURI(image);


            Calendar calendartime =Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            Currenttime = simpleDateFormat.format(calendartime.getTime());

            Calendar calendardate = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Currentdate = simpleDateFormat1.format(calendardate.getTime());
            RandomKey = Currenttime+Currentdate;


            StorageReference filepath = Mstore.child("Add Post Image").child(RandomKey+".jpg").child(image.getLastPathSegment());
            filepath.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){
                        postimageuri = task.getResult().getDownloadUrl().toString();
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddpostUpdate_Activity.this, "image is posted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        String erorr = task.getException().getMessage().toString();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    String error = e.getMessage().toString();
                    Toast.makeText(AddpostUpdate_Activity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}




