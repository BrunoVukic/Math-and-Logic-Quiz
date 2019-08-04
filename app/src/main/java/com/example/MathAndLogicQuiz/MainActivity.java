package com.example.MathAndLogicQuiz;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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


public class MainActivity extends AppCompatActivity {
    Button play;
    Button razine;
    Button izlaz;
    TextView naslov;
    MediaPlayer btnPress;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs3 = getSharedPreferences("Options", MODE_PRIVATE);
        int themeValue = prefs3.getInt("keyTheme", 1);
        if (themeValue == 1) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme2);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        naslov = findViewById(R.id.toolbar_title);
        View view = findViewById(R.id.toolbar_linear);
        TextView mainTitle = findViewById(R.id.titleView);
        if (themeValue == 1) {


            toolbar.getContext().setTheme(R.style.Toolbar);
            naslov.setBackgroundResource(R.color.colorPrimaryDark);
            view.setBackgroundResource(R.color.colorPrimaryDark);
            mainTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {


            toolbar.getContext().setTheme(R.style.Toolbar2);
            naslov.setBackgroundResource(R.color.colorSecondary);
            view.setBackgroundResource(R.color.colorSecondary);
            mainTitle.setTextColor(getResources().getColor(R.color.colorSecondary));
        }
        setSupportActionBar(toolbar);
        naslov = findViewById(R.id.toolbar_title);
        naslov.setText("");
        razine = findViewById(R.id.buttonRazine);
        razine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound();
                openRazine();
            }
        });
        izlaz = findViewById(R.id.buttonIzlaz);
        izlaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound();
                finish();
                System.exit(0);
            }
        });
        play = findViewById(R.id.buttonPlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound();
                openPlay();
            }
        });

        /*Animation mShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.floating);
        mainTitle.startAnimation(mShakeAnimation);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                PlaySound();
                OptionsShow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openPlay() {
        Intent intent = new Intent(this, razina1.class);
        SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
        editor.putBoolean("keyMain", true);
        editor.apply();
        startActivity(intent);
    }

    public void openRazine() {
        Intent intent = new Intent(this, Leveli.class);
        startActivity(intent);
    }

    public void reset(View v) {
        SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public void OptionsShow() {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int vibrateValue = prefs2.getInt("keyVibrate", 1);
        int soundValue = prefs2.getInt("keySound", 1);
        int themeValue = prefs2.getInt("keyTheme", 1);
        if (vibrateValue == 1) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.options_main, null);
            promptView.setBackground(getResources().getDrawable(R.drawable.alertbox));
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            ImageButton btn = promptView.findViewById(R.id.imageVibrate);
            ImageButton btn2 = promptView.findViewById(R.id.imageSound);
            Button btnDark = promptView.findViewById(R.id.buttonDark);
            Button btnLight = promptView.findViewById(R.id.buttonLight);
            ImageButton btnClose = promptView.findViewById(R.id.alertClose);


            btn.setImageResource(R.drawable.vibrate);
            if (soundValue == 1) {
                btn2.setImageResource(R.drawable.volume_high);

            } else {
                btn2.setImageResource(R.drawable.volume_off);
            }
            if (themeValue == 1) {
                btnDark.setBackgroundResource(R.drawable.border2);

            } else {
                btnLight.setBackgroundResource(R.drawable.border1);
            }
            alertDialog.setView(promptView);
            alertDialog.show();
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaySound();
                    alertDialog.cancel();
                }
            });

        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.options_main, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            ImageButton btn = promptView.findViewById(R.id.imageVibrate);
            ImageButton btn2 = promptView.findViewById(R.id.imageSound);
            Button btnDark = promptView.findViewById(R.id.buttonDark);
            Button btnLight = promptView.findViewById(R.id.buttonLight);
            ImageButton btnClose = promptView.findViewById(R.id.alertClose);
            btn.setImageResource(R.drawable.vibrate_off);
            if (soundValue == 1) {
                btn2.setImageResource(R.drawable.volume_high);

            } else {
                btn2.setImageResource(R.drawable.volume_off);
            }
            if (themeValue == 1) {
                btnDark.setBackgroundResource(R.drawable.border2);

            } else {
                btnLight.setBackgroundResource(R.drawable.border1);
            }
            alertDialog.setView(promptView);
            alertDialog.show();
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaySound();
                    alertDialog.cancel();
                }
            });
        }
    }

    public void Options(View v) {
        ImageButton vibrate = (ImageButton) v;
        PlaySound();
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int vibrateValue = prefs2.getInt("keyVibrate", 1);
        if (vibrateValue == 1) {
            SharedPreferences.Editor editor = getSharedPreferences("Options", MODE_PRIVATE).edit();
            editor.putInt("keyVibrate", 0);
            editor.apply();
            vibrate.setImageResource(R.drawable.vibrate_off);

        } else {
            SharedPreferences.Editor editor = getSharedPreferences("Options", MODE_PRIVATE).edit();
            editor.putInt("keyVibrate", 1);
            editor.apply();
            vibrate.setImageResource(R.drawable.vibrate);
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vib.vibrate(100);
            }

        }
    }

    public void Options2(View v) {
        ImageButton vibrate = (ImageButton) v;

        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            SharedPreferences.Editor editor = getSharedPreferences("Options", MODE_PRIVATE).edit();
            editor.putInt("keySound", 0);
            editor.apply();
            vibrate.setImageResource(R.drawable.volume_off);

        } else {
            SharedPreferences.Editor editor = getSharedPreferences("Options", MODE_PRIVATE).edit();
            editor.putInt("keySound", 1);
            editor.apply();
            vibrate.setImageResource(R.drawable.volume_high);
            PlaySound();
        }
    }

    public void OptionsLight(View v) {
        Button btn = (Button) v;
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int themeValue = prefs2.getInt("keyTheme", 1);
        if (themeValue == 1) {
            SharedPreferences.Editor editor = getSharedPreferences("Options", MODE_PRIVATE).edit();
            editor.putInt("keyTheme", 0);
            editor.apply();
            btn.setBackgroundResource(R.drawable.border1);
            PlaySound();
            recreate();
        }
    }

    public void OptionsDark(View v) {
        Button btn = (Button) v;
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int themeValue = prefs2.getInt("keyTheme", 1);
        if (themeValue == 0) {
            SharedPreferences.Editor editor = getSharedPreferences("Options", MODE_PRIVATE).edit();
            editor.putInt("keyTheme", 1);
            editor.apply();
            btn.setBackgroundResource(R.drawable.border2);
            PlaySound();
            recreate();
        }
    }

    public void PlaySound() {

        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            btnPress = MediaPlayer.create(context, R.raw.thump);
            btnPress.start();
        }
    }
}
