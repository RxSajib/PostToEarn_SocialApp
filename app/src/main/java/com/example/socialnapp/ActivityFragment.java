package com.example.socialnapp;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    private RecyclerView postrecylearview;
    private DatabaseReference MpostDatabase;
    private Boolean Likechacker = false;
    private DatabaseReference LikeDatabase;
    private FirebaseAuth mauth;
    private String CurrentUserID;
    private FloatingActionButton friendlist;
    private ImageView postimage;
    private TextView posttext;


    private String FriendsID;
    private long Counter=0;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        LikeDatabase = FirebaseDatabase.getInstance().getReference().child("Likes");

        postrecylearview = view.findViewById(R.id.ActivityID);
        postrecylearview.setHasFixedSize(true);
        postrecylearview.setLayoutManager(new LinearLayoutManager(getContext()));
        MpostDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        MpostDatabase.keepSynced(true);
        mauth = FirebaseAuth.getInstance();
        CurrentUserID = mauth.getCurrentUser().getUid();

        postimage = view.findViewById(R.id.NewsImageID);
        posttext = view.findViewById(R.id.PostTextID);

        friendlist = view.findViewById(R.id.FriendListBUttonID);
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyFriendList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onStart() {


        FirebaseRecyclerAdapter<Post_Modal, PostHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post_Modal, PostHolder>(
                Post_Modal.class,
                R.layout.sample_postbanner,
                PostHolder.class,
                MpostDatabase
        ) {
            @Override
            protected void populateViewHolder(final PostHolder postHolder, final Post_Modal post_modal, final int i) {

                final String UID = getRef(i).getKey();
                MpostDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            postimage.setVisibility(View.INVISIBLE);
                            posttext.setVisibility(View.INVISIBLE);

                            if(dataSnapshot.hasChild("username")){
                                String usernameget = dataSnapshot.child("username").getValue().toString();
                                postHolder.set_username(usernameget);
                            }
                            if(dataSnapshot.hasChild("time")){
                                String timeget = dataSnapshot.child("time").getValue().toString();
                                postHolder.settime(timeget);
                            }
                            if(dataSnapshot.hasChild("profile_image")){
                                String profile_imageget = dataSnapshot.child("profile_image").getValue().toString();
                                postHolder.setprofileimage(profile_imageget);
                            }
                            if(dataSnapshot.hasChild("post_descptrion")){
                                String post_descptrionget = dataSnapshot.child("post_descptrion").getValue().toString();
                                postHolder.set_descptrion(post_descptrionget);
                            }
                            if(dataSnapshot.hasChild("postimage")){
                                String postimageget = dataSnapshot.child("postimage").getValue().toString();
                                postHolder.setpostImage_des(postimageget);
                            }

                            postHolder.setLikestatas(UID);


                            postHolder.Mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getContext(), DeletePostActivity.class);
                                    intent.putExtra("key", UID);
                                    startActivity(intent);

                                }
                            });



                            postHolder.comment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), CommentsActivity.class);
                                    intent.putExtra("key", UID);
                                    startActivity(intent);
                                }
                            });


                            /*
                            postHolder.comment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    AlertDialog.Builder Mbuilder = new AlertDialog.Builder(getContext());
                                    view = LayoutInflater.from(getContext()).inflate(R.layout.comment_layout, null, false);

                                    RecyclerView recyclerView = view.findViewById(R.id.CommentsID);
                                    final EditText commentstext = view.findViewById(R.id.InputMessageID);
                                    FloatingActionButton sendbutton = view.findViewById(R.id.SendID);
                                    final DatabaseReference Muserdatabase;
                                    FirebaseAuth Mauth;
                                    final String CurrentUserID;
                                    Mauth = FirebaseAuth.getInstance();
                                    CurrentUserID = Mauth.getCurrentUser().getUid();
                                    Muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                                    final DatabaseReference posts_comments = FirebaseDatabase.getInstance().getReference().child("Post").child(UID).child("comments");

                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                                    sendbutton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Muserdatabase.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    if(dataSnapshot.exists()){
                                                        if(dataSnapshot.hasChild("name")){
                                                            String nameget = dataSnapshot.child("name").getValue().toString();
                                                            validate_comment(nameget);
                                                            commentstext.setText("");
                                                        }
                                                    }
                                                }

                                                private void validate_comment(String nameget) {

                                                    String  eomments = commentstext.getText().toString();
                                                    if(eomments.isEmpty()){
                                                        Toast.makeText(getContext(), "Type any comments", Toast.LENGTH_LONG).show();
                                                    }
                                                    else {



                                                        Calendar calendartime = Calendar.getInstance();
                                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                                                        String Currenttime = simpleDateFormat.format(calendartime.getTime());

                                                        Calendar calendardate = Calendar.getInstance();
                                                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMMM-yyyy");
                                                        String CurrentDate = simpleDateFormat1.format(calendardate.getTime());

                                                        String randomkey = Currenttime+CurrentDate;

                                                        HashMap commentsmap = new HashMap();
                                                        commentsmap.put("user_id", CurrentUserID);
                                                        commentsmap.put("time", Currenttime);
                                                        commentsmap.put("date", CurrentDate);
                                                        commentsmap.put("user_name", nameget);
                                                        commentsmap.put("comments", eomments);

                                                        posts_comments.child(randomkey).updateChildren(commentsmap)
                                                                .addOnCompleteListener(new OnCompleteListener() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task task) {

                                                                        if(task.isSuccessful()){
                                                                            Toast.makeText(getContext(), "post is send", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        else {
                                                                            String eror = task.getException().getMessage().toString();
                                                                            Toast.makeText(getContext(), eror, Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                    });


                                    FirebaseRecyclerAdapter<CommentModal, CommnetsHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<CommentModal, CommnetsHolder>(

                                            CommentModal.class,
                                            R.layout.comment_layout,
                                            CommnetsHolder.class,
                                            posts_comments
                                    ) {
                                        @Override
                                        protected void populateViewHolder(CommnetsHolder commnetsHolder, CommentModal commentModal, int i) {

                                            posts_comments.child(getRef(i).getKey()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Toast.makeText(getContext(), dataSnapshot.child("date").getValue().toString(),Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }


                                    };

                                    recyclerView.setAdapter(firebaseRecyclerAdapter1);
                                    AlertDialog alertDialog = Mbuilder.create();
                                    alertDialog.setView(view);
                                    alertDialog.show();
                                }
                            });

                            */









                            postHolder.likeimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    ///like

                                    if(postHolder.CurrentState.equals("not_like")){


                                        postHolder.Post.child(UID).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if(dataSnapshot.exists()){
                                                    FriendsID = dataSnapshot.child("uid").getValue().toString();
                                                    startliking( FriendsID);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                    ///like


                                    Likechacker = true;

                                    LikeDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(Likechacker.equals(true)){
                                                if(dataSnapshot.child(UID).hasChild(CurrentUserID)){

                                                    LikeDatabase.child(UID).child(CurrentUserID).removeValue();
                                                    Likechacker = false;
                                                }
                                                else {
                                                    LikeDatabase.child(UID).child(CurrentUserID).setValue(true);
                                                    Likechacker = false;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }


                               ///////////////////////
                                private void startliking(final String fid){

                                    Toast.makeText(getContext(), "call", Toast.LENGTH_LONG).show();
                                    Toast.makeText(getContext(), fid, Toast.LENGTH_LONG).show();

                                    postHolder.Mlike.child(CurrentUserID).child(fid).child("sernd_type")
                                            .setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                postHolder.Mlike.child(fid).child(CurrentUserID).child("send_type")
                                                        .setValue("recived")
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){


                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });

                                }
                                /////////////////////////
                            });
                        }
                        else {
                            postimage.setVisibility(View.INVISIBLE);
                            posttext.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        postrecylearview.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class PostHolder extends RecyclerView.ViewHolder{


        private CircleImageView mprofileimage;
        private ImageView postimage;
        private TextView username, descptrion, time;
        private View Mview;
        private Context context;
        private ImageView likeimage, comment;
        private TextView likecount;

        private int countLikes;
        private String CurrentID;
        private DatabaseReference Mlikedatabase;
        private String CurrentState = "not_like";

        private DatabaseReference Post, Mlike;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            mprofileimage = Mview.findViewById(R.id.MprofileImageID);
            postimage = Mview.findViewById(R.id.bannerImageID);
            username = Mview.findViewById(R.id.BannerNameID);
            descptrion = Mview.findViewById(R.id.BannerDescptrionID);
            time = Mview.findViewById(R.id.TimeID);
            context = Mview.getContext();
            likeimage = Mview.findViewById(R.id.LikeButtonID);
            likecount = Mview.findViewById(R.id.LikeCountID);
            comment = Mview.findViewById(R.id.CommentButtonID);

            Post = FirebaseDatabase.getInstance().getReference().child("Post");
            Mlike = FirebaseDatabase.getInstance().getReference().child("All_Like");

            Mlikedatabase = FirebaseDatabase.getInstance().getReference().child("Likes");
            CurrentID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setLikestatas(final String PostKey){

            Mlikedatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(PostKey).hasChild(CurrentID)){

                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        likeimage.setImageResource(R.drawable.redlike);
                        likecount.setText(Integer.toString(countLikes)+" Likes");

                    }
                    else {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        likeimage.setImageResource(R.drawable.black_like);
                        likecount.setText(Integer.toString(countLikes)+" Likes");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setprofileimage(String pimg){
            Picasso.with(context).load(pimg).resize(100, 100).placeholder(R.drawable.default_profileimage).into(mprofileimage);
            Picasso.with(context).load(pimg).resize(100, 100).networkPolicy(NetworkPolicy.OFFLINE).into(mprofileimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        public void setpostImage_des(String pimg){
            Picasso.with(context).load(pimg).resize(150, 150).centerCrop().placeholder(R.drawable.default_galleryicon).into(postimage);

            Picasso.with(context).load(pimg).resize(1200, 1200).networkPolicy(NetworkPolicy.OFFLINE).into(postimage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        public void set_username(String nam){
            username.setText(nam);
        }
        public void set_descptrion(String des){
            descptrion.setText(des);
        }
        public void settime(String tim){
            time.setText(tim);
        }
    }
}
