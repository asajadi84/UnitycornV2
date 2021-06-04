package ir.unitycorn.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mursaat.extendedtextview.AnimatedGradientTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.unitycorn.app.DarkModeCapable;
import ir.unitycorn.app.R;
import shortbread.Shortcut;

public class AboutActivity extends AppCompatActivity implements DarkModeCapable {

    SharedPreferences settingsPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);

        if(settingsPrefs.getBoolean("dark_mode", false)){
            setTheme(R.style.AppThemeMiscDark);
        }

        super.onCreate(savedInstanceState);

        Locale farsi = new Locale("fa", "IR");
        getResources().getConfiguration().setLocale(farsi);

        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.main_menu_about);

        if(settingsPrefs.getBoolean("dark_mode", false)){
            makeViewsDark();
        }

        //initialize toasty
        Typeface iransans = ResourcesCompat.getFont(getApplicationContext(), R.font.iransans);
        Toasty.Config.getInstance()
                .setToastTypeface(iransans)
                .allowQueue(false)
                .apply();

        //START SHAKE SNIPPET
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        //END SHAKE SNIPPET

        TextView aboutTextView = findViewById(R.id.about_textview);
        aboutTextView.setText(
                getResources().getString(R.string.about_shake_for_easter_egg)
        );

        MaterialButton showLicensesButton = findViewById(R.id.open_source_libs_button);
        showLicensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLicenses(view);
            }
        });

        MaterialButton introButton = findViewById(R.id.intro_button);
        introButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIntroActivity(view);
            }
        });

        Typeface coolvetica = ResourcesCompat.getFont(this, R.font.coolvetica);
        AnimatedGradientTextView animatedGradientLogotype = findViewById(R.id.animated_gradient_logotype);
        animatedGradientLogotype.setTypeface(coolvetica);

    }

    private String getUnitycornLifeSpan(){
        try {
            Long todayInEpoch = System.currentTimeMillis();
            Long unitycornBirthdayInEpoch = new SimpleDateFormat("MM/dd/yyyy").parse("07/10/2018").getTime();
            Long numberOfDaysPast = (todayInEpoch-unitycornBirthdayInEpoch) / (1000 * 3600 * 24);
            return Long.toString(numberOfDaysPast);
        } catch (ParseException e) {
            return "?";
        }
    }

    public void aboutCardItemClick(View view){
        switch (view.getId()){
            case R.id.about_item_website:
                openUrlAsIntent("http://unitycorn.ir");
                break;

            case R.id.about_item_email:
                emailSupport();
                break;

            case R.id.about_item_instagram:
                openUrlAsIntent("https://instagram.com/unitycorn_ir");
                break;

            case R.id.about_item_telegram:
                openUrlAsIntent("https://telegram.me/unitycorn_ir");
                break;

            case R.id.about_item_virgool:
                openUrlAsIntent("https://virgool.io/@unitycorn");
                break;
        }
    }

    public void showLicenses(View view) {
        Intent libsIntent = new Intent(this, OpenSourceLibrariesActivity.class);
        startActivity(libsIntent);
    }

    public void showIntroActivity(View view) {
        Intent introIntent = new Intent(this, IntroActivity.class);
        startActivity(introIntent);
    }

    private void openUrlAsIntent(String url){
        Intent urlIntent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(urlIntent);
    }

    @Override
    public void makeViewsDark() {
        ((MaterialCardView)findViewById(R.id.about_card)).setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ((MaterialCardView)findViewById(R.id.about_application_card)).setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ((TextView)findViewById(R.id.about_card_title)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        ((TextView)findViewById(R.id.about_application_card_title)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        ((TextView)findViewById(R.id.about_item_website)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.about_item_email)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.about_item_instagram)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.about_item_telegram)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.about_item_virgool)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));

        ((TextView)findViewById(R.id.about_creator)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.about_creator_copyright_notice_1)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.about_creator_copyright_notice_2)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Shortcut(id="email_support", icon=R.drawable.about_ic_email, shortLabelRes = R.string.about_support_email_shortcut_name)
    public void emailSupport(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"app@unitycorn.ir"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.about_support_email_title));
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "\n\n--------------------------------------\n"
                        + getResources().getString(R.string.about_support_email_attention) +"\n"
                        +"\nDevice model: "+Build.MANUFACTURER + " - "+Build.MODEL
                        +"\nAndroid Version: "+Build.VERSION.RELEASE+" => API "+Integer.toString(Build.VERSION.SDK_INT)
        );

        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.about_support_email_intent_title)));

    }

    //START SHAKE SNIPPET
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 12) {
                Toasty.success(AboutActivity.this,
                        getResources().getString(R.string.about_easter_egg_toast_1) + " " + getUnitycornLifeSpan() + " " + getResources().getString(R.string.about_easter_egg_toast_2)
                ).show();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
    //END SHAKE SNIPPET
}
