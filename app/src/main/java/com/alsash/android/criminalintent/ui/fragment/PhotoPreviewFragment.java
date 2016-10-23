package com.alsash.android.criminalintent.ui.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alsash.android.criminalintent.R;
import com.alsash.android.criminalintent.utils.PictureUtils;

import java.io.File;

public class PhotoPreviewFragment extends DialogFragment {

    private static final String ARG_FILE = "file";

    private File mPhotoFile;
    private ImageView mImage;

    public static PhotoPreviewFragment newInstance(File file) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_FILE, file);

        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mPhotoFile = (File) getArguments().getSerializable(ARG_FILE);
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            return super.onCreateDialog(savedInstanceState);
        }

        View rootView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_photo_preview, null);

        mImage = (ImageView) rootView.findViewById(R.id.dialog_photo_preview);
        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
        mImage.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }
}
