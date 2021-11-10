package com.example.comps.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This activity is the login screen for the user where he can log-in with his user and password or register.
 * When you log-in to the account the activity reads from the database if the data that was typed is correct.
 * When you register the activity writes the new user's data in the firebase.
 */
public class LoginScreen extends AppCompatActivity {
    private EditText etUserName, etPassword, etDUserName, etDPassword;
    private Button btnRegister, btnLogin, btnDRegister;
    private CheckBox cbRemember;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private SharedPreferences preferences;
    /**
     * With this function you can do the following things: login and register an account.
     * This function starts when the activity is opened.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_login_screen);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                1);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnLogin.getCurrentTextColor() == Color.GREEN) {
                    Query q = ref.child("Users").orderByValue();
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (etUserName.getText().toString().length() != 0 && etPassword.getText().toString().length() != 0) {
                                for (DataSnapshot dst : dataSnapshot.getChildren()) {
                                    User u = dst.getValue(User.class);
                                    if (u.getUserName().equals(etUserName.getText().toString()) && u.getPassword().equals(etPassword.getText().toString())) {
                                        if (cbRemember.isChecked())
                                        {
                                            SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("remember","yes");
                                            editor.putString("userName",etUserName.getText().toString());
                                            editor.putString("password",etPassword.getText().toString());
                                            editor.apply();
                                        }
                                        else
                                        {
                                            SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("remember","no");
                                            editor.apply();
                                        }
                                        Intent myIntent = new Intent(LoginScreen.this, Options.class);
                                        myIntent.putExtra("user", u);
                                        showToastMessage("נכנס!",3000);
                                        startActivity(myIntent);
                                        return;
                                    }
                                }
                                showToastMessage("שם משתמש או סיסמה לא נכונים", 1000);
                                etUserName.getText().clear();
                                etPassword.getText().clear();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    showToastMessage("אתה צריך להיות מחובר לאינטרנט על מנת להתחבר",1500);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnLogin.getCurrentTextColor() == Color.GREEN) {
                    Dialog d = new Dialog(LoginScreen.this);
                    d.setContentView(R.layout.d_register);
                    d.show();
                    etDUserName = d.findViewById(R.id.etUserName);
                    etDPassword = d.findViewById(R.id.etPassword);
                    btnDRegister = d.findViewById(R.id.btnRegister);
                    btnDRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etDUserName.getText().toString().length() != 0 && etDPassword.getText().toString().length() != 0) {

                                if (etDPassword.getText().toString().length() < 8) {
                                    showToastMessage("הסיסמה חייבת להכיל לפחות 8 תווים", 1000);
                                    return;
                                }
                                Query q = ref.child("Users").orderByValue();
                                q.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        boolean error = false;
                                        for (DataSnapshot dst : dataSnapshot.getChildren()) {
                                            User u = dst.getValue(User.class);
                                            if (u.getUserName().equals(etDUserName.getText().toString())) {
                                                showToastMessage("שם המשתמש כבר קיים במערכת", 1000);
                                                etDUserName.getText().clear();
                                                etDPassword.getText().clear();
                                                error = true;
                                            }
                                        }
                                        if (!error) {
                                            User u = new User(etDUserName.getText().toString(), etDPassword.getText().toString());
                                            ref.child("Users").child(etDUserName.getText().toString()).setValue(u);
                                            Intent myIntent = new Intent(LoginScreen.this, Options.class);
                                            myIntent.putExtra("user", u);
                                            startActivity(myIntent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
                }
                else
                {
                    showToastMessage("אתה צריך להיות מחובר לאינטרנט על מנת להירשם",1500);
                }
            }
        });
        checkWifi();
        preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if(checkbox.equals("yes"))
        {
            etUserName.setText(preferences.getString("userName",""));
            etPassword.setText(preferences.getString("password",""));
            cbRemember.setChecked(true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnLogin.performClick();
                }
            }, 100);
        }
    }

    /**
     * This function shows a toast message.
     * It is called every time the programmer wants to put a toast in the screen.
     *
     * @param text     is the text of the toast.
     * @param duration is the time duration of the toast.
     */
    public void showToastMessage(String text, int duration) {
        final Toast toast = Toast.makeText(LoginScreen.this, text, Toast.LENGTH_SHORT);
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
            @Override public void onReceive(Context context, Intent _ )
            {
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                        mobile != null && mobile.isConnectedOrConnecting();
                if (isConnected) {
                    showToastMessage("מחובר לאינטרנט!",1500);
                    btnLogin.setTextColor(Color.GREEN);
                    btnRegister.setTextColor(Color.GREEN);

                } else {
                    showToastMessage("אין חיבור לאינטרנט",1500);
                    btnLogin.setTextColor(Color.RED);
                    btnRegister.setTextColor(Color.RED);
                }
            }
        };
        this.registerReceiver( receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE") );
    }
    /**
     * This function builds the menu
     * This function opens when this activity is reached
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
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
                Intent myIntent = new Intent(LoginScreen.this, LoginScreen.class);
                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","no");
                editor.apply();
                startActivity(myIntent);
                break;
            case R.id.itUserGuide:
                Dialog d = new Dialog(LoginScreen.this);
                d.setContentView(R.layout.d_user_guide);
                d.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

