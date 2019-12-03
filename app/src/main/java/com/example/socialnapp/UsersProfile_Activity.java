package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnapp.Common.Common;
import com.example.socialnapp.Model.Myresponce;
import com.example.socialnapp.Model.Notification;
import com.example.socialnapp.Model.Sender;
import com.example.socialnapp.Model.Token;
import com.example.socialnapp.Remote.APIservice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersProfile_Activity extends AppCompatActivity {

    private Toolbar userprofile_toolbar;
    private String ReciverUserID, SenderUserID;
    private TextView backbutton;
    private TextView name, country, work, gender, username, age;
    private CircleImageView profiliamge;
    private DatabaseReference Muserdatabase;
    private String nameget;
    private Button decline_friendbutton, sendfriend_request_button;
    private String CurrentStates ;
    private FirebaseAuth Mauth;
    private DatabaseReference MFriendRequestDatabase, Friends;
    private String Currentitme, Currentdate;
    private DatabaseReference Mfollowdatabase;

    private DatabaseReference Mpermissiondata;

    private TextView number;
    private DatabaseReference UserDatabase;


    private Button followbutton;
    private String followstatas;
    private DatabaseReference followcount;
    private DatabaseReference MnotufactionDataase;

    APIservice mService;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile_);


        mService= Common.getFCMClient();


        MnotufactionDataase = FirebaseDatabase.getInstance().getReference().child("Notifactions");


//        followcount = FirebaseDatabase.getInstance().getReference().child(SenderUserID).child("followcount");
        UserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Mpermissiondata = FirebaseDatabase.getInstance().getReference().child("Permission");
        number = findViewById(R.id.NumberID);
        Mfollowdatabase = FirebaseDatabase.getInstance().getReference().child("Follow");

        followbutton = findViewById(R.id.FollowButtonID);
        clickfollowbutton();
        followstatas = "No_Follow";

        Friends = FirebaseDatabase.getInstance().getReference().child("Friends");
        MFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        Mauth = FirebaseAuth.getInstance();
        SenderUserID = Mauth.getCurrentUser().getUid();
        userprofile_toolbar = findViewById(R.id.UsersProfileToolbarID);
        setSupportActionBar(userprofile_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nameget);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        ReciverUserID = getIntent().getStringExtra("key");
        backbutton = findViewById(R.id.GobackID);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sendfriend_request_button = findViewById(R.id.RequestButtonID);
        decline_friendbutton = findViewById(R.id.CancelRequestButtonID);
        age =  findViewById(R.id.AgeID);
        country = findViewById(R.id.countryID);
        work = findViewById(R.id.WorkIDID);
        gender = findViewById(R.id.gender);
        username = findViewById(R.id.UserNameID);
        profiliamge = findViewById(R.id.ProfileImageID);
        name = findViewById(R.id.NameIDDID);
        Muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(ReciverUserID);
        Muserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("username")){
                        String usernameget = dataSnapshot.child("username").getValue().toString();
                        name.setText(usernameget);
                    }

                    if(dataSnapshot.hasChild("image_uri")){
                        String image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                        Picasso.with(UsersProfile_Activity.this).load(image_uriget).placeholder(R.drawable.default_profileimage).into(profiliamge);
                    }
                    if(dataSnapshot.hasChild("name")){
                          nameget = dataSnapshot.child("name").getValue().toString();
                        username.setText(nameget);
                    }
                    if(dataSnapshot.hasChild("country")){
                        String countryget = dataSnapshot.child("country").getValue().toString();
                        country.setText(countryget);
                    }
                    if(dataSnapshot.hasChild("sex")){
                        String sexget = dataSnapshot.child("sex").getValue().toString();
                        gender.setText(sexget);
                    }
                    if(dataSnapshot.hasChild("work")){
                        String email_addressget = dataSnapshot.child("work").getValue().toString();
                        work.setText(email_addressget);
                    }
                    if(dataSnapshot.hasChild("birth")){
                        String whatsApp_numberget = dataSnapshot.child("birth").getValue().toString();
                        age.setText(whatsApp_numberget);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        decline_friendbutton.setVisibility(View.INVISIBLE);
        decline_friendbutton.setEnabled(false);

        if(ReciverUserID.equals(SenderUserID)){
            decline_friendbutton.setVisibility(View.INVISIBLE);
            sendfriend_request_button.setVisibility(View.INVISIBLE);
        }
        else {
            sendfriend_request_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendfriend_request_button.setEnabled(false);

                    if(CurrentStates.equals("not_friends")){
                        SendFriend_Request();
                    }
                    if(CurrentStates.equals("request_send")){
                        Cancel_Request();
                    }
                    if(CurrentStates.equals("Request_Recived")){
                        AccepectRequest();
                    }
                    if(CurrentStates.equals("friends")){

                        UnfriendExistingFriend();
                    }

                }

                ///Accepect Request
                private void AccepectRequest() {
                    Calendar calendartime = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                    Currentitme = simpleDateFormat.format(calendartime.getTime());

                    Calendar calendardate = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMMM-yyyy");
                    Currentdate = simpleDateFormat1.format(calendardate.getTime());

                    Friends.child(SenderUserID).child(ReciverUserID).child("date").setValue(Currentdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        Friends.child(ReciverUserID).child(SenderUserID).child("date").setValue(Currentdate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){
                                                            MFriendRequestDatabase.child(SenderUserID).child(ReciverUserID)
                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        CurrentStates = "friends";
                                                                        sendfriend_request_button.setEnabled(true);
                                                                        sendfriend_request_button.setText("Unfriend");
                                                                        sendfriend_request_button.setBackgroundResource(R.color.colorPrimary);

                                                                        decline_friendbutton.setVisibility(View.INVISIBLE);
                                                                        decline_friendbutton.setEnabled(false);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
                ///Accepect Request
            });
        }

        inins_fildes_methodes();
        matainted_button();



        /// follow function











        ///permission
        Mpermissiondata.child("permission").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(final DataSnapshot data : dataSnapshot.getChildren()){


                    String UID = data.getValue().toString();
                    List list = new ArrayList();

                    list.add( UID);
                    Toast.makeText(getApplicationContext(), UID, Toast.LENGTH_LONG).show();

                    UserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                number.setText("set");
                            }
                            else {
                                number.setText("not set");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


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



 //       mantain_followersButton();

        maintainfollow_button();

    }




    ///unfriend
    private void UnfriendExistingFriend(){
        Friends.child(SenderUserID).child(ReciverUserID)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Friends.child(ReciverUserID).child(SenderUserID)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                CurrentStates = "not_friends";
                                sendfriend_request_button.setEnabled(true);
                                sendfriend_request_button.setText("Request");
                                sendfriend_request_button.setBackgroundResource(R.color.colorPrimary);

                                decline_friendbutton.setVisibility(View.INVISIBLE);
                                decline_friendbutton.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
    }
    ///unfriend

    /// Cancel Request
    private void Cancel_Request(){
        MFriendRequestDatabase.child(SenderUserID).child(ReciverUserID)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    MFriendRequestDatabase.child(ReciverUserID).child(SenderUserID)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            CurrentStates = "not_friends";
                            sendfriend_request_button.setEnabled(true);
                            sendfriend_request_button.setText("Request");
                            sendfriend_request_button.setBackgroundResource(R.color.colorPrimary);

                            decline_friendbutton.setVisibility(View.INVISIBLE);
                            decline_friendbutton.setEnabled(false);
                        }
                    });
                }
            }
        });
    }
    /// Cancel Request
