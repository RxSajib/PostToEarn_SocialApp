package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Find_FriendUpdate_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView, notScarch;
    private DatabaseReference MuserDatabase;
    private EditText Scarch;
    private ImageView ScarchButton;
    private  String Position = "not_scarch";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find__friend_update_);

        Scarch = findViewById(R.id.ScrchID);
        ScarchButton = findViewById(R.id.ScarchButton);

        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.keepSynced(true);
        recyclerView = findViewById(R.id.FindFriID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notScarch = findViewById(R.id.OnScarchResult);
        notScarch.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notScarch.setHasFixedSize(true);

        startReadtving();

        ScarchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String scarchText = Scarch.getText().toString();

               notScarch.setVisibility(View.GONE);
                scarchItem(scarchText);

            }


        });

    }


    private void startReadtving() {


        notScarch.setVisibility(View.VISIBLE);


        FirebaseRecyclerAdapter<FindFriendModel, FriendHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriendModel, FriendHolder>(
                FindFriendModel.class,
                R.layout.sample_findfriend_layout,
                FriendHolder.class,
                MuserDatabase

        ) {
            @Override
            protected void populateViewHolder(final FriendHolder friendHolder, FindFriendModel findFriendModel, int i) {

                String UID = getRef(i).getKey();
                MuserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild("image_uri")) {
                                String imageref = dataSnapshot.child("image_uri").getValue().toString();
                                friendHolder.setimaset(imageref);
                            }
                            if (dataSnapshot.hasChild("name")) {
                                String nameget = dataSnapshot.child("name").getValue().toString();
                                friendHolder.setnameset(nameget);
                            }
                            if (dataSnapshot.hasChild("country")) {
                                String email_addressget = dataSnapshot.child("country").getValue().toString();
                                friendHolder.setemailset(email_addressget);
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        notScarch.setAdapter(firebaseRecyclerAdapter);

    }




      private void scarchItem(String scarch){


          Query firebasequry = MuserDatabase.orderByChild("name").startAt(scarch).endAt(scarch+"\uf8ff");


          FirebaseRecyclerAdapter<FindFriendModel, FriendHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriendModel, FriendHolder>(
                    FindFriendModel.class,
                    R.layout.sample_findfriend_layout,
                    FriendHolder.class,
                    firebasequry


            ) {
                @Override
                protected void populateViewHolder(final FriendHolder friendHolder, FindFriendModel findFriendModel, int i) {

                    String UID = getRef(i).getKey();
                    MuserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.hasChild("image_uri")) {
                                    String imageref = dataSnapshot.child("image_uri").getValue().toString();
                                    friendHolder.setimaset(imageref);
                                }
                                if (dataSnapshot.hasChild("name")) {
                                    String nameget = dataSnapshot.child("name").getValue().toString();
                                    friendHolder.setnameset(nameget);
                                }
                                if (dataSnapshot.hasChild("country")) {
                                    String email_addressget = dataSnapshot.child("country").getValue().toString();
                                    friendHolder.setemailset(email_addressget);
                                }
                            } else {

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


    public static class FriendHolder extends RecyclerView.ViewHolder{

        private CircleImageView profileimae;
        private TextView name, email;
        private View Mview;
        private Context context;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            profileimae = Mview.findViewById(R.id.SprfileImageID);
            name = Mview.findViewById(R.id.SnameID);
            email = Mview.findViewById(R.id.SdetailsID);
            context = Mview.getContext();
        }


        public void setimaset(String img){
            Picasso.with(context).load(img).placeholder(R.drawable.default_image).into(profileimae);
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(profileimae, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        public void setnameset(String nam){
            name.setText(nam);
        }
        public void setemailset(String ema){
            email.setText(ema);
        }

    }
}
