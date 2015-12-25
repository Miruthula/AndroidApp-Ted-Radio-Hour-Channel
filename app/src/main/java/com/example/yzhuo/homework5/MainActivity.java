package com.example.yzhuo.homework5;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static String PODCAST_URL = "http://www.npr.org/rss/podcast.php?id=510298";
    final static String TITLE = "title";
    final static String IMAGE = "image";
    final static String DESC = "description";
    final static String PUBDATE = "date";
    final static String DURATION = "duration";
    final static String MP3FILE = "mp3";
    final static String START_STREAMING = "ready";
    Boolean inGrid = false;
    LinearLayoutManager llm;
    GridLayoutManager glm;
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    GridAdapter gridAdapter;
    ArrayList<Item> mItems;
    Toolbar toolbar;
    Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);


        RelativeLayout rl = (RelativeLayout) findViewById(R.id.playStatus);
        Button button = (Button) findViewById(R.id.playButton);
        ProgressBar pb = (ProgressBar) findViewById(R.id.mainProgressBar);
        pb.setVisibility(View.VISIBLE);

        player = new Player(rl, button, pb, MainActivity.this);

        mItems = new ArrayList<>();

        //list view
        recyclerView = (RecyclerView) findViewById(R.id.my_RV);

        llm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);


        if(isConnected()) {
            new downLoad().execute(PODCAST_URL);
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
        }





    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.gridOrList){
            if(!inGrid) {
                inGrid = true;
                //grid view
                glm = new GridLayoutManager(this,2);
                recyclerView.setLayoutManager(glm);
                gridAdapter = new GridAdapter(MainActivity.this,player, mItems);
                recyclerView.setAdapter(gridAdapter);
                item.setIcon(R.drawable.ic_stat_list);
            } else {
                inGrid = false;
                recyclerView.setLayoutManager(llm);
                listAdapter = new ListAdapter(MainActivity.this,player, mItems);
                recyclerView.setAdapter(listAdapter);
                item.setIcon(R.drawable.ic_stat_grid);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class downLoad extends AsyncTask <String, Void, ArrayList<Item>>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Downloading...");
            progressDialog.show();
        }


        @Override
        protected ArrayList<Item> doInBackground(String... params) {
            try {

                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statusCode = con.getResponseCode();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = con.getInputStream();
                    return ItemParsingXML.PodcastParser.itemsXMLArrayList(in);
                }

            } catch (MalformedURLException e) {
                Log.d("URL error", params[0]);
            } catch (IOException e) {
                Log.d("open connection error","");
            } catch (SAXException e) {
                Log.d("Parse Error","");
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
            progressDialog.dismiss();
            mItems = items;
            listAdapter = new ListAdapter(MainActivity.this,player,items);
            recyclerView.setAdapter(listAdapter);
        }

    }
}
