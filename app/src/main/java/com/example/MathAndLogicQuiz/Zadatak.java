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
import android.util.Log;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class Zadatak extends AppCompatActivity implements RewardedVideoAdListener {
    int ocitajRazinu;
    int provjeraRazineIzLevel;
    int iducaRazina;
    int tocnoRjesenje;
    int adAmount = 0;
    int brojPokusaja = 0;
    int jeliPogledanHint = 0;
    int broj = 0;
    TextView ispis;
    Button button;
    String hint = null;
    String imeRazine;
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
        ImageView zad = findViewById(R.id.zadatak_slika);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        TextView text = findViewById(R.id.toolbar_title);
        View view = findViewById(R.id.toolbar_linear);
        if (themeValue == 1) {

            zad.setBackgroundResource(R.color.zadatak);
            toolbar.getContext().setTheme(R.style.Toolbar);
            text.setBackgroundResource(R.color.colorPrimaryDark);
            view.setBackgroundResource(R.color.colorPrimaryDark);
        } else {

            zad.setBackgroundResource(R.color.zadatak2);
            toolbar.getContext().setTheme(R.style.Toolbar2);
            text.setBackgroundResource(R.color.colorSecondary);
            view.setBackgroundResource(R.color.colorSecondary);
        }
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
        if (main) {
            SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
            ocitajRazinu = prefs.getInt("keyName", 1);
        } else {
            Bundle b = getIntent().getExtras();
            try {
                provjeraRazineIzLevel = b.getInt("extra");

            } catch (Exception e) {
                SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
                provjeraRazineIzLevel = prefs.getInt("keyName2", 1);
            }
            ocitajRazinu = provjeraRazineIzLevel;
        }
        imeRazine = "Level " + ocitajRazinu;
        text.setText(imeRazine);
        button = findViewById(R.id.button10);
        ispis=findViewById(R.id.upis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound = new int[4];
        sound[0] = soundPool.load(context, R.raw.correct, 1);
        sound[1] = soundPool.load(context, R.raw.input, 1);
        sound[2] = soundPool.load(context, R.raw.thump, 1);
        sound[3] = soundPool.load(context, R.raw.wrong, 1);
        OdaberiLevel();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadAds();
            }
        }, 100);
        ispis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= ispis.getRight() - ispis.getTotalPaddingRight()) {
                        Clear();
                        return true;
                    }
                }
                return true;
            }
        });
    }

    public void LoadAds() {
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/5224354917");
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    public void OdaberiLevel() {
        ImageView zadatakSlika = findViewById(R.id.zadatak_slika);
        switch (ocitajRazinu) {
            case 1:
                zadatakSlika.setImageResource(R.drawable.lvl1);
                tocnoRjesenje = 7;
                hint = "Number after 6";
                break;
            case 2:
                zadatakSlika.setImageResource(R.drawable.lvl2);
                tocnoRjesenje = 15;
                hint = "Number before 16";
                break;
            case 3:
                zadatakSlika.setImageResource(R.drawable.lvl3);
                tocnoRjesenje = 42;
                hint = "Number multiplied by 7";
                break;
            case 4:
                zadatakSlika.setImageResource(R.drawable.lvl4);
                tocnoRjesenje = 8;
                hint = "Multiply with 8";
                break;
            case 5:
                zadatakSlika.setImageResource(R.drawable.lvl5);
                tocnoRjesenje = 17;
                hint = "Adding with a number between 10 and 15";
                break;
            case 6:
                zadatakSlika.setImageResource(R.drawable.lvl6);
                tocnoRjesenje = 20;
                hint = "Adding with a number between 10 and 15";
                break;
            case 7:
                zadatakSlika.setImageResource(R.drawable.lvl7);
                tocnoRjesenje = 24;
                hint = "Multiply then add one more";
                break;
            case 8:
                zadatakSlika.setImageResource(R.drawable.lvl8);
                tocnoRjesenje = 20;
                hint = "Add a multiplication of 2 ";
                break;
            case 9:
                zadatakSlika.setImageResource(R.drawable.lvl9);
                tocnoRjesenje = 13;
                hint = "Add a number, subtract it, add again";
                break;
            case 10:
                zadatakSlika.setImageResource(R.drawable.lvl10);
                tocnoRjesenje = 4;
                hint = "Multiply!";
                break;
            case 11:
                zadatakSlika.setImageResource(R.drawable.lvl11);
                tocnoRjesenje = 10;
                hint = "Multiply first, add second";
                break;
            case 12:
                zadatakSlika.setImageResource(R.drawable.lvl12);
                tocnoRjesenje = 49;
                hint = "Multiply first, add and subtract second";
                break;
            case 13:
                zadatakSlika.setImageResource(R.drawable.lvl13);
                tocnoRjesenje = 29;
                hint = "1.Brackets, 2.Multiplications, 3.Adding and subtracting";
                break;
            case 14:
                zadatakSlika.setImageResource(R.drawable.lvl14);
                tocnoRjesenje = 2;
                hint = "Multiply first, add and subtract second";
                break;
            case 15:
                zadatakSlika.setImageResource(R.drawable.lvl15);
                tocnoRjesenje = 10;
                hint = "Multiply digit with a digit";
                break;
            case 16:
                zadatakSlika.setImageResource(R.drawable.lvl16);
                tocnoRjesenje = 14;
                hint = "Count the big ones";
                break;
            case 17:
                zadatakSlika.setImageResource(R.drawable.lvl17);
                tocnoRjesenje = 87;
                hint = "Flip the phone upside down";
                break;
            case 18:
                zadatakSlika.setImageResource(R.drawable.lvl18);
                tocnoRjesenje = 4;
                hint = "x-y=60\n+x+y=100";
                break;
            case 19:
                zadatakSlika.setImageResource(R.drawable.lvl19);
                tocnoRjesenje = 15;
                hint = "Divide and multiply, then add";
                break;
            case 20:
                zadatakSlika.setImageResource(R.drawable.lvl20);
                tocnoRjesenje = 12;
                hint = "From left to right";
                break;
            case 21:
                zadatakSlika.setImageResource(R.drawable.lvl21);
                tocnoRjesenje = 30;
                hint = "Number below is the multiplier";
                break;
            case 22:
                zadatakSlika.setImageResource(R.drawable.lvl22);
                tocnoRjesenje = 7;
                hint = "Add the top to middle to get bottom";
                break;
            case 23:
                zadatakSlika.setImageResource(R.drawable.lvl23);
                tocnoRjesenje = 3;
                hint = "Add the columns";
                break;
            case 24:
                zadatakSlika.setImageResource(R.drawable.lvl24);
                tocnoRjesenje = 22;
                hint = "( x ) - ( - )";
                break;
            case 25:
                zadatakSlika.setImageResource(R.drawable.lvl25);
                tocnoRjesenje = 18;
                hint = "Add digits, then numbers ";
                break;
            case 26:
                zadatakSlika.setImageResource(R.drawable.lvl26);
                tocnoRjesenje = 5;
                hint = "Multiply with 3 then subtract";
                break;
            case 27:
                zadatakSlika.setImageResource(R.drawable.lvl27);
                tocnoRjesenje = 54;
                hint = "Multiply with 2 then subtract";
                break;
            case 28:
                zadatakSlika.setImageResource(R.drawable.lvl28);
                tocnoRjesenje = 51;
                hint = "Multiply with 2 then subtract";
                break;
            case 29:
                zadatakSlika.setImageResource(R.drawable.lvl29);
                tocnoRjesenje = 2;
                hint = "Zig-zag pattern";
                break;
            case 30:
                zadatakSlika.setImageResource(R.drawable.lvl30);
                tocnoRjesenje = 3;
                hint = "Add left and right, divide with a number to get the middle";
                break;
            case 31:
                zadatakSlika.setImageResource(R.drawable.lvl31);
                tocnoRjesenje = 2;
                hint = "Even numbers";
                break;
            case 32:
                zadatakSlika.setImageResource(R.drawable.lvl32);
                tocnoRjesenje = 8;
                hint = "Incrementing the number you add by 1";
                break;
            case 33:
                zadatakSlika.setImageResource(R.drawable.lvl33);
                tocnoRjesenje = 16;
                hint = "Pattern of added numbers";
                break;
            case 34:
                zadatakSlika.setImageResource(R.drawable.lvl34);
                tocnoRjesenje = 21;
                hint = "Add power of 2 (2^n)";
                break;
            case 35:
                zadatakSlika.setImageResource(R.drawable.lvl35);
                tocnoRjesenje = 29;
                hint = "Adding number to the right side to get the opposite side number";
                break;
            case 36:
                zadatakSlika.setImageResource(R.drawable.lvl36);
                tocnoRjesenje = 1;
                hint = "Add numbers on the same side of the triangle";
                break;
            case 37:
                zadatakSlika.setImageResource(R.drawable.lvl37);
                tocnoRjesenje = 46;
                hint = "Add odd numbers";
                break;
            case 38:
                zadatakSlika.setImageResource(R.drawable.lvl38);
                tocnoRjesenje = 10;
                hint = "Multipy bas numbers, add to top";
                break;
            case 39:
                zadatakSlika.setImageResource(R.drawable.lvl39);
                tocnoRjesenje = 0;
                hint = "Count the circles in numbers";
                break;
            case 40:
                zadatakSlika.setImageResource(R.drawable.lvl40);
                tocnoRjesenje = 7;
                hint = "Top and bottom half are equal";
                break;
            case 41:
                zadatakSlika.setImageResource(R.drawable.lvl41);
                tocnoRjesenje = 8;
                hint = "Fibonacci";
                break;
            case 42:
                zadatakSlika.setImageResource(R.drawable.lvl42);
                tocnoRjesenje = 8;
                hint = "+x -> (x-1) -> +x";
                break;
            case 43:
                zadatakSlika.setImageResource(R.drawable.lvl43);
                tocnoRjesenje = 56;
                hint = "Do the opposite";
                break;
            case 44:
                zadatakSlika.setImageResource(R.drawable.lvl44);
                tocnoRjesenje = 49;
                hint = "Number multiplied by itself";
                break;
            case 45:
                zadatakSlika.setImageResource(R.drawable.lvl45);
                tocnoRjesenje = 42;
                hint = "Multiply left with left + 1";
                break;
            case 46:
                zadatakSlika.setImageResource(R.drawable.lvl46);
                tocnoRjesenje = 4370;
                hint = "Patterns";
                break;
            case 47:
                zadatakSlika.setImageResource(R.drawable.lvl47);
                tocnoRjesenje = 14;
                hint = "| = 10 , V = 5 , X = 10";
                break;
            case 48:
                zadatakSlika.setImageResource(R.drawable.lvl48);
                tocnoRjesenje = 23;
                hint = "Prime numbers";
                break;
            case 49:
                zadatakSlika.setImageResource(R.drawable.lvl49);
                tocnoRjesenje = 15;
                hint = "Add opposite numbers to get the middle";
                break;
            case 50:
                zadatakSlika.setImageResource(R.drawable.lvl50);
                tocnoRjesenje = 16;
                hint = "Add a number to left triangle numbers";
                break;
            case 51:
                zadatakSlika.setImageResource(R.drawable.lvl51);
                tocnoRjesenje = 1;
                hint = "13";
                break;
            case 52:
                zadatakSlika.setImageResource(R.drawable.lvl52);
                tocnoRjesenje = 55;
                hint = "A^2 + B";
                break;
            case 53:
                zadatakSlika.setImageResource(R.drawable.lvl53);
                tocnoRjesenje = 18;
                hint = "Add a number, but in a zig-zag pattern";
                break;
            case 54:
                zadatakSlika.setImageResource(R.drawable.lvl54);
                tocnoRjesenje = 3;
                hint = "Multiply with middle to get the opposite";
                break;
            case 55:
                zadatakSlika.setImageResource(R.drawable.lvl55);
                tocnoRjesenje = 48;
                hint = "Multiples of 6";
                break;
            case 56:
                zadatakSlika.setImageResource(R.drawable.lvl56);
                tocnoRjesenje = 29;
                hint = "AB=B(A+B)";
                break;
            case 57:
                zadatakSlika.setImageResource(R.drawable.lvl57);
                tocnoRjesenje = 13;
                hint = "\u221AA + \u221AB";
                break;
            case 58:
                zadatakSlika.setImageResource(R.drawable.lvl58);
                tocnoRjesenje = 48;
                hint = "Add and subtract opposite numbers to get inner ones";
                break;
            case 59:
                zadatakSlika.setImageResource(R.drawable.lvl59);
                tocnoRjesenje = 3;
                hint = "Add 2 diagonal numbers";
                break;
            case 60:
                zadatakSlika.setImageResource(R.drawable.lvl60);
                tocnoRjesenje = 12;
                hint = "Add outer numbers, add 2, get middle";
                break;
            case 61:
                zadatakSlika.setImageResource(R.drawable.lvl61);
                tocnoRjesenje = 3;
                hint = "(A + B)/(A - B)";
                break;
            case 62:
                zadatakSlika.setImageResource(R.drawable.lvl62);
                tocnoRjesenje = 81;
                hint = "3^2 , (3+3)^2 ,...";
                break;
            case 63:
                zadatakSlika.setImageResource(R.drawable.lvl63);
                tocnoRjesenje = 10;
                hint = "Middle of the two numbers below";
                break;
            case 64:
                zadatakSlika.setImageResource(R.drawable.lvl64);
                tocnoRjesenje = 13;
                hint = "Add the digits";
                break;
            case 65:
                zadatakSlika.setImageResource(R.drawable.lvl65);
                tocnoRjesenje = 93;
                hint = "100, 150 , 200 , 250 ";
                break;
            case 66:
                zadatakSlika.setImageResource(R.drawable.lvl66);
                tocnoRjesenje = 17;
                hint = "Add all the numbers in a row";
                break;
            case 67:
                zadatakSlika.setImageResource(R.drawable.lvl67);
                tocnoRjesenje = 7;
                hint = "Add neighbouring numbers and subtract with a number";
                break;
            case 68:
                zadatakSlika.setImageResource(R.drawable.lvl68);
                tocnoRjesenje = 6;
                hint = "Add 2 lower numbers to get the third";
                break;
            case 69:
                zadatakSlika.setImageResource(R.drawable.lvl69);
                tocnoRjesenje = 420;
                hint = "Marijuana number";
                break;
            case 70:
                zadatakSlika.setImageResource(R.drawable.lvl70);
                tocnoRjesenje = 44;
                hint = "Increment circle segment to get the neighbouring circle segment";
                break;
            case 71:
                zadatakSlika.setImageResource(R.drawable.lvl71);
                tocnoRjesenje = 27;
                hint = "Cubed number";
                break;
            case 72:
                zadatakSlika.setImageResource(R.drawable.lvl72);
                tocnoRjesenje = 9;
                hint = "Do something with the top numbers to get the bottom ones";
                break;
            case 73:
                zadatakSlika.setImageResource(R.drawable.lvl73);
                tocnoRjesenje = 7;
                hint = "Add the numbers in the little triangles";
                break;
            case 74:
                zadatakSlika.setImageResource(R.drawable.lvl74);
                tocnoRjesenje = 8;
                hint = "2x2=4\n√4=2";
                break;
            case 75:
                zadatakSlika.setImageResource(R.drawable.lvl75);
                tocnoRjesenje = 30;
                hint = "Add a number to the numbers in the left diamond to get the numbers on the right";
                break;
            case 76:
                zadatakSlika.setImageResource(R.drawable.lvl76);
                tocnoRjesenje = 6;
                hint = "Add numbers on the same side";
                break;
            default:
                zadatakSlika.setImageResource(R.drawable.lvl1);
                tocnoRjesenje = 7;
                hint = "Number after 6";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        PlaySound(2);
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
            PlaySound(2);
            OptionsShow();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void upis(View v) {
        Button botun = (Button) v;
        PlaySound(1);
        if (ispis.getText().equals(getResources().getText(R.string.string8)) || ispis.getText() == null) {
            ispis.setText(null);
            ispis.setTextColor(getResources().getColor(R.color.colorAccent));
            ispis.setText(botun.getText());
        } else {
            ispis.setTextColor(getResources().getColor(R.color.colorAccent));
            ispis.append(botun.getText());
        }
    }

    public void Clear() {
        PlaySound(2);
        ispis.setText(null);
        ispis.setHint(getResources().getText(R.string.string8));
    }


    public void Check(View v) {

        int upisanoRj;

        if(ispis.getText().equals("Answer"))
        {
            ispis.setText(null);
        }
        try {
            upisanoRj = Integer.parseInt(ispis.getText().toString());
        } catch (Exception e) {
            upisanoRj = 0;
        }
        if (upisanoRj == 310796) {
            Options3();
        } else {
            if (tocnoRjesenje == upisanoRj) {
                SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
                iducaRazina = prefs.getInt("keyName", 1);

                if (ocitajRazinu == iducaRazina) {
                    SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
                    editor.putBoolean("keyMain", true);
                    editor.apply();

                }
                SharedPreferences prefs2 = getSharedPreferences("Level", MODE_PRIVATE);
                boolean main = prefs2.getBoolean("keyMain", true);

                if (ocitajRazinu < iducaRazina + 1 && main) {
                    iducaRazina++;
                    SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
                    editor.putInt("keyName", iducaRazina);
                    editor.apply();
                } else {
                    ocitajRazinu++;
                    SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
                    editor.putInt("keyName2", ocitajRazinu);
                    editor.apply();
                }
                PlaySound(0);
                View dialoglayout = LayoutInflater.from(Zadatak.this).inflate(R.layout.popup, null);
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setView(dialoglayout);
                alertDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Zadatak.this, Zadatak.class);
                        startActivity(intent); // start same activity
                        finish(); // destroy older activity
                    }
                }, 500);
            } else {
                PlaySound(3);
                SharedPreferences prefs = getSharedPreferences("Options", MODE_PRIVATE);
                int retoredBoolean = prefs.getInt("keyVibrate", 1);
                if (retoredBoolean == 1) {

                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vib.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vib.vibrate(100);
                    }
                }
                Animation mShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
                ispis.startAnimation(mShakeAnimation);
                ispis.setText(null);
                ispis.setHint(getResources().getString(R.string.string8));
                brojPokusaja++;
                if (brojPokusaja >= 3) {
                    if (adAmount < 3) {
                        PritisniMe();
                    }
                    brojPokusaja = 0;
                }
            }
        }
    }

    public void Hint(View v) {
        final ImageButton hint_b = (ImageButton) v;
        PlaySound(2);
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
                PlaySound(2);
                if (!(mAd.isLoaded())) {
                    if (haveNetworkConnection()) {
                        hintBtn.setText(getResources().getText(R.string.string4));
                        mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
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
                    Animation mShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
                    solutionBtn.startAnimation(mShakeAnimation);
                    PlaySound(3);
                } else {
                    PlaySound(2);
                    if (!(mAd.isLoaded())) {
                        if (haveNetworkConnection()) {
                            solutionBtn.setText(getResources().getText(R.string.string4));
                            mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
                        } else {
                            String poruka = "Check your internet connection!";
                            Toast toast = Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        adAmount = 2;
                        mAd.show();
                        alertDialog.cancel();
                        jeliPogledanHint = 0;
                    }
                }

            }
        });
        alertDialog.show();
        ImageButton closeBtn = dialoglayout.findViewById(R.id.hintClose);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound(2);
                alertDialog.cancel();
            }
        });
    }

    public void OptionsShow() {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int vibrateValue = prefs2.getInt("keyVibrate", 1);
        int soundValue = prefs2.getInt("keySound", 1);
        if (vibrateValue == 1) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.options, null);
            promptView.setBackground(getResources().getDrawable(R.drawable.alertbox));
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            ImageButton btn = promptView.findViewById(R.id.imageVibrate);
            ImageButton btn2 = promptView.findViewById(R.id.imageSound);
            ImageButton btnClose = promptView.findViewById(R.id.alertClose);
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
                    PlaySound(2);
                    alertDialog.cancel();
                }
            });

        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View promptView = layoutInflater.inflate(R.layout.options, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            ImageButton btn = promptView.findViewById(R.id.imageVibrate);
            ImageButton btn2 = promptView.findViewById(R.id.imageSound);
            ImageButton btnClose = promptView.findViewById(R.id.alertClose);
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
                    PlaySound(2);
                    alertDialog.cancel();
                }
            });
        }
    }

    public void Options(View v) {
        ImageButton vibrate = (ImageButton) v;
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

            PlaySound(2);
        }
    }
    public void Options3()
    {
        broj=1;
        new CountDownTimer(3000,1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        broj=broj+1;
                    }
                });
                Log.v("sadokasd"," "+broj);
                if(broj>=3)
                {
                    cancel();
                    SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
                    editor.putInt("keyName", 76);
                    editor.apply();
                    View dialoglayout = LayoutInflater.from(Zadatak.this).inflate(R.layout.popup, null);
                    TextView textView=dialoglayout.findViewById(R.id.poruka_naslov);
                    textView.setText("Good job!");
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setView(dialoglayout);
                    alertDialog.show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Zadatak.this, Zadatak.class);
                            startActivity(intent); // start same activity
                            finish(); // destroy older activity
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
    public void PlaySound(int a) {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            soundPool.play(sound[a], 1, 1, 1, 0, 1f);
        }
    }

    public void PritisniMe() {
        final ImageButton hintBtn = findViewById(R.id.hint);
        hintBtn.setBackground(getResources().getDrawable(R.drawable.hint_button_glow));
        Animation enlargeAnimation = AnimationUtils.loadAnimation(context, R.anim.large);
        hintBtn.startAnimation(enlargeAnimation);
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {
        jeliPogledanHint = 1;
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
        if (adAmount == 1) {
            afterAd = dialoglayout.findViewById(R.id.hint_solution);
            afterAd.setText(hint);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setView(dialoglayout);
            alertDialog.show();
            ImageButton closeBtn = dialoglayout.findViewById(R.id.hintClose);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaySound(2);
                    alertDialog.cancel();
                }
            });
            adAmount = 2;
            lightBulb.setImageResource(R.drawable.lightbulb);
        } else {
            afterAd = dialoglayout.findViewById(R.id.hint_solution);
            afterAd.setText("" + tocnoRjesenje);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setView(dialoglayout);
            alertDialog.show();
            ImageButton closeBtn = dialoglayout.findViewById(R.id.hintClose);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaySound(2);
                    alertDialog.cancel();
                }
            });
            lightBulb.setImageResource(R.drawable.lightbulb_on);
            adAmount = 3;
        }

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
