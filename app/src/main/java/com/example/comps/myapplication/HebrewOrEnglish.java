package com.example.comps.myapplication;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This activity is where the words' practice happens.
 * From here you can reach back to the menu activity or to save or load word list activity.
 * You can add, remove and practice any words you wish in english or hebrew as chosen in the menu.
 */
public class HebrewOrEnglish extends AppCompatActivity {
    private EditText etWord;
    private Button btnMenu, btnAddWord, btnOpenOrLoadWordList, btnPractice, btnSpeech;
    private ListView lvWords;
    private String link;
    private ArrayList<String> words;
    private StringBuilder linkBuilder;
    private User user;
    private TextView tvTitle;
    private boolean hebrew;
    private ImageButton ibSpeech;

    /**
     * With this function you can do the following things: get back to menu activity,
     * get to menu activity, add words, delete words and practice words.
     * This function starts when the activity is opened.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_hebrew_or_english);

        user = (User) getIntent().getSerializableExtra("user");
        words = getIntent().getStringArrayListExtra("words");
        hebrew = getIntent().getBooleanExtra("hebrew?", false);

        if (words == null) {
            words = new ArrayList<>();
        }

        btnMenu = findViewById(R.id.btnMenu);
        btnOpenOrLoadWordList = findViewById(R.id.btnOpenOrLoadWordList);
        btnAddWord = findViewById(R.id.btnAddWord);
        lvWords = findViewById(R.id.lvWords);
        etWord = findViewById(R.id.etWord);
        btnPractice = findViewById(R.id.btnPractice);
        tvTitle = findViewById(R.id.tvTitle);
        ibSpeech = findViewById(R.id.ibSpeech);

        if (hebrew) {
            tvTitle.setText("עברית");
        } else {
            tvTitle.setText("אנגלית");
        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(HebrewOrEnglish.this, Options.class);
                myIntent.putExtra("user", user);
                startActivity(myIntent);
            }
        });

        btnOpenOrLoadWordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnPractice.getCurrentTextColor() == Color.GREEN) {
                    Intent myIntent = new Intent(HebrewOrEnglish.this, SaveOrLoadWordList.class);
                    myIntent.putExtra("user", user);
                    myIntent.putExtra("words", words);
                    myIntent.putExtra("hebrew?", hebrew);
                    startActivity(myIntent);
                }
                else
                {
                    showToastMessage("אתה צריך להיות מחובר לאינטרנט על לשמור או לפתוח רשימות מילים",1500);
                }
            }
        });

        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etWord.getText().toString().trim().length() > 0 && !words.contains(etWord.getText().toString())) {
                    String word = etWord.getText().toString();
                    words.add(word);
                    etWord.getText().clear();
                    refreshListView();
                }
            }
        });

        lvWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToastMessage(words.get(position) + " נמחק", 750);
                words.remove(position);
                refreshListView();
            }
        });

        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnPractice.getCurrentTextColor() == Color.GREEN) {
                    generateLink();
                    link = linkBuilder.toString();
                    showToastMessage("נכנס!",1500);
                    openBrowser();
                }
                else
                {
                    showToastMessage("אתה צריך להיות מחובר לאינטרנט על מנת לתרגל מילים",1500);
                }
            }
        });
        ibSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        checkWifi();
        refreshListView();

    }

    /**
     * This function refreshes the listview.
     * This function starts when the user adds and removes words from the list of words.
     */
    public void refreshListView() {
        WordListAdapter arrayAdapter = new WordListAdapter(this, R.layout.custom_row, words);
        lvWords.setAdapter(arrayAdapter);
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
                Intent myIntent = new Intent(HebrewOrEnglish.this, LoginScreen.class);
                SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember","no");
                editor.apply();
                startActivity(myIntent);
                break;
            case R.id.itUserGuide:
                Dialog d = new Dialog(HebrewOrEnglish.this);
                d.setContentView(R.layout.d_user_guide);
                d.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function helps to open a link in a browser.
     * It called every time the user clicks to practice.
     */
    private void openBrowser() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }

