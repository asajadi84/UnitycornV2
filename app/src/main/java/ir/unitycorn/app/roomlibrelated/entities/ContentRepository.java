package ir.unitycorn.app.roomlibrelated.entities;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import ir.unitycorn.app.roomlibrelated.ApplicationRoomDatabase;

public class ContentRepository {

    private ContentEntityDao contentEntityDao;

    public ContentRepository(Application application){
        ApplicationRoomDatabase applicationRoomDatabase =
                ApplicationRoomDatabase.getInstance(application);
        contentEntityDao = applicationRoomDatabase.getContentEntityDao();
    }

    public List<String> getContentValuesOfArticleByParentId (String parentId){
        List<ContentEntity> tempEntities = contentEntityDao.getContentById(parentId);
        List<String> tempStrings = new ArrayList<>();
        for(ContentEntity c : tempEntities){
            tempStrings.add(c.getContentValue());
        }
        return tempStrings;
    }

    public List<Integer> getContentTypesOfArticleByParentId (String parentId){
        List<ContentEntity> tempEntities = contentEntityDao.getContentById(parentId);
        List<Integer> tempIntegers = new ArrayList<>();
        for(ContentEntity c : tempEntities){
            tempIntegers.add(c.getContentType());
        }
        return tempIntegers;
    }

}
