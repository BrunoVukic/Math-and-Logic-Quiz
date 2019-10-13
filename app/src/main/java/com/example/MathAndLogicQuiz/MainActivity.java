package com.example.MathAndLogicQuiz;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    final Context context = this;
    Sound sound=new Sound();
    Theme theme=new Theme();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme.currentContext=context;
        theme.settingMainTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        theme.settingTheme(0);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        setSupportActionBar(toolbar);
        sound.currentContext=context;
        sound.createSound();
        Button levels = findViewById(R.id.buttonRazine);
        levels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sound.playSound(2);
                openRazine();
            }
        });
        Button play = findViewById(R.id.buttonPlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playSound(2);
                openPlay();
            }
        });
        Button exit = findViewById(R.id.buttonIzlaz);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playSound(2);
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.ExitDialog));
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to exit?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        sound.playSound(2);
                        finish();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sound.playSound(2);
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        recreate();
    }
    /*
    @Override
    protected void onResume() {
        super.onResume();
    }
    */
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

    private void openPlay() {
        Intent intent = new Intent(this, Zadatak.class);
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
        sound.playSound(2);
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.ExitDialog));
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to reset the levels?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                sound.playSound(2);
                SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                Toast toast = Toast.makeText(getApplicationContext(), "Levels are reset!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sound.playSound(2);
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void rateMe(View v) {
        sound.playSound(2);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + /*getPackageName()*/"com.android.chrome")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + /*getPackageName()*/"com.android.chrome")));
        }
    }

}
