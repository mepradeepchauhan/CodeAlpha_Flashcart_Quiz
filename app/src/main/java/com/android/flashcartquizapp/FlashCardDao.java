package com.android.flashcartquizapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FlashCardDao {
    @Insert
    void insertFlashCard(FlashCard flashCard);

    @Query("SELECT * FROM flashcards")
    List<FlashCard> getAllFlashCards();

    @Delete
    void deleteFlashCard(FlashCard flashCard);
}
