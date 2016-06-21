package com.android.shreyas.newsroom;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.shreyas.newsroom.adapters.ChatAdapter;
import com.android.shreyas.newsroom.fragments.DbRecyclerViewFragment;
import com.android.shreyas.newsroom.fragments.RecyclerViewHomeFragment;
import com.android.shreyas.newsroom.fragments.ViewPagerFragment;
import com.android.shreyas.newsroom.models.ChatMessage;
import com.android.shreyas.newsroom.models.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity{

    Firebase mref;
    Activity act = this;
    protected Toolbar toolbar;
    int position;
    List<Map<String, ?>> chatList;
    static int count=0;

    @Override
    protected void onNewIntent(Intent intent) {
        //Batch.onNewIntent(this, intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onStop() {
        //Batch.onStop(this);
        count=0;
        super.onStop();
    }

    User user = new User();

    ChatAdapter mAdapter;
    private static final String FIREBASE_URL = "https://popping-inferno-9534.firebaseio.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        count++;
        chatList = new ArrayList<Map<String,?>>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        startService(new Intent(this, NotificationListener.class));
        mref = new Firebase(FIREBASE_URL).child("chat");

        final RecyclerView recycler = (RecyclerView) findViewById(R.id.cardList);
        recycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recycler.setLayoutManager(llm);
        if(mref.getAuth()!=null){
            user.setUserName((String) mref.getAuth().getProviderData().get("displayName"));
            user.setEmail((String) mref.getAuth().getProviderData().get("email"));
            user.setDisplayImageURL((String) mref.getAuth().getProviderData().get("profileImageURL"));
        }

        llm.setReverseLayout(true);

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                ChatMessage chat = snapshot.getValue(ChatMessage.class);

                chatList.add(position,createMovie(chat.getName(), chat.getMessage(),chat.getImageUrl()));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                chatList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mAdapter = new ChatAdapter(this, chatList);
        recycler.setAdapter(mAdapter);

        final EditText mMessage = (EditText) findViewById(R.id.messageInput);
        ImageButton sendbutton = (ImageButton) findViewById(R.id.sendButton);
        if(sendbutton!=null){
            sendbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mref.push().setValue(new ChatMessage(user.getUserName(), mMessage.getText().toString(), user.getDisplayImageURL()));
                    mMessage.setText("");
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        //Batch.onDestroy(this);
        count=0;
        super.onDestroy();
    }

    private HashMap createMovie(String name, String message, String url) {
        HashMap movie = new HashMap();

        movie.put("name", name);
        movie.put("message",message);
        movie.put("url", url);
        return movie;
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        //Batch.onStart(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
           Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
