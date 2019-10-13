package com.example.MathAndLogicQuiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import static android.content.Context.MODE_PRIVATE;

public class Options {
    Context currentContext;
    Sound sound;
    public void optionsShow(boolean activityMain) {
        final SharedPreferences prefs2 = currentContext.getSharedPreferences("Options", MODE_PRIVATE);
        final int vibrateValue = prefs2.getInt("keyVibrate", 1);
        final int soundValue = prefs2.getInt("keySound", 1);
        final int themeValue = prefs2.getInt("keyTheme", 1);
        LayoutInflater layoutInflater = LayoutInflater.from(currentContext);
        View promptView;
        if (activityMain)
        {
              promptView = layoutInflater.inflate(R.layout.options_main, null);
        }else
            {
                  promptView = layoutInflater.inflate(R.layout.options, null);
            }
        final AlertDialog alertDialog = new AlertDialog.Builder(currentContext).create();
        final ImageButton btn = promptView.findViewById(R.id.imageVibrate);
        final ImageButton btn2 = promptView.findViewById(R.id.imageSound);
        ImageButton btnClose = promptView.findViewById(R.id.alertClose);
        sound.currentContext=currentContext;
        if (activityMain)
        {
            final WebView webView = promptView.findViewById(R.id.webview);
            TextView privacy = promptView.findViewById(R.id.buttonPrivacy);
            TextView terms = promptView.findViewById(R.id.button_t_c);
            final Button btnDark = promptView.findViewById(R.id.buttonDark);
            final Button btnLight = promptView.findViewById(R.id.buttonLight);
            if (themeValue == 1) {
                btnDark.setBackgroundResource(R.drawable.border2);

            } else {
                btnLight.setBackgroundResource(R.drawable.border1);
            }
            btnDark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(themeValue!=1)
                    {
                        optionsTheme(alertDialog, btnDark);
                    }
                }
            });
            btnLight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(themeValue!=0)
                    {
                        optionsTheme(alertDialog, btnLight);
                    }
                }
            });
            privacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sound.playSound(2);
                    btnDark.setVisibility(btnDark.GONE);
                    btnLight.setVisibility(btnLight.GONE);
                    openURL("file:///android_asset/privacy_policy.html", webView);

                }
            });
            terms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sound.playSound(2);
                    btnDark.setVisibility(btnDark.GONE);
                    btnLight.setVisibility(btnLight.GONE);
                    openURL("file:///android_asset/terms_and_conditions.html", webView);
                }
            });
        }
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
                    sound.playSound(2);
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
                    sound.playSound(2);
                    alertDialog.cancel();
                }
            });
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsVibration(btn);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsSound(btn2);
            }
        });

    }
    private void optionsVibration(ImageButton vibrateBtn) {
        sound.playSound(2);
        SharedPreferences prefs2 = currentContext.getSharedPreferences("Options", MODE_PRIVATE);
        int vibrateValue = prefs2.getInt("keyVibrate", 1);
        if (vibrateValue == 1) {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keyVibrate", 0);
            editor.apply();
            vibrateBtn.setImageResource(R.drawable.vibrate_off);
        } else {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keyVibrate", 1);
            editor.apply();
            vibrateBtn.setImageResource(R.drawable.vibrate);
            Vibrator vib = (Vibrator) currentContext.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vib.vibrate(100);
            }
        }
    }
    private void optionsSound(ImageButton soundBtn) {

        SharedPreferences prefs2 = currentContext.getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keySound", 0);
            editor.apply();
            soundBtn.setImageResource(R.drawable.volume_off);
        } else {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keySound", 1);
            editor.apply();
            soundBtn.setImageResource(R.drawable.volume_high);
            sound.playSound(2);
        }
    }
    private void optionsTheme(AlertDialog alertDialog, Button btn) {
        Activity activity=(Activity) currentContext;
        SharedPreferences prefs2 = currentContext.getSharedPreferences("Options", MODE_PRIVATE);
        int themeValue = prefs2.getInt("keyTheme", 1);
        if (themeValue == 1) {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keyTheme", 0);
            editor.apply();
            btn.setBackgroundResource(R.drawable.border1);
            alertDialog.cancel();
            activity.recreate();
        } else {
            SharedPreferences.Editor editor = prefs2.edit();
            editor.putInt("keyTheme", 1);
            editor.apply();
            btn.setBackgroundResource(R.drawable.border2);
            alertDialog.cancel();
            activity.recreate();
        }
    }
    private void openURL(String url, WebView webView) {
        webView.setVisibility(webView.VISIBLE);
        webView.loadUrl(url);
    }


}
