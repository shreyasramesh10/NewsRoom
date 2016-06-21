package com.android.shreyas.newsroom;

/**
 * Created by SHREYAS on 4/28/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.android.shreyas.newsroom.models.ChatMessage;
import com.android.shreyas.newsroom.models.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


//Class extending service as it is a service that will run in background
public class NotificationListener extends Service {

    User user = new User();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    //When the service is started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Firebase firebase = new Firebase("https://popping-inferno-9534.firebaseio.com/chat/");
        if(firebase.getAuth()!=null){
            user.setUserName((String) firebase.getAuth().getProviderData().get("displayName"));
            user.setEmail((String) firebase.getAuth().getProviderData().get("email"));
            user.setDisplayImageURL((String) firebase.getAuth().getProviderData().get("profileImageURL"));
        }
        //Adding a valueevent listener to firebase
        //this will help us to  track the value changes on firebase

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                ChatMessage movie = snapshot.getValue(ChatMessage.class);
                //System.out.println(movie.getName() + " - " + movie.getMessage());


                ChatMessage cm = snapshot.getValue(ChatMessage.class);
                String author = cm.getName();

                if(ChatActivity.count>=1){

                }
                else{
                    if(author!=null && author.equals(user.getUserName()) ){
                        //Toast.makeText(act,"Same",Toast.LENGTH_SHORT).show();
                    }
                    else {
                      showNotification(cm);
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return START_STICKY;
    }


    private void showNotification(ChatMessage cm){


        Intent resultIntent = new Intent(this, ChatActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, resultIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder noti_builder = new NotificationCompat.Builder(this);
        noti_builder.setContentIntent(pIntent)
                .setSmallIcon(R.drawable.hot).setContentTitle(cm.getName()).setContentText(cm.getMessage())
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //what does this do!?

        notificationManager.notify(1, noti_builder.build());
    }
}