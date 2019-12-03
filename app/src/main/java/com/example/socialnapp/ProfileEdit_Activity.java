package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEdit_Activity extends AppCompatActivity {

    private CircleImageView proimage;
    private TextView username;
    private FirebaseAuth Mauth;
    private DatabaseReference MuserDatabase;
    private String CurrentUserID;

    private FloatingActionButton updatebutton;
    private TextInputLayout fullname,number, birth,  address, relanship, idno, joindate, country,  work;
    private RadioGroup radioGroup;


    private ProgressDialog Mprogress;
    private ImageView dateofbirth, joincalender;
    private DatePickerDialog datepicker_diolog;
    private RadioButton radioButton;
    private String gender = "";
    private RadioButton malebutton, femailbutton, othersbutton;

    private TextView joindatenotedit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_);


        joindatenotedit = findViewById(R.id.EditJoinDateID);


        malebutton = findViewById(R.id.MaleButtonID);
        femailbutton = findViewById(R.id.FemailButtonID);
        othersbutton = findViewById(R.id.OthersButtonID);

        radioGroup = findViewById(R.id.RadioGroupID);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int position = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(position);
                gender = radioButton.getText().toString();

            }
        });


        proimage = findViewById(R.id.EditImageID);
        username = findViewById(R.id.EditUserName);
        fullname = findViewById(R.id.FullnameID);
        number = findViewById(R.id.MobileNumberID);

        Mprogress = new ProgressDialog(getApplicationContext());

        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        MuserDatabase.keepSynced(true);
        updatebutton = findViewById(R.id.ItemsUpdateButtonID);
        birth = findViewById(R.id.DateOFBirthID);
        address = findViewById(R.id.EmailAddressID);
        relanship = findViewById(R.id.RelationshipID);
        idno = findViewById(R.id.IDNOID);
        joindate = findViewById(R.id.JoinDateIDID);
        country = findViewById(R.id.CountryIDID);
        work = findViewById(R.id.WorkIDID);



        readdata();

        fatchUserInfo();

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItems();
            }
        });


        dateofbirth = findViewById(R.id.DateOfBirthButtonID);
        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = new DatePicker(getApplicationContext());
                int currentmonth = datePicker.getMonth() + 1;
                int currentday = datePicker.getDayOfMonth();
                int currentyer = datePicker.getYear();



                datepicker_diolog = new DatePickerDialog(ProfileEdit_Activity.this

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

        joincalender = findViewById(R.id.JoinCalenderIDID);
        joincalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = new DatePicker(getApplicationContext());
                int currentmonth = datePicker.getMonth() + 1;
                int currentday = datePicker.getDayOfMonth();
                int currentyer = datePicker.getYear();



                datepicker_diolog = new DatePickerDialog(ProfileEdit_Activity.this

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
    }

    private void readdata(){
        MuserDatabase.child(CurrentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("birth")){
                                String birthget = dataSnapshot.child("birth").getValue().toString();
                                birth.getEditText().setText(birthget);
                            }
                            if(dataSnapshot.hasChild("work")){
                                String countryget = dataSnapshot.child("work").getValue().toString();
                                country.getEditText().setText(countryget);
                            }
                            if(dataSnapshot.hasChild("email_address")){
                                String email_addressget = dataSnapshot.child("email_address").getValue().toString();
                                address.getEditText().setText(email_addressget);
                            }

                            if(dataSnapshot.hasChild("id_number")){
                                String id_numberget = dataSnapshot.child("id_number").getValue().toString();
                                idno.getEditText().setText(id_numberget);
                            }
                            if(dataSnapshot.hasChild("join_dateupdate")){
                                String join_dateupdateget = dataSnapshot.child("join_dateupdate").getValue().toString();
                                joindate.getEditText().setText(join_dateupdateget);

                                joindatenotedit.setText("Joined: "+join_dateupdateget);
                            }


                            if(dataSnapshot.hasChild("relanship")){
                                String relanshipget = dataSnapshot.child("relanship").getValue().toString();
                                relanship.getEditText().setText(relanshipget);
                            }
                            if(dataSnapshot.hasChild("sex")){
                                String sexget = dataSnapshot.child("sex").getValue().toString();
                                if(sexget.equals("Male")){
                                    malebutton.setChecked(true);
                                }
                                 else  if(sexget.equals("Femail")){
                                    femailbutton.setChecked(true);
                                }
                                else if(sexget.equals("Others")){
                                    othersbutton.setChecked(true);
                                }
                            }

                            if(dataSnapshot.hasChild("country")){
                                String workget = dataSnapshot.child("country").getValue().toString();
                                work.getEditText().setText(workget);
                            }
                           if(dataSnapshot.hasChild("whatsApp_number")){
                               String whatsApp_numberget = dataSnapshot.child("whatsApp_number").getValue().toString();
                               number.getEditText().setText(whatsApp_numberget);
                           }
                           if(dataSnapshot.hasChild("name")){
                               String fullnameget = dataSnapshot.child("name").getValue().toString();
                               fullname.getEditText().setText(fullnameget);
                           }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void fatchUserInfo(){
        MuserDatabase.child(CurrentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            if(dataSnapshot.hasChild("image_uri")){
                                String image_uriget = dataSnapshot.child("image_uri").getValue().toString();
                                Picasso.with(getApplicationContext()).load(image_uriget).placeholder(R.drawable.default_profileimage).into(proimage);
                            }
                            if(dataSnapshot.hasChild("username")){
                                String nameget  = dataSnapshot.child("username").getValue().toString();
                                username.setText(nameget);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void updateItems(){

        Mprogress.setTitle("Please wait");
        Mprogress.setMessage("Please wait update is progressing ...");
        Mprogress.setCancelable(false);
//        Mprogress.show();

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String birthitesm = birth.getEditText().getText().toString();
                String addresstext = address.getEditText().getText().toString();
                String relanshiptext = relanship.getEditText().getText().toString();
                String idnotext = idno.getEditText().getText().toString();
                String jointext = joindate.getEditText().getText().toString();
                String countryget = country.getEditText().getText().toString();
                String workget = work.getEditText().getText().toString();
                String numbertget = number.getEditText().getText().toString();
                String fullnametext = fullname.getEditText().getText().toString();


                if(birthitesm.isEmpty()){
                    Mprogress.dismiss();
                    birth.setError("birth empty");
                }
              else if(gender == ""){
                  Toast.makeText(getApplicationContext(), "select gender", Toast.LENGTH_LONG).show();
              }
                else if(addresstext.isEmpty()){
                    Mprogress.dismiss();
                    address.setError("address empty");
                }
          //    else  if(relanshiptext.isEmpty()){
           //         Mprogress.dismiss();
           //         relanship.setError("relanship empty");
           //     }
           //   else  if(idnotext.isEmpty()){
            //        Mprogress.dismiss();
            //        idno.setError("ID not found");
            //    }
          //     else if(jointext.isEmpty()){
          //          Mprogress.dismiss();
           //         joindate.setError("Date empty");
           //     }
              else  if(countryget.isEmpty()){
                    Mprogress.dismiss();
                    country.setError("Work empty");
                }

              else  if(workget.isEmpty()){
                    Mprogress.dismiss();
                    work.setError("country empty");
                }
              else  if(numbertget.isEmpty()){
                    number.setError("number empty");
                }
              else  if(fullnametext.isEmpty()){
                    fullname.getEditText().setError("name empty");
                }


                else {

                    Map usermap = new HashMap();
                    usermap.put("birth", birthitesm);
                    usermap.put("country", workget);
                    usermap.put("join_date", jointext);
                    usermap.put("email_address", addresstext);
                    usermap.put("id_number", idnotext);
                    usermap.put("relanship", relanshiptext);
                    usermap.put("work", countryget);
                    usermap.put("whatsApp_number", numbertget);
                    usermap.put("name", fullnametext);
                    usermap.put("sex", gender);

                    MuserDatabase.child(CurrentUserID).updateChildren(usermap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        Mprogress.dismiss();
                                        Toast.makeText(getApplicationContext(), "update success", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Mprogress.dismiss();
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Mprogress.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                }


            }
        });
    }
}
