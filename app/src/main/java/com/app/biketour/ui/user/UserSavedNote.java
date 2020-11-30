package com.app.biketour.ui.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class UserSavedNote {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String registerDate;

    public UserSavedNote(String title, String description, String registerDate) {
        this.title = title;
        this.description = description;
        this.registerDate = registerDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRegisterDate() {
        return registerDate;
    }
}
