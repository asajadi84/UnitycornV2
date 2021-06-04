package ir.unitycorn.app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.jay.widget.StickyHeadersLinearLayoutManager;

import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.tapsell.sdk.bannerads.TapsellBannerType;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import ir.tapsell.sdk.bannerads.TapsellBannerViewEventListener;
import ir.unitycorn.app.adapters.recyclerviewadapters.ArticleRecyclerViewAdapter;
import ir.unitycorn.app.fragments.ArticleFragment;

import ir.unitycorn.app.R;
import ir.unitycorn.app.viewmodels.ArticleActivityViewModel;

public class ArticleActivity extends AppCompatActivity {

    Toolbar toolbar;
    SharedPreferences settingsPrefs;
    SharedPreferences readPrefs;
    int activityColor;
    boolean articleIsRead;
    String articleId;
    String previousArticleId;
    String nextArticleId;

    NotificationManagerCompat notificationManager;

    boolean articleOpenedFromNotifications = false;

    ArticleActivityViewModel articleActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        articleId = getIntent().getExtras().getString("articleId");

        if(!getIntent().getExtras().getString("followActivityStackRules").equals("true")){
            articleOpenedFromNotifications = true;
        }

        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(articleId, 1);

        articleActivityViewModel = ViewModelProviders.of(this).get(ArticleActivityViewModel.class);

        previousArticleId = articleActivityViewModel.getPreviousArticleId(articleId);
        nextArticleId = articleActivityViewModel.getNextArticleId(articleId);

        activityColor = R.color.colorAccent;

        settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        settingsPrefs.edit().putString("last_read", articleId).apply();
        readPrefs = getSharedPreferences("read_articles", Context.MODE_PRIVATE);
        articleIsRead = readPrefs.getBoolean(articleId, false);

        super.onCreate(savedInstanceState);

        if(settingsPrefs.getBoolean("unitycorn_plus", false)) {
            setContentView(R.layout.activity_article_no_ads);
        }else{
            setContentView(R.layout.activity_article);
        }

        Locale farsi = new Locale("fa", "IR");
        getResources().getConfiguration().setLocale(farsi);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(settingsPrefs.getBoolean("dark_mode", false)){
            toolbar.setPopupTheme(R.style.PopupOverlayDark);
        }
        setSupportActionBar(toolbar);

        //initialize toasty
        Typeface iransans = ResourcesCompat.getFont(getApplicationContext(), R.font.iransans);
        Toasty.Config.getInstance()
                .setToastTypeface(iransans)
                .allowQueue(false)
                .apply();

        setTitle(articleActivityViewModel.getArticleValueByArticleId(articleId));

