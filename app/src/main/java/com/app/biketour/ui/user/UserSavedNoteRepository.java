package com.app.biketour.ui.user;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserSavedNoteRepository {

    private UserSavedNoteDao userSavedNoteDao;

    private LiveData<List<UserSavedNote>> allNotes;

    public UserSavedNoteRepository(Application application) {
        UserSavedNoteDatabase database = UserSavedNoteDatabase.getInstance(application);
        userSavedNoteDao = database.noteDao();
        allNotes = userSavedNoteDao.getAllNotes();
    }

    public void insert(UserSavedNote note) {
        new InsertNoteAsyncTask(userSavedNoteDao).execute(note);
    }

    public void update(UserSavedNote note) {
        new UpdateNoteAsyncTask(userSavedNoteDao).execute(note);
    }

    public void delete(UserSavedNote note) {
        new DeleteNoteAsyncTask(userSavedNoteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(userSavedNoteDao).execute();
    }

    public LiveData<List<UserSavedNote>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<UserSavedNote, Void, Void> {
        private UserSavedNoteDao userSavedNoteDao;
        private InsertNoteAsyncTask(UserSavedNoteDao userSavedNoteDao) {
            this.userSavedNoteDao = userSavedNoteDao;
        }
        @Override
        protected Void doInBackground(UserSavedNote... notes) {
            userSavedNoteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<UserSavedNote, Void, Void> {
        private UserSavedNoteDao userSavedNoteDao;
        private UpdateNoteAsyncTask(UserSavedNoteDao userSavedNoteDao) {
            this.userSavedNoteDao = userSavedNoteDao;
        }
        @Override
        protected Void doInBackground(UserSavedNote... notes) {
            userSavedNoteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<UserSavedNote, Void, Void> {
        private UserSavedNoteDao userSavedNoteDao;
        private DeleteNoteAsyncTask(UserSavedNoteDao userSavedNoteDao) {
            this.userSavedNoteDao = userSavedNoteDao;
        }
        @Override
        protected Void doInBackground(UserSavedNote... notes) {
            userSavedNoteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserSavedNoteDao userSavedNoteDao;
        private DeleteAllNotesAsyncTask(UserSavedNoteDao userSavedNoteDao) {
            this.userSavedNoteDao = userSavedNoteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            userSavedNoteDao.deleteAllNotes();
            return null;
        }
    }
}
