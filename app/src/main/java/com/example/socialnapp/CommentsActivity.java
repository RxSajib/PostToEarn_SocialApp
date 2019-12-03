package com.example.socialnapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity {


    private EditText comments_text;
    private ImageButton commentbutton;
    private DatabaseReference UserRef, Postrf;
    private String CurrentUserID;
    private FirebaseAuth Mauth;
    private String PostKey;
    private RecyclerView MlistView;
    private Toolbar commentToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentToolbar = findViewById(R.id.CommentToolbarID);
        setSupportActionBar(commentToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        PostKey = getIntent().getStringExtra("key");
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        comments_text = findViewById(R.id.CommentsTextID);
        commentbutton = findViewById(R.id.CommentsSendID);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef.keepSynced(true);
        Postrf = FirebaseDatabase.getInstance().getReference().child("Post").child(PostKey).child("Comments");
        Postrf.keepSynced(true);
        MlistView = findViewById(R.id.CommentRecylearViewID);
        MlistView.setHasFixedSize(true);
        MlistView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        commentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserRef.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("name")){
                            String nameget = dataSnapshot.child("name").getValue().toString();

                            valid_comment(nameget);
                            comments_text.setText("");
                        }


                    }

                    private void valid_comment(String nameget) {

                        String commentinputtext = comments_text.getText().toString();
                        if(commentinputtext.isEmpty()){
                            Toast.makeText(CommentsActivity.this, "input any comment", Toast.LENGTH_LONG).show();
                        }
                        else {

                            Calendar calendartime = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("hh:mm a");
                           final String time = simpleDateFormattime.format(calendartime.getTime());

                            Calendar calendardate = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("dd-MMMM-yyyy");
                           final String date = simpleDateFormatdate.format(calendardate.getTime());


                           final String RandomKey =CurrentUserID+ date+time;

                            HashMap commentsmap = new HashMap();
                            commentsmap.put("userID", CurrentUserID);
                            commentsmap.put("comments_text", commentinputtext);
                            commentsmap.put("date", date);
                            commentsmap.put("time", time);
                            commentsmap.put("name", nameget);

                            Postrf.child(RandomKey).updateChildren(commentsmap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(CommentsActivity.this, "sending...", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                String error = task.getException().getMessage().toString();
                                                Toast.makeText(CommentsActivity.this, error, Toast.LENGTH_LONG).show();
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

        FirebaseRecyclerAdapter<CommentModal, CommentsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CommentModal, CommentsViewHolder>(
                CommentModal.class,
                R.layout.sample_comment_layout,
                CommentsViewHolder.class,
                Postrf

        ) {
            @Override
            protected void populateViewHolder(CommentsViewHolder commentsViewHolder, CommentModal commentModal, int i) {

                commentsViewHolder.setnameset(commentModal.getName());
                commentsViewHolder.setCommentsset(commentModal.getComments_text());
                commentsViewHolder.setdateset(commentModal.getDate());
                commentsViewHolder.settimeset(commentModal.getTime());
            }
        };

        super.onStart();
        MlistView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder{

        private TextView name, date, time, comments;
        private View Mview;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            Mview = itemView;
            name = Mview.findViewById(R.id.CnameID);
            date = Mview.findViewById(R.id.Cdate);
            time = Mview.findViewById(R.id.CTime);
            comments = Mview.findViewById(R.id.CComment);
        }

        public void setnameset(String nam){
            name.setText(nam);
        }
        public void setdateset(String dat){
            date.setText(dat);
        }
        public void settimeset(String tim){
            time.setText(tim);
        }
        public void setCommentsset(String com){
            comments.setText(com);
        }
    }
}
