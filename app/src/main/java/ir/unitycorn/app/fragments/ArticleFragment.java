package ir.unitycorn.app.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ir.unitycorn.app.DarkModeCapable;
import ir.unitycorn.app.R;
import ir.unitycorn.app.activities.MainActivity;
import ir.unitycorn.app.adapters.recyclerviewadapters.ArticlesTableOuterRecyclerViewAdapter;
import ir.unitycorn.app.viewmodels.ArticleFragmentViewModel;
import ir.unitycorn.app.viewmodels.MainActivityViewModel;


public class ArticleFragment extends Fragment implements DarkModeCapable {

    private ArticleFragmentViewModel articleFragmentViewModel;

    private MainActivityViewModel mainActivityViewModel;


    public ArticleFragment(MainActivityViewModel m){
        mainActivityViewModel = m;
    }


    public ArticleFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        articleFragmentViewModel = ViewModelProviders.of(this).get(ArticleFragmentViewModel.class);
        int index = 1;
        if(getArguments() != null){
            index = getArguments().getInt("section_number");
        }
        articleFragmentViewModel.setPageNumber(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ){

        View root = inflater.inflate(R.layout.fragment_article, container, false);

        RecyclerView articlesTableRecyclerView = root.findViewById(R.id.fragment_article_recycler_view);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        articlesTableRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        ArticlesTableOuterRecyclerViewAdapter articlesTableOuterRecyclerViewAdapter =
                new ArticlesTableOuterRecyclerViewAdapter(getActivity(), articleFragmentViewModel.getArticleTableModels(), mainActivityViewModel);
        articlesTableRecyclerView.setAdapter(articlesTableOuterRecyclerViewAdapter);

        if(getContext().getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_mode", false)){
            articlesTableRecyclerView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NestedScrollView fragmentNestedScrollView = root.findViewById(R.id.fragment_nested_scroll_view);
            final FloatingActionButton fab = getActivity().findViewById(R.id.main_fab);
            fragmentNestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if(i1>i3){
                        fab.hide();
                    }else if(i1<i3){
                        fab.show();
                    }
                }
            });
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void makeViewsDark() {

    }
}