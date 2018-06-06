package ch.ffhs.vity.vity.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VityItemDao {
    @Query("select * from VityItem where id = :id")
    VityItem loadItemById(long id);

    @Query("SELECT * FROM VityItem WHERE category = :category")
    List<VityItem> findItemByCategory(String category);

    @Insert(onConflict = IGNORE)
    void insertNewItem(VityItem item);

    @Delete
    void deleteItem(VityItem item);

    // only for mock data initializer
    @Query("DELETE FROM VityItem")
    void deleteAll();
}
