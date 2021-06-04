package ir.unitycorn.app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.unitycorn.app.DarkModeCapable;
import ir.unitycorn.app.R;
import ir.unitycorn.app.adapters.recyclerviewadapters.SearchResultsRecyclerViewAdapter;
import ir.unitycorn.app.roomlibrelated.entities.ArticleEntity;
import ir.unitycorn.app.viewmodels.SearchActivityViewModel;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

public class SearchActivity extends AppCompatActivity implements DarkModeCapable {

    SearchView searchBar;
    RecyclerView searchResultRecyclerView;
    SharedPreferences settingsPrefs;
    TextView noResultTextView;
    SearchActivityViewModel searchActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if(settingsPrefs.getBoolean("dark_mode", false)){
            setTheme(R.style.AppThemeMiscDark);
        }

        super.onCreate(savedInstanceState);

        Locale farsi = new Locale("fa", "IR");
        getResources().getConfiguration().setLocale(farsi);

        setContentView(R.layout.activity_search);

        searchActivityViewModel = ViewModelProviders.of(this).get(SearchActivityViewModel.class);

        noResultTextView = findViewById(R.id.search_result_zero);

        //recycler view initialization
        searchResultRecyclerView = findViewById(R.id.search_result_recyclerview);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        searchResultRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        //searchbar initialization
        searchBar = findViewById(R.id.search_bar);

        searchBar.onActionViewExpanded();

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                List<ArticleEntity> a = searchActivityViewModel.getArticlesMatchingStringQuery(newText.toString());
                inflateRecyclerView(a);
                if (a.size()==0){
                    noResultTextView.setVisibility(View.VISIBLE);
                    noResultTextView.setText(
                            getResources().getString(R.string.search_activity_no_results_found_message)+"\n"+
                                    getResources().getString(R.string.search_activity_no_results_found_attention)
                    );
                }else{
                    noResultTextView.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });


        List<ArticleEntity> articles = searchActivityViewModel.getTotalArticles();
        inflateRecyclerView(articles);


    }

    private void inflateRecyclerView (List<ArticleEntity> resultArticles){

        SearchResultsRecyclerViewAdapter recyclerAdapter = new SearchResultsRecyclerViewAdapter(this, settingsPrefs, resultArticles, searchActivityViewModel);
        SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(recyclerAdapter);
        searchResultRecyclerView.setAdapter(slideInBottomAnimationAdapter);
        searchResultRecyclerView.getAdapter().notifyDataSetChanged();

    }

    public void searchResultItemClick(View view){

        if(searchActivityViewModel.articleIdToPremiumStatus(view.getTag().toString())){
            if(settingsPrefs.getBoolean("unitycorn_plus", false)){
                openResultArticle(view.getTag().toString());
            }else{
                AlertDialog upgradeAlertDialog;
                upgradeAlertDialog = new AlertDialog.Builder(SearchActivity.this)
                        .setCancelable(false)
                        .setMessage(getResources().getString(R.string.upgrade_to_premium_alert_dialog_message))
                        .setPositiveButton(getResources().getString(R.string.upgrade_to_premium_alert_dialog_positive_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent upgradeIntent = new Intent(SearchActivity.this, UpgradeActivity.class);
                                SearchActivity.this.startActivity(upgradeIntent);
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
            openResultArticle(view.getTag().toString());
        }


    }

    @Override
    public void makeViewsDark() {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void openResultArticle(String resultArticleId){
        Intent articleFromSearchResultIntent = new Intent(this, ArticleActivity.class);
        articleFromSearchResultIntent.putExtra("articleId", resultArticleId);
        articleFromSearchResultIntent.putExtra("followActivityStackRules", "true");
        startActivity(articleFromSearchResultIntent);
    }
}
