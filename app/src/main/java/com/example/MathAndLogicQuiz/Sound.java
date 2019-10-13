package com.example.MathAndLogicQuiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import static android.content.Context.MODE_PRIVATE;

public class Sound {
    Context currentContext;
    private SoundPool soundPool;
    private int[] sound;
    public void createSound()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound = new int[4];
        sound[0] = soundPool.load(currentContext, R.raw.correct, 1);
        sound[1] = soundPool.load(currentContext, R.raw.input, 1);
        sound[2] = soundPool.load(currentContext, R.raw.thump, 1);
        sound[3] = soundPool.load(currentContext, R.raw.wrong, 1);
    }
    public void playSound(int a) {
        SharedPreferences prefs2 = currentContext.getSharedPreferences("Options", MODE_PRIVATE);
        int soundValue = prefs2.getInt("keySound", 1);
        if (soundValue == 1) {
            soundPool.play(sound[a], 1, 1, 1, 0, 1f);
        }
    }
}
