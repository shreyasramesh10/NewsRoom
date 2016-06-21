package com.android.shreyas.newsroom;

/**
 * Created by SHREYAS on 4/17/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.shreyas.newsroom.models.Result;
import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailViewActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

TextView byline;
    ImageView land;
    CircleImageView circle;
    TextView description;
    TextView title;
    TextView tag;
    FloatingActionButton fab;

    Result res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        //mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        fab=(FloatingActionButton)findViewById(R.id.share);
        CardView cardView = (CardView) findViewById(R.id.detailCard);
        CardView cardView2 = (CardView) findViewById(R.id.detailCard2);
        TextView section = (TextView) findViewById(R.id.section);
        TextView date = (TextView) findViewById(R.id.date);

        res = (Result) getIntent().getSerializableExtra("result");
        TextView titleone = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.descriptionCard);
        circle = (CircleImageView) findViewById(R.id.circleIv);
        land = (ImageView) findViewById(R.id.main_imageview_placeholder);
        title = (TextView) findViewById(R.id.titlecard);

       // tag = (TextView) findViewById(R.id.tag);
        byline = (TextView) findViewById(R.id.byline);
        final PropertyAction fabAction = PropertyAction.newPropertyAction(fab).
                scaleX(0).
                scaleY(0).
                duration(750).
                interpolator(new AccelerateDecelerateInterpolator()).
                build();
        final PropertyAction headerAction = PropertyAction.newPropertyAction(mAppBarLayout).
                interpolator(new DecelerateInterpolator()).
                translationY(-200).
                duration(550).
                alpha(0.4f).
                build();
        final PropertyAction bottomAction = PropertyAction.newPropertyAction(cardView).
                translationY(500).
                duration(550).
                alpha(0f).
                build();
        final PropertyAction bottomAction2 = PropertyAction.newPropertyAction(cardView2).
                translationY(500).
                duration(550).
                alpha(0f).
                build();
        Player.init().
                animate(headerAction).
                then().
                animate(fabAction).
                then().
                animate(bottomAction).then().animate(bottomAction2).
                play();

        title.setText( Html.fromHtml(res.getTitle()).toString());
        description.setText( Html.fromHtml(res.getAbstract()).toString());
        section.setText("Section: " + res.getSection());
        date.setText("Published: " +res.getPublishedDate().substring(0,10));

        byline.setText(res.getByline());
        if(res.getMedia().size()==0 && res.getMultimedia().size()!=0) {
            Picasso.with(this).load(res.getMultimedia().get(1).getUrl())
                    .placeholder(R.drawable.nophotoavailable)
                    .into(circle);
        } else if (res.getMultimedia().size()==0 && res.getMedia().size()!=0) {
            Picasso.with(this).load(res.getMedia().get(0).getMediaMetadata().get(1).getUrl())
                    .placeholder(R.drawable.nophotoavailable)
                    .into(circle);
        } else {
            circle.setImageResource(R.drawable.nophotoavailable);
        }
        land.setImageResource(R.drawable.detail_land);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent=new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody=res.getShortUrl();
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,res.getTitle());
                sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailViewActivity.this, WebViewActivity.class);
                intent.putExtra("news", res.getUrl());
                startActivity(intent);
            }
        });

        Button button = (Button) findViewById(R.id.gotoWeb);
        if(button!=null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailViewActivity.this, WebViewActivity.class);
                    intent.putExtra("news", res.getUrl());
                    startActivity(intent);
                }
            });
        }

    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
                //fab.setVisibility(View.INVISIBLE);
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
                //fab.setVisibility(View.VISIBLE);
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
                fab.setVisibility(View.INVISIBLE);
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
                fab.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

}

