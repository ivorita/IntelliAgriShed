package com.antelope.android.intelliagrished.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.adapter.RecAdapter;
import com.antelope.android.intelliagrished.db.Rec_item;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class noteFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private List<Rec_item> mItemList = new ArrayList<>();

    public static noteFragment newInstance() {
        noteFragment fragment = new noteFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        RecyclerView mRecView = view.findViewById(R.id.rec_view);

        mRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        for (int i = 0; i < 20; i++) {
            Rec_item item = new Rec_item("00:36", "2018年12月12日", "test");
            mItemList.add(item);
        }

        RecAdapter recAdapter = new RecAdapter(mItemList);

        mRecView.setAdapter(recAdapter);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        Toast.makeText(getContext(), "Fab Clicked", Toast.LENGTH_SHORT).show();
    }
}
