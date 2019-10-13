package com.example.MathAndLogicQuiz;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Leveli extends AppCompatActivity {
    Button level;
    final Context context = this;
    Sound sound=new Sound();
    Theme theme=new Theme();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme.currentContext=context;
        theme.settingMainTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leveli);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        theme.settingTheme(2);
        String string = getResources().getString(R.string.string2);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(string);
        setSupportActionBar(toolbar);
        onResume();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
        fillLevels();
        sound.currentContext=context;
        sound.createSound();
    }

    public void fillLevels() {
        int noLevels = 1;
        do {
            String buttonID = "level" + noLevels;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            level = findViewById(resID);
            level.setText(String.valueOf(noLevels));
            noLevels++;
        } while (noLevels <= 80);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                sound.playSound(2);
                Options options=new Options();
                options.currentContext=context;
                options.sound=sound;
                options.optionsShow(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        completedLevels();
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        recreate();
    }
    @Override
    public void onBackPressed() {
        sound.playSound(2);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void completedLevels() {
        SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
        int completed = prefs.getInt("keyName", 1);
        Button predeni;
        for (int i = 1; i < completed; i++) {
            String buttonID = "level" + i;
            int btnID = getResources().getIdentifier(buttonID, "id", getPackageName());
            predeni = findViewById(btnID);
            if (i % 2 == 0) {
                predeni.setBackground(this.getResources().getDrawable(R.drawable.round_button_predeni2));
            } else {
                predeni.setBackground(this.getResources().getDrawable(R.drawable.round_button_predeni));
            }

        }
    }

    public boolean handleOnBackPress() {
        sound.playSound(2);
        onBackPressed();
        return true;
    }

    public void levelInfo(View v) {
        Button b = (Button) v;
        SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
        int nextLevel = prefs.getInt("keyName", 1);
        String BtnText = b.getText().toString();
        int pressedLevel = Integer.parseInt(BtnText);
        if (pressedLevel <= nextLevel || pressedLevel == 1)
            /*if(nextLevel>0)*/ {
            sound.playSound(2);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("keyMain", false);
            editor.apply();
            Intent intent = new Intent(this, Zadatak.class);
            intent.putExtra("extra", pressedLevel);
            startActivity(intent);
        } else {
            Button mustPress;
            String message = "No skipping levels!";
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            String buttonID = "level" + nextLevel;
            int btnID = getResources().getIdentifier(buttonID, "id", getPackageName());
            mustPress = findViewById(btnID);
            if (nextLevel % 2 == 0) {
                mustPress.setBackground(this.getResources().getDrawable(R.drawable.round_button_moras_taj2));
                Animation mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
                mustPress.startAnimation(mShakeAnimation);
                sound.playSound(3);
            } else {
                mustPress.setBackground(this.getResources().getDrawable(R.drawable.round_button_moras_taj));
                Animation mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
                mustPress.startAnimation(mShakeAnimation);
                sound.playSound(3);
            }
        }
    }
}

