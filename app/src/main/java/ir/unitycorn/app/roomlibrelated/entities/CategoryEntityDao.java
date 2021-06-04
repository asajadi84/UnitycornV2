package ir.unitycorn.app.roomlibrelated.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryEntityDao {

    //Search Activity => Returns the category with the matching category_id
    @Query("select * from category where category_id=:categoryId")
    public CategoryEntity getCategoryById(String categoryId);

    @Query("select * from category where category_type=:categoryType")
    public List<CategoryEntity> getCategoriesByType(String categoryType);

}
