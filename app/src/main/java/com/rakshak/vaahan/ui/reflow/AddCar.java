package com.rakshak.vaahan.ui.reflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rakshak.vaahan.databinding.AddCarBinding;

public class AddCar extends Fragment {

    private AddCarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = AddCarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textReflow;
//        reflowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}