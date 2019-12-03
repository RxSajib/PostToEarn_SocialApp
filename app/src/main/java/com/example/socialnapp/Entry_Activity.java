package com.example.socialnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Entry_Activity extends AppCompatActivity {

    private CircleImageView prfileimage;
    private TextView name, post, like, comment;
    private DatabaseReference MuserDatabasem ,Mpost, Like, Comments;
    private String CurrentUserID, PostRandomKey, LikerandomKey;
    private FirebaseAuth Mauth;
    private long counter;
    private long counterlike, followcoun;
    private DatabaseReference likedata, postdata, followme, all_like;
    private TextView followtext;
    private TextView followcounttext;
    private DatabaseReference MyComments;
    private String UserComments;
    private DatabaseReference Mearn;
    private int countotoallike;
    private TextView Likes;
    private FloatingActionButton help;

    private TextView heardoller, followdoller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_);
        Likes = findViewById(R.id.LikeID);
        Mearn = FirebaseDatabase.getInstance().getReference().child("All_Like");

        heardoller = findViewById(R.id.LiksDoller);
        followdoller = findViewById(R.id.FollowMeDoller);

     //   all_like = FirebaseDatabase.getInstance().getReference().child("All_Like");

        help = findViewById(R.id.HelpButtonID);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCustomDialog();

            }
        });



        followcounttext = findViewById(R.id.FollowCount);
        MuserDatabasem = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabasem.keepSynced(true);
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        prfileimage = findViewById(R.id.ImageEID);  //CommentID
        name = findViewById(R.id.NameIDT);
        post = findViewById(R.id.PostID);
        like = findViewById(R.id.LikeID);
        comment = findViewById(R.id.CommentID);
        Mpost = FirebaseDatabase.getInstance().getReference().child("Post");
        Mpost.keepSynced(true);
        Like = FirebaseDatabase.getInstance().getReference().child("Likes");
        Like.keepSynced(true);
        Comments = FirebaseDatabase.getInstance().getReference().child("Post");
        Comments.keepSynced(true);
        followme = FirebaseDatabase.getInstance().getReference().child("Follow");
        followme.keepSynced(true);
        MyComments = FirebaseDatabase.getInstance().getReference().child("Post");
        MyComments.keepSynced(true);
        UserComments = Mauth.getCurrentUser().getUid();



      Mearn.child(CurrentUserID).addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {

              for(DataSnapshot data : dataSnapshot.getChildren()){
                  String X = data.getValue().toString();
                  if(X.equals("recived")){
                      counter++;
                      like.setText(Long.toString(counter));

                      if(counter == 1000){
                          heardoller.setText("1");
                      } if(counter == 2000){
                          heardoller.setText("2");
                      } if(counter == 3000){
                          heardoller.setText("3");
                      } if(counter == 4000){
                          heardoller.setText("4");
                      } if(counter == 5000){
                          heardoller.setText("5");
                      }
                  }
              }
          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {

          }

          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });


/*
        MyComments.orderByChild("uid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){

                    if(data.hasChild("Comments")){
                        String courrentuser = data.child("uid").getValue().toString();
                        if(courrentuser.equals(UserComments)){

                            Long uid = data.child("Comments").getChildrenCount();
                            comment.setText(Long.toString(uid));
                        }
                        else{
                            comment.setText("0");
                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/




/*
        all_like.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot data : dataSnapshot.getChildren()){

                    String datalike = data.getValue().toString();
              //      Toast.makeText(getApplicationContext(), "liketype :"+datalike, Toast.LENGTH_LONG).show();

                    if(datalike.equals("recived")){
                        counter++;
                        like.setText(Long.toString(counter));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


        followme.child(CurrentUserID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String datacount = data.getValue().toString();
               //     Toast.makeText(getApplicationContext(), datacount, Toast.LENGTH_LONG).show();
                    if(datacount.equals("recived")){
                        followcoun++;
                        followcounttext.setText(Long.toString(followcoun));

                        if(followcoun == 1000){
                            followdoller.setText("3");
                        }
                        if(followcoun == 2000){
                            followdoller.setText("6");
                        }
                        if(followcoun == 3000){
                            followdoller.setText("9");
                        }
                        if(followcoun == 4000){
                            followdoller.setText("12");
                        }
                        if(followcoun == 5000){
                            followdoller.setText("15");
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



/*
        Mpost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){

                    if(data.hasChild("uid")){
                        String uid = data.child("uid").getValue().toString();

                        if(uid.equals(CurrentUserID)){
                            counter++;
                            post.setText(Long.toString(counter));
                        }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/






        MuserDatabasem.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("image_uri")){
                        String image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                        Picasso.with(getApplicationContext()).load(image_uriget).placeholder(R.drawable.default_profileimage).into(prfileimage);
                    }
                    if(dataSnapshot.hasChild("name")){
                        String image_uriget = dataSnapshot.child("name").getValue().toString();
                        name.setText(image_uriget);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.how_to_earn, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
