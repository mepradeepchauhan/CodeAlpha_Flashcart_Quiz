package com.android.flashcartquizapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FlashCard.class}, version = 1)
public abstract class FlashCardDatabase extends RoomDatabase {
    private static FlashCardDatabase instance;

    public abstract FlashCardDao flashCardDao();

    public static synchronized FlashCardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            FlashCardDatabase.class, "flashcard_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
