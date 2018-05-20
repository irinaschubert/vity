package ch.ffhs.vity.vity.mock;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import java.util.Calendar;
import java.util.Date;

import ch.ffhs.vity.vity.database.VityItem;
import ch.ffhs.vity.vity.database.AppDatabase;

public class DatabaseInitializer {

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static VityItem addItem(final AppDatabase db, final String owner, final String title, final String description, final String category, final String link, String location) {
        VityItem item = new VityItem();
        item.setOwner(owner);
        item.setTitle(title);
        item.setDescription(description);
        item.setCategory(category);
        item.setLink(link);
        item.setLocation(location);

        db.itemModel().insertNewItem(item);
        return item;
    }

    public static void populateWithTestData(AppDatabase db) {
        db.itemModel().deleteAll();

        addItem(db, "user1", "Restaurant1", "Super Restaurant in Basel", "Food and drinks", "www.restuarant1.ch", "47.565727,7.5732980000000225");
        addItem(db, "user1","Restaurant2", "Super Restaurant in Luzern", "Food and drinks", "www.restuarant2.ch", "47.05016819999999,8.309307200000035");
        addItem(db, "user1","Restaurant3", "Super Restaurant in Bern", "Food and drinks", "www.restuarant3.ch", "46.9479739,7.447446799999966");
        addItem(db, "user1", "Restaurant4", "Super Restaurant in Basel", "Food and drinks", "www.restuarant4.ch", "47.559817,7.592415899999992");
        addItem(db, "user1","Restaurant5", "Super Restaurant in Luzern", "Food and drinks", "www.restuarant5.ch", "47.0533979,8.305655699999988");
        addItem(db, "user1","Restaurant6", "Super Restaurant in Bern", "Food and drinks", "www.restuarant6.ch", "46.9479748,7.436327200000051");

    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
