package ir.unitycorn.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;

import com.chyrta.onboarder.OnboarderActivity;
import com.chyrta.onboarder.OnboarderPage;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.unitycorn.app.R;

public class IntroActivity extends OnboarderActivity {

    SharedPreferences settingsPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);

        //Initialize Toasty
        Typeface iranSans = ResourcesCompat.getFont(getApplicationContext(), R.font.iransans);
        Toasty.Config.getInstance()
                .setToastTypeface(iranSans)
                .allowQueue(false)
                .apply();

        List<OnboarderPage> onboarderPages = new ArrayList<>();

        //Page I
        OnboarderPage onboarderPage1 = new OnboarderPage(R.string.intro_title_1, R.string.intro_description_1, R.drawable.intro_pic_1);
        onboarderPage1.setTitleColor(settingsPrefs.getBoolean("dark_mode", false) ? android.R.color.white : android.R.color.black);
        onboarderPage1.setDescriptionColor(settingsPrefs.getBoolean("dark_mode", false) ? android.R.color.white : android.R.color.black);
        onboarderPage1.setBackgroundColor(settingsPrefs.getBoolean("dark_mode", false) ? R.color.colorPrimaryDark : R.color.colorAccent);
        onboarderPages.add(onboarderPage1);

        //Page II
        OnboarderPage onboarderPage2 = new OnboarderPage(R.string.intro_title_2, R.string.intro_description_2, R.drawable.intro_pic_2);
        onboarderPage2.setTitleColor(settingsPrefs.getBoolean("dark_mode", false) ? android.R.color.white : android.R.color.black);
        onboarderPage2.setDescriptionColor(settingsPrefs.getBoolean("dark_mode", false) ? android.R.color.white : android.R.color.black);
        onboarderPage2.setBackgroundColor(settingsPrefs.getBoolean("dark_mode", false) ? R.color.colorPrimaryDark : R.color.colorAccent);
        onboarderPages.add(onboarderPage2);

        //Page III
        OnboarderPage onboarderPage3 = new OnboarderPage(R.string.intro_title_3, R.string.intro_description_3, R.drawable.intro_pic_3);
        onboarderPage3.setTitleColor(settingsPrefs.getBoolean("dark_mode", false) ? android.R.color.white : android.R.color.black);
        onboarderPage3.setDescriptionColor(settingsPrefs.getBoolean("dark_mode", false) ? android.R.color.white : android.R.color.black);
        onboarderPage3.setBackgroundColor(settingsPrefs.getBoolean("dark_mode", false) ? R.color.colorPrimaryDark : R.color.colorAccent);
        onboarderPages.add(onboarderPage3);

        setSkipButtonHidden();

        setFinishButtonTitle(settingsPrefs.getBoolean("intro_viewed", false) ? R.string.intro_button_after_intro_viewed : R.string.intro_button_before_intro_viewed);

        setOnboardPagesReady(onboarderPages);

    }

    @Override
    public void onFinishButtonPressed() {
        exitTheIntro();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitTheIntro();
    }

    private void exitTheIntro(){
        if(settingsPrefs.getBoolean("intro_viewed", false)){
            //This Means The Intro Has Opened From About Activity
            finish();
        }else{
            settingsPrefs.edit().putBoolean("intro_viewed", true).apply();
            Toasty.custom(this,
                    getResources().getString(R.string.intro_toast_message), null,
                    getResources().getColor(R.color.colorPrimary),
                    getResources().getColor(android.R.color.white),
                    Toasty.LENGTH_LONG, false, true).show();
            Intent mainActivityIntent = new Intent(IntroActivity.this, MainActivity.class);
            IntroActivity.this.startActivity(mainActivityIntent);
            IntroActivity.this.finish();
        }
    }

    //Change Default Font To Iran Sans
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
