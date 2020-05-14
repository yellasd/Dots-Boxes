package com.example.dotsboxes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GameActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    public GameView gameView;
    TextView tv1, tv2, tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);

        tv1 = findViewById(R.id.player_1);
        tv2 = findViewById(R.id.player_2);
        tv3 = findViewById(R.id.player_3);
        tv1.setText(R.string.start1);
        tv2.setText(R.string.start2);
        if (GameView.players == 3)
            tv3.setVisibility(View.VISIBLE);
        tv3.setText(R.string.start3);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.players:
                        PopupMenu noOfPlayers = new PopupMenu(GameActivity.this, bottomNavigationView.findViewById(R.id.players));
                        noOfPlayers.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent j = new Intent(getApplicationContext(), GameActivity.class);
                                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (item.getItemId() == R.id.single_player) ;
                                if (item.getItemId() == R.id.single_hard) ;
                                if (item.getItemId() == R.id.two_players)
                                    GameView.players = 2;
                                if (item.getItemId() == R.id.three_players)
                                    GameView.players = 3;
                                finish();
                                startActivity(j);
                                return false;
                            }
                        });
                        noOfPlayers.inflate(R.menu.popup_players);
                        noOfPlayers.show();
                        break;
                    case R.id.grid_size:
                        PopupMenu gridSize = new PopupMenu(GameActivity.this, bottomNavigationView.findViewById(R.id.grid_size));
                        gridSize.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent j = new Intent(getApplicationContext(), GameActivity.class);
                                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (item.getItemId() == R.id.ThreeByThree)
                                    GameView.size = 4;
                                if (item.getItemId() == R.id.FourByFour)
                                    GameView.size = 5;
                                if (item.getItemId() == R.id.FiveByFive)
                                    GameView.size = 6;
                                finish();
                                startActivity(j);
                                return false;
                            }
                        });
                        gridSize.inflate(R.menu.grid_size);
                        gridSize.show();
                        break;
                    case R.id.replay:
                        Intent i = new Intent(getApplicationContext(), GameActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(i);
                        break;
                    case R.id.undo:
                        break;
                }
                return false;
            }
        });

        gameView.setGameViewListener(new GameView.GameViewListener() {
            @Override
            public void updateScores() {
                tv1.setText(getString(R.string.player_1, gameView.scores[0]));
                tv2.setText(getString(R.string.player_2, gameView.scores[1]));
                if (GameView.players == 3) tv3.setVisibility(View.VISIBLE);
                tv3.setText(getString(R.string.player_3, gameView.scores[2]));
            }

            @Override
            public void endGame() {
                if (gameView.scores[0] + gameView.scores[1] + gameView.scores[2] == (GameView.size - 1) * (GameView.size - 1))
                    setWinnerText();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FrameLayout fl = findViewById(R.id.winnerContainer);
                        fl.setVisibility(View.GONE);
                        Intent i = new Intent(getApplicationContext(), GameActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(i);
                    }
                }, 3000);

            }
        });
    }

    public void setWinnerText() {
        FrameLayout fl = findViewById(R.id.winnerContainer);
        fl.setVisibility(View.VISIBLE);
        TextView temp = findViewById(R.id.winner_text);
        if (GameView.players == 2) {
            if (gameView.scores[0] > gameView.scores[1]) temp.setText(R.string.win1);
            if (gameView.scores[0] < gameView.scores[1]) temp.setText(R.string.win2);
            if (gameView.scores[0] == gameView.scores[1]) temp.setText(R.string.draw);
        }
        if (GameView.players == 3) {
            if (gameView.scores[0] > gameView.scores[1] && gameView.scores[0] > gameView.scores[2]) {
                temp.setText(R.string.win1);
                return;
            }
            if (gameView.scores[1] > gameView.scores[0] && gameView.scores[1] > gameView.scores[2]) {
                temp.setText(R.string.win2);
                return;
            }
            if (gameView.scores[2] > gameView.scores[1] && gameView.scores[2] > gameView.scores[0]) {
                temp.setText(R.string.win3);
                return;
            }
            temp.setText(R.string.draw);
        }
    }
}
