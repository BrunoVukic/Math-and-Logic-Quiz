package com.example.MathAndLogicQuiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;

import static android.content.Context.MODE_PRIVATE;

public class Theme {
    Context currentContext;
    int themeValue;
    public void settingMainTheme()
    {
        SharedPreferences prefs = currentContext.getSharedPreferences("Options", MODE_PRIVATE);
        themeValue = prefs.getInt("keyTheme", 1);
        if (themeValue == 1) {
            currentContext.setTheme(R.style.AppTheme);
        } else {
            currentContext.setTheme(R.style.AppTheme2);
        }
    }
    public void settingTheme(int a)
    {
        Activity activity=(Activity) currentContext;
        Toolbar toolbar = activity.findViewById(R.id.toolbarRazine);
        TextView title = activity.findViewById(R.id.toolbar_title);

        if (themeValue == 1) {
            toolbar.getContext().setTheme(R.style.Toolbar);
            toolbar.setBackgroundResource(R.color.colorPrimaryDark);
            title.setBackgroundResource(R.color.colorPrimaryDark);
            if (a==0)
            {
                TextView mainTitle=activity.findViewById(R.id.titleView);
                mainTitle.setTextColor(currentContext.getResources().getColor(R.color.colorAccent));
            }
            else if(a==1)
            {
                ImageView question=activity.findViewById(R.id.zadatak_slika);
                question.setBackgroundResource(R.color.zadatak);
            }

        } else {
            activity.setTheme(R.style.AppTheme2);
            toolbar.getContext().setTheme(R.style.Toolbar2);
            toolbar.setBackgroundResource(R.color.colorSecondary);
            title.setBackgroundResource(R.color.colorSecondary);
            if (a==0)
            {
                TextView mainTitle=activity.findViewById(R.id.titleView);
                mainTitle.setTextColor(currentContext.getResources().getColor(R.color.colorPrimary));
            }
            else if(a==1)
            {
                ImageView question=activity.findViewById(R.id.zadatak_slika);
                question.setBackgroundResource(R.color.zadatak2);
            }
        }

    }
}
