package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriendList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FirebaseAuth mauth;
    private String CurrentUserID;
    private DatabaseReference MuserDatabase, FriendDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend_list);

        mauth = FirebaseAuth.getInstance();
        CurrentUserID  = mauth.getCurrentUser().getUid();

        toolbar= findViewById(R.id.AllfriendsToolarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        FriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(CurrentUserID);
        FriendDatabase.keepSynced(true);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.keepSynced(true);

        recyclerView = findViewById(R.id.FriendsListID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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

    public void DisplayOnlineStatas(String stats){

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


        MuserDatabase.child(CurrentUserID).child("user_statas").updateChildren(statasmap);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {

        DisplayOnlineStatas("online");

        FirebaseRecyclerAdapter<FindFriend_Model, MYFriendHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriend_Model, MYFriendHolder>(
                FindFriend_Model.class,
                R.layout.sample_findfriend_layout,
                MYFriendHolder.class,
                FriendDatabase
        ) {
            @Override
            protected void populateViewHolder(final MYFriendHolder myFriendHolder, FindFriend_Model findFriend_model, int i) {

              final   String UID = getRef(i).getKey();

                MuserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){


                            if(dataSnapshot.hasChild("name")){
                                String nameget = dataSnapshot.child("name").getValue().toString();
                                myFriendHolder.setname(nameget);
                            }
                            if(dataSnapshot.hasChild("image_uri")){
                                String profileimageget = dataSnapshot.child("image_uri").getValue().toString();
                                myFriendHolder.setimage(profileimageget);
                            }
                            if(dataSnapshot.hasChild("country")){
                                String email = dataSnapshot.child("country").getValue().toString();
                                myFriendHolder.setemail(email);
                            }

                            if(dataSnapshot.hasChild("user_statas")){
                                String statas = dataSnapshot.child("user_statas").child("statas").getValue().toString();

                                if(statas.equals("online")){
                                    myFriendHolder.onlinestatss.setVisibility(View.VISIBLE);
                                    myFriendHolder.onlinestatss.setImageResource(R.drawable.active_dot);
                                }
                                else if(statas.equals("offline")){
                                    myFriendHolder.onlinestatss.setVisibility(View.VISIBLE);
                                    myFriendHolder.onlinestatss.setImageResource(R.drawable.inactive_dot);
                                }
                            }



                            myFriendHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                    intent.putExtra("key", UID);
                                    startActivity(intent);                                }
                            });
                        }
                        else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }



        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }




    public static class MYFriendHolder extends RecyclerView.ViewHolder{

        private CircleImageView mimage;
        private TextView mname, memail;
        private View Mview;
        private Context context;
        private ImageView onlinestatss;

        public MYFriendHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            mimage = Mview.findViewById(R.id.SprfileImageID);
            mname = Mview.findViewById(R.id.SnameID);
            memail = Mview.findViewById(R.id.SdetailsID);
            context = Mview.getContext();
            onlinestatss = Mview.findViewById(R.id.ActiveDotID);
        }

        public void setimage(String img){
            Picasso.with(context).load(img).placeholder(R.drawable.default_profileimage).into(mimage);

            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(mimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        public void setname(String nam){
            mname.setText(nam);
        }
        public void setemail(String em){
            memail.setText(em);
        }
    }

}
