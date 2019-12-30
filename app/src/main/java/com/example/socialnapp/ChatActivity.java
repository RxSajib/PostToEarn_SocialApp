package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnapp.Common.Common;
import com.example.socialnapp.Model.Message;
import com.example.socialnapp.Model.Myresponce;
import com.example.socialnapp.Model.Notification;
import com.example.socialnapp.Model.Sender;
import com.example.socialnapp.Model.Token;
import com.example.socialnapp.Remote.APIservice;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ChatActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private TextView seenofuser;

    private static final String LOG_TAG = "recorade";
    private static final int MAX_LENGTH = 100;
    private EditText message;
    private ImageButton SendButton;
    private String ReciverUserID, SenderID, saveCurrentDate, saveCurrentTime, messagetext;
    private FirebaseAuth Mauth;
    private DatabaseReference Roodref;
    private RecyclerView MessageView;
    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private DatabaseReference MFriendDatabase;
    private Toolbar Mtoolbar;
    private TextView username;
    private CircleImageView mimage;
    private ImageView send_file;
    private String cheacker;
    private Uri picimage_uri;
    private UploadTask uploadtask;
    private String myurl;
    private File outFile;

    private String MfileName = null;
    private StorageReference MstoreAudio;
    ///////
    private MediaPlayer player;
    private MediaRecorder recorder;
    private String output_file;
    private int Counter;
    private MediaRecorder mRecorder;
    private String AudioDownloader;
    private DatabaseReference MuserDatabase;
    private String CurrentUserID;

    ///////

    String filename = null;
    private String fileName = null;
    private ImageButton sendvoidmessage_button;

    private StorageReference MuploadeAudio;
    private ProgressBar MessageProgress;
    // private MediaRecorder recorder;

    ////

    ////

    private String mFileName = null;
    private int i;
    private TextView recodtext;


    private APIservice mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mService= Common.getFCMClient();

        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        seenofuser = findViewById(R.id.LastSeenID);
        MessageProgress = findViewById(R.id.MessageProgressID);
        MessageProgress.setVisibility(View.INVISIBLE);
        recodtext = findViewById(R.id.RecodTextID);

        MuploadeAudio = FirebaseStorage.getInstance().getReference();
        sendvoidmessage_button = findViewById(R.id.SendVoiceButtonID);
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/recode_audio.3gp";
        recodtext.setVisibility(View.INVISIBLE);

        MstoreAudio = FirebaseStorage.getInstance().getReference().child("Audio");


        /////
        output_file = Environment.getExternalStorageState() + "/audiorecorder.3gp";
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording.3gp";
        /////



        send_file = findViewById(R.id.DocumentButtonID);
        username = findViewById(R.id.UsernameToolbarID);
        mimage = findViewById(R.id.FriendImageID);
        Mtoolbar = findViewById(R.id.ChatToolbarID);
        setSupportActionBar(Mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        MessageView = findViewById(R.id.RecID);
        MessageView.setHasFixedSize(true);
        MessageView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        messageAdapter = new MessageAdapter(messageList);
        MessageView.setAdapter(messageAdapter);




        Roodref = FirebaseDatabase.getInstance().getReference();
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        SenderID = Mauth.getCurrentUser().getUid();
        ReciverUserID = getIntent().getStringExtra("key");
        message = findViewById(R.id.MessageID);
        SendButton = findViewById(R.id.SendButtonID);
        MFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(ReciverUserID);

        Fatchvalue();

        send_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] charSequences = new CharSequence[]{
                        "image",
                        "Pdf File",
                        "Doc File"
                };

                android.app.AlertDialog.Builder Mbuilder = new android.app.AlertDialog.Builder(ChatActivity.this);
                Mbuilder.setTitle("Select Options");
                Mbuilder.setItems(charSequences, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (i == 0) {
                            cheacker = "image";
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, 511);
                        }
                        if (i == 1) {
                            cheacker = "pdf";
                            Intent intent = new Intent();
                            intent.setType("application/pdf");
                            startActivityForResult(intent, 511);
                        }
                        if (i == 2) {
                            cheacker = "msword";
                            Intent intent = new Intent();
                            intent.setType("application/msword");
                            startActivityForResult(intent, 511);
                        }
                    }

                });
                AlertDialog alertDialog = Mbuilder.create();
                alertDialog.show();
            }


        });


        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                messagetext = message.getText().toString();
                if (messagetext.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Input any message", Toast.LENGTH_LONG).cancel();
                } else {

                    sendingmessage();
                }
            }
        });


        sartLising_database();




        sendvoidmessage_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {


                    if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){

                        if(CheckPermissions()) {

                            MessageProgress.setVisibility(View.VISIBLE);
                            recodtext.setVisibility(View.VISIBLE);
                            Counter++;
                            mFileName += "/AudioRecording" + Counter + ".3gp";
                            start();

                        }
                    }
                    else if(CheckPermissions()) {
                        MessageProgress.setVisibility(View.VISIBLE);
                        recodtext.setVisibility(View.VISIBLE);
                        start();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    MessageProgress.setVisibility(View.GONE);
                    startstoping();

                }
                else {
                    RequestPermissions();
                }

                return true;
            }


        });



        MuserDatabase.child(ReciverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("user_statas")){
                    String statas = dataSnapshot.child("user_statas").child("statas").getValue().toString();

                    if(statas.equals("online")){

                        seenofuser.setText("online");
                    }
                     if(statas.equals("offline")){
                        String time = dataSnapshot.child("user_statas").child("time").getValue().toString();
                        Toast.makeText(getApplicationContext(), "time :"+time, Toast.LENGTH_LONG).show();
                        seenofuser.setText("last seen: "+time);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


   // i understand ohh my god


    private void sartLising_database() {

        MFriendDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("name")) {

                        String nameget = dataSnapshot.child("name").getValue().toString();
                        username.setText(nameget);

                    }

                    if (dataSnapshot.hasChild("image_uri")) {
                        String imageuriget = dataSnapshot.child("image_uri").getValue().toString();
                        Picasso.with(ChatActivity.this).load(imageuriget).placeholder(R.drawable.default_image).into(mimage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    protected void Fatchvalue() {

        Roodref.child("Messages").child(SenderID).child(ReciverUserID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Message message = dataSnapshot.getValue(Message.class);
                        messageList.add(message);
                        messageAdapter.notifyDataSetChanged();
                        MessageView.smoothScrollToPosition(messageAdapter.getItemCount());
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

        super.onStart();
    }

    ///sending text message
    private void sendingmessage() {

        String message_senderRef = "Messages/" + SenderID + "/" + ReciverUserID;
        String message_reciverRef = "Messages/" + ReciverUserID + "/" + SenderID;

        DatabaseReference user_message_key = Roodref.child("Message").child(SenderID).child(ReciverUserID)
                .push();

        final String message_pushID = user_message_key.getKey();

        ///date
        Calendar calendardate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
        String dateget = simpleDateFormat.format(calendardate.getTime());

        Calendar caltesttime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy (HH:mm:a)", Locale.getDefault());
        String caltime = sdf.format(caltesttime.getTime());

        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+ String.format("%02d" , c.get(Calendar.MINUTE))+":"+String.format("%02d" , c.get(Calendar.SECOND))+":"+String.format("%03d" , c.get(Calendar.MILLISECOND));


        ///time
        Calendar calendartime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        String timeget = simpleDateFormat1.format(calendartime.getTime());


        /// all time
        Calendar calendaralltime = Calendar.getInstance();
        SimpleDateFormat alltime_and_date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String all_date_and_time = alltime_and_date.format(calendaralltime.getTime());

        Log.e("all_time", all_date_and_time);

        /// all time

        ///reciver time
       // Calendar calendartimekoria = Calendar.getInstance();
       // SimpleDateFormat simpleDateFormatkoria = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.CANADA);
        //String koriatime = simpleDateFormatkoria.format(calendartimekoria.getTime());

        Date today = new Date();
        DateFormat df = new SimpleDateFormat("hh:mm a");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
        String IST = df.format(today);



        Toast.makeText(getApplicationContext(), "koria_time "+IST, Toast.LENGTH_LONG).show();
        ///reciver time

        ///// send notifaction
        find_username_and_send_notification();
        ///// send notifaction



        Map messagetext_body = new HashMap();
        messagetext_body.put("message", messagetext);
        messagetext_body.put("date", dateget);
       // messagetext_body.put("time", timeget);

        messagetext_body.put("time", all_date_and_time);
        messagetext_body.put("type", "text");
        messagetext_body.put("from", SenderID);

        ///
        messagetext_body.put("to", ReciverUserID);
        messagetext_body.put("message_PushID", message_pushID);

        TelephonyManager tm = (TelephonyManager)getSystemService(getApplication().TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        Toast.makeText(getApplicationContext(), countryCode, Toast.LENGTH_LONG).show();

        ///testing
        messagetext_body.put("sendertime", caltime);


        // messagetext_body.put("update_datefortesting", dateupdate);




        Map messagebody_details = new HashMap();
        messagebody_details.put(message_senderRef + "/" + message_pushID, messagetext_body);
        messagebody_details.put(message_reciverRef + "/" + message_pushID, messagetext_body);


        Roodref.updateChildren(messagebody_details).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()) {
                    MessageProgress.setVisibility(View.INVISIBLE);
                    message.setText("");




                } else {
                    MessageProgress.setVisibility(View.INVISIBLE);
                    String error = task.getException().getMessage().toString();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                message.setText("");
            }
        });


    }
    ///sending text message

    //// send notifaction
    private void find_username_and_send_notification(){


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if(dataSnapshot.exists()){

                    String name =dataSnapshot.child("name").getValue().toString();


                    if(!name.isEmpty()){


                        // Log.i(TAG, "onDataChange: ");

                        sendNotification(name);


                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    //// send notifaction


    /// send notifaction to another devices
    private void sendNotification(final String sendername) {



        FirebaseDatabase.getInstance().getReference("Token").child(ReciverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Token token=dataSnapshot.getValue(Token.class);


                String sendermessage= sendername;
                String title= message.getText().toString();


              /*  HashMap<String,String> data=new HashMap<>();
                data.put("title",title);
                data.put("body",message);*/


                com.example.socialnapp.Model.Notification notification=new Notification(sendermessage,title);
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
    /// send notifaction to another devices


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {


        if (requestCode == 511 && resultCode == RESULT_OK) {
            picimage_uri = data.getData();

            if (cheacker != "image") {


                Calendar calendar_date = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
                final String time = simpleDateFormat.format(calendar_date.getTime());

                ///time
                Calendar calendar_time = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
                final String date = currenttime.format(calendar_date.getTime());

                StorageReference Mstores = FirebaseStorage.getInstance().getReference().child("Send_document");
                final String message_senderRef = "Messages/" + SenderID + "/" + ReciverUserID;
                final String message_reciverRef = "Messages/" + ReciverUserID + "/" + SenderID;
                DatabaseReference user_message_key = Roodref.child("Message").child(SenderID).child(ReciverUserID)
                        .push();

                final String message_pushID = user_message_key.getKey();
                final StorageReference filepath = Mstores.child(message_pushID + "." + cheacker);
                filepath.putFile(picimage_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            /// copy pdf
                            Toast.makeText(ChatActivity.this, "upload is success", Toast.LENGTH_LONG).show();


                            Map messagetext_body = new HashMap();
                            messagetext_body.put("message", task.getResult().getDownloadUrl().toString());
                            messagetext_body.put("name", picimage_uri.getLastPathSegment());
                            messagetext_body.put("type", cheacker);
                            messagetext_body.put("from", SenderID);

                            ///
                            messagetext_body.put("to", ReciverUserID);
                            //   messagetext_body.put("message_PushID", message_pushID);
                            messagetext_body.put("time", time);
                            //      messagetext_body.put("date", date);


                            Map messagebody_details = new HashMap();
                            messagebody_details.put(message_senderRef + "/" + message_pushID, messagetext_body);
                            messagebody_details.put(message_reciverRef + "/" + message_pushID, messagetext_body);



                            Roodref.updateChildren(messagebody_details);
                        }

                    }
                });

            }
            if (cheacker.equals("image")) {


                StorageReference Mstores = FirebaseStorage.getInstance().getReference().child("Send_Image");
                final String message_senderRef = "Messages/" + SenderID + "/" + ReciverUserID;
                final String message_reciverRef = "Messages/" + ReciverUserID + "/" + SenderID;
                DatabaseReference user_message_key = Roodref.child("Message").child(SenderID).child(ReciverUserID)
                        .push();

                final String message_pushID = user_message_key.getKey();
                final StorageReference filepath = Mstores.child(message_pushID + ".jpg");


                uploadtask = filepath.putFile(picimage_uri);
                uploadtask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {

                            Uri downloadurl = task.getResult();
                            myurl = downloadurl.toString();


                            Calendar calendar_date = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
                            String date = simpleDateFormat.format(calendar_date.getTime());

                            ///time
                            Calendar calendar_time = Calendar.getInstance();
                            SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm aa", Locale.US);
                            final String time = currenttime.format(calendar_date.getTime());

                            ///hear copy code
                            Map messagetext_body = new HashMap();
                            messagetext_body.put("message", myurl);
                            messagetext_body.put("name", picimage_uri.getLastPathSegment());
                            messagetext_body.put("type", cheacker);
                            messagetext_body.put("from", SenderID);
                            messagetext_body.put("time", time);
                            messagetext_body.put("date", date);

                            Toast.makeText(ChatActivity.this, time, Toast.LENGTH_LONG).show();
                            ///
                            messagetext_body.put("to", ReciverUserID);
                            //   messagetext_body.put("message_PushID", message_pushID);

                            Map messagebody_details = new HashMap();
                            messagebody_details.put(message_senderRef + "/" + message_pushID, messagetext_body);
                            messagebody_details.put(message_reciverRef + "/" + message_pushID, messagetext_body);

                            Roodref.updateChildren(messagebody_details).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (task.isSuccessful()) {
                                        message.setText("");
                                    } else {
                                        String error = task.getException().getMessage().toString();
                                        Toast.makeText(ChatActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                    message.setText("");
                                }


                            });
                        }

                    }
                });

            } else {
                Toast.makeText(ChatActivity.this, "error", Toast.LENGTH_LONG).show();
            }


        }

    }


    private void start() {

        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mFileName);

            mRecorder.prepare();
            mRecorder.start();
            Toast.makeText(getApplicationContext(), "starting", Toast.LENGTH_LONG).show();
        }catch (IllegalStateException e) {
            Log.e("REDORDING :: ",e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("REDORDING :: ",e.getMessage());
            e.printStackTrace();
        }


    }

    private void startstoping() {
        Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_LONG).show();

        try {
            mRecorder.stop();
            mRecorder.release();
        savingData();
            mRecorder = null;
        } catch (RuntimeException e) {

       }


    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(ChatActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    private void savingData(){

     //   Toast.makeText(getApplicationContext(), "audio send", Toast.LENGTH_LONG).show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z", Locale.getDefault());
        String Time_Date = simpleDateFormat.format(calendar.getTime());

        recodtext.setVisibility(View.GONE);
        StorageReference Mstores = MstoreAudio.child("AudioFile"+Time_Date+".3gp");
        Uri uri = Uri.fromFile(new File(mFileName));

        Mstores.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful()){

                    DatabaseReference user_message_key = Roodref.child("Message").child(SenderID).child(ReciverUserID)
                            .push();
                    String message_senderRef = "Messages/" + SenderID + "/" + ReciverUserID;
                    String message_reciverRef = "Messages/" + ReciverUserID + "/" + SenderID;
                    final String message_pushID = user_message_key.getKey();



                    Calendar calendar_date = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");
                    String date = simpleDateFormat.format(calendar_date.getTime());

                    ///time
                    Calendar calendar_time = Calendar.getInstance();
                    SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                    final String time = currenttime.format(calendar_date.getTime());


                    Map messagetext_body = new HashMap();
                    messagetext_body.put("message", task.getResult().getDownloadUrl().toString());
              //      messagetext_body.put("name", picimage_uri.getLastPathSegment());
                    messagetext_body.put("type", "Audio");
                    messagetext_body.put("from", SenderID);
                    messagetext_body.put("time", time);
                    messagetext_body.put("date", date);

                    Toast.makeText(ChatActivity.this, time, Toast.LENGTH_LONG).show();
                    ///
                    messagetext_body.put("to", ReciverUserID);



                    Map messagebody_details = new HashMap();
                    messagebody_details.put(message_senderRef + "/" + message_pushID, messagetext_body);
                    messagebody_details.put(message_reciverRef + "/" + message_pushID, messagetext_body);

                    Roodref.updateChildren(messagebody_details).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()) {
                                message.setText("");
                            } else {
                                String error = task.getException().getMessage().toString();
                                Toast.makeText(ChatActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                            message.setText("");
                        }


                    });


                }
                else{
                    String errormessage = task.getException().getMessage().toString();
                    Toast.makeText(getApplicationContext(), errormessage, Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


}

