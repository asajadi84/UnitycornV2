package ir.unitycorn.app.roomlibrelated.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "article", foreignKeys = @ForeignKey(entity = CategoryEntity.class, parentColumns = "category_id", childColumns = "article_parent"))
public class ArticleEntity {

    @NonNull
    @ColumnInfo(name = "article_parent")
    private String articleParent;

    @NonNull
    @ColumnInfo(name = "article_order_in_parent")
    private int articleOrderInParent;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "article_id")
    private String articleId;

    @NonNull
    @ColumnInfo(name = "article_value")
    private String articleValue;

    public ArticleEntity(String articleParent, int articleOrderInParent, String articleId, String articleValue) {
        this.articleParent = articleParent;
        this.articleOrderInParent = articleOrderInParent;
        this.articleId = articleId;
        this.articleValue = articleValue;
    }

    public String getArticleParent() {
        return articleParent;
    }

    public int getArticleOrderInParent() {
        return articleOrderInParent;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getArticleValue() {
        return articleValue;
    }
}
