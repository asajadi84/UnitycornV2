package ir.unitycorn.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.hanks.htextview.evaporate.EvaporateTextView;

import java.util.Locale;

import ir.unitycorn.app.R;

public class SplashActivity extends AppCompatActivity {

    CountDownTimer splashCountDownTimer;
    long splashCountDownMillisInFuture = 1500;
    long splashCountDownInterval = 750;

    Runnable splashRunnable;
    Handler splashHandler;
    long splashDisplayTime = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make Everything RTL
        Locale farsi = new Locale("fa","IR");
        getResources().getConfiguration().setLocale(farsi);

        setContentView(R.layout.activity_splash);

        //Animate The Logo
        ImageView splashLogo = findViewById(R.id.splash_logo);
        Drawable splashLogoDrawable = splashLogo.getDrawable();
        if(splashLogoDrawable instanceof AnimatedVectorDrawableCompat){
            AnimatedVectorDrawableCompat splashLogoAnimatedVectorDrawableCompat = (AnimatedVectorDrawableCompat) splashLogoDrawable;
            splashLogoAnimatedVectorDrawableCompat.start();
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(splashLogoDrawable instanceof  AnimatedVectorDrawable){
                AnimatedVectorDrawable splashLogoAnimatedVectorDrawable = (AnimatedVectorDrawable) splashLogoDrawable;
                splashLogoAnimatedVectorDrawable.start();
            }
        }

        //Animate The Title
        final EvaporateTextView splashTitle = findViewById(R.id.splash_title);
        splashCountDownTimer = new CountDownTimer(splashCountDownMillisInFuture, splashCountDownInterval) {

            int currentAnimationState = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                switch (currentAnimationState){
                    case 0:
                        splashTitle.animateText("Unity + Unicorn");
                        break;

                    case 1:
                        splashTitle.animateText("Unitycorn");
                        findViewById(R.id.splash_progressbar).setVisibility(View.VISIBLE);
                        break;
                }
                currentAnimationState++;
            }

            @Override
            public void onFinish() {

            }
        };
        splashCountDownTimer.start();

        //Starting The Next Activity After Splash Display Time
        splashRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intentAfterSplash = getIntentAfterSplash();
                SplashActivity.this.startActivity(intentAfterSplash);
                SplashActivity.this.finish();
            }
        };
        splashHandler = new Handler();
        splashHandler.postDelayed(splashRunnable, splashDisplayTime);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Stop Starting The Next Activity And Animation On User App Quit
        splashHandler.removeCallbacks(splashRunnable);
        splashCountDownTimer.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //Start The Next Activity Right Away
        splashHandler.postDelayed(splashRunnable, 0);
    }

    //Decides What Activity Should Start After Splash Animation
    private Intent getIntentAfterSplash(){
        SharedPreferences settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if(settingsPrefs.getBoolean("intro_viewed", false)){
            return new Intent(SplashActivity.this, MainActivity.class);
        }else{
            return new Intent(SplashActivity.this, IntroActivity.class);
        }
    }
}