    /**
     * This function shows a toast message.
     * It is called every time the programmer wants to put a toast in the screen.
     *
     * @param text     is the text of the toast.
     * @param duration is the time duration of the toast.
     */
    public void showToastMessage(String text, int duration) {
        final Toast toast = Toast.makeText(HebrewOrEnglish.this, text, Toast.LENGTH_SHORT);
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
                    btnPractice.setTextColor(Color.GREEN);
                    btnOpenOrLoadWordList.setTextColor(Color.GREEN);
                } else {
                    showToastMessage("אין חיבור לאינטרנט",1500);
                    btnPractice.setTextColor(Color.RED);
                    btnOpenOrLoadWordList.setTextColor(Color.RED);
                }
            }
        };
        this.registerReceiver( receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE") );
    }
    /**
     * This function listens to user voice
     * It is called the ibSpeech imageButton is clicked
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT,   getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, 123);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Not recognized",    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * This function put the speech result into word editText
     * It is called the ibSpeech imageButton is clicked
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 123: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etWord.setText(result.get(0));
                }
                break;
            }

        }
    }
    /**
     * This function generate the link in the site with the words the user chose to practice.
     * It called every time the user clicks to practice.
     */
    public void generateLink() {
        if (hebrew) {
            linkBuilder = new StringBuilder("https://www.hachtava.co.il/index.html?");
        } else {
            linkBuilder = new StringBuilder("https://www.hachtava.co.il/english.html?");
        }
        for (String word : words) {
            linkBuilder.append("&");
            for (int i = 0; i < word.length(); i++) {
                String character = Character.toString(word.charAt(i));
                switch (character) {
                    case "א":
                        linkBuilder.append("11");
                        break;
                    case "ב":
                        linkBuilder.append("12");
                        break;
                    case "ג":
                        linkBuilder.append("13");
                        break;
                    case "ד":
                        linkBuilder.append("14");
                        break;
                    case "ה":
                        linkBuilder.append("15");
                        break;
                    case "ו":
                        linkBuilder.append("16");
                        break;
                    case "ז":
                        linkBuilder.append("17");
                        break;
                    case "ח":
                        linkBuilder.append("18");
                        break;
                    case "ט":
                        linkBuilder.append("19");
                        break;
                    case "י":
                        linkBuilder.append("20");
                        break;
                    case "כ":
                        linkBuilder.append("21");
                        break;
                    case "ל":
                        linkBuilder.append("22");
                        break;
                    case "מ":
                        linkBuilder.append("23");
                        break;
                    case "נ":
                        linkBuilder.append("24");
                        break;
                    case "ס":
                        linkBuilder.append("25");
                        break;
                    case "ע":
                        linkBuilder.append("26");
                        break;
                    case "פ":
                        linkBuilder.append("27");
                        break;
                    case "צ":
                        linkBuilder.append("28");
                        break;
                    case "ק":
                        linkBuilder.append("29");
                        break;
                    case "ר":
                        linkBuilder.append("30");
                        break;
                    case "ש":
                        linkBuilder.append("31");
                        break;
                    case "ת":
                        linkBuilder.append("32");
                        break;
                    case "ץ":
                        linkBuilder.append("33");
                        break;
                    case "ם":
                        linkBuilder.append("34");
                        break;
                    case "ן":
                        linkBuilder.append("35");
                        break;
                    case "ף":
                        linkBuilder.append("36");
                        break;
                    case "ך":
                        linkBuilder.append("37");
                        break;
                    case " ":
                        linkBuilder.append("38");
                        break;
                    case "'":
                        linkBuilder.append("39");
                        break;
                    case "-":
                        linkBuilder.append("40");
                        break;
                    case "a":
                        linkBuilder.append("t");
                        break;
                    case "b":
                        linkBuilder.append("n");
                        break;
                    case "c":
                        linkBuilder.append("d");
                        break;
                    case "d":
                        linkBuilder.append("w");
                        break;
                    case "e":
                        linkBuilder.append("y");
                        break;
                    case "f":
                        linkBuilder.append("u");
                        break;
                    case "g":
                        linkBuilder.append("a");
                        break;
                    case "h":
                        linkBuilder.append("r");
                        break;
                    case "i":
                        linkBuilder.append("z");
                        break;
                    case "j":
                        linkBuilder.append("k");
                        break;
                    case "k":
                        linkBuilder.append("p");
                        break;
                    case "l":
                        linkBuilder.append("m");
                        break;
                    case "m":
                        linkBuilder.append("s");
                        break;
                    case "n":
                        linkBuilder.append("j");
                        break;
                    case "o":
                        linkBuilder.append("b");
                        break;
                    case "p":
                        linkBuilder.append("h");
                        break;
                    case "q":
                        linkBuilder.append("x");
                        break;
                    case "r":
                        linkBuilder.append("q");
                        break;
                    case "s":
                        linkBuilder.append("v");
                        break;
                    case "t":
                        linkBuilder.append("l");
                        break;
                    case "u":
                        linkBuilder.append("e");
                        break;
                    case "v":
                        linkBuilder.append("i");
                        break;
                    case "w":
                        linkBuilder.append("o");
                        break;
                    case "x":
                        linkBuilder.append("f");
                        break;
                    case "y":
                        linkBuilder.append("c");
                        break;
                    case "z":
                        linkBuilder.append("g");
                        break;
                    case "A":
                        linkBuilder.append("V");
                        break;
                    case "B":
                        linkBuilder.append("J");
                        break;
                    case "C":
                        linkBuilder.append("E");
                        break;
                    case "D":
                        linkBuilder.append("B");
                        break;
                    case "E":
                        linkBuilder.append("X");
                        break;
                    case "F":
                        linkBuilder.append("U");
                        break;
                    case "G":
                        linkBuilder.append("F");
                        break;
                    case "H":
                        linkBuilder.append("K");
                        break;
                    case "I":
                        linkBuilder.append("S");
                        break;
                    case "J":
                        linkBuilder.append("T");
                        break;
                    case "K":
                        linkBuilder.append("M");
                        break;
                    case "L":
                        linkBuilder.append("H");
                        break;
                    case "M":
                        linkBuilder.append("Y");
                        break;
                    case "N":
                        linkBuilder.append("Q");
                        break;
                    case "O":
                        linkBuilder.append("O");
                        break;
                    case "P":
                        linkBuilder.append("Z");
                        break;
                    case "Q":
                        linkBuilder.append("W");
                        break;
                    case "R":
                        linkBuilder.append("I");
                        break;
                    case "S":
                        linkBuilder.append("L");
                        break;
                    case "T":
                        linkBuilder.append("R");
                        break;
                    case "U":
                        linkBuilder.append("D");
                        break;
                    case "V":
                        linkBuilder.append("G");
                        break;
                    case "W":
                        linkBuilder.append("A");
                        break;
                    case "X":
                        linkBuilder.append("C");
                        break;
                    case "Y":
                        linkBuilder.append("N");
                        break;
                    case "Z":
                        linkBuilder.append("P");
                        break;
                    case ".":
                        linkBuilder.append("-");
                        break;
                }
            }
        }
    }

}
