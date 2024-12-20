package com.android.flashcartquizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FlashCardAdapter extends RecyclerView.Adapter<FlashCardAdapter.FlashCardViewHolder> {
    private final List<FlashCard> flashCards;
    private final OnDeleteFlashCardListener deleteFlashCardListener;

    public interface OnDeleteFlashCardListener {
        void onDeleteFlashCard(FlashCard flashCard);
    }

    public FlashCardAdapter(List<FlashCard> flashCards, OnDeleteFlashCardListener deleteFlashCardListener) {
        this.flashCards = flashCards;
        this.deleteFlashCardListener = deleteFlashCardListener;
    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new FlashCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position) {
        FlashCard flashCard = flashCards.get(position);
        holder.questionText.setText(flashCard.getQuestion());
        holder.answerText.setText(flashCard.getAnswer());
        holder.itemView.setOnLongClickListener(v -> {
            deleteFlashCardListener.onDeleteFlashCard(flashCard);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return flashCards.size();
    }

    public void removeFlashCard(FlashCard flashCard) {
        int position = flashCards.indexOf(flashCard);
        if (position != -1) {
            flashCards.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class FlashCardViewHolder extends RecyclerView.ViewHolder {
        TextView questionText, answerText;

        public FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.text_question);
            answerText = itemView.findViewById(R.id.text_answer_preview);
        }
    }
}
