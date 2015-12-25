package com.example.yzhuo.homework5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PlayActivity extends AppCompatActivity {

    String title;
    String imageURL;
    String desc;
    String date;
    String dur;
    String mp3URL;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.playStatus);
        Button button = (Button) findViewById(R.id.playButton);
        ProgressBar pb = (ProgressBar) findViewById(R.id.mainProgressBar);
        rl.setVisibility(View.VISIBLE);
        Button play = (Button) findViewById(R.id.playButton);

        title = getIntent().getExtras().getString(MainActivity.TITLE);
        imageURL = getIntent().getExtras().getString(MainActivity.IMAGE);
        desc = getIntent().getExtras().getString(MainActivity.DESC);
        date = getIntent().getExtras().getString(MainActivity.PUBDATE);
        dur = getIntent().getExtras().getString(MainActivity.DURATION);
        mp3URL = getIntent().getExtras().getString(MainActivity.MP3FILE);

        ((TextView)findViewById(R.id.play_title)).setText(title);
        if(imageURL!=null){
        Picasso.with(PlayActivity.this)
                .load(imageURL)
                .into(((ImageView) findViewById(R.id.play_image)));
        } else {
            ((ImageView) findViewById(R.id.play_image)).setImageResource(R.drawable.ic_stat_list);
        }
        ((TextView)findViewById(R.id.play_description)).setText(getString(R.string.desc) + desc);
        ((TextView)findViewById(R.id.play_date)).setText(getString(R.string.pubdate)+date);
        ((TextView)findViewById(R.id.play_duration)).setText(getString(R.string.dur)+dur);


        player = new Player(rl, button, pb, PlayActivity.this);
        player.setMP3URL(mp3URL);
        player.streaming();
    }

    @Override
    public void onBackPressed() {
        player.kill();
        finish();
    }
}
