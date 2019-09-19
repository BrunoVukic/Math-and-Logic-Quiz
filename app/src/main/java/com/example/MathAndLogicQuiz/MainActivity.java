package com.example.MathAndLogicQuiz;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    SoundPool soundPool;
    int sound1;
    boolean loaded = false;
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        TextView title = findViewById(R.id.toolbar_title);
        TextView mainTitle = findViewById(R.id.titleView);
        theme(themeValue, toolbar, title, mainTitle);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        sound1 = soundPool.load(context, R.raw.thump, 1);
        Button levels = findViewById(R.id.buttonRazine);
        levels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                openRazine();
            }
        });
        Button play = findViewById(R.id.buttonPlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                openPlay();
            }
        });
        Button exit = findViewById(R.id.buttonIzlaz);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.ExitDialog));
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to exit?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        playSound();
                        finish();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playSound();
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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
                playSound();
                optionsShow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void theme(int a, Toolbar toolbar, TextView title, TextView mainTitle) {
        if (a == 1) {
            super.setTheme(R.style.AppTheme);
            toolbar.getContext().setTheme(R.style.Toolbar);
            toolbar.setBackgroundResource(R.color.colorPrimaryDark);
            title.setBackgroundResource(R.color.colorPrimaryDark);
            mainTitle.setTextColor(getResources().getColor(R.color.colorAccent));

        } else {
            super.setTheme(R.style.AppTheme2);
            toolbar.getContext().setTheme(R.style.Toolbar2);
            toolbar.setBackgroundResource(R.color.colorSecondary);
            title.setBackgroundResource(R.color.colorSecondary);
            mainTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
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
        playSound();
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.ExitDialog));
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to reset the levels?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                playSound();
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
                playSound();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void rateMe(View v) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + /*getPackageName()*/"com.android.chrome")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + /*getPackageName()*/"com.android.chrome")));
        }
    }

    public void optionsShow() {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int vibrateValue = prefs2.getInt("keyVibrate", 1);
        int soundValue = prefs2.getInt("keySound", 1);
        int themeValue = prefs2.getInt("keyTheme", 1);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.options_main, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final WebView webView = promptView.findViewById(R.id.webview);
        TextView privacy = promptView.findViewById(R.id.buttonPrivacy);
        TextView terms = promptView.findViewById(R.id.button_t_c);
        ImageButton btn = promptView.findViewById(R.id.imageVibrate);
        ImageButton btn2 = promptView.findViewById(R.id.imageSound);
        final Button btnDark = promptView.findViewById(R.id.buttonDark);
        final Button btnLight = promptView.findViewById(R.id.buttonLight);
        final ImageButton btnClose = promptView.findViewById(R.id.alertClose);

        btnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsTheme(alertDialog, btnDark);
            }
        });
        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsTheme(alertDialog, btnLight);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                btnDark.setVisibility(btnDark.GONE);
                btnLight.setVisibility(btnLight.GONE);
                openURL("file:///android_asset/privacy_policy.html", webView);

            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                btnDark.setVisibility(btnDark.GONE);
                btnLight.setVisibility(btnLight.GONE);
                openURL("file:///android_asset/terms_and_conditions.html", webView);
            }
        });

        if (vibrateValue == 1) {
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
                    playSound();
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
                    playSound();
                    alertDialog.cancel();
                }
            });
        }
    }

    public void openURL(String url, WebView webView) {
        webView.setVisibility(webView.VISIBLE);
        webView.loadUrl(url);
    }

    public void optionsVibration(View v) {
        ImageButton vibrate = (ImageButton) v;
        playSound();
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
            playSound();
        }
    }

    public void optionsTheme(AlertDialog alertDialog, Button btn) {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int themeValue = prefs2.getInt("keyTheme", 1);
        if (themeValue == 1) {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keyTheme", 0);
            editor.apply();
            btn.setBackgroundResource(R.drawable.border1);
            alertDialog.cancel();
            recreate();
        } else {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keyTheme", 1);
            editor.apply();
            btn.setBackgroundResource(R.drawable.border2);
            alertDialog.cancel();
            recreate();
        }
    }

    public void playSound() {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1 && loaded) {
            soundPool.play(sound1, 1, 1, 1, 0, 1f);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
