package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.admin.DelegatedAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class Request_Activtiy extends AppCompatActivity {

    private Toolbar requesttoolbar;
    private DatabaseReference MFriendsDatabase;
    private DatabaseReference MuserDatabase;
    private String CurrentuserID;
    private FirebaseAuth Mauth;
    private RecyclerView MFriendsview;
    private RecyclerView Mreqestlist;

    private ImageView nofriends;
    private TextView nofriendtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request__activtiy);

        nofriends = findViewById(R.id.NorequestImageID);
        nofriendtext = findViewById(R.id.NorequestTextID);

        requesttoolbar = findViewById(R.id.RequestToolbarID);
        setSupportActionBar(requesttoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("My Request");

        Mreqestlist = findViewById(R.id.FriendsRequestID);
        Mreqestlist.setHasFixedSize(true);
        Mreqestlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Mauth = FirebaseAuth.getInstance();
        CurrentuserID = Mauth.getCurrentUser().getUid();
        MFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
       MFriendsDatabase.keepSynced(true);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.keepSynced(true);
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

        FirebaseRecyclerAdapter<FindFriendModel, FriendsHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriendModel, FriendsHolder>(
                FindFriendModel.class,
                R.layout.myrequest_layout,
                FriendsHolder.class,
                MFriendsDatabase.child(CurrentuserID)
        ) {
            @Override
            protected void populateViewHolder(final FriendsHolder friendsHolder, FindFriendModel findFriendModel, int i) {

                final String UID = getRef(i).getKey();
                DatabaseReference gettype = getRef(i).child("request_type").getRef();
                gettype.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            friendsHolder.Rview.setVisibility(View.VISIBLE);

                            String type = dataSnapshot.getValue().toString();

                            if(type.equals("recived")){
                                MuserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            nofriends.setVisibility(View.INVISIBLE);
                                            nofriendtext.setVisibility(View.INVISIBLE);

                                            friendsHolder.Rview.setVisibility(View.VISIBLE);
                                            if(dataSnapshot.hasChild("image_uri")){
                                                String image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                                                friendsHolder.setimage(image_uriget);
                                            }
                                            if(dataSnapshot.hasChild("name")){
                                                String nameget = dataSnapshot.child("name").getValue().toString();
                                                friendsHolder.setnamee(nameget);
                                            }
                                            if(dataSnapshot.hasChild("country")){
                                                String countryget = dataSnapshot.child("country").getValue().toString();
                                                friendsHolder.setcountry(countryget);
                                            }

                                            friendsHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    Intent intent = new Intent(friendsHolder.context, UsersProfile_Activity.class);
                                                    intent.putExtra("key", UID);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                        else {
                                            nofriendtext.setVisibility(View.VISIBLE);
                                            nofriends.setVisibility(View.VISIBLE);
                                            friendsHolder.Rview.setVisibility(View.GONE);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        else {
                            friendsHolder.Rview.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };



        Mreqestlist.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class FriendsHolder extends RecyclerView.ViewHolder{

        private CircleImageView Mimage;
        private TextView name, city;
        private View Mview;
        private Context context;
        private RelativeLayout Rview;

        public FriendsHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;

            Mimage = Mview.findViewById(R.id.SprfileImageID);
            name = Mview.findViewById(R.id.SnameID);
            city = Mview.findViewById(R.id.SdetailsID);
            context = Mview.getContext();
            Rview = Mview.findViewById(R.id.ViewID);
        }

        public void setimage(String img){
            Picasso.with(context).load(img).placeholder(R.drawable.default_profileimage).into(Mimage);
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(Mimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        public void setnamee(String nam){
            name.setText(nam);
        }
        public void setcountry(String contry){
            city.setText(contry);
        }
    }
}
