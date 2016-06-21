package com.android.shreyas.newsroom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.shreyas.newsroom.fragments.DetailUserStoriesFragment;
import com.android.shreyas.newsroom.fragments.EditFragment;
import com.android.shreyas.newsroom.fragments.UserStoriesFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SHREYAS on 4/19/2016.
 */
public class UserStoriesActivity extends AppCompatActivity implements UserStoriesFragment.OnListItemSelectedListener {

    protected Toolbar toolbar;
    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stories);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, UserStoriesFragment.newInstance())
                .commit();
    }

    @Override
    public void onListItemSelected(View view, HashMap<String, ?> movie) {
        DetailUserStoriesFragment detailUserStoriesFragment = DetailUserStoriesFragment.newInstance(movie);
        detailUserStoriesFragment.setSharedElementEnterTransition(new DetailsTransition());
        detailUserStoriesFragment.setEnterTransition(new Fade());
        detailUserStoriesFragment.setExitTransition(new Fade());
        detailUserStoriesFragment.setSharedElementReturnTransition(new DetailsTransition());
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportFragmentManager().beginTransaction()
                    .addSharedElement(view, view.getTransitionName())
                    .replace(R.id.container, detailUserStoriesFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailUserStoriesFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void editItem(HashMap<String, ?> story) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, EditFragment.newInstance(story))
                .commit();
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
    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
    }
}
