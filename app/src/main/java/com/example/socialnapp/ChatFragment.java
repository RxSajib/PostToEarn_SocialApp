package com.example.socialnapp;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.autofill.Dataset;
import android.text.DynamicLayout;
import android.text.InputType;
import android.util.Log;
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

import com.example.socialnapp.Common.Common;
import com.example.socialnapp.Model.LastTime;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private RecyclerView friendlist;
    private DatabaseReference MuserDatabase, FriendDatabase;
    private FirebaseAuth mauth;
    private String CurrentUserID;
    private ImageView chatimage;
    private TextView chattext;

    private RecyclerView allcontacts;
    private DatabaseReference Mcontactdatabase;
    private SearchView searchView;


    private String zys;
    private ArrayList<String> user_uid_list = new ArrayList<>();

    final ArrayList<LastTime> _time_list = new ArrayList<>();


    // private ArrayList<String> time_list = new ArrayList<>();


    private DatabaseReference MMessageDatabase = FirebaseDatabase.getInstance().getReference().child("Messages");


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        load();


        //  Log.e("BAAAAAAAAAAAAL", "onActivityCreated: "+_time_list );


        // shortListWithTime();


        //TODO :-- Testing...


        //TODO testing fnished


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mauth = FirebaseAuth.getInstance();
        CurrentUserID = mauth.getCurrentUser().getUid();
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


        /// lasttime\
        /// lasttime


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


        return view;
    }

    private void shortListWithTime() {


        // Log.i("Voda", "shortListWithTime: " + _time_list.get(0).getTime());


        LastTime tempj;
        LastTime tempi;


        //  ArrayList<LastTime> _temp_time_list = new ArrayList<>();

        for (int i = 0; i < _time_list.size(); i++) {


            for (int j = 0; j < _time_list.size(); j++) {


                //Log.e("Balll", "shortListWithTime: "+ Double.parseDouble("2019123003449999"));


                if (Double.parseDouble(_time_list.get(i).getTime().replaceAll(":", "").replaceAll("PM", "").replaceAll(" ", "").replaceAll("AM", "").replaceAll("-", "")) > Double.parseDouble(_time_list.get(j).getTime().replaceAll(":", "").replaceAll("PM", "").replaceAll(" ", "").replaceAll("AM", "").replaceAll("-", ""))) {


                    tempj = _time_list.get(j);
                    tempi = _time_list.get(i);


                    _time_list.set(j, tempi);

                    _time_list.set(i, tempj);


                } else {


                    // _temp_time_list.add(_time_list.get(j));
                }


            }


        }


        for (int i = 0; i < _time_list.size(); i++) {


            Log.e("ShortedTime " + i, "shortListWithTime:   " + _time_list.get(i).getUiId() + "   " + _time_list.get(i).getTime());

        }


        for (int i = 0; i < _time_list.size(); i++) {


            Log.e("ShortedTime", "My User IDDD  : " + FirebaseAuth.getInstance().getCurrentUser().getUid());

            if (_time_list.get(i).getUiId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                _time_list.remove(i);

            }


         /*   for (int j = i + 1; j < _time_list.size(); j++) {

                if (_time_list.get(i).getUiId().equals(_time_list.get(j).getUiId())) {

                    _time_list.remove(j);
                }

            }*/


        }


        for (int i = 0; i < _time_list.size(); i++) {

            Log.e("ShortedTime " + i, "NOWWWWWWWWW :   " + _time_list.get(i).getUiId() + "   " + _time_list.get(i).getTime());

        }

    }


    void load() {


        MMessageDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                _time_list.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    // Log.e(TAG, "onChildAdded:  " + ds.getKey());


                    for (DataSnapshot dataSnapshot1 : ds.getChildren()) {


                        // Log.e("Voda", "onDataChange: " + dataSnapshot1);


                        for (DataSnapshot sdc : dataSnapshot1.getChildren()) {

                            // Log.i("AAAAAAAAAAAAAAAAAA", "onDataChange: " + sdc.child("time").getValue());

                            _time_list.add(new LastTime(sdc.child("time").getValue().toString(), ds.getKey()));

                        }


                    }


                }

                shortListWithTime();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //  load();


    }

    private void startScarching(final String sec) {

        String query = sec.toLowerCase();

        final Query firebaseQry = MuserDatabase.orderByChild("search").startAt(query).endAt(query + "\uf8ff");


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
                        if (dataSnapshot.exists()) {

                            chatimage.setVisibility(View.GONE);
                            chattext.setVisibility(View.GONE);


                            if (dataSnapshot.hasChild("name")) {
                                String nameget = dataSnapshot.child("name").getValue().toString();
                                friendHolder.setname(nameget);
                            }

                            if (dataSnapshot.hasChild("image_uri")) {
                                String profileimageget = dataSnapshot.child("image_uri").getValue().toString();
                                friendHolder.setimage(profileimageget);
                            }
                            if (dataSnapshot.hasChild("country")) {
                                String email = dataSnapshot.child("country").getValue().toString();
                                friendHolder.setemail(email);
                            }

                            if (dataSnapshot.hasChild("user_statas")) {
                                String statas = dataSnapshot.child("user_statas").child("statas").getValue().toString();

                                if (statas.equals("online")) {
                                    friendHolder.onlinestats.setVisibility(View.VISIBLE);
                                    friendHolder.onlinestats.setImageResource(R.drawable.active_dot);
                                    friendHolder.onlinetext.setVisibility(View.VISIBLE);
                                    friendHolder.onlinetext.setText("online");
                                } else if (statas.equals("offline")) {
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

                            /// message time

                            /// message time
                        } else {
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


        /////


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
                        if (dataSnapshot.exists()) {

                            chatimage.setVisibility(View.GONE);
                            chattext.setVisibility(View.GONE);

                            if (dataSnapshot.hasChild("name")) {
                                String nameget = dataSnapshot.child("name").getValue().toString();
                                friendHolder.setname(nameget);
                            }
                            if (dataSnapshot.hasChild("username")) {
                                String usernameget = dataSnapshot.child("username").getValue().toString();
                                friendHolder.setusernameset(usernameget);
                            }

                            if (dataSnapshot.hasChild("image_uri")) {
                                String profileimageget = dataSnapshot.child("image_uri").getValue().toString();
                                friendHolder.setimage(profileimageget);
                            }
                            if (dataSnapshot.hasChild("country")) {
                                String email = dataSnapshot.child("country").getValue().toString();
                                friendHolder.setemail(email);
                            }

                            if (dataSnapshot.hasChild("user_statas")) {
                                String statas = dataSnapshot.child("user_statas").child("statas").getValue().toString();

                                if (statas.equals("online")) {
                                    friendHolder.onlinestats.setVisibility(View.VISIBLE);
                                    friendHolder.onlinestats.setImageResource(R.drawable.active_dot);
                                    friendHolder.onlinetext.setVisibility(View.VISIBLE);
                                    friendHolder.onlinetext.setText("online");
                                } else if (statas.equals("offline")) {
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
                        } else {
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


    public static class FriendHolder extends RecyclerView.ViewHolder {

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

        public void setimage(String img) {
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

        public void setname(String nam) {
            mname.setText(nam);
        }

        public void setemail(String em) {
            memail.setText(em);
        }

        public void setusernameset(String uname) {
            username.setText(uname);
        }
    }


}
