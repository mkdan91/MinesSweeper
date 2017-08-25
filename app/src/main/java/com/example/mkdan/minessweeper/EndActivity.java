package com.example.mkdan.minessweeper;

import android.support.v7.app.AppCompatActivity;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.LinkedHashMap;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout;


public class EndActivity extends AppCompatActivity {

    final static String END_SCORE = "EndScore";
    final static String USER_NAME_INPUT = "UserNameInput";
    final static String UPDATE_TABLE="UpsateTable";
    final static String MIN_SCORE = "MinScore";
    final static String MIN_PLAYER ="MinPlayer";
    final static String NUM_OF_SCORES = "NumOfScores";

    public static final String LIST_TABLE__PREF = "myTablePreferences";
    SharedPreferences myPref1;
    SharedPreferences myPref2;

    private boolean mGameState;
    private int mGameTime;
    private int mGameDifficulty;

    final static int EASY = 0;
    final static int MEDIUM = 1;
    final static int HARD = 2;


    private String name=" ";
    private int Score;
    private int numOfScores;
    private Button menuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        menuButton = (Button) findViewById(R.id.menuBtn);

        myPref1 = getSharedPreferences(LIST_TABLE__PREF, Context.MODE_PRIVATE);
        myPref2 = getSharedPreferences(GameActivity.GAME_PREF, Context.MODE_PRIVATE);


        new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        extractDataFromBundle();
                        UpdateList();

                    }
                });
            }
        }.start();
        setButtonListeners();

    }

    private void extractDataFromBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(GameActivity.BUNDLE_KEY);
        mGameState = bundle.getBoolean(GameActivity.END_KEY);
        mGameDifficulty = bundle.getInt(GameActivity.RESTART_KEY);
        mGameTime = bundle.getInt(GameActivity.END_SCORE);

    }

    public void UpdateList() {

        numOfScores = myPref2.getInt(NUM_OF_SCORES, 0);
        name = myPref2.getString(USER_NAME_INPUT, " ").toString();
        Score = myPref2.getInt(END_SCORE, 0);

            SharedPreferences.Editor editor = myPref1.edit();
            if (numOfScores == 1) {
                editor.clear();
                editor.commit();

            }
            Boolean isupdate = myPref2.getBoolean(UPDATE_TABLE, false);
            SharedPreferences.Editor editor2 = myPref2.edit();
            editor2.putBoolean(UPDATE_TABLE, false);
            editor2.commit();

            if (isupdate) {
                String name = myPref2.getString(MIN_PLAYER, null);
                SharedPreferences.Editor editor1 = myPref1.edit();
                editor1.remove(name);
                editor1.apply();
            }
            editor.putInt(name, Score);
            editor.commit();
            startTable();


    }

    public void startTable() {

        Map<String ,Integer> myList= SortByScore((Map<String,Integer>)myPref1.getAll());
        TableLayout layout_tbl = (TableLayout) findViewById(R.id.myTable);
        int i=0;

        for (Map.Entry<String, ?> entry : myList.entrySet()) {
            TableRow row = (TableRow) layout_tbl.getChildAt(i); // Here get row id depending on number of row
            TextView name_col = (TextView) row.getChildAt(0);
            TextView score_col = (TextView) row.getChildAt(1);
            name_col.setText(entry.getKey());
            String scoreStr=String.format("%02d:%02d", (Integer)entry.getValue() / 60,(Integer) entry.getValue() % 60);
            score_col.setText(scoreStr);
            i++;
        }

    }
    public Map<String,Integer> SortByScore(Map<String, Integer> map){

        List<Map.Entry<String,Integer>> list = new LinkedList<>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return ( o1.getValue() ).compareTo( o2.getValue() );
            }
        } );

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
            SharedPreferences.Editor editor = myPref2.edit();
            editor.putInt(MIN_SCORE,entry.getValue());
            editor.putString(MIN_PLAYER,entry.getKey());
            editor.commit();

        }
        return result;
    }

    private void setButtonListeners() {
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu();
            }
        });
    }

    private void mainMenu() {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }
}