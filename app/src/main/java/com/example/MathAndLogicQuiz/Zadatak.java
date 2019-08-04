package com.example.MathAndLogicQuiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    int jeliPogledanHint=0;
    String hint = null;
    String imeRazine;
    MediaPlayer btnPress;
    RewardedVideoAd mAd;
    TextView afterAd;
    ImageButton lightBulb;
    final Context context = this;


    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs3 = getSharedPreferences("Options", MODE_PRIVATE);
        int themeValue = prefs3.getInt("keyTheme", 1);
        if (themeValue==1)
        {
            setTheme(R.style.AppTheme);
        }else
            {
                setTheme(R.style.AppTheme2);
            }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zadatak);
        TextView zad=findViewById(R.id.zadatak_text);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        TextView text = findViewById(R.id.toolbar_title);
        View view=findViewById(R.id.toolbar_linear);
        if(themeValue==1)
        {

            zad.setBackgroundResource(R.color.zadatak);
            toolbar.getContext().setTheme(R.style.Toolbar);
            text.setBackgroundResource(R.color.colorPrimaryDark);
            view.setBackgroundResource(R.color.colorPrimaryDark);
        }
        else
            {

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
        OdaberiLevel();
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadAds();
            }
        },100);
    }
    public void LoadAds()
    {
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/5224354917");
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }
    public void OdaberiLevel() {
        TextView zadatak = findViewById(R.id.zadatak_text);
        ImageView zadatakSlika=findViewById(R.id.zadatak_slika);
        switch (ocitajRazinu) {
            case 1:
                zadatak.setText(R.string.lvl_1);
                tocnoRjesenje = 7;
                hint = "Number after 6";
                break;
            case 2:
                zadatak.setText(R.string.lvl_2);
                tocnoRjesenje = 15;
                hint = "Number before 16";
                break;
            case 3:
                zadatak.setText(R.string.lvl_3);
                tocnoRjesenje = 42;
                hint = "Number multiplied by 7";
                break;
            case 4:
                zadatak.setText(R.string.lvl_4);
                tocnoRjesenje = 8;
                hint = "Multiply with 8";
                break;
            case 5:
                zadatak.setText(R.string.lvl_5);
                tocnoRjesenje = 17;
                hint = "Adding with a number between 10 and 15";
                break;
            case 6:
                zadatak.setText(R.string.lvl_6);
                tocnoRjesenje = 20;
                hint = "Adding with a number between 10 and 15";
                break;
            case 7:
                zadatak.setText(R.string.lvl_7);
                tocnoRjesenje = 24;
                hint = "Multiply then add one more";
                break;
            case 8:
                zadatak.setText(R.string.lvl_8);
                tocnoRjesenje = 20;
                hint = "Add a multiplication of 2 ";
                break;
            case 9:
                zadatak.setText(R.string.lvl_9);
                tocnoRjesenje = 13;
                hint = "Add a number, subtract it, add again";
                break;
            case 10:
                zadatak.setText(R.string.lvl_10);
                tocnoRjesenje = 4;
                hint = "Multiply!";
                break;
            case 11:
                zadatak.setText(R.string.lvl_11);
                tocnoRjesenje = 10;
                hint = "Multiply first, add second";
                break;
            case 12:
                zadatak.setText(R.string.lvl_12);
                tocnoRjesenje = 49;
                hint = "Multiply first, add and subtract second";
                break;
            case 13:
                zadatak.setText(R.string.lvl_13);
                tocnoRjesenje = 29;
                hint = "1.Brackets, 2.Multiplications, 3.Adding and subtracting";
                break;
            case 14:
                zadatak.setText(R.string.lvl_14);
                tocnoRjesenje = 2;
                hint = "Multiply first, add and subtract second";
                break;
            case 15:
                zadatak.setText(R.string.lvl_15);
                tocnoRjesenje = 10;
                hint = "Multiply digit with a digit";
                break;
            case 16:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.rectangles);
                tocnoRjesenje = 14;
                hint = "Count the big ones";
                break;
            case 17:
                zadatak.setText(R.string.lvl_17);
                tocnoRjesenje = 87;
                hint = "Flip the phone upside down";
                break;
            case 18:
                zadatak.setText(R.string.lvl_18);
                tocnoRjesenje = 4;
                hint = "x-y=60\n+x+y=100";
                break;
            case 19:
                zadatak.setText(R.string.lvl_19);
                tocnoRjesenje = 15;
                hint = "Divide and multiply, then add";
                break;
            case 20:
                zadatak.setText(R.string.lvl_20);
                tocnoRjesenje = 12;
                hint = "From left to right";
                break;
            case 21:
                zadatak.setText(R.string.lvl_21);
                tocnoRjesenje = 30;
                hint = "Number below is the multiplier";
                break;
            case 22:
                zadatak.setText(R.string.lvl_22);
                tocnoRjesenje = 7;
                hint = "Add the top to middle to get bottom";
                break;
            case 23:
                zadatak.setText(R.string.lvl_23);
                tocnoRjesenje = 3;
                hint = "Add the columns";
                break;
            case 24:
                zadatak.setText(R.string.lvl_24);
                tocnoRjesenje = 22;
                hint = "( x ) - ( - )";
                break;
            case 25:
                zadatak.setText(R.string.lvl_25);
                tocnoRjesenje = 18;
                hint = "Add digits, then numbers ";
                break;
            case 26:
                zadatak.setText(R.string.lvl_26);
                tocnoRjesenje = 5;
                hint = "Multiply with 3 then subtract";
                break;
            case 27:
                zadatak.setText(R.string.lvl_27);
                tocnoRjesenje = 54;
                hint = "Multiply with 2 then subtract";
                break;
            case 28:
                zadatak.setText(R.string.lvl_28);
                tocnoRjesenje = 51;
                hint = "Multiply with 2 then subtract";
                break;
            case 29:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl29);
                tocnoRjesenje = 2;
                hint = "Zig-zag pattern";
                break;
            case 30:
                zadatak.setText(R.string.lvl_30);
                tocnoRjesenje = 3;
                hint = "Add left and right, divide with a number to get the middle";
                break;
            case 31:
                zadatak.setText(R.string.lvl_31);
                tocnoRjesenje = 2;
                hint = "Even numbers";
                break;
            case 32:
                zadatak.setText(R.string.lvl_32);
                tocnoRjesenje = 8;
                hint = "Incrementing the number you add by 1";
                break;
            case 33:
                zadatak.setText(R.string.lvl_33);
                tocnoRjesenje = 16;
                hint = "Pattern of added numbers";
                break;
            case 34:
                zadatak.setText(R.string.lvl_34);
                tocnoRjesenje = 21;
                hint = "Add power of 2 (2^n)";
                break;
            case 35:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl35);
                tocnoRjesenje = 29;
                hint = "Adding number to the right side to get the opposite side number";
                break;
            case 36:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl36);
                tocnoRjesenje = 1;
                hint = "Add numbers on the same side of the triangle";
                break;
            case 37:
                zadatak.setText(R.string.lvl_37);
                tocnoRjesenje = 46;
                hint = "Add odd numbers";
                break;
            case 38:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl38);
                tocnoRjesenje = 10;
                hint = "Multipy bas numbers, add to top";
                break;
            case 39:
                zadatak.setText(R.string.lvl_39);
                tocnoRjesenje = 0;
                hint = "Count the circles in numbers";
                break;
            case 40:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl40);
                tocnoRjesenje = 7;
                hint = "Top and bottom half are equal";
                break;
            case 41:
                zadatak.setText(R.string.lvl_41);
                tocnoRjesenje = 8;
                hint = "Fibonacci";
                break;
            case 42:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl42);
                tocnoRjesenje = 8;
                hint = "+x -> (x-1) -> +x";
                break;
            case 43:
                zadatak.setText(R.string.lvl_43);
                tocnoRjesenje = 56;
                hint = "Do the opposite";
                break;
            case 44:
                zadatak.setText(R.string.lvl_44);
                tocnoRjesenje = 49;
                hint = "Number multiplied by itself";
                break;
            case 45:
                zadatak.setText(R.string.lvl_45);
                tocnoRjesenje = 42;
                hint = "Multiply left with left + 1";
                break;
            case 46:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl46);
                tocnoRjesenje = 4370;
                hint = "Patterns";
                break;
            case 47:
                zadatak.setText(R.string.lvl_47);
                tocnoRjesenje = 14;
                hint = "| = 10 , V = 5 , X = 10";
                break;
            case 48:
                zadatak.setText(R.string.lvl_48);
                tocnoRjesenje = 23;
                hint = "Prime numbers";
                break;
            case 49:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl49);
                tocnoRjesenje = 15;
                hint = "Add opposite numbers to get the middle";
                break;
            case 50:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl50);
                tocnoRjesenje = 16;
                hint = "Add a number to left triangle numbers";
                break;
            case 51:
                zadatak.setText(R.string.lvl_51);
                tocnoRjesenje = 1;
                hint = "13";
                break;
            case 52:
                zadatak.setText(R.string.lvl_52);
                tocnoRjesenje = 55;
                hint = "A^2 + B";
                break;
            case 53:
                zadatak.setText(R.string.lvl_53);
                tocnoRjesenje = 18;
                hint = "Add a number, but in a zig-zag pattern";
                break;
            case 54:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl54);
                tocnoRjesenje = 3;
                hint = "Multiply with middle to get the opposite";
                break;
            case 55:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl55);
                tocnoRjesenje = 48;
                hint = "Multiples of 6";
                break;
            case 56:
                zadatak.setText(R.string.lvl_56);
                tocnoRjesenje = 29;
                hint = "AB=B(A+B)";
                break;
            case 57:
                zadatak.setText(R.string.lvl_57);
                tocnoRjesenje = 13;
                hint = "\u221AA + \u221AB";
                break;
            case 58:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl58);
                tocnoRjesenje = 48;
                hint = "Add and subtract opposite numbers to get inner ones";
                break;
            case 59:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl59);
                tocnoRjesenje = 3;
                hint = "Add 2 diagonal numbers";
                break;
            case 60:
                zadatak.setText(R.string.lvl_60);
                tocnoRjesenje = 12;
                hint = "Add outer numbers, add 2, get middle";
                break;
            case 61:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl61);
                tocnoRjesenje = 3;
                hint = "(A + B)/(A - B)";
                break;
            case 62:
                zadatak.setText(R.string.lvl_62);
                tocnoRjesenje = 81;
                hint = "3^2 , (3+3)^2 ,...";
                break;
            case 63:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl63);
                tocnoRjesenje = 10;
                hint = "Middle of the two numbers below";
                break;
            case 64:
                zadatak.setText(R.string.lvl_64);
                tocnoRjesenje = 13;
                hint = "Add the digits";
                break;
            case 65:
                zadatak.setText(R.string.lvl_65);
                tocnoRjesenje = 93;
                hint = "100, 150 , 200 , 250 ";
                break;
            case 66:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl66);
                tocnoRjesenje = 17;
                hint = "Add all the numbers in a row";
                break;
            case 67:
                zadatak.setText(R.string.lvl_67);
                tocnoRjesenje = 7;
                hint = "Add neighbouring numbers and subtract with a number";
                break;
            case 68:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl68);
                tocnoRjesenje = 6;
                hint = "Add 2 lower numbers to get the third";
                break;
            case 69:
                zadatak.setText(R.string.lvl_69);
                tocnoRjesenje = 420;
                hint = "Marijuana number";
                break;
            case 70:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl70);
                tocnoRjesenje = 44;
                hint = "Increment circle segment to get the neighbouring circle segment";
                break;
            case 71:
                zadatak.setText(R.string.lvl_71);
                tocnoRjesenje = 27;
                hint = "Cubed number";
                break;
            case 72:
                zadatak.setVisibility(zadatak.GONE);
                zadatakSlika.setVisibility(zadatakSlika.VISIBLE);
                zadatakSlika.setImageResource(R.drawable.lvl72);
                tocnoRjesenje = 9;
                hint = "Do something with the top numbers to get the bottom ones";
                break;
            default:
                zadatak.setText(R.string.lvl_1);
                tocnoRjesenje = 7;
                hint = "Number after 6";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        PlaySound(3);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                PlaySound(3);
                OptionsShow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void upis(View v) {
        Button botun = (Button) v;
        PlaySound(2);
        TextView ispis = findViewById(R.id.upis);
        if (ispis.getText() == null) {
            ispis.setText(botun.getText());
        } else {
            ispis.append(botun.getText());
        }

    }

    public void Clear(View v) {
        PlaySound(3);
        TextView ispis = findViewById(R.id.upis);
        ispis.setText(null);

    }


    public void Check(View v) {
        TextView textView = findViewById(R.id.upis);
        int upisanoRj;
        try {
            upisanoRj = Integer.parseInt(textView.getText().toString());
        } catch (Exception e) {
            upisanoRj = 0;
        }
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
            PlaySound(1);
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
            PlaySound(4);
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
            textView.startAnimation(mShakeAnimation);
            textView.setText(null);
            brojPokusaja++;
            if (brojPokusaja >= 3) {
                if(adAmount<3)
                {
                    PritisniMe();
                }
                brojPokusaja = 0;
            }
        }

    }

    public void Hint(View v) {
        final ImageButton hint_b = (ImageButton) v;
        PlaySound(3);
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
                PlaySound(3);
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
                    String a="Watch ad \nfor solution";
                    solutionBtn.setText(a);
                }
            }
        });
        solutionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound(3);
                if (adAmount < 2) {
                    String a="Look at hint first";
                    solutionBtn.setText(a);
                    Animation mShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
                    solutionBtn.startAnimation(mShakeAnimation);
                    PlaySound(4);
                }
                else
                {
                    if (!(mAd.isLoaded())) {
                        if (haveNetworkConnection()) {
                            solutionBtn.setText(getResources().getText(R.string.string4));
                            mAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
                        } else {
                            String poruka = "Check your internet connection!";
                            Toast toast = Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    else {
                        adAmount = 2;
                        mAd.show();
                        alertDialog.cancel();
                        jeliPogledanHint=0;
                    }
                }

            }
        });
        alertDialog.show();
        ImageButton closeBtn = dialoglayout.findViewById(R.id.hintClose);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound(3);
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
                    PlaySound(3);
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
                    PlaySound(3);
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

            PlaySound(3);
        }
    }

    public void PlaySound(int a) {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            switch (a) {
                case 1:
                    btnPress = MediaPlayer.create(context, R.raw.correct);
                    btnPress.start();
                    break;
                case 2:
                    btnPress = MediaPlayer.create(context, R.raw.input);
                    btnPress.start();
                    break;
                case 3:
                    btnPress = MediaPlayer.create(context, R.raw.thump);
                    btnPress.start();
                    break;
                case 4:
                    btnPress = MediaPlayer.create(context, R.raw.wrong);
                    btnPress.start();
                    break;
                default:
                    btnPress = MediaPlayer.create(context, R.raw.thump);
                    btnPress.start();
                    break;
            }
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
        jeliPogledanHint=1;
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
            afterAd=dialoglayout.findViewById(R.id.hint_solution);
            afterAd.setText(hint);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setView(dialoglayout);
            alertDialog.show();
            ImageButton closeBtn = dialoglayout.findViewById(R.id.hintClose);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaySound(3);
                    alertDialog.cancel();
                }
            });
            adAmount=2;
            lightBulb.setImageResource(R.drawable.lightbulb);
        } else {
            afterAd=dialoglayout.findViewById(R.id.hint_solution);
            afterAd.setText(""+tocnoRjesenje);
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setView(dialoglayout);
            alertDialog.show();
            ImageButton closeBtn = dialoglayout.findViewById(R.id.hintClose);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaySound(3);
                    alertDialog.cancel();
                }
            });
            lightBulb.setImageResource(R.drawable.lightbulb_on);
            adAmount=3;
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
