package ir.unitycorn.app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.xw.repo.BubbleSeekBar;

import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.unitycorn.app.DarkModeCapable;
import ir.unitycorn.app.R;

public class SettingsActivity extends AppCompatActivity implements DarkModeCapable {

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

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        //initialize toasty
        Typeface iransans = ResourcesCompat.getFont(getApplicationContext(), R.font.iransans);
        Toasty.Config.getInstance()
                .setToastTypeface(iransans)
                .allowQueue(false)
                .apply();

        setTitle(R.string.main_menu_settings);

        //1 Dark mode
        if(settingsPrefs.getBoolean("dark_mode", false)){
            makeViewsDark();
        }

        final CheckBox darkModeCheckbox = findViewById(R.id.dark_mode_checkbox);
        darkModeCheckbox.setChecked(settingsPrefs.getBoolean("dark_mode", false));
        darkModeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.activityShouldReset = true;
                if(settingsPrefs.getBoolean("dark_mode", false)){
                    settingsPrefs.edit().putBoolean("dark_mode", false).apply();
                    recreate();
                }else{
                    settingsPrefs.edit().putBoolean("dark_mode", true).apply();
                    recreate();
                }
            }
        });
        MaterialCardView settingsCardDarkMode = findViewById(R.id.settings_card_dark_mode);
        settingsCardDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                darkModeCheckbox.setChecked(!darkModeCheckbox.isChecked());
            }
        });

        //2 Distinct unread articles
        final CheckBox unreadBadgeCheckbox = findViewById(R.id.distinct_unread_checkbox);

        unreadBadgeCheckbox.setChecked(settingsPrefs.getBoolean("distinct_unread", true));

        unreadBadgeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.activityShouldReset = true;
                if(isChecked){
                    settingsPrefs.edit().putBoolean("distinct_unread", true).apply();
                }else{
                    settingsPrefs.edit().putBoolean("distinct_unread", false).apply();
                }
            }
        });
        MaterialCardView settingsCardDistinctUnread = findViewById(R.id.settings_card_distinct_unread);
        settingsCardDistinctUnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unreadBadgeCheckbox.setChecked(!unreadBadgeCheckbox.isChecked());
            }
        });

        //2.5 sticky notification
        final CheckBox stickyNotificationCheckbox = findViewById(R.id.sticky_notification_checkbox);

        stickyNotificationCheckbox.setChecked(settingsPrefs.getBoolean("sticky_notification", false));

        stickyNotificationCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    settingsPrefs.edit().putBoolean("sticky_notification", true).apply();
                }else{
                    settingsPrefs.edit().putBoolean("sticky_notification", false).apply();
                }
            }
        });
        MaterialCardView settingsCardStickyNotification = findViewById(R.id.settings_card_sticky_notification);
        settingsCardStickyNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickyNotificationCheckbox.setChecked(!stickyNotificationCheckbox.isChecked());
            }
        });

        //2.6 article sticky header
        final CheckBox articleStickyHeaderCheckbox = findViewById(R.id.article_sticky_header_checkbox);

        articleStickyHeaderCheckbox.setChecked(settingsPrefs.getBoolean("article_sticky_header", true));

        articleStickyHeaderCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    settingsPrefs.edit().putBoolean("article_sticky_header", true).apply();
                }else{
                    settingsPrefs.edit().putBoolean("article_sticky_header", false).apply();
                }
            }
        });
        MaterialCardView settingsCardArticleSticky = findViewById(R.id.settings_card_article_sticky_header);
        settingsCardArticleSticky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                articleStickyHeaderCheckbox.setChecked(!articleStickyHeaderCheckbox.isChecked());
            }
        });

        //3 Font size
        final BubbleSeekBar fontSizeSeekBar = findViewById(R.id.font_size_seek_bar);
        final TextView fontSizeTextView = findViewById(R.id.font_size_text_view);

        int currentFontSize = settingsPrefs.getInt("font_size", 16);
        fontSizeSeekBar.setProgress((float)currentFontSize);
        fontSizeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentFontSize);

        fontSizeSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                fontSizeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                settingsPrefs.edit().putInt("font_size", progress).apply();
                MainActivity.activityShouldReset=true;
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });

        ScrollView settingsScrollView = findViewById(R.id.settings_scroll_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            settingsScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    fontSizeSeekBar.correctOffsetWhenContainerOnScrolling();
                }
            });
        }

        MaterialButton restoreToDefaultButton = findViewById(R.id.restore_to_default_button);
        restoreToDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreToDefault(view);
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void makeViewsDark() {
        findViewById(R.id.settings_card_dark_mode).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        findViewById(R.id.settings_card_distinct_unread).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        findViewById(R.id.settings_card_font_size).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        findViewById(R.id.settings_card_sticky_notification).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        findViewById(R.id.settings_card_article_sticky_header).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        ((TextView)findViewById(R.id.settings_title_dark_mode)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        ((TextView)findViewById(R.id.settings_title_distinct_unread)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        ((TextView)findViewById(R.id.settings_title_font_size)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        ((TextView)findViewById(R.id.settings_title_sticky_notification)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        ((TextView)findViewById(R.id.settings_title_article_sticky_header)).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        ((TextView)findViewById(R.id.distinct_unread_articles_text_view)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.font_size_text_view)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.sticky_notification_text_view)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((TextView)findViewById(R.id.article_sticky_header_text_view)).setTextColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
        ((BubbleSeekBar)findViewById(R.id.font_size_seek_bar)).setTrackColor(ContextCompat.getColor(this, R.color.normalTextColorDark));
    }

    public void restoreToDefault(View view) {
        AlertDialog restoreToDefaultDialog;
        restoreToDefaultDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(getResources().getString(R.string.settings_reset_alert_dialog_message))
                .setPositiveButton(getResources().getString(R.string.settings_reset_alert_dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingsPrefs.edit().putBoolean("dark_mode", false).apply();
                        settingsPrefs.edit().putBoolean("distinct_unread", true).apply();
                        settingsPrefs.edit().putBoolean("sticky_notification", false).apply();
                        settingsPrefs.edit().putBoolean("article_sticky_header", true).apply();
                        settingsPrefs.edit().putInt("font_size", 16).apply();
                        MainActivity.activityShouldReset=true;
                        Toasty.success(SettingsActivity.this, getResources().getString(R.string.settings_reset_toast_text)).show();
                        finish();
                        Intent restartIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
                        startActivity(restartIntent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.settings_reset_alert_dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

        if (settingsPrefs.getBoolean("dark_mode", false)) {
            restoreToDefaultDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

            restoreToDefaultDialog.setMessage(Html.fromHtml("<font color='#e1e1e1'>"+getResources().getString(R.string.settings_reset_alert_dialog_message)+"</font>"));
            restoreToDefaultDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            restoreToDefaultDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.normalTextColorDark));

        }else{
            restoreToDefaultDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
            restoreToDefaultDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }
}
