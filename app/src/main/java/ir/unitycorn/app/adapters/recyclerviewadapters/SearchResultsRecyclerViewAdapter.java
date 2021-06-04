package ir.unitycorn.app.adapters.recyclerviewadapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ir.unitycorn.app.R;
import ir.unitycorn.app.roomlibrelated.entities.ArticleEntity;
import ir.unitycorn.app.viewmodels.SearchActivityViewModel;

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private SharedPreferences sPrefs;
    private List<ArticleEntity> resultItems;
    private SearchActivityViewModel searchActivityViewModel;

    public SearchResultsRecyclerViewAdapter(Context c, SharedPreferences s, List<ArticleEntity> r, SearchActivityViewModel sam) {
        this.context = c;
        this.sPrefs = s;
        this.resultItems = r;
        this.searchActivityViewModel = sam;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new SearchResultViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchResultViewHolder searchResultViewHolder = (SearchResultViewHolder) holder;
        searchResultViewHolder.SRVHTitle.setText(resultItems.get(position).getArticleValue());
        searchResultViewHolder.SRVHCategoryTitle.setText(
                searchActivityViewModel.getCategoryValueById(resultItems.get(position).getArticleParent())
        );
        searchResultViewHolder.SRVHContainer.setTag(resultItems.get(position).getArticleId());

        switch (searchActivityViewModel.getCategoryTypeById(resultItems.get(position).getArticleParent())){
            case 1:
                searchResultViewHolder.SRVHCategoryTitle.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.category_gamedev,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    searchResultViewHolder.SRVHCategoryTitle.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
                }
                break;

            case 2:
                searchResultViewHolder.SRVHCategoryTitle.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.category_code,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    searchResultViewHolder.SRVHCategoryTitle.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
                }
                break;

            case 3:
                searchResultViewHolder.SRVHCategoryTitle.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.category_design,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    searchResultViewHolder.SRVHCategoryTitle.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
                }
                break;

            default:
                searchResultViewHolder.SRVHCategoryTitle.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.category_gamedev,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    searchResultViewHolder.SRVHCategoryTitle.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
                }
                break;
        }

        if(sPrefs.getBoolean("dark_mode", false)){
            searchResultViewHolder.SRVHMaterialCardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.colorPrimary)
                    );
            searchResultViewHolder.SRVHTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.colorAccent)
            );
            searchResultViewHolder.SRVHCategoryTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.normalTextColorDark)
            );
        }

    }

    @Override
    public int getItemCount() {
        return resultItems.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        TextView SRVHTitle;
        TextView SRVHCategoryTitle;
        LinearLayout SRVHContainer;
        MaterialCardView SRVHMaterialCardView;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            SRVHTitle = itemView.findViewById(R.id.search_result_title);
            SRVHCategoryTitle = itemView.findViewById(R.id.search_result_category_title);
            SRVHContainer = itemView.findViewById(R.id.search_result_container);
            SRVHMaterialCardView = itemView.findViewById(R.id.search_result_card);
        }
    }

}

