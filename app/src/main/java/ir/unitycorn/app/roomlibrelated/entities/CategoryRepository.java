package ir.unitycorn.app.roomlibrelated.entities;

import android.app.Application;

import java.util.List;

import ir.unitycorn.app.roomlibrelated.ApplicationRoomDatabase;

public class CategoryRepository {

    private CategoryEntityDao categoryEntityDao;

    public CategoryRepository(Application application){
        ApplicationRoomDatabase applicationRoomDatabase =
                ApplicationRoomDatabase.getInstance(application);
        categoryEntityDao = applicationRoomDatabase.getCategoryEntityDao();
    }

    public List<CategoryEntity> getAllCategoriesOfType(int n){
        return categoryEntityDao.getCategoriesByType(Integer.toString(n));
    }

    public String getCategoryValueFromId(String categoryId){
        return categoryEntityDao.getCategoryById(categoryId).getCategoryValue();
    }

    public int getCategoryTypeFromId(String categoryId){
        return categoryEntityDao.getCategoryById(categoryId).getCategoryType();
    }

}
