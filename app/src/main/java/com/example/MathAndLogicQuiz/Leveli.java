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
    Button razina;
    String nas;
    TextView naslov;
    SoundPool soundPool;
    int[] sound;
    final Context context = this;

    @Override
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
        setContentView(R.layout.activity_leveli);
        Toolbar toolbar = findViewById(R.id.toolbarRazine);
        naslov = findViewById(R.id.toolbar_title);
        View view=findViewById(R.id.toolbar_linear);
        if(themeValue==1)
        {


            toolbar.getContext().setTheme(R.style.Toolbar);
            naslov.setBackgroundResource(R.color.colorPrimaryDark);
            view.setBackgroundResource(R.color.colorPrimaryDark);
        }
        else
        {


            toolbar.getContext().setTheme(R.style.Toolbar2);
            naslov.setBackgroundResource(R.color.colorSecondary);
            view.setBackgroundResource(R.color.colorSecondary);
        }
        setSupportActionBar(toolbar);
        onResume();
        nas = getResources().getString(R.string.string2);
        naslov = findViewById(R.id.toolbar_title);
        naslov.setText(nas);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
        FillLevels();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound=new int[2];
        sound[0]=soundPool.load(context,R.raw.thump,1);
        sound[1]=soundPool.load(context,R.raw.wrong,1);
    }
    public void FillLevels()
    {
        int brojRazina=1;
        do {
            String buttonID = "razina" + brojRazina;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
           razina = findViewById(resID);
            razina.setText(String.valueOf(brojRazina));
            brojRazina++;
        }while (brojRazina<=76);
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
                PlaySound(0);
                OptionsShow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        obojiPredeno();
    }

    @Override
    public void onBackPressed() {
        PlaySound(0);
        super.onBackPressed();
    }

    public void obojiPredeno() {
        SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
        int predeniLeveli = prefs.getInt("keyName", 1);
        Button predeni;
        for (int i = 1; i < predeniLeveli; i++) {
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
        PlaySound(0);
        onBackPressed();
        return true;
    }

    public void levelInfo(View v) {
        Button b = (Button) v;
        SharedPreferences prefs = getSharedPreferences("Level", MODE_PRIVATE);
        int iducaRazina = prefs.getInt("keyName", 1);
        String BtnText = b.getText().toString();
        int pritisnutiLevel = Integer.parseInt(BtnText);
        if (pritisnutiLevel <= iducaRazina || pritisnutiLevel == 1)
        /*if(iducaRazina>0)*/{
            PlaySound(0);
            SharedPreferences.Editor editor = getSharedPreferences("Level", MODE_PRIVATE).edit();
            editor.putBoolean("keyMain", false);
            editor.apply();
            Intent intent = new Intent(this, Zadatak.class);
            intent.putExtra("extra", pritisnutiLevel);
            startActivity(intent);
        } else {
            Button morasUc;
            String poruka = "No skipping levels!";
            Toast toast = Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_SHORT);
            toast.show();
            String buttonID = "razina" + iducaRazina;
            int btnID = getResources().getIdentifier(buttonID, "id", getPackageName());
            morasUc = findViewById(btnID);
            if (iducaRazina % 2 == 0) {
                morasUc.setBackground(this.getResources().getDrawable(R.drawable.round_button_moras_taj2));
                Animation mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
                morasUc.startAnimation(mShakeAnimation);
                PlaySound(1);
            } else {
                morasUc.setBackground(this.getResources().getDrawable(R.drawable.round_button_moras_taj));
                Animation mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
                morasUc.startAnimation(mShakeAnimation);
                PlaySound(1);
            }
        }


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
                    PlaySound(0);
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
                    PlaySound(0);
                    alertDialog.cancel();
                }
            });
        }
    }

    public void Options(View v) {
        ImageButton vibrate = (ImageButton) v;
        PlaySound(0);
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

            PlaySound(0);
        }
    }
    public void PlaySound(int a) {
        SharedPreferences prefs2 = getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            soundPool.play(sound[a],1,1,1,0,1f);
        }
    }
}

