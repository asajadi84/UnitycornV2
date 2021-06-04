package ir.unitycorn.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ir.unitycorn.app.roomlibrelated.entities.ArticleRepository;
import ir.unitycorn.app.roomlibrelated.entities.ContentRepository;

public class ArticleActivityViewModel extends AndroidViewModel {

    private ArticleRepository articleRepository;
    private ContentRepository contentRepository;

    public ArticleActivityViewModel(@NonNull Application application) {
        super(application);

        articleRepository = new ArticleRepository(application);
        contentRepository = new ContentRepository(application);
    }

    public List<String> getContentValuesOfArticleByParentId (String parentId){
        return contentRepository.getContentValuesOfArticleByParentId(parentId);
    }

    public List<Integer> getContentTypesOfArticleByParentId (String parentId){
        return contentRepository.getContentTypesOfArticleByParentId(parentId);
    }

    public String getPreviousArticleId(String articleId){
        return articleRepository.getPreviousArticle(articleId);
    }

    public String getNextArticleId(String articleId){
        return articleRepository.getNextArticle(articleId);
    }

    public String getArticleValueByArticleId(String articleId){
        return articleRepository.articleIdToValue(articleId);
    }

    public boolean isPremium(String articleId){
        return articleRepository.isArticlePremiumById(articleId);
    }

    public int getArticleUniqueId(String articleId){
        return articleRepository.getArticleUniqueId(articleId);
    }
}
