package com.antelope.android.intelliagrished.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.antelope.android.intelliagrished.R;
import com.antelope.android.intelliagrished.db.Rec_item;
import com.antelope.android.intelliagrished.note.activity.NoteActivity;
import com.antelope.android.intelliagrished.note.activity.NotePagerActivity;
import com.antelope.android.intelliagrished.note.util.NoteBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.startActivity;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private List<NoteBean> mItemList = new ArrayList<>();
    private static Context mContext;

    public RecAdapter(List<NoteBean> itemList, Context context) {
        mItemList = itemList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(), "你点击第" + (position + 1) + "项", Toast.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteBean noteBean = mItemList.get(position);
        holder.bind(noteBean);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        @BindView(R.id.item_time)
        TextView mTime;
        @BindView(R.id.item_date)
        TextView mDate;
        @BindView(R.id.title)
        TextView mTitle;

        private NoteBean mNoteBean;

        ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, view);
        }

        public void bind(NoteBean noteBean) {
            mNoteBean = noteBean;
            mTitle.setText(mNoteBean.getTitle());
        }

        @Override
        public void onClick(View view) {
            Intent intent = NotePagerActivity.newIntent(mContext,mNoteBean.getId());
            startActivity(mContext,intent,new Bundle());
        }
    }
}
