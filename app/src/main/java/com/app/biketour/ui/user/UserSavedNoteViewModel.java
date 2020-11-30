package com.app.biketour.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserSavedNoteViewModel extends AndroidViewModel {

    private UserSavedNoteRepository repository;
    private LiveData<List<UserSavedNote>> allNotes;
    public UserSavedNoteViewModel(@NonNull Application application) {
        super(application);
        repository = new UserSavedNoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(UserSavedNote note) {
        repository.insert(note);
    }

    public void update(UserSavedNote note) {
        repository.update(note);
    }

    public void delete(UserSavedNote note) {
        repository.delete(note);
    }

    public LiveData<List<UserSavedNote>> getAllNotes() {
        return allNotes;
    }
}
