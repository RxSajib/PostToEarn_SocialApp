package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Withdraw_Activity extends AppCompatActivity {

    private CircleImageView profileimage;
    private TextView follow, love;
    private DatabaseReference MuserDatabase, followme, Mearn;
    private String CurrentUserID;
    private FirebaseAuth Mauth;
    private long followcoun;
    private long lovecount;

    private Button withdrawnbutton;

    private ImageView danferimage;
    private TextView dangertext ;
    private static final int SendsmspermissionCode = 111;
    private DatabaseReference Mwithdrawdatabase, Musers;
    private String nameget, image_uriget;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_);

        Mwithdrawdatabase = FirebaseDatabase.getInstance().getReference().child("Withdraw");
        Musers = FirebaseDatabase.getInstance().getReference().child("Users");

        if(cheackpermission(Manifest.permission.SEND_SMS)){

        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SendsmspermissionCode);
        }




        danferimage = findViewById(R.id.DangerPicID);
        dangertext = findViewById(R.id.DangerTextCID);
        withdrawnbutton = findViewById(R.id.WindownButtonID);
        profileimage = findViewById(R.id.ProImageID);
        follow = findViewById(R.id.WFollowCountID);
        love = findViewById(R.id.WLikeCountID);
        followme = FirebaseDatabase.getInstance().getReference().child("Follow");
        followme.keepSynced(true);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.keepSynced(true);
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        Mearn = FirebaseDatabase.getInstance().getReference().child("All_Like");
        Mearn.keepSynced(true);


        MuserDatabase.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("image_uri")){
                        String image_uriegte = dataSnapshot.child("image_uri").getValue().toString();
                        Picasso.with(getApplicationContext()).load(image_uriegte).placeholder(R.drawable.default_profileimage).into(profileimage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








        followme.child(CurrentUserID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String datacount = data.getValue().toString();
                    //     Toast.makeText(getApplicationContext(), datacount, Toast.LENGTH_LONG).show();
                    if(datacount.equals("recived")){
                        followcoun++;
                        follow.setText(Long.toString(followcoun));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Mearn.child(CurrentUserID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String X = data.getValue().toString();
                    if(X.equals("recived")){
                        lovecount++;
                        love.setText(Long.toString(lovecount));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Musers.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){



                    if(dataSnapshot.hasChild("name")){
                         nameget = dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.hasChild("image_uri")){
                         image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        withdrawnbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {




                if(lovecount != 1000 && followcoun != 1000){
                    danferimage.setVisibility(View.VISIBLE);
                    dangertext.setVisibility(View.VISIBLE);
                    dangertext.setText("For withdrawal request \n" +
                            "Minimum hearts require =1000 \n" +
                            "Minimum followers require= 1000");



                }
                else if(lovecount == 1000 && followcoun == 1000){
                    Toast.makeText(getApplicationContext(), "you earn "+lovecount +"and"+followcoun, Toast.LENGTH_LONG).show();
                    withdrawnbutton.setEnabled(false);

                    Map withdrawmap = new HashMap();
                    withdrawmap.put("username", nameget);
                    withdrawmap.put("profileimage", image_uriget);
                    withdrawmap.put("earn", "harts: 1000  follow: 1000 ");
                    Mwithdrawdatabase.push().updateChildren(withdrawmap);


                }
                else if(lovecount == 2000 && followcoun == 2000){
                    withdrawnbutton.setEnabled(false);

                    Map withdrawmap = new HashMap();
                    withdrawmap.put("username", nameget);
                    withdrawmap.put("profileimage", image_uriget);
                    withdrawmap.put("earn", "harts: 2000  follow: 2000 ");
                    Mwithdrawdatabase.push().updateChildren(withdrawmap);
                }
                else if(lovecount == 3000 && followcoun == 3000){
                    withdrawnbutton.setEnabled(false);

                    Map withdrawmap = new HashMap();
                    withdrawmap.put("username", nameget);
                    withdrawmap.put("profileimage", image_uriget);
                    withdrawmap.put("earn", "harts: 3000  follow: 3000 ");
                    Mwithdrawdatabase.push().updateChildren(withdrawmap);
                }
                else if(lovecount == 4000 && followcoun == 4000){
                    withdrawnbutton.setEnabled(false);

                    Map withdrawmap = new HashMap();
                    withdrawmap.put("username", nameget);
                    withdrawmap.put("profileimage", image_uriget);
                    withdrawmap.put("earn", "harts: 4000  follow: 4000 ");
                    Mwithdrawdatabase.push().updateChildren(withdrawmap);
                }
                else if(lovecount == 5000 && followcoun == 5000){
                    withdrawnbutton.setEnabled(false);

                    Map withdrawmap = new HashMap();
                    withdrawmap.put("username", nameget);
                    withdrawmap.put("profileimage", image_uriget);
                    withdrawmap.put("earn", "harts: 5000  follow: 5000 ");
                    Mwithdrawdatabase.push().updateChildren(withdrawmap);
                }

            }
        });
    }

    private boolean cheackpermission(String permission){
        int cheackpermission = ContextCompat.checkSelfPermission(this, permission);
        return cheackpermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case SendsmspermissionCode:

                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    withdrawnbutton.setEnabled(true);
                }

                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
