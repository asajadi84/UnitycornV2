package ir.unitycorn.app.adapters.recyclerviewadapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import ir.unitycorn.app.R;
import ir.unitycorn.app.activities.ArticleActivity;
import ir.unitycorn.app.activities.UpgradeActivity;
import ir.unitycorn.app.viewmodels.MainActivityViewModel;

public class ArticlesTableInnerRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> articlesNames;
    private List<String> articlesIds;
    private List<Boolean> articlesUnreadStatus;
    private MainActivityViewModel mainActivityViewModel;

    public ArticlesTableInnerRecyclerViewAdapter(Context c, List<String> an, List<String> ai, List<Boolean> ars, MainActivityViewModel m) {
        this.context = c;
        this.articlesNames = an;
        this.articlesIds = ai;
        this.articlesUnreadStatus = ars;
        this.mainActivityViewModel = m;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.articles_table_list_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ArticlesTableInnerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ArticlesTableInnerViewHolder articlesTableInnerViewHolder = (ArticlesTableInnerViewHolder) holder;
        articlesTableInnerViewHolder.ATIVHTextView.setText(articlesNames.get(position));
        articlesTableInnerViewHolder.ATIVHTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                context.getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("font_size", 16));

        articlesTableInnerViewHolder.ATIVHContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If Article Is Premium
                if(mainActivityViewModel.articleIdToPremiumStatus(articlesIds.get(position))){
                    //Check If User Is Premium
                    if(context.getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("unitycorn_plus", false)){
                        openArticleById(articlesIds.get(position));
                    }else{
                        //User Is Not Premium But Attempting To Open Premium Article
                        AlertDialog upgradeAlertDialog;
                        upgradeAlertDialog = new AlertDialog.Builder(context)
                                .setCancelable(false)
                                .setMessage(context.getResources().getString(R.string.upgrade_to_premium_alert_dialog_message))
                                .setPositiveButton(context.getResources().getString(R.string.upgrade_to_premium_alert_dialog_positive_button), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent upgradeIntent = new Intent(context, UpgradeActivity.class);
                                        context.startActivity(upgradeIntent);
                                    }
                                })
                                .setNegativeButton(context.getResources().getString(R.string.upgrade_to_premium_alert_dialog_negative_button), null).show();

                        if (context.getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_mode", false)) {
                            upgradeAlertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

                            upgradeAlertDialog.setMessage(Html.fromHtml("<font color='#e1e1e1'>"+context.getResources().getString(R.string.upgrade_to_premium_alert_dialog_message)+"</font>"));
                            upgradeAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
                            upgradeAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.normalTextColorDark));

                        }else{
                            upgradeAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
                            upgradeAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                        }
                    }
                }else{
                    //Article Is Not Premium
                    openArticleById(articlesIds.get(position));
                }
            }
        });

        if(context.getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_mode", false)){
            articlesTableInnerViewHolder.ATIVHTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

//        articlesTableInnerViewHolder.ATIVHContainer.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(context, "Do it later!", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        articlesTableInnerViewHolder.ATIVHImageView.setVisibility(
                (articlesUnreadStatus.get(position) && context.getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("distinct_unread", true)) ? View.VISIBLE : View.INVISIBLE
        );
    }

    @Override
    public int getItemCount() {
        return articlesNames.size();
    }

    public class ArticlesTableInnerViewHolder extends RecyclerView.ViewHolder {

        FlexboxLayout ATIVHContainer;
        TextView ATIVHTextView;
        ImageView ATIVHImageView;


        public ArticlesTableInnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ATIVHContainer = itemView.findViewById(R.id.articles_table_list_container);
            ATIVHTextView = itemView.findViewById(R.id.articles_table_list_text);
            ATIVHImageView = itemView.findViewById(R.id.articles_table_list_image);
        }
    }

    private void openArticleById (String articleId){
        Intent articleIntent = new Intent(context, ArticleActivity.class);
        articleIntent.putExtra("articleId", articleId);
        articleIntent.putExtra("followActivityStackRules", "true");
        context.startActivity(articleIntent);
    }
}

