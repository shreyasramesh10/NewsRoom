package com.android.shreyas.newsroom;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.shreyas.newsroom.fragments.DbRecyclerViewFragment;
import com.android.shreyas.newsroom.fragments.ListBuddiesFragment;
import com.android.shreyas.newsroom.fragments.MyProfileFragment;
import com.android.shreyas.newsroom.fragments.RecyclerViewFragment;
import com.android.shreyas.newsroom.fragments.RecyclerViewHomeFragment;
import com.android.shreyas.newsroom.fragments.SearchRecyclerViewFragment;
import com.android.shreyas.newsroom.fragments.SettingsFragment;
import com.android.shreyas.newsroom.fragments.ViewPagerFragment;
import com.android.shreyas.newsroom.fragments.WeatherDetailFragment;
import com.android.shreyas.newsroom.models.Result;
import com.android.shreyas.newsroom.models.User;
import com.android.shreyas.newsroom.models.weathermodel.Weather;
import com.baoyz.widget.PullRefreshLayout;
import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RecyclerViewHomeFragment.OnEachCardSelectedonHomeListener,
        RecyclerViewHomeFragment.onRefreshListenerCustom,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        WeatherDetailFragment.onRefreshListenerWeather{
    protected Toolbar toolbar;
    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    GoogleApiClient googleApiClient;

    User user = new User();
    Firebase ref = new Firebase("https://popping-inferno-9534.firebaseio.com");


    Fragment mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            buildAlertForNoGps();
        }

        //Location
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        //gpsTracker = new GPSTracker2(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(ref.getAuth()!=null){
            user.setUserName((String) ref.getAuth().getProviderData().get("displayName"));
            user.setEmail((String) ref.getAuth().getProviderData().get("email"));
            user.setDisplayImageURL((String) ref.getAuth().getProviderData().get("profileImageURL"));
        }

        View view =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)view.findViewById(R.id.name);
        nav_user.setText(user.getUserName());

        TextView email = (TextView )view.findViewById(R.id.email);
        email.setText(user.getEmail());
        CircleImageView dp = (CircleImageView )view.findViewById(R.id.dp);
        Picasso.with(getApplicationContext()).load(user.getDisplayImageURL())
                .into(dp);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //deprecated setDrawerListener()
        //drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,RecyclerViewHomeFragment.newInstance())
                .commit();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                mContent = RecyclerViewHomeFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, RecyclerViewHomeFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.item2:
                mContent = ViewPagerFragment.newInstance("topstories");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ViewPagerFragment.newInstance("topstories"))
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.item3:
                mContent = ViewPagerFragment.newInstance("popular");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ViewPagerFragment.newInstance("popular"))
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.item4:
                if(ref.getAuth()!=null){
                    Intent chatActivity = new Intent(this,ChatActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                                .makeSceneTransitionAnimation(this, navigationView, "animate");
                        this.startActivity(chatActivity, optionsCompat.toBundle());
                    }
                    else
                        startActivity(chatActivity);
                    //finish();
                }
                else {
                    Intent loginActivity = new Intent(this, LoginActivity.class);
                    loginActivity.putExtra("activity","Chat");
                    startActivity(loginActivity);
                    finish();
                }
                break;
            case R.id.item5:
                mContent = DbRecyclerViewFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, DbRecyclerViewFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.item6:
                mContent = MyProfileFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MyProfileFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.item7:
                if(ref.getAuth()!=null) {
                    Intent intent = new Intent(this, UserStoriesActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                                .makeSceneTransitionAnimation(this, navigationView, "animate");
                        this.startActivity(intent, optionsCompat.toBundle());
                    } else
                        startActivity(intent);
                }
                else {
                    Intent loginActivity = new Intent(this, LoginActivity.class);
                    loginActivity.putExtra("activity","Notes");
                    startActivity(loginActivity);
                    finish();
                }
                break;
            case R.id.item8:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SettingsFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.item9:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ListBuddiesFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                mContent = RecyclerViewHomeFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, RecyclerViewHomeFragment.newInstance())
                        .commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
    }

//    @Override
//    public void OnEachCardSelected(int position, Result result) {
//    }

    @Override
    public void OnEachCardSelectedonHome() {
        mContent = WeatherDetailFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, WeatherDetailFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnSearch(String query) {
        mContent = SearchRecyclerViewFragment.newInstance(query);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SearchRecyclerViewFragment.newInstance(query))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

//    @Override
//    public void OnEachCardSelectedinSearch(int position, HashMap<String, ?> news) {
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("news", (String)news.get("url"));
//        startActivity(intent);
//    }

    @Override
    public void onRefreshCallFromHome(PullRefreshLayout layout) {
        mContent  = RecyclerViewHomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, RecyclerViewHomeFragment.newInstance())
                .addToBackStack(null)
                .commit();
        Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefreshCallWeather(PullRefreshLayout layout) {
        mContent  = WeatherDetailFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, WeatherDetailFragment.newInstance()).addToBackStack(null)
                .commit();
    }


////////////////////////////////////////////////////////////////////////////////////////////


    public void buildAlertForNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Current Location required for Weather information!");
        builder.setMessage("Would you like to enable GPS?")
                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                        @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    LocationRequest locationRequest;
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(20000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable r = new Runnable() {
            public void run() {
                stopLocationUpdates();
                locationRequest.setInterval(1000 * 1000);
                locationRequest.setFastestInterval(500 * 1000);
                locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                startLocationUpdates();
            }
        };
        handler.postDelayed(r, (10 * 1000));

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        //finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    Location lastLocation;
    static int count=0;
    @Override
    public void onConnected(Bundle connectionHint) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        if (lastLocation != null) {
            Weather.lat = String.valueOf(lastLocation.getLatitude());
            Weather.lon =String.valueOf(lastLocation.getLongitude());
            if(count==0){
                count++;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,RecyclerViewHomeFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }

        }

        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && (location != lastLocation)) {
            if(Weather.lat.equals("") && Weather.lon.equals("")){
                Weather.lat = ""+location.getLatitude();
                Weather.lon = ""+location.getLongitude();
            }
            lastLocation = location;
        }
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (googleApiClient.isConnected())
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(googleApiClient, locationRequest,this);
    }

    protected void stopLocationUpdates() {
        if (googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            mResolvingError = true;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, result.getErrorCode());
            } catch (IntentSender.SendIntentException e) {
                googleApiClient.connect();
            }

        } else {
            final ConnectionResult res = result;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            } else {

            }
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    showErrorDialog(res.getErrorCode());
                }
            });

            mResolvingError = true;
        }
    }

    /*
     * Error handling for onConnectionFailed
     *
     */
    // The rest of this code is all about building the error dialog
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();

        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        final ErrorDialogFragment temp = dialogFragment;
        //dialogFragment.show(getFragmentManager(), "errordialog");
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                temp.show(getSupportFragmentManager(), DIALOG_ERROR);
            }
        });
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }


    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity) getActivity()).onDialogDismissed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_RESOLVE_ERROR:
                mResolvingError = false;
                if (resultCode == RESULT_OK) {
                    // Make sure the app is not already connected or attempting to connect
                    if (!googleApiClient.isConnecting() &&
                            !googleApiClient.isConnected()) {
                        googleApiClient.connect();
                    }
                }
                break;
        }
    }

}
