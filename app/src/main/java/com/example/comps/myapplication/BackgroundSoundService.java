package com.example.comps.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * This service allows to start background music to the user.
 * The music continuous in the background in loops and when the application is closed too.
 */
public class BackgroundSoundService extends Service {

    private MediaPlayer player;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    /**
     * This function creates the media player that plays the music.
     * Works when the user click the button that starts the music.
     */
    @Override
    public void onCreate() { //creates media player
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.relaxing_music);
        player.setLooping(true);
        player.setVolume(100, 100);

    }

    /**
     * This function starts the music.
     * This function is called when the user clicks the start background music button.
     *
     * @param intent
     * @param flags
     * @param startId
     * @return 1, signals that the service started in sticky mode.
     */
    public int onStartCommand(Intent intent, int flags, int startId) { //starting player when button clicked(command)
        player.start();
        return Service.START_STICKY;
    }

    public void onStart(Intent intent, int startId) {

    }

    public IBinder onUnBind(Intent arg0) {

        return null;
    }

    public void onStop() {

    }

    public void onPause() {

    }

    @Override
    public void onDestroy() {
        player.stop();
    } //stopping service (stopservice())

    @Override
    public void onLowMemory() {

    }
}