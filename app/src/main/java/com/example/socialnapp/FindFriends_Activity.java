package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriends_Activity extends AppCompatActivity {

    private Toolbar fientoolbar;
    private RecyclerView MrecylerView, Mlist;
    private String CurrentUserID;
    private FirebaseAuth Mauth;
    private DatabaseReference Muserdatabase;

    private EditText Scarch;
    private ImageView ScarchButton;
    private Toolbar frtoolbar;
    private SearchView searchView;





   // private List<String>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends_);


        frtoolbar = findViewById(R.id.FindFriendsToolbarID);
        setSupportActionBar(frtoolbar);
        getSupportActionBar().setTitle("Find Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        ScarchButton = findViewById(R.id.ScarchButtonIDID);
        Scarch = findViewById(R.id.ScrchIDID);

        Muserdatabase  = FirebaseDatabase.getInstance().getReference().child("Users");
        Muserdatabase.keepSynced(true);
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        MrecylerView = findViewById(R.id.RecyclerViewID);



        MrecylerView.setHasFixedSize(true);
        MrecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Mlist = findViewById(R.id.MlistID);
        Mlist.setHasFixedSize(true);
        Mlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //readContant();






/*
        ScarchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Mlist.setVisibility(View.GONE);
                String ScarchText = Scarch.getText().toString();

                startScarching(ScarchText);
            }
        });
        */


       // onScarch();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_items, menu);
        final MenuItem item = menu.findItem(R.id.SearchID);

/*
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                startScarching(query);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                startScarching(newText);
                return false;
            }
        });
        */



        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                startScarching(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                startScarching(newText);
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
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
        FirebaseRecyclerAdapter<FindFriendModel, FindFriendsHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriendModel, FindFriendsHolder>(
                FindFriendModel.class,
                R.layout.sample_findfriend_layout,
                FindFriendsHolder.class,
                Muserdatabase

        ) {
            @Override
            protected void populateViewHolder(final FindFriendsHolder findFriendsHolder, final FindFriendModel findFriendModel, int i) {

                final String UID = getRef(i).getKey();
                Muserdatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("country")){
                                String email_addressget = dataSnapshot.child("country").getValue().toString();
                                findFriendsHolder.setemail(email_addressget);
                            }
                            if(dataSnapshot.hasChild("name")){
                                String namegget = dataSnapshot.child("name").getValue().toString();

                                Log.i("name", "onDataChange: ");

                                findFriendsHolder.setname(namegget);
                            }
                            if(dataSnapshot.hasChild("image_uri")){
                                String image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                                findFriendsHolder.setimage(image_uriget);
                            }

                            findFriendsHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(FindFriends_Activity.this, UsersProfile_Activity.class);
                                    intent.putExtra("key", UID);
                                    startActivity(intent);
                                }
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

        MrecylerView.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    private void startScarching(String newText){

        String query = newText.toLowerCase();

        Query firebaseQry = Muserdatabase.orderByChild("search").startAt(query).endAt(query+"\uf8ff");

        FirebaseRecyclerAdapter<FindFriendModel, FindFriendsHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriendModel, FindFriendsHolder>(
                FindFriendModel.class,
                R.layout.sample_findfriend_layout,
                FindFriendsHolder.class,
                firebaseQry

        ) {
            @Override
            protected void populateViewHolder(final FindFriendsHolder findFriendsHolder, FindFriendModel findFriendModel, int i) {

                final String UID = getRef(i).getKey();
                Muserdatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("country")){
                                String email_addressget = dataSnapshot.child("country").getValue().toString();
                                findFriendsHolder.setemail(email_addressget);
                            }
                            if(dataSnapshot.hasChild("name")){
                                String namegget = dataSnapshot.child("name").getValue().toString();
                                findFriendsHolder.setname(namegget);
                            }
                            if(dataSnapshot.hasChild("image_uri")){
                                String image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                                findFriendsHolder.setimage(image_uriget);
                            }

                            findFriendsHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(FindFriends_Activity.this, UsersProfile_Activity.class);
                                    intent.putExtra("key", UID);
                                    startActivity(intent);
                                }
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

        MrecylerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class FindFriendsHolder extends RecyclerView.ViewHolder{

        private CircleImageView mimage;
        private TextView mname, emailadress;
        private View Mview;
        private Context context;

        public FindFriendsHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            context = Mview.getContext();
            mimage = Mview.findViewById(R.id.SprfileImageID);
            mname = Mview.findViewById(R.id.SnameID);
            emailadress = Mview.findViewById(R.id.SdetailsID);
        }

        public void setname(String nam){
            mname.setText(nam);
        }
        public void setemail(String email){
            emailadress.setText(email);
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
    }


 //   @Override
  //  protected void onStart() {

    private void onScarch() {


            Mlist.setEnabled(true);
            Mlist.setVisibility(View.VISIBLE);

            FirebaseRecyclerAdapter<FindFriendModel, FindFriendsHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriendModel, FindFriendsHolder>(
                    FindFriendModel.class,
                    R.layout.sample_findfriend_layout,
                    FindFriendsHolder.class,
                    Muserdatabase

            ) {
                @Override
                protected void populateViewHolder(final FindFriendsHolder findFriendsHolder, FindFriendModel findFriendModel, int i) {

                    final String UID = getRef(i).getKey();
                    Muserdatabase.child(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.hasChild("country")) {
                                    String email_addressget = dataSnapshot.child("country").getValue().toString();
                                    findFriendsHolder.setemail(email_addressget);
                                }
                                if (dataSnapshot.hasChild("name")) {
                                    String namegget = dataSnapshot.child("name").getValue().toString();
                                    findFriendsHolder.setname(namegget);
                                }
                                if (dataSnapshot.hasChild("image_uri")) {
                                    String image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                                    findFriendsHolder.setimage(image_uriget);
                                }

                                findFriendsHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(FindFriends_Activity.this, UsersProfile_Activity.class);
                                        intent.putExtra("key", UID);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            };

        MrecylerView.setAdapter(firebaseRecyclerAdapter);


        }


 //       super.onStart();
 //   }



}
