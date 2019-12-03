package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_MoneyList extends AppCompatActivity {

    private Toolbar mtoolbar;
    private RecyclerView adminearlist;
    private DatabaseReference Mwithdraw;


    private ImageView earimage;
    private TextView noearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__money_list);

        earimage = findViewById(R.id.idea);
        noearn = findViewById(R.id.ideatext);

        earimage.setVisibility(View.VISIBLE);
        noearn.setVisibility(View.VISIBLE);

        mtoolbar = findViewById(R.id.AdminMneyToolbarID);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("Winners Contacts");
        adminearlist = findViewById(R.id.AdminListID);
        adminearlist.setHasFixedSize(true);
        adminearlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Mwithdraw = FirebaseDatabase.getInstance().getReference().child("Withdraw");
        Mwithdraw.keepSynced(true);
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

        FirebaseRecyclerAdapter<withdrawgetset, EarnHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<withdrawgetset, EarnHolder>(
                withdrawgetset.class,
                R.layout.withdraw_layout,
                EarnHolder.class,
                Mwithdraw
        ) {
            @Override
            protected void populateViewHolder(final EarnHolder earnHolder, withdrawgetset withdrawgetset, int i) {

                String UID = getRef(i).getKey();
                Mwithdraw.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            earimage.setVisibility(View.INVISIBLE);
                            noearn.setVisibility(View.INVISIBLE);

                            if(dataSnapshot.hasChild("username")){
                                String usernameget = dataSnapshot.child("username").getValue().toString();
                                earnHolder.setnameset(usernameget);
                            }
                            if(dataSnapshot.hasChild("profileimage")){
                                String profileimageget = dataSnapshot.child("profileimage").getValue().toString();
                                earnHolder.setprofileimage(profileimageget);
                            }
                            if(dataSnapshot.hasChild("earn")){
                                String earnget = dataSnapshot.child("earn").getValue().toString();
                                earnHolder.setdescptrionset(earnget);
                            }
                        }
                        else {
                            earimage.setVisibility(View.VISIBLE);
                            noearn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        adminearlist.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class EarnHolder extends RecyclerView.ViewHolder{

        private TextView name, descptrion;
        private CircleImageView imageView;
        private View Mview;
        private Context context;

        public EarnHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            name = Mview.findViewById(R.id.WusernameID);
            descptrion = Mview.findViewById(R.id.Wdetails);
            imageView = Mview.findViewById(R.id.WprofileimageID);
            context = Mview.getContext();
        }

        public void setnameset(String nam){
            name.setText(nam);
        }
        public void setprofileimage(String img){
            Picasso.with(context).load(img).placeholder(R.drawable.default_profileimage).into(imageView);

            Picasso.with(context).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        public void setdescptrionset(String des){
            descptrion.setText(des);
        }
    }
}
