package com.app.biketour.ui.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserSavedNoteDao {

    @Insert
    void insert(UserSavedNote note);

    @Update
    void update(UserSavedNote note);

    @Delete
    void delete(UserSavedNote note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY registerDate DESC")
    LiveData<List<UserSavedNote>> getAllNotes();
}