/*

    private void mantain_followersButton() {

      Mfollowdatabase.child(SenderUserID)
              .addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      if(dataSnapshot.hasChild(ReciverUserID)){
                          String requesttype = dataSnapshot.child(ReciverUserID).child("follow_statas").getValue().toString();
                          Toast.makeText(getApplicationContext(), requesttype, Toast.LENGTH_LONG).show();

                          if(requesttype.equals("send")){

                              followstatas = "deletefollow";
                              followme.setText("Unfollow");

                          }
                          else if(requesttype.equals("")){
                              followstatas = "unflow";
                              followme.setText("follow");
                          }
                      }
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });

    }
    */


    ///maintain_button
    private void matainted_button() {

        MFriendRequestDatabase.child(SenderUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(ReciverUserID)){
                            String request_type = dataSnapshot.child(ReciverUserID).child("request_type").getValue().toString();
                            if(request_type.equals("send")){
                                CurrentStates = "request_send";
                                sendfriend_request_button.setText("Remove");
                                sendfriend_request_button.setBackgroundResource(R.color.red);

                                decline_friendbutton.setVisibility(View.INVISIBLE);
                                decline_friendbutton.setEnabled(false);
                            }
                            else if(request_type.equals("recived")){

                                CurrentStates = "Request_Recived";
                                sendfriend_request_button.setText("Accepect");
                                sendfriend_request_button.setBackgroundResource(R.color.colorPrimary);
                                decline_friendbutton.setVisibility(View.VISIBLE);
                                decline_friendbutton.setEnabled(true);

                                decline_friendbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Cancel_Request();
                                    }
                                });
                            }

                        }
                        else {
                            Friends.child(SenderUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(ReciverUserID)){
                                        CurrentStates = "friends";
                                        sendfriend_request_button.setText("Unfriend");
                                        sendfriend_request_button.setBackgroundResource(R.color.red);
                                        decline_friendbutton.setVisibility(View.INVISIBLE);
                                        decline_friendbutton.setEnabled(false);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    ///send Friend Request
    private void SendFriend_Request(){

        MFriendRequestDatabase.child(SenderUserID).child(ReciverUserID).
                child("request_type").setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    MFriendRequestDatabase.child(ReciverUserID).child(SenderUserID)
                            .child("request_type").setValue("recived")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        HashMap<String, String> notifactionmap = new HashMap<>();
                                        notifactionmap.put("from", SenderUserID);
                                        notifactionmap.put("type", "request");
                                        MnotufactionDataase.child(ReciverUserID).push().setValue(notifactionmap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){

                                                        }
                                                        else {

                                                        }
                                                    }
                                                });


                                        sendfriend_request_button.setEnabled(true);
                                        CurrentStates = "request_send";
                                        sendfriend_request_button.setText("Remove");
                                        sendfriend_request_button.setBackgroundResource(R.color.red);

                                        decline_friendbutton.setVisibility(View.INVISIBLE);
                                        decline_friendbutton.setEnabled(false);




                                        sendNotification();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void sendNotification() {



        FirebaseDatabase.getInstance().getReference("Token").child(ReciverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Token token=dataSnapshot.getValue(Token.class);


                String message="New Friend Request Found!";
                String title="Friend Request";


              /*  HashMap<String,String> data=new HashMap<>();
                data.put("title",title);
                data.put("body",message);*/




                Notification notification=new Notification(message,title);
                Sender noti=new Sender(token.getToken(),notification);


                mService.sendNotification(noti).enqueue(new Callback<Myresponce>() {
                    @Override
                    public void onResponse(Call<Myresponce> call, Response<Myresponce> response) {

                        Log.i("STATUS", "onResponse: SUCCESS "  +  response.message());

                    }

                    @Override
                    public void onFailure(Call<Myresponce> call, Throwable t) {

                        Log.i("STATUS", "onResponse: FAILED ");


                    }
                });







            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
    // /send Friend Request

    private void inins_fildes_methodes(){

        CurrentStates= "not_friends";

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }








/*


    private void startfillowing(){

        Mfollowdatabase.child(SenderUserID).child(ReciverUserID)
                .child("type").setValue("send")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Mfollowdatabase.child(ReciverUserID).child(SenderUserID)
                                    .child("type").setValue("recived")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            followstatas = "Follow";
                                            followme.setText("unfollow");
                                        }
                                    });
                        }
                    }
                });

    }
*/

/*

    private void deletefollow(){
        Mfollowdatabase.child(SenderUserID)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Mfollowdatabase.child(ReciverUserID)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                followstatas = "No_Follow";
                                followme.setText("follow");
                            }
                        }
                    });
                }
            }
        });
    }


*/


private void clickfollowbutton(){


    followbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(followstatas.equals("No_Follow")){
                startfollowing();
            }
            if(followstatas.equals("follow")){
                deletefollow();
            }
        }
    });
}

