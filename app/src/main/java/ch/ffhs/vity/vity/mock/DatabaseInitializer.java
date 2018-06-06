package ch.ffhs.vity.vity.mock;


import android.os.AsyncTask;

import ch.ffhs.vity.vity.database.VityItem;
import ch.ffhs.vity.vity.database.AppDatabase;

public class DatabaseInitializer {

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void addItem(final AppDatabase db, final String owner, final String title, final String description, final String category, final String link, String location) {
        VityItem item = new VityItem();
        item.setOwner(owner);
        item.setTitle(title);
        item.setDescription(description);
        item.setCategory(category);
        item.setLink(link);
        item.setLocation(location);

        db.itemModel().insertNewItem(item);
    }

    private static void populateWithTestData(AppDatabase db) {
        db.itemModel().deleteAll();

        addItem(db, "user1", "Boo", "Bestes thailändisches Restaurant in Basel.", "Food and drinks", "www.boo.ch", "47.567042,7.590990");
        addItem(db, "user2","Gatto Nero", "Authentisches italienisches Restaurant mit gemütlicher Atmosphäre. Unbedingt reservieren, da sehr klein und immer voll!", "Food and drinks", "www.gattonero.ch", "47.567793,7.590696");
        addItem(db, "user3","Jakob's Basler Leckerly", "Besser als das Original", "Shopping", "www.baslerleckerly.ch", "47.564721,7.583269");
        addItem(db, "user4", "Am Rhein chillen", "Immer empfehlenswert", "Place to stay", "", "47.567567,7.586542");
        addItem(db, "user5","Allschwiler Wald", "Vita Parcours!", "Walk", "", "47.541911,7.551925");
        addItem(db, "user6","Fondation Beyeler", "Kunstmuseum ausserhalb von Basel mit sehenswerten Ausstellungen. Mein Lieblingsmuseum der Schweiz.", "Museum", "www.fondation.ch", "47.587879,7.651102");
        addItem(db, "user3", "Hirscheneck", "Genossenschaftlich geführtes und super alternatives Restaurant. Immer vegane und vegetarische Gerichte im Angebot.", "Food and drinks", "www.hirscheneck.ch", "47.567042,7.590990");
        addItem(db, "user4","Cargo Bar", "Direkt am Rheinufer", "Food and drinks", "www.cargobar.ch", "47.567793,7.590696");
        addItem(db, "user5","Markthalle Basel", "Unabhängiger Ort mit kleinen, kreativen Läden, Bars und Food-Ständen", "Shopping", "www.markthallebasel.ch", "47.549099,7.587230");
        addItem(db, "user6", "Uferstrasse", "Gemütlicher Spaziergang entlang des Rheinufers durch das besetzte Uferareal.", "Walk", "", "47.576851,7.586165");
        addItem(db, "user2","Kraftwerkinsel Basel", "Sehr gemütlich und ruhig, da dort nur wenige Leute hingehen. Ideal zum Baden und Grillen.", "Place to stay", "", "47.559740,7.630060");
        addItem(db, "user1","Tinguely Museum", "Dem Künstler Jean Tinguely gewidmet. Mit einer interaktiven Dauerausstellung und immer wieder sehr speziellen Sonderausstellungen. Ausserdem lässt sich von dort aus gut Rheinschwimmen.", "Museum", "www.tinguely.ch", "47.558769,7.612623");
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
