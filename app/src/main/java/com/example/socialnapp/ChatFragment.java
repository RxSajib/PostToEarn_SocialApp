package com.example.socialnapp;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment  {

    private RecyclerView friendlist;
    private DatabaseReference MuserDatabase, FriendDatabase;
    private FirebaseAuth mauth;
    private String CurrentUserID;
    private ImageView chatimage;
    private TextView chattext;

    private RecyclerView allcontacts;
    private DatabaseReference Mcontactdatabase;
    private SearchView searchView;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =   inflater.inflate(R.layout.fragment_chat, container, false);
      mauth = FirebaseAuth.getInstance();
      CurrentUserID  = mauth.getCurrentUser().getUid();
      FriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(CurrentUserID);
      FriendDatabase.keepSynced(true);
      MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
      MuserDatabase.keepSynced(true);
      searchView = view.findViewById(R.id.ScarchViewID);
      searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);


      friendlist = view.findViewById(R.id.FriendListID);
      friendlist.setHasFixedSize(true);
      friendlist.setLayoutManager(new LinearLayoutManager(getContext()));


      chatimage = view.findViewById(R.id.ChatLogoID);
      chattext = view.findViewById(R.id.ChatTextID);


      ////////////CONTACTS/////////////////////
      allcontacts = view.findViewById(R.id.AllcontactID);
      allcontacts.setHasFixedSize(true);
      allcontacts.setLayoutManager(new LinearLayoutManager(getActivity()));
      Mcontactdatabase = FirebaseDatabase.getInstance().getReference().child("");
      /////////////CONTACTS///////////////////////

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



      return  view;
    }








    private void startScarching(final String sec){

        String query = sec.toLowerCase();

            final Query firebaseQry = MuserDatabase.orderByChild("search").startAt(query).endAt(query+"\uf8ff");


        FirebaseRecyclerAdapter<FindFriend_Model, FriendHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriend_Model, FriendHolder>(
                FindFriend_Model.class,
                R.layout.sample_findfriend_layout,
                FriendHolder.class,
                firebaseQry
        ) {
            @Override
            protected void populateViewHolder(final FriendHolder friendHolder, FindFriend_Model findFriend_model, int i) {

                final String UID = getRef(i).getKey();
                MuserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            chatimage.setVisibility(View.GONE);
                            chattext.setVisibility(View.GONE);


                            if(dataSnapshot.hasChild("name")){
                                String nameget = dataSnapshot.child("name").getValue().toString();
                                friendHolder.setname(nameget);
                            }

                            if(dataSnapshot.hasChild("image_uri")){
                                String profileimageget = dataSnapshot.child("image_uri").getValue().toString();
                                friendHolder.setimage(profileimageget);
                            }
                            if(dataSnapshot.hasChild("country")){
                                String email = dataSnapshot.child("country").getValue().toString();
                                friendHolder.setemail(email);
                            }

                            if(dataSnapshot.hasChild("user_statas")){
                                String statas = dataSnapshot.child("user_statas").child("statas").getValue().toString();

                                if(statas.equals("online")){
                                    friendHolder.onlinestats.setVisibility(View.VISIBLE);
                                    friendHolder.onlinestats.setImageResource(R.drawable.active_dot);
                                    friendHolder.onlinetext.setVisibility(View.VISIBLE);
                                    friendHolder.onlinetext.setText("online");
                                }
                                else if(statas.equals("offline")){
                                    friendHolder.onlinestats.setVisibility(View.VISIBLE);
                                    friendHolder.onlinestats.setImageResource(R.drawable.inactive_dot);
                                    friendHolder.onlinetext.setVisibility(View.VISIBLE);
                                    friendHolder.onlinetext.setText("offline");
                                }
                            }



                            friendHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.putExtra("key", UID);
                                    startActivity(intent);

                                    

                                }
                            });
                        }
                        else {
                            chatimage.setVisibility(View.VISIBLE);
                            chattext.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        allcontacts.setAdapter(firebaseRecyclerAdapter);


///wait

    }

    @Override
    public void onStart() {
        FirebaseRecyclerAdapter<FindFriend_Model, FriendHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriend_Model, FriendHolder>(
                FindFriend_Model.class,
                R.layout.sample_findfriend_layout,
                FriendHolder.class,
                MuserDatabase
        ) {
            @Override
            protected void populateViewHolder(final FriendHolder friendHolder, FindFriend_Model findFriend_model, int i) {

                final String UID = getRef(i).getKey();
                MuserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            chatimage.setVisibility(View.GONE);
                            chattext.setVisibility(View.GONE);

                            if(dataSnapshot.hasChild("name")){
                                String nameget = dataSnapshot.child("name").getValue().toString();
                                friendHolder.setname(nameget);
                            }
                            if(dataSnapshot.hasChild("username")){
                                String usernameget = dataSnapshot.child("username").getValue().toString();
                                friendHolder.setusernameset(usernameget);
                            }

                            if(dataSnapshot.hasChild("image_uri")){
                                String profileimageget = dataSnapshot.child("image_uri").getValue().toString();
                                friendHolder.setimage(profileimageget);
                            }
                            if(dataSnapshot.hasChild("country")){
                                String email = dataSnapshot.child("country").getValue().toString();
                                friendHolder.setemail(email);
                            }

                            if(dataSnapshot.hasChild("user_statas")){
                                String statas = dataSnapshot.child("user_statas").child("statas").getValue().toString();

                                if(statas.equals("online")){
                                    friendHolder.onlinestats.setVisibility(View.VISIBLE);
                                    friendHolder.onlinestats.setImageResource(R.drawable.active_dot);
                                    friendHolder.onlinetext.setVisibility(View.VISIBLE);
                                    friendHolder.onlinetext.setText("online");
                                }
                                else if(statas.equals("offline")){
                                    friendHolder.onlinestats.setVisibility(View.VISIBLE);
                                    friendHolder.onlinestats.setImageResource(R.drawable.inactive_dot);
                                    friendHolder.onlinetext.setVisibility(View.VISIBLE);
                                    friendHolder.onlinetext.setText("offline");
                                }
                            }



                            friendHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.putExtra("key", UID);
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            chatimage.setVisibility(View.VISIBLE);
                            chattext.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        allcontacts.setAdapter(firebaseRecyclerAdapter);



        super.onStart();
    }


    public static class FriendHolder extends RecyclerView.ViewHolder{

        private CircleImageView mimage;
        private TextView mname, memail, onlinetext, username;
        private View Mview;
        private Context context;
        private ImageView onlinestats;


        public FriendHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            mimage = Mview.findViewById(R.id.SprfileImageID);
            mname = Mview.findViewById(R.id.SnameID);
            memail = Mview.findViewById(R.id.SdetailsID);
            context = Mview.getContext();
            onlinestats = Mview.findViewById(R.id.ActiveDotID);
            onlinetext = Mview.findViewById(R.id.onlinetextID);
            username = Mview.findViewById(R.id.UsernameIDID);
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

        public void setusernameset(String  uname){
            username.setText(uname);
        }
    }



}
