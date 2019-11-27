package com.worldline.nicolaldi.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.worldline.nicolaldi.R;
import com.worldline.nicolaldi.model.StoreItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicola Verbeeck
 */
public class DatabaseLoader {

    private static final String TAG = "DatabaseLoader";

    public static void loadDatabase(Context context, String nameKey, DatabaseLoadListener listener) {
        new DatabaseLoadingTask(context, nameKey, listener).execute();
    }

    private static class DatabaseLoadingTask extends AsyncTask<Void, Void, List<StoreItem>> {

        @SuppressLint("StaticFieldLeak")
        private final Context applicationContext;
        private final WeakReference<DatabaseLoadListener> loadListener;
        private String nameKey;

        public DatabaseLoadingTask(Context context, String nameKey, DatabaseLoadListener loadListener) {
            this.applicationContext = context.getApplicationContext();
            this.loadListener = new WeakReference<>(loadListener);
            this.nameKey = nameKey;
        }

        @Override
        protected List<StoreItem> doInBackground(Void... voids) {
            File databaseFile = applicationContext.getDatabasePath(DatabaseCreator.DatabaseConstants.DATABASE_FILE_NAME);
            if (!databaseFile.exists()) {
                copyDatabase(databaseFile);
            }

            final SQLiteOpenHelper openHelper = DatabaseCreator.createDatabaseOpenHelper(applicationContext);

            List<StoreItem> items = new ArrayList<>();

            queryIntoList(openHelper.getReadableDatabase(), items);

            return items;
        }

        @Override
        protected void onPostExecute(List<StoreItem> storeItems) {
            super.onPostExecute(storeItems);
            final DatabaseLoadListener databaseLoadListener = loadListener.get();
            if (databaseLoadListener != null)
                databaseLoadListener.onDatabaseLoaded(storeItems);
        }

        private void queryIntoList(SQLiteDatabase database, List<StoreItem> items) {
            //SELECT * FROM products WHERE nameKey=...;
            Cursor cursor = database.query(DatabaseCreator.DatabaseConstants.TABLE_NAME,
                    null, DatabaseCreator.DatabaseConstants.COLUMN_NAME_KEY + "=?", new String[]{nameKey}, null, null, null);
            try {

                if (!cursor.moveToFirst()) {
                    return;
                }

                int nameIndex = cursor.getColumnIndex(DatabaseCreator.DatabaseConstants.COLUMN_NAME_KEY);
                int unitIndex = cursor.getColumnIndex(DatabaseCreator.DatabaseConstants.COLUMN_UNIT);
                int priceIndex = cursor.getColumnIndex(DatabaseCreator.DatabaseConstants.COLUMN_PRICE);
                int imageIndex = cursor.getColumnIndex(DatabaseCreator.DatabaseConstants.COLUMN_IMAGE_KEY);

                do {

                    String nameKey = cursor.getString(nameIndex);
                    String unit = cursor.getString(unitIndex);
                    double price = cursor.getDouble(priceIndex);
                    String imageKey = cursor.getString(imageIndex);

                    int stringResource = DatabaseKeyMapper.getStringResourceForKey(nameKey);
                    int imageResource = DatabaseKeyMapper.getImageResourceForKey(imageKey);
                    StoreItem item = new StoreItem(applicationContext.getString(stringResource),
                            price,
                            unit,
                            imageResource);

                    items.add(item);
                } while (cursor.moveToNext());

            } finally {
                cursor.close();
            }
        }

        //Copy bytes from raw database resource to given file
        private void copyDatabase(File databaseFile) {
            try (InputStream in = applicationContext.getResources().openRawResource(R.raw.database)) {

                try (OutputStream out = new FileOutputStream(databaseFile)) {

                    byte[] buffer = new byte[1024];

                    int num = -1;
                    while ((num = in.read(buffer)) > 0) {
                        out.write(buffer, 0, num);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Failed to copy database file", e);
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to copy database file", e);
            }
        }

    }

    public interface DatabaseLoadListener {

        void onDatabaseLoaded(List<StoreItem> items);

    }

}
