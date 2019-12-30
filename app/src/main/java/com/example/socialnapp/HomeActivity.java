package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnapp.Common.Common;
import com.example.socialnapp.Model.LastTime;
import com.example.socialnapp.Model.Token;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeActivity extends AppCompatActivity {

    private Toolbar hometoolbar;
    private NavigationView homenav;
    private DrawerLayout drawerLayout;
    private ViewPager Mviewpager;
    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private FirebaseAuth Mauth;
    private DatabaseReference MHomedatabase;
    private FirebaseAuth NavMauth;
    private String CurrentuserID;
    private CircleImageView navimage;
    private TextView navName, navEmail;
    private DatabaseReference MuserDatabase;
    private int counter = 0;

    private String TAG = "Push Notification";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        fcm_token();


        hometoolbar = findViewById(R.id.HomeToobarID);
        Mauth = FirebaseAuth.getInstance();
        setSupportActionBar(hometoolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);

        drawerLayout = findViewById(R.id.DrawerlayoutID);
        homenav = findViewById(R.id.HomeNavID);
        Mviewpager = findViewById(R.id.ViewPagerID);
        tabLayout = findViewById(R.id.TabLayoutID);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        Mviewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(Mviewpager);

        NavMauth = FirebaseAuth.getInstance();
        CurrentuserID = NavMauth.getCurrentUser().getUid();
        MHomedatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentuserID);

        View navview = homenav.inflateHeaderView(R.layout.header_layout);
        navimage = navview.findViewById(R.id.nprofileImageID);
        navName = navview.findViewById(R.id.nnameID);
        navEmail = navview.findViewById(R.id.nemailID);

        MHomedatabase.child(CurrentuserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("image_uri")) {
                        String image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                        Picasso.with(HomeActivity.this).load(image_uriget).resize(100, 100).placeholder(R.drawable.default_profileimage).into(navimage);
                        Picasso.with(HomeActivity.this).load(image_uriget).placeholder(R.drawable.default_profileimage).into(navimage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                    if (dataSnapshot.hasChild("username")) {
                        String nameget = dataSnapshot.child("username").getValue().toString();
                        navName.setText(nameget);
                    }
                    if (dataSnapshot.hasChild("email_address")) {
                        String email_addressget = dataSnapshot.child("email_address").getValue().toString();
                        navEmail.setText(email_addressget);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        homenav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                if (menuItem.getItemId() == R.id.ProfileID) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);

                }


                if (menuItem.getItemId() == R.id.FindFriendsID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    Intent intent = new Intent(getApplicationContext(), FindFriends_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                if (menuItem.getItemId() == R.id.LogoutID) {
                    DisplayOnlineStatas("offline");
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Mauth.signOut();
                    Intent gotologin = new Intent(HomeActivity.this, LoginActivity.class);
                    gotologin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotologin);
                    finish();
                }

                if (menuItem.getItemId() == R.id.EditProfileID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent gotoedit = new Intent(HomeActivity.this, ProfileEdit_Activity.class);
                    gotoedit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gotoedit);

                }

                if (menuItem.getItemId() == R.id.EarnityID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    ;
                    Intent intent = new Intent(HomeActivity.this, Entry_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.ShareID) {
                    counter++;
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    String shareMessage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";

                    String sharebody = shareMessage;
                    String sharesubject = "Hi, let's start best friendship on Baby Chat - It allows FREE,chat FREE earning ." + "\n\n" + sharebody + "\n" + "share people: " + counter;
                    intent.putExtra(Intent.EXTRA_TEXT, sharesubject);
                    //  intent.putExtra(Intent.EXTRA_SUBJECT, sharebody);
                    startActivity(Intent.createChooser(intent, "share with"));
                }

                if (menuItem.getItemId() == R.id.RequestID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    Intent intent = new Intent(HomeActivity.this, Request_Activtiy.class);
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.WithDowID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    menuItem.setChecked(true);
                    menuItem.setCheckable(true);

                    Intent intent = new Intent(getApplicationContext(), Withdraw_Activity.class);
                    startActivity(intent);
                }

                if (menuItem.getItemId() == R.id.AdminID) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    menuItem.setCheckable(true);
                    menuItem.setChecked(true);
                    Intent intent = new Intent(getApplicationContext(), Admin_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }


                return true;
            }
        });


        DisplayOnlineStatas("online");
    }





    @Override
    protected void onStop() {
        DisplayOnlineStatas("offline");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DisplayOnlineStatas("offline");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        DisplayOnlineStatas("online");
        super.onRestart();
    }


    public void DisplayOnlineStatas(String stats) {

        ///date
        Calendar calendardate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
        String CurrentDate = simpleDateFormat.format(calendardate.getTime());

        ///time
        Calendar calendartime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
        String CurrentTime = simpleDateFormat1.format(calendartime.getTime());

        Map statasmap = new HashMap();
        statasmap.put("time", CurrentTime);
        statasmap.put("date", CurrentDate);
        statasmap.put("statas", stats);


        MHomedatabase.child(CurrentuserID).child("user_statas").updateChildren(statasmap);
    }


    private void fcm_token() {


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        //  if (refreshedToken!="")


        Log.i(TAG, "fcm_token: " + refreshedToken);


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference referance = db.getReference("Token");
        Token token = new Token(refreshedToken, true);
        referance.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.postmenu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {


        DisplayOnlineStatas("online");
        FirebaseUser Muser = Mauth.getCurrentUser();
        if (Muser == null) {
            Intent gotologin = new Intent(HomeActivity.this, LoginActivity.class);
            gotologin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(gotologin);
            finish();
        } else {
            cheackuserexsists();
        }

        super.onStart();
    }

    private void cheackuserexsists() {

        final String UserID = Mauth.getCurrentUser().getUid();
        MuserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild("username")) {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getApplicationContext(), "Please setup your profile", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }

        if (item.getItemId() == R.id.PostID) {
            Intent intent = new Intent(HomeActivity.this, AddPost_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
