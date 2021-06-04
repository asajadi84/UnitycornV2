package ir.unitycorn.app.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ir.unitycorn.app.ArticleTableModel;
import ir.unitycorn.app.fragments.ArticleFragment;
import ir.unitycorn.app.roomlibrelated.entities.ArticleRepository;
import ir.unitycorn.app.roomlibrelated.entities.CategoryEntity;
import ir.unitycorn.app.roomlibrelated.entities.CategoryRepository;

public class ArticleFragmentViewModel extends AndroidViewModel {

    private int pageNumber;
    private Context context;
    //private List<ArticleTableModel> articleTableModels;

    private CategoryRepository categoryRepository;
    private ArticleRepository articleRepository;

    public ArticleFragmentViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

        categoryRepository = new CategoryRepository(application);
        articleRepository = new ArticleRepository(application);
    }

    public List<ArticleTableModel> getArticleTableModels(){
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

    public void setPageNumber (int n){
        pageNumber = n;
    }
}
