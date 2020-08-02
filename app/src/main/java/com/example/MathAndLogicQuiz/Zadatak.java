package com.example.MathAndLogicQuiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide
        .load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
public class Zadatak extends AppCompatActivity implements RewardedVideoAdListener {
    int readLevel;
    int nextLevel;
    int correctAnswer;
    int adAmount = 0;
    int noOfTries = 0;
    int watchedHint = 0;
    int number = 0;
    ImageView question;
    TextView inputField;
    Button button;
    String hint = null;

    RewardedVideoAd mAd;
    TextView afterAd;
    ImageButton lightBulb;
    final Context context = this;

    Sound sound=new Sound();
    Theme theme=new Theme();
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        theme.currentContext=context;
        theme.settingMainTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zadatak);
        question = findViewById(R.id.zadatak_slika);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        theme.settingTheme(1);
        lightBulb = findViewById(R.id.hint);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SharedPreferences prefs2 = getSharedPreferences("Level", MODE_PRIVATE);
        boolean main = prefs2.getBoolean("keyMain", false);
        int checkLevel;
        if (main) {
            SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
            readLevel = prefs.getInt("keyName", 1);
        } else {
            Bundle b = getIntent().getExtras();
            try {
                checkLevel = b.getInt("extra");

            } catch (Exception e) {
                SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
                checkLevel = prefs.getInt("keyName2", 1);
            }
            readLevel = checkLevel;
        }
        title();
        button = findViewById(R.id.button10);
        inputField = findViewById(R.id.upis);
        sound.currentContext=context;
        sound.createSound();
        chooseLevel();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAds();
            }
        }, 100);
        inputField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= inputField.getRight() - inputField.getTotalPaddingRight()) {
                        clear(true);
                        return true;
                    }
                }
                return true;
            }
        });
    }
    public void title() {
        TextView text = findViewById(R.id.toolbar_title);
        text.setText("Level " + readLevel);
    }

    public void reset(boolean a) {
        title();
        clear(a);
        lightBulb.setImageResource(R.drawable.lightbulb_outline);
        lightBulb.setBackgroundResource(R.drawable.hint_button);
        adAmount=0;
        chooseLevel();
    }

    public void loadAds() {
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/5224354917");
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        mAd
                .loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    public void chooseLevel() {
        LevelPick level=new LevelPick();
        level.readLevel=readLevel;
        level.chooseLevel();
        String pictureID = "lvl" + readLevel;
        int lvlID = getResources().getIdentifier(pictureID, "drawable", getPackageName());
        Glide.with(context)
                .load(lvlID)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(question);
        correctAnswer=level.correctAnswer;
        hint=level.hint;
    }

    @Override
    public void onBackPressed() {
        sound.playSound(2);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_setting) {
            sound.playSound(2);
            Options options=new Options();
            options.currentContext=context;
            options.sound=sound;
            options.optionsShow(false);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void numberInput(View v) {
        Button botun = (Button) v;
        sound.playSound(1);
        if (inputField.getText().equals(getResources().getText(R.string.string8)) || inputField.getText() == null) {
            inputField.setText(null);
            inputField.setTextColor(getResources().getColor(R.color.colorAccent));
            inputField.setText(botun.getText());
        } else {
            inputField.setTextColor(getResources().getColor(R.color.colorAccent));
            inputField.append(botun.getText());
        }
    }

    public void clear(boolean a) {
        if(a)
        {
            sound.playSound(2);
        }
        inputField.setText(null);
        inputField.setHint(getResources().getText(R.string.string8));
    }
    public void check(View v) {

        int input;
        if (inputField.getText().equals("Answer")) {
            inputField.setText(null);
        }
        try {
            input = Integer.parseInt(inputField.getText().toString());
        } catch (Exception e) {
            input = 0;
        }
        if (input == 310796) {
            options3();
        } else {
            if (correctAnswer == input) {
                SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
                nextLevel = prefs.getInt("keyName", 1);

                if (readLevel == nextLevel) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("keyMain", true);
                    editor.apply();

                }
                SharedPreferences prefs2 = getSharedPreferences("Level", MODE_PRIVATE);
                boolean main = prefs2.getBoolean("keyMain", true);

                if (readLevel < nextLevel + 1 && main) {
                    nextLevel++;
                    SharedPreferences.Editor editor = prefs2.edit();
                    editor.putInt("keyName", nextLevel);
                    editor.apply();
                } else {

                    SharedPreferences.Editor editor = prefs2.edit();
                    editor.putInt("keyName2", readLevel);
                    editor.apply();
                }
                sound.playSound(0);
                View dialoglayout = LayoutInflater.from(Zadatak.this).inflate(R.layout.popup, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setView(dialoglayout);
                alertDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.cancel();
                        readLevel++;
                        boolean is_reset=false;
                        reset(is_reset);

                    }
                }, 1000);
            } else {
                sound.playSound(3);
                SharedPreferences prefs = getSharedPreferences("Options", MODE_PRIVATE);
                int retoredBoolean = prefs.getInt("keyVibrate", 1);
                if (retoredBoolean == 1) {

                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vib.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vib.vibrate(100);
                    }
                }
                Animation mShakeAnimation = AnimationUtils
                        .loadAnimation(context, R.anim.shake);
                inputField.startAnimation(mShakeAnimation);
                inputField.setText(null);
                inputField.setHint(getResources().getString(R.string.string8));
                noOfTries++;
                if (noOfTries >= 3) {
                    if (adAmount < 3) {
                        pressMe();
                    }
                    noOfTries = 0;
                }
            }
        }
    }

    public void hint(View v) {
        final ImageButton hint_b = (ImageButton) v;
        sound.playSound(2);
        final Button hintBtn;
        final Button solutionBtn;
        hint_b.clearAnimation();
        hint_b.setBackground(getResources().getDrawable(R.drawable.hint_button));
        final View dialoglayout = LayoutInflater.from(Zadatak.this).inflate(R.layout.hint, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(dialoglayout);
        hintBtn = dialoglayout.findViewById(R.id.watch_add_hint);
        solutionBtn = dialoglayout.findViewById(R.id.watch_add_solution);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playSound(2);
                if (!(mAd.isLoaded())) {
                    if (haveNetworkConnection()) {
                        hintBtn.setText(getResources().getText(R.string.string4));
                        mAd
                                .loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
                    } else {
                        String poruka = "Check your internet connection!";
                        Toast toast = Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    adAmount = 1;
                    mAd.show();
                    alertDialog.cancel();
                    String a = "Watch ad \nfor solution";
                    solutionBtn.setText(a);
                }
            }
        });
        solutionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adAmount < 2) {
                    String a = "Look at hint first";
                    solutionBtn.setText(a);
                    Animation mShakeAnimation = AnimationUtils
                            .loadAnimation(context, R.anim.shake);
                    solutionBtn.startAnimation(mShakeAnimation);
                    sound.playSound(3);
                } else {
                    sound.playSound(2);
                    if (!(mAd.isLoaded())) {
                        if (haveNetworkConnection()) {
                            solutionBtn.setText(getResources().getText(R.string.string4));
                            mAd
                                    .loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
                        } else {
                            String poruka = "Check your internet connection!";
                            Toast toast = Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        adAmount = 2;
                        mAd.show();
                        alertDialog.cancel();
                        watchedHint = 0;
                    }
                }

            }
        });
        alertDialog.show();
        ImageButton closeBtn = dialoglayout.findViewById(R.id.hintClose);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playSound(2);
                alertDialog.cancel();
            }
        });
    }
    public void options3() {
        number = 1;
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = number + 1;
                    }
                });
                if (number >= 3) {
                    cancel();
                    SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
                    editor.putInt("keyName", 80);
                    editor.apply();
                    View dialoglayout = LayoutInflater.from(Zadatak.this).inflate(R.layout.popup, null);
                    TextView textView = dialoglayout.findViewById(R.id.poruka_naslov);
                    textView.setText("Good job!");
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setView(dialoglayout);
                    alertDialog.show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Zadatak.this, Zadatak.class);
                            startActivity(intent);
                            finish();
                            Toast toast = Toast.makeText(getApplicationContext(), "All levels unlocked!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }, 500);
                }
            }
            @Override
            public void onFinish() {
                recreate();
            }
        }.start();
    }

    public void pressMe() {
        final ImageButton hintBtn = findViewById(R.id.hint);
        hintBtn.setBackground(getResources().getDrawable(R.drawable.hint_button_glow));
        Animation enlargeAnimation = AnimationUtils
                .loadAnimation(context, R.anim.large);
        hintBtn.startAnimation(enlargeAnimation);
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {
        watchedHint = 1;
    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        final View dialoglayout = LayoutInflater.from(Zadatak.this).inflate(R.layout.after_ad, null);
        afterAd = dialoglayout.findViewById(R.id.hint_solution);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        ImageButton closeBtn = dialoglayout.findViewById(R.id.hintClose);
        if (adAmount == 1) {
            afterAd.setText(hint);
            adAmount = 2;
            lightBulb.setImageResource(R.drawable.lightbulb);
        } else {
            afterAd.setText("" + correctAnswer);
            lightBulb.setImageResource(R.drawable.lightbulb_on);
            adAmount = 3;
        }
        alertDialog.setView(dialoglayout);
        alertDialog.show();
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playSound(2);
                alertDialog.cancel();
            }
        });
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
