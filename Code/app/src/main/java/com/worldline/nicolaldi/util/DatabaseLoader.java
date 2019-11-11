package com.worldline.nicolaldi.util;

import android.annotation.SuppressLint;
import android.content.Context;
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

    public static void loadDatabase(Context context, DatabaseLoadListener listener) {
        new DatabaseLoadingTask(context, listener).execute();
    }

    private static class DatabaseLoadingTask extends AsyncTask<Void, Void, List<StoreItem>> {

        @SuppressLint("StaticFieldLeak")
        private final Context applicationContext;
        private final WeakReference<DatabaseLoadListener> loadListener;

        public DatabaseLoadingTask(Context context, DatabaseLoadListener loadListener) {
            this.applicationContext = context.getApplicationContext();
            this.loadListener = new WeakReference<>(loadListener);
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

        private void queryIntoList(SQLiteDatabase database, List<StoreItem> items) {
            //TODO let's create this method!
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
