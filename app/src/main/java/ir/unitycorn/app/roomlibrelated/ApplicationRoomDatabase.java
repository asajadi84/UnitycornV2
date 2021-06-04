package ir.unitycorn.app.roomlibrelated;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.unitycorn.app.roomlibrelated.entities.ArticleEntity;
import ir.unitycorn.app.roomlibrelated.entities.ArticleEntityDao;
import ir.unitycorn.app.roomlibrelated.entities.CategoryEntity;
import ir.unitycorn.app.roomlibrelated.entities.CategoryEntityDao;
import ir.unitycorn.app.roomlibrelated.entities.ContentEntity;
import ir.unitycorn.app.roomlibrelated.entities.ContentEntityDao;
import ir.unitycorn.app.roomlibrelated.entities.LibraryEntity;
import ir.unitycorn.app.roomlibrelated.entities.LibraryEntityDao;

@Database(entities = {CategoryEntity.class, ArticleEntity.class, ContentEntity.class, LibraryEntity.class}, version = 1)
public abstract class ApplicationRoomDatabase extends RoomDatabase {
    public abstract CategoryEntityDao getCategoryEntityDao();
    public abstract ArticleEntityDao getArticleEntityDao();
    public abstract ContentEntityDao getContentEntityDao();
    public abstract LibraryEntityDao getLibraryEntityDao();

    private static ApplicationRoomDatabase instance;

    public static synchronized ApplicationRoomDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ApplicationRoomDatabase.class, "unitycorn")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .createFromAsset("databases/unitycorn.db").build();
        }
        return instance;
    }

}
