package ir.unitycorn.app.roomlibrelated.entities;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LibraryEntityDao {

    @Query("select * from library")
    public List<LibraryEntity> getLibraries();

}
