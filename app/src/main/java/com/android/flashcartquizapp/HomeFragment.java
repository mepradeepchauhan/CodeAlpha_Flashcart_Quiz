package com.android.flashcartquizapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.flashcartquizapp.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private List<FlashCard> flashCards;
    private int currentIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adjustCameraDistance();

        new Thread(() -> {
            flashCards = FlashCardDatabase.getInstance(getContext()).flashCardDao().getAllFlashCards();
            requireActivity().runOnUiThread(() -> {
                if (flashCards != null && !flashCards.isEmpty()) {
                    displayFlashCard(0);
                } else {
                    Toast.makeText(getContext(), "No flashcards available!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

        binding.flashcardContainer.setOnClickListener(v -> flipCard());

        binding.buttonPrevious.setOnClickListener(v -> navigateToPreviousCard());
        binding.buttonNext.setOnClickListener(v -> navigateToNextCard());
    }

    private void adjustCameraDistance() {
        float scale = getResources().getDisplayMetrics().density;
        binding.flashcardContainer.setCameraDistance(8000 * scale);
    }

    private void flipCard() {
        if (flashCards == null || flashCards.isEmpty()) return;
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(binding.flashcardContainer, "rotationY", 0f, 90f);
        flipOut.setDuration(300);

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (binding.textQuestion.getVisibility() == View.VISIBLE) {
                    binding.textQuestion.setVisibility(View.GONE);
                    binding.textAnswer.setVisibility(View.VISIBLE);
                } else {
                    binding.textAnswer.setVisibility(View.GONE);
                    binding.textQuestion.setVisibility(View.VISIBLE);
                }

                // Ensure the visible view is upright
                binding.textQuestion.setRotationY(0f);
                binding.textAnswer.setRotationY(0f);

                // Second half of the flip
                ObjectAnimator flipIn = ObjectAnimator.ofFloat(binding.flashcardContainer, "rotationY", -90f, 0f);
                flipIn.setDuration(300);
                flipIn.start();
            }
        });

        flipOut.start();
    }

    private void displayFlashCard(int index) {
        FlashCard flashCard = flashCards.get(index);
        binding.textQuestion.setText(flashCard.getQuestion());
        binding.textAnswer.setText(flashCard.getAnswer());
        binding.textQuestion.setVisibility(View.VISIBLE);
        binding.textAnswer.setVisibility(View.GONE);
        binding.textQuestion.setRotationY(0f); // Reset rotation
        binding.textAnswer.setRotationY(0f); // Reset rotation
    }

    private void navigateToPreviousCard() {
        if (flashCards != null && !flashCards.isEmpty()) {
            currentIndex = (currentIndex - 1 + flashCards.size()) % flashCards.size();
            displayFlashCard(currentIndex);
        }
    }

    private void navigateToNextCard() {
        if (flashCards != null && !flashCards.isEmpty()) {
            currentIndex = (currentIndex + 1) % flashCards.size();
            displayFlashCard(currentIndex);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
