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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * This activity allows the user to save or load word lists of his own in or from his data base.
 * The user can delete words from the list, remove word lists and go back to HebrewOrEnglish activity
 */
public class SaveOrLoadWordList extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private ArrayList<String> words,backup = new ArrayList<>();
    private ListOfWordList hebrewListOfWordList;
    private ListOfWordList englishListOfWordList;
    private ListView lvWords, lvWordLists;
    private Button btnAddWordList, btnBack, btnRemove;
    private EditText etWordListName;
    private User user;
    private int pos = -1;
    private boolean hebrew;
    /**
     * With this function you can do the following things: you can go back to the menu,
     * save/add word lists, remove word lists, remove words from word list and load word lists.
     * This function starts when the activity is opened.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_save_or_load_word_list);

        lvWords = findViewById(R.id.lvWords);
        lvWordLists = findViewById(R.id.lvWordLists);
        etWordListName = findViewById(R.id.etWordListName);
        btnAddWordList = findViewById(R.id.btnAddWordList);
        btnBack = findViewById(R.id.btnBack);
        btnRemove = findViewById(R.id.btnRemove);
        user = (User) getIntent().getSerializableExtra("user");
        words = getIntent().getStringArrayListExtra("words");
        hebrew = getIntent().getBooleanExtra("hebrew?", false);

        hebrewListOfWordList = user.getHebrewListOfWordList();
        englishListOfWordList = user.getEnglishListOfWordList();


        btnAddWordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etWordListName.getText().toString();
                WordList wordList = new WordList(words, name);
                if (etWordListName.getText().toString().trim().length() > 0) {
                    if (hebrew) {
                        for (WordList list : hebrewListOfWordList.getListOfWordList()) //bad code
                        {
                            if (list.getListName().equals(name)) {
                                hebrewListOfWordList.getListOfWordList().remove(list);
                                break;
                            }
                        }
                        hebrewListOfWordList.getListOfWordList().add(wordList);
                        pos = hebrewListOfWordList.getListOfWordList().size() - 1;
                    } else {
                        for (WordList list : englishListOfWordList.getListOfWordList()) //bad code
                        {
                            if (list.getListName().equals(name)) {
                                englishListOfWordList.getListOfWordList().remove(list);
                                break;
                            }
                        }
                        englishListOfWordList.getListOfWordList().add(wordList);
                        pos = englishListOfWordList.getListOfWordList().size() - 1;

                    }
                    showToastMessage("יצרת את: " + name, 1250);

                }
                etWordListName.getText().clear();
                saveData();
                refreshWordLists();
                refreshListView();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SaveOrLoadWordList.this, HebrewOrEnglish.class);
                myIntent.putExtra("user", user);
                myIntent.putExtra("words", words);
                myIntent.putExtra("hebrew?", hebrew);
                startActivity(myIntent);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos != -1) {
                    words = new ArrayList<>();
                    if (hebrew) {
                        showToastMessage("מחקת את: " + hebrewListOfWordList.getListOfWordList().remove(pos).getListName(), 1250);
                    } else {
                        showToastMessage("מחקת את: " + englishListOfWordList.getListOfWordList().remove(pos).getListName(), 1250);
                    }
                    pos = -1;
                    saveData();
                    refreshListView();
                    refreshWordLists();
                }
            }
        });

        lvWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToastMessage(words.get(position) + " נמחק", 750);
                backup = new ArrayList<>(words);
                words = backup;
                words.remove(position);
                refreshListView();
            }
        });

        lvWordLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pos != position)
                {
                    lvWordLists.getSelector().setAlpha(255);
                    pos = position;
                    if (hebrew) {
                        words = hebrewListOfWordList.getListOfWordList().get(position).getWords();
                        showToastMessage("בחרת ב: " + hebrewListOfWordList.getListOfWordList().get(position).getListName(), 1250);

                    } else {
                        words = englishListOfWordList.getListOfWordList().get(position).getWords();
                        showToastMessage("בחרת ב: " + englishListOfWordList.getListOfWordList().get(position).getListName(), 1250);
                    }
                    refreshListView();
                }
                else
                {
                    lvWordLists.getSelector().setAlpha(0);
                    pos = -1;
                    if (hebrew) {
                        backup = new ArrayList<>(words);
                        words = backup;
                        words.clear();
                        showToastMessage("הורדת בחירה מ: " + hebrewListOfWordList.getListOfWordList().get(position).getListName(), 1250);
                    }
                    else {
                        backup = new ArrayList<>(words);
                        words = backup;
                        words.clear();
                        showToastMessage("הורדת בחירה מ: " + englishListOfWordList.getListOfWordList().get(position).getListName(), 1250);
                    }
                }
                refreshListView();
            }
        });
        refreshListView();
        refreshWordLists();
        checkWifi();
    }

    /**
     * This function refreshes the listview of words.
     * This function starts when the user adds,removes and selects words from the list of words.
     */
    void refreshListView() {
        WordListAdapter arrayAdapter = new WordListAdapter(this, R.layout.custom_row, words);
        arrayAdapter.notifyDataSetChanged();
        lvWords.setAdapter(arrayAdapter);
    }

    /**
     * This function refreshes the listview of word lists.
     * This function starts when the user adds and removes word lists from the list of words.
     */
    void refreshWordLists() {
        ListOfWordListAdapter arrayAdapter;
        if (hebrew) {
            arrayAdapter = new ListOfWordListAdapter(this, R.layout.custom_row, hebrewListOfWordList.getListOfWordList());
        } else {
            arrayAdapter = new ListOfWordListAdapter(this, R.layout.custom_row, englishListOfWordList.getListOfWordList());
        }
        lvWordLists.setAdapter(arrayAdapter);
    }

    /**
     * This function shows a toast message.
     * It is called every time the programmer wants to put a toast in the screen.
     *
     * @param text     is the text of the toast.
     * @param duration is the time duration of the toast.
     */
    public void showToastMessage(String text, int duration) {
        final Toast toast = Toast.makeText(SaveOrLoadWordList.this, text, Toast.LENGTH_SHORT);
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
     * This function saves the hebrew and english word lists in the firebase.
     * This function starts when the user adds and removes word lists from the list of words.
     */
    void saveData() {
        user.setHebrewListOfWordList(hebrewListOfWordList);
        ref.child("Users").child(user.getUserName()).setValue(user);
        user.setEnglishListOfWordList(englishListOfWordList);
        ref.child("Users").child(user.getUserName()).setValue(user);
    }
    public void checkWifi() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent _ )
            {
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                        mobile != null && mobile.isConnectedOrConnecting();
                if (!isConnected) {
                    showToastMessage("אתה צריך להיות מחובר לאינטרנט על לשמור או לפתוח רשימות מילים",3000);
                    Intent myIntent = new Intent(SaveOrLoadWordList.this, HebrewOrEnglish.class);
                    myIntent.putExtra("user", user);
                    myIntent.putExtra("words", words);
                    myIntent.putExtra("hebrew?", hebrew);
                    startActivity(myIntent);
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
                Intent myIntent = new Intent(SaveOrLoadWordList.this, LoginScreen.class);
                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","no");
                editor.apply();
                startActivity(myIntent);
                break;
            case R.id.itUserGuide:
                Dialog d = new Dialog(SaveOrLoadWordList.this);
                d.setContentView(R.layout.d_user_guide);
                d.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}