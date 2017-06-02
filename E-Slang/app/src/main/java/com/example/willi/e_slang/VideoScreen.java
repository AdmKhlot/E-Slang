package com.example.willi.e_slang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.support.multidex.*;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;

/**
 * Created by willi on 02/06/2017.
 */

public class VideoScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    DbManager dbm;
    Cursor cursor;
    String flag;
    String word;
    int i=0;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    public String url;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();
    }

    protected void onStart() {
        super.onStart();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        //getSupportActionBar().setTitle("Videos");

        dbm = DbManager.getInstance();
        dbm.mCtx = this;
        dbm.open();

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        word = intent.getStringExtra("word");

        cursor = dbm.getOneWord(flag, word);
        if (cursor.moveToFirst()){
            do{
                url = (cursor.getString(cursor.getColumnIndex("video_url")));
                System.out.println(url);
            }while(cursor.moveToNext());
        }

        cursor.close();
        dbm.close();
    }

    public void wordScreen(View v) {
        Intent myIntent = new Intent(this, Dictionary.class);
        myIntent.putExtra("1", flag);
        startActivity(myIntent);
    }

    // for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        if (!wasRestored) {
            //player.cueVideo("B8J8FuNs300"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
            if (url.contains("=") && url.contains(";"))
            {
                String breaker = "[=]+";
                String[] parts = url.split(breaker);
                String breaker2 = "[;]+";
                String[] result = parts[1].split(breaker2);
                player.cueVideo(result[0]);
            }

            else if (url.contains(";"))
            {
                String breaker2 = "[;]+";
                String[] result = url.split(breaker2);
                player.cueVideo(result[0]);
            }

            else
            {
                Toast.makeText(this, "Youtube URL is invalid", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

}
