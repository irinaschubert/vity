package ch.ffhs.vity.vity.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import ch.ffhs.vity.vity.database.VityItem;

@Database(entities = {VityItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract VityItemDao itemModel();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mDb").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}