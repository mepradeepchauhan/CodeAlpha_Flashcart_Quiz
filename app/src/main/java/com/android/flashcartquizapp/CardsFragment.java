package com.android.flashcartquizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.flashcartquizapp.databinding.FragmentCardsBinding;

import java.util.ArrayList;
import java.util.List;

public class CardsFragment extends Fragment {
    private FragmentCardsBinding binding;
    private FlashCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCardsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerCards.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(() -> {
            List<FlashCard> flashCards = FlashCardDatabase.getInstance(getContext()).flashCardDao().getAllFlashCards();
            requireActivity().runOnUiThread(() -> {
                adapter = new FlashCardAdapter(new ArrayList<>(flashCards), this::deleteFlashCard);
                binding.recyclerCards.setAdapter(adapter);
            });
        }).start();
    }

    private void deleteFlashCard(FlashCard flashCard) {
        new Thread(() -> {
            FlashCardDatabase.getInstance(getContext()).flashCardDao().deleteFlashCard(flashCard);

            requireActivity().runOnUiThread(() -> {
                adapter.removeFlashCard(flashCard);
                Toast.makeText(getContext(), "Card deleted!", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
