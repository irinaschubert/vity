package ch.ffhs.vity.vity.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {VityItem.class}, version = 1, exportSchema = false)
@TypeConverters({LocationTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mDb").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public abstract VityItemDao itemModel();
}