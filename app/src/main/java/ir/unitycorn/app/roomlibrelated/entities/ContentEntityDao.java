package ir.unitycorn.app.roomlibrelated.entities;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContentEntityDao {

    //Article Activity => Returns the contents with the matching article_id
    @Query("select * from content where content_parent=:parentId")
    public List<ContentEntity> getContentById(String parentId);

}
