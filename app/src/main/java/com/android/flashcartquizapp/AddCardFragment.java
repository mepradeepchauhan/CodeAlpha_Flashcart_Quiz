package com.android.flashcartquizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.flashcartquizapp.databinding.FragmentAddCardBinding;

public class AddCardFragment extends Fragment {
    private FragmentAddCardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentAddCardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveButton.setOnClickListener(v -> {
            String question = binding.editQuestion.getText().toString().trim();
            String answer = binding.editAnswer.getText().toString().trim();

            if (!question.isEmpty() && !answer.isEmpty()) {
                saveFlashCard(question, answer);
                binding.editQuestion.setText("");
                binding.editAnswer.setText("");
                Toast.makeText(getContext(), "Card saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please enter both question and answer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFlashCard(String question, String answer) {
        FlashCard flashCard = new FlashCard(question, answer);
        new Thread(() -> {
            FlashCardDatabase.getInstance(getContext()).flashCardDao().insertFlashCard(flashCard);
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