        //Prevent capturing screenshot if the article is premium
        if(articleActivityViewModel.isPremium(articleId)){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //ui tour manager
        if(!settingsPrefs.getBoolean("article_ui_tour_seen", false)){
            AlertDialog uiTourAlertDialog;
            uiTourAlertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(getResources().getString(R.string.article_help_alert_dialog_message))
                    .setPositiveButton(getResources().getString(R.string.article_help_alert_dialog_positive_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            uiTour();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.article_help_alert_dialog_negative_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            settingsPrefs.edit().putBoolean("article_ui_tour_seen", true).apply();
                        }
                    }).show();

            if (settingsPrefs.getBoolean("dark_mode", false)) {
                uiTourAlertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

                uiTourAlertDialog.setMessage(Html.fromHtml("<font color='#e1e1e1'>"+getResources().getString(R.string.article_help_alert_dialog_message)+"</font>"));
                uiTourAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(activityColor));
                uiTourAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.normalTextColorDark));

            }else{
                uiTourAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(activityColor));
                uiTourAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.darker_gray));
            }


        }

        //initialize recycler view
        final RecyclerView rv = findViewById(R.id.articles_rv);

        RecyclerView.LayoutManager layoutManager;

        if(settingsPrefs.getBoolean("article_sticky_header", true)){
            layoutManager = new StickyHeadersLinearLayoutManager<>(this);
            ((StickyHeadersLinearLayoutManager) layoutManager).setSmoothScrollbarEnabled(true);
        }else{
            layoutManager = new LinearLayoutManager(this);
            ((LinearLayoutManager) layoutManager).setSmoothScrollbarEnabled(true);
        }

        rv.setLayoutManager(layoutManager);
        if(settingsPrefs.getBoolean("dark_mode",false)) {
            rv.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        rv.setAdapter(new ArticleRecyclerViewAdapter(this, settingsPrefs,
                articleActivityViewModel.getContentValuesOfArticleByParentId(articleId),
                articleActivityViewModel.getContentTypesOfArticleByParentId(articleId)));

        if(!settingsPrefs.getBoolean("unitycorn_plus", false)) {
            TapsellBannerView banner1 = findViewById(R.id.banner1);
            banner1.loadAd(this, "5df26833906521000141e78a", TapsellBannerType.BANNER_320x50);

            banner1.setEventListener(new TapsellBannerViewEventListener() {
                @Override
                public void onNoAdAvailable() {
                    Log.e("AshkanOne", "onNoAdAvailable");
                }

                @Override
                public void onNoNetwork() {
                    Log.e("AshkanOne", "onNoNetwork");
                }

                @Override
                public void onError(String s) {
                    Log.e("AshkanOne", "onError "+s);
                }

                @Override
                public void onRequestFilled() {
                    if(!settingsPrefs.getBoolean("unitycorn_plus", false)) {
                        rv.setPadding(0, 0, 0, 144);
                    }
                }

                @Override
                public void onHideBannerView() {
                    Log.e("AshkanOne", "onHideBannerView");
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_activity_menu, menu);

        if(articleIsRead){
            menu.getItem(2).setIcon(R.drawable.article_ic_eye_off);
        }

        if(!previousArticleId.equals("none")){
            menu.getItem(0).setTitle(getResources().getString(R.string.article_menu_skip_previous)+" ("+
                    articleActivityViewModel.getArticleValueByArticleId(previousArticleId)+
                    ")");
        }

        if(!nextArticleId.equals("none")){
            menu.getItem(1).setTitle(getResources().getString(R.string.article_menu_skip_next)+" ("+
                    articleActivityViewModel.getArticleValueByArticleId(nextArticleId)+
                    ")");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId){

            case R.id.read_or_unread:

                MainActivity.dataNeedsToReset = true;

                if(articleIsRead){
                    readPrefs.edit().putBoolean(articleId, false).apply();
                    articleIsRead=false;
                    item.setIcon(R.drawable.article_ic_eye);
                    Toasty.success(this, getResources().getString(R.string.article_bookmark_removed_toast)).show();
                }else{
                    readPrefs.edit().putBoolean(articleId, true).apply();
                    articleIsRead=true;
                    item.setIcon(R.drawable.article_ic_eye_off);
                    Toasty.success(this, getResources().getString(R.string.article_bookmark_added_toast)).show();
                }
                break;

            case R.id.add_to_notification:

                createNotificationChannel();
                Intent notificationIntent = new Intent(this, ArticleActivity.class);
                notificationIntent.putExtra("articleId", articleId);
                notificationIntent.putExtra("followActivityStackRules", "false");
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                        this,
                        articleActivityViewModel.getArticleUniqueId(articleId),
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );

                Log.e("bookmark unique id", Integer.toString(articleActivityViewModel.getArticleUniqueId(articleId)));

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "bookmark")
                        .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.article_ic_bookmark : R.drawable.article_ic_bookmark_kitkat)
                        .setContentTitle(getResources().getString(R.string.article_bookmark_notification_title))
                        .setContentText(articleActivityViewModel.getArticleValueByArticleId(articleId))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setColor(ContextCompat.getColor(this, activityColor))
                        .addAction(
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.article_ic_bookmark : R.drawable.article_ic_bookmark_kitkat,
                                getResources().getString(R.string.article_bookmark_notification_action_button_text) ,notificationPendingIntent)
                        .setOngoing(settingsPrefs.getBoolean("sticky_notification", false));
                builder.setContentIntent(notificationPendingIntent);

                notificationManager.notify(articleId, 1, builder.build());
                Toasty.success(getApplicationContext(), getResources().getString(R.string.article_bookmark_notification_added_toast)).show();
                finish();
                break;

            case R.id.skip_previous:

                if(previousArticleId.equals("none")){
                    Toasty.error(this, getResources().getString(R.string.article_there_is_no_previous_article_toast)).show();
                }else{

                    if(articleActivityViewModel.isPremium(previousArticleId)){

                        if(settingsPrefs.getBoolean("unitycorn_plus", false)){
                            finish();
                            Intent previousIntent = new Intent(this, ArticleActivity.class);
                            previousIntent.putExtra("articleId", previousArticleId);
                            previousIntent.putExtra("followActivityStackRules", "true");
                            startActivity(previousIntent);
                        }else{

                            AlertDialog upgradeAlertDialog;
                            upgradeAlertDialog = new AlertDialog.Builder(ArticleActivity.this)
                                    .setCancelable(false)
                                    .setMessage(getResources().getString(R.string.upgrade_to_premium_alert_dialog_message))
                                    .setPositiveButton(getResources().getString(R.string.upgrade_to_premium_alert_dialog_positive_button), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent upgradeIntent = new Intent(ArticleActivity.this, UpgradeActivity.class);
                                            ArticleActivity.this.startActivity(upgradeIntent);
                                        }
                                    })
                                    .setNegativeButton(getResources().getString(R.string.upgrade_to_premium_alert_dialog_negative_button), null).show();

                            if (settingsPrefs.getBoolean("dark_mode", false)) {
                                upgradeAlertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

                                upgradeAlertDialog.setMessage(Html.fromHtml("<font color='#e1e1e1'>"+getResources().getString(R.string.upgrade_to_premium_alert_dialog_message)+"</font>"));
                                upgradeAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                                upgradeAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.normalTextColorDark));

                            }else{
                                upgradeAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                                upgradeAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.darker_gray));
                            }

                        }
                    }else{
                        finish();
                        Intent previousIntent = new Intent(this, ArticleActivity.class);
                        previousIntent.putExtra("articleId", previousArticleId);
                        previousIntent.putExtra("followActivityStackRules", "true");
                        startActivity(previousIntent);
                    }
                }
                break;

            case R.id.skip_next:

                if(nextArticleId.equals("none")){
                    Toasty.error(this, getResources().getString(R.string.article_there_is_no_next_article_toast)).show();
                }else{

                    if(articleActivityViewModel.isPremium(nextArticleId)){

                        if(settingsPrefs.getBoolean("unitycorn_plus", false)){
                            finish();
                            Intent nextIntent = new Intent(this, ArticleActivity.class);
                            nextIntent.putExtra("articleId", nextArticleId);
                            nextIntent.putExtra("followActivityStackRules", "true");
                            startActivity(nextIntent);
                        }else{

                            AlertDialog upgradeAlertDialog;
                            upgradeAlertDialog = new AlertDialog.Builder(ArticleActivity.this)
                                    .setCancelable(false)
                                    .setMessage(getResources().getString(R.string.upgrade_to_premium_alert_dialog_message))
                                    .setPositiveButton(getResources().getString(R.string.upgrade_to_premium_alert_dialog_positive_button), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent upgradeIntent = new Intent(ArticleActivity.this, UpgradeActivity.class);
                                            ArticleActivity.this.startActivity(upgradeIntent);
                                        }
                                    })
                                    .setNegativeButton(getResources().getString(R.string.upgrade_to_premium_alert_dialog_negative_button), null).show();

                            if (settingsPrefs.getBoolean("dark_mode", false)) {
                                upgradeAlertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

                                upgradeAlertDialog.setMessage(Html.fromHtml("<font color='#e1e1e1'>"+getResources().getString(R.string.upgrade_to_premium_alert_dialog_message)+"</font>"));
                                upgradeAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                                upgradeAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.normalTextColorDark));

                            }else{
                                upgradeAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                                upgradeAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.darker_gray));
                            }

                        }
                    }else{
                        finish();
                        Intent nextIntent = new Intent(this, ArticleActivity.class);
                        nextIntent.putExtra("articleId", nextArticleId);
                        nextIntent.putExtra("followActivityStackRules", "true");
                        startActivity(nextIntent);
                    }
                }
                break;

            case R.id.show_help:
                uiTour();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = getResources().getString(R.string.article_notification_channel_name);
            String description = getResources().getString(R.string.article_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("bookmark",name,importance);
            notificationChannel.setDescription(description);
            notificationChannel.setShowBadge(false);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void uiTour(){
        Typeface iranSans = ResourcesCompat.getFont(getApplicationContext(), R.font.iransans);

        final TapTargetSequence tourGuide = new TapTargetSequence(this)
                .targets(
                        TapTarget.forToolbarMenuItem(toolbar, R.id.read_or_unread, getResources().getString(R.string.article_help_title_1),
                                getResources().getString(R.string.article_help_description_1))
                                .outerCircleColor(activityColor)
                                .targetCircleColor(R.color.colorPrimary)
                                .textColor(android.R.color.black)
                                .textTypeface(iranSans)
                                .descriptionTextColor(android.R.color.black)
                                .id(1),
                        TapTarget.forToolbarMenuItem(toolbar, R.id.add_to_notification, getResources().getString(R.string.article_help_title_2),
                                getResources().getString(R.string.article_help_description_2))
                                .outerCircleColor(activityColor)
                                .targetCircleColor(R.color.colorPrimary)
                                .textColor(android.R.color.black)
                                .textTypeface(iranSans)
                                .descriptionTextColor(android.R.color.black)
                                .id(2),
                        TapTarget.forToolbarOverflow(toolbar, getResources().getString(R.string.article_help_title_3),
                                getResources().getString(R.string.article_help_description_3))
                                .outerCircleColor(activityColor)
                                .targetCircleColor(R.color.colorPrimary)
                                .textColor(android.R.color.black)
                                .textTypeface(iranSans)
                                .descriptionTextColor(android.R.color.black)
                                .id(3),
                        TapTarget.forToolbarOverflow(toolbar, getResources().getString(R.string.article_help_title_4),
                                getResources().getString(R.string.article_help_description_4))
                                .outerCircleColor(activityColor)
                                .targetCircleColor(R.color.colorPrimary)
                                .textColor(android.R.color.black)
                                .textTypeface(iranSans)
                                .descriptionTextColor(android.R.color.black)
                                .id(4)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        settingsPrefs.edit().putBoolean("article_ui_tour_seen", true).apply();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                })
                .considerOuterCircleCanceled(true)
                .continueOnCancel(true);

        tourGuide.start();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if(articleOpenedFromNotifications){
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }else{
            super.onBackPressed();
        }
    }
}
