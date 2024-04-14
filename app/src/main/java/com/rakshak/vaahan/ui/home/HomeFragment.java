package com.rakshak.vaahan.ui.home;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.rakshak.vaahan.MainActivity;
import com.rakshak.vaahan.R;
import com.rakshak.vaahan.data.DatabaseHelper;
import com.rakshak.vaahan.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    MediaPlayer mediaPlayer;
    SwitchCompat driveBtn;Spinner dropdown;TextView driveStatus;DatabaseHelper mydb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        driveBtn = binding.driveBtn;
        dropdown = binding.carListShow;
        driveStatus = binding.drvStatus;
        mydb = new DatabaseHelper(getContext());
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.accident_alert);


        updateCars();

        driveBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) getActivity()).highAlert();
                            mediaPlayer.start();
                        }
                    },5000);

                    driveStatus.setText(R.string.drStatusDr);
                }else {
                    driveStatus.setText("");
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updateCars(){
        List<List<String>> carList = mydb.getCarList();
        List<String> CarList = new ArrayList<>();
        for (int i = 0; i < carList.size(); i++) {
            String item = carList.get(i).get(0) + " " + carList.get(i).get(1) + " " + carList.get(i).get(2);
            CarList.add(item);
        }
        Log.d("List Data", CarList.toString());

        String[] items = CarList.toArray(new String[0]);
//        String[] items = new String[]{"1","2","three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }
}