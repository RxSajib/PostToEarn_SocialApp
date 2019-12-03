package com.example.socialnapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.reflect.MalformedParametersException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar profiletoolbar;
    private TextInputLayout name, whatsapp, Emailaddress,  birth, username;
    private TextInputLayout relanship, idnumber, country, joindate, work;
    private Button UpdateButton;
    private CircleImageView profileimage;
    private Uri imaggeuri;
    private DatabaseReference MuserDatabase;
    private String CurrentuserID;
    private FirebaseAuth Mauth;
    private ProgressBar Mprogress;
    private StorageReference MprofileStore;
    private String downloaduri = "";
    private String Currenttime, Currentdate;
    private TextView protoolbarname;
    private Switch switchbutton;
    private FirebaseAuth MMauth;
    private String MCurrentUserID;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String gender = "", countryget, birthtext;
    private ImageView dateimage, join;
    private DatabaseReference MpermissionData;
    private DatePickerDialog datepicker_diolog;
    private RadioButton male, femail, others;
    private String code = "";
    private TextInputLayout codetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        male = findViewById(R.id.MaleID);
        femail = findViewById(R.id.FemailID);
        others = findViewById(R.id.OthersID);
        codetext = findViewById(R.id.CodeLayoutID);


        MMauth = FirebaseAuth.getInstance();
        birth = findViewById(R.id.BirthInput);
        radioGroup = findViewById(R.id.ReadioGroupID);
        dateimage = findViewById(R.id.DateImageID);
        dateimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = new DatePicker(getApplicationContext());
                int currentmonth = datePicker.getMonth() + 1;
                int currentday = datePicker.getDayOfMonth();
                int currentyer = datePicker.getYear();



                datepicker_diolog = new DatePickerDialog(ProfileActivity.this

                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        birth.getEditText().setText(i+"-"+i1+1+"-"+i2);

                    }
                }, currentday, currentyer, currentmonth
                );

                datepicker_diolog.show();

            }


        });

        join = findViewById(R.id.JoinCalenderID);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = new DatePicker(getApplicationContext());
                int currentmonth = datePicker.getMonth() + 1;
                int currentday = datePicker.getDayOfMonth();
                int currentyer = datePicker.getYear();



                datepicker_diolog = new DatePickerDialog(ProfileActivity.this

                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        joindate.getEditText().setText(i+"-"+i1+1+"-"+i2);

                    }
                }, currentday, currentyer, currentmonth
                );

                datepicker_diolog.show();


            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int radioposition = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioposition);

                 gender = radioButton.getText().toString();
            }
        });


        MCurrentUserID = MMauth.getCurrentUser().getUid();

        MpermissionData = FirebaseDatabase.getInstance().getReference().child("Permission").child("permission");

        switchbutton = findViewById(R.id.SwitchID);

        switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {

                if(ischecked){

                    MpermissionData.child("permission").push().setValue(MCurrentUserID);

                }
                else {
                    MpermissionData.child("permission").setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }
                        }
                    });
                }

            }
        });



        protoolbarname = findViewById(R.id.ProfilenameToolbar);
        profiletoolbar  = findViewById(R.id.ProfileToolbarID);
        MprofileStore = FirebaseStorage.getInstance().getReference();
        setSupportActionBar(profiletoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setTitle("Setup Profile First");
        Mauth = FirebaseAuth.getInstance();
        CurrentuserID = Mauth.getCurrentUser().getUid();
        Mprogress = findViewById(R.id.ProfileProgressID);
        Mprogress.setVisibility(View.INVISIBLE);
        username = findViewById(R.id.fullnamelayout);
        country = findViewById(R.id.CountryID);
        work = findViewById(R.id.WorkID);


        profileimage = findViewById(R.id.ProfileImageIDID);
        name = findViewById(R.id.namelayout);
        whatsapp = findViewById(R.id.MobID);
        Emailaddress = findViewById(R.id.emaillayoutID);
        relanship = findViewById(R.id.Relanship);
        idnumber = findViewById(R.id.IDNumberID);
        joindate = findViewById(R.id.JoinDateID);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentuserID);
        protoolbarname.setText("Profile");
        UpdateButton = findViewById(R.id.UpdateButtonID);



       onCheackIteams();
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileActivity.this);
            }
        });




        catch_allinfo();

    }

    private void catch_allinfo(){

        MuserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("name")){
                        String nameget = dataSnapshot.child("name").getValue().toString();
                        name.getEditText().setText(nameget);
                        protoolbarname.setText(nameget+" 's Profile");
                    }

                    if(dataSnapshot.hasChild("sex")){
                        String sexget = dataSnapshot.child("sex").getValue().toString();

                        if(sexget.equals("Male")){
                            male.setChecked(true);
                        }
                        else if(sexget.equals("Femail")){
                            femail.setChecked(true);
                        }
                        else if(sexget.equals("Others")){
                            others.setChecked(true);
                        }
                    }

                    if(dataSnapshot.hasChild("birth")){
                        String birthget = dataSnapshot.child("birth").getValue().toString();
                        birth.getEditText().setText(birthget);
                    }
                    if(dataSnapshot.hasChild("country")){
                        String dworkget = dataSnapshot.child("country").getValue().toString();
                        work.getEditText().setText(dworkget);
                    }
                    if(dataSnapshot.hasChild("number_code")){
                        String number_codeget = dataSnapshot.child("number_code").getValue().toString();
                        codetext.getEditText().setText(number_codeget);
                    }
                    if(dataSnapshot.hasChild("username")){
                        String usernameget =dataSnapshot.child("username").getValue().toString();
                        username.getEditText().setText(usernameget);
                    }
                    if(dataSnapshot.hasChild("work")){
                         countryget = dataSnapshot.child("work").getValue().toString();
                        country.getEditText().setText(countryget);
                    }
                    if(dataSnapshot.hasChild("email_address")){
                        String email_addressget = dataSnapshot.child("email_address").getValue().toString();
                        Emailaddress.getEditText().setText(email_addressget);
                    }
                    if(dataSnapshot.hasChild("id_number")){
                        String id_numberget = dataSnapshot.child("id_number").getValue().toString();
                        idnumber.getEditText().setText(id_numberget);
                    }
                    if(dataSnapshot.hasChild("join_date")){
                        String join_dateget = dataSnapshot.child("join_date").getValue().toString();
                        joindate.getEditText().setText(join_dateget);
                    }
                    if(dataSnapshot.hasChild("relanship")){
                        String relanshipget = dataSnapshot.child("relanship").getValue().toString();
                        relanship.getEditText().setText(relanshipget);
                    }

                    if(dataSnapshot.hasChild("whatsApp_number")){
                        String whatsApp_numberget = dataSnapshot.child("whatsApp_number").getValue().toString();
                        whatsapp.getEditText().setText(whatsApp_numberget);
                    }
                    if(dataSnapshot.hasChild("image_uri")){

                        String imageget = dataSnapshot.child("image_uri").getValue().toString();
                        Picasso.with(ProfileActivity.this).load(imageget).placeholder(R.drawable.default_profileimage).into(profileimage);
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Mprogress.setVisibility(View.VISIBLE);
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 imaggeuri = result.getUri();

                 StorageReference filepath = MprofileStore.child("Profile_Image").child(imaggeuri.getLastPathSegment());
                 filepath.putFile(imaggeuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                         if(task.isSuccessful()){
                             downloaduri = task.getResult().getDownloadUrl().toString();

                             Map usermap = new HashMap<>();
                            usermap.put("image_uri", downloaduri);
                            MuserDatabase.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if(task.isSuccessful()){
                                        Mprogress.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Mprogress.setVisibility(View.INVISIBLE);
                                }
                            });

                         }
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {

                         Mprogress.setVisibility(View.INVISIBLE);
                     }
                 });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void onCheackIteams(){
        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nametext = name.getEditText().getText().toString();
                String whatapptext = whatsapp.getEditText().getText().toString();
                String emailtext = Emailaddress.getEditText().getText().toString();

                String relanshiptext = relanship.getEditText().getText().toString();
                String idnumbertext = idnumber.getEditText().getText().toString();
                String countrytext = country.getEditText().getText().toString();
                String jointext = joindate.getEditText().getText().toString();
                 birthtext = birth.getEditText().getText().toString();

                String worktext = work.getEditText().getText().toString();
                String usernametext = username.getEditText().getText().toString().trim();
                code = codetext.getEditText().getText().toString();

                ///speace username is fixed now

                if(nametext.isEmpty()){
                    name.setError("Name empty");
                }
                else if(birthtext.isEmpty()){
                    birth.setError("Date empty");
                }
                else if(whatapptext.isEmpty()){
                    whatsapp.setError("WhatsApp empty");
                }
                else if(emailtext.isEmpty()){
                    Emailaddress.setError("Email empty");
                }

              //  else if(relanshiptext.isEmpty()){
             //       relanship.setError("Relanship empty");
              //  }
             //   else if(idnumbertext.isEmpty()){
             //       idnumber.setError("ID empty");
             //   }
                else if(countrytext.isEmpty()){
                    country.setError("Country empty");
                }


               else if(usernametext.isEmpty()){
                    username.setError("UserName empty");
                }
               else if(worktext.isEmpty()){
                    work.setError("work empty");
                }
               else if(gender==""){
                    Toast.makeText(getApplicationContext(), "gender empty", Toast.LENGTH_LONG).show();
                }
               else if(code.isEmpty()){
                   country.getEditText().setError("code require");
                }

                else {



                    Mprogress.setVisibility(View.VISIBLE);
                    Toast.makeText(ProfileActivity.this, "ok", Toast.LENGTH_LONG).show();

                    Calendar calendartime = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                    Currenttime = simpleDateFormat.format(calendartime.getTime());

                    Calendar calendardate = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    Currentdate = simpleDateFormat1.format(calendardate.getTime());


                    ////////////////
                    Calendar calendartimeacc = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormatacc = new SimpleDateFormat("hhmm");
                    String accounttime = simpleDateFormatacc.format(calendartime.getTime());

                    Calendar calendardateac = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat1ac = new SimpleDateFormat("yyyyMMdd-HHmmss");
                    String accountdate = simpleDateFormat1ac.format(calendardate.getTime());

                    //////////////////

                    Map usermap = new HashMap<>();
                    usermap.put("name", "("+nametext+")");
                    usermap.put("username", usernametext);
                    usermap.put("work", countrytext);
                   // usermap.put("profile_image", downloaduri);
                    usermap.put("whatsApp_number", whatapptext);
                    usermap.put("email_address", emailtext);

                    usermap.put("birth", birthtext);
                    usermap.put("relanship", relanshiptext);
                    usermap.put("id_number", idnumbertext);
                    usermap.put("join_date", jointext);
                    usermap.put("country", worktext);
                    usermap.put("number_code", code);
                    usermap.put("time", Currenttime);
                    usermap.put("date", Currentdate);
                    usermap.put("join_dateupdate", accountdate);

                    usermap.put("id_no", CurrentuserID);
                    usermap.put("sex", gender);
                    usermap.put("devices_token", FirebaseInstanceId.getInstance().getToken().toString());
                    usermap.put("search", nametext.toLowerCase());

                    MuserDatabase.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if(task.isSuccessful()){
                                Mprogress.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "data is updated", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else {
                                String errormessage = task.getException().getMessage().toString();
                                Toast.makeText(getApplicationContext(), errormessage, Toast.LENGTH_LONG).show();
                                Mprogress.setVisibility(View.INVISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            String err = e.getMessage().toString();
                            Toast.makeText(ProfileActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
