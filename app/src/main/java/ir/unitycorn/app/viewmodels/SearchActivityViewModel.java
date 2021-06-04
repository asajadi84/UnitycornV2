package ir.unitycorn.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ir.unitycorn.app.roomlibrelated.entities.ArticleEntity;
import ir.unitycorn.app.roomlibrelated.entities.ArticleRepository;
import ir.unitycorn.app.roomlibrelated.entities.CategoryRepository;

public class SearchActivityViewModel extends AndroidViewModel {

    private ArticleRepository articleRepository;
    private CategoryRepository categoryRepository;

    public SearchActivityViewModel(@NonNull Application application) {
        super(application);

        articleRepository = new ArticleRepository(application);
        categoryRepository = new CategoryRepository(application);

    }

    public List<ArticleEntity> getTotalArticles (){
        return articleRepository.getEntities();
    }

    public List<ArticleEntity> getArticlesMatchingStringQuery (String query){
        return articleRepository.allArticlesMatchingStringQuery(query);
    }

    public String getCategoryValueById(String categoryId){
        return categoryRepository.getCategoryValueFromId(categoryId);
    }

    public int getCategoryTypeById(String categoryId){
        return categoryRepository.getCategoryTypeFromId(categoryId);
    }

    public boolean articleIdToPremiumStatus(String id){
        return articleRepository.isArticlePremiumById(id);
    }
}