private void startfollowing(){
    Mfollowdatabase.child(SenderUserID).child(ReciverUserID)
            .child("send").setValue("send")
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()){
                     Mfollowdatabase.child(ReciverUserID).child(SenderUserID)
                             .child("send")
                             .setValue("recived").addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 followstatas = "follow";
                                 followbutton.setBackgroundColor(Color.GREEN);
                                 followbutton.setText("Unfollow");

                             }
                         }
                     });
                 }
                }
            });
}

private void deletefollow(){
    Mfollowdatabase.child(SenderUserID).child(ReciverUserID)
            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            if(task.isSuccessful()){
                Mfollowdatabase.child(ReciverUserID).child(SenderUserID).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    followstatas = "No_Follow";
                                    followbutton.setBackgroundColor(Color.RED);

                                    followbutton.setText("follow");
                                }
                            }
                        });
            }
        }
    });
}

private void maintainfollow_button(){

    Mfollowdatabase.child(SenderUserID)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(ReciverUserID)){

                        String type = dataSnapshot.child(ReciverUserID).child("send").getValue().toString();
                        Toast.makeText(getApplicationContext(), type, Toast.LENGTH_LONG).show();

                        if(type.equals("send")){

                            followstatas = "follow";
                            followbutton.setBackgroundColor(Color.GREEN);
                            followbutton.setText("Unfollow");
                        }
                        else if(type.equals("recived")){
                            followstatas = "No_Follow";
                            followbutton.setBackgroundColor(Color.RED);

                            followbutton.setText("follow");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

}




}
