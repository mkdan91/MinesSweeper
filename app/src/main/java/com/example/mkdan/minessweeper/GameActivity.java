package com.example.mkdan.minessweeper;
import com.example.mkdan.minessweeper.Logic.Board;
import com.example.mkdan.minessweeper.Logic.Game;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;



public class GameActivity extends AppCompatActivity {
    final static String BUNDLE_KEY = "EndBundle";
    final static String END_KEY = "EndStatus";
    final static String RESTART_KEY = "EndRestart";
    final static String END_SCORE = "EndScore";

    final static String NUM_OF_SCORES = "NumOfScores";
    final static String USER_NAME_INPUT = "UserNameInput";
    final static String MIN_SCORE = "MinScore";
    final static String UPDATE_TABLE="UpsateTable";

    final int MAX_SCORES_TABLE = 10;
    private static int ScoresBreakers = 0;

    private String UserName = "";
    public static String name ="";

    private int timeScore = 0;
    private int numOfUseCounter;
    private int mGameLevel;

    private Game mGame;
    private GridView mGrid;

    private TextView timer_v;
    private TextView numberOfFlags_v;
    private Switch switch1;
    private Timer timer;
    public static final String GAME_PREF = "myGamePreferences";
    SharedPreferences pref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        pref=this.getSharedPreferences(GAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(NUM_OF_SCORES,0);
        editor.commit();

        extractDataFromBundle();
        mGame = new Game(mGameLevel);
        startGrid();

    }

    private void extractDataFromBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(MainActivity.BUNDLE_KEY);
        mGameLevel = bundle.getInt(MainActivity.GAME_KEY);
    }


    private void startGrid() {
        mGrid = (GridView) findViewById(R.id.gridview);
        mGrid.setAdapter(new TileAdapter(this, mGame.getBoard()));
        mGrid.setNumColumns(mGame.getBoard().getRowColSize());

        checkSwitchBtn();

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGame.playTile(position);
                numberOfFlags();
                timerStart();
                ((TileAdapter) mGrid.getAdapter()).notifyDataSetChanged();
                if (mGame.getIsWon() || mGame.getIsLost()) {
                    updateScores();
                }
            }
        });
        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Board board = mGame.getBoard();
                numberOfFlags();
                timerStart();
                board.getTileByPosition(i).setFlag();
                if (board.getTileByPosition(i).isFlagged()) {
                    mGame.getBoard().addFlag();
                } else {
                    mGame.getBoard().removeFlag();
                }
                ((TileAdapter) mGrid.getAdapter()).notifyDataSetChanged();
                return true;
            }
        });
    }

    private void checkSwitchBtn() {
        switch1 = (Switch) findViewById(R.id.switchFlags);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mGame.setFlag();
            }
        });
    }

    private void numberOfFlags() {
        int i = mGame.getBoard().getNumOfMines() - mGame.getBoard().getNumOfFlags();
        numberOfFlags_v = (TextView) findViewById(R.id.flagsNumber);
        numberOfFlags_v.setText("" + i);
    }


    private void timerStart() {
        if (numOfUseCounter == 0) {
            numOfUseCounter = 1;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timer_v = (TextView) findViewById(R.id.Timer);
                            timer_v.setText(String.format("%02d:%02d", timeScore / 60, timeScore % 60));
                            timeScore++;
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    public void updateScores() {
        timer.cancel();
        SharedPreferences.Editor editor = pref.edit();
            if (ScoresBreakers < MAX_SCORES_TABLE) {
                ScoresBreakers++;
                editor.putInt(NUM_OF_SCORES, ScoresBreakers);
                editor.commit();
                alertDialog();
            } else {
                int minScore = pref.getInt(MIN_SCORE, 0);
                if (timeScore < minScore) {
                    editor.putBoolean(UPDATE_TABLE, true);
                    editor.commit();
                    alertDialog();
                } else {
                    endGame();
                }
            }
        }

    public void alertDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.activity_dialog, null);
        final EditText name = (EditText) mView.findViewById(R.id.nameInput);
        final Button okBtn = (Button) mView.findViewById(R.id.okBtn);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(final DialogInterface dialog) {
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!name.getText().toString().isEmpty()) {
                            UserName = name.getText().toString();
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(USER_NAME_INPUT, UserName);
                            editor.commit();
                            enterNewRecord();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

     public void enterNewRecord() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(END_SCORE,timeScore);
        editor.commit();
        endGame();
      }

    public void endGame() {
        final Intent EndActivity = new Intent(mGrid.getContext(), EndActivity.class);
        Bundle bundle = new Bundle();
        if (mGame.getIsWon()) {
            bundle.putBoolean(END_KEY, true);
        } else {
            bundle.putBoolean(END_KEY, false);
        }
        EndActivity.putExtra(BUNDLE_KEY, bundle);
        startActivity(EndActivity);

    }





}


