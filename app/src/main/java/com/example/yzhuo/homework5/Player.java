package com.example.yzhuo.homework5;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;

/**
 * Created by yzhuo on 10/18/2015.
 */
public class Player {

    RelativeLayout rl;
    Button button;
    ProgressBar pb;
    String MP3URL;
    Context mContext;
    boolean playPause;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean intialStage = true;

    public void gone(){
        rl.setVisibility(View.GONE);
    }

    public  void visible(){
        rl.setVisibility(View.VISIBLE);
    }

    public void jumpEnd(){
        mediaPlayer.seekTo(mediaPlayer.getDuration()-1000);
    }
    public String getMP3URL() {
        return MP3URL;
    }

    public void stop(){
        rl.setVisibility(View.GONE);
        button.setBackgroundResource(R.drawable.ic_action_play);
        pb.setProgress(0);
        mediaPlayer.reset();
    }

    public void kill(){
        mediaPlayer.stop();
    }

    public void setMP3URL(String MP3URL) {
        this.MP3URL = MP3URL;
    }

    Player(RelativeLayout rl, Button button, ProgressBar pb, Context mContext){
        this.rl=rl;
        this.button=button;
        this.pb=pb;
        this.mContext=mContext;
    }

    public void streaming(){
        stop();
        visible();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        button.setOnClickListener(pausePlay);


    }

    private View.OnClickListener pausePlay = new View.OnClickListener() {



        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // TODO Auto-generated method stub

            if (!playPause) {
                button.setBackgroundResource(R.drawable.ic_action_pause);
                if (intialStage) {
                    new download()
                            .execute(MP3URL);
                }
                else {
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                    new progressBarUpdate().execute();
                }
                playPause = true;
            } else {
                button.setBackgroundResource(R.drawable.ic_action_play);
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                playPause = false;
            }
        }

        class download extends AsyncTask<String, Void, Boolean> {
            private ProgressDialog progress;

            @Override
            protected Boolean doInBackground(String... params) {
                // TODO Auto-generated method stub
                Boolean prepared;
                try {

                    mediaPlayer.setDataSource(params[0]);

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            intialStage = true;
                            playPause = false;
                            button.setBackgroundResource(R.drawable.ic_action_play);
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            rl.setVisibility(View.GONE);
                        }
                    });
                    mediaPlayer.prepare();
                    prepared = true;
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    Log.d("IllegarArgument", e.getMessage());
                    prepared = false;
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    prepared = false;
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    prepared = false;
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    prepared = false;
                    e.printStackTrace();
                }
                return prepared;
            }


            @Override
            protected void onPostExecute(Boolean result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (progress.isShowing()) {
                    progress.cancel();
                }
                Log.d("Prepared", "//" + result);
                mediaPlayer.start();
                new progressBarUpdate().execute();
                intialStage = false;
            }

            public download() {
                progress = new ProgressDialog(mContext);
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                this.progress.setMessage("Buffering...");
                this.progress.show();

            }
        }

    };

    private class progressBarUpdate extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onProgressUpdate(Integer... values) {

            Log.d("Current:", "" +mediaPlayer.getCurrentPosition()+"/"+mediaPlayer.getDuration());
            pb.setMax(mediaPlayer.getDuration());
            pb.setProgress(mediaPlayer.getCurrentPosition());
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (mediaPlayer.isPlaying()){

                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(0);
            }
            return null;
        }
    }

}
