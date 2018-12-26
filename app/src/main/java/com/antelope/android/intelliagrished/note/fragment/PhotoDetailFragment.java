package com.antelope.android.intelliagrished.note.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.note.util.PictureUtils;

import java.io.File;

/**
 * Created by ivorita on 2018/7/26.
 */

public class PhotoDetailFragment extends DialogFragment {
    private static final String ARG_FILE = "file";
    private ImageView mPhotoView;

    public static PhotoDetailFragment newInstance(File file){
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILE,file);

        PhotoDetailFragment fragment = new PhotoDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        File file = (File)getArguments().getSerializable(ARG_FILE);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo,null);

        mPhotoView = v.findViewById(R.id.note_photo_detail);

        Bitmap bitmap = PictureUtils.getScaledBitmap(file.getPath(),getActivity());

        mPhotoView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("图片详情")
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }
}
