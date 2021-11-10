package com.example.comps.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This activity gives the user the option to choose if he would like to practice hebrew or english words or to play math games.
 */
public class Options extends AppCompatActivity {
    private TextView tvHelloUser;
    private Button btnMath, btnEnglish, btnHebrew, btnMusic, btnStopMusic;
    private User user;
    private boolean hebrew;
    private MediaPlayer player;
    /**
     * With this function you can do the following things: You can reach to math, english, hebrew activities and play music.
     * This function starts when the activity is opened.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_options);

        user = (User) getIntent().getSerializableExtra("user");

        tvHelloUser = findViewById(R.id.tvHelloUser);
        btnMath = findViewById(R.id.btnMath);
        btnEnglish = findViewById(R.id.btnEnglish);
        btnHebrew = findViewById(R.id.btnHebrew);
        btnMusic = findViewById(R.id.btnMusic);
        btnStopMusic = findViewById(R.id.btnStopMusic);


        btnMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Options.this, Math.class);
                myIntent.putExtra("user", user);
                startActivity(myIntent);
            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Options.this, HebrewOrEnglish.class);
                myIntent.putExtra("user", user);
                hebrew = false;
                myIntent.putExtra("hebrew?", hebrew);
                startActivity(myIntent);
            }
        });

        btnHebrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Options.this, HebrewOrEnglish.class);
                myIntent.putExtra("user", user);
                hebrew = true;
                myIntent.putExtra("hebrew?", hebrew);
                startActivity(myIntent);
            }
        });
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent svc = new Intent(Options.this, BackgroundSoundService.class);
                startService(svc);
            }
        });
        btnStopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent svc = new Intent(Options.this, BackgroundSoundService.class);
                stopService(svc);
            }
        });
        tvHelloUser.setText("היי " + user.getUserName());

    }

    /**
     * This function builds the menu
     * This function opens when this activity is reached
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function allows to select items in the menu.
     * This function opens when the user hits the item(button).
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.itLogout:
                Intent myIntent = new Intent(Options.this, LoginScreen.class);
                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","no");
                editor.apply();
                startActivity(myIntent);
                break;
            case R.id.itUserGuide:
                Dialog d = new Dialog(Options.this);
                d.setContentView(R.layout.d_user_guide);
                d.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
