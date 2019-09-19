package com.example.MathAndLogicQuiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
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
    SoundPool soundPool;
    int[] sound;
    RewardedVideoAd mAd;
    TextView afterAd;
    ImageButton lightBulb;
    final Context context = this;


    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs3 = getSharedPreferences("Options", MODE_PRIVATE);
        int themeValue = prefs3.getInt("keyTheme", 1);
        if (themeValue == 1) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme2);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zadatak);
        question = findViewById(R.id.zadatak_slika);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        TextView title = findViewById(R.id.toolbar_title);

        theme(themeValue, toolbar, title, question);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound = new int[4];
        sound[0] = soundPool
                .load(context, R.raw.correct, 1);
        sound[1] = soundPool
                .load(context, R.raw.input, 1);
        sound[2] = soundPool
                .load(context, R.raw.thump, 1);
        sound[3] = soundPool
                .load(context, R.raw.wrong, 1);
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

    public void theme(int a, Toolbar toolbar, TextView textView, ImageView imageview) {
        if (a == 1) {
            super.setTheme(R.style.AppTheme);
            toolbar.getContext().setTheme(R.style.Toolbar);
            toolbar.setBackgroundResource(R.color.colorPrimaryDark);
            textView.setBackgroundResource(R.color.colorPrimaryDark);
            imageview.setBackgroundResource(R.color.zadatak);

        } else {
            super.setTheme(R.style.AppTheme2);
            toolbar.getContext().setTheme(R.style.Toolbar2);
            toolbar.setBackgroundResource(R.color.colorSecondary);
            textView.setBackgroundResource(R.color.colorSecondary);
            imageview.setBackgroundResource(R.color.zadatak2);
        }
    }

    public void title() {
        TextView text = findViewById(R.id.toolbar_title);
        /*String imeRazine = "Level " + readLevel;*/
        text.setText("Level " + readLevel);
    }

    public void reset(boolean a) {
        title();
        clear(a);
        lightBulb.setImageResource(R.drawable.lightbulb_outline);
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

        switch (readLevel) {
            case 1:
                Glide.with(context)
                        .load(R.drawable.lvl1)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 7;
                hint = "Number after 6";
                break;
            case 2:
                Glide.with(context)
                        .load(R.drawable.lvl2)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 15;
                hint = "Number before 16";
                break;
            case 3:
                Glide.with(context)
                        .load(R.drawable.lvl3)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 42;
                hint = "Number multiplied by 7";
                break;
            case 4:
                Glide.with(context)
                        .load(R.drawable.lvl4)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 8;
                hint = "Multiply with 8";
                break;
            case 5:
                Glide.with(context)
                        .load(R.drawable.lvl5)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 17;
                hint = "Adding with a number between 10 and 15";
                break;
            case 6:
                Glide.with(context)
                        .load(R.drawable.lvl6)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 20;
                hint = "Adding with a number between 10 and 15";
                break;
            case 7:
                Glide.with(context)
                        .load(R.drawable.lvl7)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 24;
                hint = "Multiply then add one more";
                break;
            case 8:
                Glide.with(context)
                        .load(R.drawable.lvl8)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 20;
                hint = "Add a multiplication of 2 ";
                break;
            case 9:
                Glide.with(context)
                        .load(R.drawable.lvl9)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 13;
                hint = "Add a number, subtract it, add again";
                break;
            case 10:
                Glide.with(context)
                        .load(R.drawable.lvl10)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 4;
                hint = "Multiply!";
                break;
            case 11:
                Glide.with(context)
                        .load(R.drawable.lvl11)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 10;
                hint = "Multiply first, add second";
                break;
            case 12:
                Glide.with(context)
                        .load(R.drawable.lvl12)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 49;
                hint = "Multiply first, add and subtract second";
                break;
            case 13:
                Glide.with(context)
                        .load(R.drawable.lvl13)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 29;
                hint = "1.Brackets, 2.Multiplications, 3.Adding and subtracting";
                break;
            case 14:
                Glide.with(context)
                        .load(R.drawable.lvl14)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 2;
                hint = "Multiply first, add and subtract second";
                break;
            case 15:
                Glide.with(context)
                        .load(R.drawable.lvl15)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 10;
                hint = "Multiply digit with a digit";
                break;
            case 16:
                Glide.with(context)
                        .load(R.drawable.lvl16)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 14;
                hint = "Count the big ones";
                break;
            case 17:
                Glide.with(context)
                        .load(R.drawable.lvl17)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 87;
                hint = "Flip the phone upside down";
                break;
            case 18:
                Glide.with(context)
                        .load(R.drawable.lvl18)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 4;
                hint = "x-y=60\n+x+y=100";
                break;
            case 19:
                Glide.with(context)
                        .load(R.drawable.lvl19)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 15;
                hint = "Divide and multiply, then add";
                break;
            case 20:
                Glide.with(context)
                        .load(R.drawable.lvl20)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 12;
                hint = "From left to right";
                break;
            case 21:
                Glide.with(context)
                        .load(R.drawable.lvl21)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 30;
                hint = "Number below is the multiplier";
                break;
            case 22:
                Glide.with(context)
                        .load(R.drawable.lvl22)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 7;
                hint = "Add the top to middle to get bottom";
                break;
            case 23:
                Glide.with(context)
                        .load(R.drawable.lvl23)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 3;
                hint = "Add the columns";
                break;
            case 24:
                Glide.with(context)
                        .load(R.drawable.lvl24)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 22;
                hint = "( x ) - ( - )";
                break;
            case 25:
                Glide.with(context)
                        .load(R.drawable.lvl25)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 18;
                hint = "Add digits, then numbers ";
                break;
            case 26:
                Glide.with(context)
                        .load(R.drawable.lvl26)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 5;
                hint = "Multiply with 3 then subtract";
                break;
            case 27:
                Glide.with(context)
                        .load(R.drawable.lvl27)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 54;
                hint = "Multiply with 2 then subtract";
                break;
            case 28:
                Glide.with(context)
                        .load(R.drawable.lvl28)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 51;
                hint = "Multiply with 2 then subtract";
                break;
            case 29:
                Glide.with(context)
                        .load(R.drawable.lvl29)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 2;
                hint = "Zig-zag pattern";
                break;
            case 30:
                Glide.with(context)
                        .load(R.drawable.lvl30)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 3;
                hint = "Add left and right, divide with a number to get the middle";
                break;
            case 31:
                Glide.with(context)
                        .load(R.drawable.lvl31)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 2;
                hint = "Even numbers";
                break;
            case 32:
                Glide.with(context)
                        .load(R.drawable.lvl32)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 8;
                hint = "Incrementing the number you add by 1";
                break;
            case 33:
                Glide.with(context)
                        .load(R.drawable.lvl33)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 16;
                hint = "Pattern of added numbers";
                break;
            case 34:
                Glide.with(context)
                        .load(R.drawable.lvl34)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 21;
                hint = "Add power of 2 (2^n)";
                break;
            case 35:
                Glide.with(context)
                        .load(R.drawable.lvl35)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 29;
                hint = "Adding number to the right side to get the opposite side number";
                break;
            case 36:
                Glide.with(context)
                        .load(R.drawable.lvl36)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 1;
                hint = "Add numbers on the same side of the triangle";
                break;
            case 37:
                Glide.with(context)
                        .load(R.drawable.lvl37)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 46;
                hint = "Add odd numbers";
                break;
            case 38:
                Glide.with(context)
                        .load(R.drawable.lvl38)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 10;
                hint = "Multipy bas numbers, add to top";
                break;
            case 39:
                Glide.with(context)
                        .load(R.drawable.lvl39)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 0;
                hint = "Count the circles in numbers";
                break;
            case 40:
                Glide.with(context)
                        .load(R.drawable.lvl40)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 7;
                hint = "Top and bottom half are equal";
                break;
            case 41:
                Glide.with(context)
                        .load(R.drawable.lvl41)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 8;
                hint = "Fibonacci";
                break;
            case 42:
                Glide.with(context)
                        .load(R.drawable.lvl42)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 8;
                hint = "+x -> (x-1) -> +x";
                break;
            case 43:
                Glide.with(context)
                        .load(R.drawable.lvl43)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 56;
                hint = "Do the opposite";
                break;
            case 44:
                Glide.with(context)
                        .load(R.drawable.lvl44)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 49;
                hint = "Number multiplied by itself";
                break;
            case 45:
                Glide.with(context)
                        .load(R.drawable.lvl45)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 42;
                hint = "Multiply left with left + 1";
                break;
            case 46:
                Glide.with(context)
                        .load(R.drawable.lvl46)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 4370;
                hint = "Patterns";
                break;
            case 47:
                Glide.with(context)
                        .load(R.drawable.lvl47)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 14;
                hint = "| = 10 , V = 5 , X = 10";
                break;
            case 48:
                Glide.with(context)
                        .load(R.drawable.lvl48)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 23;
                hint = "Prime numbers";
                break;
            case 49:
                Glide.with(context)
                        .load(R.drawable.lvl49)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 15;
                hint = "Add opposite numbers to get the middle";
                break;
            case 50:
                Glide.with(context)
                        .load(R.drawable.lvl50)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 16;
                hint = "Add a number to left triangle numbers";
                break;
            case 51:
                Glide.with(context)
                        .load(R.drawable.lvl51)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 1;
                hint = "13";
                break;
            case 52:
                Glide.with(context)
                        .load(R.drawable.lvl52)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 55;
                hint = "A^2 + B";
                break;
            case 53:
                Glide.with(context)
                        .load(R.drawable.lvl53)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 18;
                hint = "Add a number, but in a zig-zag pattern";
                break;
            case 54:
                Glide.with(context)
                        .load(R.drawable.lvl54)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 3;
                hint = "Multiply with middle to get the opposite";
                break;
            case 55:
                Glide.with(context)
                        .load(R.drawable.lvl55)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 48;
                hint = "Multiples of 6";
                break;
            case 56:
                Glide.with(context)
                        .load(R.drawable.lvl56)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 29;
                hint = "AB=B(A+B)";
                break;
            case 57:
                Glide.with(context)
                        .load(R.drawable.lvl57)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 13;
                hint = "\u221AA + \u221AB";
                break;
            case 58:
                Glide.with(context)
                        .load(R.drawable.lvl58)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 48;
                hint = "Add and subtract opposite numbers to get inner ones";
                break;
            case 59:
                Glide.with(context)
                        .load(R.drawable.lvl59)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 3;
                hint = "Add 2 diagonal numbers";
                break;
            case 60:
                Glide.with(context)
                        .load(R.drawable.lvl60)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 12;
                hint = "Add outer numbers, add 2, get middle";
                break;
            case 61:
                Glide.with(context)
                        .load(R.drawable.lvl61)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 3;
                hint = "(A + B)/(A - B)";
                break;
            case 62:
                Glide.with(context)
                        .load(R.drawable.lvl62)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 81;
                hint = "3^2 , (3+3)^2 ,...";
                break;
            case 63:
                Glide.with(context)
                        .load(R.drawable.lvl63)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 10;
                hint = "Middle of the two numbers below";
                break;
            case 64:
                Glide.with(context)
                        .load(R.drawable.lvl64)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 13;
                hint = "Add the digits";
                break;
            case 65:
                Glide.with(context)
                        .load(R.drawable.lvl65)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 93;
                hint = "100, 150 , 200 , 250 ";
                break;
            case 66:
                Glide.with(context)
                        .load(R.drawable.lvl66)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 17;
                hint = "Add all the numbers in a row";
                break;
            case 67:
                Glide.with(context)
                        .load(R.drawable.lvl67)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 7;
                hint = "Add neighbouring numbers and subtract with a number";
                break;
            case 68:
                Glide.with(context)
                        .load(R.drawable.lvl68)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 6;
                hint = "Add 2 lower numbers to get the third";
                break;
            case 69:
                Glide.with(context)
                        .load(R.drawable.lvl69)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 420;
                hint = "Marijuana number";
                break;
            case 70:
                Glide.with(context)
                        .load(R.drawable.lvl70)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 44;
                hint = "Increment circle segment to get the neighbouring circle segment";
                break;
            case 71:
                Glide.with(context)
                        .load(R.drawable.lvl71)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 27;
                hint = "Cubed number";
                break;
            case 72:
                Glide.with(context)
                        .load(R.drawable.lvl72)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 9;
                hint = "Do something with the top numbers to get the bottom ones";
                break;
            case 73:
                Glide.with(context)
                        .load(R.drawable.lvl73)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 7;
                hint = "Add the numbers in the little triangles";
                break;
            case 74:
                Glide.with(context)
                        .load(R.drawable.lvl74)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 8;
                hint = "2x2=4\n√4=2";
                break;
            case 75:
                Glide.with(context)
                        .load(R.drawable.lvl75)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 30;
                hint = "Add a number to the numbers in the left diamond to get the numbers on the right";
                break;
            case 76:
                Glide.with(context)
                        .load(R.drawable.lvl76)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 6;
                hint = "Add numbers on the same side";
                break;
            default:
                Glide.with(context)
                        .asGif()
                        .load(R.drawable.splash)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(question);
                correctAnswer = 7;
                hint = "Something went wrong";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        playSound(2);
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
            playSound(2);
            optionsShow();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void numberInput(View v) {
        Button botun = (Button) v;
        playSound(1);
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
            playSound(2);
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
                    /*readLevel++;*/
                    SharedPreferences.Editor editor = prefs2.edit();
                    editor.putInt("keyName2", readLevel);
                    editor.apply();
                }
                playSound(0);
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
                playSound(3);
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
        playSound(2);
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
                playSound(2);
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
                    playSound(3);
                } else {
                    playSound(2);
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
                playSound(2);
                alertDialog.cancel();
            }
        });
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
                    playSound(2);
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
                    playSound(2);
                    alertDialog.cancel();
                }
            });
        }
    }

    public void optionsVibration(View v) {
        ImageButton vibrate = (ImageButton) v;
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
            playSound(2);
        }
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
                    editor.putInt("keyName", 76);
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

    public void playSound(int a) {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            soundPool.play(sound[a], 1, 1, 1, 0, 1f);
        }
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
                playSound(2);
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
