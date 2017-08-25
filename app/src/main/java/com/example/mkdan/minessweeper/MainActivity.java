package com.example.mkdan.minessweeper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    final static int EASY = 0;
    final static int MEDIUM = 1;
    final static int HARD = 2;
    final static String BUTTON_PRESSED = "radioButtonPressed";
    final static String BUNDLE_KEY = "GameBundle";
    final static String GAME_KEY = "GameLevel";


    private int gameLevel;
    private RadioGroup GameLevel_rg;
    private RadioButton radio_b;
    private Button StartButton;

    SharedPreferences myPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPref = getPreferences(Context.MODE_PRIVATE);
        startRadioButtons();
        startGame();
    }

    public void startRadioButtons() {
        int i = myPref.getInt(BUTTON_PRESSED, 0);

        if (EASY == i) {
            radio_b = (RadioButton) findViewById(R.id.easyBtn);
            gameLevel = EASY;
        } else if (MEDIUM == i) {
            radio_b = (RadioButton) findViewById(R.id.medBtn);
            gameLevel = MEDIUM;
        } else {
            radio_b = (RadioButton) findViewById(R.id.hardBtn);
            gameLevel = HARD;
        }
        radio_b.setChecked(true);
        GameLevel_rg = (RadioGroup) findViewById(R.id.rg_level);
        GameLevel_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

            //  Called when the checked state of a compound button has changed.
            // we are listening to the radio group on changing radio bottons

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_b = (RadioButton) group.findViewById(checkedId);
                SharedPreferences.Editor editor = myPref.edit();

                switch (radio_b.getId()) {
                    case R.id.easyBtn:
                        editor.putInt(BUTTON_PRESSED, EASY);
                        gameLevel = EASY;
                        break;

                    case R.id.medBtn:
                        editor.putInt(BUTTON_PRESSED, MEDIUM);
                        gameLevel = MEDIUM;
                        break;

                    case R.id.hardBtn:
                        editor.putInt(BUTTON_PRESSED, HARD);
                        gameLevel = HARD;
                        break;
                }
                editor.commit();
            }
        });
    }


    public void startGame() {
        StartButton = (Button) findViewById(R.id.staertBtn);
        StartButton.setOnClickListener(new View.OnClickListener() {  //when we are pressing the button this will happen
            @Override
            public void onClick(View view) {
                startGameActivity();

            }
        });
    }
    public void startGameActivity() {
        Intent GameActivity = new Intent(this, GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(GAME_KEY, gameLevel);
        GameActivity.putExtra(BUNDLE_KEY, bundle);
        startActivity(GameActivity);
    }


}
