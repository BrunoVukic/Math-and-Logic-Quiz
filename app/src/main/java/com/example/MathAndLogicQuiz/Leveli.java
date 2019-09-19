package com.example.MathAndLogicQuiz;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class Leveli extends AppCompatActivity {
    Button level;
    SoundPool soundPool;
    int[] sound;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("Options", MODE_PRIVATE);
        int themeValue = prefs.getInt("keyTheme", 1);
        if (themeValue == 1) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme2);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leveli);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        String string = getResources().getString(R.string.string2);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(string);

        theme(themeValue, toolbar, title);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound = new int[2];
        sound[0] = soundPool.load(context, R.raw.thump, 1);
        sound[1] = soundPool.load(context, R.raw.wrong, 1);
    }

    public void fillLevels() {
        int noLevels = 1;
        do {
            String buttonID = "razina" + noLevels;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            level = findViewById(resID);
            level.setText(String.valueOf(noLevels));
            noLevels++;
        } while (noLevels <= 76);
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
                playSound(0);
                optionsShow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void theme(int a, Toolbar toolbar, TextView textView) {
        if (a == 1) {
            super.setTheme(R.style.AppTheme);
            toolbar.getContext().setTheme(R.style.Toolbar);
            toolbar.setBackgroundResource(R.color.colorPrimaryDark);
            textView.setBackgroundResource(R.color.colorPrimaryDark);

        } else {
            super.setTheme(R.style.AppTheme2);
            toolbar.getContext().setTheme(R.style.Toolbar2);
            toolbar.setBackgroundResource(R.color.colorSecondary);
            textView.setBackgroundResource(R.color.colorSecondary);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        completedLevels();
    }

    @Override
    public void onBackPressed() {
        playSound(0);
        super.onBackPressed();
    }

    public void completedLevels() {
        SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
        int completed = prefs.getInt("keyName", 1);
        Button predeni;
        for (int i = 1; i < completed; i++) {
            String buttonID = "razina" + i;
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
        playSound(0);
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
            playSound(0);
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
            String buttonID = "razina" + nextLevel;
            int btnID = getResources().getIdentifier(buttonID, "id", getPackageName());
            mustPress = findViewById(btnID);
            if (nextLevel % 2 == 0) {
                mustPress.setBackground(this.getResources().getDrawable(R.drawable.round_button_moras_taj2));
                Animation mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
                mustPress.startAnimation(mShakeAnimation);
                playSound(1);
            } else {
                mustPress.setBackground(this.getResources().getDrawable(R.drawable.round_button_moras_taj));
                Animation mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
                mustPress.startAnimation(mShakeAnimation);
                playSound(1);
            }
        }
    }

    public void optionsShow() {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int vibrateValue = prefs2.getInt("keyVibrate", 1);
        int soundValue = prefs2.getInt("keySound", 1);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.options, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        ImageButton btn = promptView.findViewById(R.id.imageVibrate);
        ImageButton btn2 = promptView.findViewById(R.id.imageSound);
        ImageButton btnClose = promptView.findViewById(R.id.alertClose);

        if (vibrateValue == 1) {
            btn.setImageResource(R.drawable.vibrate);
            if (soundValue == 1) {
                btn2.setImageResource(R.drawable.volume_high);

            } else {
                btn2.setImageResource(R.drawable.volume_off);
            }
            alertDialog.setView(promptView);
            alertDialog.show();
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(0);
                    alertDialog.cancel();
                }
            });
        } else {
            btn.setImageResource(R.drawable.vibrate_off);
            if (soundValue == 1) {
                btn2.setImageResource(R.drawable.volume_high);

            } else {
                btn2.setImageResource(R.drawable.volume_off);
            }
            alertDialog.setView(promptView);
            alertDialog.show();
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(0);
                    alertDialog.cancel();
                }
            });
        }
    }

    public void optionsVibration(View v) {
        ImageButton vibrate = (ImageButton) v;
        playSound(0);
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int vibrateValue = prefs2.getInt("keyVibrate", 1);
        if (vibrateValue == 1) {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keyVibrate", 0);
            editor.apply();
            vibrate.setImageResource(R.drawable.vibrate_off);
        } else {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keyVibrate", 1);
            editor.apply();
            vibrate.setImageResource(R.drawable.vibrate);
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vib.vibrate(100);
            }
        }
    }

    public void optionsSound(View v) {
        ImageButton sound = (ImageButton) v;
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keySound", 0);
            editor.apply();
            sound.setImageResource(R.drawable.volume_off);
        } else {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keySound", 1);
            editor.apply();
            sound.setImageResource(R.drawable.volume_high);
            playSound(0);
        }
    }

    public void playSound(int a) {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            soundPool.play(sound[a], 1, 1, 1, 0, 1f);
        }
    }
}

