package com.app.biketour.ui.user;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {UserSavedNote.class}, version = 1)
public abstract class UserSavedNoteDatabase extends RoomDatabase {

    private static UserSavedNoteDatabase instance;

    public abstract UserSavedNoteDao noteDao();

    public static synchronized UserSavedNoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserSavedNoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserSavedNoteDao noteDao;
        private PopulateDbAsyncTask(UserSavedNoteDatabase db) {
            noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new UserSavedNote("Title 1", "Description 1", "05-05-2020"));
            noteDao.insert(new UserSavedNote("Title 2", "Description 2", "09-05-2020"));
            noteDao.insert(new UserSavedNote("Title 3", "Description 3", "15-05-2020"));
            return null;
        }
    }
}
