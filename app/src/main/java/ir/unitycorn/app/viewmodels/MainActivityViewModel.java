package ir.unitycorn.app.viewmodels;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

import ir.unitycorn.app.ArticleTableModel;
import ir.unitycorn.app.fragments.ArticleFragment;
import ir.unitycorn.app.roomlibrelated.entities.ArticleRepository;
import ir.unitycorn.app.roomlibrelated.entities.CategoryEntity;
import ir.unitycorn.app.roomlibrelated.entities.CategoryRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private ArticleFragment articleFragment1;
    private ArticleFragment articleFragment2;
    private ArticleFragment articleFragment3;

    private ArticleRepository articleRepository;
    private CategoryRepository categoryRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        articleRepository = new ArticleRepository(application);
        categoryRepository = new CategoryRepository(application);

        articleFragment1 = new ArticleFragment(this);
        Bundle bundle1 = new Bundle();
        bundle1.putInt("section_number", 1);
        articleFragment1.setArguments(bundle1);

        articleFragment2 = new ArticleFragment(this);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("section_number", 2);
        articleFragment2.setArguments(bundle2);

        articleFragment3 = new ArticleFragment(this);
        Bundle bundle3 = new Bundle();
        bundle3.putInt("section_number", 3);
        articleFragment3.setArguments(bundle3);

    }

    public ArticleFragment getArticleFragment(int articleFragmentCat){
        switch(articleFragmentCat){
            case 1:
                return articleFragment1;

            case 2:
                return articleFragment2;

            case 3:
                return articleFragment3;

            default:
                return articleFragment1;
        }
    }

    public boolean articleIdToPremiumStatus(String id){
        return articleRepository.isArticlePremiumById(id);
    }

    public int getNumberOfUnreadArticlesByCategoryType(int categoryType){
        return articleRepository.numberOfUnreadArticlesByCategoryType(categoryType);
    }

    public List<ArticleTableModel> getArticleTableModels(int pageNumber){
        List<ArticleTableModel> fetchedArticleTableModels = new ArrayList<>();

        List<CategoryEntity> categoryEntityList = categoryRepository.getAllCategoriesOfType(pageNumber);

        for (int i = 0; i<categoryEntityList.size(); i++){
            fetchedArticleTableModels.add(new ArticleTableModel(
                    categoryEntityList.get(i).getCategoryType(),
                    categoryEntityList.get(i).getCategoryValue(),
                    articleRepository.getNumberOfUnreadArticlesByParentId(categoryEntityList.get(i).getCategoryId()),
                    categoryEntityList.get(i).getCategoryIsPremium() == 1,
                    articleRepository.getArticlesNamesByParentId(categoryEntityList.get(i).getCategoryId()),
                    articleRepository.getArticlesIdsByParentId(categoryEntityList.get(i).getCategoryId()),
                    articleRepository.getArticlesUnreadStatusByParentId(categoryEntityList.get(i).getCategoryId())
            ));
        }

        return fetchedArticleTableModels;

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(this.getClass().getSimpleName(), "VM CLEARED");
    }
}
