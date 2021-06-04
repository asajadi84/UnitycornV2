package ir.unitycorn.app.roomlibrelated.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleEntityDao {

    @Query("select * from article")
    public List<ArticleEntity> getAllArticles();

    @Query("select * from article where article_id=:articleId")
    public ArticleEntity getArticleById(String articleId);

    @Query("select * from article where article_parent=:articleParent and article_order_in_parent=:articleOrderInParent")
    public ArticleEntity getArticleByOrderInParent(String articleParent, int articleOrderInParent);

    @Query("select * from article where article_parent=:categoryId")
    public List<ArticleEntity> getArticlesByCategory(String categoryId);

}
