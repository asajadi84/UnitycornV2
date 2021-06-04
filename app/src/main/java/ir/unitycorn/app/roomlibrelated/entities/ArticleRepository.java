package ir.unitycorn.app.roomlibrelated.entities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import ir.unitycorn.app.roomlibrelated.ApplicationRoomDatabase;

public class ArticleRepository {

    private ArticleEntityDao articleEntityDao;
    private CategoryEntityDao categoryEntityDao;
    private Context context;

    public ArticleRepository(Application application){
        ApplicationRoomDatabase applicationRoomDatabase =
                ApplicationRoomDatabase.getInstance(application);
        articleEntityDao = applicationRoomDatabase.getArticleEntityDao();
        categoryEntityDao = applicationRoomDatabase.getCategoryEntityDao();
        context = application.getApplicationContext();
    }

    public List<ArticleEntity> getEntities(){
        return articleEntityDao.getAllArticles();
    }

    public List<ArticleEntity> getArticlesByParentId(String parentId){
        return articleEntityDao.getArticlesByCategory(parentId);
    }

    public String getPreviousArticle(String articleId){
        if(articleEntityDao.getArticleById(articleId).getArticleOrderInParent()==1){
            return "none";
        }else{
            int previousArticleOrderInParent = articleEntityDao.getArticleById(articleId).getArticleOrderInParent() - 1;
            return articleEntityDao.getArticleByOrderInParent(articleEntityDao.getArticleById(articleId).getArticleParent(), previousArticleOrderInParent).getArticleId();
        }
    }

    public String getNextArticle(String articleId){
        String articleCategory = articleEntityDao.getArticleById(articleId).getArticleParent();
        int totalArticlesInThisCategory = articleEntityDao.getArticlesByCategory(articleCategory).size();

        if(articleEntityDao.getArticleById(articleId).getArticleOrderInParent()==totalArticlesInThisCategory){
            return "none";
        }else{
            int nextArticleOrderInParent = articleEntityDao.getArticleById(articleId).getArticleOrderInParent() + 1;
            return articleEntityDao.getArticleByOrderInParent(articleEntityDao.getArticleById(articleId).getArticleParent(), nextArticleOrderInParent).getArticleId();
        }
    }

    public String articleIdToValue(String articleId){
        return articleEntityDao.getArticleById(articleId).getArticleValue();
    }

    public int getNumberOfUnreadArticlesByParentId(String parentId){
        SharedPreferences readPrefs = context.getSharedPreferences("read_articles", Context.MODE_PRIVATE);
        int numberOfUnreadArticles = 0;
        List<ArticleEntity> temp = getArticlesByParentId(parentId);
        for (ArticleEntity a : temp) {
            if(!readPrefs.getBoolean(a.getArticleId(), false)){
                numberOfUnreadArticles++;
            }
        }
        return numberOfUnreadArticles;
    }

    public List<String> getArticlesNamesByParentId(String parentId){
        List<String> tempStringList = new ArrayList<>();
        List<ArticleEntity> temp = getArticlesByParentId(parentId);
        for (ArticleEntity a : temp) {
            tempStringList.add(a.getArticleValue());
        }
        return tempStringList;
    }

    public List<String> getArticlesIdsByParentId(String parentId){
        List<String> tempStringList = new ArrayList<>();
        List<ArticleEntity> temp = getArticlesByParentId(parentId);
        for (ArticleEntity a : temp) {
            tempStringList.add(a.getArticleId());
        }
        return tempStringList;
    }

    public List<Boolean> getArticlesUnreadStatusByParentId(String parentId){
        SharedPreferences readPrefs = context.getSharedPreferences("read_articles", Context.MODE_PRIVATE);
        List<Boolean> tempBooleanList = new ArrayList<>();
        List<ArticleEntity> temp = getArticlesByParentId(parentId);
        for (ArticleEntity a : temp) {
            tempBooleanList.add(!readPrefs.getBoolean(a.getArticleId(), false));
        }
        return tempBooleanList;
    }

    public boolean isArticlePremiumById(String articleId){
        ArticleEntity article = articleEntityDao.getArticleById(articleId);
        String articleParentId = article.getArticleParent();
        CategoryEntity articleCategory = categoryEntityDao.getCategoryById(articleParentId);
        return articleCategory.getCategoryIsPremium() == 1;
    }

    public int getArticleUniqueId(String articleId){
        int articleOrderInParent = articleEntityDao.getArticleById(articleId).getArticleOrderInParent();
        String articleParentId = articleEntityDao.getArticleById(articleId).getArticleParent();
        CategoryEntity c = categoryEntityDao.getCategoryById(articleParentId);
        int articleCategoryOrder = c.getCategoryOrder();
        int uniqueId = (articleCategoryOrder * 10000) + articleOrderInParent;
        return uniqueId;
    }

    public int numberOfUnreadArticlesByCategoryType(int categoryType){
        int numberOfUnreadArticles = 0;
        SharedPreferences readPrefs = context.getSharedPreferences("read_articles", Context.MODE_PRIVATE);

        //1 Get All Categories Matching The Type
        List<CategoryEntity> categoryEntityList = categoryEntityDao.getCategoriesByType(Integer.toString(categoryType));

        //2 Foreach Article In Those Categories, If Article Is Unread Then Increase The numberOfUnreadArticles
        for(CategoryEntity cat : categoryEntityList){
            String categoryId = cat.getCategoryId();
            List<ArticleEntity> tempEntities = articleEntityDao.getArticlesByCategory(categoryId);

            for(ArticleEntity art : tempEntities){
                if(!readPrefs.getBoolean(art.getArticleId(), false)){
                    numberOfUnreadArticles++;
                }
            }
        }

        return numberOfUnreadArticles;
    }

    public List<ArticleEntity> allArticlesMatchingStringQuery(String query){
        List<ArticleEntity> matchingArticleEntities = new ArrayList<>();
        List<ArticleEntity> allArticles = articleEntityDao.getAllArticles();

        for (ArticleEntity ae : allArticles){
            String articleValue = ae.getArticleValue();
            String parentId = ae.getArticleParent();
            String parentValue = categoryEntityDao.getCategoryById(parentId).getCategoryValue();

            if(articleValue.contains(query) || parentValue.contains(query)){
                matchingArticleEntities.add(ae);
            }
        }

        return matchingArticleEntities;
    }
}
