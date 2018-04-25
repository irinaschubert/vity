package ch.ffhs.vity.vity.mock;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import java.util.Calendar;
import java.util.Date;

import ch.ffhs.vity.vity.activity.VityItem;
import ch.ffhs.vity.vity.database.AppDatabase;

public class DatabaseInitializer {

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static VityItem addItem(final AppDatabase db, final String owner, final String title, final String description, final String category, final String link, final String link_image) {
        VityItem item = new VityItem();
        item.setOwner(owner);
        item.setTitle(title);
        item.setDescription(description);
        item.setCategory(category);
        item.setLink(link);
        item.setLink_image(link_image);

        db.itemModel().insertNewItem(item);
        return item;
    }

    public static void populateWithTestData(AppDatabase db) {
        db.itemModel().deleteAll();

        addItem(db, "user1", "Restaurant1", "Super Restaurant in Basel", "Food and drinks", "www.restuarant1.ch", "");
        addItem(db, "user1","Restaurant2", "Super Restaurant in Luzern", "Food and drinks", "www.restuarant2.ch", "");
        addItem(db, "user1","Restaurant3", "Super Restaurant in Bern", "Food and drinks", "www.restuarant3.ch", "");
        addItem(db, "user1", "Restaurant4", "Super Restaurant in Basel", "Food and drinks", "www.restuarant4.ch", "");
        addItem(db, "user1","Restaurant5", "Super Restaurant in Luzern", "Food and drinks", "www.restuarant5.ch", "");
        addItem(db, "user1","Restaurant6", "Super Restaurant in Bern", "Food and drinks", "www.restuarant6.ch", "");

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
