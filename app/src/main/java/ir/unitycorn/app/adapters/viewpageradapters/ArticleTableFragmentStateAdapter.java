package ir.unitycorn.app.adapters.viewpageradapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ir.unitycorn.app.fragments.ArticleFragment;
import ir.unitycorn.app.viewmodels.MainActivityViewModel;

public class ArticleTableFragmentStateAdapter extends FragmentStateAdapter {

    MainActivityViewModel mainActivityViewModel;

    public ArticleTableFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, MainActivityViewModel viewModel) {
        super(fragmentActivity);
        mainActivityViewModel = viewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //return ArticleFragment.getOrCreateArticleFragment(position);
        return mainActivityViewModel.getArticleFragment(position+1);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
