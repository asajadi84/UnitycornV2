package ir.unitycorn.app.roomlibrelated.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class CategoryEntity {

    @NonNull
    @ColumnInfo(name = "category_order")
    private int categoryOrder;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "category_id")
    private String categoryId;

    @NonNull
    @ColumnInfo(name = "category_value")
    private String categoryValue;

    @NonNull
    @ColumnInfo(name = "category_type")
    private int categoryType;

    @NonNull
    @ColumnInfo(name = "category_is_premium")
    private int categoryIsPremium;

    public CategoryEntity(int categoryOrder, String categoryId, String categoryValue, int categoryType, int categoryIsPremium) {
        this.categoryOrder = categoryOrder;
        this.categoryId = categoryId;
        this.categoryValue = categoryValue;
        this.categoryType = categoryType;
        this.categoryIsPremium = categoryIsPremium;
    }

    public int getCategoryOrder() {
        return categoryOrder;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public int getCategoryIsPremium() {
        return categoryIsPremium;
    }
}
