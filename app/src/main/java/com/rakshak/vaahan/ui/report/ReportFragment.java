package com.rakshak.vaahan.ui.report;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rakshak.vaahan.databinding.FragmentReportBinding;

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;
    ImageButton reportImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        reportImage = binding.reportImage;

        final TextView textView = binding.reportText;
//        reportViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        reportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 11);
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"),11);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @SuppressLint("Range")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case 11:
                    if (resultCode == Activity.RESULT_OK) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
//                        final Uri uri = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                        reportImage.setImageBitmap(photo);
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e("IMG Upload", "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("IMG Upload", "Exception in onActivityResult : " + e.getMessage());
        }
    }
}