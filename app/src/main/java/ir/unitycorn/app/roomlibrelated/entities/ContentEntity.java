package ir.unitycorn.app.roomlibrelated.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "content", foreignKeys = @ForeignKey(entity = ArticleEntity.class, parentColumns = "article_id", childColumns = "content_parent"))
public class ContentEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "content_id")
    private int contentId;

    @NonNull
    @ColumnInfo(name = "content_parent")
    private String contentParent;

    @NonNull
    @ColumnInfo(name = "content_order_in_parent")
    private int contentOrderInParent;

    @NonNull
    @ColumnInfo(name = "content_value")
    private String contentValue;

    @NonNull
    @ColumnInfo(name = "content_type")
    private int contentType;

    public ContentEntity(int contentId, String contentParent, int contentOrderInParent, String contentValue, int contentType) {
        this.contentId = contentId;
        this.contentParent = contentParent;
        this.contentOrderInParent = contentOrderInParent;
        this.contentValue = contentValue;
        this.contentType = contentType;
    }

    public int getContentId() {
        return contentId;
    }

    public String getContentParent() {
        return contentParent;
    }

    public int getContentOrderInParent() {
        return contentOrderInParent;
    }

    public String getContentValue() {
        return contentValue;
    }

    public int getContentType() {
        return contentType;
    }
}
