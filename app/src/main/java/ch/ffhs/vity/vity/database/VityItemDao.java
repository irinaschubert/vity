package ch.ffhs.vity.vity.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.ffhs.vity.vity.activity.VityItem;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VityItemDao {
    @Query("select * from VityItem where id = :id")
    VityItem loadItemById(long id);

    @Query("SELECT * FROM VityItem")
    List<VityItem> getAllItems();

    @Query("SELECT * FROM VityItem WHERE category = :category")
    List<VityItem> findItemByCategory(String category);

    // TODO: 25.04.2018
/*    @Query("SELECT * FROM VityItem WHERE distance = :distance")
    List<VityItem> findByDistance(int distance);*/

    @Insert(onConflict = IGNORE)
    public void insertNewItem(VityItem item);

    @Update(onConflict = REPLACE)
    public void updateItem(VityItem item);

    @Delete
    public void deleteItem(VityItem item);

    @Query("DELETE FROM VityItem")
    public void deleteAll();
}
