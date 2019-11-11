package com.worldline.nicolaldi.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.worldline.nicolaldi.R;

/**
 * @author Nicola Verbeeck
 */
public class DatabaseCreator {

    private static class StoreEntryHolder {

        final String nameKey;
        final double price;
        final String unit;
        final String imageKey;

        public StoreEntryHolder(String nameKey, double price, String unit, String imageKey) {
            this.nameKey = nameKey;
            this.price = price;
            this.unit = unit;
            this.imageKey = imageKey;
        }
    }

    private static StoreEntryHolder[] entries = new StoreEntryHolder[]{
            new StoreEntryHolder("apple", 2.0, "/kg", "apple"),
            new StoreEntryHolder("banana", 3.0, "/kg", "banana"),
            new StoreEntryHolder("berry", 4.0, "/kg", "berry"),
            new StoreEntryHolder("grape", 5.0, "/kg", "grape"),
            new StoreEntryHolder("grapefruit", 6.0, "/kg", "grapefruit"),
            new StoreEntryHolder("lemon", 7.0, "/kg", "lemon"),
            new StoreEntryHolder("orange", 8.0, "/kg", "orange"),
            new StoreEntryHolder("pear", 9.5, "/kg", "pear"),
            new StoreEntryHolder("strawberry", 10.0, "/kg", "strawberry"),
            new StoreEntryHolder("watermelon", 102.0, "/kg", "watermelon")
    };

    public static class DatabaseConstants {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME_KEY = "nameKey";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE_KEY = "imageKey";

        public static final String DATABASE_FILE_NAME = "storeDatabase";
    }

    public static SQLiteOpenHelper createDatabaseOpenHelper(Context context) {
        return new SQLiteOpenHelper(context, DatabaseConstants.DATABASE_FILE_NAME, null, 1) {

            @Override
            public void onOpen(SQLiteDatabase db) {
                super.onOpen(db);
                db.disableWriteAheadLogging();
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE " + DatabaseConstants.TABLE_NAME
                        + " (" + DatabaseConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + DatabaseConstants.COLUMN_NAME_KEY + " TEXT NOT NULL, "
                        + DatabaseConstants.COLUMN_UNIT + " TEXT NOT NULL, "
                        + DatabaseConstants.COLUMN_PRICE + " REAL NOT NULL, "
                        + DatabaseConstants.COLUMN_IMAGE_KEY + " TEXT NOT NULL)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                //No upgrade required
            }
        };
    }


    public static void createDatabase(final Context context) {

        final SQLiteDatabase writableDatabase = createDatabaseOpenHelper(context).getWritableDatabase();
        writableDatabase.beginTransaction();

        for (int i = 0; i < 10000; ++i) {
            final StoreEntryHolder holder = entries[i % entries.length];

            final ContentValues values = new ContentValues();
            values.put(DatabaseConstants.COLUMN_NAME_KEY, holder.nameKey);
            values.put(DatabaseConstants.COLUMN_IMAGE_KEY, holder.imageKey);
            values.put(DatabaseConstants.COLUMN_PRICE, holder.price);
            values.put(DatabaseConstants.COLUMN_UNIT, holder.unit);

            writableDatabase.insert(DatabaseConstants.TABLE_NAME, null, values);
        }

        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        writableDatabase.close();
    }

    public static int getStringResourceForKey(String key) {
        switch (key) {
            case "apple":
                return R.string.item_apple;
            case "banana":
                return R.string.item_banana;
            case "berry":
                return R.string.item_berry;
            case "grape":
                return R.string.item_grape;
            case "grapefruit":
                return R.string.item_grapefruit;
            case "lemon":
                return R.string.item_lemon;
            case "orange":
                return R.string.item_orange;
            case "pear":
                return R.string.item_pear;
            case "strawberry":
                return R.string.item_strawberry;
            case "watermelon":
                return R.string.item_watermelon;
        }
        return 0;
    }

    public static int getImageResourceForKey(String key) {
        switch (key) {
            case "apple":
                return R.drawable.apple;
            case "banana":
                return R.drawable.banana;
            case "berry":
                return R.drawable.berry;
            case "grape":
                return R.drawable.grape;
            case "grapefruit":
                return R.drawable.grapefruit;
            case "lemon":
                return R.drawable.lemon;
            case "orange":
                return R.drawable.orange;
            case "pear":
                return R.drawable.pear;
            case "strawberry":
                return R.drawable.strawberry;
            case "watermelon":
                return R.drawable.watermelon;
        }
        return 0;
    }

}
