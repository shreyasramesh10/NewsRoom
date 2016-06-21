package com.android.shreyas.newsroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.shreyas.newsroom.fragments.MyProfileFragment;
import com.android.shreyas.newsroom.models.User;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class LoginActivity extends FirebaseLoginBaseActivity {

    Firebase firebaseRef;
    EditText userNameET;
    EditText passwordET;
    String mName;
    String userEmail;
    String displayPicURL;

    User user = new User();
    //ArrayList<User> userDetails = new ArrayList<>();
    /* String Constants */
    private static final String FIREBASEREF = "https://popping-inferno-9534.firebaseio.com";
    private static final String FIREBASE_ERROR = "Firebase Error";
    private static final String USER_ERROR = "User Error";
    private static final String LOGIN_SUCCESS = "Login Success";
    private static final String USER_CREATION_SUCCESS =  "Successfully created user";
    private static final String USER_CREATION_ERROR =  "User creation error";
    private static final String EMAIL_INVALID =  "email is invalid :";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseRef = new Firebase(FIREBASEREF);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // userNameET = (EditText)findViewById(R.id.edit_text_email);
       // passwordET = (EditText)findViewById(R.id.edit_text_password);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.showFirebaseLoginPrompt();
            }
        });

       // Button createButton = (Button) findViewById(R.id.button);
//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createUser();
//            }
//        });
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {
        Snackbar snackbar = Snackbar.
                make(userNameET, FIREBASE_ERROR + firebaseLoginError.message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        resetFirebaseLoginPrompt();
    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {
        Snackbar snackbar = Snackbar
                .make(userNameET, USER_ERROR + firebaseLoginError.message, Snackbar.LENGTH_SHORT);
        snackbar.show();
        resetFirebaseLoginPrompt();
    }

    @Override
    public Firebase getFirebaseRef() {
        firebaseRef = new Firebase(FIREBASEREF);
        //mref = new Firebase(FIREBASE_URL).child("chat");
        return firebaseRef;
    }

    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        switch (authData.getProvider()) {
            case "password":
                mName = (String) authData.getProviderData().get("email");
                user.setUserName(mName);
                user.setEmail(userEmail);
                user.setDisplayImageURL((String) authData.getProviderData().get("profileImageURL"));
                break;
            default:
                mName = (String) authData.getProviderData().get("displayName");
                user.setUserName(mName);
                user.setEmail((String) authData.getProviderData().get("email"));
                user.setDisplayImageURL((String) authData.getProviderData().get("profileImageURL"));
                break;
        }

        //((Chats1) this.getApplication()).setLoggedIn(true);

        Toast.makeText(getApplicationContext(), LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
        Log.d("AuthData: ", user.getUserName() + user.getEmail() + user.getDisplayImageURL());
        String toActivity="";
        if(getIntent()!=null) {
            toActivity = getIntent().getStringExtra("activity");
        }
        switch (toActivity){
            case "Splash":
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent);
                finish();
                break;
            case "Chat":
                Intent myIntent2 = new Intent(LoginActivity.this, ChatActivity.class);
                LoginActivity.this.startActivity(myIntent2);
                finish();
                break;
            case "User Stories":
                Intent myIntent3 = new Intent(LoginActivity.this, UserStoriesActivity.class);
                LoginActivity.this.startActivity(myIntent3);
                finish();
                break;
            case "My Profile":
                Intent myIntent5 = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent5);
                finish();
                break;
            case "Notes":
                Intent myIntent6 = new Intent(LoginActivity.this, UserStoriesActivity.class);
                LoginActivity.this.startActivity(myIntent6);
                finish();
                break;
            default:
                Intent myIntent4 = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(myIntent4);
                finish();
                break;
        }

    }

    @Override
    public void onFirebaseLoggedOut() {
        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //setEnabledAuthProvider(AuthProviderType.PASSWORD);
        setEnabledAuthProvider(AuthProviderType.GOOGLE);
        setEnabledAuthProvider(AuthProviderType.FACEBOOK);
    }

    // Validate email address for new accounts.
    private boolean isEmailValid(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            userNameET.setError(EMAIL_INVALID + email);
            return false;
        }
        return true;
    }

    // create a new user in Firebase
    public void createUser() {
        if(userNameET.getText() == null ||  !isEmailValid(userNameET.getText().toString())) {
            return;
        }
        firebaseRef.createUser(userNameET.getText().toString(), passwordET.getText().toString(),
                new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_SUCCESS, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                Snackbar snackbar = Snackbar.make(userNameET, USER_CREATION_ERROR, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
