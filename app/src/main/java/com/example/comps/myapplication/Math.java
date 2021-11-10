package com.example.comps.myapplication;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * This activity allows the user to play math games that he chooses and go back to the menu.
 */
public class Math extends AppCompatActivity {
    private User user;
    private Button btnMenu, btnAddGames, btnSubGames, btnMulGames, btnDivGames, btnGame1, btnGame2, btnGame3, btnGame4, btnGame5, btnGame6, btnGame7;

    /**
     * With this function you can do the following things: go back to the menu and choose a game to play.
     * This function starts when the activity is opened.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_math);

        user = (User) getIntent().getSerializableExtra("user");

        btnMenu = findViewById(R.id.btnMenu);
        btnAddGames = findViewById(R.id.btnAddGames);
        btnSubGames = findViewById(R.id.btnSubGames);
        btnMulGames = findViewById(R.id.btnMulGames);
        btnDivGames = findViewById(R.id.btnDivGames);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Math.this, Options.class);
                myIntent.putExtra("user", user);
                startActivity(myIntent);
            }
        });
        btnAddGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGamesDialog(1);
            }
        });
        btnSubGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGamesDialog(2);
            }
        });
        btnMulGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGamesDialog(3);
            }
        });
        btnDivGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGamesDialog(4);
            }
        });
    }
    /**
     * This function opens the game list dialog.
     * Its called every time the user clicks on certain math game type.
     *
     * @param gameType is the math game type(1 - addGames,2 - subGames, 3 - mulGames, 4 - divGames)
     */
    private void openGamesDialog(final int gameType)
    {
        Dialog d = new Dialog(Math.this);
        d.setContentView(R.layout.d_math_games);
        d.show();
        btnGame1 = d.findViewById(R.id.btnGame1);
        btnGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameType)
                {
                    case 1:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/addition/towncreator/tc-addition.html");
                        break;
                    case 2:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/subtraction/towncreator/tc-subtraction.html");
                        break;
                    case 3:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/times-tables/towncreator/tc-multiplication.html");
                        break;
                    case 4:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/division/towncreator/tc-division.html");
                        break;
                }
            }
        });
        btnGame2 = d.findViewById(R.id.btnGame2);
        btnGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameType)
                {
                    case 1:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/addition/sealife/sl-addition.html");
                        break;
                    case 2:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/subtraction/sealife/sl-subtraction.html");
                        break;
                    case 3:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/times-tables/sealife/sl-multiplication.html");
                        break;
                    case 4:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/division/sealife/sl-division.html");
                        break;
                }
            }
        });
        btnGame3 = d.findViewById(R.id.btnGame3);
        btnGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameType)
                {
                    case 1:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/addition/addition-sharks.html");
                        break;
                    case 2:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/subtraction-math-basketball.html");
                        break;
                    case 3:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/times-tables/multiplication-airplanes.html");
                        break;
                    case 4:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/division-demolition.html");
                        break;
                }
            }
        });
        btnGame4 = d.findViewById(R.id.btnGame4);
        btnGame4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameType)
                {
                    case 1:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/addition/central-park.html");
                        break;
                    case 2:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/subtraction-mission.html");
                        break;
                    case 3:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/times-tables/multiplication-hot-air-balloons.html");
                        break;
                    case 4:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/division-croc-doc.html");
                        break;
                }
            }
        });
        btnGame5 = d.findViewById(R.id.btnGame5);
        btnGame5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameType)
                {
                    case 1:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/addition/addition-dragons.html");
                        break;
                    case 2:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/subtraction-pearl-search.html");
                        break;
                    case 3:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/times-tables/apollo-moon-landers.html");
                        break;
                    case 4:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/division-fruit-shoot.html");
                        break;
                }
            }
        });
        btnGame6 = d.findViewById(R.id.btnGame6);
        btnGame6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameType)
                {
                    case 1:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/addition/addition-washington-monument.html");
                        break;
                    case 2:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/subtraction-blast.html");
                        break;
                    case 3:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/multiplication-meteor.html");
                        break;
                    case 4:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/division-number-invaders.html");
                        break;
                }
            }
        });
        btnGame7 = d.findViewById(R.id.btnGame7);
        btnGame7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (gameType)
                {
                    case 1:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/addition/addition-empire-state.html");
                        break;
                    case 2:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/subtraction-matching.html");
                        break;
                    case 3:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/times-tables-shooting.html");
                        break;
                    case 4:
                        openGame(btnGame1,"https://www.free-training-tutorial.com/math-games/division-matching.html");
                        break;
                }
            }
        });
        checkWifi();
    }
    /**
     * This function opens a math game.
     * Its called every time the user clicks on certain math game.
     *
     * @param btnGame is the button of the game.
     */
    private void openGame(Button btnGame, String url)
    {
        if (btnGame.getCurrentTextColor() == Color.GREEN) {
            showToastMessage("נכנס!", 1500);
            openBrowser(url);
        } else {
            showToastMessage("אתה צריך להיות מחובר לאינטרנט על מנת לתרגל מתמטיקה", 1500);
        }
    }
    /**
     * This function helps to open a link in a browser.
     * Its called every time the user clicks to practice.
     */
    private void openBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    /**
     * This function builds the menu
     * This function opens when this activity is reached
     */
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
                Intent myIntent = new Intent(Math.this, LoginScreen.class);
                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","no");
                editor.apply();
                startActivity(myIntent);
                break;
            case R.id.itUserGuide:
                Dialog d = new Dialog(Math.this);
                d.setContentView(R.layout.d_user_guide);
                d.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function shows a toast message.
     * It is called every time the programmer wants to put a toast in the screen.
     *
     * @param text     is the text of the toast.
     * @param duration is the time duration of the toast.
     */
    public void showToastMessage(String text, int duration) {
        final Toast toast = Toast.makeText(Math.this, text, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }

    /**
     * This function constantly listens to a wifi connection.
     * It is called every time this activity opens (HebrewOrEnglish).
     * The user needs to be connected to the internet to practice words.
     */
    public void checkWifi() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent _) {
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                        mobile != null && mobile.isConnectedOrConnecting();
                if (isConnected) {
                    showToastMessage("מחובר לאינטרנט!", 1500);
                    btnGame1.setTextColor(Color.GREEN);
                    btnGame2.setTextColor(Color.GREEN);
                    btnGame3.setTextColor(Color.GREEN);
                    btnGame4.setTextColor(Color.GREEN);
                    btnGame5.setTextColor(Color.GREEN);
                    btnGame6.setTextColor(Color.GREEN);
                    btnGame7.setTextColor(Color.GREEN);
                } else {
                    showToastMessage("אין חיבור לאינטרנט", 1500);
                    btnGame1.setTextColor(Color.RED);
                    btnGame2.setTextColor(Color.RED);
                    btnGame3.setTextColor(Color.RED);
                    btnGame4.setTextColor(Color.RED);
                    btnGame5.setTextColor(Color.RED);
                    btnGame6.setTextColor(Color.RED);
                    btnGame7.setTextColor(Color.RED);
                }
            }
        };
        this.registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}
