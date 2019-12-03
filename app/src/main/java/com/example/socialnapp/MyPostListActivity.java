package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String postref;
    private DatabaseReference MpostDataase, Post;
    private FirebaseAuth Mauth;
    private String CurrentuserID, selectuid;
    private Toolbar posttoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_list);

        posttoolbar = findViewById(R.id.MyPostToolbarID);
        setSupportActionBar(posttoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("My Post");
        recyclerView = findViewById(R.id.MuPostViewID);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        Post = FirebaseDatabase.getInstance().getReference().child("Post");
        Post.keepSynced(true);
        Mauth = FirebaseAuth.getInstance();
        CurrentuserID = Mauth.getCurrentUser().getUid();

        postref = getIntent().getStringExtra("key");


        Post.child(postref).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("uid")){
                         selectuid = dataSnapshot.child("uid").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        super.onBackPressed();
    }

    @Override
    protected void onStart() {

        FirebaseRecyclerAdapter<Post_Modal, PostHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post_Modal, PostHolder>(
                Post_Modal.class,
                R.layout.sample_postbanner,
                PostHolder.class,
                Post
        ) {
            @Override
            protected void populateViewHolder(PostHolder postHolder, Post_Modal post_modal, int i) {

                Post.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){

                            if(data.exists()){
                                String uid = data.child("uid").getValue().toString();

                              Toast.makeText(getApplicationContext(), selectuid, Toast.LENGTH_LONG).show();


                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

       // recyclerView.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    private static class PostHolder extends RecyclerView.ViewHolder{

        private View view;
        private TextView name;
        private CircleImageView profileimage;
        private TextView time;
        private TextView textdescptrion;
        private Context context;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            profileimage = view.findViewById(R.id.MprofileImageID);
            name = view.findViewById(R.id.BannerNameID);
            textdescptrion = view.findViewById(R.id.BannerDescptrionID);
            time = view.findViewById(R.id.TimeID);
            context = view.getContext();
        }

        public void setimage(String img){
            Picasso.with(context).load(img).placeholder(R.drawable.default_profileimage).into(profileimage);
            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(profileimage, new Callback() {
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
        public void settimeset(String tim){
            time.setText(tim);
        }
        public void setdescptrionset(String des){
            textdescptrion.setText(des);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
