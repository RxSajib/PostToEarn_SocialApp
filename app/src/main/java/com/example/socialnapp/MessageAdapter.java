package com.example.socialnapp;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnapp.Model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessaageViewHolder> {

    private List<Message> usermessageList;
    private FirebaseAuth Mauth;
    private DatabaseReference usersdatabaseref;
    private Context context;

    public MessageAdapter(List<Message> usermessageList){
        this.usermessageList = usermessageList;
    }

    @NonNull
    @Override
    public MessageAdapter.MessaageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_placse, parent, false);
        Mauth = FirebaseAuth.getInstance();
        return new MessaageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.MessaageViewHolder holder, final int position) {

        final String messagesenderID = Mauth.getCurrentUser().getUid();
        final Message usermessagelist = usermessageList.get(position);
        String fromuserId = usermessagelist.getFrom();
        String frommesstype = usermessagelist.getType();

        usersdatabaseref = FirebaseDatabase.getInstance().getReference().child("Users").child(fromuserId);
        usersdatabaseref.keepSynced(true);
        usersdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.hasChild("image_uri")){
                        String image = dataSnapshot.child("image_uri").getValue().toString();
                        Picasso.with(holder.reciverprofileimage.getContext()).load(image).placeholder(R.drawable.default_image).into(holder.reciverprofileimage);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.recivermessagetext.setVisibility(View.GONE);
        holder.reciverprofileimage.setVisibility(View.GONE);
        ///
        holder.sendermessagetext.setVisibility(View.GONE);
        holder.SenderImage.setVisibility(View.GONE);
        holder.ReciverImage.setVisibility(View.GONE);
        holder.voicelayout.setVisibility(View.GONE);
        holder.recivervoice.setVisibility(View.GONE);
        holder.reciverVoiceButton.setVisibility(View.GONE);
      //  holder.sendervoiceime.setVisibility(View.GONE);
        holder.recivervoicetime.setVisibility(View.GONE);
        holder.rctime.setVisibility(View.GONE);

        holder.sendervoicetime.setVisibility(View.GONE);
        holder.sendervoivoicedate.setVisibility(View.GONE);

        holder.recivervoicetie.setVisibility(View.GONE);
        holder.recivervoicedate.setVisibility(View.GONE);

       // holder.message_reciverPic.setVisibility(View.GONE);
      //  holder.message_senderPic.setVisibility(View.GONE);

        holder.sendtexttime.setVisibility(View.GONE);

        if(frommesstype.equals("text")){

            holder.recivervoicetie.setVisibility(View.GONE);
            holder.recivervoicedate.setVisibility(View.GONE);

            holder.sendervoicetime.setVisibility(View.GONE);
            holder.sendervoivoicedate.setVisibility(View.GONE);

//            holder.sendervoiceime.setVisibility(View.INVISIBLE);
            holder.recivervoicetime.setVisibility(View.INVISIBLE);
            holder.recivervoice.setVisibility(View.INVISIBLE);
            holder.reciverVoiceButton.setVisibility(View.INVISIBLE);

            holder.playbutton.setVisibility(View.INVISIBLE);
            holder.voicelayout.setVisibility(View.INVISIBLE);
            holder.reciver_time.setVisibility(View.INVISIBLE);
            ///
            if(fromuserId.equals(messagesenderID)){
                holder.sendtexttime.setVisibility(View.VISIBLE);
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.senderimage_time.setVisibility(View.GONE);
//                holder.senderimage_time.setVisibility(View.GONE);
                holder.sendermessagetext.setVisibility(View.VISIBLE);
              //  holder.sendermessagetext.setBackgroundResource(R.drawable.sender_message_design);
                holder.linearLayout.setBackgroundResource(R.drawable.sender_message_design);
                holder.sendermessagetext.setTextColor(Color.WHITE);
                holder.sendermessagetext.setGravity(Gravity.LEFT);
                holder.sendermessagetext.setText(usermessagelist.getMessage());
                holder.sendtexttime.setText(usermessagelist.getTime()+"-"+usermessagelist.getDate());
//                holder.reciver_time.setVisibility(View.GONE);
            }
            else {
                holder.rctime.setVisibility(View.VISIBLE);
                holder.reclayoyt.setVisibility(View.VISIBLE);
                holder.playbutton.setVisibility(View.GONE);
                holder.reciver_time.setVisibility(View.GONE);
                holder.senderimage_time.setVisibility(View.GONE);
                //    holder.sendermessagetext.setVisibility(View.VISIBLE); ///in
                holder.recivermessagetext.setVisibility(View.VISIBLE); //vis
                holder.reciverprofileimage.setVisibility(View.VISIBLE); //vis
                holder.reciver_time.setVisibility(View.GONE);
                holder.reclayoyt.setBackgroundResource(R.drawable.reciver_message_design);
                holder.recivermessagetext.setTextColor(Color.BLACK);
                holder.recivermessagetext.setGravity(Gravity.LEFT);
                holder.recivermessagetext.setText(usermessagelist.getMessage());
                holder.rctime.setText(usermessagelist.getTime()+"-"+usermessagelist.getDate());
            }
        }





             else if(frommesstype.equals("image")) {

            holder.recivervoicetie.setVisibility(View.GONE);
            holder.recivervoicedate.setVisibility(View.GONE);

            holder.sendervoicetime.setVisibility(View.GONE);
            holder.sendervoivoicedate.setVisibility(View.GONE);

                  holder.rctime.setVisibility(View.GONE);
                  holder.reclayoyt.setVisibility(View.INVISIBLE);
                 holder.sendtexttime.setVisibility(View.INVISIBLE);
                 holder.linearLayout.setVisibility(View.INVISIBLE);

                 holder.recivervoicetime.setVisibility(View.INVISIBLE);
         //   holder.sendervoiceime.setVisibility(View.INVISIBLE);

            holder.recivervoice.setVisibility(View.INVISIBLE);
            holder.reciverVoiceButton.setVisibility(View.INVISIBLE);


                if (fromuserId.equals(messagesenderID)) {
                    holder.playbutton.setVisibility(View.INVISIBLE);
                    holder.voicelayout.setVisibility(View.INVISIBLE);
                    holder.reciver_time.setVisibility(View.INVISIBLE);


                    holder.reciver_time.setVisibility(View.VISIBLE);
                    holder.reciver_time.setText(usermessagelist.getTime()+" "+usermessagelist.getDate());
                    holder.SenderImage.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(usermessagelist.getMessage()).resize(150, 150).into(holder.SenderImage);


                    holder.SenderImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            android.app.AlertDialog.Builder Mbuilder = new android.app.AlertDialog.Builder(holder.itemView.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                            view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.image_view, null, false);
                            final ImageView imageView = view.findViewById(R.id.imageIDv);
                            Picasso.with(context).load(usermessagelist.getMessage()).into(imageView);

                            ZoomControls zoomControls = view.findViewById(R.id.ZoomControlsID);
                            zoomControls.setWeightSum(100);
                            zoomControls.setIsZoomInEnabled(true);
                            zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    float x = imageView.getScaleX();
                                    float y = imageView.getScaleY();
                                    imageView.setScaleX((float) (x + 1));
                                    imageView.setScaleY((float) (y + 1));

                                }
                            });

                            zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    float x = imageView.getScaleX();
                                    float y = imageView.getScaleY();

                                    imageView.setScaleX((float) (x - 1));
                                    imageView.setScaleY((float) (y - 1));
                                }
                            });

                            Mbuilder.setView(view);
                            AlertDialog alertDialog = Mbuilder.create();
                            alertDialog.show();
                        }
                    });

                } else {
                    holder.playbutton.setVisibility(View.INVISIBLE);
                    holder.voicelayout.setVisibility(View.INVISIBLE);

                    holder.senderimage_time.setVisibility(View.VISIBLE);
                    holder.senderimage_time.setText(usermessagelist.getTime()+" "+usermessagelist.getDate());
                    holder.ReciverImage.setVisibility(View.VISIBLE);
                    holder.reciverprofileimage.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(usermessagelist.getMessage()).resize(150, 150).into(holder.ReciverImage);


                  holder.ReciverImage.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {

                          android.app.AlertDialog.Builder Mbuilder = new android.app.AlertDialog.Builder(holder.itemView.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                          view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.image_view, null, false);

                          final ImageView imageView = view.findViewById(R.id.imageIDv);
                          Picasso.with(holder.itemView.getContext()).load(usermessagelist.getMessage()).into(imageView);


                          ZoomControls zoomControls = view.findViewById(R.id.ZoomControlsID);
                          zoomControls.setWeightSum(100);
                          zoomControls.setIsZoomInEnabled(true);

                          zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {

                                  float x = imageView.getScaleX();
                                  float y = imageView.getScaleY();
                                  imageView.setScaleX((float) (x + 0.2));
                                  imageView.setScaleY((float) (y + 0.2));



                              }
                          });

                          zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  float x = imageView.getScaleX();
                                  float y = imageView.getScaleY();


                                      imageView.setScaleX((float) (x - 0.2));
                                      imageView.setScaleY((float) (y - 0.2));

                              }
                          });

                          Mbuilder.setView(view);
                          AlertDialog alertDialog = Mbuilder.create();
                         alertDialog.show();

                      }
                  });
                }

            }

             else if(frommesstype.equals("Audio")){



                 holder.reclayoyt.setVisibility(View.INVISIBLE);
                 holder.sendtexttime.setVisibility(View.INVISIBLE);
                 holder.linearLayout.setVisibility(View.INVISIBLE);
                 holder.rctime.setVisibility(View.GONE);

                 if(fromuserId.equals(messagesenderID)){

                     holder.sendervoicetime.setVisibility(View.VISIBLE);
                     holder.sendervoivoicedate.setVisibility(View.VISIBLE);
                     holder.sendervoicetime.setText(usermessagelist.getTime());
                     holder.sendervoivoicedate.setText(usermessagelist.getDate());

                     holder.playbutton.setBackgroundResource(R.drawable.fuckplay);
                     holder.recivervoicetime.setVisibility(View.INVISIBLE);
                     holder.playbutton.setVisibility(View.VISIBLE);
                     holder.voicelayout.setVisibility(View.VISIBLE);
//                     holder.sendervoiceime.setVisibility(View.VISIBLE);

                     holder.recivervoice.setVisibility(View.INVISIBLE);
                     holder.reciverVoiceButton.setVisibility(View.INVISIBLE);
//                     holder.sendervoiceime.setText(usermessagelist.getTime());
               //      holder.audiosender_date.setText(usermessagelist.date);



                     holder.playbutton.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {

                           /*  MediaPlayer mediaPlayer = new MediaPlayer();
                             mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                             try {

                                 mediaPlayer.setDataSource(usermessagelist.getMessage());
                                 mediaPlayer.prepare();
                                 mediaPlayer.start();

                             }
                             catch (Exception e){

                             }
                             */
                             holder. mediaPlayer = new MediaPlayer();
                           holder.  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


                             if (holder.mediaPlayer != null &&holder. mediaPlayer.isPlaying()){
                                 holder.mediaPlayer.stop();
                                holder. mediaPlayer.release();
                                 holder.playbutton.setBackgroundResource(R.drawable.play_button);


                             }


                             else {
                                 holder.mediaPlayer = new MediaPlayer();
                                 try {

                                    holder. mediaPlayer.setDataSource(usermessagelist.getMessage());

                                    holder. mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    holder. mediaPlayer.prepare();



                                 } catch (IOException e) {
                                     e.printStackTrace();

                                 }
                              holder.   mediaPlayer.start();
                                 holder.playbutton.setBackgroundResource(R.drawable.play_button);



                             }


                         }
                     });

                 }
                 else {

                     holder.recivervoicetie.setVisibility(View.VISIBLE);
                     holder.recivervoicedate.setVisibility(View.VISIBLE);

                     holder.reciverprofileimage.setVisibility(View.VISIBLE);
                     holder.recivervoicetime.setVisibility(View.VISIBLE);
                     holder.playbutton.setVisibility(View.INVISIBLE);
                     holder.voicelayout.setVisibility(View.INVISIBLE);
              //       PlayRecVioiceID
                     holder.recivervoice.setVisibility(View.VISIBLE);
                     holder.reciverVoiceButton.setVisibility(View.VISIBLE);
                     holder.recivervoicetime.setText(usermessagelist.getTime());
                 //    holder.audioreciver_date.setText(usermessagelist.date);

                     holder.recivervoicetie.setText(usermessagelist.getTime());
                     holder.recivervoicedate.setText(usermessagelist.getDate());



                     holder.reciverVoiceButton.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {








                             if(holder.CurrenPosttion.equals("notplay")) {

                                 startplaying();
                             }

/*
                             try {

                                 mediaPlayer.setDataSource(usermessagelist.getMessage());
                                 mediaPlayer.prepare();
                                 mediaPlayer.start();
                             }
                             catch (Exception e){

                             }

 */
                         }

                         private void startplaying() {

                             MediaPlayer mediaPlayer = new MediaPlayer();
                             mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                             if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                 mediaPlayer.stop();
                                 mediaPlayer.release();
                                 holder.playbutton.setBackgroundResource(R.drawable.play_button);
                             } else {
                                 mediaPlayer = new MediaPlayer();
                                 try {

                                     mediaPlayer.setDataSource(usermessagelist.getMessage());

                                     mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                     mediaPlayer.prepare();
                                 } catch (IOException e) {
                                     e.printStackTrace();

                                 }
                                 mediaPlayer.start();
                                 holder.playbutton.setBackgroundResource(R.drawable.play_button);


                             }
                         }


                     });

                 }
        }


             else {

                 holder.SenderImage.setVisibility(View.INVISIBLE);
                 holder.ReciverImage.setVisibility(View.INVISIBLE);


            if(fromuserId.equals(messagesenderID)){

                holder.SenderImage.setVisibility(View.VISIBLE);
                holder.SenderImage.setImageResource(R.drawable.pdfmonu);
                holder.reciver_time.setText(usermessagelist.getTime());


            }
            else {
                holder.senderimage_time.setText(usermessagelist.getTime());
                holder.reciverprofileimage.setVisibility(View.VISIBLE);
                holder.ReciverImage.setVisibility(View.VISIBLE);
                holder.ReciverImage.setImageResource(R.drawable.pdfmonu);
            }

        }
    }


    @Override
    public int getItemCount() {
        return usermessageList.size();
    }

    public class MessaageViewHolder extends RecyclerView.ViewHolder {

        public TextView sendermessagetext, recivermessagetext;
        public CircleImageView reciverprofileimage;
        public TextView senderimage_time, reciver_time;
        public ImageView SenderImage, ReciverImage;


        //sender
        public ImageView playbutton;
        public RelativeLayout voicelayout;
        public TextView sendervoiceime;

        ///reciver
        public RelativeLayout recivervoice;
        private ImageView reciverVoiceButton;
        private TextView recivervoicetime;
        private CircleImageView recivervoiceimage;
        private String CurrenPosttion = "notplay";
        private MediaPlayer mediaPlayer = null;
   //     private TextView audioreciver_date, audiosender_date;



        private TextView sendtexttime, rctime;
        private RelativeLayout linearLayout, reclayoyt;


        ///voice date time
        private TextView sendervoicetime, sendervoivoicedate;
        ///voice date time

        ///voice reciver date time
        private TextView recivervoicetie, recivervoicedate;
        ///voice reciver date time



        public MessaageViewHolder(@NonNull View itemView) {
            super(itemView);

            ///voice reciver
            recivervoicetie = itemView.findViewById(R.id.ReciverVoiceTimeID);
            recivervoicedate = itemView.findViewById(R.id.ReciverVoiceDateID);
            ///voice reciver

            ////voicd
            sendervoicetime = itemView.findViewById(R.id.SederVoiceTimeID)  ;
             sendervoivoicedate  = itemView.findViewById(R.id.SenderVoiceDateID) ;

            ////voicd

            rctime = itemView.findViewById(R.id.ReciverTimetTimeID);
            reclayoyt = itemView.findViewById(R.id.ReciverLayoutID);
            sendtexttime = itemView.findViewById(R.id.SendertextTimeID);

            sendermessagetext = itemView.findViewById(R.id.SenderMessageID);
            recivermessagetext = itemView.findViewById(R.id.ReciverMessageID);
            reciverprofileimage = itemView.findViewById(R.id.ReciverImageID);
            reciver_time = itemView.findViewById(R.id.SenderTimeID);
       //     senderimage_time = itemView.findViewById(R.id.ImageTimeID);
            SenderImage = itemView.findViewById(R.id.SenderMessageImageID);
            ReciverImage = itemView.findViewById(R.id.ReciverMessageImageID);
            senderimage_time = itemView.findViewById(R.id.ImageTimeID);
            reciver_time = itemView.findViewById(R.id.SenderTimeID);
            voicelayout = itemView.findViewById(R.id.VoiceViewID);
         //   sendervoiceime = itemView.findViewById(R.id.SederVoiceTimeID);

            playbutton = itemView.findViewById(R.id.PlayButtonID);
            recivervoiceimage = itemView.findViewById(R.id.ReciverAudioID);

            recivervoice = itemView.findViewById(R.id.VoiceRecliView);
            reciverVoiceButton = itemView.findViewById(R.id.PlayRecVioiceID);

            recivervoicetime = itemView.findViewById(R.id.ReciverVoiceTimeID);

         //   audioreciver_date = itemView.findViewById(R.id.ReciverVoiceDateID);
       //     audiosender_date = itemView.findViewById(R.id.SenderVoiceDateID);

            linearLayout = itemView.findViewById(R.id.layoutIDID);

        }
    }
}
