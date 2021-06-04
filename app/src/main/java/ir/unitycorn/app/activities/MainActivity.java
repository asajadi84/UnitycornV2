package ir.unitycorn.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.unitycorn.app.DarkModeCapable;
import ir.unitycorn.app.R;
import ir.unitycorn.app.adapters.recyclerviewadapters.ArticlesTableOuterRecyclerViewAdapter;
import ir.unitycorn.app.adapters.viewpageradapters.ArticleTableFragmentStateAdapter;
import ir.unitycorn.app.fragments.ArticleFragment;
import ir.unitycorn.app.viewmodels.MainActivityViewModel;
import shortbread.Shortcut;

public class MainActivity extends AppCompatActivity implements DarkModeCapable {

    public static boolean activityShouldReset = false;
    public static boolean dataNeedsToReset = false;

    SharedPreferences settingsPrefs;
    FloatingActionButton fab;
    boolean pressedAgainToExit = false;
    ArticleTableFragmentStateAdapter articleTableFragmentStateAdapter;
    ViewPager2 viewPager;
    TabLayout tabLayout;

    MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make Everything RTL
        Locale farsi = new Locale("fa","IR");
        getResources().getConfiguration().setLocale(farsi);

        setContentView(R.layout.activity_main);

        //Initialize The Fonts Needed
        Typeface iranSans = ResourcesCompat.getFont(getApplicationContext(), R.font.iransans);
        Typeface iranSansBold = ResourcesCompat.getFont(getApplicationContext(), R.font.iransansb);

        settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);

        //Change Theme To Dark Mode If Necessary
        if(settingsPrefs.getBoolean("dark_mode", false)){
            setTheme(R.style.AppThemeDark);
        }

        //Calculate The Number Of Times That The User Opened The App, Increases Each Time User Opens The App
        settingsPrefs.edit().putInt("app_open_times",
                settingsPrefs.getInt("app_open_times", 0) + 1
        ).apply();

        //Initialize Toasty
        Toasty.Config.getInstance()
                .setToastTypeface(iranSans)
                .allowQueue(false)
                .apply();

        //Referencing The Toolbar, Making It Activity ActionBar And Titling It
        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(
                settingsPrefs.getBoolean("unitycorn_plus", false) ?
                        R.string.main_header_title_plus : R.string.main_header_title
        );

        final ImageView toolbarImageView = findViewById(R.id.main_toolbar_image_view);
        final TextView toolbarSlogan = findViewById(R.id.main_toolbar_slogan);
        final AppBarLayout appBarLayout = findViewById(R.id.main_appbar_layout);
        fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShareIntent();
            }
        });

        //Collapsing Toolbar Animation
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float range = (float) -appBarLayout.getTotalScrollRange();
                int currentAlpha = (int) (255 * (1.0f - (float) verticalOffset / range));
                toolbarImageView.setImageAlpha(clamp(0, currentAlpha, 50));
                toolbarImageView.setRotation((255-currentAlpha)*0.1f);
                int currentAlphaInverse = 255 - currentAlpha;
                float zoomScale = 1.0f + (float) currentAlphaInverse / 100;
                toolbarImageView.setScaleX(zoomScale);
                toolbarImageView.setScaleY(zoomScale);
                toolbarSlogan.setAlpha((float) currentAlpha / 255);
                toolbarSlogan.setTranslationY((255 - currentAlpha) * 4);
                if(verticalOffset==0){fab.show();}
            }
        });

        //Collapsing Toolbar Style
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.main_collapsing_toolbar_layout);
        collapsingToolbarLayout.setCollapsedTitleTypeface(iranSansBold);
        collapsingToolbarLayout.setExpandedTitleTypeface(iranSansBold);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));

        if(settingsPrefs.getBoolean("dark_mode", false)){
            mainToolbar.setPopupTheme(R.style.PopupOverlayDark);
        }

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        //VIEWPAGER INITIALIZATION
        //1- Initialize The ViewPager Adapter
        articleTableFragmentStateAdapter = new ArticleTableFragmentStateAdapter(this, mainActivityViewModel);
        //2- Assign The ViewPager Adapter To The ViewPager
        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setAdapter(articleTableFragmentStateAdapter);
        //3- Connect Tab Layout To ViewPager
        tabLayout = findViewById(R.id.main_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                        switch (position){

                            case 0:
                                tab.setText(R.string.main_category_gamedev);
                                tab.setIcon(R.drawable.category_gamedev);
                                break;

                            case 1:
                                tab.setText(R.string.main_category_code);
                                tab.setIcon(R.drawable.category_code);
                                break;

                            case 2:
                                tab.setText(R.string.main_category_design);
                                tab.setIcon(R.drawable.category_design);
                                break;
                        }
                    }
                }
        ).attach();;

        if(settingsPrefs.getBoolean("dark_mode", false)){
            makeViewsDark();
        }

        updateTabNumberBadges(tabLayout);

        //Tab Layout Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                try{
                    View fragmentView = mainActivityViewModel.getArticleFragment(viewPager.getCurrentItem()+1).getView();
                    NestedScrollView fragmentNestedScrollView = fragmentView.findViewById(R.id.fragment_nested_scroll_view);
                    RecyclerView fragmentRecyclerView = fragmentView.findViewById(R.id.fragment_article_recycler_view);
                    fragmentNestedScrollView.smoothScrollTo(0, fragmentRecyclerView.getTop());

                    fab.show();
                }catch (Exception e){}


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                try{
                    View fragmentView = mainActivityViewModel.getArticleFragment(viewPager.getCurrentItem()+1).getView();
                    NestedScrollView fragmentNestedScrollView = fragmentView.findViewById(R.id.fragment_nested_scroll_view);
                    RecyclerView fragmentRecyclerView = fragmentView.findViewById(R.id.fragment_article_recycler_view);
                    fragmentNestedScrollView.smoothScrollTo(0, fragmentRecyclerView.getTop());

                    appBarLayout.setExpanded(true);
                }catch (Exception e){}

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(activityShouldReset){
            Log.e("MainActivity", "activity should reset");

            activityShouldReset = false;
            //recreate();
            finish();
            Intent recreateIntent = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(recreateIntent);
        }

        if(dataNeedsToReset){
            Log.e("MainActivity", "data needs to reset");

            dataNeedsToReset = false;
            updateTabNumberBadges(tabLayout);

            //1 Get Current ArticleFragment
            int viewPagerActiveTab = viewPager.getCurrentItem()+1;
            ArticleFragment currentArticleFragment = mainActivityViewModel.getArticleFragment(viewPagerActiveTab);

            //2 Get The Recycler View Of The Fragment
            RecyclerView currentArticleFragmentRecyclerView = currentArticleFragment.getView().findViewById(R.id.fragment_article_recycler_view);

            //3 Fetch New Data And Apply To The Recycler View
            ArticlesTableOuterRecyclerViewAdapter articlesTableOuterRecyclerViewAdapterUpdated =
                    new ArticlesTableOuterRecyclerViewAdapter(this, mainActivityViewModel.getArticleTableModels(viewPagerActiveTab), mainActivityViewModel);
            currentArticleFragmentRecyclerView.setAdapter(articlesTableOuterRecyclerViewAdapterUpdated);
            currentArticleFragmentRecyclerView.getAdapter().notifyDataSetChanged();

            //4 todo check
        }

    }

    //Attaching The Main Menu To The Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        //If Unitycorn Plus Is Active Remove The Upgrade Item
        if(settingsPrefs.getBoolean("unitycorn_plus", false)){
            MenuItem upgradeMenuItem = menu.findItem(R.id.nav_upgrade);
            upgradeMenuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int menuId = item.getItemId();
        switch(menuId){

            case R.id.nav_search:
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                break;

            case R.id.nav_last_article:
                openTheLastArticle();
                break;

            case R.id.nav_about:
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;

            case R.id.nav_share:
                startShareIntent();
                break;

            case R.id.nav_upgrade:
                Intent upgradeIntent = new Intent(MainActivity.this, UpgradeActivity.class);
                startActivity(upgradeIntent);
                break;

            case R.id.nav_color_picker:
                Toasty.custom(this,
                        getResources().getString(R.string.main_menu_color_picker_explanation_toast), null,
                        getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(android.R.color.white),
                        Toasty.LENGTH_LONG, false, true).show();

                ColorPickerDialog colorPickerDialog;
                colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this,
                        settingsPrefs.getBoolean("dark_mode", false) ? ColorPickerDialog.DARK_THEME : ColorPickerDialog.LIGHT_THEME
                        );
                colorPickerDialog.setPositiveActionText(getResources().getString(R.string.main_menu_color_picker_positive_action));
                colorPickerDialog.setNegativeActionText(getResources().getString(R.string.main_menu_color_picker_negative_action));
                colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
                    @Override
                    public void onColorPicked(int color, String hexVal) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("HexColor", hexVal);
                        clipboardManager.setPrimaryClip(clipData);
                        Toasty.success(MainActivity.this,
                                getResources().getString(R.string.main_menu_color_picker_success_toast_1) + " " + hexVal + " " + getResources().getString(R.string.main_menu_color_picker_success_toast_2),
                                Toasty.LENGTH_LONG
                                ).show();
                    }
                });
                colorPickerDialog.show();
                break;

            case R.id.nav_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void makeViewsDark() {
        viewPager.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        fab.setSupportImageTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.black)));
    }

    @Override
    public void onBackPressed() {
        if(pressedAgainToExit) {
            super.onBackPressed();
        }else{
            pressedAgainToExit = true;

            Snackbar pressAgainToExitSnackBar = Snackbar.make(findViewById(R.id.main_coordinator_layout), R.string.main_press_again_to_exit_snack_bar_text, BaseTransientBottomBar.LENGTH_SHORT);
            //pressAgainToExitSnackBar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);

            pressAgainToExitSnackBar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(
                    settingsPrefs.getBoolean("dark_mode", false) ? R.color.colorAccent : R.color.colorPrimary
            )));

            pressAgainToExitSnackBar.setTextColor(ColorStateList.valueOf(getResources().getColor(
                    settingsPrefs.getBoolean("dark_mode", false) ? android.R.color.black : android.R.color.white
            )));

            //If User Is Ready To Rate The App Add Rating Action To The Snack Bar
            boolean userOpenedAppEnoughTimesToKnowIt = settingsPrefs.getInt("app_open_times", 0) > 5;
            if(!settingsPrefs.getBoolean("user_rated_in_market", false) && userOpenedAppEnoughTimesToKnowIt){
                pressAgainToExitSnackBar.setAction(R.string.main_press_again_to_exit_snack_bar_rate_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            settingsPrefs.edit().putBoolean("user_rated_in_market", true).apply();
                            Intent myketIntent = new Intent();
                            myketIntent.setAction(Intent.ACTION_VIEW);
                            myketIntent.setData(Uri.parse("myket://comment?id=ir.unitycorn.app"));
                            startActivity(myketIntent);
                        }catch(ActivityNotFoundException e){
                            Toasty.error(MainActivity.this, R.string.main_press_again_to_exit_snack_bar_rate_action_error).show();
                            settingsPrefs.edit().putBoolean("user_rated_in_market", false).apply();
                        }
                    }
                });
                pressAgainToExitSnackBar.setActionTextColor(ColorStateList.valueOf(getResources().getColor(
                        settingsPrefs.getBoolean("dark_mode", false) ? android.R.color.black : R.color.colorAccent
                )));
            }

            pressAgainToExitSnackBar.addCallback(new Snackbar.Callback(){

                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    pressedAgainToExit = false;
                }
            });

            pressAgainToExitSnackBar.show();
        }
    }

    //Clamp Function
    private int clamp (int minValue, int value, int maxValue){
        if(value<minValue){
            return minValue;
        }else if(value>maxValue){
            return maxValue;
        }else{
            return value;
        }
    }

    //Adding Unread Number Badges To Tab Layout Tabs
    private void attachBadgeToTabLayoutTab(TabLayout.Tab tab, int number){
        if(number > 0){
            tab.getOrCreateBadge().setNumber(number);
            tab.getOrCreateBadge().setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.unreadColor));
            tab.getOrCreateBadge().setBadgeGravity(BadgeDrawable.TOP_START);
        }else{
            tab.removeBadge();
        }
    }

    //Change Default Font To Iran Sans
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void startShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.main_share_intent_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.main_share_intent_text));
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.main_share_intent_share_title)));
    }

    @Shortcut(id="last_article", icon=R.drawable.ic_last_article, shortLabelRes=R.string.main_menu_last_article)
    public void openTheLastArticle(){
        String lastReadArticle = settingsPrefs.getString("last_read", "none");
        if(lastReadArticle.equals("none")){
            Toasty.error(MainActivity.this, R.string.main_menu_last_article_none_error).show();
        }else{
            Intent lastArticleIntent = new Intent (MainActivity.this, ArticleActivity.class);
            lastArticleIntent.putExtra("articleId", lastReadArticle);
            lastArticleIntent.putExtra("followActivityStackRules", "true");
            startActivity(lastArticleIntent);
        }
    }

    private void updateTabNumberBadges(TabLayout tabLayout){
        attachBadgeToTabLayoutTab(tabLayout.getTabAt(0), mainActivityViewModel.getNumberOfUnreadArticlesByCategoryType(1));
        attachBadgeToTabLayoutTab(tabLayout.getTabAt(1), mainActivityViewModel.getNumberOfUnreadArticlesByCategoryType(2));
        attachBadgeToTabLayoutTab(tabLayout.getTabAt(2), mainActivityViewModel.getNumberOfUnreadArticlesByCategoryType(3));
    }
}
