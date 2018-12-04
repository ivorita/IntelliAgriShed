package com.antelope.android.intelliagrished.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antelope.android.intelliagrished.R;

public class envFragment extends Fragment {

    public static envFragment newInstance() {
        envFragment fragment = new envFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_envir, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
