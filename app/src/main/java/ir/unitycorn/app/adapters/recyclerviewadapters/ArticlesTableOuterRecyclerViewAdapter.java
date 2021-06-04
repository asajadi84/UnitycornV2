package ir.unitycorn.app.adapters.recyclerviewadapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ir.unitycorn.app.ArticleTableModel;
import ir.unitycorn.app.R;
import ir.unitycorn.app.viewmodels.MainActivityViewModel;

public class ArticlesTableOuterRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ArticleTableModel> articleTableModels;
    private MainActivityViewModel mainActivityViewModel;

    public ArticlesTableOuterRecyclerViewAdapter(Context c, List<ArticleTableModel> atm, MainActivityViewModel m){
        this.context = c;
        this.articleTableModels = atm;
        this.mainActivityViewModel = m;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.articles_table_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ArticlesTableOuterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArticlesTableOuterViewHolder articlesTableOuterViewHolder = (ArticlesTableOuterViewHolder) holder;

        int properIcon;
        switch(articleTableModels.get(position).articleTableCategory){

            case 2:
                properIcon = R.drawable.category_code;
                break;

            case 3:
                properIcon = R.drawable.category_design;
                break;

            default:
                properIcon = R.drawable.category_gamedev;
                break;
        }
        Typeface iranSansBold = ResourcesCompat.getFont(context, R.font.iransansb);
        articlesTableOuterViewHolder.ATOVHIcon.setImageResource(properIcon);
        articlesTableOuterViewHolder.ATOVHTitle.setText(articleTableModels.get(position).articleTableTitle);
        articlesTableOuterViewHolder.ATOVHTitle.setTypeface(iranSansBold);
        articlesTableOuterViewHolder.ATOVHTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                context.getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("font_size", 16)+4);

        if(articleTableModels.get(position).articleTableUnreadBadge>0 &&
                context.getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("distinct_unread", true)){
            articlesTableOuterViewHolder.ATOVHUnreadBadge.setText(
                    Integer.toString(articleTableModels.get(position).articleTableUnreadBadge)
            );
            articlesTableOuterViewHolder.ATOVHUnreadBadge.setTypeface(iranSansBold);
        }else{
            articlesTableOuterViewHolder.ATOVHUnreadBadge.setVisibility(View.INVISIBLE);
        }

        articlesTableOuterViewHolder.ATOVHPremiumBadge.setVisibility(
                articleTableModels.get(position).articleTableIsPremium ? View.VISIBLE : View.GONE
        );

        if(context.getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_mode", false)){
            articlesTableOuterViewHolder.ATOVHMaterialCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            //articlesTableOuterViewHolder.ATOVHIcon.setBackground(ContextCompat.getDrawable(context, R.drawable.color_primary_dark_solid_circle));
            articlesTableOuterViewHolder.ATOVHTitle.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            articlesTableOuterViewHolder.ATOVHPremiumBadge.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            articlesTableOuterViewHolder.ATOVHPremiumBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.accent_solid_rounded_rectangle));
        }

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(context);
        articlesTableOuterViewHolder.ATOVHRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        ArticlesTableInnerRecyclerViewAdapter articlesTableInnerRecyclerViewAdapter =
                new ArticlesTableInnerRecyclerViewAdapter(context,
                        articleTableModels.get(position).articleTableListNames,
                        articleTableModels.get(position).articleTableListIds,
                        articleTableModels.get(position).articleTableListUnreadStatus, mainActivityViewModel);
        articlesTableOuterViewHolder.ATOVHRecyclerView.setAdapter(articlesTableInnerRecyclerViewAdapter);
    }

    @Override
    public int getItemCount() {
        return articleTableModels.size();
    }

    public class ArticlesTableOuterViewHolder extends RecyclerView.ViewHolder {

        ImageView ATOVHIcon;
        TextView ATOVHTitle;
        TextView ATOVHUnreadBadge;
        TextView ATOVHPremiumBadge;
        RecyclerView ATOVHRecyclerView;
        MaterialCardView ATOVHMaterialCardView;

        public ArticlesTableOuterViewHolder(@NonNull View itemView) {
            super(itemView);
            ATOVHIcon = itemView.findViewById(R.id.articles_table_title_icon);
            ATOVHTitle = itemView.findViewById(R.id.articles_table_title_text);
            ATOVHUnreadBadge = itemView.findViewById(R.id.articles_table_title_unread_badge);
            ATOVHPremiumBadge = itemView.findViewById(R.id.articles_table_title_premium_badge);
            ATOVHRecyclerView = itemView.findViewById(R.id.articles_table_recycler_view);
            ATOVHMaterialCardView = itemView.findViewById(R.id.articles_table_card);



        }


    }
}

